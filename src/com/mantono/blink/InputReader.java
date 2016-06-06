package com.mantono.blink;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReader
{
	private final Scanner scanner;
	
	public InputReader()
	{
		this.scanner = new Scanner(System.in);
	}
	
	public InputReader(final Scanner input)
	{
		this.scanner = input;
	}

	public String readString(final String prompt)
	{
			System.out.print(prompt + ": ");
			return scanner.nextLine().trim();
	}
	
	public List<String> readStrings(final String prompt)
	{
		System.out.print(prompt + ": ");
		final String labelLine = scanner.nextLine();
		final String parsedLine = labelLine.replaceAll(",", " ");
		final String[] labels = parsedLine.split("\\s+");
		return Arrays.asList(labels);
	}
}
