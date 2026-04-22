/**
 *
 */
package sunset.gui.lib;

import sunset.gui.debugger.IFFaplDebugger;

import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;

/**
 * Represents a Thread which
 *
 * @author Alexander Ortner
 * @version 1.0
 */
public class ExecuteThread extends Thread {


    private Vector<Component> _startComp;
    private Vector<Component> _stopComp;
    private Vector<Component> _stepOverComp;
    private Vector<Component> _stepIntoComp;
    private Vector<Component> _resumeComp;
    private Thread _thread;
    private IFFaplDebugger _debugger;


    /**
     * @param thread
     * @param startComp
     * @param stopComp
     */
    public ExecuteThread(Thread thread, Vector<Component> startComp, Vector<Component> stopComp, Vector<Component> stepOverComp, Vector<Component> stepIntoComp, Vector<Component> resumeComp, IFFaplDebugger debugger) {
        _thread = thread;
        _startComp = startComp;
        _stopComp = stopComp;
        _stepOverComp = stepOverComp;
        _stepIntoComp = stepIntoComp;
        _resumeComp = resumeComp;
        _debugger = debugger;
    }


    @Override
    public void run() {
        if (_debugger != null) {
            _debugger.startExecution();
        }
        startExecution();

        _thread.start();

        try {
            _thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (_debugger != null) {
            _debugger.endExecution();
        }
        endExecution();
    }

    public void startExecution() {
        setEnabled(_startComp, false);
        setEnabled(_stopComp, true);
        setEnabled(_stepOverComp, false);
        setEnabled(_stepIntoComp, false);
        setEnabled(_resumeComp, false);
    }

    public void endExecution() {
        setEnabled(_stopComp, false);
        setEnabled(_startComp, true);
        setEnabled(_stepOverComp, false);
        setEnabled(_stepIntoComp, false);
        setEnabled(_resumeComp, false);
    }

    /**
     * Enables components according val
     *
     * @param comp
     * @param val
     */
    public static void setEnabled(Vector<Component> comp, boolean val) {
        if (comp == null) return; // for running at command line
        for (Iterator<Component> itr = comp.iterator(); itr.hasNext(); ) {
            itr.next().setEnabled(val);
        }
    }


}
