package ims.framework.cn.controls;

import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.GridColumnType;

import java.io.Serializable;

abstract public class GridColumn implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public GridColumn(String caption, int captionAlignment, int cellAlignment, int width, boolean readOnly, boolean bold, boolean canGrow, int precision, int scale, String headerTooltip)
	{
		this.caption = caption;
		this.captionAlignment = captionAlignment;
		this.cellAlignment = cellAlignment;
		this.width = width;
		this.readOnly = readOnly;
		this.bold = bold;
		this.canGrow = canGrow;
		this.precision = precision;
		this.scale = scale;		
		this.headerTooltip = headerTooltip;		
	}
	public GridColumn(String caption, int captionAlignment, int cellAlignment, int width, boolean readOnly, boolean bold, boolean canGrow, int precision, int scale)
	{
		this(caption, captionAlignment, cellAlignment, width, readOnly, bold, canGrow, precision, scale, "");
	}
	public GridColumn(String caption, int captionAlignment, int cellAlignment, int width, boolean readOnly, boolean bold, boolean canGrow)
	{
		this(caption, captionAlignment, cellAlignment, width, readOnly, bold, canGrow, 0, 0, "");
	}

	public boolean canBeEmpty()
	{
		return true;
	}
	public int getSortOrder()
	{
		return 0; // none
	}

	public String getCaption() 
	{
		return this.caption;
	}

	public void setCaption(String string) 
	{
		this.caption = string;
	}
	
	public boolean getReadOnly()
	{
		return this.readOnly;
	}
	
	public void setReadOnly(boolean value)
	{
		this.readOnly = value;
	}
	public int getPrecision()
	{
		return this.precision;
	}
	public int getScale()
	{
		return this.scale;
	}
	public CharacterCasing getCharacterCasing()
	{
		return CharacterCasing.NORMAL;
	}
	public void setHeaderTooltip(String value)
	{
		headerTooltip = value;
	}
	public String getHeaderTooltip()
	{
		return headerTooltip;
	}

	abstract public GridColumnType getType();
	public void parse(StringBuffer sb)
	{
		sb.append("<col captionAlign=\"");
		sb.append(this.captionAlignment == 0 ? "left" : (this.captionAlignment == 1 ? "center" : "right"));
		sb.append("\" align=\"");
		sb.append(this.cellAlignment == 0 ? "left" : (this.cellAlignment == 1 ? "center" : "right"));
		sb.append("\" width=\"");
		sb.append(this.width);
		sb.append("\" readOnly=\"");
		sb.append(this.readOnly ? "true" : "false");
		sb.append("\" bold=\"");
		sb.append(this.bold ? "true" : "false");
		
		CharacterCasing casing = getCharacterCasing();
		if(casing != CharacterCasing.NORMAL)
			sb.append("\" casing=\"" + casing.render());
		
		if(!this.canGrow)
			sb.append("\" fixedWidth=\"true");
		
		sb.append("\" caption=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(this.caption));
		
		if (this.getSortOrder() > 0)
		{
			sb.append("\" sort=\"");
			sb.append(this.getSortOrder() == 1 ? "auto" : "manual");
		}
		
		if(this.maxDropDownItems != -1)
		{
			sb.append("\" maxPopupItems=\"");
			sb.append(this.maxDropDownItems);
		}
		
		sb.append("\" tooltip=\"");
		sb.append(this.headerTooltip == null ? "" : ims.framework.utils.StringUtils.encodeXML(this.headerTooltip));
		
		sb.append('\"');
	}

	protected String caption;
	protected int captionAlignment;
	protected int cellAlignment;
	protected int width;
	protected boolean readOnly;
	protected boolean bold;
	protected boolean canGrow;
	protected int precision;
	protected int scale;
	protected String headerTooltip;
	protected CharacterCasing casing = CharacterCasing.NORMAL;
	protected int maxDropDownItems = -1;
}