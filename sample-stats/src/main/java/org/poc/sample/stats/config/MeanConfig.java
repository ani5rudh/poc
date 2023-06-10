package org.poc.sample.stats.config;

public enum MeanConfig {
    Sum,
    Rolling,
    ;

    public boolean isSum() {
        return this == Sum;
    }

    public boolean isRolling() {
        return this == Rolling;
    }
}
