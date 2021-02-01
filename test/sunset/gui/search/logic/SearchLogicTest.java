package sunset.gui.search.logic;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.search.logic.interfaces.ISearchLogic;

class SearchLogicTest {
	
	static ISearchLogic _searchLogic;

	@BeforeEach
	void setUp() throws Exception {
		_searchLogic = new SearchLogic();
	}

	@AfterEach
	void tearDown() throws Exception {
		_searchLogic = null;
	}

	@Test
	void testSearchStandard() {
		Assert.assertTrue(_searchLogic.search("abcdefghi", "def", 0, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.search("abcdefghi", "def", 3, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.search("abcdefghi", "def", -4, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.search("abcdEfghi", "dEf", 0, true, false));
		checkResult(3,6, "dEf");
		
		Assert.assertTrue(_searchLogic.search("abcdefghi", "def", 4, false, true));
		checkResult(3,6, "def");
		
		Assert.assertFalse(_searchLogic.search("abcdefghi", "def", 4, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_searchLogic.search("abcdefghi", "def", 15, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_searchLogic.search("abcdefghi", "dEf", 0, true, false));
		checkResult(-1,-1, "dEf");
	}
	
	@Test
	void testSearchRegex() {
		Assert.assertTrue(_searchLogic.searchRegex("abcdefghi", "def", 0, false, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdefghi", "def", 3, false, false, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdefghi", "de?f", 0, false, false, false));
		checkResult(3,6, "de?f");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdfghi", "de?f", 0, false, false, false));
		checkResult(3,5, "de?f");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdEfghi", "dEf", 0, true, false, false));
		checkResult(3,6, "dEf");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdefghi", "def", 0, false, true, false));
		checkResult(3,6, "def");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcd\nfghi", "d.f", 0, false, false, true));
		checkResult(3,6, "d.f");
		
		Assert.assertTrue(_searchLogic.searchRegex("abcdefghi", "def", 4, false, true, false));
		checkResult(3,6, "def");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcdefghi", "def", 4, false, false, false));
		checkResult(-1,-1, "def");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcd\nfghi", "d.f", 0, false, false, false));
		checkResult(-1,-1, "d.f");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcdefghi", "dEf", 0, true, false, false));
		checkResult(-1,-1, "dEf");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcdefghi", "deef", 6, false, true, false));
		checkResult(-1,-1, "deef");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcdefghi", "deef", 0, false, true, false));
		checkResult(-1,-1, "deef");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcd\nfghi", "d.gf", 1, false, false, true));
		checkResult(-1,-1, "d.gf");
		
		Assert.assertFalse(_searchLogic.searchRegex("abcdefghi", "d(f", 0, false, false, false));
	}
	
	@Test
	void testSearchAdvanced() {
		Assert.assertTrue(_searchLogic.searchAdvanced("abcdefghi", "d%1f", null, 0, false, false, true));
		checkResult(3,6, "d%1f");
		
		Assert.assertTrue(_searchLogic.searchAdvanced("abcdefghi", "d%2f", null, 3, false, false, true));
		checkResult(3,6, "d%2f");
		
		Assert.assertTrue(_searchLogic.searchAdvanced("abcdEfghi", "dEf%5", null, 0, true, false, true));
		checkResult(3,9, "dEf%5");
		
		Assert.assertTrue(_searchLogic.searchAdvanced("abcdefghi", "%8def", null, 4, false, true, true));
		checkResult(0,6, "%8def");

		Assert.assertTrue(_searchLogic.searchAdvanced("abcdefghi", "d%1f", null, 0, false, false, true));
		checkResult(3,6, "d%1f");
		
		Assert.assertFalse(_searchLogic.searchAdvanced("abcdefghi", "d%1f", null, 4, false, false, true));
		checkResult(-1,-1, "d%1f");
		
		Assert.assertFalse(_searchLogic.searchAdvanced("abcdefghi", "dE%1f", null, 0, true, false, true));
		checkResult(-1,-1, "dE%1f");
		
		Assert.assertFalse(_searchLogic.searchAdvanced("abc", "%1d%2", null, 0, false, false, true));
		checkResult(-1,-1, "%1d%2");
		
		Assert.assertFalse(_searchLogic.searchAdvanced("abc", "%1%2", null, 0, false, false, true));
	}
	
	@Test
	void testErrorCases() {		
		Assert.assertFalse(_searchLogic.searchAdvanced("abcdefghi", "d%1f", null, -4, false, false, true));
		Assert.assertEquals("Index out of range: -4", _searchLogic.getMessage());
		
		Assert.assertFalse(_searchLogic.searchAdvanced("abcdefghi", "d%1f", null, 15, false, false, true));
		Assert.assertEquals("Index out of range: 15", _searchLogic.getMessage());
	}
	
	void checkResult(int start, int end, String pattern) {
		if (start != -1) { 
			Assert.assertEquals(start, _searchLogic.getStart());
			Assert.assertEquals(end, _searchLogic.getEnd());
			Assert.assertEquals("\"" + pattern + "\"" + " found at line ", _searchLogic.getMessage());
		} else {
			Assert.assertEquals(start, _searchLogic.getStart());
			Assert.assertEquals(end, _searchLogic.getEnd());
			Assert.assertEquals("\"" + pattern + "\"" + " not found from line ", _searchLogic.getMessage());
		}
	}
}