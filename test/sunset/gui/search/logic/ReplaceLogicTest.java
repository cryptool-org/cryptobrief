package sunset.gui.search.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
			assertEquals("aded", _replaceLogic.replaceRegex(new ReplaceContext("abcd", "bc", "de", false), false));
			assertEquals("abcaghi", _replaceLogic.replaceRegex(new ReplaceContext("abcdefghi", "d.*f", "a", false), false));
			assertEquals("abcaghi", _replaceLogic.replaceRegex(new ReplaceContext("abcdefdeFghi", "d.*F", "a", true), false));
			assertEquals("abcaghi", _replaceLogic.replaceRegex(new ReplaceContext("abcd\ne\nfghi", "d.*f", "a", false), true));
			assertNull(_replaceLogic.replaceRegex(new ReplaceContext("abcd", "a(b", "d", false), false));
			assertEquals("bcdefgh", _replaceLogic.replaceRegex(new ReplaceContext("abcdefghi", "a(?<name>.*)i", "${name}", false), false));
			assertEquals("abcaghi", _replaceLogic.replaceRegex(new ReplaceContext("abcde\nfdeFghi", "d.*F", "a", true), true));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testReplaceAdvanced() {
		try {
			assertEquals("aded", _replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "b%1c", "de", false), null, true));
			assertEquals("aded", _replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "b%6c", "de", true), null, true));
			assertEquals("abcaghi", _replaceLogic.replaceAdvanced(new ReplaceContext("abcdefghi", "d%1f", "a", false), null, true));
			assertEquals("abcaghi", _replaceLogic.replaceAdvanced(new ReplaceContext("abcdefdeFghi", "d%1F", "a", true), null, true));
			assertEquals("abcaghi", _replaceLogic.replaceAdvanced(new ReplaceContext("abcd\ne\nfghi", "d%2f", "a", false), null, true));
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "a(%1b", "d", false), null, true));
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "a%3d%1d", "d", false), null, true));
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "ac%1b", "d", false), null, true));
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abcd", "a%1c%2b", "d", false), null, true));
			assertEquals("bcdefgh", _replaceLogic.replaceAdvanced(new ReplaceContext("abcdefghi", "a%2i", "%2", false), null, true));
			assertEquals("abcaghi", _replaceLogic.replaceAdvanced(new ReplaceContext("abcde\nfdeFghi", "d%3F", "a", true), null, true));
			assertEquals("bcagh", _replaceLogic.replaceAdvanced(new ReplaceContext("abcde\nfdeFghi", "a%1i", "bcagh", true), null, true));
			assertEquals("bcaiagh", _replaceLogic.replaceAdvanced(new ReplaceContext("abcde\nfdeFghi", "%0b%1h%2", "bc%0%2agh", true), null, true));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testErrorCases() {	
		
		Exception e = assertThrows(Exception.class, () -> {
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abc", "b", "%1%2%3", false), null, true));
			});
		
		e = assertThrows(Exception.class, () -> {
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abc", "%1%2", "%1%2%3", false), null, true));
			});
		
		e = assertThrows(Exception.class, () -> {
			assertNull(_replaceLogic.replaceAdvanced(new ReplaceContext("abc", "%1b%2", "%1%2%3", false), null, true));
			});
		
		e = assertThrows(Exception.class, () -> {
			_replaceLogic.replaceRegex(new ReplaceContext("abc", "a.*c", "ab${name}c", false), false);
		  });
		
		if (e != null)
			;
	}
}
