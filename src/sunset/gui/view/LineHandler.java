package sunset.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sunset.gui.comparator.LineStateComparator;

/**
 * manages the state of the Editor line
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class LineHandler {

	List<FFaplStatusLine> statusLineList;
	
	public LineHandler(){
		statusLineList = new ArrayList<FFaplStatusLine>();
	}
	
	public void addLineState(FFaplStatusLine statusLine){
		statusLineList.add(statusLine);
		Collections.sort(statusLineList,new LineStateComparator());
	}
	
	public FFaplStatusLine getStateOfLine(int line){
		for(FFaplStatusLine sLine : statusLineList){
			if(sLine.getLine() == line){
				return sLine;
			}
		}
		return null;
	}
	
	public void resetHandler(){
		statusLineList.clear();
	}
}
