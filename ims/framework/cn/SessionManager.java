package ims.framework.cn;

import ims.configuration.ConfigFlag;
import ims.configuration.EnvironmentConfig;
import ims.domain.admin.AppSession;
import ims.domain.http.HttpAppSession;
import ims.framework.cn.remote.client.RemoteSessionAdminHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionManager implements HttpSessionListener, ServletContextListener,  Serializable
{
	private static final long serialVersionUID = 1L;
	private static ConcurrentMap<String, HttpAppSession> sessions = new ConcurrentHashMap<String, HttpAppSession>();
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(SessionManager.class);

	public SessionManager()
	{
		super();		
	}

	public void contextInitialized(ServletContextEvent sce)
	{
		LocalLogger.info("Context initialised. " + sce.getServletContext().getServletContextName());
		//Not really interested in this event.
	}

	public void contextDestroyed(ServletContextEvent sce)
	{
		//Hope to use this event to close Hibernate Session Factory
		//when app is re-deployed.
		//Registry.getInstance().closeSessionFactory();
		LocalLogger.info("Context destroyed." + sce.getServletContext().getServletContextName());		
	}

	public void sessionCreated(HttpSessionEvent se)
	{
		sessions.putIfAbsent(se.getSession().getId(),new HttpAppSession(se.getSession()));
		LocalLogger.info("Session added. Count is " + sessions.size());
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
		sessions.remove(se.getSession().getId());
		LocalLogger.info("Session removed. Count is " + sessions.size());
	}
	
	public static int getSessionCount()
	{
		return sessions.size();		
	}

	public static String[] getSessions(String userName, String clearPassword)
	{
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		if(null==clusterNodes||null==clusterNodes[0]||"".equals(clusterNodes[0]))
		{
			return getLocalSessions();
		}
		return RemoteSessionAdminHelper.getRemoteSessions(userName,clearPassword);
	}

	public static String[] getLocalSessions()
	{
		return getLocalSessions(null);
	}
	
	/**
	 * returns all the session details except the session identified by the parameter
	 * the UI does not want to see the web service session created to get the list of sessions!
	 * @param excludeSession a session ID
	 * @return
	 */
	public static String[] getLocalSessions(String excludeSession)
	{

			List<String> sessionDetailsList = new ArrayList<String>();
			String [] rtn = {}; //always return something
			try
			{
				Iterator<String> it = sessions.keySet().iterator();
				while (it.hasNext())
				{
					String key = it.next();
					if(key.equals(excludeSession))
						continue;
					AppSession session = sessions.get(key);
					if(null==session.getUserName()||("".equals(session.getUserName())))
						continue;
					sessionDetailsList.add(session.getSessionId());
					sessionDetailsList.add(session.getUserName());
					sessionDetailsList.add(session.getRealName());
					sessionDetailsList.add(session.getRoleName());
					if (null==session.getStartTime())
					{
						sessionDetailsList.add(null);
					} else 
					{
						sessionDetailsList.add(session.getStartTime().toString());
					}
					sessionDetailsList.add(Long.toString(session.getIdleTime()));
					sessionDetailsList.add(Integer.toString(session.getSessionTimeOut()));
					sessionDetailsList.add(session.getCurrentForm());
					sessionDetailsList.add(session.getRemoteHost());
					sessionDetailsList.add(session.getRemoteAddress());
					sessionDetailsList.add(Boolean.toString(session.loggingEnabled()));
					sessionDetailsList.add(ConfigFlag.HOST_NAME.getValue());  // BW
				}
				rtn = sessionDetailsList.toArray(new String[0]);
			}
			
			catch (RuntimeException e)
			{
				LocalLogger.warn("Exception getting session data", e);
			}
			return rtn;
	}


	
	
	
	public static int setSessionTimeout(int timeout)
	{
		int count = 0;
			Iterator<String> it = sessions.keySet().iterator();
			while (it.hasNext())
			{
				String key = it.next();
				AppSession sess = sessions.get(key);
				sess.setSessionTimeOut(timeout);
				count++;
			}

		return count;
	}

	public static AppSession getSession(String sessionId)
	{
		return sessions.get(sessionId);
	}

	public static void endSession(String sessionId,String userName, String clearPassword)
	{		
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		if(null==clusterNodes||null==clusterNodes[0]||"".equals(clusterNodes[0]))
		{
			endLocalSession(sessionId);
			return;
		}
		RemoteSessionAdminHelper.terminateRemoteSession(sessionId, userName,  clearPassword);
		return;
	}
	
	public static void endLocalSession(String sessionId)
	{		
		AppSession sess = getSession(sessionId);
		if (sess == null) return;		
		sess.endSession();
		sessions.remove(sessionId);
	}

	public static String toggleLogging(String sessionID,String userName, String clearPassword)
	{
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		if(null==clusterNodes||null==clusterNodes[0]||"".equals(clusterNodes[0]))
		{
			return toggleLocalLogging(sessionID);
		}
		return RemoteSessionAdminHelper.toggleRemoteLogging(sessionID, userName,  clearPassword);
	
	}
	
	
	public static String toggleLocalLogging(String sessionID)
	{
		AppSession sess = getSession(sessionID);
	
		if (sess.loggingEnabled())
		{
			sess.disableSessionLogging();
		}
		else
		{
			sess.enableSessionLogging();
		}
		return sessionID;
	}
	
}
