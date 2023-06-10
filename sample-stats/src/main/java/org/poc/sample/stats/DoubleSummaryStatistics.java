package org.poc.sample.stats;

import org.poc.sample.stats.config.SummaryStatisticsConfig;
import org.poc.sample.stats.internal.StatisticImplProvider;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DoubleSummaryStatistics {

    private final SummaryStatisticsConfig config;

    private long n;

    // sum
    UnivaritateStatistic sumImpl;

    // mean
    UnivaritateStatistic meanImpl;

    // variance
    UnivaritateStatistic varianceImpl;

    private DoubleSummaryStatistics(final SummaryStatisticsConfig config) {
        this.config = config;
        StatisticImplProvider implProvider = StatisticImplProvider.initConfig(config);

        n = 0;
        sumImpl = implProvider.getSumImpl();
        meanImpl = implProvider.getMeanImpl();
        varianceImpl = implProvider.getVarianceImpl();
    }

    public static DoubleSummaryStatistics withCustom(SummaryStatisticsConfig customConfig) {
        return new DoubleSummaryStatistics(customConfig);
    }

    public static DoubleSummaryStatistics withDefault() {
        return new DoubleSummaryStatistics(SummaryStatisticsConfig.DEFAULT_CONFIG);
    }

    public static Collector<Double, DoubleSummaryStatistics, StatisticalSummary> summarize() {
        return summarize(DoubleSummaryStatistics::withDefault);
    }

    public static Collector<Double, DoubleSummaryStatistics, StatisticalSummary> summarize(SummaryStatisticsConfig config) {
        return summarize(() -> DoubleSummaryStatistics.withCustom(config));
    }

    public DoubleSummaryStatistics of(Collection<Double> values) {
        values.forEach(this::addValue);
        return this;
    }

    public StatisticalSummary toStatisticalSummary() {
        return new Result(n, sumImpl.getAsDouble(), meanImpl.getAsDouble(), varianceImpl.getAsDouble());
    }

    public DoubleSummaryStatistics addValue(double d) {
        n++;
        sumImpl.accept(d);
        meanImpl.accept(d);
        varianceImpl.accept(d);

        return this;
    }

    private static Collector<Double, DoubleSummaryStatistics, StatisticalSummary> summarize(Supplier<DoubleSummaryStatistics> supplier) {
        return Collector.of(
                supplier,
                DoubleSummaryStatistics::addValue,
                DoubleSummaryStatistics::merge,
                DoubleSummaryStatistics::toStatisticalSummary,
                Collector.Characteristics.UNORDERED);
    }

    private DoubleSummaryStatistics merge(DoubleSummaryStatistics other) {
        // This is only called from the context of SummaryStatistic#summarize where *we control* the SummaryStatistic object creation logic
        // So it is safe to assume that each of these merge functions are type safe since both the partial SummaryStatistics
        // being merged here are created with the same config which means they use the same implementations to compute StatisticalSummary
        // So the hashCode check below is redundant
//        if (this.config.hashCode() != other.config.hashCode()) {
//            throw new IllegalArgumentException("Cannot merge SummaryStatistic instances with different SummaryStatisticsConfig");
//        }

        sumImpl.merge(other.sumImpl);
        meanImpl.merge(other.meanImpl);
        varianceImpl.merge(other.varianceImpl);

        return this;
    }

    static class Result implements StatisticalSummary {

        private long n;

        // running sum
        private double sum;

        // mean
        private double mean;

        // variance
        private double variance;

        private Result(final long n,
                       final double sum,
                       final double mean,
                       final double variance) {
            this.n = n;
            this.sum = sum;
            this.mean = mean;
            this.variance = variance;
        }

        @Override
        public double getMean() {
            return mean;
        }

        @Override
        public double getVariance() {
            return variance;
        }

        @Override
        public double getStandardDeviation() {
            return 0;
        }

        @Override
        public double getMax() {
            return 0;
        }

        @Override
        public double getMin() {
            return 0;
        }

        @Override
        public long getN() {
            return n;
        }

        @Override
        public double getSum() {
            return sum;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Sum = ").append(sum).append("\n");
            sb.append("Mean = ").append(mean).append("\n");
            return sb.toString();
        }
    }
}
