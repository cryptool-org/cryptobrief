package sunset.gui.search;

import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.interfaces.ISearchReplaceLogic;

class SearchReplaceLogicTest {
	
	static ISearchReplaceLogic _logic;

	@BeforeEach
	void setUp() throws Exception {
		_logic = new SearchReplaceLogic();
	}

	@AfterEach
	void tearDown() throws Exception {
		_logic = null;
	}

	@Test
	void testSearchStandard() {
		Assert.assertTrue(_logic.search("abcdefghi", "def", 0, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.search("abcdefghi", "def", 3, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.search("abcdefghi", "def", -4, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.search("abcdEfghi", "dEf", 0, true, false));
		checkResult(3,6, "dEf");
		
		Assert.assertTrue(_logic.search("abcdefghi", "def", 4, false, true));
		checkResult(3,6, "def");
		
		Assert.assertFalse(_logic.search("abcdefghi", "def", 4, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_logic.search("abcdefghi", "def", 15, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_logic.search("abcdefghi", "dEf", 0, true, false));
		checkResult(-1,-1, "dEf");
	}
	
	@Test
	void testSearchRegex() {
		Assert.assertTrue(_logic.searchRegex("abcdefghi", "def", 0, false, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.searchRegex("abcdefghi", "def", 3, false, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.searchRegex("abcdefghi", "de?f", 0, false, false, false));
		checkResult(3,6, "de?f");
		
		Assert.assertTrue(_logic.searchRegex("abcdfghi", "de?f", 0, false, false, false));
		checkResult(3,5, "de?f");
		
		Assert.assertTrue(_logic.searchRegex("abcdEfghi", "dEf", 0, true, false, false));
		checkResult(3,6, "dEf");
		
		Assert.assertTrue(_logic.searchRegex("abcdefghi", "def", 0, false, true, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_logic.searchRegex("abcd\nfghi", "d.f", 0, false, false, true));
		checkResult(3,6, "d.f");
		
		Assert.assertTrue(_logic.searchRegex("abcdefghi", "def", 4, false, true, false));
		checkResult(3,6, "def");
		
		Assert.assertFalse(_logic.searchRegex("abcdefghi", "def", 4, false, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_logic.searchRegex("abcd\nfghi", "d.f", 0, false, false, false));
		checkResult(-1,-1, "d.f");
		
		Assert.assertFalse(_logic.searchRegex("abcdefghi", "dEf", 0, true, false, false));
		checkResult(-1,-1, "dEf");
		
		Assert.assertFalse(_logic.searchRegex("abcdefghi", "deef", 6, false, true, false));
		checkResult(-1,-1, "deef");
		
		Assert.assertFalse(_logic.searchRegex("abcdefghi", "deef", 0, false, true, false));
		checkResult(-1,-1, "deef");
		
		Assert.assertFalse(_logic.searchRegex("abcd\nfghi", "d.gf", 1, false, false, true));
		checkResult(-1,-1, "d.gf");
		
		Assert.assertFalse(_logic.searchRegex("abcdefghi", "d(f", 0, false, false, false));
	}
	
	@Test
	void testSearchAdvanced() {
		Assert.assertTrue(_logic.searchAdvanced("abcdefghi", "d%1f", 0, false, false));
		checkResult(3,6, "d%1f");
		
		Assert.assertTrue(_logic.searchAdvanced("abcdefghi", "d%2f", 3, false, false));
		checkResult(3,6, "d%2f");
		
		Assert.assertTrue(_logic.searchAdvanced("abcdEfghi", "dEf%5", 0, true, false));
		checkResult(3,9, "dEf%5");
		
		Assert.assertTrue(_logic.searchAdvanced("abcdefghi", "%8def", 4, false, true));
		checkResult(0,6, "%8def");

		Assert.assertTrue(_logic.searchAdvanced("abcdefghi", "d%1f", 0, false, false));
		checkResult(3,6, "d%1f");
		
		Assert.assertFalse(_logic.searchAdvanced("abcdefghi", "d%1f", 4, false, false));
		checkResult(-1,-1, "d%1f");
		
		Assert.assertFalse(_logic.searchAdvanced("abcdefghi", "dE%1f", 0, true, false));
		checkResult(-1,-1, "dE%1f");
		
		Assert.assertFalse(_logic.searchAdvanced("abc", "%1d%2", 0, false, false));
		checkResult(-1,-1, "%1d%2");
		
		Assert.assertFalse(_logic.searchAdvanced("abc", "%1%2", 0, false, false));
	}
	
	@Test
	void testEquals() {
		Assert.assertTrue(_logic.equals("abc", "abc", false));
		Assert.assertTrue(_logic.equals("abc", "abc", true));
		Assert.assertTrue(_logic.equals("abc", "aBc", false));
		Assert.assertFalse(_logic.equals("abc", "abcd", false));
		Assert.assertFalse(_logic.equals("abc", "aBc", true));
	}
	
	@Test
	void testMatchesRegex() {
		Assert.assertTrue(_logic.matchesRegex("abc", "abc", false, false));
		Assert.assertTrue(_logic.matchesRegex("abc", "abc", true, false));
		Assert.assertTrue(_logic.matchesRegex("abc", "aBc", false, false));
		Assert.assertTrue(_logic.matchesRegex("aBc", "aBc", true, false));
		Assert.assertTrue(_logic.matchesRegex("abc", "a.*c", true, false));
		Assert.assertTrue(_logic.matchesRegex("ab\nc", "a.*c", true, true));
		
		Assert.assertFalse(_logic.matchesRegex("abc", "abcd", false, false));
		Assert.assertFalse(_logic.matchesRegex("abc", "aBc", true, false));
		Assert.assertFalse(_logic.matchesRegex("ab\nc", "a.*c", true, false));
		Assert.assertFalse(_logic.matchesRegex("abc", "a(c", true, false));
	}
	
	@Test
	void testMatchesAdvanced() {
		Assert.assertTrue(_logic.matchesAdvanced("abc", "a%1c", false));
		Assert.assertTrue(_logic.matchesAdvanced("abc", "ab%1", true));
		Assert.assertTrue(_logic.matchesAdvanced("abc", "%1Bc", false));
		Assert.assertTrue(_logic.matchesAdvanced("aBc", "aB%9", true));
		Assert.assertTrue(_logic.matchesAdvanced("abc", "a%1c", true));
		Assert.assertTrue(_logic.matchesAdvanced("ab\nc", "a%2c", true));
		
		Assert.assertFalse(_logic.matchesAdvanced("abc", "abcd", false));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "ab%5%6c", false));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "a%5b%5c", false));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "ab%5cd", false));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "%1Bc", true));
		Assert.assertFalse(_logic.matchesAdvanced("ab\nc", "a%1C", true));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "a(c%1", true));
		Assert.assertFalse(_logic.matchesAdvanced("abc", "a%0b%1d", true));
	}
	
	@Test
	void testReplaceRegex() {
		try {
			Assert.assertEquals("aded", _logic.replaceRegex("abcd", "bc", "de", false, false));
			Assert.assertEquals("abcaghi", _logic.replaceRegex("abcdefghi", "d.*f", "a", false, false));
			Assert.assertEquals("abcaghi", _logic.replaceRegex("abcdefdeFghi", "d.*F", "a", true, false));
			Assert.assertEquals("abcaghi", _logic.replaceRegex("abcd\ne\nfghi", "d.*f", "a", false, true));
			Assert.assertEquals(null, _logic.replaceRegex("abcd", "a(b", "d", false, false));
			Assert.assertEquals("bcdefgh", _logic.replaceRegex("abcdefghi", "a(?<name>.*)i", "${name}", false, false));
			Assert.assertEquals("abcaghi", _logic.replaceRegex("abcde\nfdeFghi", "d.*F", "a", true, true));
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testErrorCases() {		
		UndeclaredVariableException e1 = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			Assert.assertTrue(_logic.searchAdvanced("abc", "%1b%2", 0, false, false));
			_logic.replaceAdvanced("abc", "%1b%2", "%1%2%3", false);
		  });
		
		Assert.assertEquals(e1.getMessage(), "Variable %3 from replace text has not been used in search pattern!");
		
		Exception e2 = Assert.assertThrows(Exception.class, () -> {
			Assert.assertTrue(_logic.searchRegex("abc", "a.*c", 0, false, false, false));
			_logic.replaceRegex("abc", "a.*c", "ab${name}c", false, false);
		  });
		
		Assert.assertFalse(_logic.searchAdvanced("abcdefghi", "d%1f", -4, false, false));
		Assert.assertEquals(_logic.getMessage(), "Index out of range: -4");
		
		Assert.assertFalse(_logic.searchAdvanced("abcdefghi", "d%1f", 15, false, false));
		Assert.assertEquals(_logic.getMessage(), "Index out of range: 15");
	}
	
	@Test
	void testReplaceAdvanced() {
		try {
			Assert.assertEquals("aded", _logic.replaceAdvanced("abcd", "b%1c", "de", false));
			Assert.assertEquals("aded", _logic.replaceAdvanced("abcd", "b%6c", "de", true));
			Assert.assertEquals("abcaghi", _logic.replaceAdvanced("abcdefghi", "d%1f", "a", false));
			Assert.assertEquals("abcaghi", _logic.replaceAdvanced("abcdefdeFghi", "d%1F", "a", true));
			Assert.assertEquals("abcaghi", _logic.replaceAdvanced("abcd\ne\nfghi", "d%2f", "a", false));
			Assert.assertEquals(null, _logic.replaceAdvanced("abcd", "a(b", "d", false));
			Assert.assertEquals(null, _logic.replaceAdvanced("abcd", "a%1%1d", "d", false));
			Assert.assertEquals(null, _logic.replaceAdvanced("abcd", "ac%1b", "d", false));
			Assert.assertEquals(null, _logic.replaceAdvanced("abcd", "a%1%2b", "d", false));
			Assert.assertEquals("bcdefgh", _logic.replaceAdvanced("abcdefghi", "a%2i", "%2", false));
			Assert.assertEquals("abcaghi", _logic.replaceAdvanced("abcde\nfdeFghi", "d%3F", "a", true));
			Assert.assertEquals("bcagh", _logic.replaceAdvanced("abcde\nfdeFghi", "a%1i", "bcagh", true));
			Assert.assertEquals("bcaiagh", _logic.replaceAdvanced("abcde\nfdeFghi", "%0b%1h%2", "bc%0%2agh", true));
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	void checkResult(int start, int end, String pattern) {
		if (start != -1) { 
			Assert.assertEquals(start, _logic.getStart());
			Assert.assertEquals(end, _logic.getEnd());
			Assert.assertEquals("\"" + pattern + "\"" + " found at line ", _logic.getMessage());
		} else {
			Assert.assertEquals(start, _logic.getStart());
			Assert.assertEquals(end, _logic.getEnd());
			Assert.assertEquals("\"" + pattern + "\"" + " not found from line ", _logic.getMessage());
		}
	}

}
