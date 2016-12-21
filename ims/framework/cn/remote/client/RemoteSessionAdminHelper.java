package ims.framework.cn.remote.client;

import ims.configuration.ConfigFlag;
import ims.configuration.EnvironmentConfig;
import ims.framework.cn.remote.RemoteSessionAdmin;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

public class RemoteSessionAdminHelper
{
	private static final org.apache.log4j.Logger LOG = ims.utils.Logging.getLogger(RemoteSessionAdminHelper.class);
	
	
	public static void terminateRemoteSession(String sessionId,String userName, String clearPassword)
	{
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		if(null==clusterNodes||null==clusterNodes[0]||"".equals(clusterNodes[0]))
		{
			return ;
		}

		String context = ConfigFlag.APP_CONTEXT_NAME.getValue();

		for (String node : clusterNodes)
		{
			String remoteSessionAdminLocation=null;
			try
			{

				remoteSessionAdminLocation = "http://" + node + "/" + context + "/services/RemoteSessionAdminWebService";

				RemoteSessionAdminServiceLocator wservice = new RemoteSessionAdminServiceLocator();
				wservice.setMichaelsProjectWebServiceEndpointAddress(remoteSessionAdminLocation);
				wservice.setMaintainSession(true);
				RemoteSessionAdmin ws = wservice.getRemoteSessionAdmin();
				ws.login(userName, clearPassword);
				ws.terminateSessionWS(sessionId);
				ws.logout();
			}
			catch (ServiceException e)
			{
				LOG.error("Incorrect URL for RemoteSessionAdmin Web Service:"+remoteSessionAdminLocation, e);
			}
			catch (RemoteException e)
			{
				LOG.error("Cannot connect to RemoteSessionAdmin Web Service:"+ remoteSessionAdminLocation, e);
			}
		}

	}
	static public String toggleRemoteLogging(String sessionID,String userName, String clearPassword)
	{
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		if(null==clusterNodes||null==clusterNodes[0]||"".equals(clusterNodes[0]))
		{
			return sessionID;
		}

		String context = ConfigFlag.APP_CONTEXT_NAME.getValue();

		for (String node : clusterNodes)
		{
			String remoteSessionAdminLocation=null;
			try
			{

				remoteSessionAdminLocation = "http://" + node + "/" + context + "/services/RemoteSessionAdminWebService";

				RemoteSessionAdminServiceLocator wservice = new RemoteSessionAdminServiceLocator();
				wservice.setMichaelsProjectWebServiceEndpointAddress(remoteSessionAdminLocation);
				wservice.setMaintainSession(true);
				RemoteSessionAdmin ws = wservice.getRemoteSessionAdmin();
				ws.login(userName, clearPassword);
				ws.toggleLoggingWS(sessionID);
				ws.logout();
				 
			}
			catch (ServiceException e)
			{
				LOG.error("Incorrect URL for RemoteSessionAdmin Web Service:"+remoteSessionAdminLocation, e);
			}
			catch (RemoteException e)
			{
				LOG.error("Cannot connect to RemoteSessionAdmin Web Service:"+remoteSessionAdminLocation, e);
			}
		}
		return sessionID;
	}
	

	public static String[] getRemoteSessions(String userName, String clearPassword)
	{
		List<String> sessionDetails = new ArrayList<String>();
		String[] clusterNodes = EnvironmentConfig.getClusterNodes().split("[,;]");
		String context = ConfigFlag.APP_CONTEXT_NAME.getValue();

		for (String node : clusterNodes)
		{
			String remoteSessionAdminLocation=null;
			try
			{
				String[] sessionStrs;

				remoteSessionAdminLocation = "http://" + node + "/" + context + "/services/RemoteSessionAdminWebService";

				RemoteSessionAdminServiceLocator wservice = new RemoteSessionAdminServiceLocator();
				wservice.setMichaelsProjectWebServiceEndpointAddress(remoteSessionAdminLocation);
				wservice.setMaintainSession(true);
				RemoteSessionAdmin ws = wservice.getRemoteSessionAdmin();
				ws.login(userName, clearPassword);
				sessionStrs = ws.listSessionsWS();
				ws.logout();
				if (null != sessionStrs)
				{
					for (String string : sessionStrs)
					{
						sessionDetails.add(string);
					}
				}
				
			}
			catch (ServiceException e)
			{
				LOG.error("Incorrect URL for RemoteSessionAdmin Web Service:"+remoteSessionAdminLocation, e);
			}
			catch (RemoteException e)
			{
				LOG.error("Cannot connect to RemoteSessionAdmin Web Service:"+remoteSessionAdminLocation, e);
			}
		}
		return sessionDetails.toArray(new String[sessionDetails.size()]);
	}

}
