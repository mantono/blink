package com.mantono.blink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Index implements Serializable
{
	private static final Path ROOT = Paths.get(System.getProperty("user.home") + "/.blink");
	private static final File DIR = new File(ROOT + "/bookmarks");
	private static final File SAVED_INDEX = new File(ROOT + "/.index");
	private final Set<Bookmark> bookmarks;
	private int loadedHashCode = 0;
	private Instant indexLastBuilt;
	
	public Index() throws IOException
	{
		this.bookmarks = new HashSet<Bookmark>();
		this.indexLastBuilt = Instant.ofEpochMilli(0);
	}
	
	public int size()
	{
		return bookmarks.size();
	}
	
	public Set<Bookmark> findBookmarks()
	{
		if(DIR.exists())
		{
			if(DIR.lastModified() > indexLastBuilt.toEpochMilli())
				loadFiles(DIR, bookmarks);
			else
				return bookmarks;
		}
		else
		{
			System.err.println("Folder " + DIR + " does not exist.");
			System.exit(1);
		}
		indexLastBuilt = Instant.now();
		
		return bookmarks;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		Index index;
		
		if(SAVED_INDEX.exists())
			index = loadIndex();
		else
			index = new Index();
		
		Set<Bookmark> bms = index.findBookmarks();
		
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
		
		if(index.isUpdatedSinceLoad())
			saveIndex(index);
	}

	private boolean isUpdatedSinceLoad()
	{
		return bookmarks.hashCode() != loadedHashCode;
	}

	private static void saveIndex(Index index) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(SAVED_INDEX);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(index);
	}

	private static Index loadIndex() throws IOException, ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream(SAVED_INDEX);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Index index = (Index) ois.readObject();
		index.updateHashCode();
		return index;
	}

	private void updateHashCode()
	{
		this.loadedHashCode = bookmarks.hashCode();
	}

	private Set<Bookmark> loadFiles(File dir, Set<Bookmark> bookmarks)
	{
		File[] directoryListing = dir.listFiles();
		if(directoryListing != null)
		{
			for(File file : directoryListing)
			{
				if(file.isDirectory())
					loadFiles(file, bookmarks);
				else
				{
					Bookmark bookmark = parseBookmarkFile(file);
					if(bookmark != null)
						bookmarks.add(bookmark);
				}
			}
		}
		
		return bookmarks;
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
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("Illegal file " + file + " ignored.");
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
