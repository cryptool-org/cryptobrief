/**
 * 
 */
package sunset.gui.listener;


import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;


/**
 * Action Listener for exit Sunset 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class CaretListenerColumnPosition implements CaretListener {

	private JLabel _jLabel_LineColumn;
	
	/**
	 * 
	 * @param frame
	 * @param execution
	 */
	public CaretListenerColumnPosition(JLabel jLabel_LineColumn){
		_jLabel_LineColumn = jLabel_LineColumn;
	}

	@Override
	public void caretUpdate(CaretEvent arg) {
		
		Element root, e;
		Document document = ((JTextPane)arg.getSource()).getDocument();
		root = document.getRootElements()[0];
		String txt;
		int position = arg.getDot();
		
		for(int i = 0; i < root.getElementCount(); i++){
			e = root.getElement(i);
			if(e.getStartOffset() <= position && position < e.getEndOffset() ){
				if(_jLabel_LineColumn != null){
					try {
						txt = document.getText(e.getStartOffset(), position - e.getStartOffset());
						_jLabel_LineColumn.setText((i+1) + " : " + (position - e.getStartOffset() + 1 + countTabColumns(txt)));
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				break;
			}
		}
	}
	
	/**
	 * count columns in txt where tab is included
	 * @param txt
	 * @return columns of txt
	 */
	private int countTabColumns(String txt){
		int res = 0;
		int index = txt.indexOf("\t");
		int val;
		
			while(index >= 0){
				val = index % 8;
				if(val == 0){
					//multiple of 8, i.e. perfect match
					res = res + 7;
				}else{
					res = res + (7 - val);
				}
				
				txt = txt.substring(index + 1, txt.length());
				index = txt.indexOf("\t");
			}
			
		return res;
		
	}
	

}
