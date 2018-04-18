package ffapl.utils;

import java.util.Vector;

/**
 * Duplicated Java standard class to cut deprecated accesses.
 *
 * @see java.util.Observable java.util.Observable (deprecated since Java 9)
 */
public class Observable {

    private boolean changed = false;
    private Vector<Observer> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public Observable() {
        obs = new Vector<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing {@code null} to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(Observer o) {
        obs.removeElement(o);
    }

    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and {@code null}. In other
     * words, this method is equivalent to:
     * <blockquote>{@code
     * notifyObservers(null)}</blockquote>
     */
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and the {@code arg} argument.
     *
     * @param arg any object.
     */
    public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((Observer) arrLocal[i]).update(this, arg);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    /**
     * Marks this {@code Observable} object as having been changed; the
     * {@code hasChanged} method will now return {@code true}.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the {@code hasChanged} method will now return {@code false}.
     * This method is called automatically by the
     * {@code notifyObservers} methods.
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return {@code true} if and only if the {@code setChanged}
     * method has been called more recently than the
     * {@code clearChanged} method on this object;
     * {@code false} otherwise.
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this {@code Observable} object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }
}
