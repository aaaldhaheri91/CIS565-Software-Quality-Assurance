package com.understand.metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import com.refactor.run.ssh.commands.*;
import com.folder.settings.Settings;

public class BatchUnderstandMetrics
{
	
	public static void main(String[] args) {
		
		BatchUnderstandMetrics batchUnderstandMetrics = new BatchUnderstandMetrics();
	}	
	
	public void runUnderstand(String commitID, File directory, String projectID, int fileCount) throws IOException, InterruptedException
	{
		
		RunSshCommandsInJava ssh = new RunSshCommandsInJava();
		Settings set = new Settings();
		Runtime rt = Runtime.getRuntime();
		Process proc;

		
		String undStart = "./und";
		String metricFolderPath = set.returnThisProjectPath() + File.separator + "Metrics"; 
		String databaseFolderPath = set.returnThisProjectPath() + File.separator + "Databases"; 
		String undDatabaseOutput = " -db " + databaseFolderPath + File.separator + commitID + ".udb create ";
		String undCSVMetricOutput = "-metricsOutputFile " + metricFolderPath + File.separator + fileCount + "_prev_" + projectID + "_" + commitID + ".csv ";
		String directoryString = directory.toString();
		String undSettings = "-languages Java add " + set.returnThisProjectPath() + File.separator + "Projects" + File.separator + projectID + " settings -metrics all " + undCSVMetricOutput + "analyze metrics";
		String undCommand = undStart + undDatabaseOutput + undSettings;



	
		
		//proc = rt.exec("echo $PATH");

		//proc = rt.exec(undCommand, null, new File(set.returnUnderstandPath()));
		//System.out.println("Running Understand: " + undCommand);
		//String cmd1 = "create -languages Java" + databaseFolderPath + ".udb";
		
//		try (FileWriter fw = new FileWriter("batchCommands.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw))
//		{
//			out.println("create -languages Java " + databaseFolderPath + File.separator + commitID + ".udb");
//			out.println("add " + set.returnThisProjectPath() + File.separator + "Projects" + File.separator + projectID + " " + databaseFolderPath + File.separator + commitID + ".udb");
//			out.println("settings -metrics all " + databaseFolderPath + File.separator + commitID + ".udb");
//			out.println("settings -metricsOutputFile " + undCSVMetricOutput + " " + databaseFolderPath + File.separator + commitID + ".udb");
//			out.println("analyze " + databaseFolderPath + File.separator + commitID + ".udb");
//			out.println("metrics " + databaseFolderPath + File.separator + commitID + ".udb");
//		}
		
		
		try (FileWriter fw2 = new FileWriter("batchCommands2.txt", true); BufferedWriter bw2 = new BufferedWriter(fw2); PrintWriter out2 = new PrintWriter(bw2))
		{
			out2.println("-db " + databaseFolderPath + File.separator + commitID + ".udb create -languages Java add " + set.returnThisProjectPath() + File.separator + "Projects" + File.separator + projectID + " settings -metrics all -metricsOutputFile " + undCSVMetricOutput + " analyze metrics");

		}		
		
		
//		ProcessBuilder pb = new ProcessBuilder(undStart, cmd1);
//		pb.directory(new File(set.returnUnderstandPath()));
//		proc = pb.start();
//		proc.waitFor();

		
		//>und -db /Users/Abdullah/eclipse-workspace/PermissionsEx.udb create -languages Java add /Users/Abdullah/eclipse-workspace/PermissionsEx settings -metrics all -metricsOutputFile /Users/Abdullah/eclipse-workspace/PermissionsExMetrics.csv analyze metrics
	}



}


