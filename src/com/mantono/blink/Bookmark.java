package com.mantono.blink;

import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bookmark implements Comparable<Bookmark>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -94350200740463415L;
	public static final String DOMAIN = "\\b[\\w\\-+.]*\\w+\\.\\w{2,}";
	private final URL url;
	private final String domain;
	private final long timestamp;
	private final Set<String> labels;

	public Bookmark(final URL url, final long timestamp, Collection<String> labels)
	{
		this.url = url;
		if(timestamp > Instant.now().getEpochSecond())
			throw new DateTimeException("Timestamp for bookmark is in the future: " + timestamp);
		this.timestamp = timestamp;
		this.labels = labelsWithLowerCase(labels);
		this.domain = parseDomain();
	}
	
	private Set<String> labelsWithLowerCase(Collection<String> labels)
	{
		Set<String> labelsLower = new HashSet<String>(labels.size());
		for(String label : labels)
			labelsLower.add(label.toLowerCase());
		
		return labelsLower;
	}

	public Bookmark(final URL url, Collection<String> labels)
	{
		this(url, Instant.now().toEpochMilli()/1000, labels);
	}

	private String parseDomain()
	{
		final Pattern domain = Pattern.compile(DOMAIN);
		final Matcher matcher = domain.matcher(url.toString());
		if(!matcher.find())
			throw new IllegalArgumentException("URL of \"" + url + "\" does not seem to contain a proper domain name.");
		final int start = matcher.start();
		final int end = matcher.end();
		final String dom = url.toString().substring(start, end);
		final String lowerCaseDomain = dom.toLowerCase();
		return lowerCaseDomain.replaceFirst("www.", "");
	}

	public URL getUrl()
	{
		return url;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public Set<String> getLabels()
	{
		return labels;
	}

	public boolean addLabel(final String label)
	{
		return labels.add(label);
	}

	public boolean removeLabel(final String label)
	{
		return labels.remove(label);
	}

	public Set<String> getCommonLabels(Bookmark bookmark)
	{
		Set<String> common = new HashSet<String>(bookmark.getLabels());
		common.retainAll(labels);
		return common;
	}

	public String getDomain()
	{
		return domain;
	}

	public CharSequence getHash() throws NoSuchAlgorithmException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		final String urlText = url.toString();
		byte[] hash = digest.digest(urlText.getBytes(StandardCharsets.UTF_8));
		StringBuffer hexString = new StringBuffer();

		for(int i = 0; i < hash.length; i++)
		{
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}

	@Override
	public boolean equals(Object object)
	{
		if(object == null)
			return false;
		if(!(object instanceof Bookmark))
			return false;
		
		final Bookmark other = (Bookmark) object;
		
		return this.url.toString().equals(other.url.toString());
	}

	@Override
	public int hashCode()
	{
		return url.toString().hashCode();
	}

	@Override
	public int compareTo(Bookmark other)
	{
		return (int) (this.timestamp - other.timestamp);
	}

}
