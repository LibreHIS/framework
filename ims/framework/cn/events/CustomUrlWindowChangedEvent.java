package ims.framework.cn.events;

import ims.framework.CustomEvent;

public class CustomUrlWindowChangedEvent extends CustomEvent
{
	private static final long serialVersionUID = 1L;

	public CustomUrlWindowChangedEvent(String eventName)
	{
		this.eventName = eventName;		
	}
	public String getEventName()
	{
		return this.eventName;
	}
	
	private String eventName;	
}
