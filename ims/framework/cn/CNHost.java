package ims.framework.cn;

import ims.configuration.AppServer;
import ims.configuration.BuildInfo;
import ims.configuration.CfgFlag;
import ims.configuration.ConfigFlag;
import ims.configuration.ConfigType;
import ims.configuration.Configuration;
import ims.configuration.EnvironmentConfig;
import ims.configuration.InitConfig;
import ims.configuration.User;
import ims.domain.DomainSession;
import ims.domain.GenericIdentifierFactory;
import ims.domain.ListRecordInformation;
import ims.domain.SessionData;
import ims.domain.addressmanager.AddressManager;
import ims.domain.admin.AppSession;
import ims.domain.exceptions.DomainException;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.factory.ContextEvalFactory;
import ims.domain.factory.ContextSetterFactory;
import ims.domain.factory.LocationFactory;
import ims.domain.factory.RoleFactory;
import ims.domain.factory.SecurityTokenFactory;
import ims.domain.factory.SecurityTokenHandlerFactory;
import ims.dto.Connection;
import ims.dto.DTODomainImplementation;
import ims.framework.Alert;
import ims.framework.AlertLogic;
import ims.framework.ContextReferences;
import ims.framework.CustomEvent;
import ims.framework.ExternalApplication;
import ims.framework.FormInfo;
import ims.framework.FormInfoAlert;
import ims.framework.FormName;
import ims.framework.FormUiLogic;
import ims.framework.GenericChangeableCollection;
import ims.framework.InternalAlert;
import ims.framework.MessageBox;
import ims.framework.MessageButtons;
import ims.framework.MessageIcon;
import ims.framework.RIEAlert;
import ims.framework.SessionConstants;
import ims.framework.UILogic;
import ims.framework.Url;
import ims.framework.UrlParam;
import ims.framework.UserAgent;
import ims.framework.Version;
import ims.framework.WindowParam;
import ims.framework.cn.data.FormData;
import ims.framework.cn.events.AlertEvent;
import ims.framework.cn.events.AutoScreenLockEvent;
import ims.framework.cn.events.DialogWasClosed;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.IMenuEvent;
import ims.framework.cn.events.LoginEvent;
import ims.framework.cn.events.LogoutEvent;
import ims.framework.cn.events.MessageBoxEvent;
import ims.framework.cn.events.NavigationSelected;
import ims.framework.cn.events.NewPasswordEvent;
import ims.framework.cn.events.RIEAlertEvent;
import ims.framework.cn.events.ScreenUnlock;
import ims.framework.cn.events.SearchEvent;
import ims.framework.cn.events.SearchResultSelectionEvent;
import ims.framework.cn.events.SecurityTokenLoginEvent;
import ims.framework.cn.events.SelectRoleEvent;
import ims.framework.cn.events.SmartcardTicketReceivedEvent;
import ims.framework.cn.events.StoredLocationsEvent;
import ims.framework.cn.events.SystemPasswordClosed;
import ims.framework.cn.events.TimerEvent;
import ims.framework.cn.events.TopButtonClicked;
import ims.framework.cn.events.UpdateTheme;
import ims.framework.cn.utils.Search;
import ims.framework.controls.Timer;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.FormAccess;
import ims.framework.enumerations.FormMode;
import ims.framework.enumerations.SystemLogLevel;
import ims.framework.enumerations.SystemLogType;
import ims.framework.enumerations.UILayoutState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.EngineException;
import ims.framework.exceptions.FormMandatoryContextMissingException;
import ims.framework.exceptions.FormMandatoryLookupMissingException;
import ims.framework.exceptions.FormOpenException;
import ims.framework.exceptions.FrameworkInternalException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.factory.AppParamHandlerFactory;
import ims.framework.factory.UploadDownloadUrlProviderFactory;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IAppParam;
import ims.framework.interfaces.IAppRole;
import ims.framework.interfaces.IAppRoleLight;
import ims.framework.interfaces.IAppUser;
import ims.framework.interfaces.IClearInfo;
import ims.framework.interfaces.IContextSetter;
import ims.framework.interfaces.IDynamicNavigation;
import ims.framework.interfaces.IFormIdSelection;
import ims.framework.interfaces.IGenericIdentifier;
import ims.framework.interfaces.ILocation;
import ims.framework.interfaces.ILocationProvider;
import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.INavigation;
import ims.framework.interfaces.IRoleProvider;
import ims.framework.interfaces.ISearch;
import ims.framework.interfaces.ISearchResult;
import ims.framework.interfaces.ISecurityToken;
import ims.framework.interfaces.ISecurityTokenHandler;
import ims.framework.interfaces.ISecurityTokenHandlerProvider;
import ims.framework.interfaces.ISecurityTokenParameter;
import ims.framework.interfaces.ISecurityTokenProvider;
import ims.framework.interfaces.ISelectedPatient;
import ims.framework.interfaces.ISystemLog;
import ims.framework.interfaces.ITopButtonConfig;
import ims.framework.interfaces.IUploadDownloadUrlProvider;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;
import ims.framework.utils.TimeSpan;
import ims.hl7.HL7ControllerFactory;
import ims.hl7.interfaces.IHL7Controller;
import ims.rules.engine.RulesEngineFactory;
import ims.rules.exceptions.RulesEngineRuntimeException;
import ims.utils.Logging;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.Security;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.xs.dom.DOMParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import capscan.client.McConnection;
import capscan.client.NcConnection;

import java.net.URLConnection;	//WDEV-22509
import javax.net.ssl.HttpsURLConnection;	//WDEV-22509
import java.io.InputStream;	//WDEV-22509


public class CNHost extends HttpServlet
{
	private static final long						serialVersionUID		 = 1L;
	private static int								initCount				 = 0;
	private static final org.apache.log4j.Logger	LocalLogger				 = ims.utils.Logging.getLogger(CNHost.class);
	private static Configuration					configuration			 = null;
	private static boolean							isRulesEngineInitialized = false;	
	private StringBuffer 							getSamlXmlRequestError	 = null;

	protected static Configuration loadConfiguration() throws Exception
	{
		if (configuration == null)
		{
			configuration = loadConfiguration(null);
		}
		return configuration;
	}

	protected static Configuration loadConfiguration(HttpSession session) throws Exception
	{
		if (configuration != null)
		{
			configuration.setHttpSession(session); // Required by DTOConfiguration
		}
		else
		{
			configuration = Configuration.loadConfiguration(session);
		}
		return configuration;
	}

	@SuppressWarnings("static-access")
	public void init()
	{
		if (initCount == 0)
		{
			ServletContext servletContext = getServletContext();
			
			if(servletContext == null)
			{
				LocalLogger.warn("Starting CNHost servlet...");	
			}
			else
			{				
				LocalLogger.warn("Starting CNHost servlet for " + servletContext.getServletContextName() + " context on " + servletContext.getServerInfo() + "...");
			}
			
			initCount++;
			
			try
			{
				// For Datadirect, set the unlocking pwd
				System.setProperty("module.core.status", "imsdatadirect");
				
				InitConfig.loadInitConfig();
				ContextReferences.initialize();

				String app = "";
				String realPath = getServletContext().getRealPath("/");
				EnvironmentConfig.setBaseUri(realPath);
								
				if (!realPath.equals(""))
				{
					app = realPath.replaceAll("\\\\", "_");
					app = app.replaceAll("/", "_");
					app = app.replaceAll(":", "_");
					while (app.endsWith("_"))
						app = app.substring(0, app.length() - 1);
				}
							
				// FWUI-1412
				String flagLoggingLevel = ConfigFlag.GEN.LOGGING_LEVEL.getValue();
				EnvironmentConfig.SetLoggingLevel(flagLoggingLevel);
				// end of FWUI-1412
				
				Logging.setLoggingLevel(flagLoggingLevel);
				Logging.setMaximsAppenderFile(ConfigFlag.GEN.LOG_FILE.getValue());
				
				try
				{
					CfgFlag.loadProjectFlags();
					LocalLogger.warn("Project Flags loaded successfully in init() of CNHost");
				}
				catch (Exception e)
				{
					LocalLogger.error("Failed to load Project or Site flags in init() of CNHost. " + e.getMessage());					
				}

				BuildInfo buildInfo = Configuration.readAppBuildDetails(this.getClass());
				buildInfo.setFrameworkFullVersionInfo(Version.getFullVersionInfo());
				buildInfo.setFrameworkTimestamp(Version.getTimestamp());
				buildInfo.setFrameworkVersionInfo(Version.getVersionInfo());
				buildInfo.setMaximsICABVersionInfo();
				buildInfo.setMaximsIPDSVersionInfo();  // WDEV-21284
				buildInfo.setMaximsPDSSchemaVersionInfo();  // WDEV-21284
				LocalLogger.warn("Application Build Details loaded successfully in init() of CNHost");

				String baseUrl = getServletContext().getResource("/").getPath();
				baseUrl = baseUrl.substring(baseUrl.indexOf("/") + 1);
				baseUrl = baseUrl.substring(baseUrl.indexOf("/") + 1);
				if (baseUrl.endsWith("/"))
					baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
				ConfigFlag.setValue(ConfigFlag.APP_CONTEXT_NAME, baseUrl);			

				String appServerInfo = this.getServletContext().getServerInfo().toUpperCase();
				
				if (appServerInfo.indexOf("TOMCAT") >= 0)
					EnvironmentConfig.setApplicationServerType(AppServer.TOMCAT);
				else if (appServerInfo.indexOf("WEBLOGIC") >= 0)
					EnvironmentConfig.setApplicationServerType(AppServer.WEBLOGIC);
				else if (appServerInfo.indexOf("SUN") >= 0)
					EnvironmentConfig.setApplicationServerType(AppServer.SJSAS);
				else if (appServerInfo.indexOf("WEBSPHERE") >= 0)
					EnvironmentConfig.setApplicationServerType(AppServer.WEBSPHERE);

				LocalLogger.warn("APPSERVER_TYPE set to " + EnvironmentConfig.getApplicationServerType());
				
				if(!InitConfig.getConfigType().equals(ConfigType.XML))
				{
					LocalLogger.warn("MAIN_DATASOURCE_NAME set to " + EnvironmentConfig.getMainDataSourceName());
				}

				String hostName = InetAddress.getLocalHost().getHostName();
				ConfigFlag.setValue(ConfigFlag.HOST_NAME, hostName);
				LocalLogger.warn("Server Host Name = " + hostName);

				ConfigFlag.setValue(ConfigFlag.START_TIME, new DateTime().toString());
				
				//AddressManager connections
				try
				{
					if ((EnvironmentConfig.getCapscanServerName()) != null && (EnvironmentConfig.getCapscanServerName() != ""))
					{
						LocalLogger.warn("Capscan Servername = " + EnvironmentConfig.getCapscanServerName());
						AddressManager.connect(EnvironmentConfig.getCapscanServerName(), EnvironmentConfig.getCapscanPool(), McConnection.STATELESS);
						AddressManager.ncConnect(EnvironmentConfig.getCapscanServerName(), EnvironmentConfig.getCapscanDistPool(), NcConnection.STATELESS);
					}
				}
				catch (java.net.ConnectException ce)
				{
					LocalLogger.warn("Capscan Connection error " + ce.getMessage());
				}
			}
			catch (Exception ex)
			{
				LocalLogger.error("Failed to load Config Flags in CNHost init()", ex);
				ex.printStackTrace();
			}
			LocalLogger.warn("init() method of CNHost called for the first time.");
			
			LocalLogger.warn("CNHost servlet started.");
		}
		else
		{
			LocalLogger.warn("init() method of CNHost called " + initCount + " times");			
		}		
	}

	/*private void checkMinimumFrameworkRequirements()
	{
		if(InitConfig.getFrameworkVersion() != null)
		{
			VersionInfo currentFrameworkVersion = new VersionInfo(ims.framework.Version.Major, ims.framework.Version.Minor);
			if(InitConfig.getFrameworkVersion().compareTo(currentFrameworkVersion) > 0)
				throw new RuntimeException("Invalid framework.jar version (Required: " + InitConfig.getFrameworkVersion() + ", Current: " + currentFrameworkVersion + ").");
		}
		if(InitConfig.getDomainVersion() != null)
		{
			VersionInfo currentDomainVersion = new VersionInfo(ims.domain.Version.Major, ims.domain.Version.Minor);
			if(InitConfig.getDomainVersion().compareTo(currentDomainVersion) > 0)
				throw new RuntimeException("Invalid domain.jar version (Required: " + InitConfig.getDomainVersion() + ", Current: " + currentDomainVersion + ").");
		}
		if(InitConfig.getBaseVersion() != null)
		{
			VersionInfo currentBaseVersion = new VersionInfo(ims.base.Version.Major, ims.base.Version.Minor);
			if(InitConfig.getBaseVersion().compareTo(currentBaseVersion) > 0)
				throw new RuntimeException("Invalid base.jar version (Required: " + InitConfig.getBaseVersion() + ", Current: " + currentBaseVersion + ").");
		}
	}*/
	
	public void destroy()
	{
		IHL7Controller controller = HL7ControllerFactory.getController();
		if (controller != null)
			controller.shutdown();
	}
		
	/*private void retrieveJdbcDatasourceNames() throws Exception
	{
		javax.naming.Context initCtx = new InitialContext();
		String appServer = EnvironmentConfig.GetApplicationServerType();
		if (appServer.equals(AppServer.WEBSPHERE) || appServer.equals(AppServer.TOMCAT))
		{
			initCtx = (javax.naming.Context) initCtx.lookup("java:comp/env");
		}
		try
		{
			NamingEnumeration names = initCtx.list("jdbc");
			List nameOpts = new ArrayList();
			int i = 1;
			while (names.hasMore())
			{
				NameClassPair pair = (NameClassPair) names.next();
				LocalLogger.warn("JDBC Resource " + i + " - " + pair.getName());
				i++;
				nameOpts.add(pair.getName());
			}
			ConfigFlag.DOM.MAIN_DATASOURCE_NAME.setOptions(nameOpts);
			ConfigFlag.DOM.SECOND_DATASOURCE_NAME.setOptions(nameOpts);
		}
		catch (NameNotFoundException e)
		{
			LocalLogger.warn("No jdbc datasources are configured for this project.");
		}
	}*/


	private String loadTheme()
	{
		try
		{
			StringBuffer sb = new StringBuffer(12000);
			sb.append("<html ");
			sb.append("theme=\"");
			sb.append(InitConfig.getTheme());
			sb.append("\"><![CDATA[");

			String line;

			/*
			 * No longer needed with the new UI Layout BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("/themes/" + theme +"/login.html"))); while ((line = reader.readLine()) != null) sb.append(line); reader.close();
			 */

			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("/themes/" + InitConfig.getTheme() + ("/layout.html"))));
			while ((line = reader.readLine()) != null)
				sb.append(line);
			reader.close();

			sb.append("]]>");
			sb.append("</html>");

			BuildInfo buildInfo = Configuration.getBuildInfo();
			if (buildInfo != null)
			{
				StringBuffer mainAboutHTMLContent = new StringBuffer();
				mainAboutHTMLContent.append("<b>");
				mainAboutHTMLContent.append(buildInfo.getName());
				mainAboutHTMLContent.append("</b>");
				mainAboutHTMLContent.append("<br>");
				mainAboutHTMLContent.append("Version " + buildInfo.getAppVersion() + " (" + buildInfo.getAppTimestamp() + ")");
				mainAboutHTMLContent.append("<br>");
				mainAboutHTMLContent.append("<br><b>War File</b>");
				mainAboutHTMLContent.append("<br>Version " + buildInfo.getWarVersion() + " (" + buildInfo.getWarTimestamp() + ")");
				mainAboutHTMLContent.append("<br>");
				mainAboutHTMLContent.append("<br><b>Framework: </b>");
				mainAboutHTMLContent.append("Version " + buildInfo.getFrameworkVersionInfo() + " (" + buildInfo.getFrameworkTimestamp() + ")");
				mainAboutHTMLContent.append("<br>");
				mainAboutHTMLContent.append("<b>Domain: </b>");
				mainAboutHTMLContent.append("Version " + buildInfo.getDomainVersionInfo() + " (" + buildInfo.getDomainTimestamp() + ")");
				mainAboutHTMLContent.append("<br>");
				mainAboutHTMLContent.append("<b>Base: </b>");
				mainAboutHTMLContent.append("Version " + buildInfo.getBaseVersionInfo() + " (" + buildInfo.getBaseTimestamp() + ")");
				if (ConfigFlag.GEN.ICAB_ENABLED.getValue() && buildInfo.getMaximsICABversionInfo() != "")
				{
					mainAboutHTMLContent.append("<br>");
					mainAboutHTMLContent.append("<b>ICAB: </b>");
					mainAboutHTMLContent.append("Version " + buildInfo.getMaximsICABversionInfo() + " (" + buildInfo.getMaximsICABTimestampInfo() + ")");
				}
				
				// WDEV-21284 PDS Version info
				if (ConfigFlag.DOM.USE_PDS.getValue() != null && !ConfigFlag.DOM.USE_PDS.getValue().equals("None"))
				{
					mainAboutHTMLContent.append("<br>");
					mainAboutHTMLContent.append("<b>PDS Gateway: </b>");
					
					String pdsVersion = buildInfo.getMaximsPDSversionInfo();
					String pdsSchemaVersion = buildInfo.getMaximsPDSSchemaVersionInfo();
					
					if (pdsVersion == null)
						mainAboutHTMLContent.append("Version N/A");
					else
						mainAboutHTMLContent.append("Version " + buildInfo.getMaximsPDSversionInfo() + " (" + buildInfo.getMaximsPDSTimestampInfo() + ")");
					
					mainAboutHTMLContent.append("<br>");
					mainAboutHTMLContent.append("<b>PDS Schema: </b>");
					
					if (pdsSchemaVersion == null)
						mainAboutHTMLContent.append("Version N/A");
					else
						mainAboutHTMLContent.append("Version " + buildInfo.getMaximsPDSSchemaVersionInfo() + " (" + buildInfo.getMaximsPDSSchemaTimestampInfo() + ")");
					
				}
				
				StringBuffer secondAboutHTMLContent = new StringBuffer();
				secondAboutHTMLContent.append("Copyright (C) 1995-" + new Date().getYear() + " IMS MAXIMS. All rights reserved.");
				secondAboutHTMLContent.append("<br><br>Web: <a target=\"_blank\" href=\"http://www.imsmaxims.com\">www.imsmaxims.com</a>");
				secondAboutHTMLContent.append("<br>E-Mail: <a target=\"_blank\" href=\"mailto:info@imsmaxims.com?subject=RE: " + Configuration.getBuildInfo().getName() + " Version " + buildInfo.getAppVersion() + "\">info@imsmaxims.com</a></p>");

				sb.append("<aboutDialog title=\"About " + StringUtils.encodeXML(buildInfo.getName()) + "\">");
				sb.append("<mainHTML><![CDATA[ " + mainAboutHTMLContent.toString() + " ]]></mainHTML>");
				sb.append("<secondHTML><![CDATA[ " + secondAboutHTMLContent.toString() + " ]]></secondHTML>");
				sb.append("</aboutDialog>");
			}

			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private String login(HttpServletRequest request)
	{		
		HttpSession session = getSession(request, true);
		
		StringBuffer url = request.getRequestURL();
		if (url != null) {
			int index = url.indexOf("CNHost");
			StringBuffer fullUrl = url.replace(index, url.length(), "");
			EnvironmentConfig.setAplicationURL(fullUrl.toString());

			if (ConfigFlag.GEN.PDF_UPLOAD_URL.getValue() == null ||
					(ConfigFlag.GEN.PDF_UPLOAD_URL.getValue() != null &&
							ConfigFlag.GEN.PDF_UPLOAD_URL.getValue().length() == 0)) {
				ConfigFlag.setValue(ConfigFlag.GEN.PDF_UPLOAD_URL, fullUrl + "PDFUpload");
			}
			if (ConfigFlag.GEN.UPLOAD_URL.getValue() == null ||
					(ConfigFlag.GEN.UPLOAD_URL.getValue() != null &&
							ConfigFlag.GEN.UPLOAD_URL.getValue().length() == 0)) {
				ConfigFlag.setValue(ConfigFlag.GEN.UPLOAD_URL, fullUrl + "Upload");
			}
			if (ConfigFlag.GEN.IMAGES_UPLOAD_URL.getValue() == null ||
					(ConfigFlag.GEN.IMAGES_UPLOAD_URL.getValue() != null &&
							ConfigFlag.GEN.IMAGES_UPLOAD_URL.getValue().length() == 0)) {
				ConfigFlag.setValue(ConfigFlag.GEN.IMAGES_UPLOAD_URL, fullUrl + "ImageUpload");
			}					
		}
		
		StringBuffer sb = createResponseString();		
		sb.append("<xml>");
				
		if(!isMobileSession(session) && !isCustomNavigatorSession(session))
			sb.append(this.loadTheme());					
		
		getUILayout(session).renderLogin(sb);		
		sb.append("</xml>");

		return sb.toString();
	}

	
	
	
	private String handleLogin(HttpServletRequest request, HttpSession session, LoginEvent event, ims.framework.UIEngine engine)
	{
		String computer = (String) session.getAttribute(SessionConstants.REMOTE_HOST);
		IAppUser user = null;
		SessionData sessData = null;
		
		try
		{
			loadConfiguration(session);			
			sessData = getSessionData(session);
			
			if(event.getName() == null || event.getName().trim().length() == 0 || event.getPass() == null || event.getPass().trim().length() == 0)
			{								
				throw new Exception("Invalid username and/or password.");
			}
						
			sessData.configurationModule.set(configuration);
			sessData.currentFormMode.set(FormMode.VIEW);
			sessData.formsData.set(new HashMap());
			sessData.fireOpenEvent.set(Boolean.TRUE);
			sessData.alerts.set(new ArrayList<Alert>());
			sessData.uploadUrl.set("Upload");
			sessData.currentUILayoutState.set(UILayoutState.DEFAULT);
			sessData.uiLayoutChanged.set(true);
			sessData.sessionId.set(session.getId());
			
			
			
			if(sessData.passwordSalt.get() != null)
				user = configuration.secureLogin(event.getName(), event.getPass(), sessData.passwordSalt.get(), sessData.domainSession.get());
			else
				user = configuration.login(event.getName(), event.getPass(), sessData.domainSession.get());
			
			//*** Login Attempts ***
			if (user.isLocked()) {				
				return createResponseMessage( ConfigFlag.GEN.LOGIN_ATTEMPTS_MESSAGE.getValue(), event, engine, computer);
			}		
			else{
				sessData.loginAttempts.set(Integer.valueOf(0));
			}
			//*** End Login Attempts ***
			
			if(hasInvalidLocationContext(getUIEngine(request, session), user))
				throw new Exception("No locations configured for the current user.");
			
			user.setHostName(computer);

			sessData.changeTheme.set(user.getTheme());
			sessData.user.set(user);
			sessData.domainSession.get().setUser(user);

			StringBuffer sb = createResponseString();
			
			if (user.getEffectiveFrom() != null)
			{
				int diff = TimeSpan.getTimeSpan(user.getEffectiveFrom().getDate(), new Date()).getDays();
				if (diff > 0)
				{
					String days = " for another " + diff + " days.";
					if (diff == 1)
						days = " until tomorrow.";					
					sb.append("<xml><messages><message>User record not active" + days + "</message></messages></xml>");
					return sb.toString();
				}
			}
			if (user.getEffectiveTo() != null)
			{
				int diff = TimeSpan.getTimeSpan(user.getEffectiveTo().getDate(), new Date()).getDays();
				if (diff < 0)
				{
					sb.append("<xml><messages><message>User record no longer active.</message></messages></xml>");
					return sb.toString();
				}
			}
			if (user.getPwdExpDate() != null)
			{
				int diff = TimeSpan.getTimeSpan(user.getPwdExpDate().getDate(), new Date()).getDays();
				
				//If diff between 0 and 14: password will expire in x days 
				if (diff > 0)
				{
					if (diff <  ConfigFlag.FW.NUMBER_OF_DAYS_BEFORE_PASSWORD_EXPIRATION.getValue())
					{
						StringBuffer sb1 = new StringBuffer();
						getUILayout(session).renderPasswordExpiry(sb1, new Long(diff).intValue());
						sb.append("<xml>");
						sb.append(sb1.toString());
						sb.append("</xml>");
						return sb.toString();
					}
				}
				//If diff between 0 and -14: password expired from x days ago. After -14 user will not be able to change password.
				else
				{
					if (diff >= -(ConfigFlag.FW.NUMBER_OF_DAYS_AFTER_PASSWORD_EXPIRED.getValue()))
					{
						StringBuffer sb1 = new StringBuffer();
						getUILayout(session).renderPasswordExpiry(sb1, new Long(diff).intValue());
						sb.append("<xml>");
						sb.append(sb1.toString());
						sb.append("</xml>");
						return sb.toString();
					}
					else
					{
						sb.append("<xml><messages><message>" + ConfigFlag.FW.PASSWORD_EXPIRED_MESSAGE.getValue() + "</message></messages></xml>");
						return sb.toString();
					}					
				}
			}
			
			// FWUI-1573
			setLoggedInDetails(session, sessData, user);
			
			//Login has succeeded. Attempt to join CCOW context (if CCOW enabled)
			AppSession appSession = SessionManager.getSession(session.getId());
			if (appSession != null)
			{
				appSession.joinCcowContext();
			}
			
			handleAppParameters(engine, sessData);
			
			engine.createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.INFORMATION, "Login successful");			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			String message = ex.getMessage();
			if (message == null || message.trim().length() == 0)
				message = "Login failed.";
			
			
			//*** Login Attempts ***
			if (sessData == null) 
				sessData = getSessionData(session);	
			if (sessData == null)				
				return createResponseMessage("Session Data is null. ", event, engine, computer);
						
			if (user == null) {			
				try { user = configuration.getAppUser(event.getName(), sessData.domainSession.get()); }
				catch (Exception e) {}
			}
			if (user == null)
				return createResponseMessage("Login failed.", event, engine, computer);
									
			if (user.isLocked()) {				
					return createResponseMessage( ConfigFlag.GEN.LOGIN_ATTEMPTS_MESSAGE.getValue(), event, engine, computer);
			}
							 
			if (ConfigFlag.GEN.LOGIN_ATTEMPTS.getValue() > 0)
			{
				int maxAttempts = ConfigFlag.GEN.LOGIN_ATTEMPTS.getValue();
				int currentAttempts =  sessData.loginAttempts.get().intValue(); 
				
				if (currentAttempts >= maxAttempts) {					
					configuration.lockAccount(user, true, sessData.domainSession.get());
					return createResponseMessage( ConfigFlag.GEN.LOGIN_ATTEMPTS_MESSAGE.getValue(), event, engine, computer);
				}
				else {									
					sessData.loginAttempts.set(Integer.valueOf(++currentAttempts));
				}
			}
			//***End Login Attempts ***
														
			return createResponseMessage(message, event, engine, computer);
		}
		
		return null;
	}		

	private String createResponseMessage(String message, LoginEvent event, ims.framework.UIEngine engine, String computer) 
	{
		StringBuffer sb = createResponseString();
		
		sb.append("<xml>");
		sb.append("<messages><message>");
		sb.append(StringUtils.encodeXML(message));
		sb.append("</message></messages>");
		sb.append("</xml>");

		((UIEngine)engine).createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.ERROR, event.getName(), computer, message);

		return sb.toString();
	}

	private void setLoggedInDetails(HttpSession session, SessionData sessData, IAppUser user) 
	{
		//set Logged in Context
		ContextSetterFactory setf = new ContextSetterFactory(sessData.domainSession.get(), new Context(session));
		if (setf.hasContextSetter())				
		{
			IContextSetter setter = setf.getContextSetter();
			setter.setLoggedInUser();
			setter.setLoggedInRsno(user.getUserId());
		}
	}

	private void handleAppParameters(ims.framework.UIEngine engine, SessionData sessData)
	{
		if(sessData.appParams.get() != null)
		{
			IAppParam[] params = sessData.appParams.get();
			
			AppParamHandlerFactory factory = new AppParamHandlerFactory(sessData.domainSession.get());
			if(factory.hasProvider())
			{
				factory.getProvider().handle(engine, params);
			}
			
			sessData.appParams.set(null);
		}
	}

	private String handleLogin(HttpServletRequest request, HttpSession session, SecurityTokenLoginEvent event, ims.framework.UIEngine engine)
	{
		try
		{
			loadConfiguration(session);

			SessionData sessData = getSessionData(session);
			
			if (sessData.domainSession.get() == null)
			{
				sessData.domainSession.set(ims.domain.DomainSession.getSession(session));
			}

			sessData.configurationModule.set(configuration);
			sessData.currentFormMode.set(FormMode.VIEW);
			sessData.formsData.set(new HashMap());
			sessData.fireOpenEvent.set(Boolean.TRUE);
			sessData.alerts.set(new ArrayList<Alert>());
			sessData.uploadUrl.set("Upload");
			sessData.currentUILayoutState.set(UILayoutState.DEFAULT);
			sessData.uiLayoutChanged.set(true);
			sessData.sessionId.set(session.getId());
	
			IAppUser user = new User();
			((User)user).setUsername("SecurityToken");			
			user.setHostName((String) session.getAttribute(SessionConstants.REMOTE_HOST));			

			sessData.changeTheme.set(user.getTheme());
			sessData.user.set(user);			
			sessData.domainSession.get().setUser(user);

			String username = "";
			String password = "";
			String sourceSystem = null;
			String parameters = null;
			
			SecurityTokenFactory securityTokenFactory = new SecurityTokenFactory(sessData.domainSession.get());
			if(!securityTokenFactory.hasSecurityTokenProvider())
				throw new RuntimeException("No security token provider available.");
			ISecurityTokenProvider securityTokenProvider = securityTokenFactory.getSecurityTokenProvider();
			ISecurityToken token = securityTokenProvider.getToken(event.getSecurityToken());
			
			if(token == null) 
				throw new RuntimeException("Invalid security token.");
			if(token.getExpirationDateTime() == null)
				throw new RuntimeException("Invalid security token expiration date and time.");
			if(token.getParameters() == null)
				throw new RuntimeException("Invalid security token parameters.");
			
			DateTime now = new DateTime();			
			if(now.compareTo(token.getExpirationDateTime()) > 0)
				throw new RuntimeException("Security token has expired.");					
			
			for(int x = 0; x < token.getParameters().length; x++)
			{
				ISecurityTokenParameter param = token.getParameters()[x]; 
				if(param != null && param.getName() != null && param.getValue() != null)
				{
					if(param.getName().toLowerCase().equals("username"))
						username = param.getValue();
					else if(param.getName().toLowerCase().equals("password"))
						password = param.getValue();
					else if (param.getName().toLowerCase().equals("sourcesystem"))
						sourceSystem = param.getValue();
					else if (param.getName().toLowerCase().equals("parameters"))
						parameters = param.getValue();
				}
			}
			
			String l_ret = handleLogin(request, session, new LoginEvent(username, password), engine);
			
			if (null!=sourceSystem)
				handleSecurityTokenIntegrationParameters(session, sessData,sourceSystem,parameters);
			else
				handleSecurityTokenIntegrationParameters(session, sessData,sourceSystem,token.getParameters());
			
			return l_ret;
			
		}
		catch (Exception ex)
		{
			LocalLogger.warn("Exception handling Security Token Based Login", ex);
			engine.createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.ERROR, "Exception handling Security Token Based Login: " + ex.toString());
			StringBuffer sb = createResponseString();
			sb.append("<xml>");
			sb.append(this.loadTheme());			
			getUILayout(session).renderLogin(sb);		
			sb.append("<messages><message>" + StringUtils.encodeXML(ex.getMessage()) + "</message></messages>");
			sb.append("</xml>");
			return sb.toString();
		}		
	}	
	
	private void handleSecurityTokenIntegrationParameters(HttpSession session, SessionData sessData, String sourceSystem, ISecurityTokenParameter[] parameters) 
	{	
		SecurityTokenHandlerFactory factory = new SecurityTokenHandlerFactory(sessData.domainSession.get());
		if(!factory.hasSecurityTokenHandlerProvider())
			throw new RuntimeException("No security token handler provider available.");
		ISecurityTokenHandlerProvider provider = factory.getSecurityTokenHandlerProvider();
		ISecurityTokenHandler handler = provider.getISecurityTokenParameterHandler(sourceSystem);
		if(null==handler)
			throw new RuntimeException("No Security Token Parameter Handler configured for System:"+sourceSystem);

		ContextSetterFactory setf = new ContextSetterFactory(sessData.domainSession.get(), new Context(session));
		if (!setf.hasContextSetter())
			throw new RuntimeException("No Context Setter available. Cannot set Context.");
		IContextSetter setter = setf.getContextSetter();

		handler.setApplicationContextFromRequest(setter,sourceSystem, parameters);
	}

	private void handleSecurityTokenIntegrationParameters(HttpSession session,SessionData sessData, String sourceSystem,String parameters)
	{
		SecurityTokenHandlerFactory factory = new SecurityTokenHandlerFactory(sessData.domainSession.get());
		if(!factory.hasSecurityTokenHandlerProvider())
			throw new RuntimeException("No security token handler provider available.");
		ISecurityTokenHandlerProvider provider = factory.getSecurityTokenHandlerProvider();
		ISecurityTokenHandler handler = provider.getISecurityTokenParameterHandler(sourceSystem);
		if(null==handler)
			throw new RuntimeException("No Security Token Parameter Handler configured for System:"+sourceSystem);

		ContextSetterFactory setf = new ContextSetterFactory(sessData.domainSession.get(), new Context(session));
		if (!setf.hasContextSetter())
			throw new RuntimeException("No Context Setter available. Cannot set Context.");
		IContextSetter setter = setf.getContextSetter();

		handler.setApplicationContextFromRequest(setter,sourceSystem, parameters);
	}

	private String handleNewPassword(HttpSession session, NewPasswordEvent event, UIEngine uiEngine)
	{
		if (!event.isCancelled())
		{
			SessionData sessData = getSessionData(session);

			IAppUser user = sessData.user.get();
			String newPassword = event.getNewPassword();			
			
			try 
			{
				uiEngine.checkPasswordRequirements(newPassword, user.getClearPassword(), user.getUserPreviousPasswords());
			}
			catch (EngineException e) 
			{
				StringBuffer sb = createResponseString();
				sb.append("<xml><messages><message>" + StringUtils.encodeXML(e.getMessage()) + "</message></messages></xml>");
				return sb.toString();
			}
			
			//FWUI-1741
			
			configuration.newPassword(user, event.getNewPassword(), sessData.domainSession.get());
			
			//for External Authentication compatability, the user session data must be set after the new password is set.
			//Update IAppUser clear password with new password	
			sessData.user.get().setClearPassword(event.getNewPassword());
			
			//end FWUI-1741
			
		}
		return null;
	}

	/*
	 * private String selectLocation(HttpServletRequest request) { return selectLocation(request, true); } private String selectLocation(HttpServletRequest request, boolean checkForNewSession) { HttpSession session = getSession(request, false); if (session == null || (checkForNewSession && session.isNew())) { getSession(request); // create a new session return this.error(session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null); }
	 * 
	 * ILocation[] locations;
	 * 
	 * try { locations = new LocationFactory(ims.domain.http.DomainSession.getSession(session)).getLocationProvider().listLocations(); } catch(Exception e) { return fatalError(session, e); }
	 * 
	 * if(locations == null || locations.length == 0) return null;
	 * 
	 * try { Form form = createForm(request, session, InitConfig.getLocationsSelectionFormId());
	 * 
	 * SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA); sessData.currentFormID.set(InitConfig.getLocationsSelectionFormId()); sessData.currentFormMode.set(FormMode.VIEW); sessData.openForm.set(InitConfig.getLocationsSelectionFormId());
	 * 
	 * StringBuffer sb = new StringBuffer(); sb.append("<xml>");
	 * 
	 * UILayout.renderFullScreen(sb); form.renderForm(sb, new Integer(0), "Select Location"); sb.append("<data>"); form.renderData(sb); sb.append("</data>");
	 * 
	 * sb.append("</xml>"); return sb.toString(); } catch (Exception e) { return fatalError(session, e); } }
	 */
		private String selectRole(HttpServletRequest request, boolean includeTheme)
	{
		HttpSession session = getSession(request, false);
		if (session == null)
		{
			return this.error(request, session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null);
		}
		SessionData sessData = getSessionData(session);

		int numberOfRoles = 0;
		try
		{
			IAppUser user = sessData.user.get();
			user.setHcpChecked(false);
			IAppRoleLight[] roles = user.getAppRoles();
			if (roles != null)
			{
				numberOfRoles = roles.length;

				if (numberOfRoles == 0)
				{
					return this.error(request, session, FrameworkErrorCodes.CONFIGURATION, "There are no roles specified for the user");
				}

				// Bypass role selection if only one role
				if (numberOfRoles == 1)
				{
					IAppRoleLight role = roles[0];
					return this.handleSelectRole(request, session, role);
				}

				StringBuffer sb = createResponseString();
				sb.append("<xml>");
				
				if (includeTheme )
				{
					sb.append(this.loadTheme());
				}				
				
				getUILayout(session).renderRoleSelection(sb, roles);
				sb.append("<alerts><removeall/></alerts>");
				sb.append("</xml>");
				return sb.toString();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return this.error(request, session, FrameworkErrorCodes.CONFIGURATION, ex);
		}

		return null;
	}

	private String handleSelectRole(HttpServletRequest request, HttpSession session, IAppRoleLight lightRole)
	{
		try
		{
			SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
			
			RoleFactory roleFactory = new RoleFactory(sessData.domainSession.get());
			if (!roleFactory.hasRoleProvider())
				throw new RuntimeException("No Role Provider available. Cannot handle login.");
			
			IRoleProvider provider = roleFactory.getRoleProvider();
			IAppRole role = provider.getRole(lightRole.getId());
			INavigation nav = role.getRoleNavigation();
			if (nav == null)
				return this.error(request, session, FrameworkErrorCodes.CONFIGURATION, "There is no navigation specified for the role " + role.getName());
			IAppForm startUpForm = nav.getStartupForm();

			setCurrentForm(sessData, startUpForm.getFormId());
			IDynamicNavigation dynamicNavigation = new DynamicNavigation(sessData, nav, new FormAccessLevel(new FormAccessLoader(session), new ContextEvalFactory(sessData.domainSession.get())));
			sessData.currentDynamicNavigation.set(dynamicNavigation);
			sessData.role.set(role);
			
			// FWUI-1803
			sessData.clientSesionTimeout.get().put(sessData.uniqueClientId.get(), new DateTime());						
			sessData.clientAutolockTimer.get().put(sessData.uniqueClientId.get(), role);
			
			// FWUI-1573
			setLoggedInDetails(session, sessData, sessData.user.get());
			
			ITopButtonConfig roleTopButtonConfig = role.getRoleTopButtonConfig();
			sessData.topButtons.set(roleTopButtonConfig == null ? TopButtonConfig.getDefaultConfiguration() : new TopButtonConfig(roleTopButtonConfig));

			INavForm startUpNavForm = nav.getNavForm(startUpForm.getFormId());

			//If startup form is defined via setStartupForm web service method and role doesn't contain startup form throw an error
			if (sessData.defaultStartupForm.get() != null)
			{				
				startUpNavForm = nav.getNavForm(sessData.defaultStartupForm.get());
				if(startUpNavForm == null)
				{
					StringBuffer sb = createResponseString();
					sb.append("<xml><messages><message>");
					sb.append(StringUtils.encodeXML("Configuration error\n\nCannot get startup form!\n Navigation for role '" + role.getName()  + "' does not have any form with id '" + sessData.defaultStartupForm.get() + "'"));
					sb.append("</message></messages></xml>");
					
					getUIEngine(request, session).createSystemLogEntry(SystemLogType.WEB_SERVICE, SystemLogLevel.ERROR,  "Start form was not found");
					return sb.toString();	
				}
			}
			
			if(startUpNavForm == null)
			{
				return this.error(request, session, FrameworkErrorCodes.CONFIGURATION, "Start form was not found");
			}
			if (dynamicNavigation.getFormAccess(startUpNavForm) == FormAccess.NO_ACCESS)
			{
				return this.error(request, session, FrameworkErrorCodes.CONFIGURATION, "Start form is not accessible");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			StringBuffer sb = createResponseString();
			sb.append("<xml><messages><message>");
			sb.append(StringUtils.encodeXML(ex.getMessage()));
			sb.append("</message></messages></xml>");
			
			getUIEngine(request, session).createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.ERROR, ex.toString());
			
			return sb.toString();
		}

		return null;
	}

	private String handleSelectRole(HttpServletRequest request, HttpSession session, SelectRoleEvent event)
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		IAppRoleLight role;
		try
		{
			IAppUser user = sessData.user.get();
			role = user.getAppRole(event.getRole());
			return handleSelectRole(request, session, role);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			StringBuffer sb = createResponseString();
			sb.append("<xml><messages><message>");
			sb.append(StringUtils.encodeXML(ex.getMessage()));
			sb.append("</message></messages></selectrole></xml>");
			return sb.toString();
		}
	}

	/*
	 * private int clearSession(HttpSession session) { if (session != null) { int count = clearSessionData(session); session.invalidate(); session = null; return count; }
	 * 
	 * return 0; } private int clearSessionData(HttpSession session) { if(session != null) { Enumeration e = session.getAttributeNames(); int count = 0; while (e.hasMoreElements()) { count++; session.removeAttribute((String)e.nextElement()); } return count; } return 0; }
	 */
	private String presentationError(HttpServletRequest request, HttpSession session, Exception ex)
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		Integer formName = sessData.currentFormID.get();

		Map map = sessData.configurationModule.get().getRegisteredForms();
		IAppForm tmp = (IAppForm) map.get(formName);

		String caption = sessData.captionOverride.get();
		if (caption != null)
			sessData.captionOverride.remove();
		else
			caption = tmp.getCaption();
		
		return presentationError(request, session, getPresentationErrorTitle(ex), getPresentationErrorMessage(ex), caption, shouldSuspend(ex));
	}

	private String presentationError(HttpServletRequest request, HttpSession session, String title, String message, String formCaption, boolean suspend)
	{
		StringBuffer sb = createResponseString();
		sb.append("<xml>");

		if (!suspend)
		{
			// In case this is the first form that has an error we need to
			// render the navigation so the user can still access other forms

			SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
			INavigation nav = sessData.currentDynamicNavigation.get().getNavigation();

			if (sessData.previousNonDialogFormID.get() == null)
			{
				try
				{
					new DynamicNavigation(sessData, nav, new FormAccessLevel(new FormAccessLoader(session), new ContextEvalFactory(sessData.domainSession.get()))).renderForm(sb);
				}
				catch (Exception ex)
				{
					suspend = true;
				}
			}
		}
		
		ISystemLog eventItem = getUIEngine(request, session).createSystemLogEntry(SystemLogType.APPLICATION, suspend ? SystemLogLevel.FATALERROR : SystemLogLevel.ERROR, message);
		
		if(ConfigFlag.GEN.RELEASE_MODE.getValue() && suspend && eventItem != null)
		{
			message = "An error occured in the application, we are sorry for the inconvenience. If the error persists, please notify the administrator of the error code displayed above.";
			title = "Application Error (Code: " + eventItem.getSystemLogEventId() + ")";
			formCaption = "Application Error";
		}
		
		renderPresentationError(title, message, formCaption, formCaption + " - " + constructLoginString(session), suspend, sb);
		return sb.toString();
	}

	private void renderPresentationError(String title, String message, String formCaption, String browserTitle, boolean suspend, StringBuffer sb)
	{
		sb.append("<formError");

		if (suspend)
			sb.append(" suspend=\"true\"");

		if (formCaption != null && formCaption.length() > 0)
			sb.append(" caption=\"" + StringUtils.encodeXML(formCaption) + "\"");

		sb.append(" title=\"" + StringUtils.encodeXML(title) + "\"");
		sb.append(" browserTitle=\"" + StringUtils.encodeXML(browserTitle) + "\">");
		sb.append("<msg>");
		sb.append(StringUtils.encodeXML(message));
		sb.append("</msg>");
		sb.append("</formError>");		
		sb.append("</xml>");
	}

	private String getPresentationErrorTitle(Exception ex)
	{
		if (ex != null)
		{
			if (ex instanceof FormOpenException)
				return "Form Open Problem";
			else if (ex instanceof PresentationLogicException)
				return "Form Problem";
			else if (ex instanceof FormMandatoryContextMissingException)
				return "Form Context Problem";
			else if (ex instanceof FormMandatoryLookupMissingException)
				return "Form Lookup Problem";
		}

		return "Unknown Form Problem";
	}

	private boolean shouldSuspend(Exception ex)
	{
		if (ex != null)
		{
			if (ex instanceof FormOpenException)
				return false;
			else if (ex instanceof PresentationLogicException)
				return false;
			else if (ex instanceof FormMandatoryLookupMissingException)
				return false;
			else if (ex instanceof FormMandatoryContextMissingException)
				return !ConfigFlag.GEN.RELEASE_MODE.getValue();
		}

		return true;
	}

	private String getPresentationErrorMessage(Exception ex)
	{
		if (ex != null)
		{
			if (ex instanceof PresentationLogicException && ex.getMessage() != null && ex.getMessage().length() > 0)
				return ex.getMessage();
			if (ex instanceof FormMandatoryLookupMissingException && ex.getMessage() != null && ex.getMessage().length() > 0)
				return ex.getMessage();
			if (ex instanceof FormMandatoryContextMissingException && ex.getMessage() != null && ex.getMessage().length() > 0)
				return ex.getMessage();
		}

		if (ex != null && ex.getMessage() != null)
			return "Unknown error occured while loading the form. Exception message was " + ex.getMessage();

		return "Unknown error occured while loading the form.";

	}

	private String fatalError(HttpServletRequest request, HttpSession session, Throwable ex)
	{
		String title = ex.getClass().getName();
		String caption = "Fatal Exception";
		String message = getExceptionMessage(ex, session);		
		boolean suspend = true;
		mailError(message);
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);	
		ex.printStackTrace(ps);		
				
		return presentationError(request, session, title, message, caption, suspend);
	}

	private String getExceptionMessage(Throwable t, HttpSession session)
	{
		StringBuffer sb = new StringBuffer();
		BuildInfo buildInfo = Configuration.getBuildInfo();
		if (buildInfo != null)
		{
			sb.append(buildInfo.getName() + " Version " + buildInfo.getAppVersion() + " (" + buildInfo.getAppTimestamp() + ")\n");
			sb.append("War File Version " + buildInfo.getWarVersion() + " (" + buildInfo.getWarTimestamp() + ")\n");
			sb.append("Framework Version " + buildInfo.getFrameworkVersionInfo() + " (" + buildInfo.getFrameworkTimestamp() + ")\n");
			sb.append("Domain Version " + buildInfo.getDomainVersionInfo() + " (" + buildInfo.getDomainTimestamp() + ")\n");
			sb.append("Base Version " + buildInfo.getBaseVersionInfo() + " (" + buildInfo.getBaseTimestamp() + ")\n\n");
		}

		sb.append("Application Context: ").append(ConfigFlag.APP_CONTEXT_NAME.getValue()).append("\n");
		sb.append("Host Name: ").append(ConfigFlag.HOST_NAME.getValue()).append("\n");
		sb.append("Database: ").append(ConfigFlag.DBNAME.getValue()).append("\n");
		sb.append("Server Start Time: ").append(ConfigFlag.START_TIME.getValue()).append("\n\n");
		
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		if (sessData != null)
		{
			IAppUser user = sessData.user.get();
			if (user != null)
			{
				sb.append("+++++++++++++++++++++++++++++++++++++++++++++++++++\n");
				sb.append("Logged in user was " + user.getUsername() + "\n");
				sb.append("User " + user.getUsername() + " was logged in from host " + user.getHostName() + "\n");
				sb.append("User " + user.getUsername() + " logged in at " + user.getLoginTime() + "\n");
				sb.append("+++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
			}
		}

		sb.append("Exception Stack Trace\n");
		sb.append("-----------------------\n");

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);
		t.printStackTrace(ps);
		sb.append(bo.toString());

		return sb.toString();
	}

	private void mailError(String msg)
	{
		Notifier nt = new Notifier(msg);
		nt.start();
	}

	private String error(HttpServletRequest request, HttpSession session, int errorCode, Throwable ex)
	{
		if (ex != null)
		{
			String title = ex.getClass().getName();
			String caption = "Exception" ;
			String message = getExceptionMessage(ex, session);
			boolean suspend = true;
			if (ConfigFlag.GEN.RELEASE_MODE.getValue())
			{
				suspend = false;
			}
			mailError(message);
			return presentationError(request, session, title, message, caption, suspend);	
		}		
		
		return error(request, session, errorCode, "");
	}
	
	private String error(HttpServletRequest request, HttpSession session, int errorCode, String msg)
	{
		if (errorCode == FrameworkErrorCodes.SESSION_EXPIRED || errorCode == FrameworkErrorCodes.PARSE_EXCEPTION)
		{
			StringBuffer sb = createResponseString();
			sb.append("<xml>");
			getUILayout(session).renderLogin(sb);		
			
			if(errorCode == FrameworkErrorCodes.SESSION_EXPIRED)
			{
				getUIEngine(request, session).createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.INFORMATION, "Logout successful (due to session expired)");				
				sb.append("<messages><message>Session has expired.</message></messages>");
			}
			else if(errorCode == FrameworkErrorCodes.PARSE_EXCEPTION)
			{
				getUIEngine(request, session).createSystemLogEntry(SystemLogType.APPLICATION, SystemLogLevel.WARNING, "Connection closed");
				sb.append("<messages><message>Connection closed.</message></messages>");
			}
			
			//If SESSION_EXPIRED reset HeartBeat
			sb.append("<heartbeat reset=\"true\" />");						
			//If SESSION_EXPIRED reset Autolock
			sb.append("<autoLockTimer reset=\"true\" />");
			
			sb.append("</xml>");			
			return sb.toString();
		}		

		String title = "Application error occurred (" + errorCode + ").";
		String caption = "Error";
		String message = msg;
		boolean suspend = true;
		if (ConfigFlag.GEN.RELEASE_MODE.getValue())
		{
			suspend = false;
		}
		LocalLogger.warn(message);
		mailError(message);
		
		return presentationError(request, session, title, message, caption, suspend);
	}

	private void debugRequestInfo(HttpServletRequest request, HttpServletResponse response, StringBuilder input, boolean isRequest, boolean isResponse) throws DocumentException, IOException 
	{	
		StringBuilder requestInfo = new StringBuilder();
		
		if (isRequest)
		{
			requestInfo.append("\n#######################################################");
			requestInfo.append("#################  Request to CNHost  #################");
			requestInfo.append("#######################################################");
		}
		else if (isResponse)
		{
			requestInfo.append("\n##########################################################");
			requestInfo.append("#################  Response from CNHost  #################");
			requestInfo.append("##########################################################");
		}		
				
		requestInfo.append("\nRequest length is " + request.getContentLength() + " bytes.");		
		requestInfo.append("\nRequest type is " + request.getContentType());	
        requestInfo.append("\nInput stream: \n");
        Document doc = DocumentHelper.parseText(input.toString());  
        StringWriter sw = new StringWriter();  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        XMLWriter xw = new XMLWriter(sw, format);  
        xw.write(doc);         
        requestInfo.append(sw.toString());		
        requestInfo.append("\n-----------------");
        
        System.out.println(requestInfo.toString());                
		
		System.out.println("Session Information");
	    System.out.println("-------------------");
	    System.out.println("Identifier:	" + getSession(request).getId());
	    System.out.println("Created:	" + new java.util.Date(getSession(request).getCreationTime()));
	    System.out.println("Last Accessed:	" + new java.util.Date(getSession(request).getLastAccessedTime()));
	    System.out.println("New Session:	" + getSession(request).isNew());
	    
	    Enumeration names = getSession(request).getAttributeNames();
	    while ( names.hasMoreElements() ) 
	    {		    	 	    	 
	    	 System.out.println("\n");
	    	 String name = (String) names.nextElement();
	         System.out.print("Name: " + name);		         
	         System.out.print("\nValue: " + getSession(request).getAttribute(name));
	         
	         if (name.equals("SessionData"))
	         {		        	 
	        	 if(((SessionData)getSession(request).getAttribute(name)).currentFormID.get() != null)
	        	 {	
	        		 System.out.print("\n");
	        		 System.out.print("Name: currentFormID");
			         System.out.println("Value: " + ((SessionData)getSession(request).getAttribute(name)).currentFormID.get().toString());
			         System.out.print("\n");
	        	 }
	        	 if(((SessionData)getSession(request).getAttribute(name)).dialogID.get() != null)
	        	 {
	        		 System.out.print("Name: dialogID");
			         System.out.println("Value: " +  ((SessionData)getSession(request).getAttribute(name)).dialogID.get().toString());
			         System.out.print("\n");
	        	 }				        	 
	        	 if(((SessionData)getSession(request).getAttribute(name)).openForm.get() != null)
	        	 {
	        		 System.out.print("Name: openForm");
			         System.out.println("Value: " +  ((SessionData)getSession(request).getAttribute(name)).openForm.get().toString());
			         System.out.print("\n");
	        	 }
	        	 if(((SessionData)getSession(request).getAttribute(name)).patientInfo.get() != null)
	        	 {		        		
	        		 System.out.print("Name: patientInfo");
			         System.out.println("Value: " + ((SessionData)getSession(request).getAttribute(name)).patientInfo.get().toString());
			         System.out.print("\n");
	        	 }
	        	 if(((SessionData)getSession(request).getAttribute(name)).patientId.get() != null)
	        	 {		        		 
	        		 System.out.print("Name: patientId");
			         System.out.println("Value: " +  ((SessionData)getSession(request).getAttribute(name)).patientId.get().toString());				         
	        	 }
	         }		         		         
	    }
	    
	    if (isRequest)
		{
	    	System.out.print("\n#######################################################");
	    	System.out.print("#################  End Request to CNHost  #############");
	    	System.out.print("#######################################################\n");
		}
		else if (isResponse)
		{
			System.out.print("\n##########################################################");
			System.out.print("#################  End Response from CNHost  #############");
			System.out.print("##########################################################\n");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnsupportedEncodingException, IOException, DocumentException, ParserConfigurationException, SAXException 
	{
		boolean requireRedirect = false;
		boolean shouldRenderTheme = false;
		boolean shouldRenderLogin = false;
		
		String input = "";
		
		try
		{
			input = this.read(request, response);
			if(input == null || input.trim().length() == 0)
				throw new IOException("Invalid request");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			LocalLogger.error("Error reading request: " + e.getMessage(), e);
			throw new ServletException(e);
		}
		
		java.util.Date recvd = new java.util.Date();

		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("Request: " + input);

		if (ConfigFlag.FW.REQUEST_LOGGING.getValue() && !ConfigFlag.GEN.SESSION_LOG_LOCATION.getValue().equals(""))
		{
			try
			{
				FileWriter file = new FileWriter(ConfigFlag.GEN.SESSION_LOG_LOCATION.getValue() + "/request_messages.xml", true);
				file.write(input.replaceAll("\r", "").replaceAll("\n", "") + "\n");
				file.close();
			}
			catch (IOException ex)
			{
				LocalLogger.error("Failed to write incoming request message. " + ex.getMessage(), ex);
			}
		}
		
		// Session and xmlLogger required to store xml messages
		HttpSession session = getSession(request);		
		
		Logger xmlLogger = null;
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		if (sessData != null && (xmlLogger = sessData.xmlLogger.get()) != null)
		{
			xmlLogger.warn("Request: " + input);
		}
		
		EventManager events = null;
		String output = null;	
		
		try
		{
			events = EventParser.parse(sessData, input);			
		}
		catch (SAXParseException ex)
		{
			ex.printStackTrace();
			LocalLogger.warn("Request causing exception: " + input);
			getUIEngine(request).createSystemLogEntry(SystemLogType.JSCN, SystemLogLevel.FATALERROR, "Error occured while parsing the request: " + input);			
			throw new ServletException(ex.getMessage());			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			LocalLogger.warn("Request causing exception: " + input);
			getUIEngine(request).createSystemLogEntry(SystemLogType.JSCN, SystemLogLevel.FATALERROR, "Error occured while parsing the request: " + input);
			throw new ServletException(ex.getMessage());
		}
		
		if(events.hasAppParamEvent())
		{
			AppParamHandlerFactory factory = new AppParamHandlerFactory(sessData.domainSession.get());
			if(factory.hasProvider())
			{
				sessData.appParams.set(events.getAppParamEvent().getAppParams());
			}
		}
		
		/*if (sessData.patientAccessRestricted.get() != null &&
				sessData.patientAccessRestricted.get().equals("True") &&
				sessData.previouslySelectedPatients.get() != null &&
				sessData.previouslySelectedPatients.get().get(0).getISelectedPatientID() != sessData.restrictPatientId.get())
		{
			StringBuffer sb = createResponseString();		
			sb.append("<xml>");				
			sb.append("<messages><message>" + ims.framework.utils.StringUtils.encodeXML("Test") + "</message></messages>");
			sb.append("</xml>");
			
			sessData.patientAccessRestricted.set("False");
			
			write(request, response, sb.toString());	
			return;
		}*/
		
		if (events.isEmptyRequest())
		{
			//Initialise heartBeatCounter
			if (sessData.heartBeatCounter.get() == null) {
				sessData.heartBeatCounter.set(0);
			}
			
			Integer counter = sessData.heartBeatCounter.get();	
			counter ++ ;
			
			int interval = ConfigFlag.UI.HEART_BEAT_TIMER.getValue();
			int timeOut = ConfigFlag.GEN.SESSION_TIMEOUT.getValue();
			
			if (((interval * counter)- interval) >= timeOut)
			{
				String resp = this.error(request, session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null);
				write(request, response, recvd, xmlLogger, resp);
				
				//Reset heartBeatCounter
				sessData.heartBeatCounter.set(0);		
				
				return;
			}			
			
			StringBuffer sb = createResponseString();
			
			sb.append("<xml>");			
			sb.append("</xml>");
			
			write(request, response, recvd, xmlLogger, sb.toString());			
			
			//Save Incremented heartBeatCounter
			sessData.heartBeatCounter.set(counter);
			
			return;			
		}
		
		//Session Timeout
		HashMap<String, DateTime> roleSessionTimeout = sessData.clientSesionTimeout.get();
		if (roleSessionTimeout != null)
		{	
			String clientID = sessData.uniqueClientId.get();
			IAppRole currentRole = sessData.role.get();
			
			if (currentRole != null &&
					currentRole.getRoleSessionTimeout() != null &&
					 	currentRole.getRoleSessionTimeout().intValue() > 0)
			{
				if(events != null && events.get(0)!= null &&
						events.get(0) instanceof LogoutEvent)
				{
					roleSessionTimeout.remove(clientID);			
				}
				
				if(roleSessionTimeout.get(sessData.uniqueClientId.get()) != null && 
						roleSessionTimeout.get(sessData.uniqueClientId.get()) instanceof DateTime)
				{	
					DateTime lastSeen = (DateTime)roleSessionTimeout.get(sessData.uniqueClientId.get());
					DateTime currentDate = new DateTime();
					
					int diff = DateTime.minutesDiff(lastSeen, currentDate);
					int trigger = currentRole.getRoleSessionTimeout().intValue();
					
					if (diff >= trigger)
					{
						if (!ConfigFlag.GEN.RELEASE_MODE.getValue())
							 System.out.print("Session timeout for user '" + sessData.user.get().getUsername() + "' using role '" + currentRole.getName() + "' : total timeout: '" + diff + "' minute(s), timeout session value: '" + trigger + "' minute(s)");
						
						String resp = this.error(request, session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null);
						write(request, response, recvd, xmlLogger, resp);
							
						roleSessionTimeout.remove(clientID);		
						sessData.selectedLocation.set(null);  // FWUI-1855 WDEV-22741 - session timeout, we need to clear the selected location
						// return; WDEV-22741 
					}	
					else
					{
						roleSessionTimeout.put(clientID, new DateTime());
						sessData.clientSesionTimeout.set(roleSessionTimeout);
					}
				}
				else
				{
					//UpdateTheme event is send once at login.
					if(events != null && events.get(0)!= null)
					{
						if(!(events.get(0) instanceof UpdateTheme) && !(events.get(0) instanceof LogoutEvent)) // FWUI-1855 WDEV-22741 -  do not reset the timeout for logout event
						{
							roleSessionTimeout.put(clientID, new DateTime());
							sessData.clientSesionTimeout.set(roleSessionTimeout);		
						}
					}
				}
			}
		}
		
		if (events.hasLoginRequestEvent()) 
		{
			output = this.login(request);							
		}
		else
		{
			boolean isPostBack = true;
			boolean isSecurityLogin = events.hasSecurityTokenLoginRequestEvent();

			if (session.isNew())
			{
				if (input.indexOf("<loginEvent") < 0 && !isSecurityLogin)
					output = this.error(request, session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null);				
			}
			else if (isSecurityLogin)
			{
				// JME: 20061211: Found this to be the only way to force getting a new httpsession from this request.
				session.invalidate();
				session = getSession(request, true);
				sessData = new SessionData();
				session.setAttribute(SessionConstants.SESSION_DATA, sessData);
			}

			if (output == null)
			{
				if (sessData == null)
				{
					sessData = new SessionData();
					session.setAttribute(SessionConstants.SESSION_DATA, sessData);
				}

				initializeSessionDataVariables(sessData);
				
				if (sessData.dialogOpened.get() != null)
				{
					sessData.dialogOpened.remove();
					input = "";
					events = new EventManager();
					isPostBack = false;
				}

				if (events != null)
				{
					if (events.size() > 0)
					{
						//PDS Stuff - Process SmartcardTicketReceivedEvent event first
						if (events.size() > 1)
						{
							if (events.size() == 2 && events.get(1) instanceof SmartcardTicketReceivedEvent)
							{
								Object secondEvent = events.get(1);
								if (secondEvent instanceof SmartcardTicketReceivedEvent)
								{
									processSmartcardTicketReceived(sessData, (SmartcardTicketReceivedEvent)secondEvent);
								}
							}
							if (events.size() == 3 && events.get(2) instanceof SmartcardTicketReceivedEvent)
							{
								Object thirdEvent = events.get(2);
								if (thirdEvent instanceof SmartcardTicketReceivedEvent)
								{
									processSmartcardTicketReceived(sessData, (SmartcardTicketReceivedEvent)thirdEvent);
								}
							}
						}
						
						Object event = events.get(0);
						if (event instanceof LoginEvent)
						{
							output = this.handleLogin(request, session, (LoginEvent) event, getUIEngine(request, session));

							if (events.size() > 1)// && output == null)
							{
								Object secondEvent = events.get(1);
								if (secondEvent instanceof StoredLocationsEvent)
								{
									processClientStoredLocations(session, (StoredLocationsEvent) secondEvent);
								}
							}

							if (output == null) // redirect to select role
							{
								output = this.selectRole(request,false);
							}
							if (output == null && showLocationSelection(sessData, getUIEngine(request, session))) // redirect to location selection
							{
								sessData.startupMode.set(Boolean.TRUE);
								sessData.currentUILayoutState.set(UILayoutState.FULLSCREEN);
								sessData.uiLayoutChanged.set(true);
								sessData.openForm.set(InitConfig.getLocationsSelectionFormId());
							}

							if (output == null)
							{
								isPostBack = false;
							}
						}
						else if (event instanceof SecurityTokenLoginEvent)
						{
							shouldRenderTheme = true;							
							output = this.handleLogin(request, session, (SecurityTokenLoginEvent) event, getUIEngine(request, session));
														
							if (output == null)
							{
								isPostBack = false; // send the first form								
							}
							else
							{
								shouldRenderLogin = true;
							}
							
							if (output == null) // redirect to select role
							{
								if (sessData.role.get() == null)
								{
									output = this.selectRole(request,false);
								}
								//If role was set by web service method setUserRole
								else
								{										
									output = this.handleSelectRole(request, session, sessData.role.get());										
								}
							}
							
							ILocation savedLocation = null;													
							if(sessData.selectedLocation != null)
								savedLocation = sessData.selectedLocation.get();

							sessData.selectedLocation.set(savedLocation);
							
							if (output == null && showLocationSelection(sessData, getUIEngine(request, session))) // redirect to location selection
							{
								sessData.startupMode.set(Boolean.TRUE);
								sessData.currentUILayoutState.set(UILayoutState.FULLSCREEN);
								sessData.uiLayoutChanged.set(true);
								sessData.openForm.set(InitConfig.getLocationsSelectionFormId());
							}

							if (output == null)
							{
								isPostBack = false;
							}
						}
						else if (event instanceof NewPasswordEvent)
						{
							UIEngine uiEngine = getUIEngine(request, session);
							output = this.handleNewPassword(session, (NewPasswordEvent) event, uiEngine);

							if (output == null) // redirect to select role
							{
								output = this.selectRole(request,false);
							}
							if (output == null && showLocationSelection(sessData, uiEngine)) // redirect to location selection
							{
								sessData.startupMode.set(Boolean.TRUE);
								sessData.currentUILayoutState.set(UILayoutState.FULLSCREEN);
								sessData.uiLayoutChanged.set(true);
								sessData.openForm.set(InitConfig.getLocationsSelectionFormId());
							}
							if (output == null) // Only one role, go to first form
							{
								isPostBack = false; // send the first form
							}
						}						
						else if (event instanceof SelectRoleEvent)
						{
							output = this.handleSelectRole(request, session, (SelectRoleEvent) event);
							if (output == null)
							{
								if (showLocationSelection(sessData, getUIEngine(request, session))) // redirect to location selection
								{
									sessData.startupMode.set(Boolean.TRUE);
									sessData.currentUILayoutState.set(UILayoutState.FULLSCREEN);
									sessData.uiLayoutChanged.set(true);
									sessData.openForm.set(InitConfig.getLocationsSelectionFormId());
								}
								else
								{
									sessData.currentUILayoutState.set(UILayoutState.DEFAULT);
									isPostBack = false; // send the first form
								}
							}
						}
						else if (event instanceof ScreenUnlock)
						{
							try
							{
								output = this.handleScreenUnlock(session, (ScreenUnlock) event);
							}
							catch (Exception e)
							{
								throw new RuntimeException(e);
							}

							if (output == null)
							{
								StringBuffer sb = createResponseString();
								sb.append("<xml>");
								getUILayout(session).renderScreenUnlockRequest(sb);
								sb.append("</xml>");
								write(request, response, recvd, xmlLogger, sb.toString());
								return;
							}
						}
						else if (event instanceof DialogWasClosed)
						{
							if (((DialogWasClosed) event).getID() == 999)
							{
								StringBuffer sb = createResponseString();
								sb.append("<xml>");
								getUILayout(session).renderScreenUnlock(sb, isRie(sessData), sessData.formDarkHeaderHeight.get());
								sb.append("</xml>");
								write(request, response, recvd, xmlLogger, sb.toString());
								return;
							}

							// discardORMsession(session);
							sessData.handleDialogClosed();
							events.clear();

							// force a form open event to be fired if Recorded in Error
							Integer dialogID = sessData.dialogID.get();
							DialogResult dialogResult = sessData.dialogResult.get();
							
							Integer rieDialogFormID = InitConfig.getRecordedInErrorFormId();
							if (rieDialogFormID != null && rieDialogFormID.intValue() > 0 && dialogID != null && dialogID.intValue() == rieDialogFormID.intValue() && dialogResult != null)
							{
								Integer currentFormID = sessData.currentFormID.get();
								
								if (currentFormID != null)
								{
									// fire the event to the form
									try
									{
										Form currentForm = createForm(request, session, currentFormID);										
										currentForm.fireRIEDialogClosedEvent(dialogResult);
									}
									catch (Exception e)
									{
										throw new RuntimeException(e);
									}
									
									// clear dialog details
									sessData.dialogID.remove();
									sessData.dialogResult.remove();

									// clearing data for closed dialog
									Integer previousFormID = sessData.previousForm.get();
									IAppForm previousForm = null;
									if (previousFormID != null)
									{
										Map map = sessData.configurationModule.get().getRegisteredForms();
										previousForm = (IAppForm) map.get(previousFormID);
										if (previousForm != null)
										{
											HashMap formsData = sessData.formsData.get();
											formsData.remove(new Integer(previousForm.getFormId()));
										}
									}

									// fire the open event on the form
									sessData.fireOpenEvent.set(Boolean.TRUE);
								}
							}
						}
						else if (event instanceof LogoutEvent)
						{							
							if (configuration != null)
							{
								configuration.logout();
								
								// FWUI-1228
								// If this is a mixed project i.e. both DTO and Hibernate, we may need to logout of a dto connection
								DomainSession domSess = sessData.domainSession.get();
								if (domSess != null)
								{
									Connection connection = (Connection) domSess.getAttribute(DTODomainImplementation.DTO_CONNECTION);
									if (connection != null)
										connection.logout();
								}

							}
							
							String logoutMessage =  (((LogoutEvent) event).isIE()) ? "Logout successful (due to browser close)" : "Logout successful";							
							if(sessData!=null //http://jira/browse/WDEV-16016
									&&sessData.user!=null
									&&sessData.user.get() != null) //user null => already logged out and just closing browser
							{												
								getUIEngine(request, session).createSystemLogEntry(SystemLogType.AUTHENTICATION, SystemLogLevel.INFORMATION, logoutMessage);
							}
							clearAndInvalidateCurrentSession(request);
							if (((LogoutEvent) event).isIE())
								return;
							output = this.login(request);
						}
					}
					
					if (output == null)
					{
						try
						{
							boolean newForm = true;

							sessData.errorsTitle.set("Error");
							Integer formName = sessData.currentFormID.get();

							Form currentForm = null;							
							IGenericIdentifier[] formIdentifiers = null;
							boolean shouldFireFormOpenEventByInternalAlert = false;
							if (isPostBack)
							{
								if (sessData.openForm.get() != null && sessData.openForm.get() != formName)
									sessData.fireOpenEvent.set(Boolean.FALSE);
								
								currentForm = formName == null ? null : createForm(request, session, formName);
								newForm = false;

								for (int i = 0; i < events.size(); ++i)
								{
									Object event = events.get(i);

									if (LocalLogger.isDebugEnabled())
										LocalLogger.debug("Event Fire: " + event.getClass().getName());

									if (event instanceof IControlEvent)
									{
										if(!currentForm.fireEvent((IControlEvent) event))
										{
											//throw new FrameworkInternalException("Unable to find the control event handler for the event '" + event.getClass().getName() + "', Control Id: " + ((IControlEvent) event).getControlID() +", Form: " + currentForm.toString() + ".");
										}
									}
									else if (event instanceof CustomEvent)
									{
										currentForm.fireCustomEventEvent((CustomEvent) event);
									}
									else if (event instanceof IMenuEvent)
									{
										currentForm.fireEvent((IMenuEvent) event);
									}
									else if (event instanceof SearchEvent)
									{
										if(((SearchEvent)event).isLightWeight())
										{
											StringBuffer sb = createResponseString();
											sb.append("<xml>");
											renderSearchResult(sb, session, getUIEngine(request, session), ((SearchEvent)event).isLightWeight(), ((SearchEvent)event).getText());
											sb.append("</xml>");
											write(request, response, recvd, xmlLogger, sb.toString());
											return;
										}
										else
										{
											sessData.lastSearchText.set(((SearchEvent)event).getText());
										}
									}
									else if (event instanceof SearchResultSelectionEvent)
									{
										newForm = true;
										
										int searchId = ((SearchResultSelectionEvent)event).getId();
										ISearchResult[] lastSearchResults = sessData.lastSearchResults.get();
										
										INavForm form = null;
										if(lastSearchResults != null)
										{
											for(int x = 0; x < lastSearchResults.length; x++)
											{
												if(lastSearchResults[x].getId() == searchId)
												{
													form = lastSearchResults[x].getLink();
													break;
												}
											}
										}
											
										if(form != null)
										{
											Integer newFormName = form.getAppForm().getFormId();
											
											if (sessData.configurationModule.get().getRegisteredForms().get(newFormName) == null)
												throw new PresentationLogicException("Form not registered (ID: " + newFormName.toString() + ").");
										
											// cleaning up the old form data
		 									HashMap formsData = sessData.formsData.get();
											formsData.put(newFormName, new FormData());
	
											formName = newFormName;
											requireRedirect = true;
											if(form != null)
												formIdentifiers = form.getIdentifiers();
										}
									}
									else if (event instanceof TimerEvent)
									{
										currentForm.fireEvent((TimerEvent) event);
									}
									else if (event instanceof MessageBoxEvent)
									{
										currentForm.fireMessageBoxClosedEvent((MessageBoxEvent) event);
									}
									else if (event instanceof IFormIdSelection)
									{
										newForm = true;
										int formOrNavigationID = ((IFormIdSelection) event).getFormID();										
										if (formOrNavigationID == 999)
										{
											// lock request
											StringBuffer sb = createResponseString();
											sb.append("<xml>");
											
											String lockMessage = "Session locked";
											IAppUser user = sessData.user.get();
											if(user != null && user.getUsername() != null && user.getUserRealName() != null)
											{												
												lockMessage += " by " + user.getUserRealName() + " (" + user.getUsername() + ")";
											}
											
											lockMessage += ".";
											getUILayout(session).renderScreenLock(sb, lockMessage);
											sb.append("</xml>");
											write(request, response, recvd, xmlLogger, sb.toString());
											return;
										}
										
										INavForm form = null;
										Integer newFormName = null;
										
										if(event instanceof NavigationSelected)
										{
											form = sessData.currentDynamicNavigation.get().getForm(formOrNavigationID);
											if(form != null)
											{
												newFormName = new Integer(form.getAppForm().getFormId());
											}
											else
											{
												StringBuffer sb = createResponseString();
												sb.append("<xml></xml>");
												write(request, response, recvd, xmlLogger, sb.toString());
												return;
											}
										}
										else
										{
											newFormName = formOrNavigationID;		
										}
										
										if (sessData.configurationModule.get().getRegisteredForms().get(newFormName) == null)
											throw new PresentationLogicException("Form not registered (ID: " + newFormName.toString() + ").");

										// cleaning up the old form data
										HashMap formsData = sessData.formsData.get();
										formsData.put(newFormName, new FormData());

										// JME: 20061114: Due to Nav allowing same form > 1
										// if (!newFormName.equals(formName))
										{
											formName = newFormName;
											requireRedirect = true;
											if(form != null)
												formIdentifiers = form.getIdentifiers();
										}										
									}
									else if (event instanceof TopButtonClicked)
									{
										UIEngine uiEngine = getUIEngine(request, session);
										TopButtonClicked tmp = (TopButtonClicked) event;
										INavigation nav = sessData.currentDynamicNavigation.get().getNavigation();

										int id = tmp.getID();
										Integer newFormName = null;
										
										if(TopButton.HOME.getID() == id)
										{
											newFormName = new Integer(nav.getStartupForm().getFormId());
											if (!newFormName.equals(formName))
											{
												formName = newFormName;
												requireRedirect = true;
											}
										}
										else if(TopButton.PATIENT_SEARCH.getID() == id)
										{
											newFormName = new Integer(nav.getPatSearchForm().getFormId());
											if (!newFormName.equals(formName))
											{
												formName = newFormName;
												requireRedirect = true;
											}
										}
										else if(TopButton.SELECT_ROLE.getID() == id)
										{
											IAppUser user = sessData.user.get();
											if (user.getAppRoles().length == 1)
											{
												StringBuffer sb = createResponseString();
												sb.append("<xml><messages><message>User " + user.getUsername() + " has only one Role assigned.</message></messages></xml>");
												write(request, response, recvd, xmlLogger, sb.toString());
												return;
											}
											
											session = getSession(request, false);
											if (session != null)
												session.invalidate();
											session = getSession(request, true);

											ILocation savedLocation = null;													
											if(sessData.selectedLocation != null)
												savedLocation = sessData.selectedLocation.get();
											
											ArrayList<Alert> currentAlerts = new ArrayList<Alert>();													
											if(sessData.alerts != null)
												currentAlerts = sessData.alerts.get();
																								
											sessData = new SessionData();
											session.setAttribute(SessionConstants.SESSION_DATA, sessData);
											
											
											// we need to restore the selected location otherwise the user will be prompted again 
											sessData.selectedLocation.set(savedLocation);

											sessData.configurationModule.set(configuration);
											sessData.user.set(user);
											sessData.formsData.set(new HashMap());
											sessData.currentFormMode.set(FormMode.VIEW);
											sessData.fireOpenEvent.set(Boolean.TRUE);
											sessData.alerts.set(new ArrayList<Alert>());
											
											// the old alerts will need to be removed
											sessData.removedAlerts.set(currentAlerts);
											
											uiEngine.clearAlerts();

											output = this.selectRole(request,false);
											write(request, response, recvd, xmlLogger, output);
											return;											
										}
										else if(TopButton.PRINT.getID() == id)
										{
											Integer printDialogFormID = InitConfig.getPrintFormId();											
											if (printDialogFormID != null && printDialogFormID.intValue() > 0)
											{
												formName = printDialogFormID;
												uiEngine.open(new ims.domain.FormName(printDialogFormID.intValue()));
											}
											else
											{
												uiEngine.showMessage("Print functionality is not supported by the application.");
											}
										}
										else if(TopButton.RECORDED_IN_ERROR.getID() == id)
										{
											if (sessData.listRIERecordsOnly.get() != null && sessData.listRIERecordsOnly.get().booleanValue())
											{
												uiEngine.showMessage("Recorded in error functionality has been disabled in this context.");
											}
											else
											{
												currentForm.fireRIEDialogOpenedEvent();
												formName = processRecordedInErrorTopButtonClick(request, session, formName);
											}
										}
										else if(TopButton.AUDIT_VIEW.getID() == id)
										{
											formName = processAuditViewTopButtonClick(request, session, formName);
										}
										else if(TopButton.UPLOAD.getID() == id)
										{
											processUploadDownloadTopButtonClick(uiEngine, session, false);
										}
										else if(TopButton.DOWNLOAD.getID() == id)
										{
											processUploadDownloadTopButtonClick(uiEngine, session, true);
										}
										else if(TopButton.RECORDING_ON_BEHALF_OF.getID() == id)
										{
											formName = processRecordingOnBehalfOfTopButtonClick(request, session, formName);
										}
										else if(TopButton.MANAGE_LOCATIONS.getID() == id)
										{
											formName = processLocationsManagerTopButtonClick(request, session, formName);
										}
										else if(TopButton.PAS_CONTACTS.getID() == id)
										{
											formName = processPASContactsTopButtonClick(request, session, formName);
										}
										else if(TopButton.ORDER_ENTRY.getID() == id)
										{
											formName = processOrderEntryTopButtonClick(request, session, formName);
										}
										else if(TopButton.PATIENT_SUMMARY.getID() == id)
										{
											formName = processPatientSummaryTopButtonClick(request, session, formName);
										}
										else if(TopButton.CHANGEPASSWORD.getID() == id)
										{
											formName = processChangePasswordTopButtonClick(request, session, formName);
										}
										else if(TopButton.LOCK_SCREEN.getID() == id)
										{
											StringBuffer sb = createResponseString();
											sb.append("<xml>");
											getUILayout(session).renderScreenLockInitialization(sb, response.encodeURL("CNHost"));
											sb.append("</xml>");
											write(request, response, recvd, xmlLogger, sb.toString());
											return;
										}
										else if(TopButton.DASHBOARD.getID() == id)
										{	
											Integer patientId = sessData.patientId.get();
											String dashboardUrl = ConfigFlag.GEN.PATIENT_DASHBOARD_URL.getValue();
											
											if(dashboardUrl != null && dashboardUrl.length() > 0 && patientId != null)
											{
												List<UrlParam> params = new ArrayList<UrlParam>();
												params.add(new UrlParam("PID", patientId.toString()));
												uiEngine.openDashboardCloseableOnContextChange(dashboardUrl, params);
											}
											/*
											if(dashboardUrl.trim() != "" && patientId != null)
											{
												String finalUrl = dashboardUrl + "&PID=" + patientId.intValue();
											
												List<WindowParam> params = new ArrayList<WindowParam>();
											 		params.add(new WindowParam("FullScreen","true"));
											 		params.add(new WindowParam("ToolBar","false"));
											 		params.add(new WindowParam("StatusBar","false"));
											 		params.add(new WindowParam("MenuBar","false"));
											 		params.add(new WindowParam("AddressBar","false"));
											 		params.add(new WindowParam("Resizable","true"));		
											 		params.add(new WindowParam("Visible","true"));		
											 		//params.add(new WindowParam("Height","800"));
											 		//params.add(new WindowParam("Width","600"));
											
											 		uiEngine.openCustomUrlCloseableOnContextChange(finalUrl, params, false);
											}
											*/
										}
										else
										{
											ims.framework.TopButtonConfig topButtons = (ims.framework.TopButtonConfig)sessData.topButtons.get();
											
											TopButton topButton = (TopButton)topButtons.find(id);											
											if(topButton.getITopButtonUrl() != null)
											{
												uiEngine.openUrl(topButton.getITopButtonUrl());
											}
											else if(topButton.getITopButtonForm() != null)
											{
												newFormName = new Integer(topButton.getITopButtonForm().getID());
												if(isDialog(newFormName))
												{
													output = renderSystemPasswordPrompt(true, newFormName, sessData);
													if(output != null)
													{
														write(request, response, recvd, xmlLogger, output);
														return;
													}
													
													formName = newFormName;
													uiEngine.open(new ims.domain.FormName(newFormName.intValue()), topButton.getITopButtonDisplayCloseButton(), topButton.getITopButtonDisplayMaximiseButton());
												}
												else if (!newFormName.equals(formName))
												{
													output = renderSystemPasswordPrompt(true, newFormName, sessData);
													if(output != null)
													{
														write(request, response, recvd, xmlLogger, output);
														return;
													}
													
													formName = newFormName;
													requireRedirect = true;
												}																							
											}
											else
											{
												Object tbIdentifier = topButton.getIdentifier();
												if(tbIdentifier instanceof ISelectedPatient)
												{
													newFormName = ConfigFlag.UI.DEMOGRAPHICS_FORM.getValue().getID();
													requireRedirect = true;
													
													if(newFormName.equals(formName))
													{
														sessData.formsData.get().remove(formName);														
													}
													
													formName = newFormName;
													sessData.formParams.set(new Object[] { tbIdentifier });													
												}
											}
										}
									}
									else if (event instanceof SystemPasswordClosed)
									{
										try
										{
											SystemPasswordClosed typedEvent = (SystemPasswordClosed)event;
											if(!typedEvent.wasCanceled())
											{									
												sessData.systemPasswordEntered.set(new Boolean(!typedEvent.wasCanceled()));
												formName = sessData.systemFormToOpen.get();												
												requireRedirect = true;
											}
											else
											{
												sessData.systemFormToOpen.remove();
												output = "<xml></xml>";
											}
										}
										catch (Exception ex)
										{
											ex.printStackTrace();
											output = this.error(request, session, FrameworkErrorCodes.FRAMEWORK, ex);
										}
									}
									else if (event instanceof AutoScreenLockEvent)
									{
										StringBuffer sb = createResponseString();
										sb.append("<xml>");
										getUILayout(session).renderScreenLockInitialization(sb, response.encodeURL("CNHost"));
										sb.append("</xml>");
										write(request, response, recvd, xmlLogger, sb.toString());
										return;
									}
									else if (event instanceof AlertEvent)
									{
										AlertEvent e = (AlertEvent) event;
										if(!e.isFromDialog()) // for now we do nothing about alerts comming from dialogs.
										{	
											Alert alert = null;
											ArrayList alerts = sessData.alerts.get();
											for (int j = 0; j < alerts.size(); ++j)
											{
												Alert a = (Alert) alerts.get(j);
												if (a.getID() == e.getID())
												{
													alert = a;
													break;
												}
											}
	
											if (alert instanceof RIEAlert)
											{
												shouldFireFormOpenEventByInternalAlert = handleRIEAlertClick(sessData);
											}
											else if (alert instanceof FormInfoAlert)
											{											
												handleFormInfoAlertClick(sessData, getUIEngine(request, session));
											}
											else
											{
												UIEngine uiEngine = getUIEngine(request, session);
												Class cl = Class.forName("ims.alerts.Logic");
												Constructor constructor = cl.getDeclaredConstructor(new Class[]{ims.framework.UIEngine.class});
												constructor.setAccessible(true);
												((AlertLogic) constructor.newInstance(new Object[]{uiEngine})).dispatch(alert);
											}
										}
									}
									else if (event instanceof RIEAlertEvent)
									{
										shouldFireFormOpenEventByInternalAlert = handleRIEAlertClick(sessData);
									}
								}
							}

							Integer formToOpen = sessData.openForm.get();
							
							boolean isNewForm = false;
							if (!formName.equals(sessData.currentFormID.get()) || newForm)
								isNewForm = true;
							if (isNewForm)
								setRIEMode(sessData, formName);
							
							output = renderSystemPasswordPrompt(newForm, formName, sessData);
							if(output == null)
							{	
								if (sessData.openForm.get() != null && Boolean.FALSE.equals(sessData.openForm.get().equals(formName)))
									sessData.fireOpenEvent.set(Boolean.FALSE);
								
								if(currentForm == null)
									currentForm = formName == null ? null : createForm(request, session, formName);
								
								if (shouldFireFormOpenEventByInternalAlert)
								{
									this.freeResources(session);
									newForm = true;
									sessData.fireOpenEvent.set(Boolean.TRUE);
									sessData.formsData.get().remove(formName);									
									currentForm = createForm(request, session, formName);
								}
								else
								{
									if (formToOpen != null)
									{
										IAppForm registeredForm = UIEngine.getForm(sessData, formToOpen);
										if (!registeredForm.isDialog())
										{
											sessData.openForm.remove();
											requireRedirect = !formToOpen.equals(formName);
											formName = formToOpen;
											formToOpen = null;
											isNewForm = requireRedirect;
											if (isNewForm)
												setRIEMode(sessData, formName);
										}
									}
									if (requireRedirect)
									{
										ims.framework.delegates.CancelArgs args = new ims.framework.delegates.CancelArgs();
										if (currentForm != null)
											currentForm.fireFormClosingEvent(args);
	
										if (!args.isCancel())
										{
											this.freeResources(session);
											Integer currentFormID = sessData.currentFormID.get();
	
											IAppForm registeredForm = UIEngine.getForm(sessData, formName);
											if (!registeredForm.isDialog())
												sessData.previousForm.set(currentFormID);
	
											setCurrentForm(sessData, formName);
											sessData.fireOpenEvent.set(Boolean.TRUE);
											newForm = true;
											isNewForm = true;
											sessData.listRIERecordsOnly.set(Boolean.FALSE);
											
											//**********************************************************************************************************
											//**  If previous form was LocationsSelection and application was launched via security token (web services)
											//**  be sure that startup form will be from setStartupForm web service method								
											//**********************************************************************************************************
											// If form that need to be open is not Select Location
											if(sessData.defaultStartupForm.get() != null && sessData.currentFormID.get() != null &&
													Boolean.FALSE.equals(sessData.currentFormID.get().equals(InitConfig.getLocationsSelectionFormId())))
											{	
												// If previousForm is LocationsSelection next form will be the one from setStartupForm web service method												
												if (sessData.previousForm.get().equals(InitConfig.getLocationsSelectionFormId()))
												{
													formName = sessData.defaultStartupForm.get();
													sessData.currentFormID.set(formName);
												}
											}
																						
											if (formIdentifiers != null)
											{
												currentForm = createForm(request, session, formName, formIdentifiers);
											}
											else
											{
												currentForm = createForm(request, session, formName);
											}	
										}
									}
								}
								if (sessData.closeForm.get() != null)
								{
									sessData.closeForm.remove();
									output = "<xml><close/></xml>";
								}
								else
								{
									handleRIERecordsNotification(request, session, isNewForm, formName);
	
									// save the form mode in the session variable
									sessData.currentFormMode.set(currentForm.getMode());
	
									// Need to output them definition along with first form
									if (isSecurityLogin)
									{
										output = this.render(request, response, session, newForm, currentForm, formName, true);
									}
									else
									{
										output = this.render(request, response, session, newForm, currentForm, formName);
									}
									this.freeResources(session);
								}
							}
						}						
						catch (PresentationLogicException ex)
						{
							ex.printStackTrace();
							output = this.presentationError(request, session, ex);
						}
						catch (FormMandatoryLookupMissingException ex)
						{
							ex.printStackTrace();
							output = this.presentationError(request, session, ex);
						}
						catch (DomainException ex)
						{
							ex.printStackTrace();
							output = this.fatalError(request, session, ex);
							clearAndInvalidateCurrentSession(request);
						}
						catch (RulesEngineRuntimeException ex)
						{
							output = "<xml><messages><message>" + StringUtils.encodeXML(ex.getCause().getCause().getCause().getMessage()) + "</message></messages></xml>";
						}
						catch (DomainRuntimeException ex)
						{
							ex.printStackTrace();
							output = this.fatalError(request, session, ex);
							clearAndInvalidateCurrentSession(request);							
						}
						catch (ConfigurationException ex)
						{
							ex.printStackTrace();
							output = this.fatalError(request, session, ex);
							clearAndInvalidateCurrentSession(request);
						}
						catch (FrameworkInternalException ex)
						{
							ex.printStackTrace();
							output = this.fatalError(request, session, ex);
							clearAndInvalidateCurrentSession(request);
						}						
						catch (CodingRuntimeException ex)
						{
							ex.printStackTrace();
							output = this.error(request, session, FrameworkErrorCodes.CODING, ex);
							if (!ConfigFlag.GEN.RELEASE_MODE.getValue())
								clearAndInvalidateCurrentSession(request);
						}
						catch (IllegalStateException ex)
						{
							// JME: 20051221: Hope this will cater for situation where http
							// request arrives almost simultaneous with session timeout occurring.
							ex.printStackTrace();
							output = this.error(request, session, FrameworkErrorCodes.SESSION_EXPIRED, (String)null);
							clearAndInvalidateCurrentSession(request);
						}
						catch (InvocationTargetException ex)
						{
							if (ex.getTargetException() instanceof FormMandatoryContextMissingException)
							{
								output = this.presentationError(request, session, (FormMandatoryContextMissingException) ex.getTargetException());
								sessData.currentFormID.set(null);
							}							
							else
							{
								if (ex.getTargetException() != null)
									ex.getTargetException().printStackTrace();
								output = this.fatalError(request, session, ex.getTargetException() != null ? ex.getTargetException() : ex);
								clearAndInvalidateCurrentSession(request);
							}
						}
						catch (Throwable ex)
						{
							ex.printStackTrace();
							output = this.fatalError(request, session, ex);
							clearAndInvalidateCurrentSession(request);
						}
					}
				}
			}
		}

		if(shouldRenderTheme)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(this.loadTheme());
			if(shouldRenderLogin)
				getUILayout(session).renderLogin(sb);
			output = output.replaceFirst("<xml>", "<xml>" + sb.toString());			
		}
		
		if (events.isEmptyRequest())
		{
			StringBuffer sb = new StringBuffer();
			renderHeartBeat(sb, sessData);			
		}
				
		//Debug response				
		if(ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue() != null &&
				(ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue().equals("RESPONSE") ||
					ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue().equals("REQUEST_AND_RESPONSE")))
		{
			debugRequestInfo(request, response, new StringBuilder(output), false, true);
		}
		
		write(request, response, recvd, xmlLogger, output.toString());
	}

	private UIEngine getUIEngine(HttpServletRequest request)
	{
		return new UIEngine(request, getSession(request));
	}
	private UIEngine getUIEngine(HttpServletRequest request, HttpSession session)
	{
		return new UIEngine(request, session);
	}

	private void processUploadDownloadTopButtonClick(UIEngine uiEngine, HttpSession session, boolean isDownload)
	{
		SessionData sessData = (SessionData)session.getAttribute(SessionConstants.SESSION_DATA);
		UploadDownloadUrlProviderFactory factory = new UploadDownloadUrlProviderFactory(sessData.domainSession.get(),new Context(session));
		
		if(!factory.hasProvider())
		{
			uiEngine.showMessage("No provider available for " + (isDownload ? "download" : "upload") + ".", "Error", MessageButtons.OK, MessageIcon.ERROR);
			return;
		}
		IUploadDownloadUrlProvider provider = factory.getProvider();
				
		try
		{
			if(isDownload)
			{
				provider.download(uiEngine);			
			}
			else
			{
				provider.upload(uiEngine);
			}
		}
		catch (ConfigurationException e)
		{
			uiEngine.showMessage(e.getMessage(), "Error", MessageButtons.OK, MessageIcon.ERROR);
		}
	}

	private void renderSearchResult(StringBuffer sb, HttpSession session, UIEngine uiEngine, boolean lightweight, String searchText)
	{
		ISearch search = new Search(session, uiEngine);
		
		ISearchResult[] results = search.search(searchText);
		
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		sessData.lastSearchResults.set(results);
		sessData.lastSearchText.set(null);
		
		if(lightweight)
			sb.append("<data changeFocus=\"false\">");
				
		sb.append("<search id=\"search\">");
		sb.append("<list>");

		int length = results.length;
		if(length > 20)
			length = 20;
		
		for(int x = 0; x < length; x++)
		{
			sb.append("<i ");
			
			if(results[x].getLink().getAppForm().getImage() != null)
			{
				sb.append("img=\"" + results[x].getLink().getAppForm().getImage().getImagePath() + "\" ");
			}
			else if(results[x].getImage() != null)
			{
				sb.append("img=\"themes/common/g/" + results[x].getImage().getImagePath() + "\" ");
			}
			
			sb.append("id=\"" + results[x].getId() + "\" title=\"" + StringUtils.encodeXML(results[x].getText()) + "\">");
			sb.append(StringUtils.encodeXML(results[x].getDescription()));
			sb.append("</i>");
		}
		
		sb.append("</list>");
		sb.append("</search>");
		
		if(lightweight)
			sb.append("</data>");
	}

	private void write(HttpServletRequest request, HttpServletResponse response, java.util.Date recvd, Logger xmlLogger, String output) throws IOException
	{
		java.util.Date processed = new java.util.Date();
		long processingTime = processed.getTime() - recvd.getTime();
		String repText = "<xml pt=\"" + processingTime + "\">";
		
		SessionData sessData = (SessionData) request.getSession().getAttribute(SessionConstants.SESSION_DATA);
		boolean userInDebugMode = false;
		
		if(sessData != null && 
				sessData.user != null && 
				sessData.user.get() != null && 
				sessData.user.get().getDebugMode() != null && 
				sessData.user.get().getDebugMode().booleanValue())
		{
			userInDebugMode = true;
		}
		
		if (!ConfigFlag.GEN.RELEASE_MODE.getValue() || userInDebugMode)
			repText += "<debugWindow/>";
		
		output = output.replaceFirst("<xml>", repText);

		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("Response: " + output);

		if (xmlLogger != null)
			xmlLogger.info("Response: " + output);

		this.write(request, response, output);
	}

	private String handleScreenUnlock(HttpSession session, ScreenUnlock unlock) throws Exception
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);

		String defaultResponse = "<xml><messages><message>Invalid username and/or password. You need to enter a valid username and password to unlock this session.</message></messages><lock/></xml>";

		IAppUser user = sessData.user.get();
		if (user == null || user.getUsername() == null || user.getClearPassword() == null)
			return defaultResponse;

		if (user.getUsername().toLowerCase().equals(unlock.getUsername().toLowerCase()))
		{
			if(sessData.passwordSalt.get() == null)
			{
				 if(unlock.getPassword().equals(user.getClearPassword()))
				 {
					 return null;
				 }				 
				 else
				 {
					 return defaultResponse;
				 }
			}
			else
			{				
				if(unlock.getPassword().equals(ims.configuration.Configuration.getSHA1Hash(user.getClearPassword(), sessData.passwordSalt.get())))
				{
					return null;
				}
				else
				{
					return defaultResponse;
				}
			}
		}

		return defaultResponse;
	}

	private void initializeSessionDataVariables(SessionData sessData)
	{
		if (sessData.messageBox.get() == null)
			sessData.messageBox.set(new ArrayList<String>());
		
		if (sessData.messageBoxes.get() == null)
			sessData.messageBoxes.set(new ArrayList<MessageBox>());

		if (sessData.errors.get() == null)
			sessData.errors.set(new ArrayList());

		if (sessData.newAlerts.get() == null)
			sessData.newAlerts.set(new ArrayList<Alert>());

		if (sessData.alerts.get() == null)
			sessData.alerts.set(new ArrayList<Alert>());

		if (sessData.removedAlerts.get() == null)
			sessData.removedAlerts.set(new ArrayList<Alert>());
	}

	private void clearAndInvalidateCurrentSession(HttpServletRequest request)
	{		
		HttpSession session = getSession(request, false);
		if (session != null)
		{
			session.invalidate();
			LocalLogger.warn("Session cleared.");
		}
		else
		{
			LocalLogger.warn("Session already invalidated.");
		}
	}

	private Integer processRecordedInErrorTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer rieDialogFormID = InitConfig.getRecordedInErrorFormId();
		uiEngine = getUIEngine(request, session);
		if (rieDialogFormID != null && rieDialogFormID.intValue() > 0)
		{
			SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData.currentFormID.get() == null)
			{
				uiEngine.showMessage("Recorded in error functionality is not supported in this context.");
			}
			else
			{
				ims.framework.FormName rieForm = new ims.domain.FormName(sessData.currentFormID.get().intValue());
				if (!uiEngine.supportsRecordedInError(rieForm))
				{
					uiEngine.showMessage("Recorded in error functionality is not supported by this form.");
				}
				else if (uiEngine.getRecordedInErrorVo(rieForm) == null)
				{
					uiEngine.showMessage("There are no records to be marked as recorded in error.");
				}
				else
				{
					formName = rieDialogFormID;
					uiEngine.open(new ims.domain.FormName(rieDialogFormID.intValue()));
				}
			}
		}
		else
		{
			uiEngine.showMessage("Recorded in error functionality is not supported by the application.");
		}
		return formName;
	}

	private Integer processAuditViewTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer auditViewDialogFormID = InitConfig.getAuditViewFormId();
		uiEngine = getUIEngine(request, session);
		if (auditViewDialogFormID != null && auditViewDialogFormID.intValue() > 0)
		{
			SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);			
			if (sessData.currentFormID.get() == null)
			{
				uiEngine.showMessage("Audit View functionality is not supported in this context.");
			}
			else
			{
				ims.framework.FormName auditForm = new ims.domain.FormName(sessData.currentFormID.get().intValue());
				if (!uiEngine.supportsRecordedInError(auditForm))
				{
					uiEngine.showMessage("Audit View functionality is not supported by this form.");
				}
				else if (uiEngine.getRecordedInErrorVo(auditForm) == null)
				{
					uiEngine.showMessage("There are no records to be marked for Audit View.");
				}
				else
				{
					formName = auditViewDialogFormID;
					uiEngine.open(new ims.domain.FormName(auditViewDialogFormID.intValue()));
				}
			}
		}
		else
		{
			uiEngine.showMessage("Audit View functionality is not supported by the application.");
		}
		return formName;
	}

	private Integer processLocationsManagerTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer locationsManagerDialogFormID = InitConfig.getLocationsManagerFormId();
		uiEngine = getUIEngine(request, session);
		if (locationsManagerDialogFormID != null && locationsManagerDialogFormID.intValue() > 0)
		{
			uiEngine.open(new ims.domain.FormName(locationsManagerDialogFormID.intValue()));
		}
		else
		{
			uiEngine.showMessage("Locations manager is not included in the application.");
		}
		return formName;
	}

	private Integer processPASContactsTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer pasContactsDialogFormID = InitConfig.getPASContactsFormId();
		uiEngine = getUIEngine(request, session);
		if (pasContactsDialogFormID != null && pasContactsDialogFormID.intValue() > 0)
		{
			uiEngine.open(new ims.domain.FormName(pasContactsDialogFormID.intValue()));
		}
		else
		{
			uiEngine.showMessage("PAS contacts functionality is not supported by the application.");
		}
		return formName;
	}
	
	private Integer processChangePasswordTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		uiEngine = getUIEngine(request, session);	
		//If user use external authentication(LDAP) change password functionality is not supported
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		IAppUser user = sessData.user.get();
		if (user != null && user.useExternalAuthentication())
		{
			uiEngine.showMessage("Change password functionality is not supported for users using External Authentication.");
			return formName;
		}
						
		Integer userPasswordChangeDialogId = InitConfig.getUserPasswordChangeDialogId();						
		if (userPasswordChangeDialogId != null && userPasswordChangeDialogId.intValue() > 0)
		{
			uiEngine.open(new ims.domain.FormName(userPasswordChangeDialogId.intValue()));
		}
		else
		{						
			uiEngine.showMessage("Change password functionality is not supported by the application.");
		}
		
		return formName;
	}

	private Integer processOrderEntryTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer orderEntryFormID = InitConfig.getOrderEntryFormId();
		uiEngine = getUIEngine(request, session);
		if (orderEntryFormID != null && orderEntryFormID.intValue() > 0)
		{
			uiEngine.open(new ims.domain.FormName(orderEntryFormID.intValue()));
		}
		else
		{
			uiEngine.showMessage("Order entry functionality is not supported by the application.");
		}
		return formName;
	}

	private Integer processPatientSummaryTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer patientSummaryFormID = InitConfig.getPatientSummaryFormId();
		uiEngine = getUIEngine(request, session);
		if (patientSummaryFormID != null && patientSummaryFormID.intValue() > 0)
		{
			uiEngine.open(new ims.domain.FormName(patientSummaryFormID.intValue()));
		}
		else
		{
			uiEngine.showMessage("Patient summary functionality is not supported by the application.");
		}
		return formName;
	}

	private Integer processRecordingOnBehalfOfTopButtonClick(HttpServletRequest request, HttpSession session, Integer formName)
	{
		UIEngine uiEngine;
		Integer recordingOnBehalfOfDialogFormID = InitConfig.getRecordingOnBehalfOfFormId();
		uiEngine = getUIEngine(request, session);
		if (recordingOnBehalfOfDialogFormID != null && recordingOnBehalfOfDialogFormID.intValue() > 0)
		{
			SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
			formName = recordingOnBehalfOfDialogFormID;
			if (sessData.currentFormID.get() == null)
			{
				uiEngine.showMessage("Recording on behalf of... functionality is not supported in this context.");
			}
			else
			{
				uiEngine.open(new ims.domain.FormName(recordingOnBehalfOfDialogFormID.intValue()));
			}
		}
		else
		{
			uiEngine.showMessage("Recording on behalf of... functionality is not supported by the application.");
		}
		return formName;
	}	
	private String read(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException
	{
		ServletInputStream stream = request.getInputStream();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		StringBuilder inputBuffer = new StringBuilder();
        char[] buffer = new char[1024 * 32];
        while (true) 
        {
            int count = bufferReader.read(buffer); 
            if (count <= 0)
            	break;
            
            inputBuffer.append(new String(buffer, 0, count));
        }         
        
        //Debug request
        if(ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue() != null &&
        		(ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue().equals("REQUEST") ||
					ConfigFlag.FW.DEBUG_HTTP_REQUEST_AND_RESPONSE.getValue().equals("REQUEST_AND_RESPONSE")))
        {
        	debugRequestInfo(request, response, inputBuffer, true, false);
        }
        
        return inputBuffer.toString();
	}
	
	private void write(HttpServletRequest request, HttpServletResponse response, String outcome) throws IOException
	{
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		String encoding = request.getHeader("Accept-Encoding");
		if (encoding != null && encoding.toLowerCase().indexOf("gzip") > -1)
		{
			response.setHeader("Content-Encoding", "gzip");
			GZIPOutputStream gzos = new GZIPOutputStream(response.getOutputStream());
			gzos.write(outcome.getBytes("UTF-8"));
			gzos.close();
		}
		else
		{
			Writer writer = new java.io.OutputStreamWriter(response.getOutputStream(), "UTF-8");
			writer.write(outcome);
			writer.close();			
		}
	}

	private Form createForm(HttpServletRequest request, HttpSession session, Integer formName) throws Exception
	{
		return createForm(request, session, formName, null);
	}

	@SuppressWarnings("unchecked")
	private Form createForm(HttpServletRequest request, HttpSession session, Integer formName, IGenericIdentifier[] identifiers) throws Exception
	{
		if (LocalLogger.isDebugEnabled())
			LocalLogger.debug("Create Form:");

		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);

		Map map = sessData.configurationModule.get().getRegisteredForms();
		HashMap formsData = sessData.formsData.get();
		
		boolean isDialog = isDialog(formName); 
		
		if(isDialog)
		{
			int key = 0;
			if(sessData.dialogResults.get() != null)
				key = sessData.dialogResults.get().size();
			
			if (!formsData.containsKey(new Integer(key)))
				formsData.put(new Integer(key), new FormData());
		}
		else
		{
			if (!formsData.containsKey(formName))
				formsData.put(formName, new FormData());
		}
		FormLoader formLoader = new FormLoader(request, session);

		if (LocalLogger.isDebugEnabled())
			LocalLogger.debug("Got formLoader - CNHost");

		Integer previousFormID = sessData.previousForm.get();
		IAppForm previousForm = null;
		IAppForm currentForm = null;
		if (previousFormID != null)
		{
			sessData.previousForm.remove();
			boolean closed = false;
			if (sessData.previousFormWasClosed.get() != null)
			{
				sessData.previousFormWasClosed.remove();
				closed = true;
			}

			previousForm = (IAppForm) map.get(previousFormID);
			currentForm = (IAppForm) map.get(sessData.currentFormID.get());

			// Remove all data for the previous form if we are opening another form
			// MM: We make sure not to delete data when prev form id == current form id (the equality happens for assessments)
			if (!currentForm.isDialog() && previousFormID != null && currentForm.getFormId() != previousFormID.intValue())
				formsData.remove(previousFormID);

			if (closed)
			{
				formLoader.clearContext(previousFormID.intValue(), false);
			}
			else
			{
				if (!previousForm.isDialog() && !currentForm.isDialog())
				{
					// uncomment this line to clear all local context - MM (FWUI-1342)
					//formLoader.clearAllLocalContext();

					//clearing the local context
					formLoader.clearContext(previousFormID.intValue(), false);
					formLoader.clearContext(formName.intValue(), true);
				}
			}
		}
		FormUiLogic formUiLogic = formLoader.load(formName.intValue());
		Form form = (Form) formUiLogic.getForm();
		
		if(!isDialog)
			sessData.formDarkHeaderHeight.set(new Integer(form.getDarkHeight())); 

		if (LocalLogger.isDebugEnabled())
			LocalLogger.debug("freeFormLoader - CNHOST");

		if (previousFormID == null)
		{
			IAppForm formToBeOpened = (IAppForm) map.get(formName);
			if (formToBeOpened != null)// && formToBeOpened.isDialog())
			{
				FormMode lastMode = sessData.currentFormMode.get();
				if (lastMode != null)
					form.setMode(lastMode, false);
			}

			if (form.getMode() == null)
				form.setMode(FormMode.VIEW, false);
		}
		else
		{
			if (previousForm.isDialog() && !currentForm.isDialog())
			{
				form.setMode(sessData.currentFormMode.get(), false);
			}
			else if (!previousForm.isDialog() && !currentForm.isDialog())
			{
				form.setMode(FormMode.VIEW, false);
			}
			else if (previousForm.isDialog() && currentForm.isDialog())
			{
				// fix: the destination dialog was loosing the mode, NEEDS TESTING!!!
				form.setMode(sessData.currentFormMode.get(), false);
			}
			else if (!previousForm.isDialog() && currentForm.isDialog())
			{
				form.setMode(FormMode.EDIT, false);
			}
		}

		// restore form
		
		FormData data = null;
		if(isDialog(formName))
		{
			int key = 0;
			if(sessData.dialogResults.get() != null)
				key = sessData.dialogResults.get().size();
			
			data = (FormData) formsData.get(new Integer(key));
		}
		else
		{
			data = (FormData) formsData.get(formName);			
		}
		
		if (data == null)
		{
			sessData.fireOpenEvent.set(Boolean.TRUE);
			data = new FormData();
		}
		form.setReadOnly(isFormReadOnly(formName, sessData));
		form.restore(data, data.isEmpty());

		if (LocalLogger.isDebugEnabled())
			LocalLogger.debug("Fire Open Event - CNHost");

		// Form Open Event
		if (sessData.fireOpenEvent.get().booleanValue())
		{
			UILogic logic = formUiLogic.getUiLogic();
			if (logic != null && logic instanceof IClearInfo)
			{
				((IClearInfo) logic).clearContextInformation();
			}

			sessData.fireOpenEvent.set(Boolean.FALSE);
			if (identifiers != null)
				form.fireFormOpenEvent(GenericIdentifierFactory.instantiate(identifiers));
			else
				form.fireFormOpenEvent(sessData.formParams.get());
			form.fireFormModeChangedEvent();
		}
		else
		{
			Integer tmp = sessData.dialogID.get();
			if (tmp != null)
			{
				DialogResult dialogResult = sessData.dialogResult.get();
				sessData.dialogID.remove();
				sessData.dialogResult.remove();
				
				ArrayList<String> dialogIdentifiers = sessData.openDialogCallerIdentifiers.get();
				String callerIdentifier = "";
				if(dialogIdentifiers != null && dialogIdentifiers.size() > 0)
				{
					callerIdentifier = dialogIdentifiers.get(dialogIdentifiers.size() - 1);
					dialogIdentifiers.remove(dialogIdentifiers.size() - 1);
					
					if(dialogIdentifiers.size() == 0)
						sessData.openDialogCallerIdentifiers.set(null);
					else
						sessData.openDialogCallerIdentifiers.set(dialogIdentifiers);						
				}
				
				form.fireFormDialogClosedEvent(tmp.intValue(), dialogResult, callerIdentifier);

				// clearing data for closed dialog
				if (previousForm != null)
					formsData.remove(new Integer(previousForm.getFormId()));
			}
		}
		if (LocalLogger.isDebugEnabled())
			LocalLogger.debug("Return Form - CNHost");

		return form;
	}

	private boolean isDialog(Integer formID)
	{
		return ((IAppForm) configuration.getRegisteredForms().get(formID)).isDialog();
	}
	private boolean isSystem(Integer formID)
	{
		return ((IAppForm) configuration.getRegisteredForms().get(formID)).isSystem();
	}

	private String render(HttpServletRequest request, HttpServletResponse response, HttpSession session, boolean newForm, Form currentForm, Integer formName) throws Exception
	{
		return render(request, response, session, newForm, currentForm, formName, false);
	}
	private StringBuffer createResponseString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); // same encoding as jsCN 
		return sb;
	}
	private String render(HttpServletRequest request, HttpServletResponse response, HttpSession session, boolean newForm, Form currentForm, Integer formName, boolean includeTheme) throws Exception
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		
		StringBuffer sb = createResponseString();
		sb.append("<xml>");
		if(sessData.progressBarText.get() != null)
		{
			renderProgressBar(sb, sessData);
			sb.append("</xml>");
			return sb.toString();
		}
		
		Integer formToOpen = sessData.openForm.get();
		boolean isDialog = isDialog(formName);
		
		if(newForm && !isDialog)
		{
			sessData.systemFormToOpen.remove();			
		}

		// setCurrentForm(sessData, formName);
		
		String theme = sessData.changeTheme.get();
		boolean firstTime = false;
		
		if (theme != null)
		{
			sessData.changeTheme.remove();
			firstTime = true;
			
			// NO NEED to send the <html> again
			// sb.append(this.loadTheme(session, theme, configuration.isTabLayout()));
		}
		if (includeTheme && !isMobileSession(sessData) && !isCustomNavigatorSession(session))
		{
			sb.append(this.loadTheme());
		}
		
		String patientInfo = sessData.patientInfo.get();
		if (patientInfo != null)
		{
			String textColor = sessData.patientInfoTextColor.get();
			if (textColor == null)
			{
				sb.append("<patient>");
			}
			else
			{
				sb.append("<patient textColor=\"" + textColor + "\" >");
				//'patientInfoTextColor' already removed in ContextEvalProvider
				//sessData.patientInfoTextColor.remove();
			}

			sb.append(ims.framework.utils.StringUtils.encodeXML(patientInfo));
			sb.append("</patient>");
		}
		else
		{
			sb.append("<patient>");
			sb.append("");
			sb.append("</patient>");
		}

		IDynamicNavigation dynamicNavigation = sessData.currentDynamicNavigation.get();

		if (/*!isDialog && dynamicNavigation.shouldRenderForm() && */!isMobileSession(session))
		{
			dynamicNavigation.renderForm(sb);
		}
		if (sessData.sendStoredLocations.get() != null)
		{
			sessData.sendStoredLocations.remove();
			this.renderStoredLocations(sessData, sb);
		}
		IAppForm formToBeRendered = UIEngine.getForm(sessData, formName);		
		
		// Rendering UI Layout
		getUILayout(session).render(sb, isRie(sessData));

		if (newForm)
		{
			String caption = sessData.captionOverride.get();
			if (caption != null)
				sessData.captionOverride.remove();
			else
				caption = formToBeRendered.getCaption();

			currentForm.renderForm(sb, dynamicNavigation.getUniqueNavigationId(sessData.currentFormID.get().intValue()), caption);
		}

		String loginString = ims.framework.utils.StringUtils.encodeXML(constructLoginString(session));

		String xmlStr = "<data browserTitle=\" - " + loginString + "\" canLeaveForm=\"";
		xmlStr += (currentForm.getMode() == FormMode.EDIT ? "false" : "true");
		if (currentForm.getFocusedControl() != null)
			xmlStr += "\" focusedControl=\"a" + currentForm.getFocusedControl().getID();
		if (currentForm.getDefaultControl() != null)
			xmlStr += "\" defaultButton=\"a" + currentForm.getDefaultControl().getID();

		// if listRIERecordsOnly is true the application enters into RIE mode
		xmlStr += "\" errorMode=\"";
		if (sessData.listRIERecordsOnly.get() != null && sessData.listRIERecordsOnly.get().booleanValue())
			xmlStr += "true";
		else
			xmlStr += "false";

		if (sessData.uploadUrl.get() != null)
		{
			xmlStr += "\" uploadURL=\"" + sessData.uploadUrl.get();
			if (sessData.uploadDebug.get() != null)
				xmlStr += "\" uploadDebug=\"" + (sessData.uploadDebug.get().booleanValue() ? "true" : "false");
		}
		
		if(sessData.lastSearchText.get() != null)
		{
			xmlStr += "\" changeFocus=\"false";
		}

		xmlStr += "\">";
		sb.append(xmlStr);
		
		boolean formIsReadOnly = isFormReadOnly(formName, sessData);
		currentForm.setReadOnly(formIsReadOnly);
		currentForm.renderData(sb);				
		
		UIEngine engine = getUIEngine(request, session);
		
		if(sessData.lastSearchText.get() != null)
			renderSearchResult(sb, session, engine, false, sessData.lastSearchText.get());
		
		sb.append("</data>");
		
		if (/*!isDialog && */!isMobileSession(session));// && sessData.currentFormMode.get() == FormMode.VIEW)
		{
			dynamicNavigation.preRenderData();
			if(dynamicNavigation.wasChanged())
			{
				dynamicNavigation.renderData(sb);
				dynamicNavigation.markUnchanged();
			}
		}
		
		renderTopButtons(session, sb, formIsReadOnly);
		renderTimers(sb, sessData, currentForm.renderTimers(), newForm);		

		String emailAddress = sessData.emailAddressToOpen.get();
		if (emailAddress != null)
		{
			sb.append("<openEmail address=\"" + ims.framework.utils.StringUtils.encodeXML(emailAddress) + "\" />");
			sessData.emailAddressToOpen.set(null);
		}
		
		ArrayList<Integer> closeUrls = sessData.urlToClose.get();
		if (closeUrls != null && closeUrls.size() > 0)
		{
			for (int x = 0; x < closeUrls.size(); x++)
			{
				sb.append("<closeUrl id=\"" + closeUrls.get(x) + "\" ");
				sb.append(" />");
			}
			
			sessData.urlToClose.set(new ArrayList<Integer>());
		}
		
		ArrayList<Url> urls = sessData.urlToOpen.get();
		if (urls != null)
		{
			for (int x = 0; x < urls.size(); x++)
			{
				Url url = urls.get(x);
				if (engine.isValidUrl(url.getUrl()))
				{
					sb.append("<openUrl value=\"" + ims.framework.utils.StringUtils.encodeXML(url.getUrl()) + "\" ");
					
					if(url.getTarget() != null)
					{
						sb.append(" window=\"" + ims.framework.utils.StringUtils.encodeXML(url.getTarget()) + "\" ");
					}
					
					if(url.getParams() != null)
					{						
						sb.append(">");
						
						for(int p = 0; p < url.getParams().size(); p++)
						{
							UrlParam param = url.getParams().get(p);
							sb.append("<param name=\"" + StringUtils.encodeXML(param.getKey()) + "\" value=\"" + StringUtils.encodeXML(param.getValue()) + "\" />"); 
						}
						
						sb.append("</openUrl>");
					}
					else
					{
						sb.append(" />");
					}
				}
			}

			sessData.urlToOpen.set(null);
		}
		
		ArrayList<Url> customUrls = sessData.customUrlToOpen.get();
		if (customUrls != null)
		{
			for (int x = 0; x < customUrls.size(); x++)
			{
				Url url = customUrls.get(x);
				if (engine.isValidUrl(url.getUrl()))
				{
					sb.append("<openCustomUrl id=\"" + url.getWindowsID() + "\" value=\"" + ims.framework.utils.StringUtils.encodeXML(url.getUrl()) + "\" ");
					
					if(url.handleWindowEvents() != null)
					{
						sb.append(" handleWindowEvents=\"" + ims.framework.utils.StringUtils.encodeXML(url.handleWindowEvents() ? "true" : "false") + "\" ");
					}
					
					if(url.getWindowParams() != null)
					{						
						sb.append(">");
						
						for(int p = 0; p < url.getWindowParams().size(); p++)
						{
							WindowParam param = url.getWindowParams().get(p);
							sb.append("<windowParam name=\"" + StringUtils.encodeXML(param.getKey()) + "\" value=\"" + StringUtils.encodeXML(param.getValue()) + "\" />"); 
						}
						
						sb.append("</openCustomUrl>");
					}
					else
					{
						sb.append(" />");
					}
				}
			}
			sessData.customUrlToOpen.set(null);
		}
		
		ArrayList<String> messages = sessData.messageBox.get();
		ArrayList<MessageBox> messageBoxes = sessData.messageBoxes.get();
		ArrayList<String> domainMessages = sessData.domainMessageBox.get();
		if ((messages != null && messages.size() > 0) || (domainMessages != null && domainMessages.size() > 0) || (messageBoxes != null && messageBoxes.size() > 0))
		{
			sb.append("<messages>");
			for (int i = 0; messages != null && i < messages.size(); i++)
			{
				sb.append("<message>");
				sb.append(ims.framework.utils.StringUtils.encodeXML(messages.get(i).toString()));
				sb.append("</message>");
			}			
			for (int i = 0; domainMessages != null && i < domainMessages.size(); i++)
			{
				sb.append("<message>");
				sb.append(ims.framework.utils.StringUtils.encodeXML(domainMessages.get(i).toString()));
				sb.append("</message>");
			}							
			for (int i = 0; messageBoxes != null && i < messageBoxes.size(); i++)
			{
				messageBoxes.get(i).render(sb);				
			}
			sb.append("</messages>");
			sessData.messageBox.clear();
			sessData.domainMessageBox.clear();
			sessData.messageBoxes.clear();
		}
		
		ArrayList<String> externalNotifications = sessData.externalNotifications.get();
		if(externalNotifications != null && externalNotifications.size() > 0)
		{
			sb.append("<externalEvents>");
			for(int x = 0; x < externalNotifications.size(); x++)
			{
				sb.append("<event id=\"" + StringUtils.encodeXML(externalNotifications.get(x)) + "\"/>");
			}
			sb.append("</externalEvents>");			
			sessData.externalNotifications.clear();
		}
		
		String errorTitle = sessData.errorsTitle.get();
		ArrayList errors = sessData.errors.get();
		if (errors.size() > 0)
		{
			sb.append("<errors title=\"" + ims.framework.utils.StringUtils.encodeXML(errorTitle) + "\">");
			for (int i = 0; i < errors.size(); ++i)
			{
				sb.append("<error>");
				sb.append(ims.framework.utils.StringUtils.encodeXML(errors.get(i).toString()));
				sb.append("</error>");
			}
			sb.append("</errors>");
			sessData.errors.clear();
		}

		// open a dialog if it was requested
		if (formToOpen != null)
		{
			sessData.openForm.remove();
			sessData.dialogOpened.set(Boolean.TRUE);
			int formID = formToOpen.intValue();
			FormLoader formLoader = new FormLoader(request, session);
			FormInfo info = formLoader.getInfo(formID);
			
			sb.append("<dialog width=\"");
			sb.append(info.getWidth());
			sb.append("\" height=\"");
			sb.append(info.getHeight());

			Boolean showCloseButton = sessData.showCloseButtonForDialog.get();
			if (showCloseButton != null && !showCloseButton.booleanValue())
			{
				sb.append("\" closeButton=\"false");
			}		
			
			if(getForm(sessData, formToOpen).isInformationBarVisible())
			{
				sb.append("\" formHeader=\"true");
			}
			sessData.showCloseButtonForDialog.remove();
			
			Boolean showResizableDialog = sessData.showResizableDialog.get();
			if (showResizableDialog != null && showResizableDialog.booleanValue())
			{
				sb.append("\" resizable=\"true");				
			}
			sessData.showResizableDialog.remove();

			sb.append("\" url=\"");
			sb.append(response.encodeURL("CNHost"));
			sb.append("\" id=\"666\"/>");

			// ???????????????????????????
			ArrayList<Integer> previousForms = sessData.previousForms.get();
			ArrayList<FormMode> previousModes = sessData.previousModes.get();
			ArrayList<DialogResult> dialogResults = sessData.dialogResults.get();
			if (previousForms == null)
			{
				previousForms = new ArrayList<Integer>();
				sessData.previousForms.set(previousForms);
				previousModes = new ArrayList<FormMode>();
				sessData.previousModes.set(previousModes);
				dialogResults = new ArrayList<DialogResult>();
				sessData.dialogResults.set(dialogResults);
			}
			Integer currentFormID = sessData.currentFormID.get();
			sessData.previousForm.set(currentFormID);
			previousForms.add(currentFormID);
			previousModes.add(sessData.currentFormMode.get());
			dialogResults.add(DialogResult.CANCEL);
			setCurrentForm(sessData, formToOpen);
			sessData.currentFormMode.set(FormMode.EDIT);
			sessData.fireOpenEvent.set(Boolean.TRUE);
		}
		else
		{
			setCurrentForm(sessData, formName);
		}

		if (formToBeRendered != null)
			handleFormInfoNotification(request, session, formToBeRendered);
		renderAlerts(sessData, sb);
		renderFileDeleteRequest(sb, sessData);
		renderCopyToClipboardRequest(sb, sessData);
		renderMedicodeInputFileRequest(sb, sessData);
		renderApplicationsToRunRequest(sb, sessData);		
		renderAutoLock(sb, sessData);
		renderHeartBeat(sb, sessData);		
		
		//PDS stuff
		renderSamlXmlRequestError(sb, sessData);
		
		//test only
		//renderActionButtons(ActionButtonCollection.getWizard(), sb);

		sb.append("</xml>");

		return sb.toString();
	}

	private boolean isFormReadOnly(Integer formName, SessionData sessData) throws Exception
	{
		if (sessData.listRIERecordsOnly.get() != null && sessData.listRIERecordsOnly.get().booleanValue())
		{
			return true;
		}
		else 
		{
			boolean formIsReadOnly = false;
			IDynamicNavigation dynamicNavigation = sessData.currentDynamicNavigation.get();
			
			INavForm navForm = dynamicNavigation.getNavigation().getNavForm(formName.intValue());
			if (navForm == null)
			{
				formIsReadOnly = false;
			}
			else
			{
				formIsReadOnly = navForm.isReadOnly();
				if (!formIsReadOnly)
					formIsReadOnly = dynamicNavigation.getFormAccess(navForm) == FormAccess.READ_ONLY;
			}
			
			return formIsReadOnly;
		}
	}

	private void renderProgressBar(StringBuffer sb, SessionData sessData)
	{
		if(sessData.progressBarText.get() != null)
		{
			sb.append("<showProgress label=\"" + StringUtils.encodeXML(sessData.progressBarText.get()) + "\" />");
			sessData.progressBarText.set(null);
		}
	}

	private void renderFileDeleteRequest(StringBuffer sb, SessionData sessData)
	{
		ArrayList<String> files = sessData.filesToDelete.get();
		if(files == null)
			return;
		
		sb.append("<deleteFiles>");
		
		for(int x = 0; x < files.size(); x++)
		{
			sb.append("<file>");
			sb.append(StringUtils.encodeXML(files.get(x)));
			sb.append("</file>");
		}
		
		sb.append("</deleteFiles>");
		
		sessData.filesToDelete.set(null);
	}
	
	private void renderCopyToClipboardRequest(StringBuffer sb, SessionData sessData)
	{
		if(sessData.clearClipboard.get() != null &&
				sessData.clearClipboard.get().equals(Boolean.valueOf(true)))
		{
			sb.append("<clearClipboard/>");
			sessData.clearClipboard.set(null);
		}
		
		StringBuilder content = sessData.copyToClipboard.get();
		if (content == null)
			return;
		
		sb.append("<copyToClipboard>");
		sb.append(StringUtils.encodeXML(content.toString()));
		sb.append("</copyToClipboard>");
		
		sessData.copyToClipboard.set(null);
	}
	
	private void renderMedicodeInputFileRequest(StringBuffer sb, SessionData sessData)
	{
		String content = sessData.medicodeInputFile.get();
		if (content == null)
			return;
		
		sb.append("<medicodeInputFile>");
		sb.append(StringUtils.encodeXML(content.toString()));
		sb.append("</medicodeInputFile>");
		
		sessData.medicodeInputFile.set(null);
	}
	
	private void renderApplicationsToRunRequest(StringBuffer sb, SessionData sessData)
	{
		ArrayList<ExternalApplication> files = sessData.externalApplicationToRun.get();
		if(files == null)
			return;
		
		sb.append("<runFiles>");
		
		for(int x = 0; x < files.size(); x++)
		{
			sb.append("<file");
			
			
			if(!files.get(x).isAllowingMultipleInstances())
			{
				sb.append(" allowMultipleInstances=\"false\"");
				if(files.get(x).isCheckForRunningApplicationPerUserSession())
				{
					sb.append(" checkForRunningApplicationPerUserSession=\"true\"");
				}				
				
				sb.append(" msgForRunningApplication=\"");
				if(files.get(x).getMessageToBeDisplayedIfAlreadyRunning() == null || files.get(x).getMessageToBeDisplayedIfAlreadyRunning().trim().length() == 0)
				{
					sb.append("Application is already running.");
				}
				else
				{
					sb.append(StringUtils.encodeXML(files.get(x).getMessageToBeDisplayedIfAlreadyRunning()));
				}
				if(!files.get(x).isAutoRunEditor())
				{
					sb.append("\" >");
				}
				else
				{
					sb.append(" autoRunEditor=\"true\"");	
					sb.append(" >");
				}
			}
			else
			{
				if(files.get(x).isAutoRunEditor())
				{
					sb.append(" autoRunEditor=\"true\"");	
					sb.append(" >");
				}
				else
				{
					sb.append(">");
				}
			}
			
			sb.append("<![CDATA[");
			sb.append(StringUtils.encodeBase64(files.get(x).getFilePath()));
			sb.append("]]>");
			sb.append("</file>");
		}
		
		sb.append("</runFiles>");
		
		sessData.externalApplicationToRun.set(null);
	}
	
	private void renderHeartBeat(StringBuffer sb, SessionData sessData)
	{		
		boolean includeHearBeatTimer;
		
		if(sessData.heartBeatInterval.get() != null && sessData.heartBeatInterval.get() == ConfigFlag.UI.HEART_BEAT_TIMER.getValue())
		{
			includeHearBeatTimer = false;
			sessData.heartBeatInterval.set(ConfigFlag.UI.HEART_BEAT_TIMER.getValue());
		}
		else
		{			
			includeHearBeatTimer = ConfigFlag.UI.HEART_BEAT_TIMER.getValue() > 0;
			if(ConfigFlag.UI.HEART_BEAT_TIMER.getValue() == 0)
			{
				sb.append("<heartbeat reset=\"true\" />");
				return;
			}
		}
		
		if(includeHearBeatTimer)
		{
			sb.append("<heartbeat interval=\"");
			sb.append(ConfigFlag.UI.HEART_BEAT_TIMER.getValue() * 60);				
			sb.append("\" />");			 
		}
	}
		
	private void renderSamlXmlRequestError(StringBuffer sb, SessionData sessData)
	{			
		if (getSamlXmlRequestError != null && getSamlXmlRequestError.length() > 0)
		{			
			sb.append("<messages><message>");
			sb.append("\nAccess to PDS functionality is restricted!");
			sb.append("\n------------------------------------------");			
			sb.append("\nReason(s):\n");
			sb.append(getSamlXmlRequestError.toString());			
			sb.append("</message></messages>");
			
			getSamlXmlRequestError = null;
		}
	}
	
	private void renderAutoLock(StringBuffer sb, SessionData sessData)
	{		
		boolean includeAutoLockTimer;
		
		//Role Autolock timer
		String clientID = sessData.uniqueClientId.get();
		if (clientID != null)
		{
			if(sessData.clientAutolockTimer.get().get(clientID) != null)
			{
				IAppRole role = (IAppRole)sessData.clientAutolockTimer.get().get(clientID);
				if (role != null &&  role.getRoleAutolockTimer() != null && sessData.role.get().getId() == role.getId())
				{
					int autoclockTimerValue = role.getRoleAutolockTimer().intValue();
					if (autoclockTimerValue > 0) 
					{
						sb.append("<autoLockTimer interval=\"");
						sb.append(autoclockTimerValue * 60);				
						sb.append("\" />");
						return;
					}
				}
			}
		}		
		
		if(sessData.autoLockInterval.get() != null && sessData.autoLockInterval.get() == ConfigFlag.UI.AUTO_LOCK_TIMER.getValue())
		{
			includeAutoLockTimer = false;
			sessData.autoLockInterval.set(ConfigFlag.UI.AUTO_LOCK_TIMER.getValue());
		}
		else
		{			
			includeAutoLockTimer = ConfigFlag.UI.AUTO_LOCK_TIMER.getValue() > 0;
			if(ConfigFlag.UI.AUTO_LOCK_TIMER.getValue() == 0)
			{
				sb.append("<autoLockTimer reset=\"true\" />");
				return;
			}
		}
		
		if(includeAutoLockTimer)
		{			
			sb.append("<autoLockTimer interval=\"");
			sb.append(ConfigFlag.UI.AUTO_LOCK_TIMER.getValue() * 60);				
			sb.append("\" />");			 
		}		
	}
	
	private void renderTimers(StringBuffer sb, SessionData sessData, GenericChangeableCollection<Timer> formTimers, boolean isNewForm)
	{			
		if(formTimers.size() > 0)
		{
			if(isNewForm)
			{
				sb.append("<timers reset=\"true\">");
			}
			else
			{
				sb.append("<timers>");
			}
			
			for(int x = 0; x < formTimers.size(); x++)
			{
				formTimers.get(x).render(sb);
			}
			
			sb.append("</timers>");
		}
		else
		{
			//If is new form, stop all timers
			//Is quick to clear all timers instead of retrieving previous form and check if had any timers associate with it
			if(isNewForm)
			{
				sb.append("<timers reset=\"true\">");
				sb.append("</timers>");
				
				return;
			}					
		}
	}

	private String renderSystemPasswordPrompt(boolean newForm, Integer formName, SessionData sessData) 
	{		
		if(shouldPromptForSystemPassword(newForm, formName, sessData))
		{			
			sessData.systemFormToOpen.set(formName);
			StringBuffer sb = createResponseString();
			sb.append("<xml><passwordDialog title=\"Restricted Access\" text=\"To access this functionality please enter the security access password.\" password=\"" + StringUtils.encodeXML(ConfigFlag.SystemPassword) + "\"/></xml>");
			return sb.toString();
		}
		
		return null;
	}
	private void renderTopButtons(HttpSession session, StringBuffer sb, boolean readOnlyForm) throws Exception
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
	
		ims.domain.ContextEvalFactory evalFactory = new ContextEvalFactory(sessData.domainSession.get());
		ims.framework.FormAccessLevel formAccessLevel = new FormAccessLevel(new FormAccessLoader(session), evalFactory);
		ims.framework.TopButtonConfig topButtons = (ims.framework.TopButtonConfig)sessData.topButtons.get();
		topButtons.preRenderContext(evalFactory, formAccessLevel, sessData, readOnlyForm);
		
		if(topButtons.wasChanged())
		{
			topButtons.render(sb, sessData);
			topButtons.markUnchanged();
			sessData.topButtons.set(topButtons);
		}		
	}
	/*private void renderActionButtons(ActionButtonCollection actionButtons, StringBuffer sb)
	{
		String name = StringUtils.encodeXML(actionButtons.getName());
		sb.append("<" + name + ">");		
		
		sb.append("<form>");
		for(int x = 0; x < actionButtons.size(); x++)
		{
			ActionButton actionButton = actionButtons.get(x);
			sb.append("<button id=\"" + actionButton.getID() + "\" x=\"" + x * 100 + 10+ "\" y=\"10\" width=\"100\"/>");
		}
		sb.append("</form>");		
		
		sb.append("<data>");
		for(int x = 0; x < actionButtons.size(); x++)
		{
			ActionButton actionButton = actionButtons.get(x);
			sb.append("<button id=\"" + actionButton.getID() + "\" value=\"" + StringUtils.encodeXML(actionButton.getText()) + "\" />");
		}
		sb.append("</data>");
		
		sb.append("</" + name + ">");
	}*/
	private void renderAlerts(SessionData sessData, StringBuffer sb)
	{
		if(isMobileSession(sessData))
			return;
		
		ArrayList<Alert> newAlerts = sessData.newAlerts.get();
		ArrayList<Alert> removedAlerts = sessData.removedAlerts.get();

		if (newAlerts.size() + removedAlerts.size() > 0)
		{
			Map map = sessData.configurationModule.get().getRegisteredImages();

			sb.append("<alerts>");

			for (int x = 0; x < newAlerts.size(); x++)
			{
				Alert alert = newAlerts.get(x);

				sb.append("<newalert id=\"");
				sb.append(alert.getID());

				sb.append("\" sortIndex=\"");
				sb.append(alert.getIndex());

				sb.append("\" icon=\"");
				if (alert instanceof InternalAlert)
					sb.append(alert.getIcon().getImagePath());
				else
					sb.append(((Image) map.get(new Integer(alert.getIcon().getImageId()))).getImagePath());
				
				sb.append("\" tooltip=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(alert.getTooltip()));
				
				sb.append("\" category=\"");
				sb.append(alert.getCategory());
				
				if(alert.enabledInEditMode())
				{
					sb.append("\" alwaysEnabled=\"true");					
				}
				
				sb.append("\"/>");
			}

			for (int x = 0; x < removedAlerts.size(); x++)
			{
				Alert alert = removedAlerts.get(x);
				sb.append("<removealert id=\"");
				sb.append(alert.getID());
				sb.append("\"/>");
			}

			sb.append("</alerts>");

			sessData.newAlerts.get().clear();
			sessData.removedAlerts.get().clear();
		}
	}

	/**
	 * @return
	 */
	private String constructLoginString(HttpSession session)
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		IAppUser user = sessData.user.get();
		IAppRole role = sessData.role.get();

		// SimpleDateFormat sf = new SimpleDateFormat("EEE MMM d HH:mm");
		StringBuffer ret = new StringBuffer();
		if(user != null && user.getUsername() != null)
		{
			ret.append(user.getUsername());
		}
		else
		{
			ret.append("<unknown user>");
		}
		
		if(configuration != null)
		{
			ret.append(" logged into ");
			if (configuration.getDbuse() == null)
			{
				ret.append("<unknown>");
			}
			else
			{
				ret.append(configuration.getDbuse());
			}
		}
		
		if(ConfigFlag.HOST_NAME.getValue() != null && ConfigFlag.HOST_NAME.getValue().trim().length() > 0)
		{
			ret.append(" on server " + ConfigFlag.HOST_NAME.getValue() + " ");
		}
		
		if(role != null)
		{
			ret.append(" as ");
			ret.append(role.getName());
		}
		
		// ret.append(" on ");
		// ret.append(sf.format(user.getLoginTime()));
		// ret.append(" using Version ");
		// ret.append(getBuildInfo().getVersion());
		return ret.toString();
	}

	private void freeResources(HttpSession session) throws Exception
	{
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		sessData.freeSessionResources();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Writer writer = new java.io.OutputStreamWriter(response.getOutputStream(), "UTF-8");
		writer.write("GET method is not supported by the IMS MAXIMS CNHost servlet.");
		writer.close();	
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{ 
			processRequest(request, response);		
		}
		catch (Exception e) 
		{
			StringBuffer sb = createResponseString();		
			sb.append("<xml>");
					
			HttpSession session = getSession(request);
			if(!isMobileSession(session) && !isCustomNavigatorSession(session))
				sb.append(this.loadTheme());					
			
			getUILayout(session).renderLogin(sb);
			sb.append("<messages><message>" + ims.framework.utils.StringUtils.encodeXML(e.getMessage()) + "</message></messages>");
			sb.append("</xml>");
			
			write(request, response, sb.toString());
			
			clearAndInvalidateCurrentSession(request);
		}
	}

	private SessionData getSessionData(HttpSession session)
	{
		if(session == null)
			throw new RuntimeException("Invalid session");
		
		return (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);		
	}
	private HttpSession getSession(HttpServletRequest request)
	{
		return getSession(request, true);
	}
	private HttpSession getSession(HttpServletRequest request, boolean createNew)
	{		
		HttpSession session = request.getSession(createNew);
		if (session != null)
		{
			if (session.getAttribute(SessionConstants.SESSION_DATA) == null)
			{
				session.setAttribute(SessionConstants.SESSION_DATA, new SessionData());
			}
			// Session Timeout in ConfigFlag is configured in minutes. HttpSession.setMaxInactiveInterval expects seconds
			session.setMaxInactiveInterval(ConfigFlag.GEN.SESSION_TIMEOUT.getValue() * 60);

			if (session.getAttribute(SessionConstants.REMOTE_HOST) == null)
			{
				//SJSAS Load balancer remote_host returns the load balancer ip not the
				//client. the proxy-ip header returns the clients ip
				if(request.getHeader("proxy-ip")!=null)
				{
					session.setAttribute(SessionConstants.REMOTE_HOST, request.getHeader("proxy-ip"));
				} 
				else
				{
					session.setAttribute(SessionConstants.REMOTE_HOST, request.getRemoteHost());
				}
//			  Try to resolve the name of the IP address, if possible. SJSAS always returns an IP
				try
				{
				    InetAddress inet = InetAddress.getByName( (String)session.getAttribute(SessionConstants.REMOTE_HOST) );
				    String hostName = inet.getCanonicalHostName();
				    if (null!=hostName)
				    	session.setAttribute(SessionConstants.REMOTE_HOST,hostName);
				}
				catch( UnknownHostException uhe )
				{
					// eat the exception! The value has already beed set!
				}

				
				
			}
			if (session.getAttribute(SessionConstants.REMOTE_ADDR) == null)
			{
				session.setAttribute(SessionConstants.REMOTE_ADDR, request.getRemoteAddr());
			}
			
			SessionData sessData = (SessionData)session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData.context.get() == null)
			{
				sessData.context.set(new Context(session));
			}			
			if (sessData.domainSession.get() == null)
			{
				sessData.domainSession.set(ims.domain.DomainSession.getSession(session));
			
				if(!isRulesEngineInitialized)
				{
					isRulesEngineInitialized = RulesEngineFactory.getInstance() != null;					
				}
			}
			
			if(sessData.userAgent.get() == null)
			{				
				String userAgent = request.getHeader("User-Agent");
				sessData.userAgent.set(UserAgent.IE);
				
				if(userAgent != null)
				{
					if(userAgent.startsWith("IMSMobile"))
					{
						sessData.userAgent.set(UserAgent.MOBILE);
					}
					else if(userAgent.startsWith("CustomNavigator"))
					{
						sessData.userAgent.set(UserAgent.CUSTOMNAVIGATOR);
					}
				}				
				
				LocalLogger.warn("Connection " + userAgent);
			}
			
	}
		return session;
	}

	private void handleFormInfoNotification(HttpServletRequest request, HttpSession session, IAppForm form)
	{
		if (ConfigFlag.GEN.RELEASE_MODE == null || !ConfigFlag.GEN.RELEASE_MODE.getValue())
		{
			SessionData sessData = getSessionData(session);
			UIEngine uiEngine = getUIEngine(request, session);
			StringBuffer sb = new StringBuffer();

			// Name and ID
			sb.append("Form: <b>");
			sb.append(form.getName() == null || form.getName().trim().length() == 0 ? "[unknown]" : form.getName());
			sb.append("</b>");

			if (form.isAlias())
			{
				sb.append("<br>");
				sb.append("Alias: <b>");
				sb.append(form.getAliasName() == null || form.getAliasName().trim().length() == 0 ? "[unknown]" : form.getAliasName());
				sb.append("</b>");
			}

			sb.append("<br>");
			sb.append("ID: <b>");
			sb.append(form.getFormId());
			sb.append("</b>");

			sb.append("<br>");

			// UI Logic
			sb.append("<br>");
			sb.append("UI Logic: ");
			sb.append("<b>");
			sb.append(form.getLogicClass() == null || form.getLogicClass().trim().length() == 0 ? "[unknown]" : form.getLogicClass());
			sb.append("</b>");

			// Domain Logic
			sb.append("<br>");
			sb.append("Domain Impl: ");
			sb.append("<b>");
			sb.append(form.getDomainImpl() == null || form.getDomainImpl().trim().length() == 0 ? "[none]" : form.getDomainImpl());
			sb.append("</b>");

			sb.append("<br>");

			// RIE BO Class
			sb.append("<br>");
			sb.append("RIE BO Class: ");
			sb.append("<b>");
			sb.append(form.getRieBoClassName() == null || form.getRieBoClassName().trim().length() == 0 ? "[none]" : form.getRieBoClassName());
			sb.append("</b>");

			sb.append("<br>");

			// Current / Previous Form ID
			sb.append("<br>");
			sb.append("Current Form ID: ");
			sb.append("<b>");
			Integer currentFormID = sessData.currentFormID.get();
			sb.append(currentFormID == null ? "[none]" : currentFormID.toString());
			sb.append("</b>");

			sb.append("<br>");
			sb.append("Previous Form ID: ");
			Integer previousFormID = sessData.previousNonDialogFormID.get();
			sb.append("<b>");
			sb.append(previousFormID == null ? "[none]" : previousFormID.toString());
			sb.append("</b>");

			sb.append("<br>");
			sb.append("Parent Dialog ID: ");
			FormName parentDialogID = uiEngine.getParentDialogFormName();
			sb.append("<b>");
			sb.append(parentDialogID == null ? "[none]" : String.valueOf(parentDialogID.getID()));
			sb.append("</b>");

			// Form Mode
			sb.append("<br>");
			sb.append("<br>");
			sb.append("Form Mode: ");
			FormMode formMode = sessData.currentFormMode.get();
			sb.append("<b>");
			sb.append(formMode == null ? "[none]" : formMode.toString());
			sb.append("</b>");

			uiEngine.addAlert(new FormInfoAlert(sb.toString()));
		}
	}

	private void handleRIERecordsNotification(HttpServletRequest request, HttpSession session, boolean isNewForm, Integer formID)
	{
		if (formID == null)
			throw new CodingRuntimeException("Unknown form");

		SessionData sessData = getSessionData(session);
		if (sessData.listRecordInfo.get() == null)
			throw new CodingRuntimeException("Session data listRecordInfo not initialized");

		UIEngine uiEngine = getUIEngine(request, session);

		IAppForm registeredForm = UIEngine.getForm(sessData, formID);

		// The current form is a dialog, the RIE mode does not apply in this case
		if (registeredForm.isDialog())
		{
			// TODO: Further testing required for dialogs...
			// Clearing last record info
			// sessData.listRecordInfo.get().clear();
			return;
		}

		// Clearing the RIE Alert type so we will not end up with duplicates
		uiEngine.clearAlertsByType(RIEAlert.class);

		if (isNewForm)
		{
			// By default any new form will display active records
			// so we switch to ACTIVE mode
			sessData.listRIERecordsOnly.set(Boolean.FALSE);
		}

		// The current form does not have a local context variable
		// that is marked as recorded in error variable
		if (registeredForm.getRieBoClassName() == null || registeredForm.getRieBoClassName().trim().length() == 0)
		{
			// Clearing last record info
			sessData.listRecordInfo.get().clear();

			// Clearing RIE bo class name so the next list will not search for RIE records
			sessData.rieBoClassName.set(null);

			return;
		}

		// Checking RIE mode
		if (sessData.listRIERecordsOnly.get().booleanValue())
		{
			// We always display the alert when in RIE mode
			// to allow the user to return to the active mode
			uiEngine.addAlert(new RIEAlert("Click this icon to switch back to active view mode.", true));
			sessData.rieBoClassName.set(registeredForm.getRieBoClassName());
		}
		else
		{
			// Searching for RIE record information
			ArrayList<String> boNames = new ArrayList<String>();

			if (registeredForm.getRieBoClassName().indexOf(",") >= 0)
			{
				StringTokenizer st = new StringTokenizer(registeredForm.getRieBoClassName(), ",");
				while (st.hasMoreTokens())
				{
					String boName = st.nextToken();
					if (!boNames.contains(boName))
						boNames.add(boName);
				}
			}
			else
				boNames.add(registeredForm.getRieBoClassName());

			boolean rieFound = false;
			int i = 0;
			while (!rieFound && i < boNames.size())
			{
				ListRecordInformation listInfo = (ListRecordInformation) sessData.listRecordInfo.get().get(boNames.get(i++));
				if (listInfo != null && listInfo.getRieCount() > 0)
				{
					// There are RIE objects so the alert is added
					uiEngine.addAlert(new RIEAlert("RIE record(s) exist for this context. Click this icon to switch to RIE view mode.", false));
					break;
				}
			}

			// Clearing rie bo class name so the next list will not search for RIE records
			sessData.rieBoClassName.set(null);
		}
	}

	private boolean handleRIEAlertClick(SessionData sessData)
	{
		if (sessData == null)
			throw new CodingRuntimeException("Invalid session data");

		boolean rieActive = sessData.listRIERecordsOnly.get().booleanValue();
		
		// If not in RIE mode we clear the bo class name, it is not needed in active mode
		// so next list will not search for rie records
		if (!rieActive)
			sessData.rieBoClassName.set(null);

		sessData.listRIERecordsOnly.set(new Boolean(!rieActive));

		// Note: Returning false will prevent the current form from being refreshed!
		return true;
	}
	private boolean handleFormInfoAlertClick(SessionData sessData, UIEngine uiEngine)
	{
		if (sessData == null)
			throw new CodingRuntimeException("Invalid session data");

		if(InitConfig.getContextViewerDialogId() != null && InitConfig.getContextViewerDialogId() > 0)
		{
			uiEngine.open(new ims.domain.FormName(InitConfig.getContextViewerDialogId()));
			return true;
		}
		
		return false;
	}

	private void renderStoredLocations(SessionData sessData, StringBuffer sb)
	{
		if (sessData == null)
			throw new CodingRuntimeException("Invalid session data");

		ILocation[] locations = sessData.storedLocations.get();
		if (locations == null)
		{
			sb.append("<storedLocations/>");
			return;
		}

		sb.append("<storedLocations>");
		for (int x = 0; x < locations.length; x++)
		{
			ILocation location = locations[x];

			sb.append("<location id=\"" + location.getID());
			sb.append("\">");
			sb.append(StringUtils.encodeXML(location.getName()));
			sb.append("</location>");
		}
		sb.append("</storedLocations>");
	}

	private boolean isLocationSelectionProviderConfigured()
	{
		return InitConfig.getLocationsSelectionFormId() != null && InitConfig.getLocationsSelectionFormId().intValue() > 0 && InitConfig.getLocationProviderClassName() != null && InitConfig.getLocationProviderClassName().trim().length() > 0;
	}

	private boolean hasInvalidLocationContext(UIEngine uiEngine, IAppUser user)
	{
		if (!isLocationSelectionProviderConfigured())
			return false;
		
		ILocationProvider locationProvider = uiEngine.getLocationProvider();
		boolean shouldSelectLocation = locationProvider.shouldSelectLocation(user);
		if(shouldSelectLocation && !locationProvider.hasLocations(user))
			return true;
		
		return false;
	}
	private boolean showLocationSelection(SessionData sessData, UIEngine uiEngine)
	{
		if (!isLocationSelectionProviderConfigured())
			return false;

		ILocationProvider locationProvider = uiEngine.getLocationProvider();
		if (!locationProvider.hasLocations(sessData.user.get()))
			return false;

		if (sessData.selectedLocation.get() == null)
			return true;

		if(locationProvider.validateLocation(sessData.selectedLocation.get()) == null)
			return true;
		
		if(!locationProvider.locationIsAllowed(sessData.selectedLocation.get(), sessData.user.get()))
			return true;
		
		return false;
	}
	
	private static String currentTimestamp() 
	{
	    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	    DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
	    return f.format(c.getTime());
	}
	
	private void processSmartcardTicketReceived(SessionData sessData, SmartcardTicketReceivedEvent event) throws MalformedURLException, IOException
	{			
		boolean debugMode = Boolean.FALSE.equals(ConfigFlag.GEN.RELEASE_MODE.getValue());
		getSamlXmlRequestError = new StringBuffer();
		
		if (event.getLastError() != null && event.getLastError().trim().length() > 0) {
			System.out.println(currentTimestamp()  + "  ** processSmartcardTicketReceived() *** ERROR: Error while retrieving login ticket: " + event.getLastError());
			getSamlXmlRequestError.append("Error while retrieving login ticket:" + event.getLastError() + "\n");
		}


		if (ConfigFlag.DOM.USE_PDS.getValue()!=null && !ConfigFlag.DOM.USE_PDS.getValue().equals("None"))
		{
			if(ConfigFlag.GEN.PDS_ROLE_ASSERTION_SERVLET_URL.getValue() == null ||
					ConfigFlag.GEN.PDS_ROLE_ASSERTION_SERVLET_URL.getValue().trim().length() == 0)
			{
				System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** ERROR: ConfigFlag 'PDS_ROLE_ASSERTION_SERVLET_URL' is not set");
				getSamlXmlRequestError.append("ConfigFlag 'PDS_ROLE_ASSERTION_SERVLET_URL' is not set!" + "\n");		
				
				sessData.smartcardTicketId.set(null);	
				sessData.samlXml.set(null);
				
				return;
			}
		}
		
		if (event.getTicket() != null && event.getTicket().trim().length() > 0 && !event.getTicket().equals("-1"))
		{
			if (debugMode)
				System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: ticket current value: " + event.getTicket());
			
			PrintWriter out = null;
			SSLSocket socket = null;
			
			if (sessData.smartcardTicketId.get() != null && 
					sessData.smartcardTicketId.get().trim().length() > 0 &&
						sessData.samlXml.get() != null &&
								sessData.samlXml.get().trim().length() > 0 &&
									sessData.smartcardTicketId.get().equals(event.getTicket()))
			{
				//TODO: is ticket is valid but getSamlXml() timeout
				return;
			}
			
			//Store ticked in sessData
			sessData.smartcardTicketId.set(event.getTicket());	
			
			try {				
				String destinationUrl = ConfigFlag.GEN.PDS_ROLE_ASSERTION_SERVLET_URL.getValue() + URLEncoder.encode(event.getTicket(), "UTF-8");	
				if (debugMode) {
					System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: GEN.PDS_ROLE_ASSERTION_SERVLET_URL ConfigFlag value: " + ConfigFlag.GEN.PDS_ROLE_ASSERTION_SERVLET_URL.getValue());
					System.out.println(currentTimestamp() + " ** processSmartcardTicketReceived() *** DEBUG: GET url value: " + destinationUrl);
				}
				
				URL url = new URL(destinationUrl);
				
				URLConnection connection = url.openConnection();	//WDEV-22509				
				HttpsURLConnection httpsConn = (HttpsURLConnection) connection;	//WDEV-22509
				
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
				SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
				
				long start = System.currentTimeMillis();
				socket = (SSLSocket)factory.createSocket(url.getHost(), 443);
				long end = System.currentTimeMillis();
				NumberFormat formatter = new DecimalFormat("#0.00000");
				if (debugMode)
					System.out.println(currentTimestamp() + " ** processSmartcardTicketReceived() *** DEBUG: : createSocket() to url  " + destinationUrl +  " took " + formatter.format((end - start) / 1000d) +  " seconds");
				
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				out.println("GET " + url + " HTTP/1.1");
				out.println();
				out.flush();
			
				start = System.currentTimeMillis();

				// Read the response from PDS
				InputStream is = httpsConn.getInputStream();	//WDEV-22509				
				String samlXml = IOUtils.toString(is);	//WDEV-22509				
				
//WDEV-22509    String samlXml = IOUtils.toString(socket.getInputStream(), "UTF-8");   
			    end = System.currentTimeMillis();
				formatter = new DecimalFormat("#0.00000");
				if (debugMode)
					System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: : creating SAML xml string from socket.getInputStream() took " + formatter.format((end - start) / 1000d) +  " seconds");
				
				if(samlXml == null || samlXml.trim().length() == 0) 
				{		
					if (debugMode)
						System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: samlXml value: null");
					
					sessData.samlXml.set(null);
				}
				else 
				{
					if (debugMode)
						System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: response value: " +  samlXml);
					
					Pattern p = Pattern.compile("\\<Response>(.*?)\\</Response>");
					Matcher m = p.matcher(samlXml);
					while(m.find())
					{
						if (debugMode)
							System.out.println(currentTimestamp() + " ** allowPdsInteraction() *** DEBUG: Error response: " + m.group(1));
						
						//Check if SAML xml response is not an error
						DOMParser parser = new DOMParser();
						org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(new java.io.ByteArrayInputStream(m.group(1).getBytes("UTF-8")));
						inputSource.setEncoding("UTF-8");
						parser.parse(inputSource);
						org.w3c.dom.Document doc = parser.getDocument();
						
						if (doc.getDocumentElement().getNodeName().equals("ResponseFault"))
						{
							if (debugMode)
								System.out.println(currentTimestamp() + " ** allowPdsInteraction() *** DEBUG: ResponseFault value: " + doc.getDocumentElement().getNodeValue());
							
							sessData.samlXml.set(null);
							return;
						}
					}
					
					sessData.samlXml.set(null);
					
					String xmlStart = "<samlp:Response";
					String xmlEnd = "</samlp:Response>";
					p = Pattern.compile(xmlStart+ "(.*?)\\" + xmlEnd, Pattern.DOTALL);
					m = p.matcher(samlXml);
					while(m.find())
					{
						if (debugMode) {
							System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** DEBUG: Extracted SAML xml from response: " + xmlStart + m.group(1) + xmlEnd);
						}
						sessData.samlXml.set(xmlStart + m.group(1) + xmlEnd);	
					}
				}
			}
			catch (Exception e) 
			{				
				e.getStackTrace();
				System.out.println(currentTimestamp()  + " ** processSmartcardTicketReceived() *** ERROR: error while retrieving SAML xml: " +   e.getMessage());
				getSamlXmlRequestError.append("Error while retrieving SAML xml: "+ e.getMessage() + "\n");
				sessData.samlXml.set(null);
			}	
			finally {
				if (out != null)
					out.close();	
				if (socket != null)
					socket.close();
			}
		}
		else
		{
			sessData.smartcardTicketId.set(null);	
			sessData.samlXml.set(null);
		}
	}
	
	private void processClientStoredLocations(HttpSession session, StoredLocationsEvent storedLocationsEvent)
	{
		if (isLocationSelectionProviderConfigured())
		{
			SessionData sessData = getSessionData(session);
			sessData.storedLocations.set(storedLocationsEvent.getLocations());
			updateStoredLocations(session, storedLocationsEvent.getSelectedLocation());
		}
	}

	private void updateStoredLocations(HttpSession session, ILocation selectedLocation)
	{
		if (selectedLocation == null)
			return;
		if (!isLocationSelectionProviderConfigured())
			return;

		LocationFactory locationFactory = new LocationFactory(ims.domain.DomainSession.getSession(session));
		ILocationProvider locationProvider = locationFactory.getLocationProvider();

		SessionData sessData = getSessionData(session);

		selectedLocation = locationProvider.validateLocation(selectedLocation);
		ArrayList<ILocation> locationArray = new ArrayList<ILocation>();

		if (selectedLocation != null)
		{
			sessData.selectedLocation.set(selectedLocation);
			locationArray.add(selectedLocation);
		}

		ILocation[] locations = sessData.storedLocations.get();
		if (locations != null)
		{
			for (int x = 0; x < locations.length; x++)
			{
				if (selectedLocation == null || locations[x].getID() != selectedLocation.getID())
				{
					ILocation location = locationProvider.validateLocation(locations[x]);
					if (location != null)
						locationArray.add(location);
				}
			}
		}

		ILocation[] newLocations = new ILocation[locationArray.size()];
		for (int x = 0; x < locationArray.size(); x++)
		{
			newLocations[x] = locationArray.get(x);
		}

		sessData.storedLocations.set(newLocations);
		sessData.sendStoredLocations.set(Boolean.TRUE);
	}

	private void setRIEMode(SessionData sessData, Integer formID)
	{
		IAppForm registeredForm = UIEngine.getForm(sessData, formID);
		if (!registeredForm.isDialog())
			sessData.listRIERecordsOnly.set(Boolean.FALSE);
	}

	private void setCurrentForm(SessionData sessData, int formID)
	{
		setCurrentForm(sessData, new Integer(formID));
	}

	private void setCurrentForm(SessionData sessData, Integer formID)
	{
		Integer currentFormID = sessData.currentFormID.get();

		// no matter if dialog or form the current form ID has to be set
		sessData.currentFormID.set(formID);

		// getting the registered form
		IAppForm form = UIEngine.getForm(sessData, formID);

		// if current form is not dialog the previous form id is updated
		if (!form.isDialog())
		{
			sessData.currentNonDialogFormID.set(formID);

			if (currentFormID == null || !currentFormID.equals(formID))
			{
				sessData.previousNonDialogFormID.set(currentFormID);
			}
		}

	}
	private boolean isRie(SessionData sessData)
	{
		if(sessData.listRIERecordsOnly.get() == null)
			return false;
		
		return sessData.listRIERecordsOnly.get().booleanValue();
	}
	private boolean shouldPromptForSystemPassword(boolean newForm, Integer formName, SessionData sessData)
	{
		// this is the first form, we can't ask for password
		//if(sessData.previousNonDialogFormID.get() == null)
		//	return false;
		
		// this is not a new form so there is no need to ask
		if(!newForm)
			return false;
		
		// this is not a system form so there is no need to ask
		if(!isSystem(formName))
			return false;		
		
		// this is a dialog so there is no need to ask as we don't support system on dialogs
		if(isDialog(formName))
			return false;
		
		// we just asked for this so no need to do it again
		if(formName.equals(sessData.systemFormToOpen.get()))
			return false;
		
		// the config flag asks for password every time
		if(ConfigFlag.GEN.ALWAYS_PROMPT_FOR_SYSTEM_PASSWORD.getValue())
			return true;
		
		// the password was never entered so we need to ask for password
		if(sessData.systemPasswordEntered.get() == null)
			return true;
		
		return !sessData.systemPasswordEntered.get().booleanValue();
	}	
	private boolean isMobileSession(HttpSession session)
	{
		return isMobileSession(getSessionData(session));
	}
	private boolean isCustomNavigatorSession(HttpSession session)
	{
		return isCustomNavigatorSession(getSessionData(session));
	}
	private boolean isMobileSession(SessionData sessData)
	{		
		return sessData.userAgent.get() == UserAgent.MOBILE;
	}
	private boolean isCustomNavigatorSession(SessionData sessData)
	{		
		return sessData.userAgent.get() == UserAgent.CUSTOMNAVIGATOR;
	}
	private UILayout getUILayout(HttpSession session)
	{
		return getUILayout((SessionData)session.getAttribute(SessionConstants.SESSION_DATA));
	}
	private UILayout getUILayout(SessionData sessData)
	{
		return new UILayout(sessData, InitConfig.getTheme());
	}
	private IAppForm getForm(SessionData sessData, Integer formName)
	{
		return (IAppForm)sessData.configurationModule.get().getRegisteredForms().get(formName);		
	}
}