package org.pintoschneider.void_of_the_unfathomable.animation.curves;

/**
 * A linear animation curve that maps input directly to output.
 */
public final class LinearCurve extends Curve {
    @Override
    double transformInternal(double t) {
        return t;
    }
}
