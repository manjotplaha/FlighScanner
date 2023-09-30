package org.example;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PageRanking {
    public static void mainPageRank(String input) throws Exception{
		
		PageRanker pg = new PageRanker();
		Hashtable<String, Integer> page_rank = pg.matchPattern(input);

		if (page_rank.size() == 0)
			System.out.println("Not found");
		else {
			int totalOccurences = 0;
			for (int occurences : page_rank.values())
				totalOccurences += occurences;
			System.out.println("About " + totalOccurences + " matches ");
			System.out.println("Matches found in " + page_rank.size() + " web pages.\n");
			System.out.println("Matches\t Top 10 Pages");
			Map<String, Integer> sortedByValueDesc = page_rank.entrySet()
					.stream().limit(10)
					.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			sortedByValueDesc.forEach((key, value) -> System.out.println("  " + value + " -- " + key));
		}
	}
}
