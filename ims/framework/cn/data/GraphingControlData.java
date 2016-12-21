package ims.framework.cn.data;

import ims.framework.utils.Color;
import ims.framework.utils.DateTime;
import ims.framework.utils.graphing.GraphingHighlight;
import ims.framework.utils.graphing.GraphingPoint;
import ims.framework.utils.graphing.GraphingPointType;
import ims.framework.utils.graphing.Options;

import java.util.ArrayList;

public class GraphingControlData implements IControlData
{	
	private static final long serialVersionUID = -5704240116836631820L;
	
	public class ColorBand
	{
		public float getFromValue()
		{
			return this.fromValue;
		}
		public float getToValue()
		{
			return this.toValue;
		}
		public Color getColor()
		{
			return this.color;
		}
		
		public ColorBand(float fromValue, float toValue, Color color)
		{
			this.fromValue = fromValue;
			this.toValue = toValue;
			this.color = color;
		}
		
		private float fromValue;
		private float toValue;
		private Color color;
	}
	public class Label
	{
		public float getValue()
		{
			return this.value;
		}
		public String getText()
		{
			return this.text;
		}
		
		public Label(float value, String text)
		{
			this.value = value;
			this.text = text;
		}
		
		private float value;
		private String text;
	}
	
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void addHighlightedY(GraphingHighlight value)
	{
		for(int x = 0; x < this.highlightedY.size(); x++)
		{
			if(this.highlightedY.get(x).equals(value))
			{
				this.highlightedYChanged = true;
				this.highlightedY.set(x, value);
				return;
			}
		}
		
		this.highlightedYChanged = true;
		this.highlightedY.add(value);
	}
	public ArrayList getHighlightedY()
	{
		return this.highlightedY;
	}
	public void clearAllHighlightedY()
	{
		if(!this.highlightedYChanged)
			this.highlightedYChanged = this.highlightedY.size() != 0;
		
		this.highlightedY.clear();
	}
	public void addPoint(GraphingPoint graphingPoint)
	{
		if(getPoint(graphingPoint.getID()) != null)
			return;
		
		this.pointsChanged = true;
		this.points.add(graphingPoint);
		this.needsCalculation = true;
	}
	public void clearAllPoints()
	{
		if(!this.pointsChanged)
			this.pointsChanged =this.points.size() != 0;
			
		this.points.clear();
		this.needsCalculation = true;		
	}
	public void clearPoints(GraphingPointType type)
	{
		for(int x = this.points.size() - 1; x >= 0; x--)
		{
			GraphingPoint point = this.points.get(x);
			if(point.getType().equals(type))
			{
				this.pointsChanged = true;			
				this.points.remove(point);
			}
		}
		
		this.needsCalculation = true;
	}
	public GraphingPoint getPoint(int id)
	{
		for(int x = 0; x < this.points.size(); x++)
		{
			GraphingPoint point = this.points.get(x);
			if(point.getID() == id)
				return point;
		}
		
		return null;
	}
	public ArrayList<GraphingPoint> getAllPoints()
	{
		return this.points;
	}
	public ArrayList<GraphingPoint> getAllPoints(GraphingPointType type)
	{
		ArrayList<GraphingPoint> filter = new ArrayList<GraphingPoint>();
		
		for(int x = 0; x < this.points.size(); x++)
		{
			GraphingPoint point = this.points.get(x);
			if(point.getType() == type)
				filter.add(point);
		}
		
		return filter;
	}
	public void setTitle(String value)
	{
		if(!this.titleChanged)
		{
			if(this.title == null)
				this.titleChanged = value != null;
			else
				this.titleChanged = !this.title.equals(value);
		}
			
		this.title = value;		
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setSubTitle(String value)
	{
		if(!this.subTitleChanged)
		{
			if(this.subTitle == null)
				this.subTitleChanged = value != null;
			else
				this.subTitleChanged = !this.subTitle.equals(value);
		}
		
		this.subTitle = value;		
	}
	public String getSubTitle()
	{
		return this.subTitle;
	}
	public void setNumberOfPointsY(int value)
	{
		if(!this.numberOfPointsYChanged)
			this.numberOfPointsYChanged = this.numberOfPointsY != value; 
		
		this.numberOfPointsY = value;		
	}
	public int getNumberOfPointsY()
	{
		return this.numberOfPointsY;
	}
	public int getNumberOfPointsX()
	{
		calculate();		
		return this.numberOfPointsX;
	}
	public void calculate()
	{
		if(!this.needsCalculation)
			return;
		
		if(!this.datesChanged)
			this.datesChanged = this.dates.size() != 0;
		this.dates.clear();
						
		// building the list of unique dates
		boolean exist = false;
		for(int x = 0; x < this.points.size(); x++)
		{
			GraphingPoint point = this.points.get(x);
			
			exist = false;
			for(int i = 0; i < this.dates.size(); i++)
			{
				if(point.getRecordingDateTime().equals((this.dates.get(i))))
				{
					exist = true;
					break;
				}
					
			}
			if(!exist)
			{
				this.datesChanged = true;			
				this.dates.add(point.getRecordingDateTime());
			}
		}
		
		this.numberOfPointsX = this.dates.size();	
		
		// sorting the list of dates
		sortDates();
		
		// sorting the points
		sortPoints();
		
		this.needsCalculation = false;
	}	
	public void setPrintHeaderInfo(String value)
	{
		if(!this.printHeaderInfoChanged)
		{
			if(this.printHeaderInfo == null)
				this.printHeaderInfoChanged = value != null;
			else
				this.printHeaderInfoChanged = !this.printHeaderInfo.equals(value);
		}
		
		this.printHeaderInfo = value;
	}
	public String getPrintHeaderInfo()
	{
		return this.printHeaderInfo == null ? "" : this.printHeaderInfo; 
	}	
	private void sortDates()
	{
		int size = this.dates.size();
		for(int x = 0; x < size; x++)
		{
			for(int i = x; i < size; i++)
			{
				DateTime xDate = this.dates.get(x);
				DateTime iDate = this.dates.get(i);
				
				if(xDate.getSeconds() > iDate.getSeconds())
				{			
					this.datesChanged = true;
					this.dates.set(x, iDate);
					this.dates.set(i, xDate);					
				}
				
			}
		}
	}
	private void sortPoints()
	{
		int size = this.points.size();
		for(int x = 0; x < size; x++)
		{
			for(int i = x; i < size; i++)
			{
				GraphingPoint xPoint = this.points.get(x);
				GraphingPoint iPoint = this.points.get(i);
				
				if(xPoint.getRecordingDateTime().getSeconds() > iPoint.getRecordingDateTime().getSeconds())
				{				
					this.pointsChanged = true;
					this.points.set(x, iPoint);
					this.points.set(i, xPoint);					
				}
				
			}
		}
	}
	public long getPointOnX(GraphingPoint point)
	{
		return getPointOnX(point.getID());
	}
	public long getPointOnX(int pointID)
	{
		calculate();
		
		long startX = this.getMinimumDateTime().getSeconds();		
		
		for(int x = 0; x < this.points.size(); x++)
		{
			GraphingPoint point = this.points.get(x); 
			if(point.getID() == pointID)
			{
				if(point.getRecordingDateTime().getSeconds() - startX == 0)
					return 0;
						
				return (point.getRecordingDateTime().getSeconds() - startX);
			}
		}
		
		// This should never happen, if it does it's bad coding
		return 0; 
	}	
	public long getMaxPointX()
	{
		calculate();
		
		if(this.getNumberOfPointsX() == 0)
			return 0;
		
		long startX = this.getMinimumDateTime().getSeconds();	
		long endX = this.getMaximumDateTime().getSeconds();
		
		return endX - startX; 
	}	
	public GraphingPoint getFirstPoint()
	{
		if(this.points.size() == 0)
			return null;
		
		calculate();
		
		return this.points.get(0);
	}	
	public GraphingPoint getLastPoint()
	{
		if(this.points.size() == 0)
			return null;
		
		calculate();
		
		return this.points.get(this.points.size() - 1);
	}	
	public DateTime getMinimumDateTime()
	{		
		int size = this.points.size();
		if(size == 0)
			return null;
		
		DateTime minDateTime = null;
		for(int x = 0; x < size; x++)
		{
			DateTime toCompare = this.points.get(x).getRecordingDateTime();
			
			if(minDateTime == null || minDateTime.getSeconds() > toCompare.getSeconds())
				minDateTime = toCompare;
		}
		
		return minDateTime;
	}	
	public DateTime getMaximumDateTime()
	{
		int size = this.points.size();
		if(size == 0)
			return null;
		
		DateTime maxDateTime = null;
		for(int x = 0; x < size; x++)
		{
			DateTime toCompare = this.points.get(x).getRecordingDateTime();
			
			if(maxDateTime == null || maxDateTime.getSeconds() < toCompare.getSeconds())
				maxDateTime = toCompare;
		}
		
		return maxDateTime;
	}	
	public void showLegend(boolean value)
	{
		if(!this.showLegendChanged)
			this.showLegendChanged = this.showLegend != value;
		
		this.showLegend = value;
	}
	public boolean legendIsShown()
	{
		return this.showLegend;
	}	
	public void showYAxisValues(boolean value)
	{
		if(!this.showYAxisValuesChanged)
			this.showYAxisValuesChanged = this.showYAxisValues != value;
		
		this.showYAxisValues = value;
		this.showYAxisValuesChanged  = value;
	}
	public boolean yAxisValuesAreShown()
	{
		return this.showYAxisValues;
	}
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setTitleUnchanged()
	{
		this.titleChanged = false;
	}
	public boolean isTitleChanged()
	{
		return titleChanged;
	}
	public void setPointsUnchanged()
	{
		this.pointsChanged = false;
	}
	public boolean isPointsChanged()
	{
		return pointsChanged;
	}
	public void setDatesUnchanged()
	{
		this.datesChanged = false;
	}
	public boolean isDatesChanged()
	{
		return datesChanged;
	}
	public void setSubTitleUnchanged()
	{
		this.subTitleChanged = false;
	}
	public boolean isSubTitleChanged()
	{
		return subTitleChanged;
	}
	public void setNumberOfPointsYUnchanged()
	{
		this.numberOfPointsYChanged = false;
	}
	public boolean isNumberOfPointsYChanged()
	{
		return numberOfPointsYChanged;
	}
	public void setHighlightedYUnchanged()
	{
		this.highlightedYChanged = false;
	}
	public boolean isHighlightedYChanged()
	{
		return highlightedYChanged;
	}
	public void setShowLegendUnchanged()
	{
		this.showLegendChanged = false;
	}
	public boolean isShowLegendChanged()
	{
		return showLegendChanged;
	}
	public void setPrintHeaderInfoUnchanged()
	{
		this.printHeaderInfoChanged = false;
	}
	public boolean isPrintHeaderInfoChanged()
	{
		return printHeaderInfoChanged;
	}
	public void setOptionsUnchanged()
	{
		options.markUnchanged();
	}
	public boolean isOptionsChanged()
	{
		return options.wasChanged();
	}
	public void setShowYAxisValuesUnchanged()
	{
		this.showYAxisValuesChanged = false;
	}
	public boolean isShowYAxisValuesChanged()
	{
		return showYAxisValuesChanged;
	}
	public void addColorBandY(float fromValue, float toValue, Color color)	
	{		
		this.colorBandsYChanged = true;
		this.colorBandsY.add(new ColorBand(fromValue, toValue, color));
	}
	public void clearAllColorBandsY()
	{
		if(!this.colorBandsYChanged)
			this.colorBandsYChanged = this.colorBandsY.size() != 0;
		this.colorBandsY.clear();
	}
	public void addLabelY(float value, String text)
	{
		this.labelsYChanged = true;
		this.labelsY.add(new Label(value, text));
	}
	public void clearAllLabelsY()
	{
		if(!this.labelsYChanged)
			this.labelsYChanged = this.labelsY.size() != 0;
		
		this.labelsY.clear();
	}
	public ColorBand[] getColorBandsY()
	{
		ColorBand[] result = new ColorBand[this.colorBandsY.size()];
		for(int x = 0; x < this.colorBandsY.size(); x++)
		{
			result[x] = this.colorBandsY.get(x);
		}
		return result;
	}
	public Label[] getLabelsY()
	{
		Label[] result = new Label[this.labelsY.size()];
		for(int x = 0; x < this.labelsY.size(); x++)
		{
			result[x] = this.labelsY.get(x);
		}
		return result;
	}
	public void setColorBandsYUnchanged()
	{
		this.colorBandsYChanged = false;
	}
	public boolean isColorBandsYChanged()
	{
		return colorBandsYChanged;
	}
	public void setLabelsYUnchanged()
	{
		this.labelsYChanged = false;
	}
	public boolean isLabelsYChanged()
	{
		return labelsYChanged;
	}

	public Options options = new Options();
		
	private boolean enabled = true;
	private boolean visible = true;	
	private String title = null;
	private String subTitle = null;
	private int numberOfPointsX;
	private int numberOfPointsY;	
	private ArrayList<GraphingPoint> points = new ArrayList<GraphingPoint>();	
	private ArrayList<DateTime> dates = new ArrayList<DateTime>();
	private ArrayList<GraphingHighlight> highlightedY = new ArrayList<GraphingHighlight>();
	private boolean needsCalculation = true;	
	private boolean showLegend;
	private String printHeaderInfo = null; 
	private boolean showYAxisValues = true;
	private ArrayList<ColorBand> colorBandsY = new ArrayList<ColorBand>();
	private ArrayList<Label> labelsY = new ArrayList<Label>();
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean titleChanged = false;
	private boolean pointsChanged = false;
	private boolean datesChanged = false;
	private boolean subTitleChanged = false;
	private boolean numberOfPointsYChanged = false;
	private boolean highlightedYChanged = false;
	private boolean showLegendChanged = false;
	private boolean showYAxisValuesChanged = false;
	private boolean printHeaderInfoChanged = false;	
	private boolean colorBandsYChanged = false;
	private boolean labelsYChanged = false;
}
