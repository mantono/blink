package blink;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Set;

public class Bookmark implements Comparable<Bookmark>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -94350200740463415L;
	private final URL url;
	private final LocalDateTime timestamp;
	private final Set<String> tags;
	
	public Bookmark()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Bookmark other)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
