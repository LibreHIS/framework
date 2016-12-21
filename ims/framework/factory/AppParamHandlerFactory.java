package ims.framework.factory;
import java.io.Serializable;

import ims.configuration.InitConfig;
import ims.domain.DomainSession;
import ims.domain.impl.DomainImplFlyweightFactory;
import ims.framework.interfaces.IAppParamHandlerProvider;

public final class AppParamHandlerFactory implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private DomainSession session;
	private String className;	
	private IAppParamHandlerProvider provider;
		
	public AppParamHandlerFactory(DomainSession session)
	{
		this.session = session;
		this.className = InitConfig.getAppParamHandlerProviderClassName();
	}	
	public boolean hasProvider()
	{
		if(className == null || className.length() == 0)
			return false;
		
		if(provider != null)
			return true;
				
		try
		{
			provider = getProvider();
		}
		catch (Exception e)
		{			
			return false;
		}
		
		return provider != null;
	}
	public IAppParamHandlerProvider getProvider()
	{
		if(provider != null)
			return provider;
		
		try 
		{
//			DomainImplFlyweightFactory factory = DomainImplFlyweightFactory.getInstance();
//			Object instance = factory.create(className, session);
			Class pclass = Class.forName(className);
			Object instance = pclass.newInstance();
			if(instance instanceof IAppParamHandlerProvider)
			{
				provider = (IAppParamHandlerProvider)instance;
				return provider;
			}
			else
				throw new RuntimeException("Invalid Application Parameter Handler Provider Class: " + className + " - It should implement IAppParamHandlerProvider interface");
		} 
		catch (ClassNotFoundException e) 
		{		
			throw new RuntimeException("Invalid Application Parameter Handler Provider Class: " + className + " - Class was not found");
		} 
		catch (InstantiationException e) 
		{
			throw new RuntimeException("Invalid Application Parameter Handler Provider Class: " + className + " - Class cannot be instantiated: " + e.getMessage());
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException("Invalid Application Parameter Handler Provider Class: " + className + " - " + e.getMessage());
		}
	}	
}
