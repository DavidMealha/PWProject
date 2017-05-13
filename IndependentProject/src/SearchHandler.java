import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
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


	public SearchHandler() {}
	
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param profiles
	 */
	public List<Result> SearchProfiles(Analyzer analyzer, Similarity similarity, List<InterestProfile> profiles){
		List<Result> listDailyResults = new ArrayList<Result>();
		
		for(int i = 0; i < INDEX_PATHS.length; i++){
			for(InterestProfile profile : profiles){
				listDailyResults.addAll(searchProfile(analyzer, similarity, profile, INDEX_PATHS[i]));
			}
		}
		
		return listDailyResults;
	}
	
	/**
	 * 
	 * @param indexPath
	 */
	private List<Result> searchProfile(Analyzer analyzer, Similarity similarity, InterestProfile profile, String indexPath){
		
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
			TopDocs results = searcher.search(query, 10);
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
				
				long creationDateTimestamp = Long.parseLong(creationDate);
				Date tweetCreationDate = new Date(creationDateTimestamp);
				
				queryResults.add(new Result(tweetCreationDate, profile.getTopId(), tweetId, j+1, hits[j].score, "Lab-0"));
				
			}
			reader.close();
			
//			return getBestScores(queryResults);
			return queryResults;
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
	
	/**
	 * Reorder results taking account the number of followers
	 */
	private void reorderResults(){
		
	}
}
