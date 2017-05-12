import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexHandler {
	
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
	
	private static final Calendar[] TWEETS_DATES = new Calendar[]
			{
				new GregorianCalendar(2016,7,2,23,59,59),
				new GregorianCalendar(2016,7,3,23,59,59),
				new GregorianCalendar(2016,7,4,23,59,59),
				new GregorianCalendar(2016,7,5,23,59,59),
				new GregorianCalendar(2016,7,6,23,59,59),
				new GregorianCalendar(2016,7,7,23,59,59),
				new GregorianCalendar(2016,7,8,23,59,59),
				new GregorianCalendar(2016,7,9,23,59,59),
				new GregorianCalendar(2016,7,10,23,59,59),
				new GregorianCalendar(2016,7,11,23,59,59)
			};
	
	boolean create = true;
	private IndexWriter idx;
	
	public IndexHandler(){}
	
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param tweetsParsed
	 */
	public void generateIndexes(Analyzer analyzer, Similarity similarity, ArrayList<Tweet> tweetsParsed){
		for(int i = 0; i < INDEX_PATHS.length; i++){
			generateIndex(analyzer, similarity, INDEX_PATHS[i], tweetsParsed, TWEETS_DATES[i]);
		}
	}
	
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param indexPath
	 * @param tweetsParsed
	 * @param tweetDate
	 */
	private void generateIndex(Analyzer analyzer, Similarity similarity, String indexPath, ArrayList<Tweet> tweetsParsed, Calendar tweetDate){
		openIndex(analyzer, similarity, indexPath);
		indexDocuments(tweetsParsed, tweetDate);
		close();
	}
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param indexPath
	 */
	private void openIndex(Analyzer analyzer, Similarity similarity, String indexPath) {
		try {	
			// here invokes all the methods of the analyzer
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			
			// setting the similarity algorithm for the indexing
			iwc.setSimilarity(similarity);

			if (create) {
				// Create a new index, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			// Open/create the index in the specified location
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			idx = new IndexWriter(dir, iwc);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param tweetsParsed
	 * @param tweetDate
	 */
	private void indexDocuments(ArrayList<Tweet> tweetsParsed, Calendar tweetDate){
		if (idx == null)
			return;
		
		for(Tweet t : tweetsParsed){
			Document doc = new Document();
			
			//only add if date is equal or smaller than the tweet date received
			// if 02/08/2016 23:59:59 <= 03/08/2016 00:00:00
			if(t.getCreationDate().before(tweetDate) || t.getCreationDate().equals(tweetDate)){
				
				// id, creationDate, text, userId, userFollowers
				try {
					// Extract field Id
					doc.add(new LongPoint("Id", t.getId()));
					doc.add(new StoredField("Id", t.getId()));

					// Extract field creationDate
					doc.add(new LongPoint("CreationDate", t.getCreationDate().getTimeInMillis()));

					// Extract field text
					doc.add(new TextField("Text", t.getText(), Field.Store.YES));
					
					// Extract field userId
					doc.add(new LongPoint("userId", t.getUserId()));
					doc.add(new StoredField("userId", t.getUserId()));
					
					// Extract field userFollowers
					doc.add(new LongPoint("userFollowers", t.getUserFollowers()));
					doc.add(new StoredField("userFollowers", t.getUserFollowers()));

					// Add the document to the index
					if (idx.getConfig().getOpenMode() == OpenMode.CREATE) {
						System.out.println(tweetDate.getTime() + " >= " + t.getCreationDate().getTime());
						
						idx.addDocument(doc);
					} else {
						idx.updateDocument(new Term("Id", t.getId()+""), doc);
					}
					
				} catch (IOException e) {
					System.out.println("Failed to Add Document to Index.\n" + e.getMessage());
				}
			}
 
		}
		
	}
	
	public void close() {
		try {
			idx.close();
		} catch (IOException e) {
			System.out.println("Error closing the index.");
		}
	}
}
