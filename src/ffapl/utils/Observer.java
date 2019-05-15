package ffapl.utils;

/**
 * Duplicated Java standard class to cut deprecated accesses.
 *
 * @see java.util.Observer java.util.Observer (deprecated since Java 9)
 */
public interface Observer {
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    void update(Observable o, Object arg);
}
