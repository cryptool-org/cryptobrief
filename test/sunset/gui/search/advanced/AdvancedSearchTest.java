package sunset.gui.search.advanced;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.*;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.exception.SearchIndexOutOfBoundsException;
import sunset.gui.search.logic.SearchContext;
import sunset.gui.search.util.SearchReplaceMessageHandler;

class AdvancedSearchTest {
	
	static IAdvancedSearchReplace _searchReplace;

	@BeforeAll
	static void setUp() throws Exception {
		String matchingPairs = "(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}";
		_searchReplace = new AdvancedSearchReplace(matchingPairs);
	}
	
	void checkResult(int start, int end, String[] captures) {
		System.out.println("Expected:\t" + start + "," + end + "," + convertArrayToString(captures));
		assertEquals(start, _searchReplace.getStart());
		assertEquals(end, _searchReplace.getEnd());
		assertArrayEquals(captures, _searchReplace.getCaptures());
	}
	
	private String convertArrayToString(String[] array) {
		String result = "";
		
		if (array.length == 0) {
			return "";
		}
		
		for (String s : array) {
			result += s + ",";
		}
		
		return result.substring(0, result.length()-1);
	}

	@Test
	void testSimpleCases() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("aabbcc", "%1", 0, false), true));
			checkResult(0,6,new String[] {null, "aabbcc", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("abc", "a%1c", 0, false), true));
			checkResult(0,3,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 0, false), true));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 0, false), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 0, false), true));
			checkResult(0,8,new String[] {"aabb", "cdd", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 0, false), true));
			checkResult(0,5,new String[] {null, "a", "b", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "%1b%2d%3f%4", 0, false), true));
			checkResult(0,11,new String[] {null, "aa", "cc", "ee", "gg", null, null, null, null, null});

			assertTrue(_searchReplace.find(new SearchContext("ac", "a%7c", 0, false), true));
			checkResult(0,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			assertTrue(_searchReplace.find(new SearchContext("bdf", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			assertTrue(_searchReplace.find(new SearchContext("bdf", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			assertTrue(_searchReplace.find(new SearchContext("abcdaabbccdde", "a%8b%4c%0d%5e", 0, false), true));
			checkResult(0,13,new String[] {"", null, null, null, "", "aabbccdd", null, null, "", null});
			
			assertTrue(_searchReplace.find(new SearchContext("abcdababcdaabbccdde", "a%8b%4c%0d%5e", 0, false), true));
			checkResult(0,19,new String[] {"", null, null, null, "", "ababcdaabbccdd", null, null, "", null});
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFalseCasesSimple() {
		try {
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "a%1d", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1d", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "d%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "aabbcc%1a", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "c%2a", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%0a%1a%2a", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "b%6b%8b", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1aaa", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "aaa%2", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1c%2a", 0, false), true));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testSpecialCharacters() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("%%%", "%1%", 0, false), true));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("$%§", "%1§", 0, false), true));
			checkResult(0,3,new String[] {null, "$%", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "\"%1\\", 0, false), true));
			checkResult(1,7,new String[] {null, "§$%&", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%4\"%1\\%6", 0, false), true));
			checkResult(0,10,new String[] {null, "§$%&", null, null, "!", null, "()=", null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%0=", 0, false), true));
			checkResult(0,9,new String[] {"\"§$%&()", null, null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$\n%&()=", "%0\n%1", 0, false), true));
			checkResult(0,10,new String[] {"!\"§$", "%&()=", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%1!", 0, false), true));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%9", 0, false), true));
			checkResult(0,9,new String[] {null, null, null, null, null, null, null, null, null, "\"§$%&()="});
			
			assertTrue(_searchReplace.find(new SearchContext("1%%%%1%%%1%%%%1", "%1%", 0, false), true));
			checkResult(0,2,new String[] {null, "1", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "$%1&%2?%3!", 0, false), true));
			checkResult(0,15,new String[] {null, "%%%%", "%%%", "%%%%", null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "%4$%1&%2?%3!%0", 0, false), true));
			checkResult(0,15,new String[] {"", "%%%%", "%%%", "%%%%", "", null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("$$%$$$%$$$%", "%1%$%2", 0, false), true));
			checkResult(0,11,new String[] {null, "$$", "$$%$$$%", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("$$%$$$%$?$%", "$$%1%$?%2", 0, false), true));
			checkResult(0,11,new String[] {null, "%$$$", "$%", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\Q()\\Ed", "\\E%1d", 0, false), true));
			checkResult(4,7,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\Q(\\Q)\\E", "\\Q%1\\E", 0, false), true));
			checkResult(0,8,new String[] {null, "(\\Q)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\Q()\\Ed", "\\Q%1d", 0, false), true));
			checkResult(0,7,new String[] {null, "()\\E", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\E()\\Qd", "\\E%1d", 0, false), true));
			checkResult(0,7,new String[] {null, "()\\Q", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFalseCasesSpecialCharacters() {
		try {
			assertFalse(_searchReplace.find(new SearchContext("%%%", "%1%%2%", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("$%§", "%5$%1§%", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "\"%1\"\\", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%4\"!%1\\%6", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%0=%1!", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$\n%&()=", "%0\r%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%1!%", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%9(=", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("1%%%%1%%%1%%%%1", "1%11%21%31%41", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "$%1&%2!%3?", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "%4$$%1&%2?%3!%0", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("$$%$$$%$$$%", "%1%$%2$%$%", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("$$%$$$%$?$%", "$$%1%$?%2%$", 0, false), true));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testNumbers() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("0123456789", "0%99", 0, false), true));
			checkResult(0,10,new String[] {null, null, null, null, null, null, null, null, null, "12345678"});
			
			assertTrue(_searchReplace.find(new SearchContext("0123456789", "%00%11%22", 0, false), true));
			checkResult(0,3,new String[] {"", "", "", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("0123456789", "0%12%34%56%78%9", 0, false), true));
			checkResult(0,10,new String[] {null, "1", null, "3", null, "5", null, "7", null, "9"});
			
			assertTrue(_searchReplace.find(new SearchContext("0123456789", "%01%12%34%56%78%9", 0, false), true));
			checkResult(0,10,new String[] {"0", "", null, "3", null, "5", null, "7", null, "9"});
			
			assertTrue(_searchReplace.find(new SearchContext("0", "0%9", 0, false), true));
			checkResult(0,1,new String[] {null, null, null, null, null, null, null, null, null, ""});
			
			assertTrue(_searchReplace.find(new SearchContext("0", "%00", 0, false), true));
			checkResult(0,1,new String[] {"", null, null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("0123456789", "0123%56789", 0, false), true));
			checkResult(0,10,new String[] {null, null, null, null, null, "45", null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("0123401234567", "012345%5", 0, false), true));
			checkResult(5,13,new String[] {null, null, null, null, null, "67", null, null, null, null});
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFalseCasesNumbers() {
		try {
			assertFalse(_searchReplace.find(new SearchContext("0123456789", "9%90", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0123456789", "1%00%11%22", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0123456789", "0%12%34%56%78%90", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0123456789", "%010%12%34%56%78%9", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0", "0%90", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0", "%000", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0123456789", "012345%556789", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("0123401234567", "%18%2", 0, false), true));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testStartPositions() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 4, false), true));
			checkResult(4,6,new String[] {"a", null, null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 3, false), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 5, false), true));
			checkResult(5,8,new String[] {"", "dd", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 1, false), true));
			checkResult(1,5,new String[] {null, "", "b", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "f%4", 8, false), true));
			checkResult(8,11,new String[] {null, null, null, null, "gg", null, null, null, null, null});

			assertTrue(_searchReplace.find(new SearchContext("cc", "c%7", 1, false), true));
			checkResult(1,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			assertTrue(_searchReplace.find(new SearchContext("cccc", "c%7c", 1, false), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("cccc", "c%7c", 1, false), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("ccdc", "c%7c", 1, false), true));
			checkResult(1,4,new String[] {null, null, null, null, null, null, null, "d", null, null});
			
			assertFalse(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 7, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 7, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 6, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 3, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabccdeefgg", "f%4", 10, false), true));
			assertFalse(_searchReplace.find(new SearchContext("cc", "c%7", 2, false), true));			
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testMatchCase() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("aBc", "a%1c", 0, true), true));
			checkResult(0,3,new String[] {null, "B", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aaaaAaaaa", "%0A%1", 0, true), true));
			checkResult(0,9,new String[] {"aaaa", "aaaa", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("AAAAaAAAA", "%0a%1", 0, true), true));
			checkResult(0,9,new String[] {"AAAA", "AAAA", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aaaaaBcd", "%0B", 0, true), true));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aAAaabCD", "b%9", 0, true), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "CD"});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbcCdd", "%0C%1", 0, true), true));
			checkResult(0,8,new String[] {"aabbc", "dd", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("AaBbcd", "a%1b%2c", 0, true), true));
			checkResult(1,5,new String[] {null, "B", "", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aAbcCdeEfGg", "%1b%2d%3f%4", 0, true), true));
			checkResult(0,11,new String[] {null, "aA", "cC", "eE", "Gg", null, null, null, null, null});

			assertTrue(_searchReplace.find(new SearchContext("aACc", "a%7c", 0, true), true));
			checkResult(0,4,new String[] {null, null, null, null, null, null, null, "AC", null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("AacC", "a%7c", 0, true), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});

			assertTrue(_searchReplace.find(new SearchContext("aaBccDeeFgg", "%8B%4D%0F%5", 0, true), true));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			assertTrue(_searchReplace.find(new SearchContext("bDf", "%8b%4D%0f%5", 0, true), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			assertTrue(_searchReplace.find(new SearchContext("abcdeAabbccdde", "A%8b%4c%0d%5e", 0, true), true));
			checkResult(5,14,new String[] {"c", null, null, null, "b", "d", null, null, "a", null});
			
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "A%1c", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1b", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1h", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "%0d", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "G%9", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "aBcd%8H", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("AaaaaaaaA", "a%1A%2a", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aaaB", "a%0a%1ab", 0, true), true));
			assertFalse(_searchReplace.find(new SearchContext("aBcdEaaBBccdde", "a%8b%4c%0d%5e", 0, true), true));
			
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "A%1c", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1b", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1h", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "%0d", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "G%9", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcDeFgH", "aBcd%8H", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("AaaaaaaaA", "a%1A%2a", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aaaB", "a%0a%1ab", 0, false), true));
			assertTrue(_searchReplace.find(new SearchContext("aBcdEaaBBccdde", "a%8b%4c%0d%5e", 0, false), true));
			
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testInvalidPattern1() {
		InvalidPatternException e;
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%1%2"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%1%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%1%2"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1%2b", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%1%2"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%1b%2%3", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%2%3"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0%1c%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%0%1"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0%1%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_missingdelim", "%0%1"), e.getMessage());
	}
	
	@Test
	void testInvalidPattern2() {
		InvalidPatternException e;
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0a%0c%0", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "0"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0b%1c%0", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "0"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%0b%2c%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "2"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1a%1b%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "1"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabbccdd", "%0c%0", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "0"), e.getMessage());

		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabbcd", "a%2b%2c", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "2"), e.getMessage());

		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabccdeefgg", "%1b%1d%2f%3", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "1"), e.getMessage());

		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabccdeefgg", "%8b%8d%2f%2", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_varmorethanonce", "8"), e.getMessage());
	}
	
	@Test
	void testInvalidPattern3() {
		InvalidPatternException e;
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "b", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "aabbcc", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "dd", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("abc", "abc", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("a", "a", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("abc", "", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("$%§", "%", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("!\"§$%&\\()=", "abc", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%1", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1%2%3%4bcc", "%%1%%2%%3%%4", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%1b", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1c", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1c%%2", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%0a%%1c", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
		
		e = assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%0a%%1c%%2", 0, false), true);
		  });
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_invalidpattern_novarused"), e.getMessage());
	}

	@Test
	void testInvalidInputs() {
		SearchIndexOutOfBoundsException e;
		
		e = assertThrows(SearchIndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1a%2", -1, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_searchindexoutofbounds", "-1"), e.getMessage());
		
		e = assertThrows(SearchIndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1b%2", 5, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_searchindexoutofbounds", "5"), e.getMessage());
		
		e = assertThrows(SearchIndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("", "%1c%2", 1, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_searchindexoutofbounds", "1"), e.getMessage());
	}

	@Test
	void testEscapedVariables() {
		try {			
			assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%%1%1", 0, false), true));
			checkResult(3,8,new String[] {null, "bcc", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1", 0, false), true));
			checkResult(0,5,new String[] {null, "aab", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1%2", 0, false), true));
			checkResult(0,8,new String[] {null, "aab", "bcc", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%%1a%1c%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%%1%1%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "abc", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%0%%1%1%%1%2", 0, false), true));
			checkResult(0,7,new String[] {"", "abc", "", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("%1a%1c%1", "%%1%1%%1%2%%1", 0, false), true));
			checkResult(0,8,new String[] {null, "a", "c", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aab%1%2%3%4bcc", "%8%%1%%2%%3%%4%9", 0, false), true));
			checkResult(0,14,new String[] {null, null, null, null, null, null, null, null, "aab", "bcc"});
			
			assertTrue(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%1%2b%9", 0, false), true));
			checkResult(0,12,new String[] {null, null, "%2%4", null, null, null, null, null, "aab", "cc"});
			
			assertTrue(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%2%9", 0, false), true));
			checkResult(0,12,new String[] {null, null, null, null, null, null, null, null, "aab%1", "%4bcc"});
			
			assertTrue(_searchReplace.find(new SearchContext("%1aaaaab", "%%1%1b", 0, false), true));
			checkResult(0,8,new String[] {null, "aaaaa", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("aabbc%1d", "a%1%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "abbc", null, null, null, null, null, null, null, null});
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFalseCasesEscapedVariables() {
		try {			
			assertFalse(_searchReplace.find(new SearchContext("aab%1bcc", "%%1c%1", 0, false), true));	
			assertFalse(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1%%1", 0, false), true));	
			assertFalse(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1%2%%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("%1abc%1", "%%1a%1%%1c%%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("%1abc%1", "%1%%1%%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("%1abc%1", "%0%%1%1%%2%2", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("%1a%1c%1", "%%1%1%%%1%2%%1", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aab%1%2%3%4bcc", "%8%%1%%3%%4%9", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%1%2b%%4%9", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%3%9", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("%1aaaaab", "%%1b%1a", 0, false), true));
			assertFalse(_searchReplace.find(new SearchContext("aabbc%1d", "a%1d%%1", 0, false), true));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testSpecialSymbols() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("abc\tdef\nghi\r\ntyz", "a%1f%2\r\n%3z", 0, false), true));
			checkResult(0,16,new String[] {null, "bc\tde", "\nghi", "ty", null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("abc\tdef\nghi\r\ntyz", "abc%1def%2ghi%3t", 0, false), true));
			checkResult(0,14,new String[] {null, "\t", "\n", "\r\n", null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\n\n\n\t\t\t\n\n\n", "\n%1\t\n%2", 0, false), true));
			checkResult(0,9,new String[] {null, "\n\n\t\t", "\n\n", null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\nn\tt\rr", "%1", 0, false), true));
			checkResult(0,6,new String[] {null, "\nn\tt\rr", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\nn\\tt\\rr", "\\%1r", 0, false), true));
			checkResult(0,8,new String[] {null, "nn\\tt\\", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("abc\r\n\t\tdef\r\t\t\txyz", "%1\r\n%2\t\t%3z", 0, false), true));
			checkResult(0,17,new String[] {null, "abc", "", "def\r\t\t\txy", null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\n\r\tb\\n\\rc\\t\nd\r\te", "%1\n\r%2\\n%3c%4\n%5e", 0, false), true));
			checkResult(0,17,new String[] {null, "a", "\tb", "\\r", "\\t", "d\r\t", null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testCorrectBalancing1() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("a(a)a", "a%1a", 0, false), true));
			checkResult(0,5,new String[] {null, "(a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a(a(a)a)a", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "(a(a)a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a(a(aa))a()a", "a%1a", 0, false), false));
			checkResult(0,9,new String[] {null, "(a(aa))", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a((a))aa", "a%1a", 0, false), true));
			checkResult(0,7,new String[] {null, "((a))", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a(a)(a)a", "a%1a", 0, false), true));
			checkResult(0,8,new String[] {null, "(a)(a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a()a", "a%1a", 0, false), true));
			checkResult(0,4,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a((a)(a))a", "a%1a", 0, false), false));
			checkResult(0,10,new String[] {null, "((a)(a))", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a((a()(a())))a", "a%1a", 0, false), false));
			checkResult(0,14,new String[] {null, "((a()(a())))", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testCorrectBalancing2() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("a{(a)}a", "a%1a", 0, false), true));
			checkResult(0,7,new String[] {null, "{(a)}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({a}{(a)}a)a", "a%1a", 0, false), false));
			checkResult(0,13,new String[] {null, "({a}{(a)}a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({(a)})aa", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "({(a)})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({(a)})a{()}a", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "({(a)})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{(a){}(a)}a", "a%1a", 0, false), false));
			checkResult(0,12,new String[] {null, "{(a){}(a)}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({})a", "a%1a", 0, false), true));
			checkResult(0,6,new String[] {null, "({})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({({a})({a})})a", "a%1a", 0, false), true));
			checkResult(0,16,new String[] {null, "({({a})({a})})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a({(a()(a()))})a", "a%1a", 0, false), false));
			checkResult(0,16,new String[] {null, "({(a()(a()))})", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testCorrectBalancing3() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("a(\\begin{center}{a}{(a)}\\end{center}{a})a", "a%1a", 0, false), true));
			checkResult(0,41,new String[] {null, "(\\begin{center}{a}{(a)}\\end{center}{a})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{group1}a\\begin{group2}a\\end{group2}\\end{group1}a", "a%1a", 0, false), true));
			checkResult(0,56,new String[] {null, "\\begin{group1}a\\begin{group2}a\\end{group2}\\end{group1}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}a\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,29,new String[] {null, "\\begin{center}a\\end{center}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{\\begin{center}(a){}(a)\\end{center}}a", "a%1a", 0, false), true));
			checkResult(0,38,new String[] {null, "{\\begin{center}(a){}(a)\\end{center}}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}({})a(){()}\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,39,new String[] {null, "\\begin{center}({})a(){()}\\end{center}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{group1}a\\begin{group2}a\\end{group2}a\\begin{group3}aaa\\end{group3}aa\\end{group1}a\\begin{group4}a\\end{group4}a", "a%1a", 0, false), true));
			checkResult(0,88,new String[] {null, "\\begin{group1}a\\begin{group2}a\\end{group2}a\\begin{group3}aaa\\end{group3}aa\\end{group1}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}({(a()(a()))})a\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,43,new String[] {null, "\\begin{center}({(a()(a()))})a\\end{center}", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testCorrectBalancing4() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("({})", "(%1)", 0, false), true));
			checkResult(0,4,new String[] {null, "{}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("(\\begin{center}{}\\end{center})", "(%1)", 0, false), true));
			checkResult(0,30,new String[] {null, "\\begin{center}{}\\end{center}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\begin{center}(\\begin{center}{}\\end{center})\\end{center}", "\\begin{center}%1\\end{center}", 0, false), true));
			checkResult(0,56,new String[] {null, "(\\begin{center}{}\\end{center})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\begin{center}(\\begin{lstlisting}{}\\end{lstlisting})\\end{center}", "\\begin{center}%1\\end{center}", 0, false), true));
			checkResult(0,64,new String[] {null, "(\\begin{lstlisting}{}\\end{lstlisting})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\begin{lstlisting}(\\begin{center}{}\\end{center})\\end{lstlisting}", "(\\begin{center}%1\\end{center})", 0, false), true));
			checkResult(18,48,new String[] {null, "{}", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testCorrectBalancing5() {
		try {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), true));
			assertEquals(0, searchReplace.getStart());
			assertEquals(32, searchReplace.getEnd());
			assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{x-y-z}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
			
			searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%3-%2-%1}");
			assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}def", "a%1f", 0, false), true));
			assertEquals(0, searchReplace.getStart());
			assertEquals(32, searchReplace.getEnd());
			assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{z-y-x}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testCorrectBalancing6() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("(((()((())()))))", "(%1)", 0, false), true));
			checkResult(0,16,new String[] {null, "((()((())())))", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("(({[\\(\\)\\{\\}\\[\\]]}))", "(%1)", 0, false), true));
			checkResult(0,20,new String[] {null, "({[\\(\\)\\{\\}\\[\\]]})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("(({[\\(\\{\\[\\]\\}\\)]}))", "(%1)", 0, false), true));
			checkResult(0,20,new String[] {null, "({[\\(\\{\\[\\]\\}\\)]})", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\(\\(\\{\\[(){}[]\\]\\}\\)\\)", "\\(%1\\)", 0, false), true));
			checkResult(0,22,new String[] {null, "\\(\\{\\[(){}[]\\]\\}\\)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("\\((\\({\\{[\\[\\]]\\}}\\))\\)", "\\(%1\\)", 0, false), true));
			checkResult(0,22,new String[] {null, "(\\({\\{[\\[\\]]\\}}\\))", null, null, null, null, null, null, null, null});
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testConstructor() {
		try {
			new AdvancedSearchReplace(null);
			new AdvancedSearchReplace("");
			String matchingPairs = "(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}";
			new AdvancedSearchReplace(matchingPairs);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testInorrectBalancing1() {
		UnbalancedStringException e;

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext(")", "%1", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", ")"), e.getMessage());
		
		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a(a))aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "(a))"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a)a(a)aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", ")"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a((a)a()a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "((a)a()a"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a)(a)a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", ")("), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(ab)a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "b)"), e.getMessage());
	}
	
	@Test
	void testInorrectBalancing2() {
		UnbalancedStringException e;

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((a)})aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().
				getMessage("exception_unbalancedstring", "{((a)})"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{a(a})aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().
				getMessage("exception_unbalancedstring", "{a(a})"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a({(a)a(})a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().
				getMessage("exception_unbalancedstring", "({(a)a(})"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{)(a)a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().
				getMessage("exception_unbalancedstring", "{)("), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a({])b", "a%1b", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().
				getMessage("exception_unbalancedstring", "({])"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(b)a)}", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "{(b)a)}"), e.getMessage());
	}
	
	@Test
	void testInorrectBalancing3() {
		UnbalancedStringException e;

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "{((\\begin{center}{test}a)\\end{center}{test})}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}{a\\end{center}(a})aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}{a\\end{center}("), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}{(a)\\end{center}}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "{(\\begin{center}\\begin{center}b\\end{center})"), e.getMessage());
	}
	
	@Test
	void testInorrectBalancing4() {
		UnbalancedStringException e;

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "{((\\begin{center}{test}a)\\end{center}{test})}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}a\\end{string}a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}a\\end{string}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}{\\begin{string}(a)a\\end{string}\\end{center}}a", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}{\\begin{string}(a)a\\end{string}\\end{center}}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "\\begin{center}{(a)\\end{center}}"), e.getMessage());

		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "{(\\begin{center}\\begin{center}b\\end{center})"), e.getMessage());
	}
	
	@Test
	void testIncorrectBalancing5() {
		UnbalancedStringException e;

		e = assertThrows(UnbalancedStringException.class, () -> {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}def", "a%1f", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "bc\\beg{x-y-z}test\\end{z-y-x}de"), e.getMessage());
		
		e = assertThrows(UnbalancedStringException.class, () -> {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%3-%2-%1}");
			searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "bc\\beg{x-y-z}test\\end{x-y-z}de"), e.getMessage());
	}
	
	@Test
	void testInorrectBalancing6() {
		try {
			UnbalancedStringException e;

			e = assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("a(((()())))())())b", "a%1b", 0, false), true);
			  });
			
			assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "(((()())))())())"), e.getMessage());
			
			e = assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("(({[\\(\\{\\)\\}\\[\\]]}))", "(%1)", 0, false), true);
			  });
			
			assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "({[\\(\\{\\)\\}\\[\\]]}"), e.getMessage());
			
			e = assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("(({[\\(\\{\\)\\}\\[\\]]})", "(%1)", 0, false), true);
			  });
			
			assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "({[\\(\\{\\)\\}\\[\\]]}"), e.getMessage());
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test 
	void testComplexBalancing() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("a{a(a)a", "a%1a", 0, false), false));
			checkResult(2,7,new String[] {null, "(a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{a[a(a)a]a}a", "a%1a", 0, false), false));
			checkResult(0,13,new String[] {null, "{a[a(a)a]a}", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{a[(a)]a}a", "a%1a", 0, false), false));
			checkResult(0,11,new String[] {null, "{a[(a)]a}", null, null, null, null, null, null, null, null});
			
			assertFalse(_searchReplace.find(new SearchContext("a{a[(a)a]a})a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{a\\begin{center}(a)a\\end{center}a\\end{center}})a", "a%1a", 0, false), false));			
		} catch (Exception e) {
			
		}
	}
	
	@Test
	void testIgnoreBalancingError() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("a(a))aa", "a%1a", 0, false), false));
			checkResult(5,7,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a)a(a)aa", "a%1a", 0, false), false));
			checkResult(2,7,new String[] {null, "(a)", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a((a)a()a", "a%1a", 0, false), false));
			checkResult(5,9,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{((a)})aa", "a%1a", 0, false), false));
			checkResult(8,10,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{a(a})aa", "a%1a", 0, false), false));
			checkResult(7,9,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{a(a}a()aa", "a%1a", 0, false), false));
			checkResult(6,10,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a{a()a", "a%1a", 0, false), false));
			checkResult(2,6,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			assertFalse(_searchReplace.find(new SearchContext("(a)(a)a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("(ab)a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("a({(a)a(})a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("(a{)(a)a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("(a{(b)a)}", "a%1a", 0, false), false));

			assertTrue(_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), false));
			checkResult(46,48,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}{a\\end{center}(a})aa", "a%1a", 0, false), false));
			checkResult(33,35,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}{a\\begin{string}(a)a\\end{string}a}\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,62,new String[] {null, "\\begin{center}{a\\begin{string}(a)a\\end{string}a}\\end{center}", null, null, null, null, null, null, null, null});
			
			assertFalse(_searchReplace.find(new SearchContext("a\\begin{center}{a\\begin{string}(a)a\\end{string}a\\end{center}}a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{a\\begin{center}(a)a\\end{center}a\\end{center}})a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})a", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), false));
			assertFalse(_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), false));
			
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}defabc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), false));
			assertEquals(32, searchReplace.getStart());
			assertEquals(64, searchReplace.getEnd());
			assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{x-y-z}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testOptionCombinations() {
		// options: fromIndex (0, >0), matchCase
		try {
			// fromIndex = 0, matchCase = false, showBalancingError = false
			assertTrue(_searchReplace.find(new SearchContext("a(a))aa", "A%1a", 0, false), false));
			checkResult(5,7,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			// fromIndex = 3, matchCase = false, showBalancingError = false
			assertTrue(_searchReplace.find(new SearchContext("aaaa(a))aa", "a%1A", 3, false), false));
			checkResult(8,10,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			// fromIndex = 0, matchCase = true, showBalancingError = false
			assertTrue(_searchReplace.find(new SearchContext("aaa(a))aaaA()a", "A%1a", 0, true), false));
			checkResult(10,14,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			// fromIndex = 3, matchCase = true, showBalancingError = false
			assertTrue(_searchReplace.find(new SearchContext("A()a(a))aaaA()a", "a%1A", 3, true), false));
			checkResult(8,12,new String[] {null, "aa", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
		
		UnbalancedStringException e;

		// fromIndex = 0, matchCase = false, showBalancingError = true
		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a(a))aa", "A%1a", 0, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "(a))"), e.getMessage());
		
		// fromIndex = 3, matchCase = false, showBalancingError = true
		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a(aa))a", "a%1A", 3, false), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "))"), e.getMessage());
		
		// fromIndex = 0, matchCase = true, showBalancingError = true
		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("aaa(a))aaaA(()a", "A%1a", 0, true), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "(()a"), e.getMessage());
		
		// fromIndex = 3, matchCase = true, showBalancingError = true
		e = assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("A()a(a))aaaA()a", "a%1A", 3, true), true);
		  });
		
		assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_unbalancedstring", "(a))aaa"), e.getMessage());		
	}
	
	@Test
	void testComplexScenarios() {
		try {
			assertTrue(_searchReplace.find(new SearchContext("Inline $a^2+b^2=c^2$ and centered eq. \\[x^2 \\geq 0\\] to be exchanged", "$%1$%2\\[%3\\]", 0, false), true));
			checkResult(7,52,new String[] {null, "a^2+b^2=c^2", " and centered eq. ", "x^2 \\geq 0", null, null, null, null, null, null});
			
			assertTrue(_searchReplace.find(new SearchContext("while (c1) {if (c2) {if (c3) {if (c4) {stmt;}}}}", "if (%1) {%2}", 0, false), true));
			checkResult(12,47,new String[] {null, "c2", "if (c3) {if (c4) {stmt;}}", null, null, null, null, null, null, null});
			
			
			
			
			assertTrue(_searchReplace.find(new SearchContext("a\\begin{g1}a\\begin{g2}a\\end{g2}a\\begin{g3}aaa\\end{g3}aa\\end{g1}a\\begin{g4}a\\end{g4}a", "a%1a", 0, false), true));
			checkResult(0,64,new String[] {null, "\\begin{g1}a\\begin{g2}a\\end{g2}a\\begin{g3}aaa\\end{g3}aa\\end{g1}", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			fail();
		}
	}
	
	@AfterAll
	static void tearDown() {
		_searchReplace = null;
	}
}
