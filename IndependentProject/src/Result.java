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
	
	public Result(Date date, String queryId, String answerId, int rank, float score, String runId) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		this.date = df.format(date);

		this.queryId = queryId;
		this.answerId = answerId;
		this.rank = rank;
		this.score = score;
		this.runId = runId;
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

	@Override
	public String toString() {
		return String.format("%-15s %-10s %-10s %-20s %-10s %-10s %-10s \n", date, queryId, 0, answerId, rank, score, runId);
	}
	
	
}
