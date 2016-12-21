package ims.framework.controls;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public interface DynamicGridRow
{
	void setIdentifier(Object value);
    Object getIdentifier();
    Object getValue();
    void setValue(Object value);
    void setSelectable(boolean value);
    boolean isSelectable();
    void setReadOnly(boolean value);
    boolean isReadOnly();
    void setTextColor(Color value);
    Color getTextColor();
    void setBackColor(Color value);
    Color getBackColor();
    void setBold(boolean value);
    boolean isBold();
    void setExpanded(boolean value);
    void setExpanded(boolean value, boolean recursive);
    boolean isExpanded();
    void setExpandedImage(Image image);
    void setCollapsedImage(Image image);
    void setSelectedImage(Image image);
    void setOptions(DynamicGridRowOptions value);
    DynamicGridRowOptions getOptions();
    DynamicGridCellCollection getCells();
    DynamicGridCell[] getCellArray();
    DynamicGridRowCollection getRows();
    DynamicGridRow getParent();
    boolean canMoveUp();
    boolean canMoveDown();
    boolean moveUp();
    boolean moveDown();
    boolean moveTo(int index);
    void setCheckBoxVisible(boolean value);
    boolean isCheckBoxVisible();    
    void setChecked(boolean value);
    boolean isChecked();        
}
