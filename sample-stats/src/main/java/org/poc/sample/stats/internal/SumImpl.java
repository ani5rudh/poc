package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class SumImpl implements UnivaritateStatistic {

    private long n;

    private double value;

    SumImpl() {
        this(0, 0);
    }

    private SumImpl(final long n, final double value) {
        this.n = n;
        this.value = value;
    }

    @Override
    public long getN() {
        return n;
    }

    @Override
    public void merge(UnivaritateStatistic other) {
        SumImpl otherSum = (SumImpl) other;
        this.n += otherSum.n;
        this.value += otherSum.value;
    }

    @Override
    public void accept(double value) {
        n++;
        this.value += value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }
}
