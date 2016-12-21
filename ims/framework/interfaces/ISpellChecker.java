package ims.framework.interfaces;

public interface ISpellChecker
{
	ISpellCheckResult spellCheck(String text);	
	boolean addToDictionary(String text);
	boolean canAddToDictionary();
}
