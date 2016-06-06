package com.mantono.blink.commands;

public enum Command
{
	ADD('a', "Add a bookmark"),
	EDIT('e', "Edit an existing bookmark"),
	SEARCH('s', "Search for bookmarks based on labels"),
	DELETE('d', "Delete a bookmark"),
	HELP('h', "Show this help text");

	private final char flag;
	private final String description;

	private Command(final char flag, final String description)
	{
		this.flag = flag;
		this.description = description;
	}
	
	public char getFlag()
	{
		return flag;
	}
	
	public Invokable getInvokableClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		final String packageName = getClass().getPackage().getName();
		Class<?> foundClass = Class.forName(packageName + "." + getClassName());
		boolean invokable = false;
		for(Class<?> foundInterface : foundClass.getInterfaces())
			if(foundInterface.equals(Invokable.class))
				invokable = true;
		if(!invokable)
			throw new IllegalStateException("Found " + foundClass
					+ " does not implment the interface Invokable.");

		@SuppressWarnings("unchecked")
		Class<Invokable> invokableClass = (Class<Invokable>) foundClass;

		return invokableClass.newInstance();
	}

	private String getClassName()
	{
		final String loweCaseName = this.name().toLowerCase();
		char firstLetter = Character.toUpperCase(loweCaseName.charAt(0));
		final String className = firstLetter + loweCaseName.substring(1);
		return className;
	}

	public String getDescription()
	{
		return description;
	}
}
