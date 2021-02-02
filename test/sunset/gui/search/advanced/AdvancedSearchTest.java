package sunset.gui.search.advanced;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.*;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.SearchContext;

class AdvancedSearchTest {
	
	static IAdvancedSearchReplace _searchReplace;

	@BeforeAll
	static void setUp() throws Exception {
		String matchingPairs = "(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}";
		_searchReplace = new AdvancedSearchReplace(matchingPairs);
	}

	@Test
	void testTrueCases() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbcc", "%1", 0, false), true));
			checkResult(0,6,new String[] {null, "aabbcc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("abc", "a%1c", 0, false), true));
			checkResult(0,3,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 0, false), true));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 0, false), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 0, false), true));
			checkResult(0,8,new String[] {"aabb", "cdd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 0, false), true));
			checkResult(0,5,new String[] {null, "a", "b", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "%1b%2d%3f%4", 0, false), true));
			checkResult(0,11,new String[] {null, "aa", "cc", "ee", "gg", null, null, null, null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("ac", "a%7c", 0, false), true));
			checkResult(0,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("bdf", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("bdf", "%8b%4d%0f%5", 0, false), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("abcdaabbccdde", "a%8b%4c%0d%5e", 0, false), true));
			checkResult(0,13,new String[] {"", null, null, null, "", "aabbccdd", null, null, "", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("abcdababcdaabbccdde", "a%8b%4c%0d%5e", 0, false), true));
			checkResult(0,19,new String[] {"", null, null, null, "", "ababcdaabbccdd", null, null, "", null});
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	void checkResult(int start, int end, String[] captures) {
		System.out.println("Expected:\t" + start + "," + end + "," + convertArrayToString(captures));
		Assert.assertEquals(start, _searchReplace.getStart());
		Assert.assertEquals(end, _searchReplace.getEnd());
		Assert.assertArrayEquals(captures, _searchReplace.getCaptures());
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
	void testFalseCases() {
		try {
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "a%1d", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1d", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "d%1", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "aabbcc%1a", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "c%2a", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%0a%1a%2a", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "b%6b%8b", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1aaa", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "aaa%2", 0, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcc", "%1c%2a", 0, false), true));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testSpecialCharacters() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("%%%", "%1%", 0, false), true));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("$%§", "%1§", 0, false), true));
			checkResult(0,3,new String[] {null, "$%", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "\"%1\\", 0, false), true));
			checkResult(1,7,new String[] {null, "§$%&", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%4\"%1\\%6", 0, false), true));
			checkResult(0,10,new String[] {null, "§$%&", null, null, "!", null, "()=", null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%0=", 0, false), true));
			checkResult(0,9,new String[] {"\"§$%&()", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$\n%&()=", "%0\n%1", 0, false), true));
			checkResult(0,10,new String[] {"!\"§$", "%&()=", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$%&\\()=", "%1!", 0, false), true));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("!\"§$%&()=", "!%9", 0, false), true));
			checkResult(0,9,new String[] {null, null, null, null, null, null, null, null, null, "\"§$%&()="});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("1%%%%1%%%1%%%%1", "%1%", 0, false), true));
			checkResult(0,2,new String[] {null, "1", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "$%1&%2?%3!", 0, false), true));
			checkResult(0,15,new String[] {null, "%%%%", "%%%", "%%%%", null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("$%%%%&%%%?%%%%!", "%4$%1&%2?%3!%0", 0, false), true));
			checkResult(0,15,new String[] {"", "%%%%", "%%%", "%%%%", "", null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("$$%$$$%$$$%", "%1%$%2", 0, false), true));
			checkResult(0,11,new String[] {null, "$$", "$$%$$$%", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("$$%$$$%$?$%", "$$%1%$?%2", 0, false), true));
			checkResult(0,11,new String[] {null, "%$$$", "$%", null, null, null, null, null, null, null});
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testNumbers() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123456789", "0%99", 0, false), true));
			checkResult(0,10,new String[] {null, null, null, null, null, null, null, null, null, "12345678"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123456789", "%00%11%22", 0, false), true));
			checkResult(0,3,new String[] {"", "", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123456789", "0%12%34%56%78%9", 0, false), true));
			checkResult(0,10,new String[] {null, "1", null, "3", null, "5", null, "7", null, "9"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123456789", "%01%12%34%56%78%9", 0, false), true));
			checkResult(0,10,new String[] {"0", "", null, "3", null, "5", null, "7", null, "9"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0", "0%9", 0, false), true));
			checkResult(0,1,new String[] {null, null, null, null, null, null, null, null, null, ""});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0", "%00", 0, false), true));
			checkResult(0,1,new String[] {"", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123456789", "0123%56789", 0, false), true));
			checkResult(0,10,new String[] {null, null, null, null, null, "45", null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("0123401234567", "012345%5", 0, false), true));
			checkResult(5,13,new String[] {null, null, null, null, null, "67", null, null, null, null});
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testStartPositions() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 4, false), true));
			checkResult(4,6,new String[] {"a", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 3, false), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 5, false), true));
			checkResult(5,8,new String[] {"", "dd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 1, false), true));
			checkResult(1,5,new String[] {null, "", "b", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabccdeefgg", "f%4", 8, false), true));
			checkResult(8,11,new String[] {null, null, null, null, "gg", null, null, null, null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("cc", "c%7", 1, false), true));
			checkResult(1,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("cccc", "c%7c", 1, false), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("cccc", "c%7c", 1, false), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("ccdc", "c%7c", 1, false), true));
			checkResult(1,4,new String[] {null, null, null, null, null, null, null, "d", null, null});
			
			Assert.assertFalse(_searchReplace.find(new SearchContext("aaaaabcd", "%0b", 7, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aaaaabcd", "b%9", 7, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbccdd", "%0c%1", 6, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabbcd", "a%1b%2c", 3, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aabccdeefgg", "f%4", 10, false), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("cc", "c%7", 2, false), true));			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testMatchCase() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("aBc", "a%1c", 0, true), true));
			checkResult(0,3,new String[] {null, "B", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaAaaaa", "%0A%1", 0, true), true));
			checkResult(0,9,new String[] {"aaaa", "aaaa", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("AAAAaAAAA", "%0a%1", 0, true), true));
			checkResult(0,9,new String[] {"AAAA", "AAAA", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aaaaaBcd", "%0B", 0, true), true));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aAAaabCD", "b%9", 0, true), true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "CD"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbcCdd", "%0C%1", 0, true), true));
			checkResult(0,8,new String[] {"aabbc", "dd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("AaBbcd", "a%1b%2c", 0, true), true));
			checkResult(1,5,new String[] {null, "B", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aAbcCdeEfGg", "%1b%2d%3f%4", 0, true), true));
			checkResult(0,11,new String[] {null, "aA", "cC", "eE", "Gg", null, null, null, null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("aACc", "a%7c", 0, true), true));
			checkResult(0,4,new String[] {null, null, null, null, null, null, null, "AC", null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("AacC", "a%7c", 0, true), true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_searchReplace.find(new SearchContext("aaBccDeeFgg", "%8B%4D%0F%5", 0, true), true));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("bDf", "%8b%4D%0f%5", 0, true), true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("abcdeAabbccdde", "A%8b%4c%0d%5e", 0, true), true));
			checkResult(5,14,new String[] {"c", null, null, null, "b", "d", null, null, "a", null});
			
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "A%1c", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1b", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "a%1h", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "%0d", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "G%9", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcDeFgH", "aBcd%8H", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("AaaaaaaaA", "a%1A%2a", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aaaB", "a%0a%1ab", 0, true), true));
			Assert.assertFalse(_searchReplace.find(new SearchContext("aBcdEaaBBccdde", "a%8b%4c%0d%5e", 0, true), true));
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testInvalidPattern1() {
		InvalidPatternException e;
		String msg = "Invalid search pattern! Missing delimiter between variables: "; 
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%1%2", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%1%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%1%2", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1%2b", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%1%2", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%1b%2%3", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%2%3", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0%1c%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%0%1", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0%1%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%0%1", e.getMessage());
	}
	
	@Test
	void testInvalidPattern2() {
		InvalidPatternException e;
		String msg = "Invalid search pattern! Variable has been used more than once: ";
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0a%0c%0", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%0", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%0b%1c%0", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%0", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "a%0b%2c%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%2", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1a%1b%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%1", e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabbccdd", "%0c%0", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%0", e.getMessage());

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabbcd", "a%2b%2c", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%2", e.getMessage());

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabccdeefgg", "%1b%1d%2f%3", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%1", e.getMessage());

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aabccdeefgg", "%8b%8d%2f%2", 0, false), true);
		  });
		
		Assert.assertEquals(msg + "%8", e.getMessage());
	}
	
	@Test
	void testInvalidPattern3() {
		InvalidPatternException e;
		String msg = "Invalid search pattern! No variable used in the pattern";
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "b", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "aabbcc", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("aabbcc", "dd", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("abc", "abc", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("a", "a", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("abc", "", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("$%§", "%", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> { 
			_searchReplace.find(new SearchContext("!\"§$%&\\()=", "abc", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%1", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1%2%3%4bcc", "%%1%%2%%3%%4", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%1b", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1c", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "a%%1c%%2", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%0a%%1c", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_searchReplace.find(new SearchContext("aab%1bcc", "%%0a%%1c%%2", 0, false), true);
		  });
		Assert.assertEquals(msg, e.getMessage());
	}

	@Test
	void testInvalidInputs() {
		IndexOutOfBoundsException e;
		String msg = "Index out of range: "; 
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1a%2", -1, false), true);
		  });
		
		Assert.assertEquals(msg + "-1", e.getMessage());
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("abc", "%1b%2", 5, false), true);
		  });
		
		Assert.assertEquals(msg + "5", e.getMessage());
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_searchReplace.find(new SearchContext("", "%1c%2", 1, false), true);
		  });
		
		Assert.assertEquals(msg + "1", e.getMessage());
	}

	@Test
	void testEscapedVariables() {
		try {			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%%1%1", 0, false), true));
			checkResult(3,8,new String[] {null, "bcc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1", 0, false), true));
			checkResult(0,5,new String[] {null, "aab", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1bcc", "%1%%1%2", 0, false), true));
			checkResult(0,8,new String[] {null, "aab", "bcc", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%%1a%1c%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%%1%1%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "abc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("%1abc%1", "%0%%1%1%%1%2", 0, false), true));
			checkResult(0,7,new String[] {"", "abc", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("%1a%1c%1", "%%1%1%%1%2%%1", 0, false), true));
			checkResult(0,8,new String[] {null, "a", "c", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1%2%3%4bcc", "%8%%1%%2%%3%%4%9", 0, false), true));
			checkResult(0,14,new String[] {null, null, null, null, null, null, null, null, "aab", "bcc"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%1%2b%9", 0, false), true));
			checkResult(0,12,new String[] {null, null, "%2%4", null, null, null, null, null, "aab", "cc"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aab%1%2%4bcc", "%8%%2%9", 0, false), true));
			checkResult(0,12,new String[] {null, null, null, null, null, null, null, null, "aab%1", "%4bcc"});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("%1aaaaab", "%%1%1b", 0, false), true));
			checkResult(0,8,new String[] {null, "aaaaa", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("aabbc%1d", "a%1%%1", 0, false), true));
			checkResult(0,7,new String[] {null, "abbc", null, null, null, null, null, null, null, null});
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	void testCorrectBalancing1() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(a)a", "a%1a", 0, false), true));
			checkResult(0,5,new String[] {null, "(a)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(a(a)a)a", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "(a(a)a)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(a(aa))a()a", "a%1a", 0, false), false));
			checkResult(0,9,new String[] {null, "(a(aa))", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a((a))aa", "a%1a", 0, false), true));
			checkResult(0,7,new String[] {null, "((a))", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(a)(a)a", "a%1a", 0, false), true));
			checkResult(0,8,new String[] {null, "(a)(a)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a()a", "a%1a", 0, false), true));
			checkResult(0,4,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a((a)(a))a", "a%1a", 0, false), false));
			checkResult(0,10,new String[] {null, "((a)(a))", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a((a()(a())))a", "a%1a", 0, false), false));
			checkResult(0,14,new String[] {null, "((a()(a())))", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testCorrectBalancing2() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("a{(a)}a", "a%1a", 0, false), true));
			checkResult(0,7,new String[] {null, "{(a)}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({a}{(a)}a)a", "a%1a", 0, false), false));
			checkResult(0,13,new String[] {null, "({a}{(a)}a)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({(a)})aa", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "({(a)})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({(a)})a{()}a", "a%1a", 0, false), true));
			checkResult(0,9,new String[] {null, "({(a)})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a{(a){}(a)}a", "a%1a", 0, false), false));
			checkResult(0,12,new String[] {null, "{(a){}(a)}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({})a", "a%1a", 0, false), true));
			checkResult(0,6,new String[] {null, "({})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({({a})({a})})a", "a%1a", 0, false), true));
			checkResult(0,16,new String[] {null, "({({a})({a})})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a({(a()(a()))})a", "a%1a", 0, false), false));
			checkResult(0,16,new String[] {null, "({(a()(a()))})", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testCorrectBalancing3() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(\\begin{center}{a}{(a)}\\end{center}{a})a", "a%1a", 0, false), true));
			checkResult(0,41,new String[] {null, "(\\begin{center}{a}{(a)}\\end{center}{a})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{group1}a\\begin{group2}a\\end{group2}\\end{group1}a", "a%1a", 0, false), true));
			checkResult(0,56,new String[] {null, "\\begin{group1}a\\begin{group2}a\\end{group2}\\end{group1}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}a\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,29,new String[] {null, "\\begin{center}a\\end{center}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a{\\begin{center}(a){}(a)\\end{center}}a", "a%1a", 0, false), true));
			checkResult(0,38,new String[] {null, "{\\begin{center}(a){}(a)\\end{center}}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}({})a(){()}\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,39,new String[] {null, "\\begin{center}({})a(){()}\\end{center}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{group1}a\\begin{group2}a\\end{group2}a\\begin{group3}aaa\\end{group3}aa\\end{group1}a\\begin{group4}a\\end{group4}a", "a%1a", 0, false), true));
			checkResult(0,88,new String[] {null, "\\begin{group1}a\\begin{group2}a\\end{group2}a\\begin{group3}aaa\\end{group3}aa\\end{group1}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}({(a()(a()))})a\\end{center}a", "a%1a", 0, false), false));
			checkResult(0,43,new String[] {null, "\\begin{center}({(a()(a()))})a\\end{center}", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testCorrectBalancing4() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("({})", "(%1)", 0, false), true));
			checkResult(0,4,new String[] {null, "{}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("(\\begin{center}{}\\end{center})", "(%1)", 0, false), true));
			checkResult(0,30,new String[] {null, "\\begin{center}{}\\end{center}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("\\begin{center}(\\begin{center}{}\\end{center})\\end{center}", "\\begin{center}%1\\end{center}", 0, false), true));
			checkResult(0,56,new String[] {null, "(\\begin{center}{}\\end{center})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("\\begin{center}(\\begin{lstlisting}{}\\end{lstlisting})\\end{center}", "\\begin{center}%1\\end{center}", 0, false), true));
			checkResult(0,64,new String[] {null, "(\\begin{lstlisting}{}\\end{lstlisting})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("\\begin{lstlisting}(\\begin{center}{}\\end{center})\\end{lstlisting}", "(\\begin{center}%1\\end{center})", 0, false), true));
			checkResult(18,48,new String[] {null, "{}", null, null, null, null, null, null, null, null});
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testCorrectBalancing5() {
		try {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			Assert.assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), true));
			Assert.assertEquals(0, searchReplace.getStart());
			Assert.assertEquals(32, searchReplace.getEnd());
			Assert.assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{x-y-z}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
			
			searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%3-%2-%1}");
			Assert.assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}def", "a%1f", 0, false), true));
			Assert.assertEquals(0, searchReplace.getStart());
			Assert.assertEquals(32, searchReplace.getEnd());
			Assert.assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{z-y-x}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testCorrectBalancing6() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("(((()((())()))))", "(%1)", 0, false), true));
			checkResult(0,16,new String[] {null, "((()((())())))", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("(({[\\(\\)\\{\\}\\[\\]]}))", "(%1)", 0, false), true));
			checkResult(0,20,new String[] {null, "({[\\(\\)\\{\\}\\[\\]]})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("(({[\\(\\{\\[\\]\\}\\)]}))", "(%1)", 0, false), true));
			checkResult(0,20,new String[] {null, "({[\\(\\{\\[\\]\\}\\)]})", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("\\(\\(\\{\\[(){}[]\\]\\}\\)\\)", "\\(%1\\)", 0, false), true));
			checkResult(0,22,new String[] {null, "\\(\\{\\[(){}[]\\]\\}\\)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("\\((\\({\\{[\\[\\]]\\}}\\))\\)", "\\(%1\\)", 0, false), true));
			checkResult(0,22,new String[] {null, "(\\({\\{[\\[\\]]\\}}\\))", null, null, null, null, null, null, null, null});
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testConstructor() {
		try {
			IAdvancedSearchReplace searchReplace;
			searchReplace = new AdvancedSearchReplace(null);
			searchReplace = new AdvancedSearchReplace("");
			String matchingPairs = "(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}";
			searchReplace = new AdvancedSearchReplace(matchingPairs);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testInorrectBalancing1() {
		UnbalancedStringException e;

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a(a))aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: (a))", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a)a(a)aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: )", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a((a)a()a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: ((a)a()a", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a)(a)a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: )(", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(ab)a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: b)", e.getMessage());
	}
	
	@Test
	void testInorrectBalancing2() {
		UnbalancedStringException e;

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((a)})aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {((a)})", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{a(a})aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {a(a})", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a({(a)a(})a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: ({(a)a(})", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{)(a)a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {)(", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(b)a)}", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {(b)a)}", e.getMessage());
	}
	
	@Test
	void testInorrectBalancing3() {
		UnbalancedStringException e;

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {((\\begin{center}{test}a)\\end{center}{test})}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}{a\\end{center}(a})aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}{a\\end{center}(", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}{(a)\\end{center}}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {(\\begin{center}\\begin{center}b\\end{center})", e.getMessage());
	}
	
	@Test
	void testInorrectBalancing4() {
		UnbalancedStringException e;

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {((\\begin{center}{test}a)\\end{center}{test})}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}a\\end{string}a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}a\\end{string}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("a\\begin{center}{\\begin{string}(a)a\\end{string}\\end{center}}a", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}{\\begin{string}(a)a\\end{string}\\end{center}}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: \\begin{center}{(a)\\end{center}}", e.getMessage());

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: {(\\begin{center}\\begin{center}b\\end{center})", e.getMessage());
	}
	
	@Test
	void testIncorrectBalancing5() {
		UnbalancedStringException e;

		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}def", "a%1f", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: bc\\beg{x-y-z}test\\end{z-y-x}de", e.getMessage());
		
		e = Assert.assertThrows(UnbalancedStringException.class, () -> {
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%3-%2-%1}");
			searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), true);
		  });
		
		Assert.assertEquals("Matched string is unbalanced: bc\\beg{x-y-z}test\\end{x-y-z}de", e.getMessage());
	}
	
	@Test
	void testInorrectBalancing6() {
		try {
			UnbalancedStringException e;

			e = Assert.assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("a(((()())))())())b", "a%1b", 0, false), true);
			  });
			
			Assert.assertEquals("Matched string is unbalanced: (((()())))())())", e.getMessage());
			
			e = Assert.assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("(({[\\(\\{\\)\\}\\[\\]]}))", "(%1)", 0, false), true);
			  });
			
			Assert.assertEquals("Matched string is unbalanced: ({[\\(\\{\\)\\}\\[\\]]}", e.getMessage());
			
			e = Assert.assertThrows(UnbalancedStringException.class, () -> {
				_searchReplace.find(new SearchContext("(({[\\(\\{\\)\\}\\[\\]]})", "(%1)", 0, false), true);
			  });
			
			Assert.assertEquals("Matched string is unbalanced: ({[\\(\\{\\)\\}\\[\\]]}", e.getMessage());
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testIgnoreBalancingError() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("a(a))aa", "a%1a", 0, false), false));
			checkResult(5,7,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a)a(a)aa", "a%1a", 0, false), false));
			checkResult(2,7,new String[] {null, "(a)", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a((a)a()a", "a%1a", 0, false), false));
			checkResult(5,9,new String[] {null, "()", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a{((a)})aa", "a%1a", 0, false), false));
			checkResult(8,10,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a{a(a})aa", "a%1a", 0, false), false));
			checkResult(7,9,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertFalse(_searchReplace.find(new SearchContext("(a)(a)a", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("(ab)a", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("a({(a)a(})a", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("(a{)(a)a", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("(a{(b)a)}", "a%1a", 0, false), false));

			Assert.assertTrue(_searchReplace.find(new SearchContext("a{((\\begin{center}{test}a)\\end{center}{test})}aa", "a%1a", 0, false), false));
			checkResult(46,48,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}{a\\end{center}(a})aa", "a%1a", 0, false), false));
			checkResult(33,35,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{a\\begin{center}(a)a\\end{center}a\\end{center}})a", "a%1a", 0, false), false));
			checkResult(31,63,new String[] {null, "\\begin{center}(a)a\\end{center}", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("a\\begin{center}{a\\begin{string}(a)a\\end{string}a\\end{center}}a", "a%1a", 0, false), false));
			checkResult(16,48,new String[] {null, "\\begin{string}(a)a\\end{string}", null, null, null, null, null, null, null, null});
			
			Assert.assertFalse(_searchReplace.find(new SearchContext("a\\begin{center}(\\begin{center}{\\begin{center}(a)a\\end{center}\\end{center}})a", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("\\begin{center}a\\begin{center}{(a)\\end{center}}a\\end{center}", "a%1a", 0, false), false));
			Assert.assertFalse(_searchReplace.find(new SearchContext("(a{(\\begin{center}\\begin{center}b\\end{center})a)}", "a%1a", 0, false), false));
			
			IAdvancedSearchReplace searchReplace = new AdvancedSearchReplace("\\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			Assert.assertTrue(searchReplace.find(new SearchContext("abc\\beg{x-y-z}test\\end{z-y-x}defabc\\beg{x-y-z}test\\end{x-y-z}def", "a%1f", 0, false), false));
			Assert.assertEquals(32, searchReplace.getStart());
			Assert.assertEquals(64, searchReplace.getEnd());
			Assert.assertArrayEquals(new String[] {null, "bc\\beg{x-y-z}test\\end{x-y-z}de", null, null, null, null, null, null, null, null}, searchReplace.getCaptures());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testComplexScenarios() {
		try {
			Assert.assertTrue(_searchReplace.find(new SearchContext("Inline $a^2+b^2=c^2$ and centered eq. \\[x^2 \\geq 0\\] to be exchanged", "$%1$%2\\[%3\\]", 0, false), true));
			checkResult(7,52,new String[] {null, "a^2+b^2=c^2", " and centered eq. ", "x^2 \\geq 0", null, null, null, null, null, null});
			
			Assert.assertTrue(_searchReplace.find(new SearchContext("while (c1) {if (c2) {if (c3) {if (c4) {stmt;}}}}", "if (%1) {%2}", 0, false), true));
			checkResult(12,47,new String[] {null, "c2", "if (c3) {if (c4) {stmt;}}", null, null, null, null, null, null, null});
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@AfterAll
	static void tearDown() {
		_searchReplace = null;
	}
}