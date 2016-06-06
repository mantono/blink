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
import com.mantono.blink.InputReader;

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
			if(InputReader.saveBookmark(bookmark))
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
}
