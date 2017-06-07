package main;


public class XMLReader {

	public static void main(String[] args) throws Exception {
		
		MapData data = new MapData();
		data.readDom("mapdata/A40-16_36.xml");
		data.setEachDepth();
		System.out.println("finish");
		return;
	}

}
