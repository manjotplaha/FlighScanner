package org.example;

import java.io.File;

import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageRanker {

	public Hashtable<String, Integer> matchPattern(String pattern) throws IOException {
		Hashtable<String, Integer> pageRank = new Hashtable<String, Integer>();
		String[] fileContent = null;
		File folder = new File(CommonConstants.webPagesPath + "\\textFile");
		if (folder.listFiles() == null) {
			return null;
		} else {
			for (File fileEntry : folder.listFiles()) {
				fileContent = StringFileManipulation.readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
				if (fileContent != null)
					for (int i = 0; i < fileContent.length; i++) {
						Pattern p1 = Pattern.compile(pattern);

						// Matcher object creation
						Matcher m = p1.matcher(fileContent[i]);
						while (m.find())
							pageRank.merge(fileEntry.getName(), 1, Integer::sum);
					}
			}

			return pageRank;
		}
	}
}