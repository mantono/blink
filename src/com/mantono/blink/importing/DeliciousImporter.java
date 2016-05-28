package com.mantono.blink.importing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import com.mantono.blink.Bookmark;

public class DeliciousImporter
{
	private final File data;

	public DeliciousImporter(final File data)
	{
		this.data = data;
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		if(args.length == 0)
		{
			System.err.println("Error: Needs an HTML file as argument to import data from.");
			System.exit(1);
		}
		DeliciousImporter di = new DeliciousImporter(new File(args[0]));
		System.out.println(di.importData());
	}

	public int importData() throws IOException, NoSuchAlgorithmException
	{
		int imported = 0;
		List<String> lines = Files.readAllLines(data.toPath(), Charset.defaultCharset());
		for(String line : lines)
		{
			if(!line.contains("A HREF="))
				continue;
			line = line.replaceAll("(<DT><)|(>[^>]*</A>)", "");
			final Bookmark bookmark = createBookmark(line);
			if(saveBookmark(bookmark))
			{
				System.out.println(line);
				imported++;
			}
		}
		return imported;
	}

	private Bookmark createBookmark(String line) throws MalformedURLException
	{
		LineParser parser = new LineParser(line);
		final URL url = parser.getUrl();
		final long timestamp = parser.getTimestamp();
		final Set<String> labels = parser.getLabels();
		return new Bookmark(url, timestamp, labels);
	}

	private boolean saveBookmark(Bookmark bookmark) throws NoSuchAlgorithmException, FileNotFoundException, IOException
	{
		final CharSequence hash = bookmark.getHash();
		final String domain = bookmark.getDomain();
		final File folder = new File(System.getProperty("user.home") + "/.blink/bookmarks/" + domain);
		if(!folder.exists())
			Files.createDirectories(folder.toPath());
		final File file = new File(folder.getAbsolutePath() + "/" + hash);
		if(file.exists())
			return false;
		else
			file.createNewFile();
		try(FileOutputStream fileStream = new FileOutputStream(file);
			PrintStream printStream = new PrintStream(fileStream);)
		{
			printStream.print(bookmark.getUrl().toString()+"\n");
			printStream.print(bookmark.getTimestamp()+"\n");
			for(String label : bookmark.getLabels())
				printStream.print(label+"\n");
			printStream.flush();
		}
		return true;
	}
}
