package ims.framework.cn;

import ims.framework.cn.controls.AnswerBox;
import ims.framework.cn.controls.AvailabilityControl;
import ims.framework.cn.controls.BarControl;
import ims.framework.cn.controls.BedPlanner;
import ims.framework.cn.controls.BookingCalendar;
import ims.framework.cn.controls.Button;
import ims.framework.cn.controls.CameraControl;
import ims.framework.cn.controls.CheckBox;
import ims.framework.cn.controls.CheckedListBox;
import ims.framework.cn.controls.ComboBox;
import ims.framework.cn.controls.Container;
import ims.framework.cn.controls.CustomComponent;
import ims.framework.cn.controls.DateControl;
import ims.framework.cn.controls.DateTimeControl;
import ims.framework.cn.controls.DecimalBox;
import ims.framework.cn.controls.DecimalRangeBox;
import ims.framework.cn.controls.DrawingControl;
import ims.framework.cn.controls.DrawingControlConfigurator;
import ims.framework.cn.controls.DynamicGrid;
import ims.framework.cn.controls.FileUploader;
import ims.framework.cn.controls.FloorPlanner;
import ims.framework.cn.controls.FormDesignerControl;
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
import ims.framework.cn.controls.PartialDateBox;
import ims.framework.cn.controls.Picture;
import ims.framework.cn.controls.RadioButton;
import ims.framework.cn.controls.RecordBrowser;
import ims.framework.cn.controls.RichTextControl;
import ims.framework.cn.controls.SearchableComboBox;
import ims.framework.cn.controls.TextBox;
import ims.framework.cn.controls.TimeControl;
import ims.framework.cn.controls.TimeLineControl;
import ims.framework.cn.controls.TreeView;
import ims.framework.cn.controls.VideoPlayer;
import ims.framework.cn.controls.WavPlayer;
import ims.framework.cn.controls.PatientJourney;
import ims.framework.cn.controls.Diary;
import ims.framework.cn.controls.PortalDesigner;
import ims.framework.cn.controls.GaugeControl;
import ims.framework.cn.controls.DynamicForm;

import java.io.Serializable;

public class ControlFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;	
	
	public static final int LABEL = 0;
	public static final int PANEL = 1;
	public static final int BOOKING_CALENDAR = 2;
	public static final int BUTTON = 3;
	public static final int CHECK_BOX = 4;
	public static final int COMBO_BOX = 5;
	public static final int CONTAINER = 6;
	public static final int DATE_CONTROL = 7;
	public static final int DECIMAL_BOX = 8;
	public static final int GRID = 9;
	public static final int HORIZONTAL_LINE = 10;
	public static final int IMAGE_BUTTON = 11;
	public static final int INT_BOX = 12;
	public static final int LINK = 13;
	public static final int PICTURE = 14;
	public static final int RADIO_BUTTON = 15;
	public static final int TEXT_BOX = 16;
	public static final int TIME_CONTROL = 17;
	public static final int TREE_VIEW = 18;
	public static final int WAV_PLAYER = 19;
	public static final int AVAILABILITY_CONTROL = 20;
	public static final int FLOOR_PLANNER = 21;
	public static final int DRAWING_CONTROL = 22;
	public static final int HTML_VIEWER = 23;
	public static final int ANSWER_BOX = 24;
	public static final int VIDEO_PLAYER = 25;
	public static final int TIME_LINE = 26;
	public static final int SEARCHABLE_COMBO_BOX = 27;
	public static final int PARTIAL_DATE_BOX = 28;
	public static final int INT_RANGE_BOX = 29;
	public static final int DECIMAL_RANGE_BOX = 30;
	public static final int DRAWING_CONTROL_CONFIGURATOR = 31;
	public static final int BED_PLANNER = 32;
	public static final int GRAPHING_CONTROL = 33;
	public static final int BAR_CONTROL = 34;
	public static final int DATETIME_CONTROL = 35;
	public static final int RICH_TEXT_BOX = 36;
	public static final int FILE_UPLOADER = 37;
	public static final int DYNAMIC_GRID = 38;
	public static final int CHECKED_LIST_BOX = 39;
	public static final int RECORD_BROWSER = 40;
	public static final int CUSTOM_COMPONENT = 41;
	public static final int PATIENT_JOURNEY = 42;
	public static final int DIARY = 43;
	public static final int PORTAL_DESIGNER = 44;
	public static final int GAUGE_CONTROL = 45;
	public static final int DYNAMIC_FORM_CONTROL = 46;
	public static final int CAMERA_CONTROL = 47;
	public static final int FORM_DESIGNER_CONTROL = 48;
	public static final int HINT = 49;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  CONTROLS   ////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public ims.framework.Control getControl(int index)
	{
		return this.createControl(index);
	}


	private ims.framework.Control createControl(int index)
	{
		if (index == ControlFlyweightFactory.LABEL) 
			return new Label();
		else if (index == ControlFlyweightFactory.PANEL)
			return new Panel();
		else if (index == ControlFlyweightFactory.BOOKING_CALENDAR)
			return new BookingCalendar();
		else if (index == ControlFlyweightFactory.BUTTON)
			return new Button();
		else if (index == ControlFlyweightFactory.CHECK_BOX)
			return new CheckBox();
		else if (index == ControlFlyweightFactory.COMBO_BOX)
			return new ComboBox();
		else if (index == ControlFlyweightFactory.CONTAINER)
			return new Container();
		else if (index == ControlFlyweightFactory.DATE_CONTROL)
			return new DateControl();
		else if (index == ControlFlyweightFactory.DECIMAL_BOX)
			return new DecimalBox();
		else if (index == ControlFlyweightFactory.GRID)
			return new Grid();
		else if (index == ControlFlyweightFactory.HORIZONTAL_LINE)
			return new HorizontalLine();
		else if (index == ControlFlyweightFactory.IMAGE_BUTTON)
			return new ImageButton();
		else if (index == ControlFlyweightFactory.INT_BOX)
			return new IntBox();
		else if (index == ControlFlyweightFactory.LINK)
			return new Link();
		else if (index == ControlFlyweightFactory.PICTURE)
			return new Picture();
		else if (index == ControlFlyweightFactory.RADIO_BUTTON)
			return new RadioButton();
		else if (index == ControlFlyweightFactory.TEXT_BOX)
			return new TextBox();
		else if (index == ControlFlyweightFactory.TIME_CONTROL)
			return new TimeControl();
		else if (index == ControlFlyweightFactory.TREE_VIEW)
			return new TreeView();
		else if (index == ControlFlyweightFactory.WAV_PLAYER)
			return new WavPlayer();
		else if (index == ControlFlyweightFactory.AVAILABILITY_CONTROL)
			return new AvailabilityControl();
		else if (index == ControlFlyweightFactory.FLOOR_PLANNER)
			return new FloorPlanner();
		else if (index == ControlFlyweightFactory.DRAWING_CONTROL)
			return new DrawingControl();
		else if (index == ControlFlyweightFactory.HTML_VIEWER)
			return new HTMLViewer();
		else if (index == ControlFlyweightFactory.ANSWER_BOX)
			return new AnswerBox();
		else if (index == ControlFlyweightFactory.VIDEO_PLAYER)
			return new VideoPlayer();
		else if (index == ControlFlyweightFactory.TIME_LINE)
			return new TimeLineControl();
		else if (index == ControlFlyweightFactory.SEARCHABLE_COMBO_BOX)
			return new SearchableComboBox();
		else if (index == ControlFlyweightFactory.PARTIAL_DATE_BOX)
			return new PartialDateBox();
		else if (index == ControlFlyweightFactory.INT_RANGE_BOX)
			return new IntRangeBox();
		else if (index == ControlFlyweightFactory.DECIMAL_RANGE_BOX)
			return new DecimalRangeBox();
		else if (index == ControlFlyweightFactory.DRAWING_CONTROL_CONFIGURATOR)
			return new DrawingControlConfigurator();
		else if (index == ControlFlyweightFactory.BED_PLANNER)
			return new BedPlanner();
		else if (index == ControlFlyweightFactory.GRAPHING_CONTROL)
			return new GraphingControl();
		else if (index == ControlFlyweightFactory.BAR_CONTROL)
			return new BarControl();
		else if (index == ControlFlyweightFactory.DATETIME_CONTROL)
			return new DateTimeControl();
		else if (index == ControlFlyweightFactory.RICH_TEXT_BOX)
			return new RichTextControl();
		else if (index == ControlFlyweightFactory.FILE_UPLOADER)
			return new FileUploader();				
		else if (index == ControlFlyweightFactory.DYNAMIC_GRID)
			return new DynamicGrid();
		else if (index == ControlFlyweightFactory.CHECKED_LIST_BOX)
			return new CheckedListBox();
		else if (index == ControlFlyweightFactory.RECORD_BROWSER)
			return new RecordBrowser();
		else if (index == ControlFlyweightFactory.CUSTOM_COMPONENT)
			return new CustomComponent();
		else if (index == ControlFlyweightFactory.PATIENT_JOURNEY)
			return new PatientJourney();
		else if (index == ControlFlyweightFactory.DIARY)
			return new Diary();
		else if (index == ControlFlyweightFactory.PORTAL_DESIGNER)
			return new PortalDesigner();
		else if (index == ControlFlyweightFactory.GAUGE_CONTROL)
			return new GaugeControl();
		else if (index == ControlFlyweightFactory.DYNAMIC_FORM_CONTROL)
			return new DynamicForm();
		else if (index == ControlFlyweightFactory.CAMERA_CONTROL)
			return new CameraControl();
		else if (index == ControlFlyweightFactory.FORM_DESIGNER_CONTROL)
			return new FormDesignerControl();
		else if (index == ControlFlyweightFactory.HINT)
			return new Hint();
		
		throw new RuntimeException("Framework Internal Error: control with index " + index + " is not supported.");
	}

	// Singleton
	private ControlFlyweightFactory()
	{
	}
	public static ControlFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final ControlFlyweightFactory Singleton = new ControlFlyweightFactory();
	}
}