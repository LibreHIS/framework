package ims.framework.controls;

import ims.vo.ImsCloneable;

public interface TreeNodeCollection extends ImsCloneable
{
    public int indexOf(TreeNode node);
	public TreeNode add(Object value, String text);
	public TreeNode add(Object value, String text, boolean autoSelect);
	public TreeNode add(Object value, String text, int dragType);
	public TreeNode add(Object value, String text, boolean autoSelect, int dragType);
    public TreeNode add(Object value, String text, int dragType, int[] dropToTypes);
    public TreeNode add(Object value, String text, boolean autoSelect, int dragType, int[] dropToTypes);
    public TreeNode add(Object value, String text, int dragType, int dropToType);
    public TreeNode add(Object value, String text, boolean autoSelect, int dragType, int dropToType);
    public TreeNode add(Object value, String text, int dragType, int[] dropToTypes, boolean allowDragDropSorting);
    public TreeNode add(Object value, String text, boolean autoSelect, int dragType, int[] dropToTypes, boolean allowDragDropSorting);
    public TreeNode add(Object value, String text, int dragType, int dropToType, boolean allowDragDropSorting);
    public TreeNode add(Object value, String text, boolean autoSelect, int dragType, int dropToType, boolean allowDragDropSorting);
    public TreeNode add(Object value, String text, int dragType, boolean allowDragDropSorting);
    public TreeNode add(Object value, String text, boolean autoSelect, int dragType, boolean allowDragDropSorting);
    public TreeNode getNodeByValue(Object value);
    public boolean remove(TreeNode node);
	public int size();
	public TreeNode get(int index);
	public abstract void moveUp();
	public abstract void moveDown();
	public abstract void moveTo(int toIndex);
	public abstract void swap(int index1, int index2);
	public abstract boolean canMoveCurrentUp();
	public abstract boolean canMoveCurrentDown();
	public void clear();
	public void sortByText();
	public void sortByText(boolean ascending);
	public void sortByText(boolean ascending, boolean recursive);
	public void sortByText(boolean ascending, boolean recursive, boolean caseSensitive);	
}