package com.horsefire.tiddly.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class JavascriptServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		OutputStream os = resp.getOutputStream();
		InputStream in = getClass().getResourceAsStream("tiddlybox.js");
		byte[] buffer = new byte[500];
		int len = in.read(buffer);
		while (len == buffer.length) {
			os.write(buffer);
			len = in.read(buffer);
		}
		os.write(buffer, 0, len);
	}
}
