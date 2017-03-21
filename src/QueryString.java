
public class QueryString {
	
	private String Id;
	private String text;
	
	public QueryString(String id, String text) {
		this.Id = id;
		this.text = text;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "QueryString [Id=" + Id + ", text=" + text + "]";
	}
	
	
}
