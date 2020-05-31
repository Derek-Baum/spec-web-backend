package udel.spec.specwebbackend.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//todo: Is this completely useless?
/*
	This is the utility class that currently holds only one method to return a list of all subdirectories for downloads for all of a benchmark.
 */
public class LinkGenerator {
/*
 * 
 * https://www.spec.org/cpu2017/results/res2017q2/cpu2017-20161026-00009.csv
 */


	/*
		Returns an arraylist of all subdirectories for the given benchmark between the startyear and endyear.
	 */
	public static ArrayList<String> getLinks(String benchmarkName, int startYear, int endYear){
		ArrayList<String> output = new ArrayList<>();
		String begin = "https://www.spec.org/" + benchmarkName + "/results/res";
		for(int year = startYear;year <= endYear;year++){
			for(int q = 1; q < 5; q++){
				String toAdd = "" + begin;
				toAdd += "" + year + "q" + q + "/";
				output.add(toAdd);
			}
		}

		return output;
	}
	public static String urlToUID(String url){
		return url.substring(url.indexOf("results/")+8,url.lastIndexOf(".csv"));
	}
}
