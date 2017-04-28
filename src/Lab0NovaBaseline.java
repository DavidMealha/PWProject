

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Lab0NovaBaseline {

	String indexPath = "docs/index";
	String docPath = "docs/Answers.csv";
	String queriesPath = "docs/queries.txt";
	String queriesDescPath = "docs/queries-descriptions.offline.txt";
	String queriesKagglePath = "docs/queries.kaggle.txt";
	String queriesDescKagglePath = "docs/queries-descriptions.kaggle.txt";
	String resultsPath = "docs/results.txt";
	String resultsKagglePath = "docs/resultsKaggle.txt";

	boolean create = true;

	private IndexWriter idx;

	void openIndex(Analyzer analyzer, Similarity similarity) {
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
//			sometimes there is not OwnerId, so it throws an exception
//			System.out.println(aux);
			if(!aux.equals("NA")){
//				System.out.println("This entered because it's not NA, it's:" + aux);
				Integer OwnerUserId = Integer.decode(aux);
				doc.add(new IntPoint("OwnerUserId", OwnerUserId));
				doc.add(new StoredField("OwnerUserId", OwnerUserId));
			}

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
	List<Result> indexSearch(Analyzer analyzer, Similarity similarity, QueryString queryString, Map<Integer, Float> nsGraph) {
		
		IndexReader reader = null;	
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

			QueryParser parser = new QueryParser("Body", analyzer);
			
			Query query = null;
			try {
				query = parser.parse(QueryParser.escape(queryString.getText()));
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				System.out.println("Error parsing query string.");
			}
			
			searcher.setSimilarity(similarity);
			TopDocs results = searcher.search(query, 25);
			ScoreDoc[] hits = results.scoreDocs;

//			int numTotalHits = results.totalHits;

			List<Result> queryResults = new ArrayList<Result>();
			for (int j = 0; j < hits.length; j++) {
				Document doc = searcher.doc(hits[j].doc);

				String answer = doc.get("Body");
				String answerId = doc.get("Id");
				String ownerUserId = doc.get("OwnerUserId");
				
				int parseUserId = 0;
				float alfa = 0.9f;
				float newScore = 0.0f;
				
				if (ownerUserId != null) {
					parseUserId = Integer.parseInt(ownerUserId);
					//score = (alfa * score) + ((1 - alfa) * pageRank)
					if (nsGraph.containsKey(parseUserId)) {
						float pageRank = nsGraph.get(parseUserId);
						//newScore = (alfa * hits[j].score) + ((1 - alfa)	* pageRank);
						newScore = pageRank;
					}else{
						newScore = hits[j].score;
					}
				}else{
					newScore = hits[j].score;
				}				
				
				queryResults.add(new Result(queryString.getId(), answerId, j+1, newScore, "Lab-0"));
				//queryResults.add(new Result(queryString.getId(), answerId, j+1, hits[j].score, "Lab-0"));
			}
			reader.close();
			
			return getBestScores(queryResults);
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
		for (int i = 0; i < 10; i++) {
			Result tempResult = queryResults.get(i);
			//with .getRank() we can see what was the previous rank position of that answer 
			newResults.add(new Result(tempResult.getQueryId(), 
					tempResult.getAnswerId(), i+1, tempResult.getScore(), tempResult.getRunId()));
		}
		return newResults;
	}

	public void close() {
		try {
			idx.close();
		} catch (IOException e) {
			System.out.println("Error closing the index.");
		}
	}

	public List<QueryString> readFile(){
		List<QueryString> listQueries = new ArrayList<QueryString>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("docs/queries.kaggle-public.txt"))) {
			String line = br.readLine(); 
			while (line != null) {
				StringTokenizer lineTokens = new StringTokenizer(line, ":");
				
				//those replaces are due to a strange bug in the first query parse...
				listQueries.add(new QueryString(lineTokens.nextToken().replace("ï", "").replace("»", "").replace("¿", ""), lineTokens.nextToken("").replace(":", "").replace("\"", "")));		
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listQueries;
	}
	
	//to read from query kaggle descriptions
	public List<QueryString> readFile2(){
		List<QueryString> listQueries = new ArrayList<QueryString>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(queriesDescKagglePath))) {
			String line = br.readLine(); 
			
			QueryString aux = new QueryString();
			while (line != null) {
				//if its a line with the beggining of a query description
				if(line.equals("")){
					//do nothing
				}
				else if(line.split(":")[0].matches("\\d+")){
					//just set aux variable to null
					if(aux.getId() != null && aux.getText() != null){
						listQueries.add(aux);
						aux = new QueryString();
					}
					aux.setId(null);
					aux.setText(null);
					
					//and start tokenizing the line
					StringTokenizer lineTokens = new StringTokenizer(line, ":");
					aux.setId(lineTokens.nextToken());
					//until the end of the line
					aux.setText(lineTokens.nextToken(""));
				}
				else{
					aux.setText(aux.getText() + "\n" + line);
				}
				line = br.readLine();
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listQueries;
	}
	
	void writeFile(List<Result> results){
	
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("docs/newReportResults/test7.txt"))) {

			bw.write(String.format("%-10s %-10s %-10s %-10s %-10s %-10s \n", "QueryID", "Q0", "DocID", "Rank", "Score", "RunID"));
//			bw.write("ID,AnswerId");
			
			//all tuples save the query id, so get from the first
			String queryIdValue = "";
			for (Result result : results) { 
//				if(queryIdValue == result.getQueryId()){
//					bw.write(result.getAnswerId() + " ");
//				}else{
//					bw.write("\n" + result.getQueryId() + "," + result.getAnswerId() + " ");
//					queryIdValue = result.getQueryId();
//				}
//				
				bw.write(result.toString());
				
			}
			bw.write("\n");
			
			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static void main(String[] args) {

		// 1st step - index all the answers, just has to be done once
		// Analyzer analyzer = new StandardAnalyzer();
		Lab1NovaAnalyser analyzer = new Lab1NovaAnalyser();
		
		// Similarity similarity = new ClassicSimilarity();
		Similarity similarity = new BM25Similarity();
		// Similarity similarity = new LMDirichletSimilarity();
		// Similarity similarity = new TFIDFSimilarity();
		
		Lab0NovaBaseline baseline = new Lab0NovaBaseline();
 		baseline.openIndex(analyzer, similarity);
		baseline.indexDocuments();
		baseline.close();
		
		// Social graph instance	
		Lab3NovaSocialGraph nsGraph = new Lab3NovaSocialGraph();
//		nsGraph.loadSocialGraph();
//		nsGraph.computePageRank(10);
//		nsGraph.writePageRank();
		Map<Integer, Float> socialGraph = nsGraph.readPageRank();
		
		// 2nd step - loop over all the queries
		List<QueryString> queries = baseline.readFile();
		List<Result> results = new ArrayList<Result>();
		for (QueryString queryString : queries) {
			results.addAll(baseline.indexSearch(analyzer, similarity, queryString, socialGraph));
		}
		
		for (Result result : results) {
			System.out.println(result.toString());
		}
		System.out.println("=============================================");
		
		baseline.writeFile(results);
		
		// percorrer cada linha do queries.txt
		// por cada query fazer o index search, para procurar os 10/20 melhores resultados
		// a indexação basta fazer uma vez
		
		// escrever no results.txt os melhores resultados
		// queryID | Q0 | docId | rank | score | runId
	}

}
