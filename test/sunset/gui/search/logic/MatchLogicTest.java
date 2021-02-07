package sunset.gui.search.logic;

import org.junit.Assert;
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
		Assert.assertTrue(_matcherLogic.equals("abc", "abc", false));
		Assert.assertTrue(_matcherLogic.equals("abc", "abc", true));
		Assert.assertTrue(_matcherLogic.equals("abc", "aBc", false));
		Assert.assertFalse(_matcherLogic.equals("abc", "abcd", false));
		Assert.assertFalse(_matcherLogic.equals("abc", "aBc", true));
	}
	
	@Test
	void testMatchesRegex() {
		Assert.assertTrue(_matcherLogic.matchesRegex("abc", "abc", false, false));
		Assert.assertTrue(_matcherLogic.matchesRegex("abc", "abc", true, false));
		Assert.assertTrue(_matcherLogic.matchesRegex("abc", "aBc", false, false));
		Assert.assertTrue(_matcherLogic.matchesRegex("aBc", "aBc", true, false));
		Assert.assertTrue(_matcherLogic.matchesRegex("abc", "a.*c", true, false));
		Assert.assertTrue(_matcherLogic.matchesRegex("ab\nc", "a.*c", true, true));
		
		Assert.assertFalse(_matcherLogic.matchesRegex("abc", "abcd", false, false));
		Assert.assertFalse(_matcherLogic.matchesRegex("abc", "aBc", true, false));
		Assert.assertFalse(_matcherLogic.matchesRegex("ab\nc", "a.*c", true, false));
		Assert.assertFalse(_matcherLogic.matchesRegex("abc", "a(c", true, false));
	}
	
	@Test
	void testMatchesAdvanced() {
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", false, null));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "ab%1", true, null));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "%1Bc", false, null));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("aBc", "aB%9", true, null));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", true, null));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("ab\nc", "a%2c", true, null));
		
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "abcd", false, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5%6c", false, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a%5b%5c", false, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5cd", false, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "%1Bc", true, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("ab\nc", "a%1C", true, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a(c%1", true, null));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a%0b%1d", true, null));
	}
}