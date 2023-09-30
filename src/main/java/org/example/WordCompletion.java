package org.example;

import java.io.File;

public class WordCompletion {

    public static void wordCompletor(String word) throws Exception {
        String[] fileContent = null;
        
        TrieST<Integer> t = new TrieST<Integer>();
        File folder = new File(CommonConstants.webPagesPath+"\\textFile");
        
        for (File fileEntry : folder.listFiles()) {
            fileContent = StringFileManipulation.readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
            if (fileContent != null)
                for (int i = 0; i < fileContent.length; i++)
                    if (StringFileManipulation.IsNumAlpha(fileContent[i]))
                        t.put(fileContent[i].toLowerCase(), i);
        }
        StringFileManipulation.writeFile("TrieDictionary", t.keys().toString().replace(" ", "\n"));
        

        if (t.contains(word)) {
            System.out.println("");
        } else {
            System.out.println("Related Words");
            for (String s : t.keysWithPrefix(word))
                System.out.println(s);
        }
    }
}
