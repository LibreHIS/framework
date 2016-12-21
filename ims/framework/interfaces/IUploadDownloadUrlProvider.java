package ims.framework.interfaces;

import ims.framework.Context;
import ims.framework.UIEngine;
import ims.framework.exceptions.ConfigurationException;

public interface IUploadDownloadUrlProvider
{
	public void setContext(Context ctx);
	void upload(UIEngine engine) throws ConfigurationException;
	void download(UIEngine engine) throws ConfigurationException;
	void upload(UIEngine engine,String urlparams) throws ConfigurationException;
	void download(UIEngine engine,String urlparams) throws ConfigurationException;	
}
