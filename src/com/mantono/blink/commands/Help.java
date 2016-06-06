package com.mantono.blink.commands;

public class Help implements Invokable
{
	@Override
	public void invoke(String[] args)
	{
		for(Command cmd : Command.values())
		{
			System.out.println(cmd.name().toLowerCase() + ", " + "-" + cmd.getFlag());
			System.out.println("\t" + cmd.getDescription() + "\n");
		}
	}
}
