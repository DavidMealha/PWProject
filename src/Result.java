
public class Result {

	private String queryId; // QueryString
	private String answerId;
	private int rank;
	private float score;
	private String runId;
	
	public Result(String queryId, String answerId, int rank, float score, String runId) {
		super();
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
		return String.format("%-10s %-10s %-10s %-10s %-10s %-10s \n", queryId, 0, answerId, rank, score, runId);
//		return "Result [queryId=" + queryId + ", answerId=" + answerId + ", rank=" + rank + ", score=" + score
//				+ ", runId=" + runId + "]\n";
	}
	
	
}
