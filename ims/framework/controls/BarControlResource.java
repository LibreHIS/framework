package ims.framework.controls;

import java.io.Serializable;

public class BarControlResource implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public BarControlResource(int total, int completed, String message)
	{
		this.total = total;
		this.completed = completed;
		this.message = message;
	}
	
	public int getTotal()
	{
		return this.total;
	}
	public int getCompleted()
	{
		return this.completed;
	}
	public String getMessage()
	{
		return this.message;
	}
	
	private int total;
	private int completed;
	private String message;
}
