package sunset.gui.search.advanced;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.util.SearchReplaceMessageHandler;

class AdvancedConstructorTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testGoodMatchingPairConfiguration() {
		try {
			new AdvancedSearchReplace("");
			new AdvancedSearchReplace("(...)");
			new AdvancedSearchReplace("abcdef...xyz");
			new AdvancedSearchReplace("()%&$...?)!\"%");
			new AdvancedSearchReplace("a...b,c...d");
			new AdvancedSearchReplace("{...}, \\begin{%3}...\\end{%3}, \\begin{%1}...\\end{%1}");
			new AdvancedSearchReplace("\\begin{%1}...\\end{%1}");
			new AdvancedSearchReplace("\\begin{%0}...\\end{%0}");
			new AdvancedSearchReplace("()%&$...?)!\"%,?)!\\\"%...()%&$");
			new AdvancedSearchReplace("{...}, \\beg{%1-%2-%3}...\\end{%1-%2-%3}");
			new AdvancedSearchReplace("(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}");
		} catch (MatchingPairConfigurationException e) {
			Assert.fail();
		}
	}

	@Test
	void testBadMatchingPairConfiguration() {
		MatchingPairConfigurationException e;
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace(",");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", ","), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			AdvancedSearchReplace inst = new AdvancedSearchReplace("abcd");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "abcd"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("a...");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "a..."), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("...b");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "...b"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace(".....");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "....."), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace(",...,");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", ",...,"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("{...},");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "{...},"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("{...,");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "{...,"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("...)");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "...)"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace(",");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", ","), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("(),%&$...?)!\"%");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "(),%&$...?)!\"%"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("()%&$...?)!\",%");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "()%&$...?)!\",%"), e.getMessage());

		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("{...}, \\begin{%3}...\\end{%3}, \\begin{%1}...\\end{}");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "\\begin{%1}...\\end{}"), e.getMessage());

		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("\\begin{}...\\end{%1}");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "\\begin{}...\\end{%1}"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("\\begin{%0}...\\end{%1}");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "\\begin{%0}...\\end{%1}"), e.getMessage());
		
		e = Assert.assertThrows(MatchingPairConfigurationException.class, () -> {
			new AdvancedSearchReplace("{...}, \\beg{%1-%2-%3}...\\end{%1-%2-%4}");
		  });
		
		Assert.assertEquals(SearchReplaceMessageHandler.getInstance().getMessage("exception_matchingpairconfig", "\\beg{%1-%2-%3}...\\end{%1-%2-%4}"), e.getMessage());
	}

}
