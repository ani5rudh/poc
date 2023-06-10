package org.poc.sample.stats.config;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;

public class SummaryStatisticsConfig {

    private final MeanConfig meanConfig;
    private final VarianceConfig varianceConfig;
    private final EnumSet<Statistic> statisticsToCompute = EnumSet.noneOf(Statistic.class);

    private final int hash;

    static final MeanConfig DEFAULT_MEAN_CONFIG = MeanConfig.Sum;
    static final VarianceConfig DEFAULT_VARIANCE_CONFIG = VarianceConfig.Sample;

    public static final SummaryStatisticsConfig DEFAULT_CONFIG =
            new SummaryStatisticsConfig(DEFAULT_MEAN_CONFIG, DEFAULT_VARIANCE_CONFIG, EnumSet.allOf(Statistic.class));

    private SummaryStatisticsConfig(final MeanConfig meanConfig,
                                    final VarianceConfig varianceConfig,
                                    final EnumSet<Statistic> statisticsToCompute) {
        this.meanConfig = meanConfig;
        this.varianceConfig = varianceConfig;
        this.statisticsToCompute.addAll(statisticsToCompute);
        this.hash = Objects.hash(meanConfig, varianceConfig, statisticsToCompute);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public MeanConfig getMeanConfig() {
        return this.meanConfig;
    }

    public VarianceConfig getVarianceConfig() {
        return this.varianceConfig;
    }

    public boolean shouldCompute(Statistic statistic) {
        return this.statisticsToCompute.contains(statistic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryStatisticsConfig that = (SummaryStatisticsConfig) o;
        return meanConfig == that.meanConfig && varianceConfig == that.varianceConfig && Objects.equals(statisticsToCompute, that.statisticsToCompute);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public static class Builder {
        private MeanConfig meanConfig = DEFAULT_MEAN_CONFIG;
        private VarianceConfig varianceConfig = DEFAULT_VARIANCE_CONFIG;
        private EnumSet<Statistic> statisticsToCompute = EnumSet.allOf(Statistic.class);

        private Builder() {
            // assign default impl here
        }

        public Builder withMeanConfig(final MeanConfig config) {
            this.meanConfig = meanConfig;
            return this;
        }

        public Builder withVarianceConfig(final VarianceConfig varianceConfig) {
            this.varianceConfig = varianceConfig;
            return this;
        }

        // TODO: Just for now. Planning to overload this to take 1, 2, 3, 4, 5, 6 params ?
        public Builder only(Statistic statisticToInclude, Statistic... statisticsToInclude) {
            this.statisticsToCompute.clear();
            this.statisticsToCompute.add(statisticToInclude);
            Collections.addAll(this.statisticsToCompute, statisticsToInclude);
            return this;
        }

        public Builder excluding(Statistic statisticToExclude, Statistic... statisticsToExclude) {
            this.statisticsToCompute.clear();
            EnumSet<Statistic> excludedStatistics = EnumSet.of(statisticToExclude);
            Collections.addAll(excludedStatistics, statisticsToExclude);
            this.statisticsToCompute = EnumSet.complementOf(excludedStatistics);
            return this;
        }

        public SummaryStatisticsConfig build() {
            return new SummaryStatisticsConfig(this.meanConfig, this.varianceConfig, this.statisticsToCompute);
        }
    }
}
