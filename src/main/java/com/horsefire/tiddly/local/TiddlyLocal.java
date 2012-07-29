package com.horsefire.tiddly.local;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class TiddlyLocal {

	public static final String LOCAL_URL = "/local/";

	private static void displayHelp() {
		StringBuilder buffer = new StringBuilder();
		new JCommander(new CommandLineArgs()).usage(buffer);
		System.err.println(buffer.toString());
	}

	public static void main(String[] args) throws Exception {
		CommandLineArgs options = new CommandLineArgs();
		try {
			new JCommander(options, args);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			displayHelp();
			return;
		}

		if (options.isHelp()) {
			displayHelp();
			return;
		}

		final String logProperty = "logback.configurationFile";
		if (options.getLogback() == null) {
			System.setProperty(logProperty, "logback.xml");
		} else {
			System.setProperty(logProperty, options.getLogback());
		}

		File wikiFile;
		if (options.getFile() == null) {
			wikiFile = new File("wiki.html");
			System.out
					.println("No wiki file specified, so defaulting to wiki.html");
		} else {
			wikiFile = new File(options.getFile());
		}
		if (!wikiFile.isFile()) {
			System.err.println("File " + wikiFile + " must exist");
			return;
		}

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/");
		contextHandler.addServlet(new ServletHolder(new LocalServlet(wikiFile,
				new WikiMorpher(LOCAL_URL + "wiki.html", "/tiddlybox.js"))),
				LOCAL_URL + "wiki.html");
		contextHandler.addServlet(new ServletHolder(new JavascriptServlet()),
				"/tiddlybox.js");
		contextHandler.addServlet(
				new ServletHolder(new FileServlet(wikiFile.getParentFile())),
				LOCAL_URL + "*");

		Server server = new Server(options.getPort());
		server.setHandler(contextHandler);
		server.start();
		org.slf4j.LoggerFactory.getLogger(TiddlyLocal.class).info(
				"Serving file at http://localhost:{}/{}wiki.html",
				options.getPort(), LOCAL_URL);
		server.join();
	}
}
