package udel.spec.specwebbackend.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Example program to list links from a URL.
 */

public class LinkGrabberService {


    static ArrayList<String> broken;
    public static void main(String[] args) throws IOException {
       getLinksFromBenchmark("mpi2007");
    }



    //this method accepts a benchmark name, and it will return a list to all download links of that benchmark.
    public static Set<String> getLinksFromBenchmark(String benchmark)throws IOException{
        String url="https://www.spec.org/"+benchmark+"/results/";

        HashSet<String> folders = (HashSet<String>) getLinks(url).stream().filter(s -> s.contains("/results/res")).collect(Collectors.toSet());
        HashSet<String> links = new HashSet<>();

        for(String s : folders){
            HashSet<String> toAdd;
            if((toAdd = getLinks(s))!=null)
                links.addAll(toAdd);
        }
        links = (HashSet<String>) links.stream().filter(s -> s.substring(s.length()-4).equals(".csv")).collect(Collectors.toSet());

        System.out.println("all csv links:");
        for(String s : links) {
            System.out.println(s);
        }

        System.out.println(links.size());
        return links;
    }


    /*
     * Given a url, return a HashSet of all Links.
     */
    private static HashSet<String> getLinks(String url) throws IOException{
        HashSet<String> output = new HashSet<>();
        print("Fetching %s...", url);
        Document doc;
        try{
            doc = Jsoup.connect(url).get();
        }catch(IOException e){
            broken.add(url);
            return null;
        }
        Elements links = doc.select("a[href]");
        for(Element link : links){
            output.add(link.attr("abs:href"));
        }
        return output;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}