import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

public class SearchHandler {

	public SearchHandler() {}
	
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param profiles
	 */
	public List<Result> searchProfiles(Analyzer analyzer, Similarity similarity, List<InterestProfile> profiles){
		List<Result> listDailyResults = new ArrayList<Result>();
		
		for(int i = 0; i < Utils.INDEX_PATHS.length; i++){
			for(InterestProfile profile : profiles){
				listDailyResults.addAll(searchProfile(analyzer, similarity, profile, Utils.INDEX_PATHS[i], Utils.TWEETS_DATES[i]));
			}
		}
		return listDailyResults;
	}
	
	/**
	 * 
	 * @param indexPath
	 */
	private List<Result> searchProfile(Analyzer analyzer, Similarity similarity, InterestProfile profile, String indexPath, Calendar tweetDate){
		
		IndexReader reader = null;	
		
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

			QueryParser parser = new QueryParser("text", analyzer);
			
			Query query = null;
			try {
				query = parser.parse(QueryParser.escape(profile.getTitle()));
			
				} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				System.out.println("Error parsing query string.");
			}
			
			searcher.setSimilarity(similarity);
			//search how many? Can't be 10, because top 10 might not be from the same day, and those won't be added..
			TopDocs results = searcher.search(query, 20);
			ScoreDoc[] hits = results.scoreDocs;

//			int numTotalHits = results.totalHits;

			List<Result> queryResults = new ArrayList<Result>();
			for (int j = 0; j < hits.length; j++) {
				Document doc = searcher.doc(hits[j].doc);

				String tweetBody = doc.get("text");
				String tweetId = doc.get("id");
				String userId = doc.get("userId");
				String userFollowers = doc.get("userFollowers");
				String creationDate = doc.get("creationDate");
				String userName = doc.get("userName");
				String userAvatar = doc.get("userAvatar");
				
				long creationDateTimestamp = Long.parseLong(creationDate);
				Calendar tweetCreationDate = Calendar.getInstance();
				tweetCreationDate.setTimeInMillis(creationDateTimestamp);
				
				//calculation of the new score, accounting the nr of followers can go here!
				
				if (Utils.areDatesEqual(tweetCreationDate, tweetDate)) {
					queryResults.add(new Result(tweetCreationDate.getTime(), profile.getTopId(), tweetId, j+1, hits[j].score, "Lab-0", tweetBody, userId, userName, userAvatar, userFollowers));
				}
				
			}
			reader.close();
			
			return getBestScores(queryResults);
			//return queryResults;
		} catch (IOException e) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Result> getBestScores(List<Result> queryResults) {
		Collections.sort(queryResults, new Comparator<Result>() {
		    @Override
		    public int compare(Result r1, Result r2) {
		        return Float.compare(r1.getScore(), r2.getScore());
		    }
		});
		
		//it's ordered in reverse
		Collections.reverse(queryResults);
			
		List<Result> newResults = new ArrayList<Result>();
		for (int i = 0; i < queryResults.size(); i++) {
			if(i < 10){
				Result tempResult = queryResults.get(i);
				//with .getRank() we can see what was the previous rank position of that answer 
				newResults.add(new Result(tempResult.getDate(), tempResult.getQueryId(), tempResult.getAnswerId(),
						i + 1, tempResult.getScore(), tempResult.getRunId(), tempResult.getTweetText(),
						tempResult.getUserId(), tempResult.getUserName(), tempResult.getUserAvatar(), tempResult.getUserFollowers()));
			}			
		}
		return newResults;
	}
	
	/**
	 * Reorder results taking account the number of followers
	 */
	private void reorderResults(){
		
	}
}
