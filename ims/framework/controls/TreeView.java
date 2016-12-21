package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.TreeViewCheck;
import ims.framework.delegates.TreeViewNodeDropped;
import ims.framework.delegates.TreeViewNodeEdited;
import ims.framework.delegates.TreeViewNodeExpandedCollapsed;
import ims.framework.delegates.TreeViewSelectionChanged;
import ims.framework.delegates.TreeViewSelectionCleared;

public abstract class TreeView extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
		
		this.treeViewNodeEditedDelegate = null;
		this.treeViewSelectionChangedDelegate = null;
		this.treeViewSelectionClearedDelegate = null;
		this.treeViewNodeExpandCollapseDelegate = null;
		this.treeViewCheckDelegate = null;
		this.treeViewNodeDropDelegate = null;		
	}	
	
	public abstract TreeNodeCollection getNodes();
	public abstract void clear();
	public abstract Object getValue();
	public abstract void setValue(Object value);
	public abstract TreeNode getSelectedNode();
	public abstract TreeNode getNodeByValue(Object value);
	public abstract void clearSelection();
	public abstract void collapseAll();
	public abstract void expandAll();	
	public abstract void beginEditSelectedNode();
	
	public abstract void moveUp();
	public abstract void moveDown();
	public abstract void moveTo(int toIndex);
	public abstract void swap(int index1, int index2);
	public abstract boolean canMoveCurrentUp();
	public abstract boolean canMoveCurrentDown();
	public abstract void setAllowDragDrop(boolean value);
	public abstract boolean isDragDropAllowed();
	
	public abstract void setUnselectable(boolean value);
	public abstract boolean isUnselectable();
	
	public void setTreeViewNodeEditedEvent(TreeViewNodeEdited delegate)
	{
		this.treeViewNodeEditedDelegate = delegate;
	}
	public void setTreeViewSelectionChangedEvent(TreeViewSelectionChanged delegate)
	{
		this.treeViewSelectionChangedDelegate = delegate;
	}
	public void setTreeViewSelectionClearedEvent(TreeViewSelectionCleared delegate)
	{
		this.treeViewSelectionClearedDelegate = delegate;
	}
	public void setTreeViewNodeExpandedCollapsedEvent(TreeViewNodeExpandedCollapsed delegate)
	{
		this.treeViewNodeExpandCollapseDelegate = delegate;
	}
	public void setTreeViewNodeDroppedEvent(TreeViewNodeDropped delegate)
	{
		this.treeViewNodeDropDelegate = delegate;
	}
	public void setTreeViewCheckEvent(TreeViewCheck delegate)
	{
		this.treeViewCheckDelegate = delegate;
	}
	
	protected TreeViewNodeEdited treeViewNodeEditedDelegate = null;
	protected TreeViewSelectionChanged treeViewSelectionChangedDelegate = null;
	protected TreeViewSelectionCleared treeViewSelectionClearedDelegate = null;
	protected TreeViewNodeExpandedCollapsed treeViewNodeExpandCollapseDelegate = null;
	protected TreeViewCheck treeViewCheckDelegate = null;
	protected TreeViewNodeDropped treeViewNodeDropDelegate;
}
