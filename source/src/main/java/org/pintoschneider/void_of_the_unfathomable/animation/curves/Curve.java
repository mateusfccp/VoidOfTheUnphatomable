package org.pintoschneider.void_of_the_unfathomable.animation.curves;

public abstract class Curve {
    public final double transform(double t) {
        if (t == 0.0 || t == 1.0) {
            return t;
        } else {
            return transformInternal(t);
        }
    }

    abstract double transformInternal(double t);
}

