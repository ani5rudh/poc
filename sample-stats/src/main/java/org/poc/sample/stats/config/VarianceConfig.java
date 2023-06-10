package org.poc.sample.stats.config;

public enum VarianceConfig {
    Population,
    Sample,
    ;

    public boolean isSampleVariance() {
        return this == Sample;
    }

    public boolean isPopulationVariance() {
        return this == Population;
    }
}
