package com.mantono.blink;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.junit.Test;

public class BookmarkTest
{
	@Test
	public void testGetDomainBasicUrl() throws MalformedURLException
	{
		final URL url = new URL("http://mantono.com");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("mantono.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainInLowerCase() throws MalformedURLException
	{
		final URL url = new URL("http://MANTONO.COM");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("mantono.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainNoLeadingWww() throws MalformedURLException
	{
		final URL url = new URL("http://www.mantono.com");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("mantono.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainUrlWithResource() throws MalformedURLException
	{
		final URL url = new URL("http://mantono.com/cant-touch-me");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("mantono.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainUrlWithSubdomain() throws MalformedURLException
	{
		final URL url = new URL("http://sub.mantono.com");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("sub.mantono.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainWithNumber() throws MalformedURLException
	{
		final URL url = new URL("http://nord59.se");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("nord59.se", bookmark.getDomain());
	}

	@Test
	public void testGetDomainWithHyphen() throws MalformedURLException
	{
		final URL url = new URL("http://a-hyphen.com");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("a-hyphen.com", bookmark.getDomain());
	}

	@Test
	public void testGetDomainWithPort() throws MalformedURLException
	{
		final URL url = new URL("http://mantono.com:80");
		Bookmark bookmark = new Bookmark(url, 1L, new ArrayList<String>(0));
		assertEquals("mantono.com", bookmark.getDomain());
	}

}