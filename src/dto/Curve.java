package dto;


public class Curve {
	
	private String id;
	
//	private Segments segments;
	
	private String posList;
	
	private String depth;
	
	public Curve(String id, String posList) {
		this.id = id;
		this.posList = posList;
	}
	
	public String getId() {
		return id;
	}
	
//	public Segments getSegments() {
//		return segments;
//	}
	
	public String getPostList() {
		return posList;
	}
	
	public void setDepth(String depth) {
		this.depth = depth;
	}
	
	public String getDepth() {
		return depth;
	}
}

