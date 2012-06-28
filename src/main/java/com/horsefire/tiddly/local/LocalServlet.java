package com.horsefire.tiddly.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class LocalServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(LocalServlet.class);

	private static String extractString(InputStream in2) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(in2));

		StringBuilder wikiContents = new StringBuilder();
		String line = in.readLine();
		while (line != null) {
			wikiContents.append(line).append('\n');
			line = in.readLine();
		}
		in.close();
		return wikiContents.toString();
	}

	private String getUpdatedWikiContents() throws IOException {
		FileInputStream in = new FileInputStream(m_path);
		try {
			return extractString(in);
		} finally {
			in.close();
		}
	}

	private final File m_path;
	private final WikiMorpher m_morpher;

	@Inject
	public LocalServlet(File path, WikiMorpher morpher) {
		m_path = path;
		m_morpher = morpher;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		if (m_path == null) {
			throw new IOException("No file path");
		}
		PrintWriter out = resp.getWriter();
		final String contents = getUpdatedWikiContents();
		out.println(m_morpher.prepareToServe(contents));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (m_path == null) {
			throw new RuntimeException("No path");
		}
		final PrintWriter out = resp.getWriter();
		try {

			final String store = extractString(req.getInputStream());

			String newFile = m_morpher.prepareToSave(getUpdatedWikiContents(),
					store);
			out.println(newFile);
			FileOutputStream fileOut = new FileOutputStream(m_path);
			try {
				fileOut.write(newFile.getBytes());
			} finally {
				out.close();
			}
		} catch (IOException e) {
			postError(out, e);
		}
	}

	private void postError(PrintWriter out, Exception e) throws IOException {
		out.print("{\"success\":false,\"message\":\"Error during save. See server logs\"}");
		LOG.error("Error during save", e);
	}
}
