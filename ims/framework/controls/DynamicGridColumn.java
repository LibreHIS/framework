package ims.framework.controls;

import ims.framework.enumerations.Alignment;
import ims.framework.enumerations.SortMode;
import ims.framework.enumerations.VerticalAlignment;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public interface DynamicGridColumn 
{
    void setIdentifier(Object value);
    Object getIdentifier();
    void setCaption(String value);
    String getCaption();
    void setCaptionImage(Image value);
    Image getCaptionImage(); 
    void setReadOnly(boolean value);
    boolean isReadOnly();
    void setAlignment(Alignment value);
    Alignment getAlignment();
    void setVerticalAlignment(VerticalAlignment value);
    VerticalAlignment getVerticalAlignment();
    void setHeaderAlignment(Alignment value);
    Alignment getHeaderAlignment();
    void setVisible(boolean value);
    boolean isVisible();
    void setWidth(int value);
    int getWidth();
    void setTextColor(Color value);
    Color getTextColor();
    void setHeaderTooltip(String value);
    void clearHeaderTooltip();
    String getHeaderTooltip();
    void setBackColor(Color value);
    Color getBackColor();
    void setCanGrow(boolean value);
    boolean canGrow();
    void setSortMode(SortMode value);
    SortMode getSortMode();
    DynamicGridCell[] getCellArray();
    void setDynamicWidthSupported(boolean value);
    boolean isDynamicWidthSupported();
}
