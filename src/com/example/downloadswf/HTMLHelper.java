package com.example.downloadswf;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class HTMLHelper {
	
	TagNode rootNode;
	
	public HTMLHelper(URL htmlPage) throws IOException{
		// TODO Auto-generated constructor stub
		HtmlCleaner cleaner = new HtmlCleaner();
		rootNode = cleaner.clean(htmlPage, "windows-1251");
	}
	
	List<TagNode> getLinksByClass(String CSSClassName){
		List<TagNode> linkList = new ArrayList<TagNode>();
		TagNode linkElements[] = rootNode.getElementsByName("img", true);
		for (int i = 0; linkElements != null && i < linkElements.length; i++){
			String classType = linkElements[i].getAttributeByName("class");
			if (classType != null && classType.equals(CSSClassName))
				linkList.add(linkElements[i]);
		}
		return linkList;
	}

	public int getCountLinksByClass(){
		return rootNode.getElementsByName("a", true).length;
	}
}
