/**
 * 
 */
package sunset.gui.lib;

import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;

/**
 * Represents a Thread which 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ExecuteThread extends Thread {

	
	private Vector<Component> _startComp;
	private Vector<Component> _stopComp;
	private Thread _thread;


	/**
	 * 
	 * @param thread
	 * @param execute
	 * @param stop
	 */
	public ExecuteThread(Thread thread, Vector<Component> startComp, Vector<Component> stopComp) {
		_thread = thread;
		_startComp = startComp;
		_stopComp = stopComp;
	}
	
	
	@Override
	public void run(){
		setEnabled(_startComp, false);
		setEnabled(_stopComp, true);
		_thread.start();
		
		try {
			_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setEnabled(_stopComp, false);
		setEnabled(_startComp, true);	
	}
	
	/**
	 * Enables components according val
	 * @param comp
	 * @param val
	 */
	private void setEnabled(Vector<Component> comp, boolean val){
		if (comp == null) return; // for running at command line
		for(Iterator<Component> itr = comp.iterator(); itr.hasNext(); ){
			itr.next().setEnabled(val);
		}
	}

	

}
