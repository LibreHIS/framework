package ims.framework.cn;

import ims.configuration.AppRight;
import ims.configuration.BuildInfo;
import ims.configuration.ClassConfig;
import ims.configuration.ConfigFlag;
import ims.configuration.Configuration;
import ims.domain.SessionData;
import ims.domain.WebServiceData;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.factory.AddressFactory;
import ims.domain.factory.ConfigFlagsFactory;
import ims.domain.factory.ContextEvalFactory;
import ims.domain.factory.DictionaryFactory;
import ims.domain.factory.LocationFactory;
import ims.domain.factory.PrintersFactory;
import ims.domain.factory.ScreenHintFactory;
import ims.domain.factory.SecurityTokenFactory;
import ims.domain.factory.SystemLogFactory;
import ims.domain.factory.UserFactory;
import ims.framework.Alert;
import ims.framework.ContextDescriptor;
import ims.framework.ContextQuery;
import ims.framework.ExternalApplication;
import ims.framework.FormBridge;
import ims.framework.FormName;
import ims.framework.IEnhancedItem;
import ims.framework.IItem;
import ims.framework.IItemCollection;
import ims.framework.IReportField;
import ims.framework.IReportSeed;
import ims.framework.LocalSettings;
import ims.framework.Location;
import ims.framework.MessageButtons;
import ims.framework.MessageDefaultButton;
import ims.framework.MessageIcon;
import ims.framework.ObjectInstanceSize;
import ims.framework.SessionConstants;
import ims.framework.TopButtonCollection;
import ims.framework.TopButtonSection;
import ims.framework.Url;
import ims.framework.UrlParam;
import ims.framework.WindowParam;
import ims.framework.controls.DynamicForm;
import ims.framework.controls.DynamicGridCell;
import ims.framework.enumerations.ImageType;
import ims.framework.enumerations.SystemLogLevel;
import ims.framework.enumerations.SystemLogType;
import ims.framework.enumerations.UILayoutState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.EngineException;
import ims.framework.factory.ExternalEncodingProviderFactory;
import ims.framework.factory.UploadDownloadUrlProviderFactory;
import ims.framework.interfaces.IAddressProvider;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IAppRole;
import ims.framework.interfaces.IAppUser;
import ims.framework.interfaces.IConfigFlagsProvider;
import ims.framework.interfaces.IDynamicNavigation;
import ims.framework.interfaces.IExternalEncodingProvider;
import ims.framework.interfaces.ILocation;
import ims.framework.interfaces.ILocationProvider;
import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.INotificationsProvider;
import ims.framework.interfaces.IPrintersProvider;
import ims.framework.interfaces.IScreenHint;
import ims.framework.interfaces.IScreenHintProvider;
import ims.framework.interfaces.ISecurityTokenProvider;
import ims.framework.interfaces.ISelectedPatient;
import ims.framework.interfaces.ISpellChecker;
import ims.framework.interfaces.ISystemLog;
import ims.framework.interfaces.ISystemLogProvider;
import ims.framework.interfaces.IUploadDownloadUrlProvider;
import ims.framework.interfaces.IUserProvider;
import ims.framework.utils.DateTime;
import ims.framework.utils.Image;
import ims.framework.utils.Color;
import ims.framework.utils.ImageInfo;
import ims.framework.utils.ImagePath;
import ims.framework.utils.PDF;
import ims.framework.utils.SpellCheckerFactory;
import ims.notifications.NotificationsFactory;
import ims.rules.engine.RulesEngine;
import ims.rules.exceptions.RulesEngineCompilationException;
import ims.rules.exceptions.RulesEngineException;
import ims.scheduler.IScheduledJobsProvider;
import ims.scheduler.ScheduledJobsFactory;
import ims.vo.ValueObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public final class UIEngine extends ims.framework.UIEngine
{  
    private static final String	OPENED_FORM_ERROR_ACCESS_LEVEL	= "Requested feature cannot be accessed as it does not meet the minimum context data requirements.";
    private static final String	OPENED_FORM_ERROR_NOT_AVAILABLE	= "Requested feature is not available.";
	private static final long serialVersionUID = 1L;
    private transient HttpSession session;
    private transient HttpServletRequest request;
    private SessionData sessData;
    private ContextQuery contextQuery;
    private String callerIdentifier = null;
    private DynamicForm parentDynamicForm = null;
    
    private enum OpenMode {CLOSEABLE_ON_CONTEXT_CHANGE, CUSTOM_URL};

    public UIEngine (HttpServletRequest request, HttpSession session)
    {
    	this(request, session, null, null);
    }
    public UIEngine (HttpServletRequest request, HttpSession session, String callerIdentifier, DynamicForm parentDynamicForm)
    {    	    	
    	setContext(request, session);
    	
    	this.callerIdentifier = callerIdentifier;
    	this.parentDynamicForm = parentDynamicForm;
    }
    
    public boolean isSecureConnection()
    {
    	return request.isSecure();
    }
    public String getSessionId()
    {
    	return session.getId(); 
    }
    
    /**
     *  (non-Javadoc)
     * @see ims.framework.UIEngine#getAllcookies()
     * returns a Map (implemented as a Hashmap) containing all the 
     * name - values of the currect requests cookies 
     * if no cookies have been set an empty hashmap is returned
     */
    public Map<String, String> getAllCookies()
	{
		HashMap<String, String> cookieMap = new HashMap<String, String>();
		if (request != null)
		{
			Cookie[] cookies = request.getCookies();
			if (cookies != null)
			{
				for (int i = 0; i < cookies.length; i++)
				{
					cookieMap.put(cookies[i].getName(), cookies[i].getValue());
				}
			}
		}
		return cookieMap;
	}
    
    public Map<String,Enumeration> getAllHttpHeaders()
    {
    	HashMap<String,Enumeration> headerMap = new HashMap<String,Enumeration>();
    	if(request != null)
    	{
    		Enumeration headerNames = request.getHeaderNames();
    		while(headerNames.hasMoreElements() )
    		{
    			String headerName = headerNames.nextElement().toString();
    			headerMap.put(headerName,request.getHeaders(headerName));
    		}
    		
    	}
    	return headerMap;
    }
    
    
    public ims.framework.ContextQuery getContext()
    {
    	if(contextQuery == null)
    		contextQuery = new ContextQuery(session);
    	return contextQuery;
    }
    private final void setContext(HttpServletRequest request, HttpSession session)
    {
        this.session = session;
        this.request = request;
        this.sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
    }
        
	@SuppressWarnings("unchecked")
	public void showMessage(String text)
	{
		//this.sessData.messageBoxes.get().add(new MessageBox(text, "Application"));
		
        text = text.replace('\"', '\'');
        ArrayList script = this.sessData.messageBox.get();
		script.add(text);		
	}
	public void showMessage(String text, String title)
	{		
		this.sessData.messageBoxes.get().add(new MessageBox(text, title));
	}    
    public int showMessage(String text, String title, MessageButtons buttons)
    {   
    	int identifierID = getNextMessageBoxIdentifier();
    	this.sessData.messageBoxes.get().add(new MessageBox(identifierID, text, title, buttons));
    	return identifierID;
    }
    public int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon)
    {    	
    	int identifierID = getNextMessageBoxIdentifier();
    	this.sessData.messageBoxes.get().add(new MessageBox(identifierID, text, title, buttons, icon));
    	return identifierID;
    }
    public int showMessage(String text, String title, MessageButtons buttons, MessageIcon icon, MessageDefaultButton defaultButton)
    {    	
    	int identifierID = getNextMessageBoxIdentifier();
    	this.sessData.messageBoxes.get().add(new MessageBox(identifierID, text, title, buttons, icon, defaultButton));
    	return identifierID;
    }
    
    public void showProgressBar(String text)
    {
    	sessData.progressBarText.set(text);
    }
    
    
	public boolean openStartUpForm()
	{
		sessData.startupMode.set(Boolean.FALSE);
		sessData.currentUILayoutState.set(UILayoutState.DEFAULT);
		sessData.uiLayoutChanged.set(true);
		return open(getStartUpForm());
	}
	public boolean open(ims.framework.FormName value)
	{
		return openForm(value, null, null, null, null, callerIdentifier);
	}
	public boolean open(ims.framework.FormName value, Object[] params)
	{
		return openForm(value, params, null, null, null, callerIdentifier);
	}
	public boolean open(ims.framework.FormName value, Object[] params, boolean showCloseButtonIfAvailable)
    {
		return openForm(value, params, null, new Boolean(showCloseButtonIfAvailable), null, callerIdentifier);
    }
	public boolean open(ims.framework.FormName value, Object[] params, boolean showCloseButtonIfAvailable, boolean resizableDialog)
    {
		return open(value, params, null, new Boolean(showCloseButtonIfAvailable), new Boolean(resizableDialog));
    }	
	public boolean open(ims.framework.FormName value, boolean showCloseButtonIfAvailable)
    {
		return openForm(value, null, null, new Boolean(showCloseButtonIfAvailable), null, callerIdentifier);
    }
	public boolean open(ims.framework.FormName value, boolean showCloseButtonIfAvailable, boolean resizableDialog)
    {
		return open(value, null, null, new Boolean(showCloseButtonIfAvailable), new Boolean(resizableDialog));
    }		
	public boolean open(ims.framework.FormName value, String caption)
	{
	    return openForm(value, null, caption, null, null, callerIdentifier);
	}
	public boolean open(ims.framework.FormName value, String caption, boolean showCloseButtonIfAvailable)
    {
		return openForm(value, null, caption, new Boolean(showCloseButtonIfAvailable), null, callerIdentifier);
    }
	public boolean open(ims.framework.FormName value, String caption, boolean showCloseButtonIfAvailable, boolean resizableDialog)
    {
		return open(value, null, caption, new Boolean(showCloseButtonIfAvailable), new Boolean(resizableDialog));
    }
	public boolean open(ims.framework.FormName value, Object[] params, String caption)
	{
	    return openForm(value, params, caption, null, null, callerIdentifier);
	}	
	public boolean open(ims.framework.FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable)
    {
		return openForm(value, params, caption, new Boolean(showCloseButtonIfAvailable), null, callerIdentifier);
    }
	public boolean open(ims.framework.FormName value, Object[] params, String caption, boolean showCloseButtonIfAvailable, boolean resizableDialog)
    {
		return openForm(value, params, caption, new Boolean(showCloseButtonIfAvailable), new Boolean(resizableDialog), callerIdentifier);
    }
	public boolean canOpen(ims.framework.FormName value)
	{
		if(!FormExist(value))
		{
			return false;
		}
		if(!canOpenCheckAccessLevel(value))
		{
			return false;
		}		
		if(isDialog() && !IsDialog(value))
		{
			return false;
		}
		
		return true;
	}	
	public boolean openForm(ims.framework.FormName value, Object[] params, String caption, Boolean showCloseButtonIfAvailable, Boolean resizableDialog, String callerIdentifier)
	{
		if(parentDynamicForm != null && !IsDialog(value))
		{
			parentDynamicForm.open(value, params);
			return true;
		}		
		if(!FormExist(value))
		{
			showMessage(OPENED_FORM_ERROR_NOT_AVAILABLE);
			return false;
		}
		if(!canOpenCheckAccessLevel(value))
		{
			showMessage(OPENED_FORM_ERROR_ACCESS_LEVEL);
			return false;
		}		
		if(isDialog() && !IsDialog(value))
		{
			throw new CodingRuntimeException("Cannot open a form from a dialog.");
		}
		
		if(caption != null)
			this.sessData.captionOverride.set(caption);
		
		if(showCloseButtonIfAvailable != null)
			this.sessData.showCloseButtonForDialog.set(showCloseButtonIfAvailable);
		
		if(resizableDialog != null)
			this.sessData.showResizableDialog.set(resizableDialog);
		
		// settings the form arguments
		this.sessData.formParams.set(params == null ? new Object[0] : params);
		
		if(this.sessData.openDialogCallerIdentifiers.get() == null)
			this.sessData.openDialogCallerIdentifiers.set(new ArrayList<String>());
		this.sessData.openDialogCallerIdentifiers.get().add(callerIdentifier);
		
		this.sessData.handleOpen(new Integer(super.getFormID(value)));
		
		return true;
	}
	private boolean FormExist(FormName value)
	{
		Map map = this.sessData.configurationModule.get().getRegisteredForms();
		return map.get(new Integer(value.getID())) != null;				
	}
	private boolean IsDialog(FormName value)
	{
		Map map = this.sessData.configurationModule.get().getRegisteredForms();
		IAppForm registeredForm = (IAppForm)map.get(new Integer(value.getID()));
		if(registeredForm == null)
			return false;
		return registeredForm.isDialog();
	}
	
	public boolean isFormAccessibleFromNavigation(ims.framework.FormName value)
	{
		try
		{
			ims.domain.ContextEvalFactory evalFactory = new ContextEvalFactory(sessData.domainSession.get());
			ims.framework.FormAccessLevel formAccessLevel = new FormAccessLevel(new FormAccessLoader(session), evalFactory);			
			IDynamicNavigation dynamicNavigation = sessData.currentDynamicNavigation.get();			
			INavForm navForm = dynamicNavigation.getNavigation().getNavForm(value.getID());
			
			return formAccessLevel.isAccessibleFromNavigation(navForm, sessData); // FWUI-1770
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	private boolean canOpenCheckAccessLevel(ims.framework.FormName value)
	{		
		ims.domain.ContextEvalFactory evalFactory = new ContextEvalFactory(sessData.domainSession.get());
		ims.framework.FormAccessLevel formAccessLevel = new FormAccessLevel(new FormAccessLoader(session), evalFactory);		
	
		try
		{ 			
			return formAccessLevel.isAccessibleFromNavigation(value.getID());
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
    @SuppressWarnings("unchecked")
	public void close(ims.framework.enumerations.DialogResult result)
    {
		ArrayList dialogResults = this.sessData.dialogResults.get();
		//If this close has been called from a form that is NOT a dialogue, then dialogResults == null
		//Just return silently. Doesn't need to be an error.
		if (dialogResults == null) return;
		
   		dialogResults.set(dialogResults.size() - 1, result);
   		this.sessData.closeForm.set(Boolean.TRUE);
    }

    public void setPatientInfo(String value)
    {
    	this.sessData.patientInfo.set(value);
    	this.sessData.patientInfoTextColor.set("");
    }
    public void setPatientInfo(String value, Color textColor)
    {
    	this.sessData.patientInfo.set(value);
    	this.sessData.patientInfoTextColor.set(textColor.toString());
    }    
    public boolean isFormRegistered(ims.framework.FormName value)
    {
    	if(value == null)
    		throw new CodingRuntimeException("Invalid form");
    	return this.sessData.configurationModule.get().getRegisteredForms().get(new Integer(super.getFormID(value))) != null;    	
    }
//    public boolean isFormAccessible(ims.framework.FormName value)
//    {    	
//    	if(value == null)
//    		throw new CodingRuntimeException("Invalid form");
//    	
//    	RegisteredForm form = (RegisteredForm)(this.sessData.configurationModule.get().getRegisteredForms().get(new Integer(super.getFormID(value))));
//    	if (form == null)
//    		return false; // form is not registered so it is not accessible (it might be better to throw exception).
//    	
//    	Role role = this.sessData.role.get();
//		Configuration configuration = this.sessData.configurationModule.get();
//		return configuration.isFormAccessible(role, form.getFormID());
//    }
    
    public String getContextPath()
	{
    	return this.request.getContextPath();
    }
    public boolean isDialog()
	{
    	if(this.sessData.currentFormID.get() == null)
    		return false;
    	
    	Map map = this.sessData.configurationModule.get().getRegisteredForms();
    	IAppForm registeredForm = (IAppForm) map.get(this.sessData.currentFormID.get());
    	return registeredForm.isDialog();
    }
    
	public void setCaption(String caption)
	{
		Map map = this.sessData.configurationModule.get().getRegisteredForms();
    	IAppForm registeredForm = (IAppForm) map.get(this.sessData.currentFormID.get());
		//if (registeredForm.isDialog()) 
		{
			registeredForm.setCaption(caption);
		}
	}

	public void setCaption(ims.framework.FormName formName, String caption)
	{
		Map map = this.sessData.configurationModule.get().getRegisteredForms();
	    IAppForm registeredForm = (IAppForm) map.get(new Integer(super.getFormID(formName)));
		//if (registeredForm.isDialog()) 
		{
			registeredForm.setCaption(caption);
		}
	}
	
    public ims.framework.FormName getFormName()
	{
    	class FormName extends ims.framework.FormName
		{
            private static final long serialVersionUID = 1L;
    		FormName(int id)
			{
    			super(id);
    		}
    	}
    	return new FormName(this.sessData.currentFormID.get().intValue());
    }    
    public ims.framework.FormName getPreviousNonDialogFormName()
	{
    	if(parentDynamicForm != null)
    	{
    		return parentDynamicForm.getPreviousNonDialogFormName();
    	}
    	
    	class FormName extends ims.framework.FormName
		{
            private static final long serialVersionUID = 1L;
    		FormName(int id)
			{
    			super(id);
    		}
    	}
    	
    	Integer formID = null;
    	    	
    	if(isDialog())
    	{
    		formID = this.sessData.currentNonDialogFormID.get();
    	}
    	else
    	{
    		formID = this.sessData.previousNonDialogFormID.get();
    	}
    	
    	return formID == null ? null : new FormName((formID).intValue());
    }
    public ims.framework.FormName getParentDialogFormName()
    {
    	class FormName extends ims.framework.FormName
    	{
            private static final long serialVersionUID = 1L;
    		FormName(int id, String name)
    		{
    			super(id, name);
    		}
    	}
    	
    	if(isDialog())
    	{
    		ArrayList previousForms = this.sessData.previousForms.get();
    		if(previousForms == null)
    			return null;
    		
    		if(previousForms.size() > 0)
    		{
    			IAppForm form = getForm(sessData, (Integer)previousForms.get(previousForms.size() - 1));
    			
    			if(form != null && form.isDialog())
    			{
    				return new FormName(form.getFormId(), form.getName());
    			}
    		}
    	}
    	
    	return null;
    }   
    public ims.framework.FormName getPreviosFormName()
    {
    	class FormName extends ims.framework.FormName
    	{
            private static final long serialVersionUID = 1L;
    		FormName(int id, String name)
    		{
    			super(id, name);
    		}
    	}
    	
    	ArrayList previousForms = this.sessData.previousForms.get();
		if(previousForms == null)
			return null;
    	
		if(previousForms.size() > 0)
		{
			IAppForm form = getForm(sessData, (Integer)previousForms.get(previousForms.size() - 1));
			
			if(form != null)
			{
				return new FormName(form.getFormId(), form.getName());
			}
		}
		
    	return null;
    }    
    public IAppUser getLoggedInUser()
    {
    	return this.sessData.user.get();
    }
    
    public void changeUserPassword(IAppUser user, String currentPassword, String newPassword) throws EngineException
    {
    	IUserProvider provider = getUserProvider();
    	if(provider == null)
    		throw new EngineException("Not supported.");
    	try
		{
    		checkPasswordRequirements(newPassword, currentPassword, user.getUserPreviousPasswords());
    		user = provider.changePassword(user, newPassword);
			user.setClearPassword(newPassword);			
			this.sessData.user.set(user);
		}
		catch (Exception e)
		{
			throw new EngineException(e);
		}
    }
    
    public IAppRole getLoggedInRole()
	{
    	return this.sessData.role.get();
	}

    public void addAlert(Alert alert)
	{
    	this.sessData.addAlert(alert);    	
    }
    public void removeAlert(Alert alert)
	{
    	this.sessData.removeAlert(alert);
    }
    public void removeAlertsByType(Class typeOfAlert)
	{
    	this.sessData.removeAlertsByType(typeOfAlert);
    }
    public void clearAlertsByType(Class typeOfAlert)
	{
    	this.sessData.clearAlertsByType(typeOfAlert);
    }
    public void clearAlerts()
	{
    	this.sessData.clearAlerts();
    } 
	public void unRegisterImage(Image image) 
	{
		Map images = this.sessData.configurationModule.get().getRegisteredImages();
		images.remove(new Integer(image.getImageId()));		
	}

    @SuppressWarnings("unchecked")
	public Image registerImage(Image image)
	{
    	Map images = this.sessData.configurationModule.get().getRegisteredImages();
		images.put(new Integer(image.getImageId()), image);
		return image;
    }
      
	public Image getRegisteredImage(int id)
	{
		Map images = this.sessData.configurationModule.get().getRegisteredImages();
		Image image = (Image)images.get(new Integer(id));
		return image;
	}
	
	public Image[] getRegisteredImages()
	{
		Map images = this.sessData.configurationModule.get().getRegisteredImages();
		Image[] regImages = new Image[images.size()];
		Collection c = images.values();
		Iterator iter = c.iterator();
		int i = 0;
		while (iter.hasNext())
		{
			regImages[i] = (Image)iter.next();
			i++;			
		}
		return regImages;
		
	}
	public Image[] getActiveRegisteredImages()
	{
		Map images = this.sessData.configurationModule.get().getRegisteredImages();
		Collection c = images.values();
		Iterator iter = c.iterator();
		List<Image> list = new ArrayList<Image>();
		
		while (iter.hasNext())
		{
			if(((Image)iter.next()).isActive())
				list.add((Image)iter.next());
		}
		
		Image[] result = new Image[list.size()];
		
		for(int x = 0; x < list.size(); x++)
		{
			result[x] = list.get(x);
		}
		
		return result;
	}


	public void showErrors(String[] value)
	{
		showErrors("Error", value, false);
	}
	public void showErrors(String title, String[] value)
	{
		showErrors(title, value, false);
	}
	@SuppressWarnings("unchecked")
	public void showErrors(String title, String[] value, boolean allowDuplicates)
	{
		if(value == null || value.length == 0)
			return;
		if(title == null)
			title = "Error";
		else
			title = title.replace('\"', '\'');
		
		this.sessData.errorsTitle.set(title);
		ArrayList script = this.sessData.errors.get();
		for(int x = 0; x < value.length; x++)
		{
			if(value[x] != null)
			{
	            String valueToAdd = value[x].replace('\"', '\'');
	            if(allowDuplicates || script.indexOf(valueToAdd) < 0)
	            	script.add(valueToAdd);
			}
		}		
	}
	
	public IAppForm[] getRegisteredForms() 
	{
		Map forms = this.sessData.configurationModule.get().getRegisteredForms();
		ArrayList<IAppForm> retList = new ArrayList<IAppForm>();
		Iterator iter = forms.values().iterator();
		while (iter.hasNext())
		{
			IAppForm form = (IAppForm)iter.next();
			if (form.getFormId() < 1000000)
			{
				retList.add(form);
			}
		}
		IAppForm[] ret = new IAppForm[retList.size()];
		retList.toArray(ret);
		return ret;
	}

	
	public String getTheme() 
	{
		IAppUser user = this.sessData.user.get();
		return user.getTheme();
	}

	public IAppForm getRegisteredForm(FormName formName) 
	{
		Map forms = this.sessData.configurationModule.get().getRegisteredForms();
		IAppForm registeredForm = (IAppForm) forms.get(new Integer(formName.getID()));
		return registeredForm;		
	}

	
	private FormBridge getFormInstance(FormName formName) throws Exception
	{
		IAppForm regForm = this.getRegisteredForm(formName);
		if (regForm == null) return null;

		UIFactory uiFactory = new UIFactory(this.request, this.session);		
		Context ctx = new Context(this.session);
		//UIEngine uiEngine = new UIEngine(this.request, this.session);
		
		// Save objects in session, so we can free them later
		this.sessData.context.set(ctx);
		//this.sessData.uiEngine.set(uiEngine);

		ims.framework.Form form = uiFactory.getForm();
		
		// Creating the form info instance
		form.setFormInfo(FormBridgeFlyweightFactory.getInstance().createFormInfo(regForm.getGenFormPackageName().concat(".FormInfo"), formName.getID()));
		
		// NOTE: Setting the last parameter to "true" when calling createForm 
		// will force the form to SKIP context validation.
		
		FormBridge genForm = (FormBridge)FormBridgeFlyweightFactory.getInstance().createForm(regForm.getGenFormPackageName().concat(".GenForm"), new FormLoader(this.request, this.session), form, regForm, uiFactory, ctx, true, 0, null, null, 0);
		return genForm;
	}

	public boolean canFormProvideData(FormName formName, IReportSeed[] iSeeds) 
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			boolean canProvide = gen.canProvideData(iSeeds);			
			//freeFormInstance(gen);
			return canProvide;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public boolean formHasData(FormName formName, IReportSeed[] iSeeds)
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			boolean hasData = gen.hasData(iSeeds);			
			//freeFormInstance(gen);
			return hasData;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	public IReportField[] getFormData(FormName formName, IReportSeed[] iSeeds)
	{
		return getFormData(formName, iSeeds, false);
	}
	public IReportField[] getFormData(FormName formName, IReportSeed[] iSeeds, boolean excludeNulls)
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			IReportField[] data = gen.getData(iSeeds, excludeNulls);			
			//freeFormInstance(gen);
			return data;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}		
	}
	
	
	
	public void openEMailClient(String emailAddress)
	{
		if(emailAddress != null && emailAddress.length() > 0)
           this.sessData.emailAddressToOpen.set(emailAddress);
	}
	public boolean isValidUrl(String url)
	{
		if(url == null || url.trim().length() == 0 || url.toLowerCase().equals("http://") || url.equals(";") || url.equals("."))
			return false;
		return true;
	}
	public void openUrl(String url)
    {
		openUrl(url, null, null);
    }
	public void openUrl(String url, String target)
    {
		openUrl(url, null, target);
    }
	public void openUrl(String url, List<UrlParam> params)
	{
		openUrl(url, params, null);
	}
	public void openUrl(String url, List<UrlParam> params, String target)
    {
		if(isValidUrl(url))
        {
            ArrayList<Url> urls = this.sessData.urlToOpen.get();
            if(urls == null)
                urls = new ArrayList<Url>();
            urls.add(new Url(url, params, -1, null, target, false));
            
            this.sessData.urlToOpen.set(urls);
        }
    }
	public int openCustomUrl(String url, List<WindowParam> windowParams, boolean handleWindowEvents)
    {
		if(isValidUrl(url))
        {
            ArrayList<Url> urls = this.sessData.customUrlToOpen.get();
            if(urls == null)
                urls = new ArrayList<Url>();
            
            int currentID = this.sessData.currentCustomUrlToOpenID.get().intValue();  
            currentID ++;
            
            urls.add(new Url(url, null, currentID, windowParams, null, handleWindowEvents));
            
            this.sessData.currentCustomUrlToOpenID.set(Integer.valueOf(currentID));
            this.sessData.customUrlToOpen.set(urls);
            
            return currentID;
        }
		
		return -1;
    }
	
	public int openCustomUrlCloseableOnContextChange(String url, List<WindowParam> windowParams,  boolean handleWindowEvents)
	{
		if(isValidUrl(url))
        {
            ArrayList<Url> urls = this.sessData.customUrlToOpen.get();
            if(urls == null)
                urls = new ArrayList<Url>();
            
            int currentID = this.sessData.currentCustomUrlToOpenID.get().intValue();  
            currentID ++;
            
            urls.add(new Url(url, null, currentID, windowParams, null, handleWindowEvents));
            
            this.sessData.currentCustomUrlToOpenID.set(Integer.valueOf(currentID));
            this.sessData.customUrlToOpen.set(urls);
            
            ArrayList<Integer> urlsID = this.sessData.urlToCloseOnContextChanged.get();
            if(urlsID == null)
            	urlsID = new ArrayList<Integer>();
              
            urlsID.add(Integer.valueOf(currentID));
            this.sessData.urlToCloseOnContextChanged.set(urlsID);
                        
            return currentID;
        }
		
		return -1;
	}
	
	public void closeCustomUrl(int id)
    {
		ArrayList<Integer> urlsID = this.sessData.urlToClose.get();
        if(urlsID == null)
        	urlsID = new ArrayList<Integer>();
          
        urlsID.add(Integer.valueOf(id));
        this.sessData.urlToClose.set(urlsID);
    }	
	
	public void setUploadUrl(String url) 
	{
    	this.sessData.uploadUrl.set(url);
	}
	public String getUploadUrl() 
	{
		return this.sessData.uploadUrl.get();
	}
	public void setUploadDebug(boolean value)
	{
		this.sessData.uploadDebug.set(new Boolean(value));
	}

    public void populate(DynamicGridCell cell, IItemCollection values)
    {
        populate(cell, values, true);
    }
    public void populate(DynamicGridCell cell, IItemCollection values, boolean useEnhancedOptionsIfAvailable) 
    {
        if(cell == null)
            throw new RuntimeException("Invalid dynamic grid cell");
        if(!cell.getType().isSupportingItems())
            throw new RuntimeException("The specified dynamic grid cell does not support items");
        
        cell.getItems().clear();        
        if(values == null)
            return;
        
        int valuesCount = values.getItems().length;
        for(int x = 0; x < valuesCount; x++)
        {
            IItem item = values.getItems()[x];
            
            if(item != null)
            {
                if(useEnhancedOptionsIfAvailable && item instanceof IEnhancedItem)
                    cell.getItems().newItem(item, ((IEnhancedItem)item).getIItemImage(), ((IEnhancedItem)item).getIItemTextColor());
                else
                    cell.getItems().newItem(item);
            }
        }
    }

	public BuildInfo getBuildInfo() 
	{
		return Configuration.getBuildInfo();
	}

	public ImageInfo getImageInfo(Image img)
	{
		String imagePath = getImageRealPath(img);
		return ImagePath.getImageInfo(imagePath); 
	}

	public byte[] getImageContent(Image img)
	{
		String imagePath = getImageRealPath(img);
		return ImagePath.getImageContent(imagePath);
	}
	
	private String getImageRealPath(Image img)
	{
		String imagePath;
		if (img.getImageId() > 0) //IMS delivered Image
		{
			ServletContext ctx = request.getSession().getServletContext();
			imagePath = ctx.getRealPath(img.getImagePath());			
		}
		else //Site uploaded Image
		{
			String root = ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue();
			if (!root.endsWith("/") && !root.endsWith("\\"))
			{
				root += "/";
			}
			String name = img.getImagePath();
			if (name.startsWith("/") || name.startsWith("\\"))
			{
				name = name.substring(1);
			}
			imagePath = root + name;
		}
		return imagePath;
	}

	public Image saveImage(Image img, byte[] content) throws Exception
	{
		String path = ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue();
		if (path == "") throw new DomainRuntimeException("A value must be supplied for Config Flag IMAGE_FOLDER.");
		
		if (!path.endsWith("/") && !path.endsWith("\\"))
		{
			path += "/";
		}
		String name = img.getImagePath();
		if (name.startsWith("/") || name.startsWith("\\"))
		{
			name = name.substring(1);
		}
		try 
		{
			File f = new File(path + name);
			if (f.exists())
			{
				throw new Exception("File " + f.getAbsolutePath() + " already exists. Cannot overwrite.");
			}
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(content);
			fo.close();
			this.registerImage(img);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}		
		return img;
	}

	public boolean supportsRecordedInError(FormName formName) 
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			boolean supportsData = gen.supportsRecordedInError();			
			//freeFormInstance(gen);
			return supportsData;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	public boolean hasRecordedInErrorData(FormName formName)
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			return gen.getRecordedInErrorVo() != null;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	public ValueObject getRecordedInErrorVo(FormName formName)
	{
		try
		{
			FormBridge gen = getFormInstance(formName);
			ValueObject voData = gen.getRecordedInErrorVo();			
			//freeFormInstance(gen);
			return voData;
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	public ILocation[] getStoredLocations() 
	{
		if(getLocationProvider() == null || this.sessData.storedLocations.get() == null)
			return new ILocation[] { };
		
		return this.sessData.storedLocations.get();
	}
	public void setStoredLocations(ILocation[] locations)
	{		
		setStoredLocations(locations, true);
	}
	public void setStoredLocations(ILocation[] locations, boolean setDirtyFlagIfLocationsDiffer) 
	{
		if(getLocationProvider() == null)
			return;
			
		if(!setDirtyFlagIfLocationsDiffer || isDirty(this.sessData.storedLocations.get(), locations))
		{
			if(locations != null)
			{
				// filter the duplicate locations 
				// and reduce the location class size by converting the instances
				// to a small Location class that implements ILocation
				
				ArrayList<Location> newLocations = new ArrayList<Location>();
				for(int x = 0; x < locations.length; x++)
				{
					Location newLocation = new Location(locations[x]);
					if(!newLocations.contains(newLocation))
						newLocations.add(newLocation);
				}
				
				locations = new Location[newLocations.size()];
				for(int x = 0; x < newLocations.size(); x++)
				{
					locations[x] = newLocations.get(x);
				}
			}
			
			this.sessData.storedLocations.set(locations);
			if(setDirtyFlagIfLocationsDiffer)
				this.sessData.sendStoredLocations.set(Boolean.TRUE);
		}
	}
	public ILocation getCurrentLocation()
	{
		return sessData.selectedLocation.get();
	}
	public void setCurrentLocation(ILocation location)
	{
		if(location == null)
			throw new CodingRuntimeException("Invalid location");
		
		sessData.selectedLocation.set(location);
	
		ILocation[] locations = getStoredLocations();
		ILocation[] newLocations = new ILocation[locations.length + 1];
		newLocations[0] = location;
		
		for(int x = 0; x < locations.length; x++)
		{
			newLocations[x + 1] = locations[x];
		}
		
		setStoredLocations(newLocations, true);
	}
	
	public ILocationProvider getLocationProvider()
	{
		LocationFactory locationFactory = new LocationFactory(ims.domain.DomainSession.getSession(session));
		if(locationFactory.hasLocationProvider())
			return locationFactory.getLocationProvider();
		return null;
	}
	public INotificationsProvider getNotificationsProvider()
	{
		NotificationsFactory notificationsFactory = new NotificationsFactory(ims.domain.DomainSession.getSession(session));
		if(notificationsFactory.hasNotificationsProvider())
			return notificationsFactory.getNotificationsProvider();
		return null;
	}
	public ISecurityTokenProvider getSecurityTokenProvider()
	{
		SecurityTokenFactory securityTokenFactory = new SecurityTokenFactory(ims.domain.DomainSession.getSession(session));
		if(securityTokenFactory.hasSecurityTokenProvider())
			return securityTokenFactory.getSecurityTokenProvider();
		return null;
	}	
	public IAddressProvider getAddressProvider()
	{
		AddressFactory addressFactory = new AddressFactory(ims.domain.DomainSession.getSession(session));
		if(addressFactory.hasAddressProvider())
			return addressFactory.getProvider();
		return null;
	}
	public ISystemLogProvider getSystemLogProvider()
	{
		SystemLogFactory systemLogFactory = new SystemLogFactory(ims.domain.DomainSession.getSession(session));
		if(systemLogFactory.hasSystemLogProvider())
			return systemLogFactory.getProvider();
		return null;
	}
	public IUserProvider getUserProvider()
	{
		UserFactory userFactory = new UserFactory(ims.domain.DomainSession.getSession(session));
		if(userFactory.hasUserProvider())
			return userFactory.getUserProvider();
		return null;
	}
	public IUploadDownloadUrlProvider getUploadDownloadUrlProvider()
	{
		UploadDownloadUrlProviderFactory uplFactory = new UploadDownloadUrlProviderFactory(ims.domain.DomainSession.getSession(session));
		if(uplFactory.hasProvider())
			return uplFactory.getProvider();
		return null;
	}
	private boolean isDirty(ILocation[] oldLocations, ILocation[] newLocations) 
	{
		if(oldLocations == null && newLocations == null)
			return false;
		if(oldLocations == null && newLocations != null)
			return true;
		if(oldLocations != null && newLocations == null)
			return true;		
		if(oldLocations.length != newLocations.length)
			return true;
		
		for(int x = 0; x < oldLocations.length; x++)
		{
			if(oldLocations[x].getID() != newLocations[x].getID())
				return true;
			if(!oldLocations[x].getName().equals(newLocations[x].getName()))
				return true;
		}
		
		return false;
	}

	public boolean hasRight(AppRight right)
	{
		return sessData.role.get().hasRight(right);
	}
	
	public static IAppForm getForm(SessionData sessData, Integer formID)
	{
		Map map = sessData.configurationModule.get().getRegisteredForms();
		IAppForm registeredForm = (IAppForm) map.get(formID);
		
		if(registeredForm == null)
			throw new RuntimeException("Form not registered: " + formID);
		
		return registeredForm;
	}
	public TopButtonCollection getBuiltInTopButtons()
	{
		return TopButton.getBuiltInTopButtons();
	}
	private FormName getStartUpForm()
    {
    	class FormName extends ims.framework.FormName
    	{
            private static final long serialVersionUID = 1L;
    		FormName(int id, String name)
    		{
    			super(id, name);
    		}
    	}
    	
    	IAppForm startUpForm = this.sessData.currentDynamicNavigation.get().getNavigation().getStartupForm();
    	return new FormName(startUpForm.getFormId(), startUpForm.getCaption());
    }

	public ClassConfig getClassConfig()
	{
		return this.sessData.configurationModule.get().getClassConfig();
	}
	@Override
	public UILayoutState getCurrentLayoutState()
	{
		return sessData.currentUILayoutState.get();
	}
	@Override
	public void setCurrentLayoutState(UILayoutState layoutState)
	{
		if(sessData.currentUILayoutState.get() != null && !sessData.currentUILayoutState.get().equals(layoutState))
		{
			sessData.uiLayoutChanged.set(true);
		}
		
		sessData.currentUILayoutState.set(layoutState);		
	}
	@Override
	public boolean navigationIsCollapsed()
	{
		return sessData.navigationCollapsed.get() == null ? false : sessData.navigationCollapsed.get(); 
	}	
	@Override
	public void setNavigationCollapsed(boolean value)
	{
		boolean collapsed = sessData.navigationCollapsed.get();
		if(value != collapsed)
		{
			DynamicNavigation dynamicNavigation = (DynamicNavigation)sessData.currentDynamicNavigation.get();		
			dynamicNavigation.markChanged();
			sessData.navigationCollapsed.set(value);
		}
	}

	@Override
	public List<ContextDescriptor> getGlobalContextVariablesInfo()
	{
		return getContextVariablesInfo(true, false);
	}
	@Override
	public List<ContextDescriptor> getPersistentGlobalContextVariablesInfo()
	{
		return getContextVariablesInfo(true, true);
	}
	@Override
	public List<ContextDescriptor> getLocalContextVariablesInfo()
	{
		return getContextVariablesInfo(false, false);
	}	
	private List<ContextDescriptor> getContextVariablesInfo(boolean global, boolean persistent)
	{
		List<ContextDescriptor> result = new ArrayList<ContextDescriptor>();
		
		ims.framework.Context context = sessData.context.get();
		
		String[] keys = global ? (persistent ? context.getPersistentGlobalVariablesNames() : context.getGlobalVariablesNames()) : context.getAllLocalVariablesNames();
		
		for(int x = 0; x < keys.length; x++)
		{
			String key = keys[x];
			Object instance = context.get(key);		
			String type = instance == null ? "" : instance.getClass().toString();				
			String value = instance == null ? "" : instance.toString();
			double size = instance == null ? 0 : ObjectInstanceSize.getSize(instance);
			
			result.add(new ContextDescriptor(key, type, value, size));
		}
		
		return result;
	}
	public void addPatientSelectionToHistory(ISelectedPatient patient)
	{	
		if(patient.getISelectedPatientID() == null || patient.getISelectedPatientName() == null)
			throw new RuntimeException("Invalid patient id / name to be stored in selection history");
		
		ArrayList<ISelectedPatient> list = sessData.previouslySelectedPatients.get();
		if(list == null)
			list = new ArrayList<ISelectedPatient>();
				 
		for (ISelectedPatient existingPatient : list)
		{
			if(existingPatient.getISelectedPatientID().equals(patient.getISelectedPatientID()))
			{
				list.remove(existingPatient);
				break;
			}
		}
		
		list.add(0, new SelectedPatient(patient.getISelectedPatientID(), patient.getISelectedPatientName(), patient.getISelectedPatientInterfaceID(), patient.getISelectedPatientInterfaceIDType()));
		
		if(list.size() > TopButtonSection.MAX_PATIENTS_SECTION_ITEMS)
		{
			ArrayList<ISelectedPatient> newList = new ArrayList<ISelectedPatient>();
			for(int x = 0; x < TopButtonSection.MAX_PATIENTS_SECTION_ITEMS; x++)				
			{
				newList.add(x, list.get(x));
			}
			sessData.previouslySelectedPatients.set(newList);
		}
		else
		{
			sessData.previouslySelectedPatients.set(list);	
		}		
	}
	public void clearPatientSelectionHistory()
	{		
		sessData.previouslySelectedPatients.clear();
	}
	
	private class SelectedPatient implements ISelectedPatient
	{
		private static final long	serialVersionUID	= 1L;
		
		private Integer id;
		private String name;
		private String interfaceID;
		private Integer interfaceIDType;
		
		public SelectedPatient(Integer id, String name, String interfaceID, Integer interfaceIDType)
		{
			this.id = id;
			this.name = name;
			this.interfaceID = interfaceID;
			this.interfaceIDType = interfaceIDType;
		}

		public Integer getISelectedPatientID()
		{
			return id;
		}

		public String getISelectedPatientName()
		{
			return name;
		}
		public void setISelectedPatientName(String value)
		{
			name = value;			
		}
		public String getISelectedPatientInterfaceID()
		{
			return interfaceID;
		}
		public Integer getISelectedPatientInterfaceIDType()
		{
			return interfaceIDType;
		}		
	}
	private int getNextMessageBoxIdentifier()
	{
		int next = 0;
		Integer nextMessageBoxIdentifier = this.sessData.nextMessageBoxIdentifier.get();
		if(nextMessageBoxIdentifier != null)
			next = nextMessageBoxIdentifier;
		
		next++;
		this.sessData.nextMessageBoxIdentifier.set(new Integer(next));
		
		return next;
	}
	@Override
	public boolean isRIEMode()
	{
		if(sessData.listRIERecordsOnly.get() == null)
			return false;
		
		return sessData.listRIERecordsOnly.get().booleanValue();
	}

	@Override
	public ISpellChecker getSpellChecker()
	{
		DictionaryFactory dictionaryFactory = new DictionaryFactory(ims.domain.DomainSession.getSession(session));
		if(!dictionaryFactory.hasDictionaryProvider())
			return null;
		
		return SpellCheckerFactory.getInstance(dictionaryFactory.getProvider());		
	}
	@Override
	public void notifyExternalApplication(String id)
	{
		if(id == null || id.trim().length() == 0)
			throw new CodingRuntimeException("Invalid notification ID");
		
		ArrayList<String> list = sessData.externalNotifications.get();
		if(list == null)
			list = new ArrayList<String>();
		if(!list.contains(id))
		{
			list.add(id);		
			sessData.externalNotifications.set(list);
		}
	}
	@Override
	public String getRequestUrl()
	{
		return request.getRequestURL().toString().replace("/CNHost", "");
	}	
	public ISystemLog createSystemLogEntry(SystemLogType type, SystemLogLevel level, String message)
	{
		ISystemLogProvider provider = getSystemLogProvider();
		if(provider == null)
			return null;
		
		IAppUser user = getLoggedInUser();
		String computer = "";
		String username = "";		
		
		if(user != null)
		{
			username = user.getUsername();
			computer = user.getHostName();			
		}
		
		return createSystemLogEntry(type, level, username, computer, message);
	}	
	public ISystemLog createSystemLogEntry(SystemLogType type, SystemLogLevel level, String username, String computer, String message)
	{
		ISystemLogProvider provider = getSystemLogProvider();
		if(provider == null)
			return null;		
		
		String source = "";
		BuildInfo buildInfo = Configuration.getBuildInfo();
		
		if(buildInfo != null)
		{
			source = buildInfo.getName();
			source += " version ";
			source += buildInfo.getAppVersion() + " (" + buildInfo.getAppTimestamp() + ")";
		}
		
		String userAgent = request.getHeader("User-Agent");				
		
		return provider.save(new DateTime(), type, level, ConfigFlag.HOST_NAME.getValue(), username, source, computer, userAgent, getSessionId(), message);
	}
	public void deleteFile(String fileName)
	{
		if(fileName == null || fileName.trim().length() == 0)
			throw new CodingRuntimeException("Invalid file name");
		
		if(this.sessData.filesToDelete.get() == null)
		{
			this.sessData.filesToDelete.set(new ArrayList<String>());
		}
		
		fileName = fileName.replace("\\", "\\\\");
		
		if(!this.sessData.filesToDelete.get().contains(fileName))
		{
			this.sessData.filesToDelete.get().add(fileName);
		}
	}
	public void runExternalApplication(String filePath)
	{
		runExternalApplication(filePath, false, true, null, true, false);
	}
	public void runExternalApplication(String filePath, boolean fixFilePath)
	{
		runExternalApplication(filePath, false, true, null, fixFilePath, false);
	}
	public void runExternalApplication(String filePath, boolean autoRunEditor, boolean fixFilePath)
	{
		runExternalApplication(filePath, autoRunEditor, true, null, fixFilePath, false);
	}
	public void runExternalApplication(String filePath, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning)
	{
		runExternalApplication(filePath, false, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning, true, false);
	}
	public void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning)
	{
		runExternalApplication(filePath, autoRunEditor, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning, true, false);
	}
	public void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, boolean checkForRunningApplicationPerUserSession, String messageToBeDisplayedIfAlreadyRunning)
	{
		runExternalApplication(filePath, autoRunEditor, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning, true, checkForRunningApplicationPerUserSession);
	}
	public void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning, boolean fixFilePath)
	{
		runExternalApplication(filePath, autoRunEditor, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning, fixFilePath, false);
	}
	public void runExternalApplication(String filePath, boolean autoRunEditor, boolean allowMultipleInstances, String messageToBeDisplayedIfAlreadyRunning, boolean fixFilePath, boolean checkForRunningApplicationPerUserSession)
	{
		if(filePath == null || filePath.trim().length() == 0)
			throw new CodingRuntimeException("Invalid file path");
		
		if(this.sessData.externalApplicationToRun.get() == null)
		{
			this.sessData.externalApplicationToRun.set(new ArrayList<ExternalApplication>());
		}
		if(fixFilePath)
		{
			filePath = filePath.replace("\\", "\\\\");
		}
		
		this.sessData.externalApplicationToRun.get().add(new ExternalApplication(filePath, autoRunEditor, allowMultipleInstances, messageToBeDisplayedIfAlreadyRunning, checkForRunningApplicationPerUserSession));		
	}
	public void checkPasswordRequirements(String newPassword, String currentPassword, String[] previousPasswords) throws EngineException
	{
		if(newPassword == null)
			throw new EngineException("Password cannot be null.");
		
		int minLen = ConfigFlag.FW.PASSWD_MIN_LEN.getValue();
		if (minLen > 0 && newPassword.length() < minLen)
		{
			throw new EngineException("Password minimum length is " + minLen + ".");
		}
		
		// WDEV-15903
		int maxLen = ConfigFlag.FW.PASSWD_MAX_LEN.getValue();
		if(maxLen > 0 && newPassword.length() > maxLen)
		{
			throw new EngineException("Password Maximum length is " + maxLen + ".");
		}
		
		
		if(currentPassword != null && currentPassword.equals(newPassword))
		{
			throw new EngineException("The new password is the same as the current password.");
		}
		int reuseCount = ConfigFlag.FW.PASSWD_REUSE.getValue();
		if(reuseCount > 0 && previousPasswords != null)
		{
			int start = previousPasswords.length - 1;			
			int length = start - reuseCount;
			if(length < 0)
				length = 0;
			
			for(int x = start; x >= length; x--)
			{
				if(newPassword.equals(previousPasswords[x]))
				{
					throw new EngineException("Reusing the last " + reuseCount + " previous password" + (reuseCount == 1 ? "" : "s") + " is not allowed.");
				}
			}
		}
		
		boolean hasNonAlpha = false;
		for(int x = 0; x < newPassword.length(); x++)
		{
			if(!Character.isLetter(newPassword.charAt(x)))
			{
				hasNonAlpha = true;
				break;
			}
		}
		
		if(!hasNonAlpha)
			throw new EngineException("Password must contain at least one non-alpha character.");
	}

	@Override
	public IExternalEncodingProvider getExternalEncodingProvider()
	{
		ExternalEncodingProviderFactory factory = new ExternalEncodingProviderFactory();
		if(factory.hasProvider())
			return factory.getProvider();
		return null;
	}
	
	@Override
	public IPrintersProvider getPrinterProvider()
	{
		PrintersFactory factory = new PrintersFactory(ims.domain.DomainSession.getSession(session));
		if(factory.hasPrintersProvider())
			return factory.getProvider();
		return null;
	}
	
	@Override
	public IScheduledJobsProvider getScheduledJobsProvider()
	{
		ScheduledJobsFactory factory = new ScheduledJobsFactory(ims.domain.DomainSession.getSession(session));
		if(factory.hasScheduledJobsProvider())
			return factory.getScheduledJobsProvider();
		return null;
	}
	
	public boolean reloadAllBusinessRules()
	{
		try 
		{
			RulesEngine rulesEngine = ims.rules.engine.RulesEngineFactory.getInstance();
			if(rulesEngine == null)
				return false;
			
			rulesEngine.reloadAll();			
		} 
		catch (RulesEngineException e) 
		{
			e.printStackTrace();
			return false;
		} 
		catch (RulesEngineCompilationException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public String getCurrentConfigFlagsGroup()
	{
		IConfigFlagsProvider provider = new ConfigFlagsFactory().getProvider();
		if(provider == null)
			return "Default";
		
		return new ConfigFlagsFactory().getProvider().getActiveGroup();
	}
	
	public LocalSettings getLocalSettings()
	{
		return new ims.framework.cn.LocalSettings(session, sessData);
	}
	
	public IScreenHint getScreenHint(String hintId)
	{
		IScreenHintProvider provider = new ScreenHintFactory(sessData.domainSession.get()).getProvider();
		if(provider == null)
			return null;
		
		return provider.getHint(hintId);
	}

	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor) throws IOException 
	{
		return convertPdfToImages(inputBuffer, outputType, dpi, scalingFactor, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi) throws IOException 
	{
		return convertPdfToImages(inputBuffer, outputType, dpi, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType) throws IOException 
	{
		return convertPdfToImages(inputBuffer, outputType, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer) throws IOException 
	{
		return convertPdfToImages(inputBuffer, ImageType.JPG, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertPdfToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor, int maxDimension) throws IOException 
	{
		return new PDF().convertToImages(inputBuffer, outputType, dpi, scalingFactor, maxDimension);
	}
	@Override
	public void copyToClipboard(String text) 
	{	
		if (text != null && text.trim().length() > 0)
		{
			StringBuilder content = this.sessData.copyToClipboard.get();
			if (content == null)
				content = new StringBuilder();
			content.append(text);
			this.sessData.copyToClipboard.set(content);
		}
		else
			this.sessData.clearClipboard.set(Boolean.valueOf(true));
			
	}
		
	public void clearClipboard()
	{
		this.sessData.clearClipboard.set(Boolean.valueOf(true));
	}	
	
	public WebServiceData getWebServiceData()
	{
		return new WebServiceData(this.session);		
	}
	@Override
	public void writeMedicodeInputFile(String content) 
	{
		String encodedContent = ims.framework.utils.Base64.encodeBytes(content.getBytes());
		this.sessData.medicodeInputFile.set(encodedContent);
	}
	
	public String getSAMLXmlContent()
	{
		return this.sessData.samlXml.get();
	}
	public boolean allowPdsInteraction()
	{
		return (this.sessData.samlXml.get() != null && this.sessData.samlXml.get().trim().length() > 0) ? true : false;
	}
	@Override
	public boolean openDashboardCloseableOnContextChange(String url, List<UrlParam> params)
	{
		return internalOpenDashboard(url, params, OpenMode.CLOSEABLE_ON_CONTEXT_CHANGE);
	}
	@Override
	public boolean openDashboard(String url, List<UrlParam> params)
	{
		return internalOpenDashboard(url, params, OpenMode.CUSTOM_URL);	
	}
	
	private boolean internalOpenDashboard(String dashboardUrl, List<UrlParam> dashboardParams, OpenMode openMode)
	{
		boolean ret = false;
		String url = dashboardUrl;
		boolean useSecureKey = true;
		
		IAppUser appUser = sessData.user.get();
		
		if(url.indexOf("?sk=0") != -1 || url.indexOf("&sk=0") != -1)
		{
			//sk=0 means without Logi SecureKey authentication
			useSecureKey = false;
		}
		
		url = url.replaceAll("[&?]sk.*?(?=&|\\?|$)", ""); //regex to remove the query param "sk" and its value
        int indexAmp = url.indexOf('&');

        if(indexAmp > -1)
        {
            int indexQuestion = url.indexOf('?');

            if(indexQuestion == -1)
            {
                char[] cc = url.toCharArray();
                cc[indexAmp] = '?';
                url = new String(cc);
            }
        }
		
		if(url.endsWith("/"))
			url = url.substring(0, url.length() - 1);
		
		if(appUser == null)
		{
			showMessage("Could not get the domain user.");
			return false;
		}

		if(appUser.getUsername() == null || appUser.getUsername().length() == 0)
		{
			showMessage("Could not get the domain user name.");
			return false;
		}

		if(useSecureKey)
		{
    		//we pass "0.0.0.0" as clientIPAddress because MAXIMS server could be behind a proxy/load balancer
			String sKey = requestSecureKey("0.0.0.0", appUser.getUsername(), url);
    		if(sKey == null || sKey.length() == 0)
    		{
    			showMessage("Could not get a Secure key from Logi server.");
    			return false;
    		}
    		
    		if(url.indexOf('?') == -1)
    		{
    			url += "?";
    		}
    		else
    		{
    			url += "&";
    		}
    		
    		url += "rdSecureKey=" + sKey;
		}
		
		if(dashboardParams != null)
		{
    		for(int i = 0; i < dashboardParams.size(); i++)
    		{
        		if(url.indexOf('?') == -1)
        		{
        			url += "?";
        		}
        		else
        		{
        			url += "&";
        		}
    
    			try
    			{
    				url += dashboardParams.get(i).getKey() + "=" + URLEncoder.encode(dashboardParams.get(i).getValue(), "UTF-8");
    			}
    			catch (UnsupportedEncodingException e)
    			{
    				e.printStackTrace();
    				showMessage(e.toString());
    				return false;
    			}
    		}
		}
		
		List<WindowParam> params = new ArrayList<WindowParam>(); 
		params.add(new WindowParam("FullScreen","false")); 
		params.add(new WindowParam("ToolBar","false")); 
		params.add(new WindowParam("StatusBar","false")); 
		params.add(new WindowParam("StatusBar","false")); 
		params.add(new WindowParam("MenuBar","false")); 
		params.add(new WindowParam("AddressBar","false")); 
		params.add(new WindowParam("Resizable","true")); 
		params.add(new WindowParam("Visible","true"));
		//params.add(new WindowParam("Width","1440")); 
		//params.add(new WindowParam("Height","1000")); 

		if(OpenMode.CLOSEABLE_ON_CONTEXT_CHANGE.equals(openMode))
			openCustomUrlCloseableOnContextChange(url, params, true);
		else if(OpenMode.CUSTOM_URL.equals(openMode))
			openCustomUrl(url, params, true);
		
		return ret;
	}
	
	public String requestSecureKey(String clientIP, String userName, String appURL)
	{
		String result = "XXX";
		String securityKeyFileLocation = "/rdTemplate/rdGetSecureKey.aspx";
		final int TIMEOUT = 1000 * 60 * 15;
		final int MAX_BUFFER_LIMIT_NO_WARNING = 1024 * 1024;
		HttpClient client;
		

		client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT);
		client.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		client.getParams().setIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, MAX_BUFFER_LIMIT_NO_WARNING);
		
		PostMethod post = new PostMethod(appURL + securityKeyFileLocation);

		NameValuePair[] data = { new NameValuePair("Username", userName), new NameValuePair("ClientBrowserAddress", clientIP) };
		post.setRequestBody(data);

		int iGetResultCode;

		try
		{

			iGetResultCode = client.executeMethod(post);

			if (iGetResultCode == HttpStatus.SC_OK)
			{
				result = new String(getResponseAsByteArray(post));
			}
			else
			{
				showMessage("The requestSecureKey call returned the error: " + iGetResultCode);
				//log.error("The error message was : '" + post.getResponseBodyAsString() + "'");
			}

		}
		catch (HttpException e)
		{
			showMessage("The requestSecureKey call returned the error: " + e.toString());
		}
		catch (IOException e)
		{
			showMessage("The requestSecureKey call returned the error: " + e.toString());
		}
		finally
		{
			post.releaseConnection();
		}

		return result;
	}

	private byte[] getResponseAsByteArray(PostMethod post) throws IOException
	{
		InputStream inStream = post.getResponseBodyAsStream();
		if (inStream != null)
		{
			long contentLength = post.getResponseContentLength();
			if (contentLength > Integer.MAX_VALUE)
			{
				throw new IOException("Content too large to be buffered: " + contentLength + " bytes");
			}

			ByteArrayOutputStream outstream = new ByteArrayOutputStream(contentLength > 0 ? (int) contentLength : 4 * 1024);
			byte[] buffer = new byte[4096];
			int len;
			while ((len = inStream.read(buffer)) > 0)
			{
				outstream.write(buffer, 0, len);
			}
			outstream.close();

			return outstream.toByteArray();
		}
		else
		{
			return null;
		}
	}
	
}
