package com.horsefire.tiddly.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FileServlet extends HttpServlet {

	private final File m_dir;

	public FileServlet(File dir) {
		m_dir = dir;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		File absolute = new File(m_dir, req.getPathInfo());
		if (absolute.isFile()) {
			OutputStream os = resp.getOutputStream();
			FileInputStream in = new FileInputStream(absolute);
			byte[] buffer = new byte[500];
			int len = in.read(buffer);
			while (len == buffer.length) {
				os.write(buffer);
				len = in.read(buffer);
			}
			os.write(buffer, 0, len);
		} else {
			throw new FileNotFoundException();
		}
	}
}
