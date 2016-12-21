package ims.framework.cn;

import java.io.Serializable;
import java.util.StringTokenizer;

import ims.framework.FormAccessLogic;
import ims.framework.UILogic;
import ims.framework.interfaces.IAppForm;

public class UILogicFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final org.apache.log4j.Logger	LocalLogger			= ims.utils.Logging.getLogger(UILogicFactory.class);
	public UILogic createUILogic(String moduleName, String logicClassName) throws Exception
	{
		if (null == logicClassName) 
		{
			StringTokenizer st = new StringTokenizer(moduleName,".");
			String nspace = st.nextToken();
			String formName = st.nextToken();
			logicClassName = "ims." + nspace.toLowerCase() + ".forms." +  formName.toLowerCase() + ".Logic";		
		}
		Class logicClass = Class.forName(logicClassName);
		UILogic logic = (UILogic)logicClass.newInstance();
		return logic;
	}	
	public FormAccessLogic createFormAccessLogic(IAppForm form) throws Exception, IllegalAccessException
	{
		String moduleName = form.getModule();
		
		StringTokenizer st = new StringTokenizer(moduleName,".");
		String namespace = st.nextToken();
		String formName = st.nextToken();
		
		String accessLogicClassName = "ims." + namespace.toLowerCase() + ".forms." +  formName.toLowerCase() + ".AccessLogic";
		
		return createFormAccessLogic(accessLogicClassName, form.getFormId());
	}
	public FormAccessLogic createFormAccessLogic(String accessLogicClassName, Integer formId) throws Exception
	{
		Class logicClass = null;
		try
		{
			logicClass = Class.forName(accessLogicClassName);
		}
		catch (Exception e)
		{
			// If a form included in navigation does not have AccessLogic we should throw an error
			// For now we ignore it and return null - in this case the form will not appear in the navigation
			// and that's good for development.
			
			// throw new RuntimeException("The access logic '" + accessLogicClassName + "' was not found");
			//FWUI-884
			LocalLogger.warn("The access logic '" + accessLogicClassName + "' (ID: " + (formId == null ? "unknown" : String.valueOf(formId)) + ") was not found.");
			return null;
		}
		
		FormAccessLogic accessLogic = (FormAccessLogic)logicClass.newInstance(); 
		return accessLogic;
	}
	
	// Singleton
	private UILogicFactory()
	{
	}
	public static UILogicFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final UILogicFactory Singleton = new UILogicFactory();
	}
}
