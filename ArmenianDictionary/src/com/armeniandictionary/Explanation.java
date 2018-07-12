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

}
class ExplanationContentTree {
	public String[] types;
	public BigBlock bigBlock;
	public ExplanationContentTree(String text) {
		bigBlock = new BigBlock(text);
	}
	public String getContent() {
		return bigBlock.getContent();
	}
}

class BigBlock{
	public MiddleBlock[] middleBlocks;
	public BigBlock(String text) {
		String[] rawBlocks = text.split("\\d+\\)\\s*");
		rawBlocks = Arrays.copyOfRange(rawBlocks, 1, rawBlocks.length);
		middleBlocks = new MiddleBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			middleBlocks[i] = new MiddleBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = "";
		for (MiddleBlock mb : middleBlocks) {
			res += mb.getContent();
		}
		res+="\n";
		return res;
	}
}

class MiddleBlock {
	public String modification;
	public String type;
	public SmallBlock[] smallBlocks;
	public static Pattern pattern = Pattern.compile("^([\\s\\S]*)\\[([^\\]]*)\\]\\s*(\\d+\\.[\\s\\S]+)\\s*$");
	public MiddleBlock(String text) {
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		modification = matcher.group(1);
		type = matcher.group(2);
		String[] rawBlocks = matcher.group(3).replaceAll("(?<!^)\\s*\\d+\\.\\s*", "\n").replaceAll("\\s*\\d+\\.\\s*", "").split("\\n");
		smallBlocks = new SmallBlock[rawBlocks.length];
		for (int i = 0; i < rawBlocks.length; i++) {
			smallBlocks[i] = new SmallBlock(rawBlocks[i]);
		}
	}
	public String getContent() {
		String res = (modification.length() > 0 ? modification + " -- ":"") + type + "\n";
		for (SmallBlock smb : smallBlocks) {
			res += smb.getContent();
		}
		res += "\n";
		return res;
	}
}

class SmallBlock {
	public String res, expl;
	public static Pattern pattern = Pattern.compile("^\\s*([^:]+)\\s*:\\s*(?:([^:]+):)?\\s*$");
	public SmallBlock(String text) {
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		res = matcher.group(1);
		expl = matcher.group(2);
	}
	public String getContent() {
		return String.format(res + (expl!=null && expl.length()>0?" - " + expl:"")+"\n");
	}
}
