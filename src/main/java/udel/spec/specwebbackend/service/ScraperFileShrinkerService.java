package udel.spec.specwebbackend.service;

import org.apache.commons.io.FileUtils;
import udel.spec.specwebbackend.model.BenchmarkType;
import udel.spec.specwebbackend.model.DBEntry;
import udel.spec.specwebbackend.utility.LinkGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ScraperFileShrinkerService {
	//openACC
	/*static final String[] keywords = {"\"Hardware Model:\"",
									  "SPECaccel_acc_base",
									  "\"CPU Name\"",
									  "\"CPU(s) enabled\"",
									  "\"Accel Model Name\"",
									  "Memory",
									  "\"Operating System\"",
									  "\"Other Software\"",
									  "Compiler"};*/
	static final String[] keywords_openacc = {"\"Hardware Model:\"",
			  								  "SPECaccel_acc_base",
			  								  "\"CPU Name\"",
			  								  "\"CPU(s) enabled\"",
			  								  "\"Accel Model Name\"",
			  								  "Memory",
			  								  "\"Operating System\"",
			  								  "\"Other Software\"",
	                                          "Compiler"};
	static final String[] keywords_cpu2017 = {"\"Hardware Model:\"",
											  "SPECrate2017_fp_base",
											  "\"CPU Name\"",
											  "Enabled",
											  "Memory",
											  "OS",
											  "Compiler"};
	static final String[] keywords_cpu2006 = {"\"Hardware Model:\"",
					                          "SPECint_base2006",
					                          "\"CPU Name\"",
					                          "\"CPU(s) enabled\"",					                         
					                          "Memory",
					                          "\"Operating System\"",
					                          "\"Other Software\"",
											  "Compiler"};
	static final String[] keywords_omp2012 = {"\"Hardware Model:\"",
											  "SPECompG_base2012",
			                                  "\"CPU Name\"",
			                                  "\"CPU(s) enabled\"",
			                                  "Memory",
			                                  "\"Operating System\"",
	                                          "Compiler"};
	static final String[] keywords_mpi2007 = {"Model",
											  "SPECmpiL_base2007",
			                                  "\"CPU Name\"",
											  "\"Chips enabled\"",
											  "\"Cores enabled\"",
											  "\"Cores per chip\"",
											  "\"Threads per core\"",
											  "Memory",
											  "\"Operating System\"",
											  "\"Other Software\"",};
	static String currentTest = "undefined";

	//TODO: turn this main method, into a non main method, that accepts a url to a .csv file, is able to download csv, use the shrinker methods below on the file, build it into a DBEntry, and then later add it to the db.
	public static void main(String[] args) throws IOException{
		processBenchmark(BenchmarkType.OMP2012,"omp2012");
		/*String s = "blasdhkasdfh\nSPECaccel_acc_base,1.740416,,,,\n"
				+ "etckjasdhfksdaf,,,dshfas,jasdfkj,\n\n,ashfasdhf,\n"
				+ " \"Accel Model Name\",\"Tesla K20\" ";
		System.out.println(getNext(s,"SPECaccel_acc_base"));
		System.out.println(getNext(s,"\"Accel Model Name\""));
		stringToCSV("does,this,work?");*/
		
		/*
		currentTest="accel-20140228-00005.csv";
		String test = csvToString("src/accel-20140228-00005.csv");
		System.out.println(buildString(test));
		stringToCSV(buildString(test));
		System.out.println("Done");
		*/
		/*

		int i = 0;
		for(String s : args){
			if(isCSV(s)){
				if(i == 0){
					keywords = keywords_openacc;
				}
				System.out.println("Folding " + s);
				currentTest=s;
				//here we would call buildDBEntry, set it to a new object, for now print it, and then TODO: db integration
				stringToCSV(buildString(csvToString(s)));
			}else{
				i++;
				assignKeywords(s);
			}
		}

		*/
	}





	/*
		The root to start processing an entire benchmark at once.
	 */
	public static void processBenchmark(BenchmarkType benchmark,String benchmarkUrl) throws IOException {
		String[] currentKeywords = assignKeywords(benchmark);

		Set<String> urls = LinkGrabberService.getLinksFromBenchmark(benchmarkUrl);

		for(String url : urls){
			processUrlToDBEntry(url,benchmark,currentKeywords);
		}
	}
	/*
		processes one download link at a time.
	 */
	public static void processUrlToDBEntry(String url, BenchmarkType benchmarkType, String[] currentKeywords) throws IOException {
		String fileName = ".tmp127351723";
		//download the file
		FileUtils.copyURLToFile(
				new URL(url),
				new File(fileName)
		);

		DBEntry entry = buildDBEntry(csvToString(fileName),benchmarkType,currentKeywords, LinkGenerator.urlToUID(url));


		//delete the file after it has been processed.

		FileUtils.forceDelete(new File(fileName));
	}

	public static String[] assignKeywords(BenchmarkType dataset){
		String output[] = keywords_openacc;
		/*
		 * openacc
		 * cpu2017
		 * cpu2006
		 * mpi2007
		 * omp2012
		 */
		if(dataset.equals(BenchmarkType.ACCEL)){
			output = keywords_openacc;
		}else if(dataset.equals(BenchmarkType.CPU2006)){
			output = keywords_cpu2006;
		}else if(dataset.equals(BenchmarkType.CPU2017)){
			output = keywords_cpu2017;
		}else if(dataset.equals(BenchmarkType.MPI2007)){
			output = keywords_mpi2007;
		}else if(dataset.equals(BenchmarkType.OMP2012)){
			output = keywords_omp2012;
		}
		return output;
	}
	public static boolean isCSV(String s){
		if(s.length() >= 4){
			return s.substring(s.length()-4).equals(".csv");
		}
		return false;
	}
	/*
	 * Given a .csv file, return it as a big string.
	 */
	public static String csvToString(String fname) throws IOException{
		String contents = new String(Files.readAllBytes(Paths.get(fname)));
		return contents;
	}

	//TODO: instead of creating a new file, we can just make an object.
	public static String buildString(String wholeFile){
		/*
		String output = "";
		for(int i = 0; i < keywords.length;i++){
			output+=getNext(wholeFile,keywords[i])+(i==keywords.length-1 ? "" : ",");
		}
		output=output.replaceAll("\n", "");
		return output;*/
		return null;
	}

	public static DBEntry buildDBEntry(String wholeFile,BenchmarkType benchmarkType,String[] currentKeywords,String uniqueId){
		DBEntry dbEntry = new DBEntry(uniqueId,benchmarkType.toString(),benchmarkType);
		List<String> valList = new ArrayList<>();
		for(int i = 0; i < currentKeywords.length;i++){
			valList.add(getNext(wholeFile,currentKeywords[i]).replaceAll("\n", "").trim());
		}
		dbEntry.addAttributes(valList);

		System.out.println("degugging purposes to test if this is working\nValues from this file:\n");
		System.out.println(benchmarkType.toString());

		for(String v : valList){
			System.out.println(v);
		}

		return dbEntry;
	}
	/*
	 * given our thing we get returned from buildString, output a .csv file somewhere.
	 * boom done.
	 */
	public static void stringToCSV(String s) throws IOException{
		System.out.println("We have read the file and folded it into the following string?.");
		s=s.replaceAll("\\R", "");
		System.out.println(s);
		System.out.println("RAHH");
		String[] arr = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		System.out.println("");
		System.out.println(Arrays.toString(arr));
		
		String appender = currentTest;
		
		s+=",\"" + currentTest.substring(currentTest.indexOf("accel-")+6, currentTest.lastIndexOf(".csv")) + "\"";
		System.out.println("final s:\n" + s + "\nrahhrhashdf");

/*
		if(arr.length >2){
			appender+=arr[1];
		}
		appender = appender.replaceAll("\"", "");
		*/
		FileWriter writer = new FileWriter("scraped-"+appender);
		writer.append(s);
		writer.close();
	}
	/*
	 * Given Strings s1 and s2, return a string that is the next
	 * "entry" contained within s1 that follows after the string
	 * s2.
	 * 
	 * return null if s2 is not found within s1
	 */
	public static String getNext(String s1,String s2){
		int x = s1.indexOf(s2) + s2.length();
		if(x==s2.length()-1) {
			System.out.println("we are here");
			return null;
		}
		String[] arr = s1.substring(x).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)[1].split("\n");
		return arr[0];
	}

}
