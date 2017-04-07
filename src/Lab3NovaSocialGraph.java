
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	protected double damping;

	public void loadSocialGraph(String answersPath, String questionsPath, double d) {

		damping = d;

		loadData(questionsPath, answersPath);

		for (Map.Entry<Integer, TreeMap<Integer, QA>> question : answersMap.entrySet()) {
			Integer QuestionId = question.getKey();
			TreeMap<Integer, QA> answers = question.getValue();
			QA q = questionsMap.get(QuestionId);

			for (Map.Entry<Integer, QA> ans : answers.entrySet()) {
				QA a = ans.getValue();
				if(a == null){
					System.out.println("This answer is null!");
				}
				if (q == null) {
					System.out.println("This question is null!");
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
		System.out.println("Question user id:" + srcUserId);
		
		if(!socialGraph.containsKey(srcUserId)){
			src = new User(srcUserId);
			socialGraph.put(src.Id, src);
		}else{
			src = socialGraph.get(srcUserId);
		}
		
		User dst = null;
		Integer dstUserId = answer.ownerUserId;
		System.out.println("Answer user id:" + dstUserId);
		
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
	 * 
	 * @param iter - number of iterations to update the PageRank(i)
	 */
	public void computePageRank(Integer iter) {
		// dampening = d = 0.8
		float d = 0.8f;

		for (Map.Entry<Integer, User> user : socialGraph.entrySet()) {
			User tempUser = user.getValue();
			
			// N = num users
			int numUsers = socialGraph.size();
			
			// initialize the PR of each user with a seed value of 1/#numUsers
			tempUser.userRank = (1/numUsers);
			
			for (Link link : tempUser.inLinks) {
				// Sum(PRoutLindstkUser / nrOutlinksdstUser) - [PR(B)/OL(B) + PR(C)/OL(C)]
				tempUser.userRank += link.dstUser.userRank / link.dstUser.outLinks.size();
			}
			// PRuser = (1-d) + d* Sum(PRoutLindstkUser / nrOutlinksdstUser)
			tempUser.userRank = (1-d)/numUsers + d*tempUser.userRank; 

			user.setValue(tempUser);
		}

		//Initialize d=0.08 or d=0.15

	}

	public void outUserRank() {
		for (Entry<Integer, User> usr : socialGraph.entrySet()) {
			System.out.println(usr.getKey() + "\t" + usr.getValue().userRank + "\t" + usr.getValue().inLinks.size()
					+ "\t" + usr.getValue().outLinks.size());
		}
	}

	public static void main(String[] args) {

		Lab3NovaSocialGraph temp = new Lab3NovaSocialGraph();

		String answersPath = "docs/Answers.csv";
		String questionsPath = "docs/Questions.csv";

		temp.loadSocialGraph(answersPath, questionsPath, 0.1);

		temp.computePageRank(10);

		temp.outUserRank();

		return;

	}

}
