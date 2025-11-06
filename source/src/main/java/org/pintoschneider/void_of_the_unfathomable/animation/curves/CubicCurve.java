package org.pintoschneider.void_of_the_unfathomable.animation.curves;

/**
 * A cubic Bezier curve defined by two control points.
 */
public final class CubicCurve extends Curve {
    /**
     * A cubic animation curve that starts slowly, speeds up, and then ends slowly.
     */
    public static final CubicCurve EASE_IN_OUT = new CubicCurve(0.42, 0.0, 0.58, 1.0);
    private static final double CUBIC_ERROR_BOUND = 0.001;
    private final double a;
    private final double b;
    private final double c;
    private final double d;

    /**
     * Creates a cubic Bezier curve with the given control points.
     *
     * @param a The x-coordinate of the first control point.
     * @param b The y-coordinate of the first control point.
     * @param c The x-coordinate of the second control point.
     * @param d The y-coordinate of the second control point.
     */
    CubicCurve(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    private double evaluateCubic(double p1, double p2, double m) {
        return 3 * p1 * (1 - m) * (1 - m) * m
            + 3 * p2 * (1 - m) * m * m
            + m * m * m;
    }

    @Override
    double transformInternal(double t) {
        double start = 0.0;
        double end = 1.0;
        while (true) {
            double midpoint = (start + end) / 2.0;
            double estimate = evaluateCubic(a, c, midpoint);
            if (Math.abs(t - estimate) < CUBIC_ERROR_BOUND) {
                return evaluateCubic(b, d, midpoint);
            }
            if (estimate < t) {
                start = midpoint;
            } else {
                end = midpoint;
            }
        }
    }
}
