package com.refactor.csv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.io.PrintWriter;
import com.folder.settings.*;

public class CSVReader {
	public static void main(String[] args) {
		
		CSVReader reader = new CSVReader();
		reader.getProjectUrlAndRefactoredCommit();
	}	
	
	public ArrayList<String[]> getProjectUrlAndRefactoredCommit() {
		
		Settings set = new Settings();
		
		//String csvFile = "/Users/aaldhahe/Documents/CIS 565/Refacing matrix/Summary/MetricsMain1.csv";
		File parentDirectory = new File(set.returnThisProjectPath());
		String csvFile = parentDirectory + File.separator + "MetricsMain1.csv";
		String line = "";
        String csvSplitBy = ",";
        int refCommitId = 1, projectUrl = 0, prevCommitId = 2;
        ArrayList<String[]> result = new ArrayList<String[]>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			int count = 0;
            while ((line = br.readLine()) != null) {
            	if(count == 0) {
            		count++;
            		continue;
            	}
            		
            	String[] lineSplit = line.split(csvSplitBy);
            	lineSplit[refCommitId] = lineSplit[refCommitId].replaceAll(".csv", "");
            	result.add(lineSplit);

            	count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return result;
	}
	
	public void writeRefactorinMinerResultsToCsv(HashMap<String, String[]> map) throws IOException {
		//File parentDirectory = new File(System.getProperty("user.dir")).getParentFile();
		//String csvFile = parentDirectory + File.separator + "refactoringMinerResults.csv";
		//PrintWriter writer = new PrintWriter(new File(csvFile));
		//PrintWriter writer = new PrintWriter(new File("/Users/aaldhahe/Documents/CIS 565/Refacing matrix/Summary/refactoringMinerResults.csv"));
		Settings set = new Settings();
		
		PrintWriter writer = new PrintWriter(new File(set.returnThisProjectPath() + File.separator + "refactoringMinerResults.csv"));
		for(Map.Entry<String, String[]> entry : map.entrySet()) {
			for(int i = 0; i < entry.getValue().length; i++) {
				if(entry.getValue()[i] != null)
					entry.getValue()[i] = entry.getValue()[i].replaceAll("[,]", " ");
			}
	        writer.write(entry.getKey() + "," + String.join(",", entry.getValue()));
	        writer.println();
	        System.out.println(entry.getValue());
		}
		writer.close();
	}
}
