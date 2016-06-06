package com.mantono.blink.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.mantono.blink.Bookmark;
import com.mantono.blink.InputReader;

public class Add implements Invokable
{

	@Override
	public void invoke(String[] args)
	{
		InputReader input = new InputReader();
		String urlAsString = "";
		try
		{
			urlAsString = input.readString("URL");
			final URL url = new URL(urlAsString);
			
			final List<String> labels = input.readStrings("Labels");
			Bookmark bookmark = new Bookmark(url, labels);
			saveBookmark(bookmark);
		}
		catch(MalformedURLException e)
		{
			if(urlAsString.isEmpty())
				System.err.println("The given URL was empty.");
			else
				System.err.println("URL " +  urlAsString + " is not a valid URL.");
		}
		catch(NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean saveBookmark(Bookmark bookmark) throws NoSuchAlgorithmException, FileNotFoundException, IOException
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
			printStream.print(bookmark.getUrl().toString() + "\n");
			printStream.print(bookmark.getTimestamp() + "\n");
			for(String label : bookmark.getLabels())
				printStream.print(label + "\n");
			printStream.flush();
		}
		return true;
	}

}
