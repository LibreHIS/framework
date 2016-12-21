package ims.framework.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import ims.framework.interfaces.IDictionaryProvider;
import ims.framework.interfaces.ISpellCheckError;
import ims.framework.interfaces.ISpellCheckResult;
import ims.framework.interfaces.ISpellChecker;

public class JazzySpellChecker implements ISpellChecker
{
	private IDictionaryProvider dictionaryProvider;
	private SpellCheckResult result;	
	private SpellChecker spellChecker;
	
	public JazzySpellChecker(IDictionaryProvider dictionaryProvider)
	{
		this.dictionaryProvider = dictionaryProvider;
		spellChecker = new SpellChecker();
		
		SpellDictionaryHashMap map;
		try
		{
			map = new SpellDictionaryHashMap();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		String[] words = dictionaryProvider.getWords();
		
		for(int x = 0; x < words.length; x++)
		{
			map.addWord(words[x]);
		}
		
		spellChecker.setUserDictionary(map);
	}
		
	public boolean addToDictionary(String text)
	{
		if(!canAddToDictionary())
			return false;
		
		if(text == null || text.trim().length() == 0)
			return false;
		
		boolean result = dictionaryProvider.addWord(text);
		if(result)
			spellChecker.addToDictionary(text);
		return result;
	}
	public boolean canAddToDictionary()
	{
		return !dictionaryProvider.isReadOnly();
	}
	public ISpellCheckResult spellCheck(String text)
	{
		if(text == null || text.trim().length() == 0)
			return new SpellCheckResult("");
		
		result = new SpellCheckResult(text);
				
		SpellCheckListener listner = new SpellCheckListener()
		{
			public void spellingError(SpellCheckEvent event)
			{
				SpellCheckError error = result.getError(event.getInvalidWord());
				if(error == null)
					error = new SpellCheckError(event.getInvalidWord());
				
				error.addPosition(event.getWordContextPosition());
				List suggestions = event.getSuggestions();
				for(int x = 0; x < suggestions.size(); x++)
				{
					error.addSuggestedWord(suggestions.get(x).toString());
				}
				result.setError(error);
			}
		};
		
		spellChecker.addSpellCheckListener(listner); 
		spellChecker.checkSpelling(new StringWordTokenizer(text));
		spellChecker.removeSpellCheckListener(listner);
		
		return result;
	}
	
	public class SpellCheckResult implements ISpellCheckResult
	{
		private String originalText;
		private List<SpellCheckError> errors = new ArrayList<SpellCheckError>();
		
		public SpellCheckResult(String originalText)
		{
			if(originalText == null)
				throw new RuntimeException("Invalid spell check text");
			
			this.originalText = originalText;
		}
				
		public SpellCheckError getError(String word)
		{
			for(int x = 0; x < errors.size(); x++)
			{
				if(errors.get(x).getErrorWord().equals(word))
					return errors.get(x);
			}
			
			return null;
		}
		
		public void setError(SpellCheckError error)
		{
			if(error == null)
				throw new RuntimeException("Invalid spell check error");
			
			int index = errors.indexOf(error);
			if(index < 0)
				errors.add(error);	
			else
				errors.set(index, error);
		}
		
		public ISpellCheckError[] getErrors()
		{
			ISpellCheckError[] result = new ISpellCheckError[errors.size()];
			
			for(int x = 0; x < errors.size(); x++)
			{
				result[x] = errors.get(x);
			}
			
			return result;
		}
		public String getOriginalText()
		{
			return originalText;
		}		
	}
	public class SpellCheckError implements ISpellCheckError
	{
		private String word;
		private List<Integer> positions = new ArrayList<Integer>();
		private List<String> suggestedWords = new ArrayList<String>();
		
		public SpellCheckError(String word)
		{
			if(word == null || word.trim().length() == 0)
				throw new RuntimeException("Invalid word");
			this.word = word;
		}
		
		public void addPosition(int position)
		{
			if(position < 0)
				throw new RuntimeException("Invalid word position");
			
			if(positions.indexOf(position) < 0)
				positions.add(position);
		}
		public void addSuggestedWord(String word)
		{
			if(word == null || word.trim().length() == 0)
				throw new RuntimeException("Invalid suggested word");
			
			if(suggestedWords.indexOf(word) < 0)			
				suggestedWords.add(word);
		}		
		
		public String getErrorWord()
		{
			return word;
		}
		public int[] getPositions()
		{
			int[] result = new int[positions.size()];
			
			for(int x = 0; x < positions.size(); x++)
			{
				result[x] = positions.get(x);
			}
			
			return result;
		}
		public String[] getSuggestedWords()
		{
			String[] result = new String[suggestedWords.size()];
			
			for(int x = 0; x < suggestedWords.size(); x++)
			{
				result[x] = suggestedWords.get(x);
			}
			
			return result;
		}		
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj instanceof SpellCheckError)
				return word.equals(((SpellCheckError)obj).word);
			return false;
		}
		@Override
		public String toString()
		{
			return word;
		}
	}
}
