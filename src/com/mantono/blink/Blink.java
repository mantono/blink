package com.mantono.blink;

import java.util.Arrays;

import com.mantono.blink.commands.Command;
import com.mantono.blink.commands.Invokable;

public class Blink
{
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if(args.length == 0)
		{
			System.err.println("No argument given. Try 'help' or '-h' for available options.");
			System.exit(1);
		}
		
		try
		{
			final Command command = Command.valueOf(args[0].toUpperCase());
			Invokable invCommand = command.getInvokableClass();
			args = removeFirstElement(args);
			invCommand.invoke(args);
		}
		catch(IllegalArgumentException exception)
		{
			System.err.println("'" + args[0] + "' is not a valid command, try help or -h for available commands.");
			System.exit(2);
		}
	}

	private static String[] removeFirstElement(String[] args)
	{
		if(args.length == 1)
			return new String[]{};
		return Arrays.copyOfRange(args, 1, args.length);
	}
}
