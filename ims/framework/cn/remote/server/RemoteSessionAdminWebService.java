/*
 * This code was generated
 * Copyright (C) 1995-2004 IMS MAXIMS plc. All rights reserved.
 * IMS Development Environment (version 1.60 build 2865.28611)
 * WARNING: DO NOT MODIFY the content of this file
 * Generated: 06/11/2007, 11:33
 *
 */
package ims.framework.cn.remote.server;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import ims.domain.admin.AppSession;
import ims.framework.cn.SessionManager;
import ims.framework.cn.remote.RemoteSessionAdmin;

public class RemoteSessionAdminWebService extends ims.domain.impl.DomainWebService implements RemoteSessionAdmin,ServiceLifecycle 
{
	private ServletEndpointContext jaxrpcContext;

	public RemoteSessionAdminWebService()
	{
		super();
	}

	public String login(String username, String password) throws RemoteException
	{
		try
		{
			return super.login(username, password);
		}
		catch (Exception e) 
		{
			throw new RemoteException("Web Service login failed ",e);
		}
	}

	public void logout()
	{
		super.logout();
	}

	public void logout(String sessionToken)
	{
		super.logout(sessionToken);
	}
	
	public String[] listSessionsWS() throws RemoteException
	{
		if (!loggedIn())
			throw new RemoteException("Not logged in!!");
		String sessionID = jaxrpcContext.getHttpSession().getId();
		return SessionManager.getLocalSessions( sessionID);
/*		String [] rtn = new String[sessions.length*11];
		int rtncounter = 0;
		for (AppSession session : sessions)
		{
			rtn[rtncounter++]=session.getSessionId();
			rtn[rtncounter++]=session.getUserName();
			rtn[rtncounter++]=session.getRealName();
			rtn[rtncounter++]=session.getRoleName();
			rtn[rtncounter++]=session.getStartTime().toString();
			rtn[rtncounter++]=Long.toString(session.getIdleTime());
			rtn[rtncounter++]=Integer.toString(session.getSessionTimeOut());
			rtn[rtncounter++]=session.getCurrentForm();
			rtn[rtncounter++]=session.getRemoteHost();
			rtn[rtncounter++]=session.getRemoteAddress();
			rtn[rtncounter++]=Boolean.toString(session.loggingEnabled());

		}
		return rtn;
*/
	}

	public void terminateSessionWS(	String sessionID) throws RemoteException
	{
		if (!loggedIn())
			throw new RemoteException("Not logged in!!");
		SessionManager.endLocalSession(sessionID);
	}

	public String toggleLoggingWS(String sessionID) throws RemoteException
	{
		if (!loggedIn())
			throw new RemoteException("Not logged in!!");

		AppSession session = SessionManager.getSession(sessionID);
		if(session.loggingEnabled())
			session.disableSessionLogging();
		else
			session.enableSessionLogging();
		return sessionID;
	}

	public String getLogFileWS(	String sessionID, Integer tailSize) throws RemoteException
	{
		if (!loggedIn())
			throw new RemoteException("Not logged in!!");
		return null;
	}

	public void destroy()
	{
		jaxrpcContext = null;
	}

	public void init(Object context) throws ServiceException
	{
		jaxrpcContext = (ServletEndpointContext) context;

		
	}
}
