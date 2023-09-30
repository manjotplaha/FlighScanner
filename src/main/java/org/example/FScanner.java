package org.example;

import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FScanner {
	private static Scanner myObj;
	private static Scanner sc;

	public static void main(String[] args) throws Exception {

		File frequencyfile = new File("./src/search_frequency.txt");

		// Deleting all generated .html and .txt files
		StringFileManipulation.DeleteMultipleFiles(CommonConstants.webPagesPath + "webPage\\", ".html");
		StringFileManipulation.DeleteMultipleFiles(CommonConstants.webPagesPath + "textFile\\", ".txt");

		// Checking search_frequency file exists
		if (frequencyfile.exists()) {

			List<String> words = new ArrayList<String>();

			Scanner myReader = new Scanner(frequencyfile);
			while (myReader.hasNextLine()) {
				words.add(myReader.nextLine());
			}
			myReader.close();

			// Calculating occurence of each words in the search_frequency text file
			Map<String, Integer> wordfreq = new HashMap<>();
			for (String word : words) {
				Integer integer = wordfreq.get(word);
				if (integer == null)
					wordfreq.put(word, 1);
				else {
					wordfreq.put(word, integer + 1);
				}
			}

			System.out.println("This Year's Most Searched Destinations:");

			for (String key : wordfreq.keySet()) {
				System.out.println(key.toUpperCase() + " " + wordfreq.get(key) + " Times");
			}

			System.out.println("-----------------------------------------------------");
		} else {
			frequencyfile.createNewFile();
		}

		myObj = new Scanner(System.in);

		// Reading the data from the Airports.csv file and storing it into data variable
		File airports = new File("Airports.csv");
		Scanner myReader = new Scanner(airports);
		String data = " ";
		while (myReader.hasNextLine()) {
			data = data + myReader.nextLine();

		}

		// Spliting data by ";" and "," and storing it into citiesCode hashmap as key
		// value pairs
		String[] airport = data.split(";");
		Map<String, String> citiesCodes = new HashMap<String, String>();
		for (int i = 0; i < airport.length; i++) {
			String[] airportData = airport[i].split(",");
			citiesCodes.put(airportData[1], airportData[0]);
		}

		// Creating cities list from citiesCode hashmap
		ArrayList<String> cities = new ArrayList<>();
		int index = 1;
		for (String e : citiesCodes.keySet()) {
			cities.add(e);
		}

		// Displaying list of cities with an index
		System.out.println(
				"Please pick one of the following options for source and destination: ");
		for (String e : cities) {
			System.out.println(index + ". " + e);
			index++;
		}

		System.out.println("-----------------------------------------------------");

		// Obtaining the name of the source city from the user
		System.out.println("Enter name of source city");
		String src = myObj.nextLine();

		// Validation for city name
		while (!src.matches("[a-zA-Z_]+")) {
			System.out.println("Invalid city");
			System.out.println("Please enter correct name of source city");
			src = myObj.nextLine();
		}

		src = src.toLowerCase();
		String fileword = src + "\n";
		src = StringFileManipulation.capitalize(src).trim();
		String source = citiesCodes.get(src);

		if (source == null) {
			source = SuggestedWords.wordCheck(src);
			if (source == null || !citiesCodes.values().contains(source)) {
				System.out.println("Invalid city name");
				System.exit(0);
			} else {
				fileword = source + "\n";
				System.out.println(fileword);
				source = StringFileManipulation.capitalize(source).trim();
				source = citiesCodes.get(source).trim();
			}
		}

		// Adding source city name into search_frequency text file
		Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);

		// Implemented Djikstra algorithm for source city
		NearestToSource.djikstra(src);

		System.out.println("-----------------------------------------------------");

		// Obtaining the name of the destination city from the user
		System.out.println("Enter name of destination city");
		String dest = myObj.nextLine();

		// Validation for city name
		while (!dest.matches("[a-zA-Z_]+")) {
			System.out.println("Invalid city");
			System.out.println("Please enter correct name of destination city");
			dest = myObj.nextLine();
		}

		dest = dest.toLowerCase();
		fileword = dest + "\n";
		dest = StringFileManipulation.capitalize(dest).trim();
		String destination = citiesCodes.get(dest);

		if (destination == null) {
			destination = SuggestedWords.wordCheck(src);
			if (destination == null || !(citiesCodes.values().contains(destination))) {
				System.out.println("Invalid city name");
				System.exit(0);
			} else {
				fileword = destination + "\n";
				System.out.println(fileword);
				destination = StringFileManipulation.capitalize(destination).trim();
				destination = citiesCodes.get(destination).trim();
			}

		}

		// Adding destination city name into search_frequency text file
		Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);

		System.out.println("-----------------------------------------------------");

		// Obtaining the date of departure from the user in yyyymmdd format
		System.out.println("Enter the departure date in the format : yyyymmdd");
		String departureDate = myObj.nextLine();

		// Date validation
		while (!(DateValidation.isDateValid(departureDate) && Integer.parseInt(departureDate) > Integer
				.parseInt(java.time.LocalDate.now().toString().replaceAll("-", "")))) {
			System.out.println("Please enter the Correct departure date");
			departureDate = myObj.nextLine();
		}

		System.out.println("-----------------------------------------------------");

		// Getting information from the user on the total number of passengers
		System.out.println("Enter the number of passengers");
		String count = myObj.nextLine();

		// Count validation
		while (Integer.parseInt(count) <= 0) {
			System.out.println("Please enter valid number of passengers");
			count = myObj.nextLine();
		}

		// Url for cheapflights website with all the parameters
		String url = "https://www.cheapflights.ca/flight-search/" + source.trim() + "-" + destination.trim() + "/"
				+ DateValidation.dateFormatterf(departureDate) + "/" + count + "adults?sort=bestflight_a";

		System.out.println("-----------------------------------------------------");

		// url crawling
		Document document = GetDataOfFlights.request(url);

		// Creating text files from the html files
		document.text();
		String newTitle = source + destination;
		BufferedWriter writer = new BufferedWriter(new FileWriter(CommonConstants.webPagesPath + newTitle + ".txt"));
		writer.write(document.text().toLowerCase());
		writer.close();

		// Displaying each word's overall frequency in ascending order
		System.out.println("\n\n======================== Frequency Count ========================\n\n");
		frequencyCounter(newTitle);

		// Displaying pages ranking in descending order
		System.out.println("\n\n======================== Page Ranking ========================\n\n");
		rankWebPage();

		// Word completion and listing related words
		System.out.println("\n\n======================== Word Completion ========================\n\n");
		System.out.println("Enter a word:");
		Scanner scan = new Scanner(System.in);
		String wordComplete = scan.nextLine();
		WordCompletion.wordCompletor(wordComplete);

		System.out.println("Thank you");
	}

	// Method for frequency count of each words in text file
	public static void frequencyCounter(String newTitle) {
		String[] strArr = WordFrequency.htmlParse(CommonConstants.webPagesPath + newTitle + ".txt");
		WordFrequency.hashTable(strArr);
	}

	// Method for page ranking
	public static void rankWebPage() throws Exception {
		System.out.print("Enter word: ");
		sc = new Scanner(System.in);
		String input = sc.nextLine();
		PageRanking.mainPageRank(input);

	}

}
