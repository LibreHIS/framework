package ims.framework.delegates;

import ims.framework.controls.TreeNode;

public interface TreeViewNodeDropped
{
	public void handle(TreeNode value, TreeNode previousParentNode) throws ims.framework.exceptions.PresentationLogicException;
}
