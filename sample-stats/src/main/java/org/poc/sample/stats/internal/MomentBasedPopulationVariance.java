package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class MomentBasedPopulationVariance extends MomentBasedVariance {

    MomentBasedPopulationVariance(final SecondMoment secondMoment) {
        super(secondMoment);
    }

    @Override
    protected double getVariance() {
        long n = secondMoment.getN();
        double m2 = secondMoment.getAsDouble();
        return m2 / n;
    }

    @Override
    public void merge(UnivaritateStatistic other) {
        MomentBasedPopulationVariance otherMomentBasedPopulationVariance = (MomentBasedPopulationVariance) other;

    }
}