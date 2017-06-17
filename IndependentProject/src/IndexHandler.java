import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
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
		for(int i = 0; i < Utils.INDEX_PATHS.length; i++){
			generateIndex(analyzer, similarity, Utils.INDEX_PATHS[i], tweetsParsed, Utils.TWEETS_DATES[i]);
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
					doc.add(new LongPoint("id", t.getId()));
					doc.add(new StoredField("id", t.getId()));

					// Extract field creationDate
					doc.add(new LongPoint("creationDate", t.getCreationDate().getTimeInMillis()));
					doc.add(new StoredField("creationDate", t.getCreationDate().getTimeInMillis()));
					
					// Extract field text
					doc.add(new TextField("text", t.getText(), Field.Store.YES));
					
					// Extract field userId
					doc.add(new LongPoint("userId", t.getUserId()));
					doc.add(new StoredField("userId", t.getUserId()));
					
					// Extract field userFollowers
					doc.add(new DoublePoint("userFollowers", t.getUserFollowers()));
					doc.add(new StoredField("userFollowers", t.getUserFollowers()));
					
					// Extract username text
					doc.add(new TextField("userName", t.getUserName(), Field.Store.YES));
					
					// Extract userAvatar text
					doc.add(new TextField("userAvatar", t.getUserAvatar(), Field.Store.YES));
					
					//Add account verified to the index
					doc.add(new TextField("userVerified", t.getUserVerified(), Field.Store.YES));
					
					// Add the document to the index
					if (idx.getConfig().getOpenMode() == OpenMode.CREATE) {						
						idx.addDocument(doc);
					} else {
						idx.updateDocument(new Term("id", t.getId()+""), doc);
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
