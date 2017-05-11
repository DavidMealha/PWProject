
public class InterestProfile {
	
	private String topId;
	private String title;
	private String description;
	private String narrative;
	
	public InterestProfile(String topId, String title, String description, String narrative) {
		this.topId = topId;
		this.title = title;
		this.description = description;
		this.narrative = narrative;
	}

	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId;
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

	@Override
	public String toString() {
		return "InterestProfile [topId=" + topId + ", title=" + title + ", description=" + description + ", narrative="
				+ narrative + "]";
	}
	
}
