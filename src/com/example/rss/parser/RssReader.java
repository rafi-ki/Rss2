package com.example.rss.parser;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import at.diamonddogs.service.processor.XMLProcessor;

public class RssReader extends XMLProcessor <String[]>
{
	public static final int ID = 2930239;
	
	@Override
	protected String[] parse(Document inputObject) {
		//TODO write to Feed (parser package)
		
		//for now just the title is added
		NodeList nodeList = inputObject.getElementsByTagName("title");
		String[] ret = new String[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			ret[i] = nodeList.item(i).getTextContent();
		}
		return ret;
	}

	@Override
	public int getProcessorID() {
		return ID;
	}
}
