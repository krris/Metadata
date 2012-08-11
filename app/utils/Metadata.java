package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Holds ectracted metadata.
 * Every data is String. If there is more than one data of one type 
 * it should be stored in List<String> (for example: authors). 
 * 
 */
public class Metadata 
{
	private String filename;
	private List<String> authors = new ArrayList<String>();
	private List<String> rest = new ArrayList<String>(); 
	
	public Metadata()
	{
		// sample autohors etc...
		authors.add("author1");
		authors.add("author2");
		authors.add("author3");
		
		rest.add("something 1");
		rest.add("something 2");
	}
	
	/**
	 * Return a map of metadata. It helps in displaying the extracted 
	 * metada in a table on a website. 
	 */
	public Map<String, List<String>> getAllMetadataAsMap()
	{
		SortedMap<String, List<String>> map = new TreeMap<String, List<String>>();
		
		List<String> filenameList = new ArrayList<String>();
		filenameList.add(filename);
		map.put("filename", filenameList);
		
		map.put("author", authors);
		map.put("rest", rest);
				
		return map;
	}

	public String getFilename() 
	{
		return filename;
	}

	public void setFilename(String filename) 
	{
		this.filename = filename;
	}

	public void setAuthors(List<String> authors) 
	{
		this.authors = authors;
	}

	public void setRest(List<String> rest) 
	{
		this.rest = rest;
	}
}
