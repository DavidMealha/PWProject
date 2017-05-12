import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
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
	
	private IndexWriter idx;
	
	public IndexHandler(){}
	
	/**
	 * 
	 * @param analyzer
	 * @param similarity
	 * @param indexPath
	 */
	public void openIndex(Analyzer analyzer, Similarity similarity, String indexPath) {
		try {	
			// here invokes all the methods of the analyzer
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			
			// setting the similarity algorithm for the indexing
			iwc.setSimilarity(similarity);

			// Create a new index, removing any previously indexed documents:
			iwc.setOpenMode(OpenMode.CREATE);

			// ====================================================
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
	 */
	public void indexDocuments(ArrayList<Tweet> tweetsParsed){
		if (idx == null)
			return;
		
		for(Tweet t : tweetsParsed){
			Document doc = new Document();

			// id, creationDate, text, userId, userFollowers
			try {
				// Extract field Id
				doc.add(new LongPoint("Id", t.getId()));
				doc.add(new StoredField("Id", t.getId()));

				// Extract field creationDate
				doc.add(new LongPoint("CreationDate", t.getCreationDate().getTime()));

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
					System.out.println("adding " + t.toString());
					idx.addDocument(doc);
				}
				
			} catch (IOException e) {
				System.out.println("Failed to Add Document to Index.\n" + e.getMessage());
			} 
		}
		
	}
}
