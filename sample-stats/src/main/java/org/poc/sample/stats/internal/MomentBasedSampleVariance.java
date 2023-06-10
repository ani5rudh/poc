package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class MomentBasedSampleVariance extends MomentBasedVariance {

    MomentBasedSampleVariance(final SecondMoment secondMoment) {
        super(secondMoment);
    }

    @Override
    public void merge(UnivaritateStatistic other) {
        // TODO: Implementation
    }

    @Override
    protected double getVariance() {
        long n = super.getN();
        double m2 = secondMoment.getAsDouble();
        return m2 / (n - 1d);
    }
}
