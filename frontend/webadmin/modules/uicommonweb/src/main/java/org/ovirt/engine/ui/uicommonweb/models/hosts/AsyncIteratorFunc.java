package org.ovirt.engine.ui.uicommonweb.models.hosts;

/**
 * Defines method to be executed in order to process item in AsyncIterator.
 *
 * @param <T>
 */
public interface AsyncIteratorFunc<T> {

    public void run(T item, AsyncIteratorCallback callback);
}
