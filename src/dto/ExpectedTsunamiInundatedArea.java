package dto;


public class ExpectedTsunamiInundatedArea {
	private String id;
	
	private String name;
	
	private String depth;
	
	public ExpectedTsunamiInundatedArea(String id, String name, String depth) {
		this.id = id;
		this.name = name;
		this.depth = depth;
	}
	
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDepth() {
		return depth;
	}
}
