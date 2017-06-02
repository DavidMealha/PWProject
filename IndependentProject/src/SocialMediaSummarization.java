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
import org.apache.lucene.search.similarities.Similarity;

public class SocialMediaSummarization {

	public static void main(String[] args) throws FileNotFoundException, ParseException {

		// Load Data
		DatasetParser dp = new DatasetParser();

		// Documents
		ArrayList<Tweet> tweets = dp.readTweets();

		// Queries
		ArrayList<InterestProfile> profiles = dp.readProfiles();

		// ======= FOR TEST PURPOSE =========
		ArrayList<Analyzer> analyzers = new ArrayList<Analyzer>();
		analyzers.add(new StandardAnalyzer());
		analyzers.add(new CustomAnalyzer());

		ArrayList<Similarity> similarities = new ArrayList<Similarity>();
		similarities.add(new ClassicSimilarity());
		similarities.add(new BM25Similarity());
		similarities.add(new LMDirichletSimilarity());

		String[] resultPaths = new String[] {"docs/results/test1Classic.txt",
											 "docs/results/test2Classic.txt",
											 "docs/results/test3Classic.txt",
											 "docs/results/test4Classic.txt",
											 "docs/results/test5Classic.txt",
											 "docs/results/test6Classic.txt",
											 "docs/results/test7Classic.txt",
											 "docs/results/test8Classic.txt",
											 "docs/results/test9Classic.txt",
											 "docs/results/test10Classic.txt",
											 "docs/results/test11Classic.txt",
											 "docs/results/test12Classic.txt",
											 "docs/results/test13Classic.txt",
											 "docs/results/test14Classic.txt",
											 "docs/results/test15Classic.txt",
											 "docs/results/test1BM25.txt",
											 "docs/results/test2BM25.txt",
											 "docs/results/test3BM25.txt",
											 "docs/results/test4BM25.txt",
											 "docs/results/test5BM25.txt",
											 "docs/results/test6BM25.txt",
											 "docs/results/test7BM25.txt",
											 "docs/results/test8BM25.txt",
											 "docs/results/test9BM25.txt",
											 "docs/results/test10BM25.txt",
											 "docs/results/test11BM25.txt",
											 "docs/results/test12BM25.txt",
											 "docs/results/test13BM25.txt",
											 "docs/results/test14BM25.txt",
											 "docs/results/test15BM25.txt",
											 "docs/results/test1LMD.txt",
											 "docs/results/test2LMD.txt",
											 "docs/results/test3LMD.txt",
											 "docs/results/test4LMD.txt",
											 "docs/results/test5LMD.txt",
											 "docs/results/test6LMD.txt",
											 "docs/results/test7LMD.txt",
											 "docs/results/test8LMD.txt",
											 "docs/results/test9LMD.txt",
											 "docs/results/test10LMD.txt",
											 "docs/results/test11LMD.txt",
											 "docs/results/test12LMD.txt",
											 "docs/results/test13LMD.txt",
											 "docs/results/test14LMD.txt",
											 "docs/results/test15LMD.txt"
											 };
		// ======= FOR TEST PURPOSE =========

		int i = 0;
		for (Analyzer analyzer : analyzers) {
			for (Similarity similarity : similarities) {

				// Analyzer analyzer = new StandardAnalyzer();
				// CustomAnalyzer analyzer = new CustomAnalyzer();

				//Similarity similarity = new ClassicSimilarity();
				// Similarity similarity = new BM25Similarity();
				// Similarity similarity = new LMDirichletSimilarity();

				// Index Documents(tweets)
				IndexHandler ih = new IndexHandler();
				ih.generateIndexes(analyzer, similarity, tweets);

				// search the documents with the queries(interest profiles)
				SearchHandler sh = new SearchHandler();
				List<Result> searchResults = sh.searchProfiles(analyzer, similarity, profiles);

				// write to file the results
				FileHandler fh = new FileHandler();
				fh.writeFile(searchResults, resultPaths[i]);

				i++;
			}
		}
	}

}
