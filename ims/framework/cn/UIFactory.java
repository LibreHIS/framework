package ims.framework.cn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ims.framework.FormUiLogic;
import ims.framework.cn.controls.CustomComponent;
import ims.framework.cn.controls.AnswerBox;
import ims.framework.cn.controls.AvailabilityControl;
import ims.framework.cn.controls.BarControl;
import ims.framework.cn.controls.BedPlanner;
import ims.framework.cn.controls.BookingCalendar;
import ims.framework.cn.controls.Button;
import ims.framework.cn.controls.CheckBox;
import ims.framework.cn.controls.CheckedListBox;
import ims.framework.cn.controls.ComboBox;
import ims.framework.cn.controls.DateControl;
import ims.framework.cn.controls.DateTimeControl;
import ims.framework.cn.controls.DecimalBox;
import ims.framework.cn.controls.DecimalRangeBox;
import ims.framework.cn.controls.DrawingControl;
import ims.framework.cn.controls.DrawingControlConfigurator;
import ims.framework.cn.controls.DynamicGrid;
import ims.framework.cn.controls.FileUploader;
import ims.framework.cn.controls.FloorPlanner;
import ims.framework.cn.controls.GraphingControl;
import ims.framework.cn.controls.Grid;
import ims.framework.cn.controls.HTMLViewer;
import ims.framework.cn.controls.Hint;
import ims.framework.cn.controls.HorizontalLine;
import ims.framework.cn.controls.ImageButton;
import ims.framework.cn.controls.IntBox;
import ims.framework.cn.controls.IntRangeBox;
import ims.framework.cn.controls.Label;
import ims.framework.cn.controls.Link;
import ims.framework.cn.controls.Panel;
import ims.framework.cn.controls.GaugeControl;
import ims.framework.cn.controls.PartialDateBox;
import ims.framework.cn.controls.Picture;
import ims.framework.cn.controls.RadioButton;
import ims.framework.cn.controls.RecordBrowser;
import ims.framework.cn.controls.RichTextControl;
import ims.framework.cn.controls.TextBox;
import ims.framework.cn.controls.TimeControl;
import ims.framework.cn.controls.TimeLineControl;
import ims.framework.cn.controls.TreeView;
import ims.framework.cn.controls.VideoPlayer;
import ims.framework.cn.controls.WavPlayer;
import ims.framework.cn.controls.PatientJourney;
import ims.framework.cn.controls.Diary;
import ims.framework.cn.controls.DynamicForm;
import ims.framework.cn.controls.CameraControl;
import ims.framework.cn.controls.FormDesignerControl;
import ims.framework.controls.GaugeStyle;
import ims.framework.controls.Timer;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.SortOrder;
import ims.framework.enumerations.TextTrimming;
import ims.framework.interfaces.IScreenHint;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.ImagePath;

public class UIFactory extends ims.framework.UIFactory
{
	private static final long serialVersionUID = 1L;
	private UIEngine engine;
	
	public UIFactory (HttpServletRequest request, HttpSession session)
	{
		setContext(request, session);
	}
	private final void setContext(HttpServletRequest request, HttpSession session)
	{
		this.request = request;		
		this.session = session;			
		this.engine = new UIEngine(this.request, this.session);
	}

	public ims.framework.UIEngine getUIEngine()
	{
		return engine;
	}
	public ims.framework.FormLoader getFormLoader()
	{
		return new FormLoader(this.request, this.session);
	}
	public ims.framework.Form getForm()
	{
		return FormFlyweightFactory.getInstance().getForm();
	}
	public ims.framework.Control getControl(Class controlClass, Object[] params)
	{
		return getControl(controlClass, null, params);
	}
	public ims.framework.Control getControl(Class controlClass, ims.framework.Form parentForm, Object[] params)
	{
		ims.framework.Control parentControl = (ims.framework.Control) params[0];
		int id = ((Integer) params[1]).intValue();
		if (controlClass.equals(ims.framework.controls.RadioButton.class))
		{
			ControlState viewMode = (ControlState) params[3];
			ControlState editMode = (ControlState) params[4];
			ControlAnchoring anchor = (ControlAnchoring) params[5]; 
			boolean autoPostBack = ((Boolean) params[6]).booleanValue();

			RadioButton control = (RadioButton) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.RADIO_BUTTON);
			control.setContext(parentControl, id, viewMode, editMode, anchor, autoPostBack);
			return control;
		}

		int x = ((Integer) params[2]).intValue();
		int y = ((Integer) params[3]).intValue();
		int width = ((Integer) params[4]).intValue();
		int height = ((Integer) params[5]).intValue();
		if (controlClass.equals(ims.framework.controls.Label.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			String text = (String) params[9];
			int style = ((Integer) params[10]).intValue();
			String tooltip = null;
			Integer size = null;
			
			if (params.length > 11)
				tooltip = (String) params[11];
			if (params.length > 12)
				size = (Integer) params[12];

			Label control = (Label) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.LABEL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, text, style, tooltip, (size == null ? 0 : size.intValue()));
			return control;
		}
		if (controlClass.equals(ims.framework.controls.Hint.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			String hintId = (String) params[9];
			IScreenHint hint = engine.getScreenHint(hintId);
			Hint control = (Hint) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.HINT);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, hint);
			return control;
		}
		else if (controlClass.equals(ims.framework.CustomComponent.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			Integer tabIndex = (Integer) params[9];
			FormUiLogic form = (FormUiLogic) params[10]; 
			CustomComponent control = (CustomComponent) params[11];
			Boolean isRequired = false;
			
			if(params.length > 12)
				isRequired = (Boolean) params[12];
			
			control.setContext(form, parentControl, id, x, y, width, height, viewMode, editMode, anchor, tabIndex == null ? -1 : tabIndex.intValue(), isRequired);
			return control;
		}
		else if (controlClass.equals(ims.framework.Container.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			String caption = (String) params[9];
			Integer groupID = new Integer(-1);
			
			Boolean isInLayer = new Boolean(false);
			Boolean layerHasTabs = new Boolean(false);
			Boolean layerHasAutoPostBack = new Boolean(false);
			Boolean collapsable = new Boolean(false);
			
			boolean standAloneContainer = false;
			Boolean visible = new Boolean(true);
			Boolean autoPostBack = new Boolean(false);
			
			if (params.length > 10)
			{
				if(params[10] instanceof Boolean)
				{
					standAloneContainer = true;				
					collapsable = (Boolean) params[10];
				}
				else
					groupID = (Integer) params[10];
			}	
			
			if(!standAloneContainer)
			{
				if (params.length > 11)
					visible = (Boolean) params[11];				
				if (params.length > 12)
					autoPostBack = (Boolean) params[12];
				if (params.length > 15)
				{
					isInLayer = (Boolean) params[13];
					layerHasTabs = (Boolean) params[14];
					layerHasAutoPostBack = (Boolean) params[15];
				}
				if (params.length > 16)
				{
					collapsable = (Boolean) params[16];
				}		
			}
			
			ims.framework.Container control = (ims.framework.Container) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.CONTAINER);			
			super.setContainerContext(parentControl, control, id, x, y, width, height, viewMode, editMode, anchor, caption, groupID.intValue(), visible.booleanValue(), autoPostBack.booleanValue(), isInLayer.booleanValue(), layerHasTabs.booleanValue(), layerHasAutoPostBack.booleanValue(), collapsable.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.Link.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String text = (String) params[10];
			Boolean canEditData = Boolean.FALSE;
			String toolTip = null;
			
			if (params.length > 11)
			{
				canEditData = (Boolean) params[11];
			}
			if (params.length > 12)
			{
				toolTip = (String) params[12];
			}			

			Link control = (Link) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.LINK);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, text, canEditData.booleanValue(), toolTip);
			return control; 
		}
		else if (controlClass.equals(ims.framework.controls.Panel.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			String text = (String) params[9];
			int style = ((Integer) params[10]).intValue();			
			String imageUrl = "";
			if (params.length > 11)
			{
				imageUrl = (String) params[11];	
			}

			Panel control = (Panel) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.PANEL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, text, style, imageUrl);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.HorizontalLine.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			int style = ((Integer) params[9]).intValue();

			HorizontalLine control = (HorizontalLine) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.HORIZONTAL_LINE);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, style);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.Button.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String text = (String) params[10];
			Boolean canEditData = Boolean.FALSE;
			String toolTip = null;
			Color backgroundColor = null;
			Color textColor = null;
			Boolean isDefaultButton = Boolean.FALSE;
			Boolean causesValidation = Boolean.TRUE;
			Boolean upload = Boolean.FALSE;
			
			if (params.length > 11)
			{
				canEditData = (Boolean) params[11];
			}		
			if (params.length > 12)
			{
				toolTip = (String) params[12];
			}
			if (params.length > 13)
			{
				isDefaultButton = (Boolean) params[13];
			}
			if (params.length > 14)
			{
				causesValidation = (Boolean) params[14];
			}
			if (params.length > 15)
			{
				upload = (Boolean) params[15];
			}
			Menu menu = null;
			if (params.length > 16)
			{
				menu = (Menu) params[16];
			}
			if (params.length > 17)
			{
				backgroundColor = (Color) params[17];
			}
			if (params.length > 18)
			{
				textColor = (Color) params[18];
			}
			
			Button control = (Button) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.BUTTON);			
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, text, canEditData.booleanValue(), toolTip, isDefaultButton.booleanValue(), causesValidation.booleanValue(), upload.booleanValue(), menu, backgroundColor, textColor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.ImageButton.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			Image enabledImage = (Image) params[10];
			Image disabledImage = (Image) params[11];
			String toolTip = null;			
			Boolean canEditData = Boolean.FALSE;
			Boolean isDefaultButton = Boolean.FALSE;
			Boolean causesValidation = Boolean.TRUE;
			Boolean upload = Boolean.FALSE;
			
			if (params.length > 12) 
			{
				toolTip = (String) params[12];
			}
			if (params.length > 13)
			{
				canEditData = (Boolean) params[13];
			}		
			if (params.length > 14)
			{
				isDefaultButton = (Boolean) params[14];
			}
			if (params.length > 15)
			{
				causesValidation = (Boolean) params[15];
			}
			if (params.length > 16)
			{
				upload = (Boolean) params[16];
			}
			Menu menu = null;
			if (params.length > 17)
			{
				menu = (Menu) params[17];
			}
			
			ImageButton control = (ImageButton) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.IMAGE_BUTTON);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, enabledImage, disabledImage, toolTip, canEditData.booleanValue(), isDefaultButton.booleanValue(), causesValidation.booleanValue(), upload.booleanValue(), menu);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.CheckBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String text = (String) params[10];
			boolean autoPostBack = ((Boolean) params[11]).booleanValue();						
			String toolTip = null;
						
			if (params.length > 12)
				toolTip = (String) params[12];
			
			CheckBox control = (CheckBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.CHECK_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, text, autoPostBack, toolTip);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.TextBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean multiLine = ((Boolean) params[10]).booleanValue();
			int maxLength = ((Integer) params[11]).intValue();
			boolean hasBorder = ((Boolean) params[12]).booleanValue();
			boolean isPassword = false;
			Menu menu = null;
			String tooltip = null;
			Boolean required = Boolean.FALSE;
			CharacterCasing casing = CharacterCasing.NORMAL; 
			TextTrimming textTrimming = TextTrimming.NONE;
			String mask = "";
			String validationString = null;
			
			if (params.length > 13)
				isPassword = ((Boolean) params[13]).booleanValue();
			if (params.length > 14)
				menu = (Menu)params[14];
			if (params.length > 15)
				tooltip = (String)params[15];
			if (params.length > 16)
				required = (Boolean)params[16];
			if (params.length > 17)
				casing = (CharacterCasing)params[17];
			if (params.length > 18)
				textTrimming = (TextTrimming)params[18];
			if (params.length > 19)
				mask = (String)params[19];
			if (params.length > 20)
				validationString = (String)params[20];

			TextBox control = (TextBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.TEXT_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, multiLine, maxLength, hasBorder, isPassword, menu, tooltip, required.booleanValue(), casing, textTrimming, mask, validationString);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.HTMLViewer.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9]; 
			Boolean keepSizeInfo = Boolean.FALSE;
			Boolean hasBorder = Boolean.TRUE;			
			Boolean shadow = Boolean.FALSE;
			
			if (params.length > 10)
				keepSizeInfo = (Boolean)params[10];
			if (params.length > 11)
				hasBorder = (Boolean)params[11];
			if (params.length > 12)
				shadow = (Boolean)params[12];
			
			HTMLViewer control = (HTMLViewer) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.HTML_VIEWER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, keepSizeInfo.booleanValue(), hasBorder.booleanValue(), shadow);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.IntBox.class))
		{
			String validationString = null;
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();
			
			boolean allowSign = ((Boolean) params[11]).booleanValue();
			boolean autoPostBack = ((Boolean) params[12]).booleanValue();
			String tooltip = null;
			Boolean required = Boolean.FALSE;
			Integer maxLength = 9;
			
			if (params.length > 13)
				validationString = (String)params[13];
			if (params.length > 14)
				tooltip = (String)params[14];
			if (params.length > 15)
				required = (Boolean)params[15];
			if (params.length > 16)
				maxLength = (Integer)params[16];
			
			IntBox control = (IntBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.INT_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, allowSign, autoPostBack, validationString, tooltip, required.booleanValue(), maxLength.intValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DecimalBox.class))
		{
			String validationString = null;
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();

			boolean allowSign = ((Boolean) params[11]).booleanValue();
			boolean autoPostBack = ((Boolean) params[12]).booleanValue();
			int precision = ((Integer) params[13]).intValue();
			int scale = ((Integer) params[14]).intValue();
			String tooltip = null;
			Boolean required = Boolean.FALSE;
			
			if (params.length > 15)
				validationString = (String)params[15];
			if (params.length > 16)
				tooltip = (String)params[16];
			if (params.length > 17)
				required = (Boolean)params[17];

			DecimalBox control = (DecimalBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DECIMAL_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, allowSign, autoPostBack, precision, scale, validationString, tooltip, required.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DateControl.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();
			boolean autoPostBack = false;
			Menu menu = null;
			Boolean required = Boolean.FALSE;
			String tooltip = null;

			String validationString = null;
			if (params.length > 11)
				validationString = (String)params[11];
			if (params.length > 12)
				autoPostBack = ((Boolean)params[12]).booleanValue();
			if (params.length > 13)
				menu = (Menu)params[13];
			if (params.length > 14)
				required = (Boolean)params[14];
			if (params.length > 15)
				tooltip = (String)params[15];

			DateControl control = (DateControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DATE_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, validationString, autoPostBack, menu, required.booleanValue(), tooltip);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.TimeControl.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();
			boolean autoPostBack = ((Boolean) params[11]).booleanValue();
			Boolean required = Boolean.FALSE;
			String tooltip = null;

			String validationString = null;
			if (params.length > 12)
				validationString = (String)params[12];
			if (params.length > 13)
				required = (Boolean)params[13];
			if (params.length > 14)
				tooltip = (String)params[14];

			TimeControl control = (TimeControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.TIME_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, autoPostBack, validationString, required.booleanValue(), tooltip);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.ComboBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();
			boolean autoPostBack = ((Boolean) params[11]).booleanValue();
			SortOrder sortOrder = (SortOrder) params[12];
			
			boolean searchable = false;
			boolean livesearch = false;
			int miNumberOfChars = 1;
            String tooltip = null;
            Boolean required = Boolean.FALSE;
            Integer maxVisibleItems = new Integer(-1); 
			
			if (params.length > 14)
			{
				searchable = ((Boolean) params[13]).booleanValue();
				miNumberOfChars = ((Integer) params[14]).intValue();
			}
			if (params.length > 15)
                tooltip = (String)params[15];
			if (params.length > 16)
                required = (Boolean)params[16];
			if (params.length > 17)
				maxVisibleItems = (Integer)params[17];
			if (params.length > 18)
				livesearch = ((Boolean) params[18]).booleanValue();							
            
			ComboBox control = (ComboBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.COMBO_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, autoPostBack, sortOrder, searchable, miNumberOfChars, tooltip, required.booleanValue(), maxVisibleItems.intValue(), livesearch);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.CheckedListBox.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];			
			Menu menu = (Menu) params[9];
			boolean autoPostBack = ((Boolean) params[10]).booleanValue();
			String tooltip = (String) params[11];
			int tabIndex = ((Integer) params[12]).intValue();
			Boolean required = Boolean.FALSE;
			Integer maxCheckedItems = null;
			
			if (params.length > 13)
				required = (Boolean)params[13];
			if (params.length > 14)
				maxCheckedItems = (Integer)params[14];
			
			CheckedListBox control = (CheckedListBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.CHECKED_LIST_BOX);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu, autoPostBack, tooltip, tabIndex, required.booleanValue(), maxCheckedItems == null ? -1 : maxCheckedItems.intValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.Grid.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canSelect = ((Boolean) params[10]).booleanValue();
			boolean canUnselect = ((Boolean) params[11]).booleanValue();
			int headerHeight = ((Integer) params[12]).intValue();
			boolean groupParents = ((Boolean) params[13]).booleanValue();
			Menu menu = null;
			boolean alwaysShowVScroll = false;
			boolean autoPostBackTreeNode = false;
			int footerMaxHeight = 0;
			String footerValue = null;
			boolean shadow = false;
			boolean alternateRowColor = true;
			
			if (params.length > 14)
				menu = (Menu)params[14];
			if (params.length > 15)
				alwaysShowVScroll = ((Boolean)params[15]).booleanValue();
			if (params.length > 16)
				autoPostBackTreeNode = ((Boolean)params[16]).booleanValue();
			if (params.length > 17)
				footerMaxHeight = ((Integer)params[17]).intValue();
			if (params.length > 18)
				footerValue = ((String)params[18]);
			if (params.length > 19)
				shadow = ((Boolean)params[19]);
			if (params.length > 20)
				alternateRowColor = ((Boolean)params[20]);

			Grid control = (Grid) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.GRID);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canSelect, canUnselect, headerHeight, groupParents, menu, alwaysShowVScroll, autoPostBackTreeNode, footerMaxHeight, footerValue, shadow, alternateRowColor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.TreeView.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean autoPostBack = ((Boolean) params[10]).booleanValue();
			boolean checkBoxes = ((Boolean) params[11]).booleanValue();
			boolean autoPostBackForCheckBoxes = ((Boolean) params[12]).booleanValue();
			Menu menu = null;
			boolean autoExpandSelection = true;			
			boolean allowDragDrop = false;
			boolean unselectable = false;
			Boolean required = Boolean.FALSE;
			boolean shadow = false;
			
			if (params.length > 13)
				menu = (Menu)params[13];			
			if (params.length > 14)
				autoExpandSelection = ((Boolean)params[14]).booleanValue();
			if (params.length > 15)
				allowDragDrop = ((Boolean)params[15]).booleanValue();
			if (params.length > 16)
				unselectable = ((Boolean)params[16]).booleanValue();
			if (params.length > 17)
				required = ((Boolean)params[17]).booleanValue();
			if (params.length > 18)
				shadow = ((Boolean)params[18]).booleanValue();
			
			TreeView control = (TreeView) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.TREE_VIEW);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, autoPostBack, checkBoxes, autoPostBackForCheckBoxes, menu, autoExpandSelection, allowDragDrop, unselectable, required, shadow);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.BookingCalendar.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean isRebooking = ((Boolean) params[10]).booleanValue();

			BookingCalendar control = (BookingCalendar) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.BOOKING_CALENDAR);
			control.setContext(this.session, parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, isRebooking);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.Picture.class))
		{
			ControlState viewMode = (ControlState) params[6];
			ControlState editMode = (ControlState) params[7];
			ControlAnchoring anchor = (ControlAnchoring) params[8];
			boolean autoSize = false;
			
			if(params.length > 9)
				autoSize = ((Boolean) params[9]).booleanValue();

			Picture control = (Picture) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.PICTURE);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, autoSize);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.WavPlayer.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];

			WavPlayer control = (WavPlayer) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.WAV_PLAYER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.AvailabilityControl.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			AvailabilityControl control = (AvailabilityControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.AVAILABILITY_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.FloorPlanner.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			Boolean areasEnabled = new Boolean(false);
			
			if( params.length > 10)
				areasEnabled = (Boolean) params[10];

			FloorPlanner control = (FloorPlanner) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.FLOOR_PLANNER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, areasEnabled);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DrawingControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9]; 
			Boolean allowMultiAreaDrawing = new Boolean(true);
			
			if( params.length > 10)
				allowMultiAreaDrawing = (Boolean) params[10];
			
			DrawingControl control = (DrawingControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DRAWING_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, allowMultiAreaDrawing.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.GraphingControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String title = (String) params[10];
			String subTitle = (String) params[11];
			Float minY = (Float) params[12];
			Float maxY = (Float) params[13];			
			Integer numberOfPointsY = (Integer) params[14];
			Integer numberOfLabelsX = (Integer) params[15];
			String labelX = (String) params[16];
			String labelY = (String) params[17];
			Boolean showLegend = (Boolean) params[18];
						
			GraphingControl control = (GraphingControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.GRAPHING_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, title, subTitle, minY.floatValue(), maxY.floatValue(), numberOfPointsY.intValue(), numberOfLabelsX.intValue(), labelX, labelY, showLegend.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.AnswerBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String text = (String)params[10];
			boolean autoPostBack = ((Boolean) params[11]).booleanValue();
			
			AnswerBox control = (AnswerBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.ANSWER_BOX);

			boolean canBeEmpty = true;
			int imgHeight = 10;
			Boolean required = Boolean.FALSE;
			
			if (params.length > 12)
				canBeEmpty = ((Boolean) params[12]).booleanValue();
			if (params.length > 13) 
				imgHeight = ((Integer) params[13]).intValue();
			if (params.length > 14) 
				required = (Boolean) params[14];
			
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, text, autoPostBack, canBeEmpty, imgHeight, required.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.VideoPlayer.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			VideoPlayer control = (VideoPlayer) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.VIDEO_PLAYER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.TimeLineControl.class))
		{
			ControlState viewMode = (ControlState) params[7];			
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			TimeLineControl control = (TimeLineControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.TIME_LINE);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.PartialDateBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String validationString = null;
			Boolean required = Boolean.FALSE;
			boolean autoPostBack = false;
			
			if (params.length > 10)
				validationString = (String)params[10];
			if (params.length > 11)
				required = (Boolean)params[11];
			if (params.length > 12)
				autoPostBack = (Boolean)params[12];
			
			PartialDateBox control = (PartialDateBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.PARTIAL_DATE_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, validationString, required.booleanValue(), autoPostBack);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.IntRangeBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			Boolean required = Boolean.FALSE;
			
			if (params.length > 10)
				required = (Boolean)params[10];
			
			IntRangeBox control = (IntRangeBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.INT_RANGE_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, required.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DecimalRangeBox.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9]; 
			int precision = ((Integer) params[10]).intValue();
			int scale = ((Integer) params[11]).intValue();
			Boolean required = Boolean.FALSE;
			
			if (params.length > 12)
				required = (Boolean)params[12];
			
			DecimalRangeBox control = (DecimalRangeBox) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DECIMAL_RANGE_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, precision, scale, required.booleanValue());
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DrawingControlConfigurator.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];			

			DrawingControlConfigurator control = (DrawingControlConfigurator) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DRAWING_CONTROL_CONFIGURATOR);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.BedPlanner.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			BedPlanner control = (BedPlanner) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.BED_PLANNER);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.BarControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			BarControl control = (BarControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.BAR_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DateTimeControl.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			boolean canBeEmpty = ((Boolean) params[10]).booleanValue();
			boolean autoPostBack = false;
			Menu menu = null;
			Boolean required = Boolean.FALSE;
			String tooltip = null;
			
			String validationString = null;
			if (params.length > 11)
				validationString = (String)params[11];
			if (params.length > 12)
				autoPostBack = ((Boolean)params[12]).booleanValue();
			if (params.length > 13)
				menu = (Menu)params[13];
			if (params.length > 14)
				required = (Boolean)params[14];
			if (params.length > 15)
				tooltip = (String)params[15];

			DateTimeControl control = (DateTimeControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DATETIME_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, canBeEmpty, validationString, autoPostBack, menu, required.booleanValue(), tooltip);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.RichTextControl.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			int maxLength = ((Integer) params[12]).intValue();
			Boolean required = Boolean.FALSE;
			Boolean simpleMode = Boolean.FALSE;
			
			if (params.length > 10)
				required = (Boolean)params[10];
			if (params.length > 11)
				simpleMode = (Boolean)params[11];
			
			RichTextControl control = (RichTextControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.RICH_TEXT_BOX);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, required.booleanValue(), getUIEngine().getSpellChecker(), simpleMode, maxLength);
			return control;
		}		
		else if (controlClass.equals(ims.framework.controls.FileUploader.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];			
			Boolean autoPostBack = Boolean.FALSE;
			Boolean uploadOnSelection = Boolean.FALSE;
			Boolean showOverwrite = Boolean.FALSE;			
			
			if (params.length > 10)
				autoPostBack = (Boolean)params[10];
			if (params.length > 11)
				uploadOnSelection = (Boolean)params[11];
			if (params.length > 12)
				showOverwrite = (Boolean)params[12];
			
						
			FileUploader control = (FileUploader) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.FILE_UPLOADER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, autoPostBack.booleanValue(), uploadOnSelection.booleanValue(), showOverwrite.booleanValue());
			return control;
		}		
		else if (controlClass.equals(ims.framework.controls.DynamicGrid.class))
		{
		    int tabIndex = ((Integer) params[6]).intValue();
		    ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			Menu menu = null;
			boolean autoPostBackTreeNode = false;
			boolean shadow = false;
			boolean alternateRowColor = true;
			
			if (params.length > 10)
				menu = (Menu)params[10];
			if (params.length > 11)
				autoPostBackTreeNode = ((Boolean)params[11]).booleanValue();
			if (params.length > 12)
				shadow = ((Boolean)params[12]).booleanValue();
			if (params.length > 13)
				alternateRowColor = ((Boolean)params[13]).booleanValue();
			
		    DynamicGrid control = (DynamicGrid) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DYNAMIC_GRID);
		    control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, menu, autoPostBackTreeNode, shadow, alternateRowColor);
		    return control;
		}	
		else if (controlClass.equals(ims.framework.controls.RecordBrowser.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			            
			RecordBrowser control = (RecordBrowser) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.RECORD_BROWSER);
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.PatientJourney.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];						
			Menu menu = null;
			boolean autoPostBack = false;
			
			if (params.length > 10)
				menu = (Menu)params[10];
			if (params.length > 11)
				autoPostBack = ((Boolean)params[11]).booleanValue();		
			            
			PatientJourney control = (PatientJourney) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.PATIENT_JOURNEY);			
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, menu, autoPostBack);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.Diary.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];						
			Menu menu = null;
			
			if (params.length > 10)
				menu = (Menu)params[10];
			            
			Diary control = (Diary) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DIARY);			
			control.setContext(parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor, menu);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.PortalDesigner.class))
		{
			return null;
		}
		else if (controlClass.equals(ims.framework.controls.GaugeControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			String caption = (String) params[10];
			String unitsSuffix = (String) params[11];
			float minimumValue = (Float) params[12];
			float maximumValue =  (Float) params[13];
			GaugeStyle style = (GaugeStyle) params[14];
			
			GaugeControl control = (GaugeControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.GAUGE_CONTROL);
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, caption, unitsSuffix, minimumValue, maximumValue, style);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.DynamicForm.class))
		{
			int tabIndex = ((Integer) params[6]).intValue();
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			DynamicForm control = (DynamicForm) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.DYNAMIC_FORM_CONTROL);			
			control.setContext(getFormLoader(), parentForm, parentControl, id, x, y, width, height, tabIndex, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.CameraControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			CameraControl control = (CameraControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.CAMERA_CONTROL);			
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		else if (controlClass.equals(ims.framework.controls.FormDesignerControl.class))
		{
			ControlState viewMode = (ControlState) params[7];
			ControlState editMode = (ControlState) params[8];
			ControlAnchoring anchor = (ControlAnchoring) params[9];
			
			FormDesignerControl control = (FormDesignerControl) ControlFlyweightFactory.getInstance().getControl(ControlFlyweightFactory.FORM_DESIGNER_CONTROL);			
			control.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
			return control;
		}
		
		throw new RuntimeException("Framework Internal Error: control " + controlClass.toString() + " is not implemented.");
	}
	public Timer createTimer(int id, int interval, boolean enabled)
	{
		return new ims.framework.cn.controls.Timer(id, interval, enabled);
	}
	public ims.framework.Menu createMenu(int id)
	{
		return new Menu(id);		
	}
	public ims.framework.MenuItem createMenuItem(int id, String text, boolean enabled, boolean visible, Integer imageID, boolean canEditData, boolean beginAGroup)
	{
		MenuItem menuItem = new MenuItem(id);
		menuItem.setContext(this.session, text, enabled, visible, imageID == null ? null : new ImagePath(imageID.intValue()), canEditData, beginAGroup);
		return menuItem;
	}
	private transient HttpServletRequest request;	
	private transient HttpSession session;

	public ims.framework.CustomComponent getEmptyCustomComponent() 
	{
		return new CustomComponent();
	}
}