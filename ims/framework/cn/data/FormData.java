package ims.framework.cn.data;

import ims.framework.cn.controls.CameraControl;
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
import ims.framework.cn.controls.Container;
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
import ims.framework.cn.controls.Label;
import ims.framework.cn.controls.PatientJourney;
import ims.framework.cn.controls.Diary;
import ims.framework.cn.controls.GaugeControl;
import ims.framework.cn.controls.PortalDesigner;
import ims.framework.cn.controls.DynamicForm;

import java.util.ArrayList;

public class FormData implements java.io.Serializable
{	
	private static final long serialVersionUID = -3368347509778501545L;
	public boolean isEmpty()
	{
		return this.controlsData.size() == 0;
	}
	public void addMenuItem(IControlData value)
	{
		this.menus.add(value);
	}
	public IControlData getMenuItem(int i)
	{
		return this.menus.get(i);
	}
	public void addTimer(IControlData value)
	{
		this.timers.add(value);
	}
	public IControlData getTimer(int i)
	{
		return this.timers.get(i);
	}
	public void add(Class controlClass)
	{
		if (controlClass.equals(Button.class))
			this.controlsData.add(new ButtonData());
		else if (controlClass.equals(ImageButton.class))
			this.controlsData.add(new ImageButtonData());
		else if (controlClass.equals(Panel.class))
			this.controlsData.add(new PanelData());
		else if (controlClass.equals(TextBox.class))
			this.controlsData.add(new TextBoxData());
		else if (controlClass.equals(Label.class))
			this.controlsData.add(new LabelData());
		else if (controlClass.equals(ComboBox.class))
			this.controlsData.add(new ComboBoxData());
		else if (controlClass.equals(Grid.class))
			this.controlsData.add(new GridData());
		else if (controlClass.equals(DateControl.class))
			this.controlsData.add(new DateControlData());
		else if (controlClass.equals(HorizontalLine.class))
			this.controlsData.add(new HorizontalLineData());
		else if (controlClass.equals(CheckBox.class))
			this.controlsData.add(new CheckBoxData());
		else if (controlClass.equals(TimeControl.class))
			this.controlsData.add(new TimeControlData());
		else if (controlClass.equals(IntBox.class))
			this.controlsData.add(new IntBoxData());
		else if (controlClass.equals(RadioButton.class))
			this.controlsData.add(new RadioButtonData());
		else if (controlClass.equals(Container.class))
			this.controlsData.add(new ContainerData());
		else if (controlClass.equals(DecimalBox.class))
			this.controlsData.add(new DecimalBoxData());
		else if (controlClass.equals(TreeView.class))
			this.controlsData.add(new TreeViewData());
		else if (controlClass.equals(Link.class))
			this.controlsData.add(new LinkData());
		else if (controlClass.equals(AnswerBox.class))
			this.controlsData.add(new AnswerBoxData());
		else if (controlClass.equals(Picture.class))
			this.controlsData.add(new PictureData());
		else if (controlClass.equals(WavPlayer.class))
			this.controlsData.add(new WavPlayerData());
		else if (controlClass.equals(HTMLViewer.class))
			this.controlsData.add(new HTMLViewerData());
		else if (controlClass.equals(DrawingControl.class))
			this.controlsData.add(new DrawingControlData());
		else if (controlClass.equals(VideoPlayer.class))
			this.controlsData.add(new VideoPlayerData());
		else if (controlClass.equals(TimeLineControl.class))
			this.controlsData.add(new TimeLineControlData());
		else if (controlClass.equals(SearchableComboBox.class))
			this.controlsData.add(new SearchableComboBoxData());
		else if (controlClass.equals(BookingCalendar.class))
			this.controlsData.add(new BookingCalendarData());
		else if (controlClass.equals(AvailabilityControl.class))
			this.controlsData.add(new AvailabilityControlData());
		else if (controlClass.equals(PartialDateBox.class))
			this.controlsData.add(new PartialDateBoxData());
		else if (controlClass.equals(IntRangeBox.class))
			this.controlsData.add(new IntRangeBoxData());
		else if (controlClass.equals(DecimalRangeBox.class))
			this.controlsData.add(new DecimalRangeBoxData());
		else if (controlClass.equals(DrawingControlConfigurator.class))
			this.controlsData.add(new DrawingControlConfiguratorData());
		else if (controlClass.equals(FloorPlanner.class))
			this.controlsData.add(new FloorPlannerData());
		else if (controlClass.equals(BedPlanner.class))
			this.controlsData.add(new BedPlannerData());
		else if (controlClass.equals(GraphingControl.class))
			this.controlsData.add(new GraphingControlData());
		else if (controlClass.equals(BarControl.class))
			this.controlsData.add(new BarControlData());
		else if (controlClass.equals(DateTimeControl.class))
			this.controlsData.add(new DateTimeControlData());
		else if (controlClass.equals(RichTextControl.class))
			this.controlsData.add(new RichTextControlData());
		else if (controlClass.equals(FileUploader.class))
			this.controlsData.add(new FileUploaderData());
		else if (controlClass.equals(DynamicGrid.class))
			this.controlsData.add(new DynamicGridData());
		else if (controlClass.equals(CheckedListBox.class))
			this.controlsData.add(new CheckedListBoxData());
		else if (controlClass.equals(RecordBrowser.class))
			this.controlsData.add(new RecordBrowserData());
		else if (controlClass.equals(CustomComponent.class))
			this.controlsData.add(new CustomComponentData());
		else if (controlClass.equals(PatientJourney.class))
			this.controlsData.add(new PatientJourneyData());
		else if (controlClass.equals(Diary.class))
			this.controlsData.add(new DiaryData());
		else if (controlClass.equals(PortalDesigner.class))
			this.controlsData.add(new PortalDesignerData());
		else if (controlClass.equals(GaugeControl.class))
			this.controlsData.add(new GaugeControlData());
		else if (controlClass.equals(DynamicForm.class))
			this.controlsData.add(new DynamicFormData());
		else if (controlClass.equals(CameraControl.class))
			this.controlsData.add(new CameraControlData());
		else if (controlClass.equals(FormDesignerControl.class))
			this.controlsData.add(new FormDesignerControlData());
		else if (controlClass.equals(Hint.class))
			this.controlsData.add(new HintData());
	}
	public IControlData getData(int i)
	{
		return this.controlsData.get(i);
	}
	private ArrayList<IControlData> controlsData = new ArrayList<IControlData>();
	private ArrayList<IControlData> menus = new ArrayList<IControlData>();
	private ArrayList<IControlData> timers = new ArrayList<IControlData>();
}
