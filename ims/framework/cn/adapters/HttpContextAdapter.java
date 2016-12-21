package ims.framework.cn.adapters;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import ims.framework.interfaces.IContextAdapter;

public class HttpContextAdapter implements IContextAdapter
{ 
	private transient HttpSession session;
	
	public HttpContextAdapter(HttpSession session)
	{
		this.session = session;
	}
	
	public Object getAttribute(String key)
	{
		return session.getAttribute(key);
	}
	public void setAttribute(String key, Object value)
	{
		session.setAttribute(key, value);
	}
	public void removeAttribute(String key)
	{
		session.removeAttribute(key);
	}
	@SuppressWarnings("unchecked")
	public Iterator getAttributeNames()
	{
		List result = new ArrayList<String>();
		Enumeration names = session.getAttributeNames();
		while(names.hasMoreElements())
		{
			result.add(names.nextElement());
		}		
		return result.iterator();
	}
	public boolean isDomainOnlySession()
	{
		return false;
	}	
}
