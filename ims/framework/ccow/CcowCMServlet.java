package ims.framework.ccow;

import ims.domain.admin.AppSession;
import ims.framework.cn.SessionManager;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CcowCMServlet extends HttpServlet 
{
	private static final long serialVersionUID = -1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(CcowCMServlet.class);
	
	private static Map<String, CommonContext> clientDesktops = Collections.synchronizedMap(new HashMap<String, CommonContext>());

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		LocalLogger.info("CcowCMServlet called with " + request.getQueryString());
		Map map = request.getParameterMap();
		
		String interfaceName =  getParamValue(map, "interface");
		if (interfaceName == null || !interfaceName.equals("ContextManager"))
		{
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "GeneralFailure", "Invalid Interface name supplied - " + interfaceName);
			return;
		}
		
		String methodName =  getParamValue(map, "method");
		if (methodName == null)
		{
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "GeneralFailure", "No Method name supplied");
			return;		
		}
		
		if (methodName.equals("JoinCommonContext"))
		{
			String[] jSessionId = new String[1];
			String ipAddress = retrieveIpAddress(map, jSessionId);
			if (ipAddress == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "IP Address and JSessionId not found. Can't join context.");
				return;
			}				
			
			String cpUrl = getParamValue(map, "contextParticipant");
			if (cpUrl == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "Participant URL must be supplied in " + methodName + " method");
				return;				
			}

			CommonContext ctx = clientDesktops.get(ipAddress);	
			CcowParticipant cp = null;
			if (ctx != null)
			{
				cp = ctx.getParticipant(cpUrl);
				if (cp != null)
				{
					sendError(response, HttpServletResponse.SC_NOT_FOUND, "AlreadyJoined", "Participant with URL = " + cpUrl + " already joined.");
					return;									
				}
			}
			else
			{
				ctx = new CommonContext(ipAddress);
				clientDesktops.put(ipAddress, ctx);				
			}
			
			cp = ctx.addParticipant(ipAddress, jSessionId[0], cpUrl);
			String waitVal = getParamValue(map, "wait");
			if (waitVal != null)
				cp.setWait(new Boolean(waitVal).booleanValue());
			
			String surveyVal = getParamValue(map, "survey");
			if (surveyVal != null)
				cp.setSurvey(new Boolean(surveyVal).booleanValue());
			
			cp.setAppName(getParamValue(map, "applicationName"));
			response.getWriter().print("participantCoupon=" + cp.getParticipantCoupon());
		}
		else if (methodName.equals("LeaveCommonContext"))
		{
			String[] jSessionId = new String[1];
			String ipAddress = retrieveIpAddress(map, jSessionId);
			if (ipAddress == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "IP Address and JSessionId not found. Can't leave context.");
				return;
			}				
			String cpCoupon = getParamValue(map, "participantCoupon");
			if (cpCoupon == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "participantCoupon must be supplied in " + methodName + " method");
				return;				
			}
			
			Map pc = (Map)clientDesktops.get(ipAddress);	
			if (pc != null)
			{
				pc.remove(cpCoupon);
			}			
		}
		else if (methodName.equals("GetMostRecentContextCoupon"))
		{
			String[] jSessionId = new String[1];
			String ipAddress = retrieveIpAddress(map, jSessionId);
			if (ipAddress == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "IP Address and JSessionId not found. Can't join context.");
				return;
			}				
			CommonContext ctx = clientDesktops.get(ipAddress);	
			if (ctx == null)
			{
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "BadItemValue", "No common context exists for the ip address = " + ipAddress);				
			}
			response.getWriter().print("contextCoupon=" + ctx.getContextCoupon());
		}
	}

	private String retrieveIpAddress(Map map, String[] jSessionId)
	{
		String ipAddress = getParamValue(map, "clientIPAddress");
		String sessId = null;
		if (ipAddress == null)
		{
			sessId = getParamValue(map, "JSessionId");
			if (sessId == null)
			{
				return null;
			}
			AppSession appSession = SessionManager.getSession(sessId);
			if (appSession == null)
			{
				return null;					
			}
			ipAddress = appSession.getRemoteAddress();
			jSessionId[0] = sessId;
		}
		return ipAddress;
	}

	private String getParamValue(Map map, String paramName)
	{
		String[] paramValues = (String[])map.get(paramName);
		String paramValue = null;
		if (paramValues != null && paramValues.length > 0)
		{
			paramValue = paramValues[0];
		}
		return paramValue;
	}

	private void sendError(HttpServletResponse response, int errorCode, String exceptionName, String message) throws IOException
	{
		response.sendError(errorCode);
		response.setHeader("exception", exceptionName);
		LocalLogger.error(message);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
}
