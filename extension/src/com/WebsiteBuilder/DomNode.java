package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/



import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomNode {
    protected Node node;
    protected int deepness;
    protected ArrayList<DomNode> inner;

    private static final Map<String, Float> attrs;

    static {
        attrs = new HashMap<String, Float>();
        attrs.put("id",5.0f);
        attrs.put("class", 3.0f);
        attrs.put("href", 2.0f);
        attrs.put("type", 3.0f);
    }


    public DomNode(Node node, int deepness) {
        this.node = node;
        this.deepness = deepness;
        this.inner = new ArrayList<>();
        for (int i = 0; i < node.childNodes().size(); i++) {
            inner.add(new DomNode(node.childNode(i), deepness + 1));
        }
    }

    public void print() {
        for (int i = 0; i < deepness; i++) {
            System.out.print(" ");
        }

        String attrs = "";
        for (Attribute attr : node.attributes().asList()) {
            attrs += "|" + attr.getKey() + ":" + attr.getValue();
        }
        System.out.println(node.nodeName() + attrs);
        for (DomNode anInner : this.inner) {
            anInner.print();
        }
    }

    public void addChild(DomNode domNode) {
        this.inner.add(domNode);
    }

    public boolean equal(DomNode other) {
        if (!this.node.nodeName().equals(other.node.nodeName())) {
            return false;
        }
        if (this.deepness != other.deepness) {
            return false;
        }
        float score = 0.0f;
        for (Map.Entry<String, Float> entry : attrs.entrySet()) {
            if (this.node.attr(entry.getKey()).equals(other.node.attr(entry.getKey()))) {
                score += entry.getValue();
            }
        }
        if (score < 2.0) {
            return false;
        }

        return true;
    }

    public DomNode getEgalityContent(DomNode other) {
        DomNode egality = new DomNode(this.node.clone(), this.deepness);
        for (Attribute attr : egality.node.attributes().asList()) {
            if (!other.node.attr(attr.getKey()).equals(attr.getValue())) {
                egality.node.removeAttr(attr.getKey());
            }
        }
        int lastFind = 0;
        for (int i = 0; i < node.childNodes().size(); i++) {
            for (int j = lastFind; j < other.node.childNodes().size(); j++) {
                if (this.inner.get(i).equals(other.inner.get(j))) {
                    egality.addChild(this.inner.get(i).getEgalityContent(other.inner.get(j)));
                    lastFind = j;
                    break;
                }
            }
        }
        return egality;
    }

    public void attrs(Map<String, Map<String, Integer>> attrs) {
        for (Attribute attr : node.attributes().asList()) {
            if (attrs.get(attr.getKey()) != null) {
                if (attrs.get(attr.getKey()).get(attr.getValue()) != null) {
                    attrs.get(attr.getKey()).put(attr.getValue(), attrs.get(attr.getKey()).get(attr.getValue()) + 1);
                }
            } else {
                Map<String, Integer> map =  new HashMap<String, Integer>();
                map.put(attr.getValue(), 1);
                attrs.put(attr.getKey(), map);
            }
        }
        for (DomNode anInner : this.inner) {
            anInner.attrs(attrs);
        }
    }
}
