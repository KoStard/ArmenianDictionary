package com.armeniandictionary;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Synonyms extends TranslationSegment {
	private String title;
	private String body;
	private SynonymsContentTree tree;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
		tree = new SynonymsContentTree(body);
	}

	@Override
	public String getContent() {
		return String.format("##--[  %s%n%n%s", title, tree.getContent());
	}
	public SynonymsContentTree getTree() {return tree;}
}

class SynonymsContentTree extends ContentTree{
	public String[] types;
	public SynonymsBigBlock SynonymsBigBlock;
	public SynonymsContentTree(String text) {
		SynonymsBigBlock = new SynonymsBigBlock(text);
	}
	public String getContent() {
		return SynonymsBigBlock.getContent();
	}
}

class SynonymsBigBlock{
	public SynonymsMiddleBlock[] SynonymsMiddleBlocks;
	public SynonymsBigBlock(String text) {
		String[] rawBlocks = text.split("\\d+\\)\\s*");
		if (rawBlocks.length > 1)
			rawBlocks = Arrays.copyOfRange(rawBlocks, 1, rawBlocks.length);
		SynonymsMiddleBlocks = new SynonymsMiddleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			SynonymsMiddleBlocks[i] = new SynonymsMiddleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (SynonymsMiddleBlock mb : SynonymsMiddleBlocks) {
			res += mb.getContent();
		}
		return res;
	}
}

class SynonymsMiddleBlock {
	public String type;
	public SynonymsSmallBlock[] SynonymsSmallBlocks;
	public static Pattern pattern = Pattern.compile("^\\s*([ա-ևԱ-Ֆ]*\\.)?\\s*([\\s\\S]+)\\s*$");
	public SynonymsMiddleBlock(String text) {
		text = TextVariations.standartize(text);
		Matcher matcher = pattern.matcher(text);
		if (!matcher.find()) return;
		type = TextVariations.standartize(matcher.group(1));
		String[] rawBlocks = matcher.group(2).replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		SynonymsSmallBlocks = new SynonymsSmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			SynonymsSmallBlocks[i] = new SynonymsSmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = (type!=null?type + " - ":"");
		if (SynonymsSmallBlocks!=null)
		for (SynonymsSmallBlock smb : SynonymsSmallBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class SynonymsSmallBlock {
	public String[] ress;
	public SynonymsSmallBlock(String text) {
		ress = TextVariations.standartize(text).split("\\s*(,)\\s*");
		for (int i = 0; i<ress.length;i++) {
			ress[i] = TextVariations.standartize(ress[i]);
		}
	}
	public String getContent() {
		return String.join(", ", ress)+"\n";
	}
}
