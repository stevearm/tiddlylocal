package com.horsefire.tiddly.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiMorpher {

	private static final Logger LOG = LoggerFactory
			.getLogger(WikiMorpher.class);

	private final String m_saveUrl;
	private final String m_jsPath;

	public WikiMorpher(String saveUrl, String jsPath) {
		m_saveUrl = saveUrl;
		m_jsPath = jsPath;
	}

	private static String getToEndOf(String base, String searchFor) {
		int index = base.indexOf(searchFor);
		if (index == -1) {
			LOG.error("Could not find '{}' in base string", searchFor);
			throw new IndexOutOfBoundsException();
		}
		return base.substring(0, index + searchFor.length());
	}

	private static String getFromStartOf(String base, String searchFor) {
		int index = base.indexOf(searchFor);
		if (index == -1) {
			LOG.error("Could not find '{}' in base string", searchFor);
			throw new IndexOutOfBoundsException();
		}
		return base.substring(index, base.length() - 1);
	}

	public StringBuilder prepareToServe(String original) {
		StringBuilder result = new StringBuilder();
		result.append(getToEndOf(original, "<!--POST-SCRIPT-START-->"));
		result.append("<script type=\"text/javascript\" src=\"" + m_jsPath
				+ "\"></script>");
		result.append("<script type=\"text/javascript\">var tiddlybox_post_url = '"
				+ m_saveUrl + "'</script>");
		result.append(getFromStartOf(original, "<!--POST-SCRIPT-END-->"));
		return result;
	}

	public String prepareToSave(String original, String newStore) {
		StringBuilder result = new StringBuilder();
		result.append(getToEndOf(original, "<!--POST-SHADOWAREA-->")).append(
				'\n');
		result.append("<div id=\"storeArea\">").append('\n');
		result.append(newStore);
		result.append("</div>").append('\n');
		result.append(getFromStartOf(original, "<!--POST-STOREAREA-->"));
		return result.toString();
	}
}
