import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Result {

	private String date;
	private String queryId; // QueryString
	private String answerId;
	private int rank;
	private float score;
	private String runId;
	
	private String tweetText;
	private String userId;
	private String userName;
	private String userFollowers;
	private String userAvatar;
	
	public Result(Date date, String queryId, String answerId, int rank, float score, String runId, String tweetText, String userId, String userName, String userAvatar, String userFollowers) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		this.date = df.format(date);

		this.queryId = queryId;
		this.answerId = answerId;
		this.rank = rank;
		this.score = score;
		this.runId = runId;
		
		this.tweetText = tweetText;
		this.userId = userId;
		this.userFollowers = userFollowers;
		this.userName = userName;
		this.userAvatar = userAvatar;
	}
	
	public Result(String date, String queryId, String answerId, int rank, float score, String runId, String tweetText, String userId, String userName, String userAvatar, String userFollowers) {
		this.date = date;
		this.queryId = queryId;
		this.answerId = answerId;
		this.rank = rank;
		this.score = score;
		this.runId = runId;
		
		this.tweetText = tweetText;
		this.userId = userId;
		this.userFollowers = userFollowers;
		this.userName = userName;
		this.userAvatar = userAvatar;
	}


	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFollowers() {
		return userFollowers;
	}

	public void setUserFollowers(String userFollowers) {
		this.userFollowers = userFollowers;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	@Override
	public String toString() {
		return String.format("%-15s %-10s %-10s %-20s %-10s %-15s %-10s \n", date, queryId, 0, answerId, rank, score, runId);
	}
	
	
}
