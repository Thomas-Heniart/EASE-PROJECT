package com.Ease.Utils.JsonProcessing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public final class DOMMapProcessing {

	private DOMMapProcessing(){
		
	}

	// generate selector for element with all attributes + class
	public static String getStringSelectorFromElement(Element elem){
		String ret = "";
		
		if (elem == null)
			return null;
		ret += elem.tagName();
		if (elem.classNames().size() > 0)
			for (String str1 : elem.classNames())
				ret += "." + str1;
		if (elem.attr("id") != null && !(elem.attr("id").equals("")))
			ret += "#" + elem.attr("id");
		for (Attribute attr : elem.attributes())
			if (!attr.getKey().equals("class") && !attr.getKey().equals("id") && !attr.getValue().equals(""))
				ret += "[" + attr.getKey() + "=" + attr.getValue() + "]";
		return ret;
	}
	
	// check if string is in list
	public static boolean isInList(List<String> list, String str){
		for (String s : list)
			if (s.equals(str))
				return true;
		return false;
	}
	
	// generate list of unique tags from list of elements
	public static List<String> getDifferentTags(List<Element> l){
		List<String>	ret = new ArrayList<String>();
		
		for (Element elem : l ){
			if (!(DOMMapProcessing.isInList(ret, elem.tagName()))){
				ret.add(elem.tagName());
			}
		}
		return ret;
	}
	
	// print functions :)
	
	public static void printStringListWithWriter(List<String> l, PrintWriter writer){
		for (String str: l){
			writer.println("   " + str);
		}
	}
	
	public static void printAllValuesFromKey(Map<String, Map<String, List<Element>>> myMap, String key){
		Map<String, List<Element>> tmp = myMap.get(key);
		Set<Map.Entry<String, List<Element>>> set;
		System.out.println("--------  " + key + " ---------");
		if (tmp != null){
			set = tmp.entrySet();
			
			for (Map.Entry<String, List<Element>> me : set){
				System.out.println(me.getKey());
			}
		} else {
			System.out.println("no values...");
		}
	}

	public static void printAllValuesFromKeyWithWriter(Map<String, Map<String, List<Element>>> myMap, String key, PrintWriter writer){
		Map<String, List<Element>> tmp = myMap.get(key);
		Set<Map.Entry<String, List<Element>>> set;
		writer.println("--------  " + key + " ---------");
		if (tmp != null){
			set = tmp.entrySet();
			
			for (Map.Entry<String, List<Element>> me : set){
				writer.println(me.getKey());
				DOMMapProcessing.printStringListWithWriter(DOMMapProcessing.getDifferentTags(me.getValue()), writer);
			}
		} else {
			writer.println("no values...");
		}
	}
	
	public static void printAllMapWithWriter(Map<String, Map<String, List<Element>>> myMap, PrintWriter writer){
		Set<Map.Entry<String, Map<String, List<Element>>>> set = myMap.entrySet();
		
		for (Map.Entry<String, Map<String, List<Element>>> me : set){
			DOMMapProcessing.printAllValuesFromKeyWithWriter(myMap, me.getKey(), writer);
		}
	}
	// print functions end
}
