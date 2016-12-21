package ims.framework.delegates;

import ims.framework.controls.TreeNode;

public interface TreeViewSelectionChanged
{
	public void handle(TreeNode value) throws ims.framework.exceptions.PresentationLogicException;
}
