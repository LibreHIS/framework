package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import ims.base.interfaces.IModifiable;
import ims.framework.controls.DynamicGridCell;
import ims.framework.controls.DynamicGridRow;
import ims.framework.enumerations.Align;
import ims.framework.enumerations.BackgroundRepeat;
import ims.framework.enumerations.BorderStyle;
import ims.framework.enumerations.FontFamily;
import ims.framework.enumerations.FontStyle;
import ims.framework.enumerations.FontWeight;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class DynamicGridCellTable implements IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;

	private boolean dataWasChanged = true; 
	private TableRowCollection rows = new TableRowCollection();
	private DynamicGridCell partentCell;
	private int tablebBorder = -1;
	
	final String HTML_START_TAG = "<";
	final String HTML_CLOSE_TAG = ">";
	final String HTML_END_TAG = "/>";
	final String HTML_START_TABLE_TAG = "<table";
	final String HTML_CLOSE_TABLE_TAG = "</table>";	
	final String HTML_START_TR_TAG = "<tr";
	final String HTML_CLOSE_TR_TAG = "</tr>";
	final String HTML_START_TD_TAG = "<td";
	final String HTML_CLOSE_TD_TAG = "</td>";
	
	public DynamicGridCellTable() 
	{		
	}
	
	public DynamicGridCellTable(DynamicGridCell partentCell) 
	{
		this.partentCell = partentCell;
	}
	
	public ArrayList<TableCell> getCellsByIdentifier(Object identifier)
	{
		ArrayList<TableCell> cells = new ArrayList<TableCell>();
		
		for (int i = 0; i < this.getRows().size(); i++) 
		{
			TableRow row =  this.getRows().get(i);
			if (row != null)
			{
				for (int j = 0; j < row.getCells().size(); j++)
				{
					if (row.getCells().get(j).getIdentifier().equals(identifier))
					{
						cells.add(row.getCells().get(j));
					}
				}
			}
		}
		
		return cells;
	}
	
	public class TableRow implements IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;

		public TableCellCollection cells = new TableCellCollection() ;
		private Object identifier = null;
		private boolean dataWasChanged = true; 
		
		public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;	    	
	    }	
	    public void setIdentifier(Object value)
	    {
			this.identifier = value;
	    }
	    public Object getIdentifier()
	    {
	      	return this.identifier;
	    }	
	    public TableCellCollection getCells()
	    {
	    	return this.cells;
	    }	    	    
	}
	
	public class TableRowCollection implements IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;

		private boolean dataWasChanged = true; 
		private ArrayList<TableRow> rows = new ArrayList<TableRow>();
		
		public int size()
		{
			return this.rows.size();
		}
				
		public TableRow get(int index)         
	    {
	    	return this.rows.get(index);
	    }
		
		public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;	    	
	    }	

		public TableRow newRow() 
		{
			TableRow row = new TableRow();
			rows.add(row);
			this.dataWasChanged = true; 
			
			return row;
		}
		
		public void remove(DynamicGridRow row) 
		{
			if(row == null)
				return;
	    
			if(this.rows.contains(row))
            {
            	this.rows.remove(row);
            	this.dataWasChanged = true;
            }	            			
		}				
	}
	
	public class TableCell implements IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;

		private Object identifier = null;
		private boolean dataWasChanged = true; 
		private TableCellType type;
		private TableCellOptions options;
		private Object value;
		private int width = -1;
		private int colspan = -1;
		private int rowspan = -1;	
		private String tooltip;
	
		public TableCell() 
		{
			
		}
		public TableCell(TableCellType type) 
		{
			this.type = type;
		}
		public TableCell(TableCellType type, TableCellOptions options) 
		{
			this.type = type;
			this.options = options;
		}
		
		public void setTooltip(String tooltip)
	    {
			this.tooltip = tooltip;
	    }	
		public String getTooltip()
	    {
			return tooltip;
	    }	
		public void setIdentifier(Object value)
	    {
			this.identifier = value;
	    }
	    public Object getIdentifier()
	    {
	      	return this.identifier;
	    }		
		public TableCellType getType() 
		{
			return type;
		}
		public void setType(TableCellType type) 
		{
			this.type = type;			
		}
		public void setOptions(TableCellOptions options) 
		{
			this.options = options;
		}	
		public TableCellOptions getOptions() 
		{
			return options;
		}
		public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;	    	
	    }
	    public Object getValue() 
	    {
	        return this.value;
	    }
	    public void setValue(Object value) 
	    {
	    	if(!this.dataWasChanged)
	    	{
	    		if(this.value == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.value.equals(value);
	    	}
	    	
	        this.value = value;
	    }	
	    public void setWidth(int width)
	    {
	    	this.width = width;
	    }
	    private int getWidth()
	    {
	    	return width;
	    }
	    public void setColSpan(int colspan)
	    {
	    	this.colspan = colspan;
	    }
	    public int getColSpan()
	    {
	    	return this.colspan;
	    }
	    public void setRowSpan(int rowspan)
	    {
	    	this.rowspan = rowspan;
	    }
	    public int getRowSpan()
	    {
	    	return this.rowspan;
	    }	    
	}
		
	public class TableCellCollection  implements IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;

		private boolean dataWasChanged = true; 
		public ArrayList<TableCell> cells = new ArrayList<TableCell>();
		
		public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;	    	
	    }	

	    public TableCell get(int index)         
	    {
	    	return this.cells.get(index);
	    }
	    
		public int size() 
		{
			return this.cells.size();
		}

		public TableCell newCell() 
		{
			TableCell cell = new TableCell();
			this.cells.add(cell);
			this.dataWasChanged = true;
			
			return cell;
		}
		
		public TableCell newCell(TableCellType type) 
		{
			TableCell cell = new TableCell(type);
			this.cells.add(cell);
			this.dataWasChanged = true;
			
			return cell;
		}

		public TableCell newCell(TableCellType type, TableCellOptions options) 
		{
			TableCell cell = new TableCell(type, options);
			this.cells.add(cell);
			this.dataWasChanged = true;
			
			return cell;
		}			
	}
	public static class TableCellOptions 
	{
		public TableCellOptions() 
		{	
		}
		
		//Align
		Align align;
		
		//Cell border
		Color borderColor;
		BorderStyle borderStyle;
		int borderWidth = 0;	
		
		//Text colour
		Color textColor;
		
		//Background
		Color backgroundColor;
		String backgroundImageUrl;
		BackgroundRepeat backgroundRepeat;
				
		//Font properties 
		FontFamily fontFamily;
		FontWeight fontWeight;
		FontStyle fontStyle;	
		int fontSize;
		
		//Blink
		boolean blink;
		Color textFromColor;
		Color textToColor;
		Color backgroundFromColor;
		Color backgroundToColor; 
		
		//Autopostback
		boolean autopostback;
		
		//Image for button
		Image buttonImage;
		
		//ButtonSize
		int buttonWidth = 0;
		int buttonHeight = 0;
		
		//Text for button
		String buttonImageText;
		
		public Align getAlign() 
		{
			return align;
		}
		public void setAlign(Align align) 
		{
			this.align = align;
		}
		public Color getBorderColor() 
		{
			return borderColor;
		}
		public void setBorderColor(Color color) 
		{
			this.borderColor = color;
		}
		public BorderStyle getBorderStyle() 
		{
			return borderStyle;
		}
		public void setBorderStyle(BorderStyle style) 
		{
			this.borderStyle = style;
		}
		public int getBorderWidth() 
		{
			return borderWidth;
		}
		public void setBorderWidth(int width) 
		{
			this.borderWidth = width;
		}
		public Color getBackgroundColor() 
		{
			return backgroundColor;
		}
		public void setBackgroundColor(Color color) 
		{
			this.backgroundColor = color;
		}
		public String getBackgroundImageUrl() 
		{
			return backgroundImageUrl;
		}
		public void setBackgroundImageUrl(String imageUrl) 
		{
			this.backgroundImageUrl = imageUrl;
		}
		public BackgroundRepeat getBackgroundRepeat() 
		{
			return backgroundRepeat;
		}
		public void setBackgroundRepeat(BackgroundRepeat repeat) 
		{
			this.backgroundRepeat = repeat;
		}
		public Color getTextColor() 
		{
			return textColor;
		}
		public void setTextColor(Color color) 
		{
			this.textColor = color;
		}
		public FontFamily getFontFamily() 
		{
			return fontFamily;
		}
		public void setFontFamily(FontFamily family) 
		{
			this.fontFamily = family;
		}
		public FontStyle getFontStyle() 
		{
			return fontStyle;
		}
		public void setFontStyle(FontStyle style) 
		{
			this.fontStyle = style;
		}
		public FontWeight getFontWeight() 
		{
			return fontWeight;
		}
		public void setFontWeight(FontWeight weight) 
		{
			this.fontWeight = weight;
		}
		public int getFontSize() 
		{
			return fontSize;
		}
		public void setFontSize(int size) 
		{
			this.fontSize = size;
		}
		public boolean isBlinking() 
		{
			return blink;
		}
		public void stopBlink() 
		{
			this.blink = false;
		}
		public void setTextAndBackgroundBlink(Color textFromColor,Color textToColor, Color backgroundFromColor, Color backgroundToColor) 
		{
			this.blink = true;
			this.textFromColor = textFromColor;
			this.textToColor = textToColor;
			this.backgroundFromColor = backgroundFromColor;
			this.backgroundToColor = backgroundToColor;
		}		
		
		public boolean hasAutopostback() 
		{
			return autopostback;
		}
		public void setAutopostback(boolean autopostback) 
		{
			this.autopostback = autopostback;
		}
		public void setButtonImage(Image image) 
		{
			this.buttonImage = image;
		}
		public Image getButtonImage() 
		{
			return this.buttonImage;
		}		
		public void setButtonImageText(String text) 
		{
			this.buttonImageText = text;
		}
		public String getButtonImageText() 
		{
			return this.buttonImageText;
		}
		public void setButtonWidth(int width) 
		{
			this.buttonWidth = width;
		}
		public int getButtonWidth() 
		{
			return this.buttonWidth;
		}
		public void setButtonHeight(int height) 
		{
			this.buttonHeight = height;
		}
		public int getButtonHeight() 
		{
			return this.buttonHeight;
		}
	}	
	
	public static class TableCellType implements Serializable
	{
		private static final long serialVersionUID = 1L;
	    public static TableCellType EMPTY = new TableCellType(0, null, false, false, 50);
	    public static TableCellType BUTTON = new TableCellType(1, String.class, false, true, 80);
	    public static TableCellType STRING = new TableCellType(2, String.class, false, true, 200);
	    public static TableCellType IMAGE = new TableCellType(3, String.class, false, true, 200);
	    
	    private TableCellType(int id, Class<?> type, boolean supportsValidation, boolean supportsAutoPostBack, int defaultWidth)
	    {
	        this.id = id;
	        this.type = type;
	        this.supportsValidation = supportsValidation;
	        this.supportsAutoPostBack = supportsAutoPostBack;
	        this.defaultWidth = defaultWidth;        
	    }
	    public Class<?> getCellType()
	    {
	        return this.type;
	    }
	    public void checkType(Class<?> currentType)
	    {
	        if(this.type == null) // empty
	            throw new RuntimeException("Could not set value for cell of type " + this.toString());
	        	
	        if(this.type.isAssignableFrom(currentType))
	            return;
	        
	        throw new RuntimeException("Could not set cell value. Type expected: " + this.type + ", current type: " + currentType);
	    }
	    public String toString()
	    {
	        if(this.id == 0)
	            return "Empty";
	        if(this.id == 1)            
	            return "Button";
	        else if(this.id == 2)
	            return "String";        
	        else if(this.id == 3)
	            return "Image";
	        else return "";
	    }
	    public String render()
	    {
	        return toString();
	    }
	    
	    public boolean isSupportingValidation()
	    {
	        return this.supportsValidation;
	    }
	    public boolean isSupportingAutoPostBack()
	    {
	        return this.supportsAutoPostBack;
	    }
	    public int getID()
	    {
	        return this.id;
	    }
	    public int getDefaultWidth()
	    {
	    	return this.defaultWidth;
	    }
	    
	    private int id;
	    private Class<?> type;
	    private boolean supportsValidation;
	    private boolean supportsAutoPostBack;
	    private int defaultWidth;    
	}
	
	public boolean wasChanged()
    {
    	if(this.dataWasChanged)
    		return true;
    	
    	return false;
    }
	
    public void markUnchanged()
    {
    	this.dataWasChanged = false;	    	
    }	
	
	public TableRowCollection getRows()
	{
		return this.rows;
	}	
	
	public String renderValue()
    {		
		StringBuffer sb = new StringBuffer();
		
		sb.append(startTableTag());		
		
		for (int i = 0; i < this.rows.size(); i++) 
		{
			sb.append(startTRtag(i));
			sb.append(closeTag());
			
			TableRow row = this.rows.get(i);
			for (int j = 0; j < row.getCells().size(); j++) 
			{
				TableCell cell = row.getCells().get(j);
				sb.append(startTDtag(j, cell));
				
				if (cell != null)
				{					
					if (cell.getType() != null)
					{
						if(cell.getType().equals(TableCellType.BUTTON)) {
							sb.append(setWidth(cell.getOptions() != null ? (cell.getOptions().getButtonImage() != null) ? cell.getOptions().getButtonImage().getImageWidth() + 6 :30 : 30));	
						}						
						else {
							sb.append(setWidth(cell.getWidth()));
						}						
						
						sb.append(cellType(cell.getType()));	
						
						if (cell.getOptions() != null) {
							sb.append(cellOptions(cell.getOptions(), cell.getType()));						
						}	
						
						sb.append(closeTag());
						
						if (cell.getValue() != null) {
							sb.append(cellValue(cell));						
						}
						if (cell.getTooltip() != null) {
							sb.append(cellTooltip(cell));						
						}
					}		
					else {
						sb.append(closeTag());
					}
				}
				else {
					sb.append(closeTag());
				}
				
				sb.append(closeTDtag());
			}
			
			sb.append(closeTRtag());			
		}	
		
		sb.append(closeTableTag());				
		
		return sb.toString();		
    }
		
	private String closeTag()
	{		
		return HTML_CLOSE_TAG;
	}
	
	private String startTableTag()
	{	
		StringBuffer sb = new StringBuffer();
		
		if (this.tablebBorder != -1)
		{
			sb.append(" border=\"");
			sb.append(this.tablebBorder);
			sb.append("\"");
		}
        
		return HTML_START_TABLE_TAG + sb.toString() + HTML_CLOSE_TAG;
	}
	
	private String closeTableTag()
	{			
		return HTML_CLOSE_TABLE_TAG;
	}
	
	private String startTRtag(int id)
	{	
		StringBuffer sb = new StringBuffer();
		
		sb.append(" id=\"");
        sb.append(id);
        sb.append("\"");
        
		return HTML_START_TR_TAG + sb.toString();
	}
	private String closeTRtag()
	{		
		return HTML_CLOSE_TR_TAG;
	}
	private String startTDtag(int id, TableCell cell)
	{	
		StringBuffer sb = new StringBuffer();
		
		sb.append(" id=\"");
        sb.append(id);
        sb.append("\"");    
        
        if(cell.getColSpan() != -1)
        {
        	sb.append(" colspan=\"");
            sb.append(cell.getColSpan());
            sb.append("\"");
        }
        else if(cell.getRowSpan() != -1)
        {
        	sb.append(" rowspan=\"");
            sb.append(cell.getRowSpan());
            sb.append("\"");
        }
        
        return HTML_START_TD_TAG + sb.toString();
	}
	
	private String setWidth(int width)
	{
		StringBuffer sb = new StringBuffer();
		
		if (width != -1)
		{
			sb.append(" width=\"");
			sb.append(width);
			sb.append("\"");
		}
        
		return sb.toString();
	}
	
	public void setBorder(int border)
    {
    	this.tablebBorder = border;
    }
    public int getBorder()
    {
    	return this.tablebBorder;
    }
	
	private String closeTDtag()
	{		
		return HTML_CLOSE_TD_TAG;
	}
	private String cellValue(TableCell cell)
	{
		StringBuffer sb = new StringBuffer();
		
		if (cell.getType() != null)
		{
			if (cell.getType().equals(TableCellType.STRING))
			{
				sb.append("<value><![CDATA[");;
				sb.append(StringUtils.encodeHtml((String)cell.getValue()));
				sb.append("]]></value>");
			}
			else if (cell.getType().equals(TableCellType.IMAGE) &&
					cell.getValue() instanceof Image)
			{
				sb.append("<value><![CDATA[");;
				sb.append(((Image)cell.getValue()).getImagePath());
				sb.append("]]></value>");
			}
		}
		
		return sb.toString();
	}
	
	private String cellTooltip(TableCell cell)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tooltip><![CDATA[");;
		sb.append(StringUtils.encodeHtml((String)cell.getTooltip()));
		sb.append("]]></tooltip>");
		
		return sb.toString();
	}
		
	private String cellType(TableCellType type)
	{	
		StringBuffer sb = new StringBuffer();
		
		sb.append(" type=\"");
        sb.append(type.toString());
        sb.append("\"");
        
		return sb.toString();
	}
	private String cellOptions(TableCellOptions options, TableCellType type)
	{
		StringBuffer sb = new StringBuffer();		
		
		if (type.equals(TableCellType.BUTTON))
		{
			if (options.getButtonWidth() > 0)
			{
				sb.append(" buttonWidth=\"");
				sb.append(options.getButtonWidth());
				sb.append("\"");
			}
			if (options.getButtonHeight() > 0)
			{
				sb.append(" buttonHeight=\"");
				sb.append(options.getButtonHeight());
				sb.append("\"");
			}
			
			if (options.getButtonImage() != null)
			{
				sb.append(" imgSrc=\"");
		        sb.append(options.getButtonImage().getImagePath());
		        sb.append("\"");
			}
		    
		    if (options.getButtonImageText() != null)
			{
				sb.append(" imgText=\"");
		        sb.append(StringUtils.encodeXML(options.getButtonImageText()));
		        sb.append("\"");
			}
		}
		
		if (options.getAlign() != null)
		{
			sb.append(" align=\"");
	        sb.append(options.getAlign().toString());
	        sb.append("\"");
		}		
		if (options.getBorderColor() != null)
		{
			sb.append(" borderColor=\"");
	        sb.append(options.getBorderColor().toString());
	        sb.append("\"");
		}
		if (options.getBorderStyle() != null)
		{
			sb.append(" borderStyle=\"");
	        sb.append(options.getBorderStyle().toString());
	        sb.append("\"");
		}
		
		sb.append(" borderWidth=\"");
	    sb.append(options.getBorderWidth());
	    sb.append("\"");
		
	    if (options.getBackgroundColor() != null)
		{
			sb.append(" bgColor=\"");
	        sb.append(options.getBackgroundColor().toString());
	        sb.append("\"");
		}
	    
	    if (options.getBackgroundImageUrl() != null)
		{
			sb.append(" bgImage=\"");
	        sb.append(options.getBackgroundImageUrl());
	        sb.append("\"");
		}
	    
	    if (options.getBackgroundRepeat() != null)
		{
			sb.append(" bgRepeat=\"");
	        sb.append(options.getBackgroundRepeat().toString());
	        sb.append("\"");
		}
	    
	    if (options.getTextColor()!= null)
		{
			sb.append(" color=\"");
	        sb.append(options.getTextColor().toString());
	        sb.append("\"");
		}
		
	    if (options.getFontFamily() != null)
		{
			sb.append(" fontFamily=\"");
	        sb.append(options.getFontFamily().toString());
	        sb.append("\"");
		}
	    if (options.getFontStyle()!= null)
		{
			sb.append(" fontStyle=\"");
	        sb.append(options.getFontStyle().toString());
	        sb.append("\"");
		}
	    
	    if (options.getFontWeight()!= null)
		{
			sb.append(" fontWeight=\"");
	        sb.append(options.getFontWeight().toString());
	        sb.append("\"");
		}
	    
	    sb.append(" fontSize=\"");
	    sb.append(options.getFontSize());
	    sb.append("\"");
	    
	    if (options.isBlinking())
		{
			sb.append(" blink=\"");
	        sb.append("true");
	        sb.append("\"");
	        
	        sb.append(" identifier=\"");
	        sb.append(UUID.randomUUID().toString());
	        sb.append("\"");
	        
	        if (options.textFromColor != null)
	        {
	        	sb.append(" textFromColor=\"");
		        sb.append(options.textFromColor.toString());
		        sb.append("\"");
	        }
	        if (options.textToColor != null)
	        {
	        	sb.append(" textToColor=\"");
		        sb.append(options.textToColor.toString());
		        sb.append("\"");
	        }
	        if (options.backgroundFromColor != null)
	        {
	        	sb.append(" backgroundFromColor=\"");
		        sb.append(options.backgroundFromColor.toString());
		        sb.append("\"");
	        }
	        if (options.backgroundToColor != null)
	        {
	        	sb.append(" backgroundToColor=\"");
		        sb.append(options.backgroundToColor.toString());
		        sb.append("\"");
	        }
		}
	    else
		{
			sb.append(" blink=\"");
	        sb.append("false");
	        sb.append("\"");	    
		}	    
	    
	    if (options.hasAutopostback())
		{
			sb.append(" autopostback=\"");
	        sb.append("true");
	        sb.append("\"");
		}
	    	    	    
	    return sb.toString();		
	}
	
	public void setParentCell(DynamicGridCell cell)
	{
		this.partentCell = cell; 
	}
	
	public DynamicGridCell getParentCell()
	{
		return this.partentCell; 
	}
	
	public TableCell getFiredCell()
    {
		if (partentCell != null)
		{
			if (partentCell.getTableRowId() != -1 && partentCell.getTableCellId() != -1)
			{
				TableRow firedRow = rows.get(partentCell.getTableRowId());
				if (firedRow != null)
				{
					return firedRow.getCells().get(partentCell.getTableCellId());
				}
			}
		}
		
		return null;
    }
}

	