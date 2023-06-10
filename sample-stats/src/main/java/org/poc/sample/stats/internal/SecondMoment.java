package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class SecondMoment implements UnivaritateStatistic {

    private final FirstMoment firstMoment;
    private final boolean updateFirstMoment;

    private double m2;

    SecondMoment(final FirstMoment firstMoment, final boolean updateFirstMoment) {
        this.firstMoment = firstMoment;
        this.updateFirstMoment = updateFirstMoment;
    }

    @Override
    public long getN() {
        return firstMoment.getN();
    }

    @Override
    public void merge(UnivaritateStatistic other) {}

    @Override
    public void accept(double value) {
        if (updateFirstMoment) {
            firstMoment.accept(value);
        }
        m2 += ((double) firstMoment.getN() - 1) * firstMoment.getDev() * firstMoment.getnDev();
    }

    @Override
    public double getAsDouble() {
        return m2;
    }

}
