package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

/**
 * <p>Marker interface for unmodifiable views. Any class that implements this interface
 * will be assumed to be an unmodifiable view, and will be treated as such. Classes
 * which implement this interface must not allow any changes to the underlying
 * value data represented by the class; otherwise the behavior of methods that
 * return collection views backed by the implementing classes may also violate
 * their unmodifiability contracts.</p>
 * @apiNote <p>This does not in fact contractually require that any method which
 * theoretically could modify the underlying represented data must always throw
 * {@code UnsupportedOperationException}; rather, it only requires that an operation
 * whose contract would otherwise require it to modify the data must instead throw
 * an exception or error. Many operations may situationally return a value indicating
 * that the data was not modified, or may throw an exception other than
 * {@code UnsupportedOperationException} to indicate failure to modify data (e.g.
 * {@code IllegalStateException}).</p>
 * <p>Further, this does not actually require that methods exposed by this class
 * not alter state, only that it not alter the data contractually represented by the
 * view. For example, an "unmodifiable" iterator will still update its cursor when
 * the {@code next} method is called, because the state of the iteration is not a
 * reflection of the data it contractually represents.</p>
 */
@PrereleaseContent
interface UnmodifiableView { }
