package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.GraphingControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.GraphingControlClicked;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.DateTime;
import ims.framework.utils.StringUtils;
import ims.framework.utils.graphing.GraphingBloodPressure;
import ims.framework.utils.graphing.GraphingBloodSugar;
import ims.framework.utils.graphing.GraphingCustomOneValue;
import ims.framework.utils.graphing.GraphingGlasgowComaScale;
import ims.framework.utils.graphing.GraphingGroup;
import ims.framework.utils.graphing.GraphingHighlight;
import ims.framework.utils.graphing.GraphingMetrics;
import ims.framework.utils.graphing.GraphingOxygen;
import ims.framework.utils.graphing.GraphingPeakFlow;
import ims.framework.utils.graphing.GraphingPoint;
import ims.framework.utils.graphing.GraphingPointType;
import ims.framework.utils.graphing.GraphingPulse;
import ims.framework.utils.graphing.GraphingPupils;
import ims.framework.utils.graphing.GraphingRespiration;
import ims.framework.utils.graphing.GraphingTemperature;
import ims.framework.utils.graphing.Options;

public class GraphingControl extends ims.framework.controls.GraphingControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String title, String subTitle, float minY, float maxY, int numberOfPointsY, int numberOfLabelsX, String labelX, String labelY, boolean showLegend)
	{		
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.minY = minY;
		this.maxY = maxY;
		this.title = title;
		this.subTitle = subTitle;
		this.numberOfPointsY = numberOfPointsY;
		this.numberOfLabelsX = numberOfLabelsX;
		this.labelX = labelX;
		this.labelY = labelY;
		this.showLegend = showLegend;		
	}
	protected void free()
	{
		super.free();
		
		this.title = null;
		this.subTitle = null;
		this.labelX = null;
		this.labelY = null;
		this.data = null;		
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (GraphingControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();	
		
		if(isNew)
		{
			this.data.setTitle(this.title);
			this.data.setSubTitle(this.subTitle);
			this.data.setNumberOfPointsY(this.numberOfPointsY);
			this.data.showLegend(this.showLegend);
		}
		else
		{
			this.title = this.data.getTitle();
			this.subTitle = this.data.getSubTitle();
			this.numberOfPointsY = this.data.getNumberOfPointsY();
			this.showLegend = this.data.legendIsShown(); 
		}
	}	
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof GraphingControlClicked)
		{
			if(super.graphingControlClickDelegate != null)
			{
				super.graphingControlClickDelegate.handle(getPoint((((GraphingControlClicked)event).getPointID())));
			}
			
			return true;
		}
		
		return false;
	}
	public void getSerializedControl(StringBuffer sb) throws ConfigurationException
	{
		this.renderControl(sb);
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		this.data.calculate();
		
		sb.append("<graphingcontrol id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"-1");
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\">");
		renderYAxis(sb, false);
		sb.append("</graphingcontrol>");
	}

	public void getSerializedData(StringBuffer sb) throws ConfigurationException
	{
		this.renderData(sb);
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<graphingcontrol id=\"a");
		sb.append(super.id);
		
		//if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				//if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledUnchanged();
				}
			}
		
			//if(this.data.isTitleChanged())
			{
				sb.append("\" title=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(this.title));
				this.data.setTitleUnchanged();
			}
			//if(this.data.isSubTitleChanged())
			{
				sb.append("\" subTitle=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(this.subTitle));
				this.data.setSubTitleUnchanged();
			}
			
			//if(this.data.isPrintHeaderInfoChanged())
			{
				sb.append("\" printHeaderInfo=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getPrintHeaderInfo()));
				this.data.setPrintHeaderInfoUnchanged();
			}
			
			sb.append("\">");
		
			//if(this.data.isShowLegendChanged())
			{
				// adding the legend if needed
				if(this.showLegend)
					sb.append(getLegend());
				else
					sb.append("<legend/>");
				
				this.data.setShowLegendUnchanged();
			}
		
			long addition = this.data.getMaxPointX() * this.data.options.getAdditionalPercent() / 100;
			long minX = (-1 * addition);
			long maxX = (this.data.getMaxPointX() + addition);
			
			if((maxX - minX) < this.data.options.getMinimumXRangeAddition())
			{
				addition = this.data.options.getMinimumXRangeAddition();
				minX = (-1 * addition);
				maxX = (this.data.getMaxPointX() + addition);
			}	
			
			if(this.data.getNumberOfPointsX() < this.numberOfLabelsX && this.data.getNumberOfPointsX() > 1)
			{
				addition = this.data.getMaxPointX() * 15 / 100;
				minX = (-1 * addition);
				maxX = (this.data.getMaxPointX() + addition);
			}
			
			if(addition == 0)
				maxX = 1;
			
			renderYAxis(sb, true);
			
			//sb.append("<xaxis from=\"" + minX + "\" to=\"" + maxX + "\" visibleRange=\"" + (minX + maxX) + "\"/>");
			sb.append("<xaxis from=\"" + minX + "\" to=\"" + maxX + "\" />");
				 
			// adding the labels
			sb.append(this.getXLabels(minX, maxX));
			
			// draws the points
			sb.append(this.getGraphs());
			
			// draws the custom points
			sb.append(this.getCustomGraphs());
			
			//if(this.data.isPrintHeaderInfoChanged())
			{
				// adds the printing information			
				sb.append(this.getPrintInfo());
				this.data.setPrintHeaderInfoUnchanged();
			}
			
			sb.append("</graphingcontrol>");
		}
		else
		{
			sb.append("\" />");
		}
	}
	public void renderGraphingHighlight(StringBuffer sb, GraphingHighlight highlight)
	{
		if(highlight == null)
			return;
		
		sb.append("<highlight y=\"");
		sb.append(highlight.getValue());
		
		if(highlight.getColor() != null)
		{
			sb.append("\" color=\"");
			sb.append(highlight.getColor());
		}
		
		if(highlight.getWeight() != 2.4)
		{
			sb.append("\" weight=\"");
			sb.append(highlight.getWeight());
		}
		
		sb.append("\" />");
	}
	public void renderYAxis(StringBuffer sb, boolean forData)
	{
		if(forData)
		{
			if(!this.data.isShowYAxisValuesChanged() && !this.data.isHighlightedYChanged() && !this.data.isLabelsYChanged() && !this.data.isColorBandsYChanged())
				return;
			
			sb.append("<yaxis");	
			if(this.data.isShowYAxisValuesChanged())
			{
				sb.append(" showNumericValues=\"" + (this.data.yAxisValuesAreShown() ? "true" : "false"));
				sb.append("\"");
				this.data.setShowYAxisValuesUnchanged();
			}
			
			sb.append(">");
			
			if(this.data.isHighlightedYChanged())
			{
				if(this.data.getHighlightedY().size() == 0)
				{
					sb.append("<nohighlights/>");
				}
				else
				{
					for(int x = 0; x < this.data.getHighlightedY().size(); x++)
					{
						renderGraphingHighlight(sb, (GraphingHighlight)this.data.getHighlightedY().get(x));
					}
				}
				
				this.data.setHighlightedYUnchanged();
			}
			
			if(this.data.isLabelsYChanged())
			{
				if(this.data.getLabelsY().length == 0)
				{
					sb.append("<nolabels/>");
				}
				else
				{
					for(int x = 0; x < this.data.getLabelsY().length; x++)
					{				
						sb.append("<label y=\"" + this.data.getLabelsY()[x].getValue() + "\" text=\"" + StringUtils.encodeXML(this.data.getLabelsY()[x].getText()) + "\"/>");
					}
				}
				
				this.data.setLabelsYUnchanged();
			}
			
			if(this.data.isColorBandsYChanged())
			{
				if(this.data.getColorBandsY().length == 0)
				{
					sb.append("<nobands/>");
				}
				else
				{
					for(int x = 0; x < this.data.getColorBandsY().length; x++)
					{				
						sb.append("<band fromY=\"" + this.data.getColorBandsY()[x].getFromValue() + "\" toY=\"" + this.data.getColorBandsY()[x].getToValue() + "\" color=\"" + this.data.getColorBandsY()[x].getColor() + "\"/>");
					}
				}
				
				this.data.setColorBandsYUnchanged();
			}
			
			sb.append("</yaxis>");
		}
		else
		{
			sb.append("<yaxis caption=\"" + this.labelY + "\" from=\"" + this.minY + "\" to=\"" + this.maxY + "\" incremental=\"" + this.getYStep() + "\" pointsInIncremental=\"" + this.getYNumberOfPointsInIncremental() + "\" showNumericValues=\"" + (this.data.yAxisValuesAreShown() ? "true" : "false") + "\">");
			
			for(int x = 0; x < this.data.getHighlightedY().size(); x++)
			{
				renderGraphingHighlight(sb, (GraphingHighlight)this.data.getHighlightedY().get(x));
			}
			sb.append("</yaxis>");
		}		
	}
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}	
	public void addHighlightedY(float value)
	{
		addHighlightedY(new GraphingHighlight(value));
	}
	public void addHighlightedY(GraphingHighlight value)
	{
		if(value == null)
			throw new CodingRuntimeException("Invalid highlight");
		
		if(value.getValue() >= this.minY && value.getValue() <= this.maxY)
			this.data.addHighlightedY(value);
	}
	public void clearAllHighlightedY()
	{
		this.data.clearAllHighlightedY();
	}	
	public void addColorBandY(float fromValue, float toValue, Color color)
	{
		this.data.addColorBandY(fromValue, toValue, color);
	}
	public void clearAllColorBandsY()
	{
		this.data.clearAllColorBandsY();
	}
	public void addLabelY(float value, String text)
	{
		this.data.addLabelY(value, text);
	}
	public void clearAllLabelsY()
	{
		this.data.clearAllLabelsY();
	}
	public void showYAxisValues()
	{
		this.data.showYAxisValues(true);
	}
	public void showYAxisLabels()
	{
		this.data.showYAxisValues(false);
	}
	public void addPoint(GraphingPoint graphingPoint)
	{
		this.data.addPoint(graphingPoint);
	}
	public void addPoints(GraphingPoint[] graphingPoints)
	{
		for(int x = 0; x < graphingPoints.length; x++)
			this.data.addPoint(graphingPoints[x]);
	}
	public void clearAllPoints()
	{
		this.data.clearAllPoints();
	}
	public void clearPoints(GraphingPointType type)
	{
		this.data.clearPoints(type);		
	}	
	public void setPrintHeaderInfo(String info)
	{
		this.data.setPrintHeaderInfo(info);
	}	
	protected GraphingPoint getPoint(int id)
	{
		return this.data.getPoint(id);
	}	
	public void setTitle(String title)
	{
		this.title = title;
		this.data.setTitle(title);
	}	
	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
		this.data.setSubTitle(subTitle);
	}	
	public void setNumberOfPointsY(int numberOfPointsY)
	{
		this.numberOfPointsY = numberOfPointsY;
		this.data.setNumberOfPointsY(numberOfPointsY); 
	}	
	public void showLegend(boolean value)
	{
		this.showLegend = value;
		this.data.showLegend(value);
	}
	public boolean legendIsShown()
	{
		return this.showLegend;
	}				
	private float getYStep()
	{
		return (this.maxY - this.minY) / this.numberOfPointsY;
	}	
	private int getYNumberOfPointsInIncremental()
	{
		// using default value until we can calculate this...
		return 5;  
	}
	public Options graphingOptions()
	{
		return this.data.options;
	}
	private StringBuffer getPrintInfo()
	{
		int index = 0;
		ArrayList points = new ArrayList(); 
		StringBuffer printInfo = new StringBuffer();	
		
		printInfo.append("<printInfo>");
		
		// Temperature
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.TEMPERATURE);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.TEMPERATURE + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Pulse
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.PULSE);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.PULSE + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Respiration
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.RESPIRATION);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.RESPIRATION + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Blood Pressure
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.BLOODPRESSURE);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.BLOODPRESSURE + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Glasgow Coma Scale
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.GLASGOWCOMASCALE);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.GLASGOWCOMASCALE + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Oxygen
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.OXYGEN);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.OXYGEN + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}

		// Blood Sugar
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.BLOODSUGAR);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.BLOODSUGAR + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Metrics
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.METRICS);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.METRICS + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Peak Flow
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.PEAKFLOW);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.PEAKFLOW + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		// Pupils
		index = 0;
		points = this.data.getAllPoints(GraphingPointType.PUPILS);
		if(points.size() > 0)
		{
			printInfo.append("<group title=\"" + GraphingPointType.PUPILS + "\">");
			
			for(int x = 0; x < points.size(); x++)
			{
				GraphingPoint castPoint = (GraphingPoint)points.get(x);
				printInfo.append("<info value=\"" + ++index + ". " + ims.framework.utils.StringUtils.encodeXML(castPoint.getPrintInfo()) + "\"/>");
			}
			
			printInfo.append("</group>");
		}
		
		printInfo.append("</printInfo>");
		
		return printInfo;
	}
	private StringBuffer getLegend()
	{
		StringBuffer legend = new StringBuffer();				
		
		ArrayList standardTypes = getExistingStandardTypes();
		GraphingGroup[] groups = getAllGroups();
		if(standardTypes.size() == 0 && groups.length == 0)
		{
			legend.append("<legend />");
			return legend;		
		}
				
		legend.append("<legend>");
		
		for(int x = 0; x < standardTypes.size(); x++)
		{
			GraphingPointType type = (GraphingPointType)standardTypes.get(x);
			
			if(type == GraphingPointType.TEMPERATURE)
			{
				legend.append("<item img=\"" + GraphingTemperature.getImageUrl() + "\" caption=\"" + type + "\"/>");
			}
			if(type == GraphingPointType.PULSE)
			{
                if(this.data.options.pulse.getDisplayRadial())
                    legend.append("<item img=\"" + GraphingPulse.getRadialImageUrl() + "\" caption=\"" + "Radial " + type + "\"/>");
                if(this.data.options.pulse.getDisplayApex())
                    legend.append("<item img=\"" + GraphingPulse.getApexImageUrl() + "\" caption=\"" + "Apex " + type + "\"/>");
			}
			if(type == GraphingPointType.PEAKFLOW)
			{
                if(this.data.options.peakFlow.getDisplayPreValue())
                    legend.append("<item img=\"" + GraphingPeakFlow.getPreImageUrl() + "\" caption=\"" + "Pre " + type + "\"/>");
                if(this.data.options.peakFlow.getDisplayPostValue())
                    legend.append("<item img=\"" + GraphingPeakFlow.getPostImageUrl() + "\" caption=\"" + "Post " + type + "\"/>");
			}
			if(type == GraphingPointType.BLOODSUGAR)
			{
				if(this.data.options.bloodSugar.getDisplayPreValue())
					legend.append("<item img=\"" + GraphingBloodSugar.getPreValueImageUrl() + "\" caption=\"" + "CBG Pre Value\"/>");
				if(this.data.options.bloodSugar.getDisplayPostValue())
					legend.append("<item img=\"" + GraphingBloodSugar.getPostValueImageUrl() + "\" caption=\"" + "CBG Post Value\"/>");
				if(this.data.options.bloodSugar.getDisplayValue())
					legend.append("<item img=\"" + GraphingBloodSugar.getValueImageUrl() + "\" caption=\"" + "Random Blood Glucose\"/>");				
			}
			if(type == GraphingPointType.METRICS)
			{
				if(this.data.options.metrics.getDisplayHeight())
					legend.append("<item img=\"" + GraphingMetrics.getHeightImageUrl() + "\" caption=\"" + type + " Height\" />");
				if(this.data.options.metrics.getDisplayWeight())
					legend.append("<item img=\"" + GraphingMetrics.getWeightImageUrl() + "\" caption=\"" + type + " Weight\"/>");
				if(this.data.options.metrics.getDisplayBMI())
					legend.append("<item img=\"" + GraphingMetrics.getBMIImageUrl() + "\" caption=\"" + type + " BMI\"/>");
			}
			if(type == GraphingPointType.PUPILS)
			{
				if(this.data.options.pupils.getDisplayLeft())
					legend.append("<item img=\"" + GraphingPupils.getLeftImageUrl() + "\" caption=\"" + type + " Left\" />");
				if(this.data.options.pupils.getDisplayRight())
					legend.append("<item img=\"" + GraphingPupils.getRightImageUrl() + "\" caption=\"" + type + " Right\"/>");
			}
			if(type == GraphingPointType.RESPIRATION)
			{
				legend.append("<item img=\"" + GraphingRespiration.getImageUrl() + "\" caption=\"" + type + "\"/>");
			}
			if(type == GraphingPointType.OXYGEN)
			{
				legend.append("<item img=\"" + GraphingOxygen.getImageUrl() + "\" caption=\"" + type + "\"/>");
			}
			if(type == GraphingPointType.BLOODPRESSURE)
			{				
				if(this.data.options.bloodPressure.getDisplayStanding())
				{
					legend.append("<item img=\"" + GraphingBloodPressure.getStandingSysImageUrl() + "\" caption=\"" + type + " Standing Systolic\"/>");
					legend.append("<item img=\"" + GraphingBloodPressure.getStandingDiaImageUrl() + "\" caption=\"" + type + " Standing Diastolic\"/>");
				}
				if(this.data.options.bloodPressure.getDisplaySitting())
				{
					legend.append("<item img=\"" + GraphingBloodPressure.getSittingSysImageUrl() + "\" caption=\"" + type + " Sitting Systolic\"/>");
					legend.append("<item img=\"" + GraphingBloodPressure.getSittingDiaImageUrl() + "\" caption=\"" + type + " Sitting Diastolic\"/>");
				}
				if(this.data.options.bloodPressure.getDisplayLying())
				{
					legend.append("<item img=\"" + GraphingBloodPressure.getLyingSysImageUrl() + "\" caption=\"" + type + " Lying Systolic\"/>");
					legend.append("<item img=\"" + GraphingBloodPressure.getLyingDiaImageUrl() + "\" caption=\"" + type + " Lying Diastolic\"/>");
				}
			}
			if(type == GraphingPointType.GLASGOWCOMASCALE)
			{
				if(this.data.options.glasgowComaScale.getDisplayVerbal())
					legend.append("<item img=\"" + GraphingGlasgowComaScale.getVerbalImageUrl() + "\" caption=\"" + type + " Verbal\"/>");
				if(this.data.options.glasgowComaScale.getDisplayMotor())
					legend.append("<item img=\"" + GraphingGlasgowComaScale.getMotorImageUrl() + "\" caption=\"" + type + " Motor\"/>");
				if(this.data.options.glasgowComaScale.getDisplayEye())
					legend.append("<item img=\"" + GraphingGlasgowComaScale.getEyeImageUrl() + "\" caption=\"" + type + " Eye\"/>");
				if(this.data.options.glasgowComaScale.getDisplayTotal())
					legend.append("<item img=\"" + GraphingGlasgowComaScale.getTotalImageUrl() + "\" caption=\"" + type + " Total\"/>");
			}
		}			
		
		for(int x = 0; x < groups.length; x++)
		{
			legend.append("<item img=\"" + groups[x].getImage().getImagePath() + "\" caption=\"" + groups[x].getName() + " \" tooltip=\"" + StringUtils.encodeXML(groups[x].getLegendTooltip()) + "\"/>");
		}
			
		legend.append("</legend>");
		
		return legend;
	}
	private StringBuffer getXLabels(long minX, long maxX)
	{
		boolean lessPointsThanLabels = false;
		StringBuffer xlabels = new StringBuffer();
		xlabels.append("<xaxisLabels caption=\"" + ims.framework.utils.StringUtils.encodeXML(this.labelX) + "\"");
	
		if(this.data.getNumberOfPointsX() == 0 || maxX == minX)
		{
			xlabels.append("/>");
			return xlabels;
		}
		
		xlabels.append(">");
						
		int noOfLabels = this.numberOfLabelsX;
		if(noOfLabels > this.data.getNumberOfPointsX())
		{
			lessPointsThanLabels = true;
			noOfLabels = this.data.getNumberOfPointsX();
		}
		
		if(lessPointsThanLabels && this.data.getNumberOfPointsX() > 1) // not an ideal solution...
		{			
			GraphingPoint first = this.data.getFirstPoint();
			GraphingPoint last = this.data.getLastPoint();
			
			xlabels.append("<label x=\"" + (this.data.getPointOnX(first)) + "\" value=\"" + first.getRecordingDateTime() + "\"/>");
			xlabels.append("<label x=\"" + (this.data.getPointOnX(last)) + "\" value=\"" + last.getRecordingDateTime() + "\"/>");
			xlabels.append("</xaxisLabels>");
			
			return xlabels;
		}
		
		GraphingPoint firstPoint = this.data.getFirstPoint();
		long firstPointXPos = this.data.getPointOnX(firstPoint);
		//long firstPointSeconds = firstPoint.getRecordingDateTime().getSeconds();
		long seconds = 0;
		DateTime date = firstPoint.getRecordingDateTime().copy();
		long oldPos = 0;
		long stepX = (maxX - minX) / (noOfLabels + 1);
		
		for(int x = 1; x <= noOfLabels; x++)
		{
			long posX = (stepX * x) + minX;
			
			if(x == 1)
			{
				seconds = posX - firstPointXPos;
				if(seconds < 0)
					seconds = -1 * seconds;
				oldPos = seconds;
			}
			if(x == 2)
				seconds = posX - oldPos;
			
			date.addSeconds(seconds);
			
			xlabels.append("<label x=\"" + (posX) + "\" value=\"" + date.toString() + "\"/>");
		}
		
		xlabels.append("</xaxisLabels>");
		
		return xlabels;
	}
	private StringBuffer getGraphs()
	{		
		StringBuffer graphs = new StringBuffer();		
		
		ArrayList existingTypes = getExistingStandardTypes(); 		
		for(int x = 0; x < existingTypes.size(); x++)
		{
			GraphingPointType type = (GraphingPointType)existingTypes.get(x);
			
			if(type == GraphingPointType.TEMPERATURE)
				graphs.append(getTemperatureGraph());
			else if(type == GraphingPointType.METRICS)
				graphs.append(getMetricsGraph());
			else if(type == GraphingPointType.PEAKFLOW)
				graphs.append(getPeakFlowGraph());
			else if(type == GraphingPointType.BLOODSUGAR)
				graphs.append(getBloodSugarGraph());
			else if(type == GraphingPointType.PUPILS)
				graphs.append(getPupilsGraph());
			else if(type == GraphingPointType.RESPIRATION)
				graphs.append(getRespirationGraph());
			else if(type == GraphingPointType.OXYGEN)
				graphs.append(getOxygenGraph());
			else if(type == GraphingPointType.PULSE)
				graphs.append(getPulseGraph());
			else if(type == GraphingPointType.BLOODPRESSURE)
				graphs.append(getBloodPressureGraph());
			else if(type == GraphingPointType.GLASGOWCOMASCALE)
				graphs.append(getGlasgowComaScaleGraph());			
		}
		
		// nothing to display
		if(graphs.length() == 0)
			graphs.append("<graph/>");
		
		return graphs;
	}
	private StringBuffer getCustomGraphs()
	{
		StringBuffer result = new StringBuffer();
		
		GraphingGroup[] groups = getAllGroups();
		for(int x = 0; x < groups.length; x++)
		{
			result.append(getCustomOneValueGraph(groups[x]));
		}
		
		return result;
	}
	private StringBuffer getMetricsGraph()
	{
		StringBuffer graph = new StringBuffer();
		StringBuffer graphWeight = new StringBuffer();
		StringBuffer graphHeight = new StringBuffer();
		StringBuffer graphBMI = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.METRICS);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{				
			GraphingMetrics castPoint = (GraphingMetrics)allPoints.get(x);
				
			if(!startAdded)
			{
				graphWeight.append("<graph color=\"" + GraphingMetrics.getWeightLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphHeight.append("<graph color=\"" + GraphingMetrics.getHeightLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphBMI.append("<graph color=\"" + GraphingMetrics.getBMILineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}

			if(castPoint.getWeightValue() != null)
				graphWeight.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getWeightValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingMetrics.getWeightImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getHeightValue() != null)
				graphHeight.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getHeightValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingMetrics.getHeightImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getBMIValue() != null)
				graphBMI.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getBMIValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingMetrics.getBMIImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
		}
		
		graphWeight.append("</graph>");
		graphHeight.append("</graph>");
		graphBMI.append("</graph>");
		
		if(this.data.options.metrics.getDisplayWeight())
			graph.append(graphWeight);
		if(this.data.options.metrics.getDisplayHeight())
			graph.append(graphHeight);
		if(this.data.options.metrics.getDisplayBMI())
			graph.append(graphBMI);
		
		return graph;
	}
	private StringBuffer getPupilsGraph()
	{
		StringBuffer graph = new StringBuffer();
		StringBuffer graphLeft = new StringBuffer();
		StringBuffer graphRight = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.PUPILS);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingPupils castPoint = (GraphingPupils)allPoints.get(x);
				
			if(!startAdded)
			{
				graphLeft.append("<graph color=\"" + GraphingPupils.getLeftLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphRight.append("<graph color=\"" + GraphingPupils.getRightLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getLeftSize() != null)
				graphLeft.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getLeftSize() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPupils.getLeftImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getRightSize() != null)
				graphRight.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getRightSize() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPupils.getRightImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
		}
		
		graphLeft.append("</graph>");
		graphRight.append("</graph>");
		
		if(this.data.options.pupils.getDisplayLeft())
			graph.append(graphLeft);
		if(this.data.options.pupils.getDisplayRight())
			graph.append(graphRight);
		
		return graph;
	}
	private StringBuffer getPeakFlowGraph()
	{
		StringBuffer graph = new StringBuffer();
        StringBuffer graphPre = new StringBuffer();
        StringBuffer graphPost = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.PEAKFLOW);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingPeakFlow castPoint = (GraphingPeakFlow)allPoints.get(x);
				
			if(!startAdded)
			{
                graphPre.append("<graph color=\"" + GraphingPeakFlow.getPreLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
                graphPost.append("<graph color=\"" + GraphingPeakFlow.getPostLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getPreValue() != null)
				graphPre.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getPreValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPeakFlow.getPreImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");				            
            if(castPoint.getPostValue() != null)
                graphPost.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getPostValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPeakFlow.getPostImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
		}
		
        graphPre.append("</graph>");
        graphPost.append("</graph>");
		
        if(this.data.options.peakFlow.getDisplayPreValue())
            graph.append(graphPre);
        if(this.data.options.peakFlow.getDisplayPostValue())
            graph.append(graphPost);
		
		return graph;
	}
	private StringBuffer getBloodSugarGraph()
	{
		StringBuffer graph = new StringBuffer();
		StringBuffer graphPreValue = new StringBuffer();
		StringBuffer graphValue = new StringBuffer();
		StringBuffer graphPostValue = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.BLOODSUGAR);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingBloodSugar castPoint = (GraphingBloodSugar)allPoints.get(x);
				
			if(!startAdded)
			{
				graphPreValue.append("<graph color=\"" + GraphingBloodSugar.getPreValueLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");				
				graphPostValue.append("<graph color=\"" + GraphingBloodSugar.getPostValueLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphValue.append("<graph color=\"" + GraphingBloodSugar.getValueLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getPreValue() != null)
				graphPreValue.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getPreValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodSugar.getPreValueImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			if(castPoint.getPostValue() != null)
				graphPostValue.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getPostValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodSugar.getPostValueImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			if(castPoint.getValue() != null)
				graphValue.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodSugar.getValueImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");							
		}
				
		graphPreValue.append("</graph>");
		graphPostValue.append("</graph>");
		graphValue.append("</graph>");
		
		if(this.data.options.bloodSugar.getDisplayPreValue())
            graph.append(graphPreValue);
		if(this.data.options.bloodSugar.getDisplayPostValue())
            graph.append(graphPostValue);
		if(this.data.options.bloodSugar.getDisplayValue())
            graph.append(graphValue);        
		
		return graph;
	}
	private StringBuffer getTemperatureGraph()
	{
		StringBuffer graph = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.TEMPERATURE);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingTemperature castPoint = (GraphingTemperature)allPoints.get(x);
				
			if(!startAdded)
			{
				graph.append("<graph color=\"" + GraphingTemperature.getLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getValue() != null)
				graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingTemperature.getImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");				
		}
		
		graph.append("</graph>");
		
		return graph;
	}
	private StringBuffer getRespirationGraph()
	{
		StringBuffer graph = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.RESPIRATION);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{	
			GraphingRespiration castPoint = (GraphingRespiration)allPoints.get(x);
				
			if(!startAdded)
			{
				graph.append("<graph color=\"" + GraphingRespiration.getLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getRate() != null)
				graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getRate() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingRespiration.getImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");				
		}
		
		graph.append("</graph>");
		
		return graph;
	}
	private StringBuffer getOxygenGraph()
	{
		StringBuffer graph = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.OXYGEN);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingOxygen castPoint = (GraphingOxygen)allPoints.get(x);
				
			if(!startAdded)
			{
				graph.append("<graph color=\"" + GraphingOxygen.getLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getValue() != null)
				graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingOxygen.getImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");				
		}
		
		graph.append("</graph>");
		
		return graph;
	}
	private StringBuffer getPulseGraph()
	{
		StringBuffer graph = new StringBuffer();
        StringBuffer graphRadial = new StringBuffer();
        StringBuffer graphApex = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.PULSE);
		if(allPoints.size() == 0)
			return graph;
			
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingPulse castPoint = (GraphingPulse)allPoints.get(x);
				
			if(!startAdded)
			{
				graphRadial.append("<graph color=\"" + GraphingPulse.getRadialLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
                graphApex.append("<graph color=\"" + GraphingPulse.getApexLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getRadialRate() != null)
				graphRadial.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getRadialRate() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPulse.getRadialImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");				
            if(castPoint.getApexRate() != null)
                graphApex.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getApexRate() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingPulse.getApexImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
		}
		
		graphRadial.append("</graph>");
        graphApex.append("</graph>");
        
        if(this.data.options.pulse.getDisplayRadial())
            graph.append(graphRadial);
        if(this.data.options.pulse.getDisplayApex())
            graph.append(graphApex);
		
		return graph;
	}
	private StringBuffer getBloodPressureGraph()
	{
		StringBuffer graph = new StringBuffer();
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.BLOODPRESSURE);
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingBloodPressure castPoint = (GraphingBloodPressure)allPoints.get(x);
				
			if(this.data.options.bloodPressure.getDisplayStanding())
			{
				if(castPoint.getStandingDiaValue() != null && castPoint.getStandingSysValue() != null)
				{
					graph.append("<graph color=\"" + GraphingBloodPressure.getLineColorStanding().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getStandingDiaValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getStandingDiaImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getStandingSysValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getStandingSysImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("</graph>");
				}
			}
			if(this.data.options.bloodPressure.getDisplaySitting())
			{
				if(castPoint.getSittingDiaValue() != null && castPoint.getSittingSysValue() != null)
				{
					graph.append("<graph color=\"" + GraphingBloodPressure.getLineColorSitting().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getSittingDiaValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getSittingDiaImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getSittingSysValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getSittingSysImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("</graph>");
				}
			}
			if(this.data.options.bloodPressure.getDisplayLying())
			{
				if(castPoint.getLyingDiaValue() != null && castPoint.getLyingSysValue() != null)
				{
					graph.append("<graph color=\"" + GraphingBloodPressure.getLineColorLying().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getLyingDiaValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getLyingDiaImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getLyingSysValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingBloodPressure.getLyingSysImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
					graph.append("</graph>");
				}
			}
		}
		
		return graph;
	}
	private GraphingGroup[] getAllGroups()
	{
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.CUSTOM_ONE_VALUE);
		ArrayList<GraphingGroup> groups = new ArrayList<GraphingGroup>();
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingCustomOneValue castPoint = (GraphingCustomOneValue)allPoints.get(x);
			if(!groups.contains(castPoint.getGroup()))
				groups.add(castPoint.getGroup());
		}		
		
		GraphingGroup[] result = new GraphingGroup[groups.size()];		
		for(int x = 0; x < groups.size(); x++)
		{
			result[x] = groups.get(x);
		}		
		return result;
	}
	private StringBuffer getCustomOneValueGraph(GraphingGroup group)
	{
		StringBuffer graph = new StringBuffer();
		boolean startAdded = false;
				
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.CUSTOM_ONE_VALUE);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingCustomOneValue castPoint = (GraphingCustomOneValue)allPoints.get(x);
			
			if(group.equals(castPoint.getGroup()))
			{
				if(!startAdded)
				{
					graph.append("<graph color=\"" + group.getLineColor().toString() + "\" weight=\"" + group.getLineWidth() + "\" type=\"" + group.getLineStyle().getValue() + "\">");
					startAdded = true;
				}
				
				if(castPoint.getValue() != null)
					graph.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + group.getImage().getImagePath() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			}
		}
		
		graph.append("</graph>");
		
		return graph;
	}
	private StringBuffer getGlasgowComaScaleGraph()
	{		
		StringBuffer graph = new StringBuffer();
		StringBuffer graphVerbal = new StringBuffer();
		StringBuffer graphMotor = new StringBuffer();
		StringBuffer graphEye = new StringBuffer();
		StringBuffer graphTotal = new StringBuffer();
		boolean startAdded = false;
			
		ArrayList allPoints = this.data.getAllPoints(GraphingPointType.GLASGOWCOMASCALE);
		if(allPoints.size() == 0)
			return graph;
		
		for(int x = 0; x < allPoints.size(); x++)
		{		
			GraphingGlasgowComaScale castPoint = (GraphingGlasgowComaScale)allPoints.get(x);
					
			if(!startAdded)
			{
				graphVerbal.append("<graph color=\"" + GraphingGlasgowComaScale.getVerbalLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphMotor.append("<graph color=\"" + GraphingGlasgowComaScale.getMotorLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphEye.append("<graph color=\"" + GraphingGlasgowComaScale.getEyeLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				graphTotal.append("<graph color=\"" + GraphingGlasgowComaScale.getTotalLineColor().toString() + "\" weight=\"" + castPoint.getType().getLineWeight() + "\" type=\"" + castPoint.getType().getLineType() + "\">");
				startAdded = true;
			}
			
			if(castPoint.getVerbalValue() != null)
				graphVerbal.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getVerbalValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingGlasgowComaScale.getVerbalImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getMotorValue() != null)
				graphMotor.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getMotorValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingGlasgowComaScale.getMotorImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getEyeValue() != null)
				graphEye.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getEyeValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingGlasgowComaScale.getEyeImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");
			
			if(castPoint.getTotalValue() != null)
				graphTotal.append("<point id=\"" + castPoint.getID() + "\" y=\"" + castPoint.getTotalValue() + "\" x=\"" + this.data.getPointOnX(castPoint) + "\" img=\"" + GraphingGlasgowComaScale.getTotalImageUrl() + "\" tooltip=\"" + ims.framework.utils.StringUtils.encodeXML(castPoint.getTooltip()) + "\"/>");			
		}
		
		graphVerbal.append("</graph>");
		graphMotor.append("</graph>");
		graphEye.append("</graph>");
		graphTotal.append("</graph>");
		
		if(this.data.options.glasgowComaScale.getDisplayVerbal())
			graph.append(graphVerbal);
		if(this.data.options.glasgowComaScale.getDisplayMotor())
			graph.append(graphMotor);
		if(this.data.options.glasgowComaScale.getDisplayEye())
			graph.append(graphEye);
		if(this.data.options.glasgowComaScale.getDisplayTotal())
			graph.append(graphTotal);
		
		return graph;
	}
	public void clear()
	{
		clearAllColorBandsY();
		clearAllHighlightedY();
		clearAllLabelsY();
		clearAllPoints();
	}
	public float getMinY()
	{
		return this.minY;
	}
	public float getMaxY()
	{
		return this.maxY;
	}
	private ArrayList getExistingStandardTypes()
	{
		ArrayList<GraphingPointType> types = new ArrayList<GraphingPointType>();
		
		boolean exist;
		for(int x = 0; x < this.data.getAllPoints().size(); x++)
		{
			GraphingPoint point = this.data.getAllPoints().get(x);
			if(point.getType() != GraphingPointType.CUSTOM_ONE_VALUE)
			{
				exist = false;
				
				for(int i = 0; i < types.size(); i++)
				{
					if(types.get(i) == point.getType())
					{
						exist = true;
						break;
					}
				}
				
				if(!exist)
					types.add(this.data.getAllPoints().get(x).getType());
			}
		}
		
		return types;
	}	
	public boolean wasChanged() 
	{
		if(this.data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
					return true;
			}
			
			if(this.data.isTitleChanged())
				return true;
			
			if(this.data.isSubTitleChanged())
				return true;
			
			if(this.data.isPrintHeaderInfoChanged())
				return true;
			
			if(this.data.isShowLegendChanged())
				return true;
			
			if(this.data.isPrintHeaderInfoChanged())
				return true;
			
			if(this.data.isShowYAxisValuesChanged())
				return true;
			
			if(this.data.isLabelsYChanged())
				return true;
			
			if(this.data.isColorBandsYChanged())
				return true;
			
			if(this.data.isHighlightedYChanged())
				return true;
		}
		
		// for now we return true - it will change later @mm
		return true;
	}
	public void markUnchanged() 
	{		
	}

	private GraphingControlData data;
	private float minY;
	private float maxY;
	private String title;
	private String subTitle;
	private int numberOfLabelsX;
	private int numberOfPointsY;
	private String labelX;
	private String labelY;
	private boolean showLegend;	
}
