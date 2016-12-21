package ims.framework.cn;

import java.io.Serializable;

import ims.configuration.ConfigFlag;
import ims.configuration.InitConfig;
import ims.domain.SessionData;
import ims.framework.enumerations.NavigationStyle;
import ims.framework.enumerations.UILayoutState;
import ims.framework.enumerations.UITheme;
import ims.framework.interfaces.IAppRoleLight;
import ims.framework.utils.StringUtils;

// LayoutManager.TOP = 0x1; // 1
// LayoutManager.LEFT = 0x2; // 10
// LayoutManager.RIGHT = 0x4; // 100
// LayoutManager.BOTTOM = 0x8; // 1000

public final class UILayout implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final int initialWidth = 800;
	private final int initialHeight = 600;
	
	private int formX = 141;
	private int formY = 81;
	private int formWidth = 652;
	private int formHeight = 518;
	private String formAnchor = "1111";
	
	private int formBgX = 2;
	private int formBgY = 77;
	private int formBgWidth = 796;
	private int formBgHeight = 522;
	private String formBgAnchor = "1111";
		
	private int formBottomX = 0;
	private int formBottomY = 588;
	private int formBottomWidth = 800;
	private int formBottomHeight = 12;
	private String formBottomAnchor = "1110";
	
	private int formDarkHeaderBgX = 134;
	private int formDarkHeaderBgY = 77;
	private int formDarkHeaderBgWidth = 663;
	private int formDarkHeaderBgHeight = 30;
	private String formDarkHeaderBgAnchor = "111";
	
	private int navigationX = 8;
	private int navigationY = 83;
	private int navigationWidth = 126;
	private int navigationHeight = 505;
	private String navigationAnchor = "1011";
	
	private int navigationBgX = 3;
	private int navigationBgY = 77;
	private int navigationBgWidth = 138;
	private int navigationBgHeight = 522;
	private String navigationBgAnchor = "1011";
	
	private int plainNavigationBgX = 0;
	private int plainNavigationBgY = 54;
	private int plainNavigationBgWidth = 174;
	private int plainNavigationBgHeight = 534;
	private String plainNavigationBgAnchor = "1011";
	
	private int formCaptionX = 8;
	private int formCaptionY = 0;
	private int formCaptionWidth = 496;
	private int formCaptionHeight = 48;
	private String formCaptionAnchor = "0111";
		
	private int formHeaderBgX = 2;
	private int formHeaderBgY = 49;
	private int formHeaderBgWidth = 796;
	private int formHeaderBgHeight = 35;
	private String formHeaderBgAnchor = "0111";
	
	private int iconsX = 12;
	private int iconsY = 53;
	private int iconsWidth = 150;
	private int iconsHeight = 30;
	private String iconsAnchor = "0011";
	
	private int shortPatientInfoX = 236;
	private int shortPatientInfoY = 56;
	private int shortPatientInfoWidth = 550;
	private int shortPatientInfoHeight = 30;	
	private String shortPatientInfoAnchor = "111";
	
	private int actionButtonsHeight = 30;
	
	private int topButtonsX = 280;	
	private int topButtonsY = 6;	
	private int topButtonsWidth = 510;
	private int topButtonsHeight = 30;
	private String topButtonsAnchor = "111";
	
	private int searchX = 715;	
	private int searchY = 16;	
	private int searchWidth = 80;
	private int searchHeight = 24;
	private String searchAnchor = "101";	
	
	private SessionData sessData;
	private UITheme theme;
	private NavigationStyle navigationStyle;
		
	public UILayout(SessionData sessData, UITheme theme)
	{				
		this.sessData = sessData;
		this.theme = theme;
		
		navigationStyle = NavigationStyle.Image;
		if(sessData.currentDynamicNavigation.get() != null && sessData.currentDynamicNavigation.get().getNavigation() != null)
		{
			navigationStyle = sessData.currentDynamicNavigation.get().getNavigation().getNavigationStyle();
		}
				
		if(theme.equals(UITheme.Blue))
		{
			if(navigationStyle == NavigationStyle.Text)
			{
				formX = 180;
				formWidth = 612;
				
				formDarkHeaderBgX = 173;				
				formDarkHeaderBgWidth = 624;
				
				navigationWidth = 162;		
				
				navigationBgWidth = 176;				
			}
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			formHeaderBgX = 0;
			formHeaderBgY = 0;
			formHeaderBgWidth = 800;
			formHeaderBgHeight = 62;
			formHeaderBgAnchor = "0111";
			
			formCaptionX = 8;
			formCaptionY = 2;
			formCaptionWidth = 300;
			formCaptionHeight = 22;
			formCaptionAnchor = "0111";
			
			shortPatientInfoX = 8;
			shortPatientInfoY = 33;
			shortPatientInfoWidth = 550;
			shortPatientInfoHeight = 20;			
			shortPatientInfoAnchor = "0111";
			
			iconsX = 500;
			iconsY = 30;
			iconsWidth = 300;
			iconsHeight = 24;
			iconsAnchor = "101";
			
			searchX = 680;	
			searchY = 2;	
			searchWidth = 115;
			searchHeight = 24;
			searchAnchor = "101";
			
			topButtonsX = 177;	
			topButtonsY = 1;	
			topButtonsWidth = 450;
			topButtonsHeight = 25;
			topButtonsAnchor = "101";
			
			navigationBgX = 0;
			navigationBgY = 54;
			navigationBgWidth = 138;
			navigationBgHeight = 539;
			navigationBgAnchor = "1011";
			
			navigationX = 3;
			navigationY = 54;
			navigationWidth = 126;
			navigationHeight = 543;
			navigationAnchor = "1011";
			
			formX = 134;
			formY = 54;
			formWidth = 665;
			formHeight = 534;
			formAnchor = "1111";
			
			formDarkHeaderBgX = 130;
			formDarkHeaderBgY = 54;
			formDarkHeaderBgWidth = 670;
			formDarkHeaderBgHeight = 30;
			formDarkHeaderBgAnchor = "111";
			
			if(navigationStyle == NavigationStyle.Text)
			{
				formX = 172;
				formY = 54;
				formWidth = 628;
				formHeight = 534;				
				
				navigationX = 0;
				navigationY = 54;
				navigationWidth = 169;
				navigationHeight = 534;
				
				formDarkHeaderBgX = 166;
				formDarkHeaderBgY = 54;
				formDarkHeaderBgWidth = 643;
				formDarkHeaderBgHeight = 30;
			}			
		}		
	}
	
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderLogin(java.lang.StringBuffer)
	 */
	public void renderLogin(StringBuffer sb)
	{
		renderLogin(sb, ConfigFlag.GEN.LOGIN_MESSAGE.getValue());
	}
	private void renderLogin(StringBuffer sb, String loginMessage)
	{
		sb.append("<close postback=\"false\"/><screen clearForm=\"true\" ");
		sb.append("initialWidth=\"");
		sb.append(initialWidth);
		sb.append("\" ");
		sb.append("initialHeight=\"");
		sb.append(initialHeight);
		sb.append("\" ");
		sb.append(">");		
				
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<item id=\"form\" x=\"0\" y=\"300\" width=\"800\" height=\"150\" anchor=\"0111\" />");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<item id=\"form\" x=\"0\" y=\"0\" width=\"800\" height=\"460\" anchor=\"110\"/>");
		}		
		
		sb.append("</screen>");
		
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<form id=\"loginForm\" width=\"200\" height=\"150\">");
			sb.append("<logincontrol id=\"a1001\" x=\"0\" y=\"0\" width=\"200\" height=\"150\" anchor=\"1111\" />");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<form id=\"loginForm\" width=\"800\" height=\"600\">");
			sb.append("<logincontrol_newblue id=\"a1001\" x=\"0\" y=\"0\" width=\"800\" height=\"600\" anchor=\"1111\"/>");
		}
		
		sb.append("</form>");	
		sb.append("<data browserTitle=\"Login\">");
		
		boolean loginRendered = false;
		if(sessData.passwordSalt.get() != null)
		{
			loginRendered = true;
			if(theme.equals(UITheme.Blue))
			{
				sb.append("<logincontrol id=\"a1001\" salt=\"" + StringUtils.encodeXML(sessData.passwordSalt.get()) + "\"");				
			}
			else if(theme.equals(UITheme.NewBlue))
			{
				sb.append("<logincontrol_newblue id=\"a1001\" salt=\"" + StringUtils.encodeXML(sessData.passwordSalt.get()) + "\"");								
			}			
		}
		else if(loginMessage != null && loginMessage.trim().length() > 0)
		{
			loginRendered = true;
			
			if(theme.equals(UITheme.Blue))
			{
				sb.append("<logincontrol id=\"a1001\"");				
			}			
			else
			{
				sb.append("<logincontrol id=\"a1001\"");
			}
		}
		
		if(loginRendered)
		{
			if(loginMessage != null && loginMessage.trim().length() > 0)
			{
				sb.append(">");
				renderLoginMessage(sb, loginMessage);
				sb.append("</logincontrol>");
			}
			else
			{
				sb.append("/>");
			}
		}
		
		sb.append("</data>");
		
		renderRemoveAllAlerts(sb);
	}	
	private void renderLoginMessage(StringBuffer sb, String loginMessage)
	{
		sb.append("<message><br/>");
		
		if(loginMessage != null)
		{
			sb.append(StringUtils.encodeXML(loginMessage));
		}
		
		sb.append("<br/> <br/> </message>");
	}

	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderRemoveAllAlerts(java.lang.StringBuffer)
	 */
	public void renderRemoveAllAlerts(StringBuffer sb)
	{
		sb.append("<alerts><removeall/></alerts>");
	}
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderPasswordExpiry(java.lang.StringBuffer, int)
	 */
	public void renderPasswordExpiry(StringBuffer sb, int passwordExpiryDays)
	{
		sb.append("<data>"); 
		sb.append("<logincontrol id=\"a1001\" passwordExpireDays=\"" + passwordExpiryDays + "\"/>");
		sb.append("</data>");
	}
	public void renderScreenLockInitialization(StringBuffer sb, String url)
	{
		sb.append("<screen width=\"800\" height=\"600\"></screen>");
		sb.append("<dialog background=\"false\" fullScreen=\"true\" resizable=\"true\" width=\"800\" height=\"600\" url=\"" + url + "\" id=\"999\"/>");			
	}
	public void renderScreenLock(StringBuffer sb, String lockMessage)
	{
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<form width=\"800\" height=\"600\">");
			sb.append("<logincontrol id=\"a1001\" lock=\"true\" x=\"0\" y=\"200\" width=\"800\" height=\"100\" anchor=\"1111\"/>");
			sb.append("</form>");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<form width=\"800\" height=\"600\">");
			sb.append("<logincontrol_newblue id=\"a1001\" lock=\"true\" x=\"0\" y=\"0\" width=\"800\" height=\"600\" anchor=\"1111\"/>");
			sb.append("</form>");
		}
		
		if(sessData.passwordSalt.get() != null)
		{
			// creating a new password salt
			sessData.initializePasswordSalt();
			
			sb.append("<data>");
			sb.append("<logincontrol id=\"a1001\" salt=\"" + StringUtils.encodeXML(sessData.passwordSalt.get()) + "\"");
			
			if(lockMessage != null && lockMessage.trim().length() > 0)
			{
				sb.append(">");
				renderLoginMessage(sb, lockMessage);
				sb.append("</logincontrol>");
			}
			else
			{
				sb.append("/>");
			}
			
			sb.append("</data>");
		}
		else if(lockMessage != null && lockMessage.trim().length() > 0)
		{
			sb.append("<data><logincontrol id=\"a1001\">");
			renderLoginMessage(sb, lockMessage);
			sb.append("</logincontrol></data>");
		}		
	}
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderScreenUnlockRequest(java.lang.StringBuffer)
	 */
	public void renderScreenUnlockRequest(StringBuffer sb)
	{
		sb.append("<close/>");		
	}
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderScreenUnlock(java.lang.StringBuffer, boolean, java.lang.Integer)
	 */
	public void renderScreenUnlock(StringBuffer sb, boolean isRie, Integer formDarkHeaderHeight)
	{
		render(sb, isRie, formDarkHeaderHeight);
	}
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#renderRoleSelection(java.lang.StringBuffer, ims.framework.interfaces.IAppRoleLight[])
	 */
	public void renderRoleSelection(StringBuffer sb, IAppRoleLight[] roles)
	{
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<screen clearForm=\"true\">");
			sb.append("<item id=\"form\" x=\"0\" y=\"0\" width=\"800\" height=\"600\" anchor=\"1111\" />");
		}
		else if(theme.equals(UITheme.NewBlue))		
		{
			sb.append("<screen clearForm=\"true\" initialWidth=\"800\" initialHeight=\"600\">");
			sb.append("<item id=\"form\" x=\"0\" y=\"0\" width=\"800\" height=\"460\" anchor=\"110\" />");
		}
		
		sb.append("</screen>");		
		
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<form id=\"selectRoleForm\" width=\"800\" height=\"600\">");
			sb.append("<selectrolecontrol id=\"a1001\" x=\"0\" y=\"0\" width=\"800\" height=\"600\" anchor=\"1111\" includeStoredLocations=\"false\">");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<form id=\"loginForm\" width=\"800\" height=\"600\">");
			sb.append("<selectrole_newblue id=\"selectrole1\" x=\"0\" y=\"0\" width=\"800\" height=\"460\" anchor=\"111\" includeStoredLocations=\"false\">");
		}
		
		boolean usePds = false;
		if(ims.configuration.ConfigFlag.DOM.USE_PDS.getValue() != null &&
			ims.configuration.ConfigFlag.DOM.USE_PDS.getValue().trim().length() > 0 &&
				!ims.configuration.ConfigFlag.DOM.USE_PDS.getValue().trim().equals("None"))
		{
			usePds = true;				
		}
		
		for(int x = 0; x < roles.length; x++)
		{
			//if ((roles[x].)
			sb.append("<role id=\"");
			sb.append(roles[x].getId());			
			sb.append("\" requiresPDS=\"");
			sb.append(roles[x].getRequiresPDS() != null && usePds ? (roles[x].getRequiresPDS() ? "true" : "false") : "false");
			sb.append("\">");
			sb.append(StringUtils.encodeXML(roles[x].getName()));
			sb.append("</role>");
		}
		
		if(theme.equals(UITheme.Blue))
			sb.append("</selectrolecontrol>");
		else if(theme.equals(UITheme.NewBlue))
			sb.append("</selectrole_newblue>");
		
		sb.append("</form>");
	}
	private void renderFullScreen(StringBuffer sb, Integer formDarkHeaderHeight)
	{
		render(sb, false, false, false, false, false, false, formDarkHeaderHeight);
	}
	private void render(StringBuffer sb, boolean isRie, Integer formDarkHeaderHeight)
	{
		render(sb, true, true, true, false, isRie, InitConfig.isSearchEnabled(), formDarkHeaderHeight);
	}
	/* (non-Javadoc)
	 * @see ims.framework.cn.IUILayout#render(java.lang.StringBuffer, ims.domain.SessionData, boolean)
	 */
	public void render(StringBuffer sb, boolean isRie)
	{
		UILayoutState currentState = sessData.currentUILayoutState.get();
		
		if((sessData.uiLayoutChanged.get() == null || !sessData.uiLayoutChanged.get()) && !isRie)
			return;
		
		sessData.uiLayoutChanged.set(null);
		
		if(currentState.equals(UILayoutState.FULLSCREEN))
			renderFullScreen(sb, sessData.formDarkHeaderHeight.get());
		else
			render(sb, isRie, sessData.formDarkHeaderHeight.get());
	}
	private void render(StringBuffer sb, boolean topControls, boolean informationBar, boolean navigation, boolean actionButtons, boolean isRie, boolean search, Integer formDarkHeaderHeight)
	{
		//sb.append("<screen clearForm=\"true\" ");
		sb.append("<screen ");
		
		sb.append("initialWidth=\"");
		sb.append(initialWidth);
		sb.append("\" ");
		sb.append("initialHeight=\"");
		sb.append(initialHeight);
		sb.append("\" ");
		sb.append(">");
		
		renderForm(sb, topControls, informationBar, navigation, actionButtons, formDarkHeaderHeight);
		
		if(topControls)
			renderTopControls(sb, search);
		
		if(informationBar)
			renderInformationBar(sb, topControls, isRie);
		
		if(navigation)
			renderNavigation(sb, topControls, informationBar);
		
		if(actionButtons)
			renderActionButtons(sb);
		
		sb.append("</screen>");
	}
	private void renderActionButtons(StringBuffer sb)
	{
		sb.append("<item id=\"actionBar\" x=\"179\" y=\"553\" width=\"604\" height=\"" + actionButtonsHeight + "\" anchor=\"1110\" attachAsFormToTag=\"actionBar\" />");
	}
	private void renderForm(StringBuffer sb, boolean topControls, boolean informationBar, boolean navigation, boolean actionButtons, Integer formDarkHeaderHeight)
	{
		int x, y, width, height;	
		
		// --- Form Bottom ----------------------------
		if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<item id=\"formBottom\" x=\"" + formBottomX + "\" y=\"" + formBottomY + "\" width=\"" + formBottomWidth + "\" height=\"" + formBottomHeight + "\" anchor=\"" + formBottomAnchor + "\"/>");
		}
		
		// --- Form -----------------------------------
		sb.append("<item id=\"form");
		
		sb.append("\" x=\"");
		x = formX;
		if(!navigation)
			x -= navigationWidth;		
		sb.append(x);
					
		sb.append("\" y=\"");		
		y = formY;		
		if(!topControls)
			y -= topButtonsHeight;		
		if(!informationBar)
			y -= shortPatientInfoHeight;		
		sb.append(y);		
		
		sb.append("\" width=\"");
		width = formWidth;
		if(!navigation)
			width += navigationWidth;			
		sb.append(width);
				
		sb.append("\" height=\"");
		height = formHeight;
		if(!topControls)
			height += topButtonsHeight;		
		if(!informationBar)
			height += shortPatientInfoHeight;
		if(actionButtons)
			height -= actionButtonsHeight;
		sb.append(height);
		
		sb.append("\" anchor=\"");
		sb.append(formAnchor);
		sb.append("\" />");
		
		// --- Form Background -----------------------		
		sb.append("<item id=\"formBG");
			
		sb.append("\" x=\"");
		x = formBgX;
		sb.append(x);
		
		sb.append("\" y=\"");
		y = formBgY;
		if(!topControls)
			y -= topButtonsHeight;		
		if(!informationBar)
			y -= shortPatientInfoHeight;			
		sb.append(y);
		
		sb.append("\" width=\"");
		width = formBgWidth;
		sb.append(width);
		
		sb.append("\" height=\"");
		height = formBgHeight;
		if(!topControls)
			height += topButtonsHeight;		
		if(!informationBar)
			height += shortPatientInfoHeight;
		sb.append(height);
		
		sb.append("\" anchor=\"");
		sb.append(formBgAnchor);
		sb.append("\" />");		
		
		// --- Dark Header --------------------------	
		sb.append("<item id=\"darkHeader");
		
		sb.append("\" x=\"");
		x = formDarkHeaderBgX;
		if(!navigation)
			x -= navigationWidth;
		sb.append(x);
		
		sb.append("\" y=\"");
		y = formDarkHeaderBgY;
		if(!topControls)
			y -= topButtonsHeight;		
		if(!informationBar)
			y -= shortPatientInfoHeight;
		sb.append(y);
		
		sb.append("\" width=\"");
		width = formDarkHeaderBgWidth;
		if(!navigation)
			width += navigationWidth;
		sb.append(width);
		
		sb.append("\" height=\"");
		height = formDarkHeaderHeight == null ? formDarkHeaderBgHeight : (formDarkHeaderHeight.intValue() + 21);
		sb.append(height);
		
		sb.append("\" anchor=\"");
		sb.append(formDarkHeaderBgAnchor);
		sb.append("\" />");
	}
	private void renderTopControls(StringBuffer sb, boolean search)
	{
		// --- Top Buttons --------------------------	
		sb.append("<item id=\"topRightControls");
		
		if(!theme.equals(UITheme.NewBlue) && search)		
		{
			topButtonsX =  199;
			topButtonsWidth = 510;
		}
		
		sb.append("\" x=\"");
		sb.append(topButtonsX);
		sb.append("\" y=\"");
		sb.append(topButtonsY);
		sb.append("\" width=\"");
		sb.append(topButtonsWidth);
		sb.append("\" height=\"");
		sb.append(topButtonsHeight);
		sb.append("\" anchor=\"");
		sb.append(topButtonsAnchor);
		sb.append("\" />");
		
		if(search)
		{
			// --- Search --------------------------	
			sb.append("<item id=\"search");
			
			sb.append("\" control=\"search");
			
			sb.append("\" x=\"");
			sb.append(searchX);
			sb.append("\" y=\"");
			sb.append(searchY);
			sb.append("\" width=\"");
			sb.append(searchWidth);
			sb.append("\" height=\"");
			sb.append(searchHeight);
			sb.append("\" anchor=\"");
			sb.append(searchAnchor);
			sb.append("\" />");
		}
		
		// --- Form Caption ------------------------
		sb.append("<item id=\"formCaption");
		
		sb.append("\" x=\"");
		sb.append(formCaptionX);
		sb.append("\" y=\"");
		sb.append(formCaptionY);
		sb.append("\" width=\"");
		sb.append(formCaptionWidth);
		sb.append("\" height=\"");
		sb.append(formCaptionHeight);
		sb.append("\" anchor=\"");
		sb.append(formCaptionAnchor);
		sb.append("\" />");
	}
	private void renderNavigation(StringBuffer sb, boolean topControls, boolean informationBar)
	{
		int x, y, width, height;	
		
		sb.append("<item id=\"navigation");		
		sb.append("\" x=\"");
		x = navigationX;
		sb.append(x);
		sb.append("\" y=\"");
		y = navigationY;
		if(!topControls)
			y -= topButtonsHeight + 3;
		if(!informationBar)
			y -= shortPatientInfoHeight + 3;
		sb.append(y);
		sb.append("\" width=\"");
		width = navigationWidth;
		sb.append(width);
		sb.append("\" height=\"");
		height = navigationHeight;
		if(!topControls)
			height += topButtonsHeight + 3;
		if(!informationBar)
			height += shortPatientInfoHeight + 3;
		sb.append(height);
		sb.append("\" anchor=\"");
		sb.append(navigationAnchor);
		sb.append("\" />");
		
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<item id=\"navigationBG");
			sb.append("\" x=\"");
			x = navigationBgX;
			sb.append(x);
			sb.append("\" y=\"");
			y = navigationBgY;
			if(!topControls)
				y -= topButtonsHeight + 3;
			if(!informationBar)
				y -= shortPatientInfoHeight + 3;
			sb.append(y);
			sb.append("\" width=\"");
			width = navigationBgWidth;
			sb.append(width);
			sb.append("\" height=\"");
			height = navigationBgHeight;		
			if(!topControls)
				height += topButtonsHeight + 3;
			if(!informationBar)
				height += shortPatientInfoHeight + 3;
			sb.append(height);
			sb.append("\" anchor=\"");
			sb.append(navigationBgAnchor);
			sb.append("\" />");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			if(navigationStyle == NavigationStyle.Image)
			{
				sb.append("<item id=\"navigationBG");
				sb.append("\" x=\"");
				x = navigationBgX;
				sb.append(x);
				sb.append("\" y=\"");
				y = navigationBgY;
				if(!topControls)
					y -= topButtonsHeight + 3;
				if(!informationBar)
					y -= shortPatientInfoHeight + 3;
				sb.append(y);
				sb.append("\" width=\"");
				width = navigationBgWidth;
				sb.append(width);
				sb.append("\" height=\"");
				height = navigationBgHeight;		
				if(!topControls)
					height += topButtonsHeight + 3;
				if(!informationBar)
					height += shortPatientInfoHeight + 3;
				sb.append(height);
				sb.append("\" anchor=\"");
				sb.append(navigationBgAnchor);
				sb.append("\" />");
			}
			else
			{
				sb.append("<item id=\"plainNavigationBG");
				sb.append("\" x=\"");
				x = plainNavigationBgX;
				sb.append(x);
				sb.append("\" y=\"");
				y = plainNavigationBgY;
				if(!topControls)
					y -= topButtonsHeight + 3;
				if(!informationBar)
					y -= shortPatientInfoHeight + 3;
				sb.append(y);
				sb.append("\" width=\"");
				width = plainNavigationBgWidth;
				sb.append(width);
				sb.append("\" height=\"");
				height = plainNavigationBgHeight;		
				if(!topControls)
					height += topButtonsHeight + 3;
				if(!informationBar)
					height += shortPatientInfoHeight + 3;
				sb.append(height);
				sb.append("\" anchor=\"");
				sb.append(plainNavigationBgAnchor);
				sb.append("\" />");
			}
		}
	}
	private void renderInformationBar(StringBuffer sb, boolean topControls, boolean isRie)
	{
		int x, y, width, height;
				
		sb.append("<item id=\"shortPatientInfo");
		
		sb.append("\" x=\"");
		x = shortPatientInfoX;
		sb.append(x);
		
		sb.append("\" y=\"");
		y = shortPatientInfoY;
		if(!topControls)
			y -= topButtonsHeight;
		sb.append(y);
		
		sb.append("\" width=\"");
		width = shortPatientInfoWidth;
		sb.append(width);
		
		sb.append("\" height=\"");
		height = shortPatientInfoHeight;
		sb.append(height);
		
		sb.append("\" anchor=\"");
		sb.append(shortPatientInfoAnchor);
		sb.append("\" />");			
		
		if(theme.equals(UITheme.Blue))
		{
			sb.append("<item id=\"formHeaderBG");		
			
			sb.append("\" x=\"");
			x = formHeaderBgX;
			sb.append(x);
			
			sb.append("\" y=\"");
			y = formHeaderBgY;
			if(!topControls)
				y -= topButtonsHeight;
			sb.append(y);
			
			sb.append("\" width=\"");
			width = formHeaderBgWidth;
			sb.append(width);
			
			sb.append("\" height=\"");
			height = formHeaderBgHeight;
			sb.append(height);
			
			sb.append("\" anchor=\"");
			sb.append(formHeaderBgAnchor);
			sb.append("\" />");
		}
		else if(theme.equals(UITheme.NewBlue))
		{
			sb.append("<item id=\"formHeaderBG");		
			
			sb.append("\" x=\"");
			x = formHeaderBgX;
			sb.append(x);
			
			sb.append("\" y=\"");
			y = formHeaderBgY;
			if(!topControls)
				y -= topButtonsHeight;
			sb.append(y);
			
			sb.append("\" width=\"");
			width = formHeaderBgWidth;
			sb.append(width);
			
			sb.append("\" height=\"");
			height = formHeaderBgHeight;
			sb.append(height);
			
			sb.append("\" anchor=\"");
			sb.append(formHeaderBgAnchor);
			sb.append("\" />");
		}
		
		if(isRie)
		{
			sb.append("<item id=\"formRedHeaderBG");
			
			sb.append("\" x=\"");
			x = formHeaderBgX;
			sb.append(x);
			
			sb.append("\" y=\"");
			y = formHeaderBgY;
			if(!topControls)
				y -= topButtonsHeight;
			sb.append(y);
			
			sb.append("\" width=\"");
			width = formHeaderBgWidth;
			sb.append(width);
			
			sb.append("\" height=\"");
			height = formHeaderBgHeight;
			sb.append(height);
			
			sb.append("\" anchor=\"");
			sb.append(formHeaderBgAnchor);
			sb.append("\" />");
		}
		
		// --- Alert Icons ------------------------
		sb.append("<item id=\"alertIcons");
		
		sb.append("\" x=\"");
		x = iconsX;
		sb.append(x);
		
		sb.append("\" y=\"");
		y = iconsY;
		if(!topControls)
			y -= topButtonsHeight;
		sb.append(y);
		
		sb.append("\" width=\"");
		width = iconsWidth;
		sb.append(width);
		
		sb.append("\" height=\"");
		height = iconsHeight;
		sb.append(height);
		
		sb.append("\" anchor=\"");
		sb.append(iconsAnchor);
		sb.append("\" />");
	}
}
