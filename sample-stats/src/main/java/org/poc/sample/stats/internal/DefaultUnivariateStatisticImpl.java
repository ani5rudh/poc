package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class DefaultUnivariateStatisticImpl implements UnivaritateStatistic {

    private static final DefaultUnivariateStatisticImpl INSTANCE = new DefaultUnivariateStatisticImpl();

    private DefaultUnivariateStatisticImpl() {}

    @Override
    public long getN() {
        return 0;
    }

    @Override
    public void merge(UnivaritateStatistic other) {}

    @Override
    public void accept(double value) {}

    @Override
    public double getAsDouble() {
        return Double.NaN;
    }

    public static DefaultUnivariateStatisticImpl getInstance() {
        return INSTANCE;
    }
}