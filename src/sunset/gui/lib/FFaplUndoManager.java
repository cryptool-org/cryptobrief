/**
 * 
 */
package sunset.gui.lib;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class FFaplUndoManager extends UndoManager {

	private int _idx;
	private int _savedIdx; //index were save was performed
	
	/**
	 * 
	 */
	public FFaplUndoManager() {
		_idx = 0;
		_savedIdx = 0;
	}
	
	@Override
	public void undo(){
		super.undo();
		_idx --;
	}
	
	@Override
	public void redo(){
		super.redo();
		_idx ++;
	}
	
	@Override
	public void discardAllEdits(){
		super.discardAllEdits();
		_idx = 0;
	}

	
	@Override
	public boolean addEdit(UndoableEdit anEdit){
		boolean result = super.addEdit(anEdit);
		if(_idx < _savedIdx){
			_savedIdx = -1; //can never come to saved state new edit
		}
		_idx ++;
		return result;
	}
	
	/**
	 * returns if current UndoManager state was saved
	 * @return
	 */
	public boolean isSaved(){
		return _savedIdx == _idx;
	}
	
	/**
	 * sets the current UndoManager state as saved state
	 */
	public void setSaved(){
		_savedIdx = _idx;
	}

	
	
	

}
