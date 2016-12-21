package ims.framework.cn.events;

import java.io.Serializable;

public class SmartcardTicketReceivedEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public SmartcardTicketReceivedEvent(String ticket, String lastError)
	{
		this.ticket = ticket;	
		this.lastError = lastError;
	}
	public String getTicket()
	{
		return this.ticket;
	}
	public String getLastError()
	{
		return this.lastError;
	}
	
	private String ticket;
	private String lastError;
}
