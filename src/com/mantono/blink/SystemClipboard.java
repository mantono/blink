package com.mantono.blink;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemClipboard
{
	public static void main(String[] args) throws InterruptedException
	{
		final String input = args[0];
		StringSelection selection = new StringSelection(input);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		try
		{
			Process process = Runtime.getRuntime().exec("echo "+ input +"|xclip -selection clipboard");
            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));
            String s = null;
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		System.out.println("Copied " + input + " to clipboard.");
	}
}
