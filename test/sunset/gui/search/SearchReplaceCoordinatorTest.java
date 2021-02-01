package sunset.gui.search;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

class SearchReplaceCoordinatorTest {

	static ISearchReplaceCoordinator _coordinator;
	static FFaplJFrame _frame;
	static JDialogSearchReplace _dialog;

	@BeforeAll
	static void setUp() throws Exception {
		_frame = new FFaplJFrame(null);
		_dialog = new JDialogSearchReplace(_frame);
		_coordinator = new SearchReplaceCoordinator(_dialog);
		_coordinator.resetCaretPosition();
	}
	
	@BeforeEach
	void beforeEach() throws Exception {
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\nnew line\n\t%453!\"\n\t\t§$%&/()=?\nend of text.");
		_dialog.setReplaceAllFromStart(true);
	}

	@Test
	void testSimpleSearch() {
		_dialog.setSearchPattern("this");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setSearchPattern("t%1s");
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
	}
	
	@Test
	void testMatchCase() {
		_dialog.setMatchCase(false);
		_dialog.setSearchPattern("tHis");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setSearchPattern("tH%1s");
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_dialog.setMatchCase(true);
		
		_coordinator.resetCaretPosition();
		_dialog.setUseStandardSearch(true);
		Assert.assertFalse(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertFalse(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setSearchPattern("tH%s");
		_dialog.setUseAdvancedSearch(true);
		Assert.assertFalse(_coordinator.findString(false));
	}
	
	@Test
	void testWrapAround() {
		_dialog.setSearchPattern("line");
		_dialog.setMatchCase(false);
		_dialog.setWrapAround(true);
		
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_dialog.setUseStandardSearch(true);
		Assert.assertFalse(_coordinator.findString(true));
		
		_dialog.setUseRegEx(true);
		Assert.assertFalse(_coordinator.findString(true));
		
		_dialog.setUseAdvancedSearch(true);
		_dialog.setSearchPattern("new l%5e");
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertFalse(_coordinator.findString(true));
	}
	
	@Test
	void testReplaceAllFromStart() {
		/*_frame.getCurrentCodePanel().getCodePane()
		.setText("abcabcabc");
		
		_dialog.setSearchPattern("a%1c");
		_dialog.setUseAdvancedSearch(true);
		_dialog.setReplaceText("aa%1cc");
		
		_coordinator.findString(false);
		
		// start "Replace" button pressed
		if (_coordinator.isSearchPatternSelected()) {
			if (_coordinator.replaceText()) {
				boolean found = _coordinator.findString(false);
				
				_coordinator.setStatus("Replace: 1 occurrence replaced, " + 
				(found ? "next occurrence found" : "no further occurrences found"), SearchStatus.REPLACE_SUCCESS);
			}
		} else {
			_coordinator.findString(false);
		}
		// end
		
		_dialog.setReplaceAllFromStart(false);
		
		_coordinator.resetCaretPosition();
		
		int count = 0;
		while (_coordinator.findString(true)) {
			if (_coordinator.replaceText()) {
				count++;
			} else {
				break;
			}
		}
		
		Assert.assertEquals(2, count);
		String text = _frame.getCurrentCodePanel().getCodePane().getText();
		Assert.assertEquals("aabccaabccaabcc", text);
		
		/*_frame.getCurrentCodePanel().getCodePane()
		.setText("abcabcabc");
		
		_coordinator.findString(false);
		_coordinator.replaceText();
		
		_dialog.setReplaceAllFromStart(true);
		
		_coordinator.resetCaretPosition();
		
		count = 0;
		while (_coordinator.findString(true)) {
			if (_coordinator.replaceText()) {
				count++;
			} else {
				break;
			}
		}
		
		Assert.assertEquals(3, count);
		text = _frame.getCurrentCodePanel().getCodePane().getText();
		Assert.assertEquals("aaabcccaabccaabcc", text);*/
	}
	
	@Test
	void testUseSpecialSymbols() {
		// standard search
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\nnew line\n\t%453!\"\n\t\t§$%&/()=?\nend of text.");
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(true);
		_dialog.setUseStandardSearch(true);
		
		_dialog.setSearchPattern("\\n");
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(false);
		Assert.assertFalse(_coordinator.findString(false));
		
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\\nnew line\\n\t%453!\"\\t\t§$%&/()=?end of text.");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(true);
		Assert.assertFalse(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(false);
		Assert.assertTrue(_coordinator.findString(false));
		
		// advanced search
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\nnew line\n\t%453!\"\n\t\t§$%&/()=?\nend of text.");
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(true);
		_dialog.setUseAdvancedSearch(true);
		
		_dialog.setSearchPattern("\\n%1 ");
		Assert.assertTrue(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(false);
		Assert.assertFalse(_coordinator.findString(false));
		
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\\nnew line\\n\t%453!\"\\t\t§$%&/()=?end of text.");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(true);
		Assert.assertFalse(_coordinator.findString(false));
		
		_coordinator.resetCaretPosition();
		_dialog.setUseSpecialSymbols(false);
		Assert.assertTrue(_coordinator.findString(false));
	}
	
	@Test
	void testZeroLengthMatch() {
		_dialog.setMatchCase(false);
		_dialog.setWrapAround(false);
		
		_dialog.setSearchPattern("");
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_dialog.setSearchPattern("^");
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		
		_frame.getCurrentCodePanel().getCodePane().setText("");
		_dialog.setSearchPattern("%1");
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
	}
	
	@Test
	void testSearchPatternSelected() {
		_dialog.setMatchCase(false);
		_dialog.setWrapAround(false);
		
		_dialog.setSearchPattern("%453!");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		
		_coordinator.resetCaretPosition();
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
	}
	
	@Test
	void testReplaceText() {
		_dialog.setMatchCase(false);
		_dialog.setWrapAround(false);
		
		_dialog.setSearchPattern("new line");
		_dialog.setReplaceText("end");
		
		_coordinator.resetCaretPosition();
		_dialog.setUseStandardSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		Assert.assertTrue(_coordinator.replaceText());
		
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\nnew line\n\t%453!\"\n\t\t§$%&/()=?\nend of text.");
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		Assert.assertTrue(_coordinator.replaceText());
		
		_frame.getCurrentCodePanel().getCodePane()
		.setText("$this$ is %a complex %$§1 text\nnew line\n\t%453!\"\n\t\t§$%&/()=?\nend of text.");
		_coordinator.resetCaretPosition();
		_dialog.setSearchPattern("text%1line");
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		Assert.assertTrue(_coordinator.replaceText());
	}
	
	@Test
	void testIncorrectBalancing() {
		_frame.getCurrentCodePanel().getCodePane().setText("a({])b");
		
		_dialog.setSearchPattern("a%1b");
		_dialog.setUseAdvancedSearch(true);
		_dialog.setShowBalancingError(true);
		
		Assert.assertFalse(_coordinator.findString(false));
		
		String selectedText = _frame.getCurrentCodePanel().getCodePane().getSelectedText();
		
		Assert.assertEquals("a({])", selectedText);
	}
	
	@Test
	void testErrorCases() {
		_dialog.setMatchCase(false);
		_dialog.setWrapAround(false);
		
		_dialog.setSearchPattern("ab(c");
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertFalse(_coordinator.findString(false));
		Assert.assertFalse(_coordinator.isSearchPatternSelected());
		Assert.assertFalse(_coordinator.replaceText());
		
		_dialog.setSearchPattern("line");
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		_dialog.setSearchPattern("li(ne");
		Assert.assertFalse(_coordinator.replaceText());
		
		_dialog.setSearchPattern("a%1%2c");
		_coordinator.resetCaretPosition();
		_dialog.setUseAdvancedSearch(true);
		Assert.assertFalse(_coordinator.findString(false));
		Assert.assertFalse(_coordinator.isSearchPatternSelected());
		Assert.assertFalse(_coordinator.replaceText());
		
		_dialog.setSearchPattern("t.*c");
		_dialog.setReplaceText("ab${name}c");
		_coordinator.resetCaretPosition();
		_dialog.setUseRegEx(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		Assert.assertFalse(_coordinator.replaceText());
		
		_dialog.setSearchPattern("t%1$%2c");
		_dialog.setReplaceText("%3");
		_coordinator.resetCaretPosition();
		_dialog.setUseAdvancedSearch(true);
		Assert.assertTrue(_coordinator.findString(false));
		Assert.assertTrue(_coordinator.isSearchPatternSelected());
		Assert.assertFalse(_coordinator.replaceText());
	}
}
