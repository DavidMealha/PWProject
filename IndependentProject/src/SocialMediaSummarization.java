import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class SocialMediaSummarization {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Analyzer analyzer = new StandardAnalyzer();
		//Lab1NovaAnalyser analyzer = new Lab1NovaAnalyser();
		
		Similarity similarity = new ClassicSimilarity();
		// Similarity similarity = new BM25Similarity();
		// Similarity similarity = new LMDirichletSimilarity();
		// Similarity similarity = new TFIDFSimilarity();
		
		//Load Data
		DatasetParser dp = new DatasetParser();
		
		//Documents
		ArrayList<Tweet> tweets = dp.readTweets();
		
		//Queries
		ArrayList<InterestProfile> profiles = dp.readProfiles();
		
		//Index Documents(tweets)
		IndexHandler ih = new IndexHandler();
		ih.generateIndexes(analyzer, similarity, tweets);
		
		//search the documents with the queries(interest profiles)
		SearchHandler sh = new SearchHandler();
		List<Result> searchResults = sh.SearchProfiles(analyzer, similarity, profiles);
		
		//write to file the results
		FileHandler fh = new FileHandler();
		fh.writeFile(searchResults);
	}

}
