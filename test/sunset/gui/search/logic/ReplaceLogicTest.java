package sunset.gui.search.logic;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.logic.interfaces.IReplaceLogic;

class ReplaceLogicTest {
	
	static IReplaceLogic _replaceLogic;

	@BeforeEach
	void setUp() throws Exception {
		_replaceLogic = new ReplaceLogic();
	}

	@AfterEach
	void tearDown() throws Exception {
		_replaceLogic = null;
	}
	
	@Test
	void testReplaceRegex() {
		try {
			Assert.assertEquals("aded", _replaceLogic.replaceRegex("abcd", "bc", "de", false, false));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceRegex("abcdefghi", "d.*f", "a", false, false));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceRegex("abcdefdeFghi", "d.*F", "a", true, false));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceRegex("abcd\ne\nfghi", "d.*f", "a", false, true));
			Assert.assertNull(_replaceLogic.replaceRegex("abcd", "a(b", "d", false, false));
			Assert.assertEquals("bcdefgh", _replaceLogic.replaceRegex("abcdefghi", "a(?<name>.*)i", "${name}", false, false));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceRegex("abcde\nfdeFghi", "d.*F", "a", true, true));
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	void testReplaceAdvanced() {
		try {
			Assert.assertEquals("aded", _replaceLogic.replaceAdvanced("abcd", "b%1c", "de", null, false, true));
			Assert.assertEquals("aded", _replaceLogic.replaceAdvanced("abcd", "b%6c", "de", null, true, true));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceAdvanced("abcdefghi", "d%1f", "a", null, false, true));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceAdvanced("abcdefdeFghi", "d%1F", "a", null, true, true));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceAdvanced("abcd\ne\nfghi", "d%2f", "a", null, false, true));
			Assert.assertNull(_replaceLogic.replaceAdvanced("abcd", "a(b", null, "d", false, true));
			Assert.assertNull(_replaceLogic.replaceAdvanced("abcd", "a%1%1d", "d", null, false, true));
			Assert.assertNull(_replaceLogic.replaceAdvanced("abcd", "ac%1b", "d", null, false, true));
			Assert.assertNull(_replaceLogic.replaceAdvanced("abcd", "a%1%2b", "d", null, false, true));
			Assert.assertEquals("bcdefgh", _replaceLogic.replaceAdvanced("abcdefghi", "a%2i", "%2", null, false, true));
			Assert.assertEquals("abcaghi", _replaceLogic.replaceAdvanced("abcde\nfdeFghi", "d%3F", "a", null, true, true));
			Assert.assertEquals("bcagh", _replaceLogic.replaceAdvanced("abcde\nfdeFghi", "a%1i", "bcagh", null, true, true));
			Assert.assertEquals("bcaiagh", _replaceLogic.replaceAdvanced("abcde\nfdeFghi", "%0b%1h%2", "bc%0%2agh", null, true, true));
		} catch (UndeclaredVariableException e) {
			Assert.fail();
		}
	}
	
	@Test
	void testErrorCases() {		
		UndeclaredVariableException e1 = Assert.assertThrows(UndeclaredVariableException.class, () -> {
			_replaceLogic.replaceAdvanced("abc", "%1b%2", "%1%2%3", null, false, true);
		  });
		
		Assert.assertEquals("Variable %3 from replace text has not been used in search pattern!", e1.getMessage());
		
		Exception e2 = Assert.assertThrows(Exception.class, () -> {
			_replaceLogic.replaceRegex("abc", "a.*c", "ab${name}c", false, false);
		  });
		
		if (e2 != null)
			;
	}
}