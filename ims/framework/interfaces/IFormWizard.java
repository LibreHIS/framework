package ims.framework.interfaces;

public interface IFormWizard
{
	boolean allowWizardNext();
	boolean allowWizardPrevious();
	boolean allowWizardCancel();
	boolean allowWizardFinish();
	
	boolean onWizardNext();
	boolean onWizardPrevious();
	boolean onWizardCancel();
	boolean onWizardFinish();
}
