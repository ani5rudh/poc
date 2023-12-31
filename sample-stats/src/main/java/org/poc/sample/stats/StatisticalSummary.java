package org.poc.sample.stats;

public interface StatisticalSummary {

    double getMean();

    double getVariance();

    double getStandardDeviation();

    double getMax();

    double getMin();

    long getN();

    double getSum();
}
