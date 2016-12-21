package ims.framework.cn;

import ims.framework.cn.events.AppParamEvent;
import ims.framework.cn.events.SecurityTokenLoginEvent;
import ims.framework.cn.events.SmartcardTicketReceivedEvent;
import ims.framework.cn.events.UpdateTheme;

import java.util.ArrayList;

public class EventManager
{
	public EventManager()
	{
	}	
	
	public void clear()
	{
		events.clear();
	}
	public int size()
	{
		return events.size();
	}
	public Object get(int index)
	{
		return events.get(index);
	}
	public void add(Object event)
	{
		events.add(event);
	}
	public boolean hasLoginRequestEvent()
	{
		if(hasSecurityTokenLoginRequestEvent())
			return false;
		
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof UpdateTheme)
				return true;
		}
		return false;
	}
	public boolean hasSecurityTokenLoginRequestEvent()
	{
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof SecurityTokenLoginEvent)
				return true;
		}
		return false;
	}
	public boolean hasAppParamEvent()
	{
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof AppParamEvent)
				return true;
		}
		return false;
	}
	public AppParamEvent getAppParamEvent()
	{
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof AppParamEvent)
				return (AppParamEvent)events.get(x);
		}
		return null;
	}
	public boolean hasSmartcardTicketReceivedEvent()
	{
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof SmartcardTicketReceivedEvent)
				return true;
		}
		return false;
	}
	public SmartcardTicketReceivedEvent getSmartcardTicketReceivedEvent()
	{
		for(int x = 0; x < events.size(); x++)
		{
			if(events.get(x) instanceof SmartcardTicketReceivedEvent)
				return (SmartcardTicketReceivedEvent)events.get(x);
		}
		return null;
	}			
	public boolean isEmptyRequest()
	{
		return emptyRequest;
	}
	public void setIsEmptyRequest(boolean value)
	{
		emptyRequest = value;
	}
	
	private ArrayList<Object> events = new ArrayList<Object>();
	private boolean emptyRequest = false;
}
