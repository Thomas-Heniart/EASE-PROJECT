package com.Ease.Utils.JsonProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public List<String> getCommonItemsFromLists(List<String> l1, List<String> l2){
		List<String> ret = new ArrayList<String>();
		
		for (String str1: l1){
			for (String str2: l2){
				if (str1.equals(str2)){
					ret.add(str1); 
					break;
				}
			}
		}
		return ret;
	}
	
	public List<String> getDifferentItemsFromLists(List<String> l1, List<String> l2){
		List<String> ret = new ArrayList<String>();
		boolean	found = false;
		
		for (String str1 : l1){
			found = false;
			for (String str2 : l2){
				if (str1.equals(str2)){
					found = true;
					break;
				}
			}
			if (!found)
				ret.add(str1);
		}
		return ret;
	}
	
	public boolean isInList(List<String> list, String name){
		for (String str: list){
			if (str.equals(name))
				return true;
		}
		return false;
	}
	public void getAllClassFromDOM(Element dom, List<String> list){
		for (String elem: dom.classNames()){
			if (!isInList(list, elem))
				list.add(elem);
		}
		for (Element child : dom.children()){
			this.getAllClassFromDOM(child, list);
		}
	}
	
	public Element cloneDiv(Element toClone){
		Element ret = toClone.clone();
		ret.html("");
		return ret;
	}
	
	public void printElement(Element elem, int stage){
		int	tmp = stage;
		
		while (tmp > 0){
			System.out.print("   ");
			tmp--;
		}
		System.out.print(elem.tagName());
		
		for (String className : elem.classNames()){
			System.out.print("." + className);
		}
		if (!elem.attr("id").equals("")){
			System.out.print("#" + elem.attr("id"));
		}
		System.out.println("");
		for (Element child : elem.children()){
			this.printElement(child, stage + 1);
		}
	}
	
	public void recursiveDOM(Element first, Element second, Element third){
		Elements fChildren = first.children();
		Elements sChildren;
		Element	tmp = null;
		Integer	  selectHelper = 0;
		boolean matched = false;
		
		System.out.println("enter recursive function");
		for (Element fChild : fChildren){
			matched = false;
			System.out.println("here");
			sChildren = second.children();
			System.out.println("sChild length : " + sChildren.size());
			for (Element sChild : sChildren){
				if (this.isSameElement(fChild, sChild) > 0.6f){
					tmp = this.cloneDiv(sChild);
					tmp.addClass("easeSelectHelper" + selectHelper.toString());
					third.append(tmp.toString());
					tmp = third.select(".easeSelectHelper" + selectHelper.toString()).get(0);
					tmp.removeClass("easeSelectHelper" + selectHelper.toString());
					selectHelper++;
					this.recursiveDOM(fChild, sChild, tmp);
					matched = true;
					fChild.remove();
					sChild.remove();
					break;
				}
			}
		}
	}
	
	
	public void  printElem(Element elem){
		System.out.print(elem.tagName());
		for (String className : elem.classNames()){
			System.out.print("." + className);
		}
		if (!elem.attr("id").equals("")){
			System.out.print("#" + elem.attr("id"));
		}
		for (Attribute attr : elem.attributes()){
			if (!attr.getKey().equals("class") && !attr.getKey().equals("id")){
				System.out.print(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
			}
		}
		System.out.println("");
	}
	
	public float matchInnerText(Element first, Element second){
		return first.html().equals(second.html()) ? 1.0f : 0f;
	}
	
	public float matchInnerHtml(Element first, Element second){
		return first.text().equals(second.text()) ? 1.0f : 0f;		
	}
	
	public float matchIndex(Element first, Element second){
		return first.siblingIndex() == second.siblingIndex() ? 1f : 0f;
	}
	
	public float matchTagName(Element first, Element second){
		return first.tagName().toUpperCase().equals(second.tagName().toUpperCase()) ? 1f : 0f;
	}

	public Float evaluateClass(Element first, Element second){
		int	  sameClassNb = 0;
		
		if (first.classNames().size() == 0 || second.classNames().size() == 0)
			return 0f;
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
		System.out.println("Comparison");
		this.printElem(first);
		this.printElem(second);
		if (first.tagName() != second.tagName()){
			System.out.println("0");
			return 0f;
		}
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
					if (attr.getKey().equals(attr2.getKey()) && attr.getValue().equals(attr2.getValue())){
						Float weight = map.get(attr.getKey());
						if (weight != null)
							nodeWeight += weight;
						else{
							nodeWeight += map.get("other");
							System.out.println("added as other");
						}
						break;
					}
				}
			}
		}
		System.out.println(nodeWeight);
		return nodeWeight;
	}
}
