package com.Ease.Utils.JsonProcessing;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;

public final class DOMMapProcessing {

	private DOMMapProcessing(){
		
	}

	// print functions :)
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
