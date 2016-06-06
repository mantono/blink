package com.mantono.blink.commands;

import java.io.IOException;

import com.mantono.blink.Index;

public class Search implements Invokable
{

	@Override
	public void invoke(String[] args)
	{
		try
		{
			Index index = Index.getIndex();
			index.search(args);
		}
		catch(ClassNotFoundException e)
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

}
