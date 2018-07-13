package com.armeniandictionary;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class English extends TranslationSegment {
	private String title;
	private String body;
	private EnglishContentTree tree;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
		tree = new EnglishContentTree(body);
	}

	@Override
	public String getContent() {
		return String.format("##--[  %s%n%n%s", title, tree.getContent());
	}
	public EnglishContentTree getTree() {return tree;}

}

class EnglishContentTree extends ContentTree {
	public String[] types;
	public EnglishBigBlock EnglishBigBlock;
	public EnglishContentTree(String text) {
		EnglishBigBlock = new EnglishBigBlock(text);
	}
	public String getContent() {
		return EnglishBigBlock.getContent();
	}
}

class EnglishBigBlock{
	public EnglishMiddleBlock[] EnglishMiddleBlocks;
	public EnglishBigBlock(String text) {
		String[] rawBlocks = text.split("\\d+\\)\\s*");
		if (rawBlocks.length > 1)
			rawBlocks = Arrays.copyOfRange(rawBlocks, 1, rawBlocks.length);
		EnglishMiddleBlocks = new EnglishMiddleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			EnglishMiddleBlocks[i] = new EnglishMiddleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (EnglishMiddleBlock mb : EnglishMiddleBlocks) {
			res += mb.getContent();
		}
		return res;
	}
}

class EnglishMiddleBlock {
	public EnglishSmallBlock[] EnglishSmallBlocks;
	public EnglishMiddleBlock(String text) {
		text = TextVariations.standartize(text);
		String[] rawBlocks = text.replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		EnglishSmallBlocks = new EnglishSmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			EnglishSmallBlocks[i] = new EnglishSmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (EnglishSmallBlock smb : EnglishSmallBlocks) {
			res += smb.getContent();
		}
		return res;
	}
}

class EnglishSmallBlock {
	public String[] ress;
	public EnglishSmallBlock(String text) {
		ress = TextVariations.standartize(text).split("\\s*,\\s*");
	}
	public String getContent() {
		return String.join(", ", ress)+"\n";
	}
}
