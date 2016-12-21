package ims.framework.cn;

import ims.framework.Control;
import ims.framework.FormInfo;
import ims.framework.interfaces.IAppForm;
import ims.framework.utils.SizeInfo;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class FormBridgeFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Object createForm(String className, ims.framework.FormLoader loader, ims.framework.Form form, IAppForm appForm, ims.framework.UIFactory factory, ims.framework.Context ctx, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex) throws Exception
	{
		return createForm(className, loader, form, appForm, factory, ctx, false, startControlID, runtimeSize, parentControl, startTabIndex);
	}
	public Object createForm(String className, ims.framework.FormLoader loader, ims.framework.Form form, IAppForm appForm, ims.framework.UIFactory factory, ims.framework.Context ctx, boolean skipContextValidation, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex) throws Exception
	{
		Class cl = Class.forName(className);
		Object result = cl.newInstance();
		java.lang.reflect.Method method = cl.getDeclaredMethod("setContext", new Class[] { ims.framework.FormLoader.class, ims.framework.Form.class, ims.framework.interfaces.IAppForm.class, ims.framework.UIFactory.class, ims.framework.Context.class, Boolean.class, Integer.class, SizeInfo.class, Control.class, Integer.class});
		method.setAccessible(true);
		method.invoke(result, new Object[] { loader, form, appForm, factory, ctx, new Boolean(skipContextValidation), new Integer(startControlID), runtimeSize, parentControl, new Integer(startTabIndex) });
    	return result;
  	}
	public FormInfo createFormInfo(String className, int formId) throws Exception
	{
		Class cl = Class.forName(className);
		Constructor c = cl.getConstructor(new Class[] { Integer.class });
		return (FormInfo)c.newInstance(new Object[] { formId });		
	}
	
	// Singleton
	private FormBridgeFlyweightFactory()
	{
	}
	public static FormBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final FormBridgeFlyweightFactory Singleton = new FormBridgeFlyweightFactory();
	}
}
