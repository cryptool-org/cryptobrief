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
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", null, false));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "ab%1", null, true));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "%1Bc", null, false));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("aBc", "aB%9", null, true));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("abc", "a%1c", null, true));
		Assert.assertTrue(_matcherLogic.matchesAdvanced("ab\nc", "a%2c", null, true));
		
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "abcd", null, false));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5%6c", null, false));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a%5b%5c", null, false));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "ab%5cd", null, false));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "%1Bc", null, true));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("ab\nc", "a%1C", null, true));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a(c%1", null, true));
		Assert.assertFalse(_matcherLogic.matchesAdvanced("abc", "a%0b%1d", null, true));
	}
}