package com.armeniandictionary;

import org.jsoup.nodes.Element;

public abstract class TranslationSegment {
	public String getTitle(Element element) {
		return element.getElementsByClass("word-arm").get(0).text().substring(2);
	}
	public String getBody(Element element) {
		return element.getElementsByClass("word-content").get(0).text();
	}
	public abstract void initContent(Element element);
	public abstract String getContent();
}
