import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class SocialMediaSummarization {
	
	private static final String[] INDEX_PATHS = new String[]
			{
				"docs/index/2016/08/02", 
				"docs/index/2016/08/03", 
				"docs/index/2016/08/04", 
				"docs/index/2016/08/05", 
				"docs/index/2016/08/06", 
				"docs/index/2016/08/07", 
				"docs/index/2016/08/08", 
				"docs/index/2016/08/09", 
				"docs/index/2016/08/10",
				"docs/index/2016/08/11"
			};

	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param indexPath
	 * @param tweetsParsed
	 */
	private static void generateIndex(Analyzer analyzer, Similarity similarity, String indexPath, ArrayList<Tweet> tweetsParsed){
		IndexHandler ih = new IndexHandler();
		ih.openIndex(analyzer, similarity, indexPath);
		ih.indexDocuments(tweetsParsed);
	}
	
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
		for(int i = 0; i < INDEX_PATHS.length; i++){
			generateIndex(analyzer, similarity, INDEX_PATHS[i], tweets);
		}
		
		//search the documents with the queries(interest profiles)
	}

}
