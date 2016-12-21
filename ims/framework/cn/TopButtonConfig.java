package ims.framework.cn;

import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.FormAccessLevel;
import ims.framework.interfaces.ITopButton;
import ims.framework.interfaces.ITopButtonConfig;
import ims.framework.interfaces.ITopButtonSection;

public class TopButtonConfig extends ims.framework.TopButtonConfig
{
	private static final long serialVersionUID = 1L;
	private TopButtonExtension extension = new TopButtonExtension(true);
	
	public TopButtonConfig()
	{		
	}
	public TopButtonConfig(ITopButtonConfig configuration)
	{		
		if(configuration == null || configuration.getITopButtonConfigButtons() == null)
			throw new RuntimeException("Invalid top button configuration");
		
		ITopButton[] buttons = configuration.getITopButtonConfigButtons();
		for(int x = 0; x < buttons.length; x++)
		{
			items.add(new TopButton(buttons[x]));			
		}
		extension.setNoColumns(configuration.getITopButtonConfigNoColumns());
		extension.setIncludePatientsSelectionHistory(configuration.getITopButtonConfigIncludePatientSelectionHistory());
		
		ITopButtonSection[] sections = configuration.getITopButtonConfigSections();
		if(sections != null)
		{
			for(int x = 0; x < sections.length; x++)
			{
				extension.getItems().add(new TopButtonSection(sections[x]));
				
				if(x == 0 && extension.getItems().get(0).getText().trim().length() == 0 && extension.getItems().get(0).getText().length() > 0)
					extension.getItems().get(0).setText("");
			}
		}
	}	
	public ims.framework.TopButtonExtension getExtension()
	{
		return extension;
	}	
	public static ims.framework.TopButtonConfig getDefaultConfiguration()
	{
		TopButtonConfig defaultConfiguration = new TopButtonConfig();
		defaultConfiguration.getExtension().setNoColumns(1);		
		defaultConfiguration.getExtension().setColumnWidth(180);		
		
		defaultConfiguration.getItems().add(TopButton.HOME);
		defaultConfiguration.getItems().add(TopButton.PATIENT_SEARCH);
		defaultConfiguration.getItems().add(TopButton.PRINT);	
		defaultConfiguration.getItems().add(TopButton.LOCK_SCREEN);	
		defaultConfiguration.getItems().add(TopButton.LOGOUT);		
		
		TopButtonSection topSection = new TopButtonSection(-1);
		TopButtonSection middleSection = new TopButtonSection(-2, " ", true);
		TopButtonSection bottomSection = new TopButtonSection(-3, " ", true);
		
		topSection.getItems().add(TopButton.RECORDED_IN_ERROR);
		topSection.getItems().add(TopButton.SELECT_ROLE);			
				
		middleSection.getItems().add(TopButton.PATIENT_SUMMARY);
		middleSection.getItems().add(TopButton.ORDER_ENTRY);
		middleSection.getItems().add(TopButton.AUDIT_VIEW);				
		middleSection.getItems().add(TopButton.MANAGE_LOCATIONS);
		middleSection.getItems().add(TopButton.PAS_CONTACTS);
		middleSection.getItems().add(TopButton.RECORDING_ON_BEHALF_OF);		
				
		bottomSection.getItems().add(TopButton.CHANGEPASSWORD);
		bottomSection.getItems().add(TopButton.ABOUT);
		
		defaultConfiguration.getExtension().getItems().add(topSection);
		defaultConfiguration.getExtension().getItems().add(middleSection);
		defaultConfiguration.getExtension().getItems().add(bottomSection);
		
		return defaultConfiguration;
	}
	public void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception
	{
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).preRenderContext(contextEvalFactory, formAccessLevel, sessData, currentFormIsReadOnly);
		}
		
		extension.preRenderContext(contextEvalFactory, formAccessLevel, sessData, currentFormIsReadOnly);
	}
	public void render(StringBuffer sb, SessionData sessData)
	{		
		sb.append("<topbuttons>");
		sb.append("<create>");
		
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).render(sb);			
		}
		
		extension.render(sb, sessData);		
		
		sb.append("</create>");
		sb.append("</topbuttons>");		
	}
	public ims.framework.TopButton find(int id)
	{
		for(int x = 0; x < items.size(); x++)
		{
			if(items.get(x).getID() == id)
				return items.get(x);
		}
		
		return extension.find(id);
	}
}
