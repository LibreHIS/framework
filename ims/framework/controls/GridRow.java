package ims.framework.controls;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

public interface GridRow 
{
	public Object get(int index);
	public void set(int index, Object value);
	public void set(int index, Object value, boolean addValueIfNotFound);
	public Object getValue();
	public void setValue(Object value);
	public boolean isReadOnly();
	public void setReadOnly(boolean value);
	public void setSelectable(boolean value);
	public boolean isSelectable();
	public void setBackColor(Color value);
	public Color getBackColor();
	public void setBackColor(int column, Color value);
	public void setTextColor(Color value);
	public void setTextColor(int column, Color value);
	public void setTooltip(String value);
	public void setTooltip(int column, String value);
	public void setExpanded(boolean value);
	public boolean isExpanded();
	public void setBold(boolean value);
	public GridRowCollection getRows();
	public GridRow getParent();
	public void showOpened(int column);
    public void setReadOnly(int column, boolean value);
    public boolean isReadOnly(int column);    
	public void setIsParentRow(boolean value);
	public boolean isParentRow();
    
	// Empty cell
	public void setIsEmpty(int column, boolean value);
	public boolean isEmpty(int column);
	
	// Mutable combobox
	public void newRow(int column, Object value, String text);
	public void clear(int column);
	public Object getValue(int column);
	public void setValue(int column, Object value);
	public void setEditedText(int column, String value);
	public String getEditedText(int column); 
	public void setMinNumberOfChars(int column, int value);
	public int getMinNumberOfChars(int column);
	
	// TreeGrid
	public void setCollapsedImage(Image image);
	public void setExpandedImage(Image image);
	public void setSelectedImage(Image image);
	
	// Mutable answerbox
	public void addAnswerBoxOption(int column, AnswerBoxOption option);
	public void clearAnswerBox(int column);
	public AnswerBoxOption getSelectedAnswerBoxOption(int column);
	public void setSelectedAnswerBoxOption(int column, AnswerBoxOption option);
	
	
	
} 
