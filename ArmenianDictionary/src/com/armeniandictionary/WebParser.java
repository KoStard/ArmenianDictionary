package com.armeniandictionary;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {
	public int wrapLength = 100;
	public Pattern pattern = Pattern.compile("(?<=[^\\n\\d])(?=\\d+(\\.|\\)) )");

	public WebParser() {
		Scanner scanner = new Scanner(System.in);
		String word = "";
		while (true) {
			System.out.print("Փնտրել - ");
			word = scanner.nextLine();
			if (word.length()==0 || word=="stop" || word=="Stop") {
				log("Շնորհակալություն ծրագրից օգտվելու համար։");
				scanner.close();
				System.exit(0);
			}
			search(word);
		}
	}
	
	public String wrap(String str) {
		return wrap(str, wrapLength);
	}
	
	public String wrap(String str, int wrapLength) {
		String[] res = str.split("\n");
		for (int j = 0; j < res.length; j++) {
			StringBuilder sb = new StringBuilder(res[j]);
			int i = 0;
			while (i + wrapLength < sb.length() && ((i = sb.lastIndexOf(" ", i + wrapLength)) != -1 || ((i = sb.indexOf(" ")) != -1))) {
				sb.replace(i, i + 1, "\n");
			}
			res[j] = sb.toString();
		}
		return String.join("\n", res);
	}
	
	public void log(String str) {
		log(str, wrapLength);
	}
	
	public void log(String str, int wrapLength) {
		str = pattern.matcher(str).replaceAll("\n");
		System.out.println(wrap(str));
	}
	
	public Document parse(String http) {
		try {
			return Jsoup.connect(http).get();
		} catch (IOException e) {
			System.out.println("Invalid word.");
			return null;
		}
	}
	
	public void search(String word) {
		represent(parse(String.format("https://bararanonline.com/%s", word)));
	}
	
	public void represent(Document doc) {
		if (doc == null) return;
		Elements els = doc.getElementsByClass("word-body");
		if (els.size() == 0) {
			System.out.println("Ոչինչ չի գտնվել։");
			return;
		}
		Word word = new Word(els);
		System.out.println(word.getContent());
	}
	
	public static void main(String[] args) {
		new WebParser();
	}
}