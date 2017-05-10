import java.util.Date;

public class Tweet {
	
	private Date creationDate;
	private int id;
	private String text;
	private int userId;
	private int userFollowers;
	
	public Tweet(Date creationDate, int id, String text, int userId, int userFollowers) {
		this.creationDate = creationDate;
		this.id = id;
		this.text = text;
		this.userId = userId;
		this.userFollowers = userFollowers;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserFollowers() {
		return userFollowers;
	}

	public void setUserFollowers(int userFollowers) {
		this.userFollowers = userFollowers;
	}

	@Override
	public String toString() {
		return "Tweet [creationDate=" + creationDate + ", id=" + id + ", text=" + text + ", userId=" + userId
				+ ", userFollowers=" + userFollowers + "]";
	}
	
}
