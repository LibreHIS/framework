package ims.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Enumeration;

import ims.configuration.AppRight;
import ims.configuration.BuildInfo;
import ims.configuration.ClassConfig;
import ims.framework.ContextQuery;
import ims.framework.controls.DynamicGridCell;
import ims.framework.enumerations.ImageType;
import ims.framework.enumerations.UILayoutState;
import ims.framework.exceptions.EngineException;
import ims.framework.interfaces.IAddressProvider;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IAppRole;
import ims.framework.interfaces.IAppUser;
import ims.framework.interfaces.IExternalEncodingProvider;
import ims.framework.interfaces.ILocation;
import ims.framework.interfaces.ILocationProvider;
import ims.framework.interfaces.INotificationsProvider;
import ims.framework.interfaces.IPrintersProvider;
import ims.framework.interfaces.ISecurityTokenProvider;
import ims.framework.interfaces.ISelectedPatient;
import ims.framework.interfaces.ISpellChecker;
import ims.framework.interfaces.ISystemLogProvider;
import ims.framework.interfaces.ISystemLogWriter;
import ims.framework.interfaces.IUploadDownloadUrlProvider;
import ims.framework.interfaces.IUserProvider;
import ims.framework.utils.Color;
import ims.framework.utils.FileUpload;
import ims.framework.utils.Image;
import ims.framework.utils.ImageInfo;
import ims.scheduler.IScheduledJobsProvider;

public abstract class UIEngine implements ISystemLogWriter, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public abstract boolean isSecureConnection();
	public abstract String getSessionId();
	public abstract Map<String, String> getAllCookies();
	public abstract Map<String, Enumeration> getAllHttpHeaders();
	
    public abstract void showMessage(String text);    
    public abstract void showMessage(String text, String title);
        
    public abstract int showMessage(String text, String title, MessageButtons buttons);
    public abstract int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon);
    public abstract int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon, MessageDefaultButton defaultButton);    
    
    public abstract void showProgressBar(String text);
    
    public abstract boolean openStartUpForm();
    
    public abstract boolean open(FormName value); 
    
    public abstract boolean open(FormName value, Object[] params);
    public abstract boolean open(FormName value, Object[] params, boolean showCloseButtonIfAvailable);
    public abstract boolean open(FormName value, Object[] params, boolean showCloseButtonIfAvailable, boolean resizableDialog);
    
    public abstract boolean open(FormName value, boolean showCloseButtonIfAvailable);
    public abstract boolean open(FormName value, boolean showCloseButtonIfAvailable, boolean resizableDialog);
    
    public abstract boolean open(FormName value, String caption);        
    public abstract boolean open(FormName value, String caption, boolean showCloseButtonIfAvailable);
    public abstract boolean open(FormName value, String caption, boolean showCloseButtonIfAvailable, boolean resizableDialog);
    
    public abstract boolean open(FormName value, Object[] params, String caption);
    public abstract boolean open(FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable);
    public abstract boolean open(FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable, boolean resizableDialog);
    
    public abstract boolean canOpen(FormName value);
    
    public abstract LocalSettings getLocalSettings();
    
    public abstract void close(ims.framework.enumerations.DialogResult result);
    public abstract void setPatientInfo(String value);
    public abstract void setPatientInfo(String value, Color textColor);
    public abstract boolean isFormRegistered(ims.framework.FormName value);
    public abstract String getContextPath();
    public abstract boolean isDialog();
    public abstract void setCaption(String caption);
    public abstract void setCaption(FormName formName, String caption);    
    public abstract FormName getFormName();
    public abstract FormName getPreviousNonDialogFormName();    
    public abstract FormName getParentDialogFormName();    
    
    public abstract FormName getPreviosFormName();
    
    public abstract String getRequestUrl();
    
    public abstract void addAlert(Alert alert);    
    public abstract void removeAlert(Alert alert);    
    public abstract void clearAlertsByType(Class typeOfAlert);
    public abstract void clearAlerts();
    
    public abstract Image registerImage(Image img);
	public abstract void unRegisterImage(Image img);
    public abstract Image getRegisteredImage(int id);
    public abstract Image[] getRegisteredImages();
    public abstract Image[] getActiveRegisteredImages();
    public abstract ImageInfo getImageInfo(Image img);
    public abstract byte[] getImageContent(Image img);
    public abstract Image saveImage(Image img, byte[] content) throws Exception;
    
    public abstract IAppUser getLoggedInUser();
    public abstract void changeUserPassword(IAppUser user, String currentPassword, String newPassword) throws EngineException;
    public abstract IAppRole getLoggedInRole();
    
    public abstract void showErrors(String[] value);
    public abstract void showErrors(String title, String[] value);
    public abstract void showErrors(String title, String[] value, boolean allowDuplicates);
    
	public abstract IAppForm[] getRegisteredForms();
	public abstract IAppForm getRegisteredForm(FormName formName);
	
	public abstract boolean canFormProvideData(FormName formName, IReportSeed[] iSeeds);
	public abstract boolean formHasData(FormName formName, IReportSeed[] iSeeds);
	public abstract IReportField[] getFormData(FormName formName, IReportSeed[] iSeeds);
	public abstract IReportField[] getFormData(FormName formName, IReportSeed[] iSeeds, boolean excludeNulls);
	
	public abstract boolean isRIEMode();
	public abstract boolean supportsRecordedInError(FormName formName);	
	public abstract ims.vo.ValueObject getRecordedInErrorVo(FormName formName);
	public abstract boolean isValidUrl(String url);
	
	public abstract void openUrl(String url);
	public abstract void openUrl(String url, String target);
	public abstract void openUrl(String url, List<UrlParam> params);
	public abstract void openUrl(String url, List<UrlParam> params, String target);
	
	public abstract int openCustomUrl(String url, List<WindowParam> windowParams,  boolean handleWindowEvents);	
	public abstract void closeCustomUrl(int id);
	
	public abstract int openCustomUrlCloseableOnContextChange(String url, List<WindowParam> windowParams,  boolean handleWindowEvents);
	
	public abstract void openEMailClient(String emailAddress);
	public abstract void populate(DynamicGridCell cell, IItemCollection values);
    public abstract void populate(DynamicGridCell cell, IItemCollection values, boolean useEnhancedOptionsIfAvailable);
    public abstract BuildInfo getBuildInfo();
    
    public abstract void setUploadUrl(String url);
    public abstract String getUploadUrl();
    public abstract void setUploadDebug(boolean value);
        
	public abstract String getTheme();
	public abstract ILocation[] getStoredLocations();
	public abstract void setStoredLocations(ILocation[] locations);
	public abstract ILocation getCurrentLocation();
	public abstract void setCurrentLocation(ILocation location);
	public abstract ILocationProvider getLocationProvider();
	
	public abstract ClassConfig getClassConfig();

	public abstract boolean hasRight(AppRight right);
	public abstract TopButtonCollection getBuiltInTopButtons();

    protected final int getFormID(FormName formName)
    {
    	return formName.getID();
    }
    public abstract ContextQuery getContext();
    public abstract UILayoutState getCurrentLayoutState();
	public abstract void setCurrentLayoutState(UILayoutState layoutState);
	public abstract boolean navigationIsCollapsed();
	public abstract void setNavigationCollapsed(boolean value);
	
	public abstract List<ContextDescriptor> getGlobalContextVariablesInfo();
	public abstract List<ContextDescriptor> getPersistentGlobalContextVariablesInfo();
	public abstract List<ContextDescriptor> getLocalContextVariablesInfo();
	
	public abstract void addPatientSelectionToHistory(ISelectedPatient patient);	
	public abstract void clearPatientSelectionHistory();
	
	public abstract ISpellChecker getSpellChecker();
	
	public abstract void notifyExternalApplication(String id);
		
	public abstract ISecurityTokenProvider getSecurityTokenProvider();
	public abstract IExternalEncodingProvider getExternalEncodingProvider();
	public abstract IUserProvider getUserProvider();
	public abstract IAddressProvider getAddressProvider();
	public abstract ISystemLogProvider getSystemLogProvider();
	public abstract INotificationsProvider getNotificationsProvider();
	public abstract IUploadDownloadUrlProvider getUploadDownloadUrlProvider();
	
	public String uploadFile(String url, byte[] content, String fileName, String localFolder)
	{
		return new FileUpload(this, url).upload(content, fileName, localFolder);
	}
	
	public abstract void deleteFile(String fileName);
	public abstract void runExternalApplication(String filePath);
	public abstract void runExternalApplication(String filePath, boolean fixFilePath);
	public abstract void runExternalApplication(String filePath, boolean autoRunEditor, boolean fixFilePath);
	public abstract void runExternalApplication(String filePath, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning);
	public abstract void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, boolean checkForRunningApplicationPerUserSession, String messageToBeDisplayedIfAlreadyRunning);
	public abstract void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning, boolean fixFilePath);
	public abstract void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning, boolean fixFilePath, boolean checkForRunningApplicationPerUserSession);
	public abstract void checkPasswordRequirements(String newPassword, String currentPassword, String[] previousPasswords) throws EngineException;
	
	public final void clearPatientContextInformation()
	{
		clearAlerts();
		setPatientInfo("");
		for(int x = 0; x < ims.framework.enumerations.ContextQueryItem.getAll().size(); x++)
		{
			getContext().clear(ims.framework.enumerations.ContextQueryItem.getAll().get(x));
		}		
	}
	
	public abstract boolean reloadAllBusinessRules();
	public abstract String getCurrentConfigFlagsGroup();
	public abstract IPrintersProvider getPrinterProvider();
	public abstract IScheduledJobsProvider getScheduledJobsProvider();
	
	public abstract ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor, int maxDimension)  throws IOException;
	public abstract ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor)  throws IOException; 
	public abstract ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi)  throws IOException;
	public abstract ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType)  throws IOException;
	public abstract ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer)  throws IOException;
	
	public abstract boolean isFormAccessibleFromNavigation(ims.framework.FormName value);
	
	public abstract void copyToClipboard(String text);
	public abstract void clearClipboard();
	
	public abstract void writeMedicodeInputFile(String content);
	
	public abstract ims.domain.WebServiceData getWebServiceData();
	
	public abstract String getSAMLXmlContent();
	
	public abstract boolean  allowPdsInteraction();
	
	public abstract boolean openDashboardCloseableOnContextChange(String url, List<UrlParam> params);
	public abstract boolean openDashboard(String url, List<UrlParam> params);
}