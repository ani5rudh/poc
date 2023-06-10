package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class MomentBasedMean implements UnivaritateStatistic {

    protected final FirstMoment firstMoment;

    MomentBasedMean(final FirstMoment firstMoment) {
       this.firstMoment = firstMoment;
    }

    @Override
    public void accept(double value) {
        firstMoment.accept(value);
    }

    @Override
    public double getAsDouble() {
        return firstMoment.getAsDouble();
    }

    @Override
    public long getN() {
        return firstMoment.getN();
    }

    @Override
    public void merge(UnivaritateStatistic other) {
        MomentBasedMean otherMomentBasedMean = (MomentBasedMean) other;
        this.firstMoment.merge(otherMomentBasedMean.firstMoment);
    }
}