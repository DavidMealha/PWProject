
public class InterestProfile {
	
	private String topic;
	private String title;
	private String description;
	private String narrative;
	
	public InterestProfile(String topic, String title, String description, String narrative) {
		this.topic = topic;
		this.title = title;
		this.description = description;
		this.narrative = narrative;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
}
