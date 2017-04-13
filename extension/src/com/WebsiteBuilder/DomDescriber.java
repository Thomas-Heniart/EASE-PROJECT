package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DomDescriber {

    protected DomNode body;
    protected Map<String, Map<String, Integer>> attrs;


    public DomDescriber(String dom) {
        Document doc = Jsoup.parse(dom);
        body = new DomNode(doc.childNode(0), 0);
        attrs = new HashMap<String, Map<String, Integer>>();
        body.attrs(attrs);

    }

    private DomDescriber(DomNode body) {
        this.body = body;
        attrs = new HashMap<String, Map<String, Integer>>();
        this.body.attrs(attrs);
    }

    public void print() {
        body.print();
    }

    public void printAttrs() {
        for (Map.Entry<String, Map<String, Integer>> entry : attrs.entrySet()) {
            System.out.println("===" + entry.getKey() + "===");
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                System.out.println(entry2.getKey() + " " + entry2.getValue());
            }
        }
    }

    public DomDescriber getEgality(DomDescriber other) {
        return new DomDescriber(body.getEgalityContent(other.body));
    }

    public Map<String, List<String>> getAttrsNotIn(DomDescriber other) {
        Map<String, List<String>> notIn = new HashMap<String, List<String>>();
        for (Map.Entry<String, Map<String, Integer>> entry : attrs.entrySet()) {

            if (other.attrs.get(entry.getKey()) != null) {
                Map<String, Integer> otherAttrValues = other.attrs.get(entry.getKey());
                Map<String, Integer> attrValues = attrs.get(entry.getKey());
                for (Map.Entry<String, Integer> entry2 : attrValues.entrySet()) {
                    if (otherAttrValues.get(entry2.getKey()) == null) {
                        notIn.computeIfAbsent(entry.getKey(), k -> new LinkedList<>());
                        notIn.get(entry.getKey()).add(entry2.getKey());
                    }
                }
            } else {
                List<String> values = new LinkedList<String>();
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    values.add(entry2.getKey());
                }
                notIn.put(entry.getKey(), values);
            }
        }
        return notIn;
    }
}
