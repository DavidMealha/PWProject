import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class SocialMediaSummarization {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		//Analyzer analyzer = new StandardAnalyzer();
		CustomAnalyzer analyzer = new CustomAnalyzer();
		
		// Similarity similarity = new ClassicSimilarity();
		// Similarity similarity = new BM25Similarity();
		Similarity similarity = new LMDirichletSimilarity();
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
		List<Result> searchResults = sh.searchProfiles(analyzer, similarity, profiles);
		
		//write to file the results
		FileHandler fh = new FileHandler();
		fh.writeFile(searchResults);
		
		Calendar cal2 = Calendar.getInstance();
		System.out.println(cal2.getTimeInMillis() - cal.getTimeInMillis());
	}

}
