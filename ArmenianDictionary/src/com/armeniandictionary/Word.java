package com.armeniandictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

public class Word {
	public TranslationSegment explanation = new Explanation(), synonyms = new Synonyms(), russian = new Russian(), english = new English();
	
	public Word(Elements els) {
		explanation.initContent(els.get(0));
		System.out.println(explanation.getContent());
//		synonyms = new Synonyms(els[0]);
//		russian = new Russian(els[0]);
//		english = new English(els[0]);
	}
}
