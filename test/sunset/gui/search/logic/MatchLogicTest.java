package sunset.gui.search.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.search.logic.interfaces.IMatcherLogic;

class MatchLogicTest {
	
	static IMatcherLogic _matcherLogic;

	@BeforeEach
	void setUp() throws Exception {
		_matcherLogic = new MatcherLogic();
	}

	@AfterEach
	void tearDown() throws Exception {
		_matcherLogic = null;
	}
	
	@Test
	void testEquals() {
		assertTrue(_matcherLogic.equals("abc", "abc", false));
		assertTrue(_matcherLogic.equals("abc", "abc", true));
		assertTrue(_matcherLogic.equals("abc", "aBc", false));
		assertFalse(_matcherLogic.equals("abc", "abcd", false));
		assertFalse(_matcherLogic.equals("abc", "aBc", true));
	}
	
	@Test
	void testMatchesRegex() {
		assertTrue(_matcherLogic.matchesRegex("abc", "abc", false, false));
		assertTrue(_matcherLogic.matchesRegex("abc", "abc", true, false));
		assertTrue(_matcherLogic.matchesRegex("abc", "aBc", false, false));
		assertTrue(_matcherLogic.matchesRegex("aBc", "aBc", true, false));
		assertTrue(_matcherLogic.matchesRegex("abc", "a.*c", true, false));
		assertTrue(_matcherLogic.matchesRegex("ab\nc", "a.*c", true, true));
		
		assertFalse(_matcherLogic.matchesRegex("abc", "abcd", false, false));
		assertFalse(_matcherLogic.matchesRegex("abc", "aBc", true, false));
		assertFalse(_matcherLogic.matchesRegex("ab\nc", "a.*c", true, false));
		assertFalse(_matcherLogic.matchesRegex("abc", "a(c", true, false));
	}
	
	@Test
	void testMatchesAdvanced() {
		assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", false, null));
		assertTrue(_matcherLogic.matchesAdvanced("abc", "ab%1", true, null));
		assertTrue(_matcherLogic.matchesAdvanced("abc", "%1Bc", false, null));
		assertTrue(_matcherLogic.matchesAdvanced("aBc", "aB%9", true, null));
		assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", true, null));
		assertTrue(_matcherLogic.matchesAdvanced("ab\nc", "a%2c", true, null));
		
		assertFalse(_matcherLogic.matchesAdvanced("abc", "abcd", false, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5%6c", false, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "a%5b%5c", false, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5cd", false, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "%1Bc", true, null));
		assertFalse(_matcherLogic.matchesAdvanced("ab\nc", "a%1C", true, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "a(c%1", true, null));
		assertFalse(_matcherLogic.matchesAdvanced("abc", "a%0b%1d", true, null));
	}
}
