package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dto.Curve;
import dto.ExpectedTsunamiInundatedArea;

public class MapData {
	private List<Curve> curves = new ArrayList<Curve>();
    private List<ExpectedTsunamiInundatedArea> expectedTsunamiInundatedAreas = new ArrayList<ExpectedTsunamiInundatedArea>();
    
    public List<Curve> getCurves() {
		return curves;
	}

	public void setCurves(List<Curve> curves) {
		this.curves = curves;
	}

	public List<ExpectedTsunamiInundatedArea> getExpectedTsunamiInundatedAreas() {
		return expectedTsunamiInundatedAreas;
	}

	public void setExpectedTsunamiInundatedAreas(List<ExpectedTsunamiInundatedArea> expectedTsunamiInundatedAreas) {
		this.expectedTsunamiInundatedAreas = expectedTsunamiInundatedAreas;
	}

	public void setEachDepth() {
		String prevID = "";
		int prevIndex = 0;
		String regx = "[0-9]+$";
		Pattern pattern = Pattern.compile(regx);
		long start = System.currentTimeMillis();
		for(Curve curve : curves) {
			Matcher matcher = pattern.matcher(curve.getId().split("_")[0]);
			String cID;
			if (matcher.find()) {
				cID = matcher.group(0);
			} else {
				continue;
			}
			if (cID.equals(prevID)) {
				ExpectedTsunamiInundatedArea etiArea = expectedTsunamiInundatedAreas.get(prevIndex);
				System.out.println(curve.getId() + " : " + etiArea.getId() + " depth : " + etiArea.getDepth());
				continue;
			} else if (prevID.equals("")) {
				prevID = cID;
			} else {
				prevID = cID;
				expectedTsunamiInundatedAreas.remove(prevIndex);
			}
			for(int i = 0; i < expectedTsunamiInundatedAreas.size(); i++) {
				ExpectedTsunamiInundatedArea etiArea = expectedTsunamiInundatedAreas.get(i);
				matcher = pattern.matcher(etiArea.getId());
				String eID = "0";
				if (matcher.find()) {
					eID = matcher.group(0);
				} else {
					continue;
				}
				if (cID.equals(eID)) {
					System.out.println(curve.getId() + " : " + etiArea.getId() + " depth : " + etiArea.getDepth());
					prevIndex = i;
					break;
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start)  + "ms");
	}
	
	public void readDom(String file) throws SAXException, IOException, ParserConfigurationException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	    Document document = documentBuilder.parse(file);

	    Element root = document.getDocumentElement();

	    //ルート要素のノード名を取得する
	    System.out.println("ノード名：" +root.getNodeName());

	    //ルート要素の属性を取得する
	    System.out.println("ルート要素の属性：" + root.getAttribute("gml:id"));

	    //ルート要素の子ノードを取得する
	    NodeList rootChildren = root.getChildNodes();

//	    System.out.println("子要素の数：" + rootChildren.getLength());
	    System.out.println("------------------");

	    List<Curve> curves = new ArrayList<Curve>();
	    List<ExpectedTsunamiInundatedArea> expectedTsunamiInundatedAreas = new ArrayList<ExpectedTsunamiInundatedArea>();
	    for(int i = 0; i < rootChildren.getLength(); i++) {
	    	Node node = rootChildren.item(i);
	    	if (node.getNodeType() == Node.ELEMENT_NODE) {
	    		Element element = (Element)node;
	    		String id = element.getAttribute("gml:id");
	    		switch (element.getNodeName()) {
				case "gml:Curve":
					id = StringUtils.strip(id, "Cv");
					String posList = element.getElementsByTagName("gml:posList").item(0).getTextContent();
					curves.add(new Curve(id, posList));
					break;
				case "ksj:ExpectedTsunamiInundatedArea":

					id = StringUtils.strip(id, "etia");
					String name = element.getElementsByTagName("ksj:prefectureName").item(0).getTextContent();
					String depth = element.getElementsByTagName("ksj:cassificationOfWaterDepth").item(0).getTextContent();
					expectedTsunamiInundatedAreas.add(new ExpectedTsunamiInundatedArea(id, name, depth));
					break;
				default:
					break;
				}
	    	}
	    }
	    setCurves(curves);
	    setExpectedTsunamiInundatedAreas(expectedTsunamiInundatedAreas);
	    
	    return;
	  }
}
