package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.GraphingControlClick;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.utils.Color;
import ims.framework.utils.graphing.GraphingHighlight;
import ims.framework.utils.graphing.GraphingPoint;
import ims.framework.utils.graphing.GraphingPointType;
import ims.framework.utils.graphing.Options;

abstract public class GraphingControl extends Control
{	
	private static final long serialVersionUID = 1L;
	
	public void setGraphingControlClickEvent(GraphingControlClick delegate)
    {
        this.graphingControlClickDelegate = delegate;        
    }
	protected void free()
	{
		super.free();
		
		this.graphingControlClickDelegate = null;
	}
	
	public abstract void clear();
	
	public abstract float getMinY();
	public abstract float getMaxY();
	
	public abstract void addHighlightedY(float value);
	public abstract void addHighlightedY(GraphingHighlight value);
	public abstract void clearAllHighlightedY();
	public abstract void addColorBandY(float fromValue, float toValue, Color color);
	public abstract void clearAllColorBandsY();
	public abstract void addLabelY(float value, String text);
	public abstract void clearAllLabelsY();
	public abstract void showYAxisValues();
	public abstract void showYAxisLabels();
	
	public abstract void addPoint(GraphingPoint graphingPoint);
	public abstract void addPoints(GraphingPoint[] graphingPoints);
	public abstract void clearAllPoints();
	public abstract void clearPoints(GraphingPointType type);
	public abstract void setTitle(String title);
	public abstract void setSubTitle(String subTitle);
	public abstract Options graphingOptions();
	public abstract void showLegend(boolean value);
	public abstract boolean legendIsShown();
	public abstract void getSerializedControl(StringBuffer sb) throws ConfigurationException;
	public abstract void getSerializedData(StringBuffer sb) throws ConfigurationException;
	public abstract void setPrintHeaderInfo(String info);
	
	protected GraphingControlClick graphingControlClickDelegate = null;
}
