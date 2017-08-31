package ffapl.java.matcher;

import java.awt.Font;

import sunset.gui.editor.FFaplRegex;


public class FuncProcMatcher extends RegexMatcher{

	public FuncProcMatcher(String regex){
		super(regex);
		setFontWeight(Font.ITALIC);
		setColor(FFaplRegex.BLACK);
	}
	
	public boolean find() {
		boolean result = super.find();
		if(result){
			setEnd(end()-1);
		}
		return result;
	}
}
