package ims.framework;

import javax.servlet.http.HttpSession;

import ims.framework.enumerations.ContextQueryItem;

public class ContextQuery 
{
	private transient HttpSession session;
	private transient Context context;
	
	public ContextQuery(HttpSession session)
	{
		this.session = session;
		this.context = new ims.framework.cn.Context(session);
	}
	public boolean isNull(ContextQueryItem item) 
	{		
		return session.getAttribute(item.getKey()) == null;
	}
	public void clear(ContextQueryItem item)
	{
		context.put(item.getKey(), null);
		session.removeAttribute(item.getKey());
	}
}
