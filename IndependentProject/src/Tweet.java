import java.util.Calendar;

public class Tweet {
	
	private Calendar creationDate;
	private long id;
	private String text;
	private long userId;
	private long userFollowers;
	
	public Tweet(Calendar creationDate, long id, String text, long userId, long userFollowers) {
		this.creationDate = creationDate;
		this.id = id;
		this.text = text;
		this.userId = userId;
		this.userFollowers = userFollowers;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
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
		return "Tweet [creationDate=" + creationDate.getTime() + ", id=" + id + ", text=" + text + ", userId=" + userId
				+ ", userFollowers=" + userFollowers + "]";
	}
	
}