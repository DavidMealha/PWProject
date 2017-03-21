
public class QueryString {
	
	private int Id;
	private String text;
	
	public QueryString(int id, String text) {
		super();
		Id = id;
		this.text = text;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
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
