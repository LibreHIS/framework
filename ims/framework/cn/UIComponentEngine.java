package ims.framework.cn;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ims.configuration.AppRight;
import ims.framework.Alert;
import ims.framework.FormName;
import ims.framework.MessageButtons;
import ims.framework.MessageDefaultButton;
import ims.framework.MessageIcon;
import ims.framework.UrlParam;
import ims.framework.enumerations.ImageType;
import ims.framework.enumerations.SystemLogLevel;
import ims.framework.enumerations.SystemLogType;
import ims.framework.interfaces.IAddressProvider;
import ims.framework.interfaces.IAppRole;
import ims.framework.interfaces.IAppUser;
import ims.framework.interfaces.IExternalEncodingProvider;
import ims.framework.interfaces.ILocation;
import ims.framework.interfaces.INotificationsProvider;
import ims.framework.interfaces.IPrintersProvider;
import ims.framework.interfaces.ISystemLog;
import ims.framework.interfaces.ISystemLogProvider;
import ims.framework.interfaces.IUploadDownloadUrlProvider;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.scheduler.IScheduledJobsProvider;

public class UIComponentEngine extends ims.framework.UIComponentEngine
{ 
	private static final long serialVersionUID = 1L;
	
	protected UIEngine engine;
	protected String componentIdentifier;
	
	public UIComponentEngine(UIEngine engine, String componentIdentifier)
	{
		this.engine = engine;		
		this.componentIdentifier = componentIdentifier;
	}
	
	@Override
	public String getSessionId()
	{
		return engine.getSessionId();
	}
	@Override
	public void showMessage(String text)
	{
		engine.showMessage(text);
	}
	@Override
	public void showMessage(String text, String title)
	{
		engine.showMessage(text, title);
	}        
	@Override
    public int showMessage(String text, String title, MessageButtons buttons)
    {
    	return engine.showMessage(text, title, buttons);
    }
    @Override
    public int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon)
    {
    	return engine.showMessage(text, title, buttons, icon);
    }
    @Override
    public int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon, MessageDefaultButton defaultButton)
    {
    	return engine.showMessage(text, title, buttons, icon, defaultButton);
    }    
    @Override
    public void showProgressBar(String text)
    {
    	engine.showProgressBar(text);
    }
    @Override
	public boolean open(ims.framework.FormName value)
	{	    
	    return engine.openForm(value, null, null, null, null, componentIdentifier);
	}
	@Override
	public boolean open(ims.framework.FormName value, boolean showCloseButtonIfAvailable)
    {		
		return engine.openForm(value, null, null, showCloseButtonIfAvailable, null, componentIdentifier);
    }
	@Override
	public boolean open(ims.framework.FormName value, String caption)
	{
	    return engine.openForm(value, null, caption, null, null, componentIdentifier);
	}
	@Override
	public boolean open(ims.framework.FormName value, String caption, boolean showCloseButtonIfAvailable)
    {
		return engine.openForm(value, null, caption, showCloseButtonIfAvailable, null, componentIdentifier);
    }
	@Override
	public void showErrors(String[] value)
	{
		engine.showErrors(value);
	}
	@Override
    public void showErrors(String title, String[] value)
    {
    	engine.showErrors(title, value);
    }
    @Override
    public Image getRegisteredImage(int id)
    {
    	return engine.getRegisteredImage(id);
    }
    @Override
    public Image[] getRegisteredImages()
    {
    	return engine.getRegisteredImages();
    }
    @Override
    public FormName getFormName()
    {
    	return engine.getFormName();
    }
    @Override
    public void openUrl(String url)
    {
    	engine.openUrl(url);
    }
    @Override
    public void openUrl(String url, List<UrlParam> params)
    {
    	engine.openUrl(url, params);
    }
    @Override
    public boolean canOpen(FormName value)
    {
    	return engine.canOpen(value);
    }
    @Override
    public IAddressProvider getAddressProvider()
	{
		return engine.getAddressProvider();
	}
    @Override
    public ISystemLogProvider getSystemLogProvider()
    {
    	return engine.getSystemLogProvider();
    }
    @Override
    public String getRequestUrl()
    {
    	return engine.getRequestUrl();
    }
    @Override
    public void deleteFile(String fileName)
    {
    	engine.deleteFile(fileName);
    }
    @Override
    public void runExternalApplication(String filePath)
    {
    	engine.runExternalApplication(filePath);
    }
    @Override
    public void runExternalApplication(String filePath, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning)
    {
    	engine.runExternalApplication(filePath, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning);
    }
    @Override
    public void runExternalApplication(String filePath, boolean allowMultipleInstances, boolean checkForRunningApplicationPerUserSession, String messageToBeDisplayedIfAlreadyRunning)
    {
    	engine.runExternalApplication(filePath, allowMultipleInstances, checkForRunningApplicationPerUserSession, messageToBeDisplayedIfAlreadyRunning);
    }
    @Override
    public void setPatientInfo(String value)
    {
    	engine.setPatientInfo(value);
    }
    @Override
    public void setPatientInfo(String value, Color textColor)
    {
    	engine.setPatientInfo(value, textColor);
    }
    @Override
    public ILocation getCurrentLocation()
    {
    	return engine.getCurrentLocation();
    }
    @Override
    public boolean hasRight(AppRight right)
    {
    	return engine.hasRight(right);
    }
    @Override
    public void addAlert(Alert alert)
    {
    	engine.addAlert(alert);
    }
    @Override
    public void removeAlert(Alert alert)
    {
    	engine.removeAlert(alert);
    }
    @Override
    public void clearAlertsByType(Class typeOfAlert)
    {
    	engine.clearAlertsByType(typeOfAlert);
    }
    @Override
    public void clearAlerts()
    {
    	engine.clearAlerts();
    }
	@Override
	public IAppRole getLoggedInRole()
	{
		return engine.getLoggedInRole();
	}
	@Override
	public IAppUser getLoggedInUser()
	{
		return engine.getLoggedInUser();
	}
	@Override
	public String uploadFile(String url, byte[] content, String fileName, String localFolder)
	{
		return engine.uploadFile(url, content, fileName, localFolder);
	}
	public ISystemLog createSystemLogEntry(SystemLogType type, SystemLogLevel level, String message)
	{
		return engine.createSystemLogEntry(type, level, message);
	}
	@Override
	public IExternalEncodingProvider getExternalEncodingProvider()
	{
		return engine.getExternalEncodingProvider();
	}

	@Override
	public boolean open(FormName value, Object[] params) 
	{
		return engine.openForm(value, params, null, null, null, componentIdentifier);
	}

	@Override
	public boolean open(FormName value, Object[] params, boolean showCloseButtonIfAvailable) 
	{
		return engine.openForm(value, params, null, showCloseButtonIfAvailable, null, componentIdentifier);
	}

	@Override
	public boolean open(FormName value, Object[] params, boolean showCloseButtonIfAvailable, boolean resizableDialog) 
	{
		return engine.openForm(value, params, null, showCloseButtonIfAvailable, resizableDialog, componentIdentifier);
	}

	@Override
	public boolean open(FormName value, Object[] params, String caption) 
	{
		return engine.openForm(value, params, caption, null, null, componentIdentifier);
	}

	@Override
	public boolean open(FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable) 
	{
		return engine.openForm(value, params, caption, showCloseButtonIfAvailable, null, componentIdentifier);
	}

	@Override
	public boolean open(FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable, boolean resizableDialog) 
	{
		return engine.openForm(value, params, caption, showCloseButtonIfAvailable, resizableDialog, componentIdentifier);
	}
	
	@Override
	public INotificationsProvider getNotificationsProvider()
	{
		return engine.getNotificationsProvider();
	}

	@Override
	public IUploadDownloadUrlProvider getUploadDownloadUrlProvider() 
	{
		return engine.getUploadDownloadUrlProvider();
	}
	
	@Override
	public IPrintersProvider getPrinterProvider()
	{
		return engine.getPrinterProvider();
	}
	
	@Override
	public IScheduledJobsProvider getScheduledJobsProvider()
	{
		return engine.getScheduledJobsProvider();
	}
	public boolean isRIEMode()
	{
		return engine.isRIEMode();
	}
	
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor) throws IOException 
	{
		return engine.convertPdfToImages(inputBuffer, outputType, dpi, scalingFactor, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi) throws IOException 
	{
		return engine.convertPdfToImages(inputBuffer, outputType, dpi, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType) throws IOException 
	{
		return engine.convertPdfToImages(inputBuffer, outputType, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer) throws IOException 
	{
		return engine.convertPdfToImages(inputBuffer, ImageType.JPG, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor, int maxDimension) throws IOException 
	{
		return engine.convertPdfToImages(inputBuffer, outputType, dpi, scalingFactor, maxDimension);
	}
	public void copyToClipboard(String text) 
	{	
		engine.copyToClipboard(text);			
	}		
	public void clearClipboard()
	{
		engine.clearClipboard();
	}	
	public String getSAMLXmlContent()
	{
		return engine.getSAMLXmlContent();
	}
	public boolean allowPdsInteraction()
	{
		return engine.allowPdsInteraction();
	}
}
