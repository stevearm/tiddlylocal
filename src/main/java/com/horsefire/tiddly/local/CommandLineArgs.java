package com.horsefire.tiddly.local;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class CommandLineArgs {

	@Parameter(names = { "--help" }, description = "Display help")
	private boolean help;

	@Parameter(names = { "--port" }, description = "Http port")
	private int port = 9801;

	@Parameter(names = { "--logfile" }, description = "Logback config file")
	private String logback;

	@Parameter(description = "/path/to/wiki.html")
	private final List<String> args = Lists.newArrayList();

	public boolean isHelp() {
		return help;
	}

	public int getPort() {
		return port;
	}

	public String getLogback() {
		return logback;
	}

	public String getFile() {
		if (args.size() > 0) {
			return args.get(0);
		}
		return null;
	}
}
