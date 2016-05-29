package com.mantono.blink;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Index implements Serializable
{
	private static final Path ROOT = Paths.get(System.getProperty("user.home") + "/.blink");
	private static final File DIR = new File(ROOT + "/bookmarks");
	private Instant indexLastBuilt;
	
	public Set<Bookmark> findBookmarks()
	{
		Set<Bookmark> assets = new HashSet<Bookmark>();
		if(DIR.exists())
		{
			if(indexLastBuilt == null)
				loadFiles(DIR, assets);
			else if(DIR.lastModified() > indexLastBuilt.toEpochMilli())
				loadFiles(DIR, assets);
		}
		else
		{
			System.err.println("Folder " + DIR + " does not exist.");
			System.exit(1);
		}
		indexLastBuilt = Instant.now();
		return assets;
	}
	
	public static void main(String[] args)
	{
		Set<Bookmark> bms = new Index().findBookmarks();
		for(Bookmark bookmark : bms)
		{
			Set<String> labels = bookmark.getLabels();
			for(String label : args)
			{
				if(labels.contains(label))
				{
					System.out.println(bookmark.getUrl() + " : " + bookmark.getLabels());
					break;
				}
			}
		}
	}

	private void loadFiles(File dir, Set<Bookmark> assets)
	{
		File[] directoryListing = dir.listFiles();
		if(directoryListing != null)
		{
			for(File file : directoryListing)
			{
				if(file.isDirectory())
					loadFiles(file, assets);
				else
				{
					Bookmark bookmark = parseBookmarkFile(file);
					assets.add(bookmark);
				}
			}
		}
	}

	private Bookmark parseBookmarkFile(File file)
	{
		try
		{
			List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
			final URL url = new URL(lines.remove(0));
			final String timestampData = lines.remove(0);
			final long timestamp = Long.parseLong(timestampData);
			Set<String> labels = parseLabels(lines);
			return new Bookmark(url, timestamp, labels);
		}
		catch(IOException e)
		{
			System.err.println("Error reading " + file.getAbsolutePath());
			e.printStackTrace();
		}
		return null;
	}

	private Set<String> parseLabels(List<String> lines)
	{
		Set<String> labels = new HashSet<String>(lines.size());
		for(String line : lines)
			for(String label : line.split(","))
				labels.add(label.trim());
		
		return labels;
	}
}
