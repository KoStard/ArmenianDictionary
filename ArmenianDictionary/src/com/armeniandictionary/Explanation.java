package com.armeniandictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Explanation extends TranslationSegment {
	private String title;
	private String body;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
	}

	@Override
	public String getContent() {
		return null;
	}

}
class ExplanationContentTree {
	Pattern bigBlockPattern = Pattern.compile("\\d+\\)[^\\d]+"),
			bigBlockHeader = Pattern.compile("(?<=(\\d+\\)|^))[^\\[]]"),
			squareBracketsContentPattern = Pattern.compile("\\[[^\\[\\]]+\\]"),
			middleBlockContainerPattern = Pattern.compile("\\][^\\d\\)]$"),
			middleBlockPattern = Pattern.compile("\\d+\\.[^\\d\\n]"),
			middleBlockPartsPattern = Pattern.compile("([^:]+)(:|$)");
	public ExplanationContentTree(String text) {
		Matcher matcher = bigBlockPattern.matcher(text);
	}
}
