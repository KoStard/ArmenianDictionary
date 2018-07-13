package com.armeniandictionary;

import java.util.regex.Pattern;

public class TextVariations {
	public static Pattern RussianPattern = Pattern.compile("[а-яёА-ЯЁ0-9()+~!\"';:,.\\/\\\\<>~ -]");
	public static Pattern ArmenianPattern = Pattern.compile("[ա-ևԱ-Ֆ0-9()+~!\"';:,.\\/\\\\<>՝՜~ -]");
	public static String standartize(String text) {
		if (text == null || text.length() == 0) return text; 
		return text.replaceAll("․", ".").replaceAll("(^\\s*[,.+;'-]*)|([,.+;'-]*\\s*$)()", "").replaceAll(" {2,}", " ").replaceAll("\\s{2,}", "\n");
	}
}
