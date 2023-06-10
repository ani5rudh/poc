package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;

class FirstMoment implements UnivaritateStatistic {
    private long n;
    private double m1;
    private double dev;
    private double nDev;

    FirstMoment() {
        this(0, 0, 0, 0);
    }

    private FirstMoment(final long n, final double m1, final double dev, final double nDev) {
        this.n = n;
        this.m1 = m1;
        this.dev = dev;
        this.nDev = nDev;
    }

    @Override
    public long getN() {
        return n;
    }

    @Override
    public void merge(UnivaritateStatistic other) {
        FirstMoment otherFirstMoment = (FirstMoment) other;
        long otherN = otherFirstMoment.n;
        double otherDev = otherFirstMoment.dev;
        double othernDev = otherFirstMoment.nDev;
        double otherM1 = otherFirstMoment.m1;

        if (otherN == 0) {
            return; // Nothing to merge
        }

        if (this.n == 0) {
            this.n = otherN;
            this.m1 = otherM1;
            this.dev = otherDev;
            this.nDev = othernDev;
        } else {
            this.n += otherN;

            double n0 = n;
            this.dev = otherM1 - m1;
            this.nDev = otherN * dev / n0;
            this.m1 += nDev;
        }
    }

    @Override
    public void accept(double value) {
        n++;
        double n0 = n;
        dev = value - m1;
        nDev = dev / n0;
        m1 += nDev;
    }

    @Override
    public double getAsDouble() {
        return m1;
    }

    double getDev() {
        return dev;
    }

    double getnDev() {
        return nDev;
    }
}
