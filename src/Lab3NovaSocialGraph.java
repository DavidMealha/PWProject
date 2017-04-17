
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Lab3NovaSocialGraph extends DatasetParser {

	public class User {
		public Integer Id;
		public double userRank;
		public List<Link> inLinks;
		public List<Link> outLinks;

		public User(Integer id) {
			Id = id;
			userRank = 0;
			inLinks = new ArrayList<>();
			outLinks = new ArrayList<>();
		}
	};

	public class Link {
		public User srcUser;
		public User dstUser;
		public QA question;
		public QA answer;
		public int score;
		
	};

	// <QuestionId, Question>
	protected Map<Integer, QA> questionsMap;

	// <QuestionId, <ScoreId, Answer >>
	protected Map<Integer, TreeMap<Integer, QA>> answersMap;
	
	// List<Link>
	private List<Link> links; 

	public Lab3NovaSocialGraph() {
		socialGraph = new HashMap<Integer, User>();

		questionsMap = new HashMap<Integer, QA>();
		answersMap = new HashMap<Integer, TreeMap<Integer, QA>>();
		links = new ArrayList<Link>();
	}

	@Override
	protected void handleAnswer(QA tmp) {

		// Store the answer in the pool of
		// question-answers
		TreeMap<Integer, QA> answers = answersMap.get(tmp.parentId);
		if (answers == null)
			answers = new TreeMap<Integer, QA>();
		answers.put(tmp.Id, tmp);
		answersMap.put(tmp.parentId, answers);

	}

	@Override
	protected void handleQuestion(QA tmp) {
		questionsMap.put(tmp.Id, tmp);
	}

	protected HashMap<Integer, User> socialGraph;
	//protected double damping;

	public void loadSocialGraph() {
		String answersPath = "docs/Answers.csv";
		String questionsPath = "docs/Questions.csv";
		//damping = d;

		loadData(questionsPath, answersPath);

		for (Map.Entry<Integer, TreeMap<Integer, QA>> question : answersMap.entrySet()) {
			Integer QuestionId = question.getKey();
			TreeMap<Integer, QA> answers = question.getValue();
			QA q = questionsMap.get(QuestionId);

			for (Map.Entry<Integer, QA> ans : answers.entrySet()) {
				QA a = ans.getValue();
				if(a == null){
					//System.out.println("This answer is null!");
				}
				if (q == null) {
					//System.out.println("This question is null!");
				}else{
					addLinkToGraph(q, a);
				}
				
			}
		}
	}


	//TODO TODO No fim de cada iteração, normalizar os pageRanks (garantir que a soma dos pageRank = 1)

	public void addLinkToGraph(QA question, QA answer) {
		
		User src = null;
		Integer srcUserId = question.ownerUserId;
		//System.out.println("Question user id:" + srcUserId);
		
		if(!socialGraph.containsKey(srcUserId)){
			src = new User(srcUserId);
			socialGraph.put(src.Id, src);
		}else{
			src = socialGraph.get(srcUserId);
		}
		
		User dst = null;
		Integer dstUserId = answer.ownerUserId;
		//System.out.println("Answer user id:" + dstUserId);
		
		if(!socialGraph.containsKey(dstUserId)){
			dst = new User(dstUserId);
			socialGraph.put(dst.Id, dst);
		}else{
			dst = socialGraph.get(dstUserId);
		}
		
		// TODO: complete the InLinks and the OutLinks
		Link l = new Link();
		l.question = question;
		l.answer = answer;
		l.score = answer.score;

		//Step 2b: Decide which should be the direction of the link (Q->A or A->Q).
		//We are assuming Q->A, the source user is the question user.
		l.srcUser = src;
		l.srcUser.outLinks.add(l);

		l.dstUser = dst;
		l.dstUser.inLinks.add(l);
		
		links.add(l);
		
		//Step 2c: Store the weight of the link according to the answer/question score.
		
		//Step 2d: You should use a variable for storing the full set of links sorted by user.
		
		
	}

	/**
	 * Basically PageRank is the probability of going to Page A, from the other pages that have
	 * links to the page A
	 * So if A has 2 inlinks, it means there are pages with 2 outlinks to Page A
	 * @param iter - number of iterations to update the PageRank(i)
	 */
	public void computePageRank(Integer iter) {
		// dampening = d = 0.8
		float d = 0.8f;
		for (int i = 0; i < 10; i++) {
			for (Map.Entry<Integer, User> user : socialGraph.entrySet()) {
				User tempUser = user.getValue();	
				// N = num users
				int numUsers = socialGraph.size();
				
				// initialize the PR of each user with a seed value of 1/#numUsers
				tempUser.userRank = (1.0f/numUsers);
				
				for (Link link : tempUser.inLinks) {
					// PR(A) = (1-d) + d * Sum[userInlinks](PRoutLindstkUser / nrOutlinksdstUser) - [PR(B)/OL(B) + PR(C)/OL(C)]
					tempUser.userRank += link.srcUser.userRank / link.srcUser.outLinks.size();
					//giving advantage to answers with better scores
					tempUser.userRank += link.score;
				}
				tempUser.userRank = (1.0f-d)/(float) numUsers + d*tempUser.userRank; 
	
				user.setValue(tempUser);
			}
		}
	}

	public void outUserRank() {
		for (Entry<Integer, User> usr : socialGraph.entrySet()) {
			System.out.println(usr.getKey() + "\t" + usr.getValue().userRank + "\t" + usr.getValue().inLinks.size()
					+ "\t" + usr.getValue().outLinks.size());
		}
	}
	
	public void writePageRank() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("docs/pagerank.txt"))) {

			for (Entry<Integer, User> usr : socialGraph.entrySet()) {
				bw.write(usr.getKey() + "," + usr.getValue().userRank + "\n");
			}
			
			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	
	public Map<Integer, Float> readPageRank() {
		Map<Integer, Float> socialGraph = new HashMap<Integer, Float>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("docs/pagerank.txt"))) {
			String line = br.readLine();
			
			while (line != null) {
				StringTokenizer lineTokens = new StringTokenizer(line, ",");
				
				socialGraph.put(Integer.parseInt(lineTokens.nextToken()), Float.parseFloat(lineTokens.nextToken()));		
				line = br.readLine();	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return socialGraph;
	}

	public static void main(String[] args) {

		Lab3NovaSocialGraph temp = new Lab3NovaSocialGraph();

		temp.loadSocialGraph();

		temp.computePageRank(10);

		temp.outUserRank();
		
		temp.writePageRank();

		return;

	}

}
