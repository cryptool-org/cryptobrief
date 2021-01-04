package sunset.gui.search.advanced;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.*;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;

public class AdvancedReplaceTest {

	static IAdvancedSearchReplace _searchReplace;

	@BeforeAll
	static void setUp() throws Exception {
		_searchReplace = new AdvancedSearchReplace();
	}

	@Test
	void testAllVariables() {
		try {
			_searchReplace.find("aabbcc", "%1", 0, false);
			Assert.assertEquals("aabbcc", _searchReplace.getReplaceString("%1"));
			Assert.assertEquals("daabbccf", _searchReplace.getReplaceString("d%1f"));
			Assert.assertEquals("aabbccf", _searchReplace.getReplaceString("%1f"));
			Assert.assertEquals("daabbcc", _searchReplace.getReplaceString("d%1"));
			
			_searchReplace.find("abc", "a%1c", 0, false);
			Assert.assertEquals("b", _searchReplace.getReplaceString("%1"));
			Assert.assertEquals("abc", _searchReplace.getReplaceString("a%1c"));
			Assert.assertEquals("bbb", _searchReplace.getReplaceString("%1%1%1"));
			Assert.assertEquals("bab", _searchReplace.getReplaceString("%1a%1"));
			
			_searchReplace.find("aaaaabcd", "%0b", 0, false);
			Assert.assertEquals("aaaaaaa", _searchReplace.getReplaceString("a%0a"));
			
			_searchReplace.find("aaaaabcd", "b%9", 0, false);
			Assert.assertEquals("cd", _searchReplace.getReplaceString("%9"));
			
			_searchReplace.find("aabbccdd", "%0c%1", 0, false);
			Assert.assertEquals("cddxaabb", _searchReplace.getReplaceString("%1x%0"));
			Assert.assertEquals("cddaabbcddaabb", _searchReplace.getReplaceString("%1%0%1%0"));
			
			_searchReplace.find("aabbcd", "a%1b%2c", 0, false);
			Assert.assertEquals("aabbc", _searchReplace.getReplaceString("a%1b%2c"));
			Assert.assertEquals("babca", _searchReplace.getReplaceString("%2abc%1"));
			Assert.assertEquals("aba", _searchReplace.getReplaceString("%1%2%1"));
			
			_searchReplace.find("aabccdeefgg", "%1b%2d%3f%4", 0, false);
			Assert.assertEquals("ggeeccaa", _searchReplace.getReplaceString("%4%3%2%1"));
			Assert.assertEquals("aaaacccceeeegggg", _searchReplace.getReplaceString("%1%1%2%2%3%3%4%4"));
			
			_searchReplace.find("ac", "a%7c", 0, false);
			Assert.assertEquals("ca", _searchReplace.getReplaceString("c%7%7%7a"));
			
			_searchReplace.find("aabccdeefgg", "%8b%4d%0f%5", 0, false);
			Assert.assertEquals("eebggdccfaa", _searchReplace.getReplaceString("%0b%5d%4f%8"));			
			
			_searchReplace.find("bdf", "%8b%4d%0f%5", 0, false);
			Assert.assertEquals("bdf", _searchReplace.getReplaceString("%8b%4d%0f%5"));
			
			_searchReplace.find("abcdaabbccdde", "a%8b%4c%0d%5e", 0, false);
			Assert.assertEquals("eaabbccdddcba", _searchReplace.getReplaceString("e%5d%0c%4b%8a"));
			
			_searchReplace.find("abcdababcdaabbccdde", "a%8b%4c%0d%5e", 0, false);
			Assert.assertEquals("ababcdaabbccddababcdaabbccdd", _searchReplace.getReplaceString("%5%0%4%5%8"));
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	@Test
	void testNotAllVariables() {
		try {
			_searchReplace.find("aabbccdd", "%0c%1", 0, false);
			Assert.assertEquals("x", _searchReplace.getReplaceString("x"));
			Assert.assertEquals("cddx", _searchReplace.getReplaceString("%1x"));
			Assert.assertEquals("xaabb", _searchReplace.getReplaceString("x%0"));
			Assert.assertEquals("aabbaabb", _searchReplace.getReplaceString("%0%0"));
			
			_searchReplace.find("aabbcd", "a%1b%2c", 0, false);
			Assert.assertEquals("aabca", _searchReplace.getReplaceString("%1abc%1"));
			Assert.assertEquals("aaa", _searchReplace.getReplaceString("%1%1%1"));
			
			_searchReplace.find("aabccdeefgg", "%1b%2d%3f%4", 0, false);
			Assert.assertEquals("abc", _searchReplace.getReplaceString("abc"));
			Assert.assertEquals("ggccggcc", _searchReplace.getReplaceString("%4%2%4%2"));
			Assert.assertEquals("aaaacccc", _searchReplace.getReplaceString("%1%1%2%2"));
			
			_searchReplace.find("aabccdeefgg", "%8b%4d%0f%5", 0, false);
			Assert.assertEquals("", _searchReplace.getReplaceString(""));
			Assert.assertEquals("ee", _searchReplace.getReplaceString("%0"));
			
			_searchReplace.find("bdf", "%8b%4d%0f%5", 0, false);
			Assert.assertEquals("bdf", _searchReplace.getReplaceString("bdf"));
			Assert.assertEquals("", _searchReplace.getReplaceString("%8%8"));
			
			_searchReplace.find("abcdaabbccdde", "a%8b%4c%0d%5e", 0, false);
			Assert.assertEquals("aaabbccddaaa", _searchReplace.getReplaceString("a%5a%8a%4a"));
			
			_searchReplace.find("abcdababcdaabbccdde", "a%8b%4c%0d%5e", 0, false);
			Assert.assertEquals("abcababcdaabbccddxyz", _searchReplace.getReplaceString("abc%5xyz"));
		} catch (InvalidPatternException e) {
			Assert.fail();
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	@Test
	void testSpecialCharacters() {
		try {
			_searchReplace.find("%%%", "%1%", 0, false);
			Assert.assertEquals("", _searchReplace.getReplaceString("%1%1"));
			
			_searchReplace.find("$%§", "%1§", 0, false);
			Assert.assertEquals("$%$%", _searchReplace.getReplaceString("%1%1"));
			
			_searchReplace.find("!\"§$%&\\()=", "\"%1\\", 0, false);
			Assert.assertEquals("\\§$%&\\", _searchReplace.getReplaceString("\\%1\\"));
			
			_searchReplace.find("!\"§$%&\\()=", "%4\"%1\\%6", 0, false);
			Assert.assertEquals("-§$%&-!-()=-", _searchReplace.getReplaceString("-%1-%4-%6-"));
			
			_searchReplace.find("!\"§$%&\\()=", "!%0=", 0, false);
			Assert.assertEquals("=\"§$%&\\()!", _searchReplace.getReplaceString("=%0!"));
			
			_searchReplace.find("!\"§$\n%&\\()=", "%0\n%1", 0, false);
			Assert.assertEquals("\n%&\\()=\t!\"§$\b", _searchReplace.getReplaceString("\n%1\t%0\b"));
			
			_searchReplace.find("!\"§$%&\\()=", "%1!%2", 0, false);
			Assert.assertEquals("!\"§$%&\\()=\"§$%&\\()=!", _searchReplace.getReplaceString("!%2%1%2!"));
			
			_searchReplace.find("!\"§$%&\\()=", "!%9", 0, false);
			Assert.assertEquals("\"§$%&\\()=!", _searchReplace.getReplaceString("%9!"));
			
			_searchReplace.find("1%%%%1%%%1%%%%1", "%1%", 0, false);
			Assert.assertEquals("1%-1%", _searchReplace.getReplaceString("%1%-%1%"));
			
			_searchReplace.find("$%%%%&%%%?%%%%!", "$%1&%2?%3!", 0, false);
			Assert.assertEquals("%%%%%%%%%%%", _searchReplace.getReplaceString("%3%2%1"));
			Assert.assertEquals("%%%%-%%%-%%%%", _searchReplace.getReplaceString("%3-%2-%1"));
			
			_searchReplace.find("$%%%%&%%%?%%%%!", "%4$%1&%2?%3!%0", 0, false);
			Assert.assertEquals("%%%%%%%%%%%", _searchReplace.getReplaceString("%0%1%2%3%4"));
			
			_searchReplace.find("$$%$$$%$$$%", "%1%$%2", 0, false);
			Assert.assertEquals("$$$$%$$$%", _searchReplace.getReplaceString("%1%2"));
			Assert.assertEquals("$$%$$$%$$$%", _searchReplace.getReplaceString("%1%$%2"));
			
			_searchReplace.find("$$%$$$%$?$%", "$$%1%$?%2", 0, false);
			Assert.assertEquals("", _searchReplace.getReplaceString(""));
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	@Test
	void testUndeclaredVariables() {
		UndeclaredVariableException e;
		String msg1 = "Variable %";
		String msg2 = " from replace text has not been used in search pattern!"; 
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "%1b%2", 0, false);
			_searchReplace.getReplaceString("%3");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "3" + msg2);
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "a%1c%2", 0, false);
		  	_searchReplace.getReplaceString("%1%2%3");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "3" + msg2);
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "%1b%2", 0, false);
		  	_searchReplace.getReplaceString("%0%1%2");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "0" + msg2);
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "a%1b%2c%3", 0, false);
		  	_searchReplace.getReplaceString("%0");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "0" + msg2);
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "%0a%1c%2", 0, false);
		  	_searchReplace.getReplaceString("%1%9");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "9" + msg2);
		
		e = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_searchReplace.find("abc", "%0b%1c%2", 0, false);
		  	_searchReplace.getReplaceString("%1%4");
		  });
		
		Assert.assertEquals(e.getMessage(), msg1 + "4" + msg2);
	}
	
	@Test
	void testComplexReplacements() {
		try {
			/* ______________________________ TEST ______________________________ */
			
			String text = "Assert.assertTrue(_searchReplace.find(\"!\\\"§$%&\\\\()=\", \"!%0=\", 0, false));\n"
					+ "\t\t\tcheckResult(0,10,new String[] {\"\\\"§$%&\\\\()\", null, null, null, null, null, null, null, null, null});";
			String pattern = "Assert.assertTrue(%1);\n\t\t\tcheckResult(%2);";
			
			_searchReplace.find(text, pattern, 0, false);
			
			String newText = "_searchReplace.find(\"!\\\"§$%&\\\\()=\", \"!%0=\", 0, false);\n"
					+ "\t\t\tAssert.assertEquals(\"\", _searchReplace.getReplaceString(\"\"));";
			String replaceText = "%1;\n\t\t\tAssert.assertEquals(\"\", _searchReplace.getReplaceString(\"\"));";
			
			Assert.assertEquals(newText, _searchReplace.getReplaceString(replaceText));
			
			/* ______________________________ TEST ______________________________ */
			
			text = "e = Assert.assertThrows(InvalidPatternException.class, () -> {"
					+ "\n\t\t\t_searchReplace.find(\"abc\", \"a%1b%2%3\", 0, false);\n\t\t  });" 
					+ "\n\t\tAssert.assertEquals(e.getMessage(), msg + \"%2%3\");";
			
			pattern = "e = Assert.assertThrows(InvalidPatternException.class, () -> {%1});%3Assert.assertEquals(e.getMessage(), %2);";
			
			_searchReplace.find(text, pattern, 0, false);
			
			newText = "e = Assert.assertThrows(UndeclaredVariableException.class, () -> {"
					+ "\n\t\t\t_searchReplace.find(\"abc\", \"a%1b%2%3\", 0, false);\n\t\t  "
					+ "\n\t\t\t_searchReplace.getReplaceString(\"\")\n\t\t  });"
					+ "\n\t\tAssert.assertEquals(e.getMessage(), msg1 + \"\" + msg2);";
			replaceText = "e = Assert.assertThrows(UndeclaredVariableException.class, () -> {"
					+ "%1\n\t\t\t_searchReplace.getReplaceString(\"\")\n\t\t  });"
					+ "%3Assert.assertEquals(e.getMessage(), msg1 + \"\" + msg2);";
			
			Assert.assertEquals(newText, _searchReplace.getReplaceString(replaceText));
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	@AfterAll
	static void tearDown() {
		_searchReplace = null;
	}
}
