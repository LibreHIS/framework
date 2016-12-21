package ims.framework.cn;

import ims.configuration.InitConfig;
import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.FormAccessLevel;
import ims.framework.TopButtonCollection;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.ITopButton;
import ims.framework.utils.Image;
import ims.framework.utils.ImagePath;
import ims.framework.utils.StringUtils;

public class TopButton extends ims.framework.TopButton
{
	private static final long serialVersionUID = 1L;
	
	private String action = null;
	private String emptyPatientSelection = "Please enter Patient ID or Surname and/or Forename";
	
	public static TopButtonCollection getBuiltInTopButtons()
	{
		TopButtonCollection items = new TopButtonCollection();
		
		items.add(HOME);
		items.add(PATIENT_SEARCH);
		items.add(SELECT_ROLE);
		items.add(LOGOUT);
		items.add(PATIENT_SUMMARY);
		items.add(ORDER_ENTRY);
		items.add(PRINT);
		items.add(RECORDED_IN_ERROR);
		items.add(AUDIT_VIEW);
		items.add(RECORDING_ON_BEHALF_OF);
		items.add(MANAGE_LOCATIONS);
		items.add(PAS_CONTACTS);
		items.add(LOCK_SCREEN);
		items.add(ABOUT);
		items.add(CHANGEPASSWORD);
		items.add(UPLOAD);
		items.add(DOWNLOAD);
		items.add(DASHBOARD);
		
		return items;
	}
	
	public static TopButton HOME = new TopButton(-1, "Home", new ImagePath(-1, "home-2.png"), true, true, null);
	public static TopButton PATIENT_SEARCH = new TopButton(-2, "Patient Search", new ImagePath(-2, "search-2.png"), true, true, null);
	public static TopButton SELECT_ROLE = new TopButton(-3, "Select Role", new ImagePath(-3, "select-role.png"), true, true, null);
	public static TopButton LOGOUT = new TopButton(-4, "Logout", new ImagePath(-4, "arrow-2.png"), true, true, "logout");
	public static TopButton PATIENT_SUMMARY = new TopButton(-5, "Patient Summary", new ImagePath(-5, "clipboard-1.png"), true, true, null);
	public static TopButton ORDER_ENTRY = new TopButton(-6, "Order Entry", new ImagePath(-6, "order-entry.png"), true, true, null);
	public static TopButton PRINT = new TopButton(-7, "Print", new ImagePath(-7, "print-1.png"), true, true, null);
	public static TopButton RECORDED_IN_ERROR = new TopButton(-8, "Recorded in Error", new ImagePath(-8, "tb-warn-1.png"), true, true, null);
	public static TopButton AUDIT_VIEW = new TopButton(-9, "Audit View", new ImagePath(-9, "note-2.png"), true, true, null);
	public static TopButton RECORDING_ON_BEHALF_OF = new TopButton(-10, "Recording on behalf of...", new ImagePath(-10, "keyboard-1.png"), true, true, null);
	public static TopButton MANAGE_LOCATIONS = new TopButton(-11, "Manage Locations", new ImagePath(-11, "manage-locations-1.png"), true, true, null);
	public static TopButton PAS_CONTACTS = new TopButton(-12, "PAS Contacts", new ImagePath(-12, "calendar-tb.png"), true, true, null);
	public static TopButton LOCK_SCREEN = new TopButton(-13, "Lock Screen", new ImagePath(-13, "lock.png"), true, true, null);
	public static TopButton ABOUT = new TopButton(-14, "About", new ImagePath(-14, "question-4.png"), true, true, "about");	
	public static TopButton CHANGEPASSWORD = new TopButton(-15, "Change Password", new ImagePath(-15, "change-password-1.png"), true, true, null);
	public static TopButton UPLOAD = new TopButton(-16, "Upload", new ImagePath(-16, "upload-1.png"), true, true, null);
	public static TopButton DOWNLOAD = new TopButton(-17, "Download", new ImagePath(-17, "download-1.png"), true, true, null);
	public static TopButton DASHBOARD = new TopButton(-18, "LPR", new ImagePath(-18, "note-2.png"), true, true, null);//FWUI-1857
	
	public TopButton(ITopButton button)
	{
		TopButtonCollection builtInTopButtons = TopButton.getBuiltInTopButtons();
		TopButton builtInTopButton = null;
		
		for(int x = 0; x < builtInTopButtons.size(); x++)
		{
			if(builtInTopButtons.get(x).getID() == button.getITopButtonID())
			{
				builtInTopButton = (TopButton)builtInTopButtons.get(x);
				break;
			}
		}
		
		if(builtInTopButton != null)
		{
			id = builtInTopButton.id;
			text = builtInTopButton.text;
			action = builtInTopButton.action;
			enabled = true;
			visible = true;
			image = builtInTopButton.image;
			builtIn = true;
			alwaysEnabled = builtInTopButton.alwaysEnabled;			
		}
		else
		{
			id = button.getITopButtonID();
			text = button.getITopButtonText();
			enabled = button.getITopButtonEnabled();
			visible = true;
			form = button.getITopButtonForm();
			url = button.getITopButtonUrl();
			builtIn = false;
			alwaysEnabled = button.getITopButtonIsAlwaysEnabled();
			patientMustBeSelected = button.getITopButtonPatientMustBeSelected();
			contextDependent = button.getITopButtonContextDependent();			
			displayMaximiseButton = button.getITopButtonDisplayMaximiseButton();
			displayCloseButton = button.getITopButtonDisplayCloseButton();
			
			if(url != null)
				image = new ImagePath(0, "world.png");			
			else
				image = new ImagePath(0, "top-ar-right.png");
		}
	}
	public TopButton(int id, String text)
	{
		this(id, text, true, true);
	}
	public TopButton(int id, String text, boolean enabled, boolean visible)
	{
		super(id, text, new ImagePath(0, "top-ar-right.png"), enabled, visible);
	}
	private TopButton(int id, String text, Image image, boolean enabled, boolean visible, String action)
	{
		super(id, text, image, enabled, visible);		
		this.action = action;
		builtIn = true;
	}
	
	public void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception
	{
		IAppForm registeredForm = UIEngine.getForm(sessData, sessData.currentFormID.get());
		
		// Print
		if(TopButton.PRINT.id == id)
		{
			Integer printFormID = InitConfig.getPrintFormId();
			if (printFormID == null || printFormID.intValue() <= 0)
			{
				setVisible(false);
			}
			else
			{
				setVisible(true);
			}
		}
		// Recorded in Error
		else if(TopButton.RECORDED_IN_ERROR.id == id)
		{
			Integer rieFormID = InitConfig.getRecordedInErrorFormId();
			if (rieFormID == null || rieFormID.intValue() <= 0)
			{
				setVisible(false);	
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				if (registeredForm.isDialog() || registeredForm.getRieBoClassName() == null || registeredForm.getRieBoClassName().trim().length() == 0 || sessData.listRIERecordsOnly.get().booleanValue() || currentFormIsReadOnly)
				{
					setEnabled(false);				
				}
				else
				{
					setEnabled(formAccessLevel.isAccessibleFromNavigation(rieFormID.intValue()));
				}
			}
		}
		// Audit View
		else if(TopButton.AUDIT_VIEW.id == id)
		{
			Integer auditFormID = InitConfig.getAuditViewFormId();
			if (auditFormID == null || auditFormID.intValue() <= 0)
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				if (registeredForm.isDialog() || registeredForm.getRieBoClassName() == null || registeredForm.getRieBoClassName().trim().length() == 0)
				{
					setEnabled(false);				
				}
				else
				{
					setEnabled(formAccessLevel.isAccessibleFromNavigation(auditFormID.intValue()));
				}
			}
		}
		// Recording on behalf of...
		else if(TopButton.RECORDING_ON_BEHALF_OF.id == id)
		{
			Integer recordingOnBehalfOfFormID = InitConfig.getRecordingOnBehalfOfFormId();
			if (recordingOnBehalfOfFormID == null || recordingOnBehalfOfFormID.intValue() <= 0)
			{
				setVisible(false);
			}
			else
			{
				setVisible(true);
			}
		}
		// Locations Manager
		else if(TopButton.MANAGE_LOCATIONS.id == id)
		{
			Integer locationsManagerFormID = InitConfig.getLocationsManagerFormId();
			if (locationsManagerFormID == null || locationsManagerFormID.intValue() <= 0 || !isLocationSelectionProviderConfigured())
			{
				setEnabled(false);
			}
			else
			{
				setEnabled(formAccessLevel.isAccessibleFromNavigation(locationsManagerFormID.intValue()));
			}
		}
		// PAS Contacts
		else if(TopButton.PAS_CONTACTS.id == id)
		{
			Integer pasContactsFormID = InitConfig.getPASContactsFormId();
			if (pasContactsFormID == null || pasContactsFormID.intValue() <= 0)
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				setEnabled(formAccessLevel.isAccessibleFromNavigation(pasContactsFormID.intValue()));
			}
		}
		// Order Entry
		else if(TopButton.ORDER_ENTRY.id == id)
		{
			Integer orderEntryFormID = InitConfig.getOrderEntryFormId();
			if (orderEntryFormID == null || orderEntryFormID.intValue() <= 0)
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				setEnabled(formAccessLevel.isAccessibleFromNavigation(orderEntryFormID.intValue()));
			}
		}
		// Patient Summary
		else if(TopButton.PATIENT_SUMMARY.id == id)
		{
			Integer patientSummaryFormID = InitConfig.getPatientSummaryFormId();
			if (patientSummaryFormID == null || patientSummaryFormID.intValue() <= 0)
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				setEnabled(formAccessLevel.isAccessibleFromNavigation(patientSummaryFormID.intValue()));
			}	
		}
		// Change Password
		else if(TopButton.CHANGEPASSWORD.id == id)
		{
			Integer userPasswordChangeDialogId = InitConfig.getUserPasswordChangeDialogId();
			if (userPasswordChangeDialogId == null || userPasswordChangeDialogId.intValue() <= 0)
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				setEnabled(formAccessLevel.isAccessibleFromNavigation(userPasswordChangeDialogId.intValue()));
			}	
		}	
		// Dashboard
		else if(TopButton.DASHBOARD.id == id)
		{
			
			if(sessData.patientInfo.get() == null || sessData.patientInfo.get().equals(emptyPatientSelection))
			{
				setVisible(false);
				setEnabled(false);
			}
			else
			{
				setVisible(true);
				setEnabled(true);
			}
		}
		else if(form != null)
		{	
			boolean isAccessibleFromNavigation = formAccessLevel.isAccessibleFromNavigation(form.getID());
			if (contextDependent)
			{
				setEnabled(isAccessibleFromNavigation && enabled);
			}
			else
			{
				setEnabled(isAccessibleFromNavigation);
			}
		}
	}
	public void render(StringBuffer sb)
	{
		sb.append("<link id=\"");
		sb.append(id);			
		
		if(visible == true)
		{
			sb.append("\" image=\"");
			sb.append(image == null ? "" : image.getImagePath());
			
			sb.append("\" text=\"");
			sb.append(StringUtils.encodeXML(text));			
			sb.append("\" enabled=\"");
			sb.append(enabled ? "true" : "false");
			
			if(action != null)
			{
				sb.append("\" action=\"");
				sb.append(StringUtils.encodeXML(action));
			}
			
			if(alwaysEnabled)
			{
				sb.append("\" alwaysEnabled=\"true");				
			}
		}
		
		sb.append("\" visible=\"");
		sb.append(visible ? "true" : "false");			
		sb.append("\" />");
	}
	private boolean isLocationSelectionProviderConfigured()
	{
		return InitConfig.getLocationsSelectionFormId() != null && InitConfig.getLocationsSelectionFormId().intValue() > 0 && InitConfig.getLocationProviderClassName() != null && InitConfig.getLocationProviderClassName().trim().length() > 0;
	}	
}
