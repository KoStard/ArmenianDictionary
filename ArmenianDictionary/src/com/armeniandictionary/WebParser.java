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
//		Scanner scanner = new Scanner(System.in);
//		String word = "";
//		while (true) {
//			System.out.print("Ներմուծեք բառ - ");
//			word = scanner.nextLine();
//			if (word.length()==0 || word=="stop" || word=="Stop") {
//				log("Շնորհակալություն ծրագրից օգտվելու համար։");
//				scanner.close();
//				System.exit(0);
//			}
//			search(word);
//		}
		search("բարդ");
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
		Elements els = doc.getElementsByClass("word-body"); // բացատրություն
		
		Word word = new Word(els);
		
//		Element element = els.get(0);
//		log(element.getElementsByClass("word-h1").get(0).text().split(" - ")[1]);
//		log(element.getElementsByClass("word-content").get(0).text());
//		log(new String(new char[wrapLength]).replace("\0", "-"));
//
//		element = els.get(1); // հոմանիշներ
//		log(element.getElementsByClass("word-h1").get(0).text().split(" - ")[1]);
//		String content = element.getElementsByClass("word-content").get(0).text(); 
//		if (content.length() > 0) {
//			content = content.substring(3);
//		}
//		log(content);
//		log(new String(new char[wrapLength]).replace("\0", "-"));
//		
//		element = els.get(2); // ռուսերեն
//		log(element.getElementsByClass("word-h1").get(0).text().split(" - ")[1]);
//		content = element.getElementsByClass("word-content").get(0).text(); 
//		if (content.length() > 0) {
//			content = content.substring(2).replace("\n", "");
//			String[] parts = content.split(" [◊♢] ");
//			if (parts.length > 1) {
//				parts[1] = parts[1].replace(". ", ".\n");
//				content = String.join("\n-----\n", parts);
//			}
//		}
//		log(content);
//		log(new String(new char[wrapLength]).replace("\0", "-"));
//
//		element = els.get(3); // անգլերեն
//		log(element.getElementsByClass("word-h1").get(0).text().split(" - ")[1]);
//		log(element.getElementsByClass("word-content").get(0).text());
//		log(new String(new char[wrapLength]).replace("\0", "-"));
		
	}
	
	public static void main(String[] args) {
		new WebParser();
//		new ContentTree("1)\n" + 
//				"\n" + 
//				"[ածական]\n" + 
//				"1. Դիզված, կուտակված: Բարդ ամպեր:\n" + 
//				"2. Շատ մասերից կամ տարրերից բաղկացած: \n" + 
//				"3. Դժվարին, խրթին, դժվարալույծ: Բարդ հարց՝ խնդիր: \n" + 
//				"4. Խճճված, խառնակ: Բարդ գործ: \n" + 
//				"5. Դժվար, ծանր: Բարդ աշխատանք:\n" + 
//				"\n" + 
//				"2)\n" + 
//				"\n" + 
//				"բարդի, [գոյական] \n" + 
//				"1. Չորացրած խոտի՝ դեզ բաղկացած 30-40 խուրձից (ըստ տարբեր տեղերի): \n" + 
//				"2. Մի երեսնյակ: Բարդ խոտ( = 30 խուրձ): \n" + 
//				"3. (գավառական) Վարս, ծամ: Ախ, էնոր բաժ խորոտ էր, քանց բաժն իմ բարդի (Ս. Տարոնցի):");
	}
}