package com.armeniandictionary;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Explanation extends TranslationSegment {
	private String title;
	private String body;
	private ExplanationContentTree tree;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
		tree = new ExplanationContentTree(body);
	}

	@Override
	public String getContent() {
		return String.format("##--[  %s%n%n%s", title, tree.getContent());
	}
	public ExplanationContentTree getTree() {return tree;}
}

class ExplanationContentTree extends ContentTree {
	public String[] types;
	public ExplanationBigBlock ExplanationBigBlock;
	public ExplanationContentTree(String text) {
		ExplanationBigBlock = new ExplanationBigBlock(text);
	}
	public String getContent() {
		return ExplanationBigBlock.getContent();
	}
}

class ExplanationBigBlock{
	public ExplanationMiddleBlock[] ExplanationMiddleBlocks;
	public ExplanationBigBlock(String text) {
		String[] rawBlocks = text.split("(?<!\\()\\d+\\)\\s*");
		if (rawBlocks.length > 1)
			rawBlocks = Arrays.copyOfRange(rawBlocks, 1, rawBlocks.length);
		ExplanationMiddleBlocks = new ExplanationMiddleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			ExplanationMiddleBlocks[i] = new ExplanationMiddleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (ExplanationMiddleBlock mb : ExplanationMiddleBlocks) {
			res += mb.getContent();
		}
		return res;
	}
}

class ExplanationMiddleBlock {
	public String modification;
	public String type;
	public ExplanationSmallBlock[] ExplanationSmallBlocks;
	public static Pattern pattern = Pattern.compile("^([\\s\\S]*)\\[([^\\]]*)\\]\\s*([\\s\\S]+)\\s*$");
	public ExplanationMiddleBlock(String text) {
		text = TextVariations.standartize(text);
		Matcher matcher = pattern.matcher(text);
		if (!matcher.find()) return;
		modification = TextVariations.standartize(matcher.group(1));
		type = TextVariations.standartize(matcher.group(2));
		String[] rawBlocks = matcher.group(3).replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		ExplanationSmallBlocks = new ExplanationSmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			ExplanationSmallBlocks[i] = new ExplanationSmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = (modification.length() > 0 ? modification + " -- ":"") + type + "\n";
		if (ExplanationSmallBlocks != null)
		for (ExplanationSmallBlock smb : ExplanationSmallBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class ExplanationSmallBlock {
	public String res, expl;
	public static Pattern pattern = Pattern.compile("\\s*(?:\\(([^)]+)\\)\\s*:)?([^:]+)\\s*:\\s*(?:([^:]+):)?\\s*");
	public ExplanationSmallBlock(String text) {
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		res = TextVariations.standartize(matcher.group(2));
		expl = TextVariations.standartize(matcher.group(3));
	}
	public String getContent() {
		return res + (expl!=null && expl.length()>0?" - " + expl:"")+"\n";
	}
}
