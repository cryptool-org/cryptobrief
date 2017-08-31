package sunset.gui.comparator;

import java.util.Comparator;

import sunset.gui.view.FFaplStatusLine;

public class LineStateComparator implements Comparator<FFaplStatusLine> {

	@Override
	public int compare(FFaplStatusLine arg0, FFaplStatusLine arg1) {
		if(arg0.getState().equals(arg1.getState())){
			if(arg0.getLine() == arg1.getLine()){
				if(arg0.getColumn() == arg1.getColumn()){
					return 0;
				}else{
					return arg0.getColumn().compareTo(arg1.getColumn());
				}
			}else{
				return arg0.getLine().compareTo(arg1.getLine());
			}
		}else{
			return arg0.getState().getCode().compareTo(arg1.getState().getCode());
		}
	}
}
