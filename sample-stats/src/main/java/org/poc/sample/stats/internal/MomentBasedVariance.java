package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

abstract class MomentBasedVariance implements UnivaritateStatistic {
    protected final SecondMoment secondMoment;

    MomentBasedVariance(final SecondMoment secondMoment) {
        this.secondMoment = secondMoment;
    }

    @Override
    public long getN() {
        return secondMoment.getN();
    }

    @Override
    public void accept(double value) {
        secondMoment.accept(value);
    }

    @Override
    public double getAsDouble() {
        long n = secondMoment.getN();
        if (n == 0) {
            return Double.NaN;
        } else if (n == 1) {
            return 0d;
        } else {
            return getVariance();
        }
    }

    protected abstract double getVariance();
}