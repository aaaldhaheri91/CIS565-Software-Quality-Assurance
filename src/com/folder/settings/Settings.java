package com.folder.settings;

import java.io.File;

public class Settings 
{
	
	String refactoringMinerPath = "/Users/aaldhahe/Documents/CIS 565/RefactoringMiner/build/distributions/RefactoringMiner/bin";
	String understandPath = "/Applications/Understand.app/Contents/MacOS";
	
	
	public String returnRefactorinMinerPath()
	{
		return refactoringMinerPath;
	}
	
	public String returnUnderstandPath()
	{
		return understandPath;
	}
	
	public String returnThisProjectPath()
	{
		File parentDirectory = new File(System.getProperty("user.dir"));
		return parentDirectory.toString();
	}

}
