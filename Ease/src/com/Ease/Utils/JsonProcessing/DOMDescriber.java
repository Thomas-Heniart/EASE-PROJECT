package com.Ease.Utils.JsonProcessing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DOMDescriber {
	private Map<String, Map<String, List<Element>>> myMap = null;
	private Element									dom = null;

	public DOMDescriber(Element elem){
		this.initWith(elem);
	}
	
	public void initWith(Element elem){
		myMap = new HashMap<String, Map<String, List<Element>>>();
		dom = elem;
		this.initMap(elem);
	}
		
	public Map<String, Map<String, List<Element>>> getCommonMapWith(DOMDescriber dd){
		Map<String, Map<String, List<Element>>> ret = new HashMap<String, Map<String, List<Element>>>();
		Set<Map.Entry<String, Map<String, List<Element>>>> set1 = myMap.entrySet();
		Set<Map.Entry<String, Map<String, List<Element>>>> set2 = dd.getMap().entrySet();
		
		for (Map.Entry<String, Map<String, List<Element>>> me1 : set1){
			for (Map.Entry<String, Map<String, List<Element>>> me2 : set2){
				if (me1.getKey().equals(me2.getKey())){
					ret.put(me1.getKey(), this.getCommonElements(me1.getValue(), me2.getValue()));
					break;
				}
			}			
		}
		
		return ret;
	}
	
	public Map<String, Map<String, List<Element>>> getDiffMapWith(DOMDescriber dd){
		Map<String, Map<String, List<Element>>> ret = new HashMap<String, Map<String, List<Element>>>();
		Set<Map.Entry<String, Map<String, List<Element>>>> set1 = myMap.entrySet();
		Set<Map.Entry<String, Map<String, List<Element>>>> set2 = dd.getMap().entrySet();
		Map<String, List<Element>> tmp;
		
		for (Map.Entry<String, Map<String, List<Element>>> me1 : set1){
			for (Map.Entry<String, Map<String, List<Element>>> me2 : set2)
				if (me1.getKey().equals(me2.getKey())){
					tmp = this.getDiffElements(me1.getValue(), me2.getValue());
					if (tmp.size() > 0)
						ret.put(me1.getKey(), tmp);
			}
		}				
		return ret;
	}

	public boolean isPresentInMap(String key, String value){
		Map<String, List<Element>> elem = myMap.get(key);
		Set<Map.Entry<String, List<Element>>> set = null;
		
		if (elem != null){
			set = elem.entrySet();
			for (Map.Entry<String, List<Element>> me: set){
				if (me.getKey().equals(value))
					return true;
			}
		}
		return false;
	}
	
	public boolean isElementPresentInMap(Element elem){
		Map<String, List<Element>> tmpList = null;
		
		tmpList = this.myMap.get("tagName");
		if (tmpList.get(elem.tagName()) == null)
			return false;
		tmpList = this.myMap.get("class");
		if (tmpList == null && elem.classNames().size() > 0)
			return false;
		for (String str: elem.classNames()){
			if (tmpList.get(str) == null)
				return false;
		}
		for (Attribute attr : elem.attributes()){
			if (!attr.getKey().equals("class") && attr.getValue() != null && !attr.getValue().equals("")){
				tmpList = this.myMap.get(attr.getKey());
				if (tmpList == null || tmpList.get(attr.getValue()) == null)
					return false;
			}
		}
		return true;
	}
	
	public Map<String, Map<String, List<Element>>> getMap(){
		return myMap;
	}
	
	public Element getDOM(){
		return this.dom;
	}
	
	public Elements executeQuery(String query){
		if (this.dom != null){
			return this.dom.select(query);
		}
		return null;
	}
	// private helper functions
	private void initMap(Element elem){
		this.addToTheMap(elem, "tagName", elem.tagName());
		for (String className: elem.classNames())
			this.addToTheMap(elem, "class", className);
		for (Attribute attr: elem.attributes()){
			if (!(attr.getKey().equals("class")) && attr.getValue() != null && !(attr.getValue().equals("")))
				this.addToTheMap(elem, attr.getKey(), attr.getValue());
		}
		for (Element child : elem.children())
			this.initMap(child);
	}

	private Map<String, List<Element>> getDiffElements(Map<String, List<Element>> m1, Map<String, List<Element>> m2){
		Map<String, List<Element>> ret = new HashMap<String, List<Element>>();
		Set<Map.Entry<String, List<Element>>> set1 = m1.entrySet();
		Set<Map.Entry<String, List<Element>>> set2 = m2.entrySet();
		boolean								  found = false;

		for (Map.Entry<String, List<Element>> me1 : set1){
			found = false;
			for (Map.Entry<String, List<Element>> me2 : set2){
				if (me1.getKey().equals(me2.getKey())){
					found = true;
					break;
				}
			}
			if (!found)
				ret.put(me1.getKey(), me1.getValue());
		}
		return ret;
	}

	private Map<String, List<Element>> getCommonElements(Map<String, List<Element>> m1, Map<String, List<Element>> m2){
		Map<String, List<Element>> ret = new HashMap<String, List<Element>>();
		Set<Map.Entry<String, List<Element>>> set1 = m1.entrySet();
		Set<Map.Entry<String, List<Element>>> set2 = m2.entrySet();
		
		for (Map.Entry<String, List<Element>> me1 : set1){
			for (Map.Entry<String, List<Element>> me2 : set2){
				if (me1.getKey().equals(me2.getKey())){
					ret.put(me1.getKey(), me1.getValue());
					break;
				}
			}
		}
		return ret;
	}	

	private void addToTheMap(Element elem, String key, String value){
		Map<String, List<Element>> 	tmp = null;
		List<Element>				tmpList = null;
		tmp = myMap.get(key);
		if (tmp == null){
			myMap.put(key, new HashMap<String, List<Element>>());
			tmp = myMap.get(key);
		}
		tmpList = tmp.get(value);
		if (tmpList == null){
			tmp.put(value, new ArrayList<Element>());
			tmpList = tmp.get(value);
		}
		tmpList.add(elem);
	}
}
