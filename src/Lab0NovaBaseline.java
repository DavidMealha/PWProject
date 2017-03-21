

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Lab0NovaBaseline {

	String indexPath = "docs/index";
	String docPath = "docs/Answers.csv";
	String queriesPath = "docs/queries.txt";

	boolean create = true;

	private IndexWriter idx;

	void openIndex(Analyzer analyzer) {
		try {
			// ====================================================
			// Select the data analyser to tokenise document data
			
			
			// ====================================================
			// Configure the index to be created/opened
			//
			// IndexWriterConfig has many options to be set if needed.
			//
			// Example: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer. But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);
			
			// here invokes all the methods of the analyzer
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			if (create) {
				// Create a new index, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			// ====================================================
			// Open/create the index in the specified location
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			idx = new IndexWriter(dir, iwc);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void indexDocuments() {
		if (idx == null)
			return;

		// ====================================================
		// Parse the Answers data
		try (BufferedReader br = new BufferedReader(new FileReader(docPath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine(); // The first line is dummy
			line = br.readLine();

			// ====================================================
			// Read documents
			while (line != null) {
				int i = line.length();

				// Search for the end of document delimiter
				if (i != 0)
					sb.append(line);
				sb.append(System.lineSeparator());
				if (((i >= 2) && (line.charAt(i - 1) == '"') && (line.charAt(i - 2) != '"'))
						|| ((i == 1) && (line.charAt(i - 1) == '"'))) {
					// Index the document
					indexDoc(sb.toString());

					// Start a new document
					sb = new StringBuilder();
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void indexDoc(String rawDocument) {

		Document doc = new Document();

		// ====================================================
		// Each document is organized as:
		// Id,OwnerUserId,CreationDate,ParentId,Score,Body
		Integer Id = 0;
		try {

			// Extract field Id
			Integer start = 0;
			Integer end = rawDocument.indexOf(',');
			String aux = rawDocument.substring(start, end);
			Id = Integer.decode(aux);
			doc.add(new IntPoint("Id", Id));
			doc.add(new StoredField("Id", Id));

			// Extract field OwnerUserId
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer OwnerUserId = Integer.decode(aux);
			doc.add(new IntPoint("OwnerUserId", OwnerUserId));

			// Extract field CreationDate
			try {
				start = end + 1;
				end = rawDocument.indexOf(',', start);
				aux = rawDocument.substring(start, end);
				Date creationDate;
				creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(aux);
				doc.add(new LongPoint("CreationDate", creationDate.getTime()));
			} catch (ParseException e1) {
				System.out.println("Error parsing date for document " + Id);
			}

			// Extract field ParentId
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer ParentId = Integer.decode(aux);
			doc.add(new IntPoint("ParentId", ParentId));

			// Extract field Score
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer Score = Integer.decode(aux);
			doc.add(new IntPoint("Score", Score));
			doc.add(new StoredField("Score", Score));

			// Extract field Body
			String body = rawDocument.substring(end + 1);
			doc.add(new TextField("Body", body, Field.Store.YES));

		// ====================================================
		// Add the document to the index
			if (idx.getConfig().getOpenMode() == OpenMode.CREATE) {
				System.out.println("adding " + Id);
				idx.addDocument(doc);
			} else {
				idx.updateDocument(new Term("Id", Id.toString()), doc);
			}
		} catch (IOException e) {
			System.out.println("Error adding document " + Id);
		} catch (Exception e) {
		System.out.println("Error parsing document " + Id);
		}
	}

	// ====================================================
	// ANNOTATE THIS METHOD YOURSELF
	void indexSearch(Analyzer analyzer, String queryString) {

		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);

			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

			QueryParser parser = new QueryParser("Body", analyzer);
//			while (true) {
//				System.out.println("Enter query: ");

//				String line = in.readLine();

//				if (line == null || line.length() == -1) {
//					break;
//				}
//
//				line = line.trim();
//				if (line.length() == 0) {
//					break;
//				}

			Query query = null;
			try {
				query = parser.parse(queryString);
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				System.out.println("Error parsing query string.");
			}

			TopDocs results = searcher.search(query, 10);
			ScoreDoc[] hits = results.scoreDocs;

			int numTotalHits = results.totalHits;
//			System.out.println(numTotalHits + " total matching documents");

			for (int j = 0; j < hits.length; j++) {
				Document doc = searcher.doc(hits[j].doc);
				String answer = doc.get("Body");
				String Id = doc.get("Id");
				System.out.println("DocId: " + Id + " | DocScore: " + hits[j].score);
			}

//				if (line.equals("")) {
//					break;
//				}
//			}
				
			reader.close();
		} catch (IOException e) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			idx.close();
		} catch (IOException e) {
			System.out.println("Error closing the index.");
		}
	}

	public HashMap<String, String> readFile(){
		// format of each line (id:query)
		HashMap<String, String> queries = new HashMap<String, String>();
		//instead have something like this, and add new QueryString(...)
		//ArrayList<QueryString> listQueries = new ArrayList<QueryString>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(queriesPath))) {
			String line = br.readLine(); 
			
			while (line != null) {
				StringTokenizer lineTokens = new StringTokenizer(line, ":");
				//need to remove the : because some queries have : in the text, which leads to wrong tokenization
				queries.put(lineTokens.nextToken(), lineTokens.nextToken("").replace(":", ""));		
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queries;
	}

	public static void main(String[] args) {

		// 1st step - index all the answers, just has to be done once
		Analyzer analyzer = new StandardAnalyzer();
		//Lab1NovaAnalyser analyzer = new Lab1NovaAnalyser();
		
		Lab0NovaBaseline baseline = new Lab0NovaBaseline();
//		baseline.openIndex(analyzer);
//		baseline.indexDocuments();
//		baseline.close();
		
		// 2nd step - loop over all the queries
		// guardar assim ou criar um objecto QueryString ou wtv com 2 atributos(id e text)? e criar uma lista desse objecto
		HashMap<String, String> a = baseline.readFile();
		Iterator ai = a.keySet().iterator();
		while(ai.hasNext()){
			String key = ai.next().toString();
			System.out.println("Key: " + key + " | Value: " + a.get(key));
			baseline.indexSearch(analyzer, a.get(key));
		}
		
		// percorrer cada linha do queries.txt
		// por cada query fazer o index search, para procurar os 10/20 melhores resultados
		// a indexação basta fazer uma vez
	
		
//
//		baseline.indexSearch(analyzer);
		
		
		// escrever no results.txt os melhores resultados
	}

}
