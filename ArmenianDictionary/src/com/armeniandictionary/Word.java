package com.armeniandictionary;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Word {
	public TranslationSegment explanation = new Explanation(), 
			synonyms = new Synonyms(),
			antonyms = new Antonyms(),
			russian = new Russian(), 
			english = new English();
	
	public Word(Elements els) {
		for (Element el : els) {
		switch(TranslationSegment.getTitle(el)) {
			case "բացատրություն":
				explanation.initContent(el);
				break;
			case "հոմանիշներ":
				synonyms.initContent(el);
				break;
			case "հականիշներ":
				antonyms.initContent(el);
				break;
			case "ռուսերեն թարգմանություն":
				russian.initContent(el);
				break;
			case "անգլերեն թարգմանություն":
				english.initContent(el);
				break;
			default:
				System.out.println("Invalid title ["+TranslationSegment.getTitle(el)+"]");
			}
		}
	}
	
	public String getContent() {
		return (explanation.getTree()!=null?explanation.getContent():"")+
				(synonyms.getTree()!=null?synonyms.getContent():"")+
				(antonyms.getTree()!=null?antonyms.getContent():"")+
				(russian.getTree()!=null?russian.getContent():"")+
				(english.getTree()!=null?english.getContent():"");
	}
}
