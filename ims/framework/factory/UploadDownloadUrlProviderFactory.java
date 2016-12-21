package ims.framework.factory;
import java.io.Serializable;

import ims.configuration.InitConfig;
import ims.domain.DomainSession;
import ims.domain.impl.DomainImplFlyweightFactory;
import ims.framework.Context;
import ims.framework.interfaces.IUploadDownloadUrlProvider;

public final class UploadDownloadUrlProviderFactory implements Serializable
{	
	private static final long serialVersionUID = 1L;	
	private String className;	
	private IUploadDownloadUrlProvider provider;
	private Context ctx;
	private DomainSession session;
	
			
	public UploadDownloadUrlProviderFactory(DomainSession sess)
	{						
		this.session = sess;
		this.className = InitConfig.getUploadDownloadUrlProviderClassName();		
	}	

	public UploadDownloadUrlProviderFactory(DomainSession sess, Context ctx)
	{						
		this.ctx = ctx;
		this.session = sess;
		this.className = InitConfig.getUploadDownloadUrlProviderClassName();		
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
	public IUploadDownloadUrlProvider getProvider()
	{ 
		if(provider != null)
			return provider;
		
		try 
		{
			DomainImplFlyweightFactory factory = DomainImplFlyweightFactory.getInstance();
			Object instance = factory.create(className, session);
			if(instance instanceof IUploadDownloadUrlProvider)
			{
				provider = (IUploadDownloadUrlProvider)instance;
				provider.setContext(ctx);
				return provider;
			}
			else
				throw new RuntimeException("Invalid Upload Download Url Provider Class: " + className + " - It should implement IUploadDownloadUrlProvider interface");
		} 
		catch (ClassNotFoundException e) 
		{		
			throw new RuntimeException("Invalid Upload Download Url Provider Class: " + className + " - Class was not found");
		} 
		catch (InstantiationException e) 
		{
			throw new RuntimeException("Invalid Upload Download Url Provider Class: " + className + " - Class cannot be instantiated: " + e.getMessage());
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException("Invalid Upload Download Url Provider Class: " + className + " - " + e.getMessage());
		}
	}	
}
