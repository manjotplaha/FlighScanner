package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetDataOfFlights {

	public static Document request(String next_link2) throws Exception {
		ArrayList<String> linkList = new ArrayList<>();

		// Jsoup library for parsing HTML files
		Document doc = Jsoup.connect(next_link2).get();

		// Selenium webdriver initialization
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriver webDriver = new ChromeDriver(options);
		webDriver.get(next_link2);
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		String pageSource = webDriver.getPageSource();
		doc = Jsoup.parse(pageSource);

		List<WebElement> prices = webDriver.findElements(By.className("f8F1-price-text"));
		List<WebElement> airlinename = webDriver.findElements(By.className("J0g6-operator-text"));

		// Crawling links using Jsoup
		try {
			doc = Jsoup.connect(next_link2).get();
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				String s = link.attr("abs:href");
				String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				Pattern p1 = Pattern.compile(regex);
				Matcher m1 = p1.matcher(s);
				while (m1.find()) {
					linkList.add(m1.group(0));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Passed link for HTML to text file creation
		HTMLToText.DynamicHTMLtoTextConverter(linkList);

		System.out.println("-----------------------------------------------------");
		System.out.println("Flight details:");
		System.out.println();

		// Printing all listed flights details
		for (int i = 0; i < prices.size(); i++) {
			System.out.println("Airline name " + airlinename.get(i).getText() + " Price: " + prices.get(i).getText());
		}

		return doc;
	}
}