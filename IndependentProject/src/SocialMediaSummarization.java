import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class SocialMediaSummarization {

	public static void main(String[] args) throws FileNotFoundException, ParseException {

		long initTime = System.currentTimeMillis();
		
		// Load Data
		DatasetParser dp = new DatasetParser();

		// Documents
		ArrayList<Tweet> tweets = dp.readTweets();

		// Queries
		ArrayList<InterestProfile> profiles = dp.readProfiles();

		// ======= FOR TEST PURPOSE =========

		ArrayList<Similarity> similarities = new ArrayList<Similarity>();
		//similarities.add(new ClassicSimilarity());
		similarities.add(new BM25Similarity());
		//similarities.add(new LMDirichletSimilarity());
		//similarities.add(new LMJelinekMercerSimilarity(0.5f));

		// ======= FOR TEST PURPOSE =========

		for (String config: Utils.TEST_CASES) {
			for (Similarity similarity : similarities) {

				// Analyzer analyzer = new StandardAnalyzer();
				CustomAnalyzer analyzer = new CustomAnalyzer(config);

				// Index Documents(tweets)
				IndexHandler ih = new IndexHandler();
				ih.generateIndexes(analyzer, similarity, tweets);

				// search the documents with the queries(interest profiles)
				SearchHandler sh = new SearchHandler();
				List<Result> searchResults = sh.searchProfiles(analyzer, similarity, profiles);

				// write to file the results
				FileHandler fh = new FileHandler();
				fh.writeFile(searchResults, "docs/results/" + "QUERY_TITLE_" + config + "_" + similarity.getClass().getSimpleName() + "_WITH_NUM_FOLLOWERS_90PERCENT_AND_VERIFIED");
			}
		}
		long finalTime = System.currentTimeMillis() - initTime;
		System.out.println("The program took " + finalTime/1000 + " seconds.");
	}

}
