package com.refactor.run.ssh.commands;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.refactor.csv.reader.CSVReader;
import com.understand.metrics.BatchUnderstandMetrics;
import com.folder.settings.*;



public class RunSshCommandsInJava {
	
	public printOutput getStreamWrapper(InputStream is, String type) {
		return new printOutput(is, type);
	}
 
	public static void main(String[] args) throws IOException, InterruptedException {
 
		int fileCount = 1;
		
		Runtime rt = Runtime.getRuntime();
		Process proc;
		HashMap<String, String> totalResult = new HashMap<String, String>();
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		RunSshCommandsInJava rte = new RunSshCommandsInJava();
		CSVReader csvRead = new CSVReader();
		int projectUrl = 0, commitId = 1;
		String prevProjectName = null;
		String projectName = null;
		Settings set = new Settings();
		String refMinerPath = set.returnRefactorinMinerPath();
		String projectPath = set.returnThisProjectPath();
		
		int count = 0, limit = 7;
		
		ArrayList<String[]> csvFileData = csvRead.getProjectUrlAndRefactoredCommit();
		for(String[] item: csvFileData) {
			
			//String refMinerPath = rte.getProjectAndRefactoringMinerPaths();

			
			System.out.println("Ahmed: " + item[projectUrl]);
			
			projectName = fileCount + "_" + rte.getProjectName(item[projectUrl]);
			if(!projectName.equals(prevProjectName) ) {
				rte.deleteProject(rt, prevProjectName, projectPath);
				rte.cloneGitHubProject(rt, projectPath, item[projectUrl], projectName);
				TimeUnit.SECONDS.sleep(2);
			}
			
			System.out.println(projectName);
			//rte.checkoutBranchToRefactorCommit(rt, item[commitId], projectName);
			
			proc = rte.runRefactoringMiner(rt, item[commitId], projectName, refMinerPath, projectPath, fileCount);
			TimeUnit.SECONDS.sleep(2);
			rte.filterRefactoringMinerResultsToOnlyOneRefactoring(proc, item[commitId], map, item[projectUrl]);
			fileCount++;
			
			prevProjectName = projectName;
			
			//if(count == limit)
			//	break;
			count++;
			System.out.println(count);
		}	
		//TimeUnit.SECONDS.sleep(25);
		//rte.deleteProject(rt, prevProjectName, projectPath);
		
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String[]> pair = (Map.Entry<String, String[]>)it.next();
			System.out.println(pair.getKey() + " = ");
			for(String str: pair.getValue())
				System.out.print(str + ",");
			System.out.println();
		}
		
		csvRead.writeRefactorinMinerResultsToCsv(map);
 
	}
	
	public String getProjectAndRefactoringMinerPaths() {
		//File parentDirectory = new File(System.getProperty("user.dir")).getParentFile();
		//return parentDirectory.toString();
		return "/Users/aaldhahe/Documents/CIS 565/RefactoringMiner/build/distributions/RefactoringMiner/bin";
		//return "/Users/Abdullah/eclipse-workspace/RefactoringMiner/build/distributions/RefactoringMiner/bin";
	}
	
	public String getProjectName(String projectUrl) {
		String[] split = projectUrl.split("/");
		return split[split.length - 1];
	}
	
	public void cloneGitHubProject(Runtime run, String projectPath, String projectUrl, String projectName) {
		String cmd = getSshCommand("clone") + projectUrl + " " + projectName;
		System.out.println("Running command: " + cmd);
		File directory = new File(projectPath + File.separator + "Projects");
		Process proc;
		try {
			proc = run.exec(cmd, null, directory);
			print(proc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getSshCommand(String command) {
		switch(command) {
		case "clone":
			return "git clone ";
		case "checkout":
			return "git checkout ";
		case "delete":
			return "rm -rf ";
		case "refactoring":
			return "./RefactoringMiner -c ";
		}
		return command;
	}
	
	public void checkoutBranchToRefactorCommit(Runtime run, String commitId, String project) {
		String cmd = getSshCommand("checkout");
		System.out.println("running cmd: " + cmd);
		File directory = new File(getProjectAndRefactoringMinerPaths() + "/" + project);
		Process proc;
		
		try {
			proc = run.exec(cmd + commitId, null, directory);
			print(proc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Process runRefactoringMiner(Runtime run, String commitId, String project, String refMinerPath, String projectPath, int fileCount) throws IOException, InterruptedException {
		BatchUnderstandMetrics undMetrics = new BatchUnderstandMetrics();
		String cmd = getSshCommand("refactoring") + projectPath + File.separator + "Projects" + File.separator + project + " " + commitId;
		System.out.println("running cmd: " + cmd);
		File directory = new File(refMinerPath);
		Process proc;
		
		proc = run.exec(cmd, null, directory);
		undMetrics.runUnderstand(commitId, directory, project, fileCount);
		print(proc);
		
		return proc;
	}
	
	public void filterRefactoringMinerResultsToOnlyOneRefactoring(Process proc, String commitId, HashMap<String, String[]> map, String project) throws IOException {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String output;
		int count = 0;
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(project);
		while((output = stdInput.readLine()) != null) {
			System.out.println("Output: " + output);
			if(count != 0){
			//if(count == 0 && output.contains("1 refactorings")) {
				//System.out.println(output);
				//String addNextLine = stdInput.readLine();
				//String addNextLine = output;
				//System.out.println("Adding: " + addNextLine);
				//temp[0] = addNextLine;
				//temp[1] = project;
				//map.put(commitId, temp);
				temp.add(output);
				
				count++;
				//break;
			}
			else
			{
				count++;
			}
			
		}

		String[] array = new String[temp.size()];
		map.put(commitId, temp.toArray(array));
	}
	
	public void deleteProject(Runtime run, String projectName, String projectPath) throws IOException {
		File directory = new File(projectPath + File.separator + "Projects");
		String cmd = getSshCommand("delete") + projectName;
		System.out.println("Running command: " + cmd);
		Process proc;
		proc = run.exec(cmd, null, directory);
		
	}
	
	public void print(Process proc) throws IOException {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		//System.out.println("stdInput: " + stdInput.readLine());
		String output;
		while((output = stdInput.readLine()) != null) {
			System.out.println(output);
		}
	}
	
	private class printOutput extends Thread {
		InputStream is = null;
 
		printOutput(InputStream is, String type) {
			this.is = is;
		}
 
		public void run() {
			String s = null;
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				while ((s = br.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
}
