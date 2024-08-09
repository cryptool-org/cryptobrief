package web;

import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSPromise;
import org.teavm.jso.core.JSString;
import org.teavm.jso.function.JSConsumer;

/**
 * Thread executor that returns a JavaScript Promise which resolves/rejects depending on the execution result.
 * @author Bernd Busse
 * @version 1.0
 */
class ThreadExecutor extends Thread implements JSPromise.Executor<Object> {
    private Thread _thread;
    private int _timeout;

    private JSConsumer<Object> _resolve;
    private JSConsumer<Object> _reject;

    public ThreadExecutor(Thread thread, int timeout) {
        _thread = thread;
        _timeout = timeout;
    }

    @Override
    public void run() {
        _thread.start();

        try {
            _thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (_reject != null && !JSObjects.isUndefined(_reject)) {
                _reject.accept(JSString.valueOf("Interrupted"));
            }
        } catch (Exception e) {
            if (_reject != null && !JSObjects.isUndefined(_reject)) {
                _reject.accept(JSString.valueOf(e.toString()));
            }
        } finally {
            if (_resolve != null && !JSObjects.isUndefined(_resolve)) {
                _resolve.accept(null);
            }
        }
    }

    @Override
    public void onExecute(JSConsumer<Object> resolveFunc, JSConsumer<Object> rejectFunc) {
        _resolve = resolveFunc;
        _reject = rejectFunc;

        this.start();
    }
}
