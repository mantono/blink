package com.mantono.blink.importing;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser
{
	private final static String URL_LEGAL_CHARACTERS = "[\\w+\\-\\.]";
	private final static String PROTOCOL = "http[s]?://";
	private final static String RESOURCE_LEGAL_CHARACTERS = "[/\\w-\\.\\?\\=\\#\\~\\%\\@\\,\\!\\:\\;\\+\\&åäöÅÄÖ\\|\\*\\(\\)]*";
	private final static String URL = "\""+PROTOCOL+URL_LEGAL_CHARACTERS+"+\\w{2,}"+RESOURCE_LEGAL_CHARACTERS+"\"";
	private final static String DATE = "ADD_DATE=\"\\d+\"";
	private final static String LABELS = "TAGS=\".+\"";
	private final String line;
	
	public LineParser(final String line)
	{
		this.line = line;
	}
	
	public java.net.URL getUrl() throws MalformedURLException
	{
		Pattern pattern = Pattern.compile(URL);
		Matcher matcher = pattern.matcher(line);
		if(!matcher.find())
			throw new IllegalArgumentException(line);
		final int start = matcher.start();
		final int end = matcher.end();
		String cleaned = line.subSequence(start, end).toString();
		cleaned = cleaned.replaceAll("\"", "");
		return new java.net.URL(cleaned);
	}
	
	long getTimestamp()
	{
		Pattern pattern = Pattern.compile(DATE);
		Matcher matcher = pattern.matcher(line);
		if(!matcher.find())
			throw new IllegalArgumentException(line);
		final int start = matcher.start();
		final int end = matcher.end();
		String str = line.subSequence(start, end).toString();
		str = str.replaceAll("ADD_DATE=", "");
		str = str.replaceAll("\"", "");
		return Long.parseLong(str);
	}
	
	Set<String> getLabels()
	{
		Pattern pattern = Pattern.compile(LABELS);
		Matcher matcher = pattern.matcher(line);
		if(!matcher.find())
		{
			System.err.println("Found no labls in " + line);
			return new HashSet<String>(0);
		}
		final int start = matcher.start();
		final int end = matcher.end();
		String str = line.subSequence(start, end).toString();
		str = str.replaceAll("TAGS=", "");
		str = str.replaceAll("\"", "");
		return new HashSet<String>(Arrays.asList(str.split(",")));
	}
}
