package org.pintoschneider.void_of_the_unfathomable.ui.core.exceptions;

/**
 * The base class for all UI-related exceptions in the application.
 */
public abstract class UIException extends RuntimeException {
    /**
     * Constructs a new UIException with the specified detail message.
     *
     * @param message The detail message.
     */
    protected UIException(String message) {
        super(message);
    }
}
