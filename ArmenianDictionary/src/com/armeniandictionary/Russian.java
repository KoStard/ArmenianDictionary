package com.armeniandictionary;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Russian extends TranslationSegment {
	private String title;
	private String body;
	private RussianContentTree tree;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
		tree = new RussianContentTree(body);
	}

	@Override
	public String getContent() {
		return String.format("##--[  %s%n%n%s", title, tree.getContent());
	}
	public RussianContentTree getTree() {return tree;}

}

class RussianContentTree extends ContentTree {
	public String[] types;
	public RussianBigBlock RussianBigBlock;
	public RussianContentTree(String text) {
		RussianBigBlock = new RussianBigBlock(text);
	}
	public String getContent() {
		return RussianBigBlock.getContent();
	}
}

class RussianBigBlock{
	public RussianMiddleBlock russianMiddleBlock;
	public RussianMiddleExampleBlock russianMiddleExampleBlock;
	public RussianBigBlock(String text) { // Multiple types... One contains [◊♢], one only normal middle blocks, one their mixture
		try {
			String[] rawBlocks = text.split("\\s*[◊♢]\\s*");
			if (rawBlocks.length == 2) {
				russianMiddleBlock = new RussianMiddleBlock(rawBlocks[0]);
				russianMiddleExampleBlock = new RussianMiddleExampleBlock(rawBlocks[1]);
			} else if (rawBlocks.length == 1) {
				russianMiddleExampleBlock = new RussianMiddleExampleBlock(rawBlocks[0]); // Priority to examples - ներմուծել
			}
		} catch (Exception e) {
			russianMiddleBlock = new RussianMiddleBlock(text);
		}
	}
	public String getContent() {
		return (russianMiddleBlock!=null?russianMiddleBlock.getContent():"")+(russianMiddleExampleBlock!=null?russianMiddleExampleBlock.getContent():"");
	}
}

class RussianMiddleBlock {
	public RussianSmallBlock[] RussianSmallBlocks;
	public RussianMiddleBlock(String text) {
		text = TextVariations.standartize(text);
		String[] rawBlocks = text.replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		RussianSmallBlocks = new RussianSmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			RussianSmallBlocks[i] = new RussianSmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (RussianSmallBlock smb : RussianSmallBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class RussianMiddleExampleBlock {
	public RussianSmallExampleBlock[] russianSmalExamplelBlocks;
	public RussianMiddleExampleBlock(String text) {
		text = TextVariations.standartize(text);
		String[] rawBlocks = text.split("(?<="+TextVariations.RussianPattern+"\\. )(?="+TextVariations.ArmenianPattern+")");
		russianSmalExamplelBlocks = new RussianSmallExampleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			russianSmalExamplelBlocks[i] = new RussianSmallExampleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (RussianSmallExampleBlock smb : russianSmalExamplelBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class RussianSmallBlock {
	public String type;
	public String notes;
	public String res = "";
	public Pattern pattern = Pattern.compile("([ա-և]+\\.)?\\s*(?:\\(([ա-և.-]+)\\))?\\s*("+TextVariations.RussianPattern+"+)");
	public RussianSmallBlock(String text) {
		Matcher matcher = pattern.matcher(TextVariations.standartize(text));
		if (!matcher.find()) return;
		type = matcher.group(1);
		notes = matcher.group(2);
		res = matcher.group(3);
	}
	public String getContent() {
		return (type!=null?type+" ":"")+(notes!=null?notes+" ":"")+res+"\n";
	}
}

class RussianSmallExampleBlock {
	public String arm, rus;
	public Pattern pattern = Pattern.compile("\\s*("+TextVariations.ArmenianPattern+"+)\\s*("+TextVariations.RussianPattern+"+)\\s*");
	public RussianSmallExampleBlock(String text) {
		Matcher matcher = pattern.matcher(TextVariations.standartize(text));
		matcher.find();
		arm = TextVariations.standartize(matcher.group(1));
		rus = TextVariations.standartize(matcher.group(2));
	}
	public String getContent() {
		return arm+" - "+rus+"\n";
	}
}
