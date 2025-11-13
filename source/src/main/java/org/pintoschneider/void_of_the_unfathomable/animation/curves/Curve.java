package org.pintoschneider.void_of_the_unfathomable.animation.curves;

/**
 * An abstract class that represents a Bézier curve.
 */
public abstract class Curve {
    /**
     * Transforms the given value t (0.0 to 1.0) according to the curve.
     *
     * @param t A value between 0.0 and 1.0 that represents the progress of the animation.
     * @return The transformed value according to the curve.
     */
    public final double transform(double t) {
        if (t == 0.0 || t == 1.0) {
            return t;
        } else {
            return transformInternal(t);
        }
    }

    abstract double transformInternal(double t);
}

