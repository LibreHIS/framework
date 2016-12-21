package ims.framework.interfaces;

public interface ISpellCheckResult
{
	String getOriginalText();
	ISpellCheckError[] getErrors();
}
