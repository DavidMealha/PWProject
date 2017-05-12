import java.util.Date;

public class Tweet {
	
	private Date creationDate;
	private long id;
	private String text;
	private long userId;
	private long userFollowers;
	
	public Tweet(Date creationDate, long id, String text, long userId, long userFollowers) {
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserFollowers() {
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
