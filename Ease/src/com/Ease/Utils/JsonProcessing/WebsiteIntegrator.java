package com.Ease.Utils.JsonProcessing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebsiteIntegrator {
	private String home_url = null;
	private String scrapping_json = null;
	private Map<String, Float> map = new HashMap<String, Float>();

	public WebsiteIntegrator(){
		map.put("class", 0.2f);
		map.put("id", 0.2f);
		map.put("href", 0.1f);
		map.put("name", 0.2f);
		map.put("type", 0.15f);
		map.put("other", 0.05f);
	}
	
	public void startWith(String json) {
		
	}
	
	public String generateWebsiteJson(){
		return null;
	}
	
	public Element getSharedDOM(Element first, Element second){
		Elements children = first.children();
		Element tmpSecondChild = null;
		
		for (Element child : children){
			tmpSecondChild = null;
			for (Element child2 : second.children()){
				if (this.isSameElement(child, child2) > 0.6f){
					tmpSecondChild = child2;
					break;
				}
			}
			if (tmpSecondChild != null){
				
			}
			
		}
		return null;
	}
	
	public Float evaluateClass(Element first, Element second){
		int	  sameClassNb = 0;
		
		for (String firstClass : first.classNames()){
			for (String secondClass : second.classNames()){
				if (firstClass.equals(secondClass)){
					sameClassNb++;
					break;
				}
			}
		}
		
		return ((float)sameClassNb / (float)first.classNames().size());
	}
	
	public Float isSameElement(Element first, Element second){
		Float nodeWeight = 0f;
		if (first.tagName() != second.tagName())
			return 0f;
		nodeWeight += 0.1f;
		if (first.children().size() == second.children().size()){
			nodeWeight += 0.1f;
		}
		if (first.siblingIndex() == second.siblingIndex()){
			nodeWeight += 0.2f;
		}
		nodeWeight += this.evaluateClass(first, second) * map.get("class");
		for (Attribute attr: first.attributes()){
			if (!attr.getKey().equals("class")){
				for (Attribute attr2 : second.attributes()){
					if (attr.getKey().equals(attr2.getKey()) && attr.getValue().equals(attr.getValue())){
						Float weight = map.get(attr.getKey());
						if (weight != null)
							nodeWeight += weight;
						else
							nodeWeight += map.get("other");
						break;
					}
				}
			}
		}
		return nodeWeight;
	}
}
