package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public class DynamicGridRowOptions implements Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;
    public boolean readOnly = false;
    public boolean selectable = true;
    public boolean bold = false;
    public Color textColor = null;
    public Color backColor = null;
    public Image expandedImage = null;
    public Image collapsedImage = null;
    public Image selectedImage = null;
    public boolean expanded = false;
    
    public Object clone()
    {
        DynamicGridRowOptions clone = new DynamicGridRowOptions();
        
        clone.readOnly = this.readOnly;
        clone.selectable = this.selectable;
        clone.bold = this.bold;
        clone.textColor = this.textColor;
        clone.backColor = this.backColor;
        clone.expandedImage = this.expandedImage;
        clone.collapsedImage = this.collapsedImage;
        clone.selectedImage = this.selectedImage;
        clone.expanded = this.expanded;
        
        return clone;
    }
}
