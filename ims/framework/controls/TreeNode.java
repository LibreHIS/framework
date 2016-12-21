package ims.framework.controls;

import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.vo.ImsCloneable;

public interface TreeNode extends ImsCloneable
{
	public Object getValue();
	public void setValue(Object value);
	public String getText();
	public void setText(String value);
	public boolean isChecked();
	public void setChecked(boolean value);
	public boolean isExpanded();
	public void setExpanded(boolean value);
	public boolean isEnabled();
	public void setEnabled(boolean value);
	public boolean isCheckBoxVisible();
	public void setCheckBoxVisible(boolean value);
	public void setCollapsedImage(Image image);
	public void setExpandedImage(Image image);
	public void setSelectedImage(Image image);
	public TreeNodeCollection getNodes();
	public TreeNode getParent();
	public boolean remove(TreeNode node);
	public void setParent(TreeNode newParent);
	public void setTooltip(String text);
    public void setTextColor(Color value);
    public Color getTextColor();
    public void sortChildrenByText();
	public void sortChildrenByText(boolean ascending);
	public void sortChildrenByText(boolean ascending, boolean recursive);
	public void sortChildrenByText(boolean ascending, boolean recursive, boolean caseSensitive);
	public Integer getDragDropType();
	public Object getIdentifier();
	public void setIdentifier(Object identifier);
}
