package ims.framework.cn.events;

import java.io.Serializable;

public class SearchResultSelectionEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public SearchResultSelectionEvent(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return this.id;
	}
	private int id;	
}
