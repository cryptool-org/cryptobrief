package sunset.gui.search.advanced;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import sunset.gui.search.exception.InvalidPatternException;

class AdvancedSearchTest {
	
	static AdvancedSearch _search;

	@BeforeAll
	static void setUp() throws Exception {
		_search = new AdvancedSearch();
	}

	@Test
	void testTrueCases() {
		try {
			Assert.assertTrue(_search.find("aabbcc", "%1", 0, false));
			checkResult(0,6,new String[] {null, "aabbcc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("abc", "a%1c", 0, false));
			checkResult(0,3,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aaaaabcd", "%0b", 0, false));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aaaaabcd", "b%9", 0, false));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			Assert.assertTrue(_search.find("aabbccdd", "%0c%1", 0, false));
			checkResult(0,8,new String[] {"aabb", "cdd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aabbcd", "a%1b%2c", 0, false));
			checkResult(0,5,new String[] {null, "a", "b", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aabccdeefgg", "%1b%2d%3f%4", 0, false));
			checkResult(0,11,new String[] {null, "aa", "cc", "ee", "gg", null, null, null, null, null});

			Assert.assertTrue(_search.find("ac", "a%7c", 0, false));
			checkResult(0,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_search.find("aabccdeefgg", "%8b%4d%0f%5", 0, false));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			Assert.assertTrue(_search.find("bdf", "%8b%4d%0f%5", 0, false));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_search.find("bdf", "%8b%4d%0f%5", 0, false));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_search.find("abcdaabbccdde", "a%8b%4c%0d%5e", 0, false));
			checkResult(0,13,new String[] {"", null, null, null, "", "aabbccdd", null, null, "", null});
			
			Assert.assertTrue(_search.find("abcdababcdaabbccdde", "a%8b%4c%0d%5e", 0, false));
			checkResult(0,19,new String[] {"", null, null, null, "", "ababcdaabbccdd", null, null, "", null});
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}
	
	void checkResult(int start, int end, String[] captures) {
		System.out.println("Expected:\t" + start + "," + end + "," + convertArrayToString(captures));
		Assert.assertEquals(start, _search.getStart());
		Assert.assertEquals(end, _search.getEnd());
		Assert.assertArrayEquals(captures, _search.getCaptures());
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
			// if pattern comprises no variable, false should be returned (normal search is done)
			Assert.assertFalse(_search.find("aabbcc", "", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "b", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "aabbcc", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "dd", 0, false));
			Assert.assertFalse(_search.find("abc", "abc", 0, false));
			Assert.assertFalse(_search.find("a", "a", 0, false));
			Assert.assertFalse(_search.find("abc", "", 0, false));
			Assert.assertFalse(_search.find("$%§", "%", 0, false));
			Assert.assertFalse(_search.find("!\"§$%&\\()=", "abc", 0, false));
			
			Assert.assertFalse(_search.find("aabbcc", "a%1d", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "%1d", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "d%1", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "aabbcc%1a", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "c%2a", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "%0a%1a%2a", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "b%6b%8b", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "%1aaa", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "aaa%2", 0, false));
			Assert.assertFalse(_search.find("aabbcc", "%1c%2a", 0, false));
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testSpecialCharacters() {
		try {
			Assert.assertTrue(_search.find("%%%", "%1%", 0, false));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("$%§", "%1§", 0, false));
			checkResult(0,3,new String[] {null, "$%", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$%&\\()=", "\"%1\\", 0, false));
			checkResult(1,7,new String[] {null, "§$%&", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$%&\\()=", "%4\"%1\\%6", 0, false));
			checkResult(0,10,new String[] {null, "§$%&", null, null, "!", null, "()=", null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$%&\\()=", "!%0=", 0, false));
			checkResult(0,10,new String[] {"\"§$%&\\()", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$\n%&\\()=", "%0\n%1", 0, false));
			checkResult(0,11,new String[] {"!\"§$", "%&\\()=", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$%&\\()=", "%1!", 0, false));
			checkResult(0,1,new String[] {null, "", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("!\"§$%&\\()=", "!%9", 0, false));
			checkResult(0,10,new String[] {null, null, null, null, null, null, null, null, null, "\"§$%&\\()="});
			
			Assert.assertTrue(_search.find("1%%%%1%%%1%%%%1", "%1%", 0, false));
			checkResult(0,2,new String[] {null, "1", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("$%%%%&%%%?%%%%!", "$%1&%2?%3!", 0, false));
			checkResult(0,15,new String[] {null, "%%%%", "%%%", "%%%%", null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("$%%%%&%%%?%%%%!", "%4$%1&%2?%3!%0", 0, false));
			checkResult(0,15,new String[] {"", "%%%%", "%%%", "%%%%", "", null, null, null, null, null});
			
			Assert.assertTrue(_search.find("$$%$$$%$$$%", "%1%$%2", 0, false));
			checkResult(0,11,new String[] {null, "$$", "$$%$$$%", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("$$%$$$%$?$%", "$$%1%$?%2", 0, false));
			checkResult(0,11,new String[] {null, "%$$$", "$%", null, null, null, null, null, null, null});
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testNumbers() {
		try {
			Assert.assertTrue(_search.find("0123456789", "0%99", 0, false));
			checkResult(0,10,new String[] {null, null, null, null, null, null, null, null, null, "12345678"});
			
			Assert.assertTrue(_search.find("0123456789", "%00%11%22", 0, false));
			checkResult(0,3,new String[] {"", "", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("0123456789", "0%12%34%56%78%9", 0, false));
			checkResult(0,10,new String[] {null, "1", null, "3", null, "5", null, "7", null, "9"});
			
			Assert.assertTrue(_search.find("0123456789", "%01%12%34%56%78%9", 0, false));
			checkResult(0,10,new String[] {"0", "", null, "3", null, "5", null, "7", null, "9"});
			
			Assert.assertTrue(_search.find("0", "0%9", 0, false));
			checkResult(0,1,new String[] {null, null, null, null, null, null, null, null, null, ""});
			
			Assert.assertTrue(_search.find("0", "%00", 0, false));
			checkResult(0,1,new String[] {"", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("0123456789", "0123%56789", 0, false));
			checkResult(0,10,new String[] {null, null, null, null, null, "45", null, null, null, null});
			
			Assert.assertTrue(_search.find("0123401234567", "012345%5", 0, false));
			checkResult(5,13,new String[] {null, null, null, null, null, "67", null, null, null, null});
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testStartPositions() {
		try {
			Assert.assertTrue(_search.find("aaaaabcd", "%0b", 4, false));
			checkResult(4,6,new String[] {"a", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aaaaabcd", "b%9", 3, false));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "cd"});
			
			Assert.assertTrue(_search.find("aabbccdd", "%0c%1", 5, false));
			checkResult(5,8,new String[] {"", "dd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aabbcd", "a%1b%2c", 1, false));
			checkResult(1,5,new String[] {null, "", "b", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aabccdeefgg", "f%4", 8, false));
			checkResult(8,11,new String[] {null, null, null, null, "gg", null, null, null, null, null});

			Assert.assertTrue(_search.find("cc", "c%7", 1, false));
			checkResult(1,2,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_search.find("cccc", "c%7c", 1, false));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			Assert.assertTrue(_search.find("cccc", "c%7c", 1, false));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});
			
			Assert.assertTrue(_search.find("ccdc", "c%7c", 1, false));
			checkResult(1,4,new String[] {null, null, null, null, null, null, null, "d", null, null});
			
			Assert.assertFalse(_search.find("aaaaabcd", "%0b", 7, false));
			Assert.assertFalse(_search.find("aaaaabcd", "b%9", 7, false));
			Assert.assertFalse(_search.find("aabbccdd", "%0c%1", 6, false));
			Assert.assertFalse(_search.find("aabbcd", "a%1b%2c", 3, false));
			Assert.assertFalse(_search.find("aabccdeefgg", "f%4", 10, false));
			Assert.assertFalse(_search.find("cc", "c%7", 2, false));			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testMatchCase() {
		try {
			Assert.assertTrue(_search.find("aBc", "a%1c", 0, true));
			checkResult(0,3,new String[] {null, "B", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aaaaAaaaa", "%0A%1", 0, true));
			checkResult(0,9,new String[] {"aaaa", "aaaa", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("AAAAaAAAA", "%0a%1", 0, true));
			checkResult(0,9,new String[] {"AAAA", "AAAA", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aaaaaBcd", "%0B", 0, true));
			checkResult(0,6,new String[] {"aaaaa", null, null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aAAaabCD", "b%9", 0, true));
			checkResult(5,8,new String[] {null, null, null, null, null, null, null, null, null, "CD"});
			
			Assert.assertTrue(_search.find("aabbcCdd", "%0C%1", 0, true));
			checkResult(0,8,new String[] {"aabbc", "dd", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("AaBbcd", "a%1b%2c", 0, true));
			checkResult(1,5,new String[] {null, "B", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aAbcCdeEfGg", "%1b%2d%3f%4", 0, true));
			checkResult(0,11,new String[] {null, "aA", "cC", "eE", "Gg", null, null, null, null, null});

			Assert.assertTrue(_search.find("aACc", "a%7c", 0, true));
			checkResult(0,4,new String[] {null, null, null, null, null, null, null, "AC", null, null});
			
			Assert.assertTrue(_search.find("AacC", "a%7c", 0, true));
			checkResult(1,3,new String[] {null, null, null, null, null, null, null, "", null, null});

			Assert.assertTrue(_search.find("aaBccDeeFgg", "%8B%4D%0F%5", 0, true));
			checkResult(0,11,new String[] {"ee", null, null, null, "cc", "gg", null, null, "aa", null});
			
			Assert.assertTrue(_search.find("bDf", "%8b%4D%0f%5", 0, true));
			checkResult(0,3,new String[] {"", null, null, null, "", "", null, null, "", null});
			
			Assert.assertTrue(_search.find("abcdeAabbccdde", "A%8b%4c%0d%5e", 0, true));
			checkResult(5,14,new String[] {"c", null, null, null, "b", "d", null, null, "a", null});
			
			Assert.assertFalse(_search.find("aBcDeFgH", "A%1c", 0, true));
			Assert.assertFalse(_search.find("aBcDeFgH", "a%1b", 0, true));
			Assert.assertFalse(_search.find("aBcDeFgH", "a%1h", 0, true));
			Assert.assertFalse(_search.find("aBcDeFgH", "%0d", 0, true));
			Assert.assertFalse(_search.find("aBcDeFgH", "G%9", 0, true));
			Assert.assertFalse(_search.find("aBcDeFgH", "aBcd%8H", 0, true));
			Assert.assertFalse(_search.find("AaaaaaaaA", "a%1A%2a", 0, true));
			Assert.assertFalse(_search.find("aaaB", "a%0a%1ab", 0, true));
			Assert.assertFalse(_search.find("aBcdEaaBBccdde", "a%8b%4c%0d%5e", 0, true));
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testInvalidPattern1() {
		InvalidPatternException e;
		String msg = "Invalid search pattern! Missing delimiter between variables: "; 
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%1%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%1%2");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "a%1%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%1%2");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%1%2b", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%1%2");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "a%1b%2%3", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%2%3");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%0%1c%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%0%1");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%0%1%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%0%1");
	}
	
	@Test
	void testInvalidPattern2() {
		InvalidPatternException e;
		String msg = "Invalid search pattern! Variable has been used more than once: ";
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%0a%0c%0", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%0");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%0b%1c%0", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%0");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "a%0b%2c%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%2");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("abc", "%1a%1b%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%1");
		
		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("aabbccdd", "%0c%0", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%0");

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("aabbcd", "a%2b%2c", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%2");

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("aabccdeefgg", "%1b%1d%2f%3", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%1");

		e = Assert.assertThrows(InvalidPatternException.class, () -> {
			_search.find("aabccdeefgg", "%8b%8d%2f%2", 0, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg + "%8");
	}

	@Test
	void testInvalidInputs() {
		IndexOutOfBoundsException e;
		String msg = "Illegal start index"; 
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_search.find("abc", "%1a%2", -1, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg);
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_search.find("abc", "%1b%2", 5, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg);
		
		e = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
			_search.find("", "%1c%2", 1, false);
		  });
		
		Assert.assertEquals(e.getMessage(), msg);
	}

	@Test
	void testEscapedVariables() {
		try {
			// variables are escaped, so find should return false (no variables used)
			Assert.assertFalse(_search.find("aab%1bcc", "%%1", 0, false));
			Assert.assertFalse(_search.find("aab%1%2%3%4bcc", "%%1%%2%%3%%4", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "a%%1", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "%%1b", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "a%%1c", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "a%%1c%%2", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "%%0a%%1c", 0, false));
			Assert.assertFalse(_search.find("aab%1bcc", "%%0a%%1c%%2", 0, false));
			
			Assert.assertTrue(_search.find("aab%1bcc", "%%1%1", 0, false));
			checkResult(3,8,new String[] {null, "bcc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aab%1bcc", "%1%%1", 0, false));
			checkResult(0,5,new String[] {null, "aab", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aab%1bcc", "%1%%1%2", 0, false));
			checkResult(0,8,new String[] {null, "aab", "bcc", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("%1abc%1", "%%1a%1c%%1", 0, false));
			checkResult(0,7,new String[] {null, "b", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("%1abc%1", "%%1%1%%1", 0, false));
			checkResult(0,7,new String[] {null, "abc", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("%1abc%1", "%0%%1%1%%1%2", 0, false));
			checkResult(0,7,new String[] {"", "abc", "", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("%1a%1c%1", "%%1%1%%1%2%%1", 0, false));
			checkResult(0,8,new String[] {null, "a", "c", null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aab%1%2%3%4bcc", "%8%%1%%2%%3%%4%9", 0, false));
			checkResult(0,14,new String[] {null, null, null, null, null, null, null, null, "aab", "bcc"});
			
			Assert.assertTrue(_search.find("aab%1%2%4bcc", "%8%%1%2b%9", 0, false));
			checkResult(0,12,new String[] {null, null, "%2%4", null, null, null, null, null, "aab", "cc"});
			
			Assert.assertTrue(_search.find("aab%1%2%4bcc", "%8%%2%9", 0, false));
			checkResult(0,12,new String[] {null, null, null, null, null, null, null, null, "aab%1", "%4bcc"});
			
			Assert.assertTrue(_search.find("%1aaaaab", "%%1%1b", 0, false));
			checkResult(0,8,new String[] {null, "aaaaa", null, null, null, null, null, null, null, null});
			
			Assert.assertTrue(_search.find("aabbc%1d", "a%1%%1", 0, false));
			checkResult(0,7,new String[] {null, "abbc", null, null, null, null, null, null, null, null});
			
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testBalancing() {
		try {
			// not implemented yet
			Assert.assertTrue(_search.find("abc", "a%1c", 0, false));
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}

	@Test
	void testComplexScenarios() {
		try {
			// todo
			Assert.assertTrue(_search.find("abc", "a%1c", 0, false));
		} catch (InvalidPatternException e) {
			Assert.fail();
		}
	}
	
	@AfterAll
	static void tearDown() {
		_search = null;
	}
}