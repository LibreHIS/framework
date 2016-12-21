package ims.framework.factory;
import java.io.Serializable;

import ims.configuration.InitConfig;
import ims.framework.interfaces.IExternalEncodingProvider;

public final class ExternalEncodingProviderFactory implements Serializable
{	
	private static final long serialVersionUID = 1L;	
	private String className;	
	private IExternalEncodingProvider provider;
		
	public ExternalEncodingProviderFactory()
	{						
		this.className = InitConfig.getExternalEncodingProviderClassName();
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
	public IExternalEncodingProvider getProvider()
	{
		if(provider != null)
			return provider;
		
		try 
		{
			Class pclass = Class.forName(className);
			Object instance = pclass.newInstance();
			if(instance instanceof IExternalEncodingProvider)
			{
				provider = (IExternalEncodingProvider)instance;
				return provider;
			}
			else
				throw new RuntimeException("Invalid External Encoding Provider Class: " + className + " - It should implement IExternalEncodingProvider interface");
		} 
		catch (ClassNotFoundException e) 
		{		
			throw new RuntimeException("Invalid External Encoding Provider Class: " + className + " - Class was not found");
		} 
		catch (InstantiationException e) 
		{
			throw new RuntimeException("Invalid External Encoding Provider Class: " + className + " - Class cannot be instantiated: " + e.getMessage());
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException("Invalid External Encoding Provider Class: " + className + " - " + e.getMessage());
		}
	}	
}
