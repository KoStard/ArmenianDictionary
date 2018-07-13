package com.armeniandictionary;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Antonyms extends TranslationSegment {
	private String title;
	private String body;
	private AntonymsContentTree tree;
	@Override
	public void initContent(Element element) {
		title = getTitle(element);
		body = getBody(element);
		tree = new AntonymsContentTree(body);
	}

	@Override
	public String getContent() {
		return String.format("##--[  %s%n%n%s", title, tree.getContent());
	}
	
	public AntonymsContentTree getTree() {return tree;}
}

class AntonymsContentTree extends ContentTree {
	public String[] types;
	public AntonymsBigBlock AntonymsBigBlock;
	public AntonymsContentTree(String text) {
		AntonymsBigBlock = new AntonymsBigBlock(text);
	}
	public String getContent() {
		return AntonymsBigBlock.getContent();
	}
}

class AntonymsBigBlock{
	public AntonymsMiddleBlock[] AntonymsMiddleBlocks;
	public AntonymsBigBlock(String text) {
		String[] rawBlocks = text.split("\\d+\\)\\s*");
		if (rawBlocks.length>1)
			rawBlocks = Arrays.copyOfRange(rawBlocks, 1, rawBlocks.length);
		AntonymsMiddleBlocks = new AntonymsMiddleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			AntonymsMiddleBlocks[i] = new AntonymsMiddleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (AntonymsMiddleBlock mb : AntonymsMiddleBlocks) {
			res += mb.getContent();
		}
		return res;
	}
}

class AntonymsMiddleBlock {
	public String type;
	public AntonymsSmallBlock[] AntonymsSmallBlocks;
	public static Pattern pattern = Pattern.compile("^\\s*([ա-ևԱ-Ֆ]*\\.)?\\s*([\\s\\S]+)\\s*$");
	public AntonymsMiddleBlock(String text) {
		text = TextVariations.standartize(text);
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		type = TextVariations.standartize(matcher.group(1));
		String[] rawBlocks = matcher.group(2).replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		AntonymsSmallBlocks = new AntonymsSmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			AntonymsSmallBlocks[i] = new AntonymsSmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = (type!=null && type.length()>0?type + " - ":"");
		for (AntonymsSmallBlock smb : AntonymsSmallBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class AntonymsSmallBlock {
	public String[] ress;
	public AntonymsSmallBlock(String text) {
		ress = TextVariations.standartize(text).split("\\s*(,)\\s*");
		for (int i = 0; i<ress.length;i++) {
			ress[i] = TextVariations.standartize(ress[i]);
		}
	}
	public String getContent() {
		return String.join(", ", ress)+"\n";
	}
}
