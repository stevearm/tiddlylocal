package com.horsefire.tiddly.local;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class TiddlyLocal {

	public static final String LOCAL_URL = "/local/";

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Must specify a path to a file");
			return;
		}
		File file = new File(args[0]);
		if (!file.isFile()) {
			System.err.println("File " + file + " must exist");
			return;
		}

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/");
		contextHandler.addServlet(new ServletHolder(new LocalServlet(file,
				new WikiMorpher(LOCAL_URL + "wiki.html", "/tiddlybox.js"))),
				LOCAL_URL + "wiki.html");
		contextHandler.addServlet(new ServletHolder(new JavascriptServlet()),
				"/tiddlybox.js");
		contextHandler.addServlet(
				new ServletHolder(new FileServlet(file.getParentFile())),
				LOCAL_URL + "*");

		Server server = new Server(9801);
		server.setHandler(contextHandler);
		server.start();
		server.join();
	}
}
