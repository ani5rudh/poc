package org.poc.sample.stats.internal;

import org.poc.sample.stats.UnivaritateStatistic;
import org.poc.sample.stats.config.Statistic;
import org.poc.sample.stats.config.SummaryStatisticsConfig;

public class StatisticImplProvider {

    private final UnivaritateStatistic sumImpl;
    private final UnivaritateStatistic meanImpl;
    private final UnivaritateStatistic varianceImpl;

    private final boolean shouldComputeMean;
    private final boolean shouldComputeVariance;

    private FirstMoment firstMoment;
    private SecondMoment secondMoment;

    private StatisticImplProvider(SummaryStatisticsConfig config) {

        boolean shouldComputeSum = config.shouldCompute(Statistic.Sum);
        shouldComputeMean = config.shouldCompute(Statistic.Mean);
        shouldComputeVariance = config.shouldCompute(Statistic.Variance);

        boolean useMomentBasedMeanImpl = useMomentBasedMeanImpl(config);
        boolean useMomentBasedVarianceImpl = useMomentBasedVarianceImpl(config);

        if (shouldComputeSum) {
            sumImpl = new SumImpl();
        } else {
            sumImpl = DefaultUnivariateStatisticImpl.getInstance();
        }

        boolean firstMomentInitialized = false;
        if (useMomentBasedMeanImpl) {
            initializeFirstMoment();
            firstMomentInitialized = true;
            meanImpl = new MomentBasedMean(firstMoment);
        } else {
            meanImpl = DefaultUnivariateStatisticImpl.getInstance();
        }

        if (useMomentBasedVarianceImpl) {
            if (!firstMomentInitialized) {
                initializeFirstMoment();
                initializeSecondMoment(true);
            } else {
                initializeSecondMoment(false);
            }

            if (config.getVarianceConfig().isSampleVariance()) {
                varianceImpl = new MomentBasedSampleVariance(secondMoment);
            } else if (config.getVarianceConfig().isPopulationVariance()) {
                varianceImpl = new MomentBasedPopulationVariance(secondMoment);
            } else {
                varianceImpl = DefaultUnivariateStatisticImpl.getInstance();
            }
        } else {
            varianceImpl = DefaultUnivariateStatisticImpl.getInstance();
        }
    }

    public static StatisticImplProvider initConfig(SummaryStatisticsConfig config) {
        return new StatisticImplProvider(config);
    }

    public UnivaritateStatistic getSumImpl() {
        return sumImpl;
    }

    public UnivaritateStatistic getMeanImpl() {
        return meanImpl;
    }

    public UnivaritateStatistic getVarianceImpl() {
        return varianceImpl;
    }

    private boolean useMomentBasedMeanImpl(SummaryStatisticsConfig config) {
        return shouldComputeMean && config.getMeanConfig().isSum();
    }

    private boolean useMomentBasedVarianceImpl(SummaryStatisticsConfig config) {
        return shouldComputeVariance && (config.getVarianceConfig().isSampleVariance()
                || config.getVarianceConfig().isPopulationVariance());
    }

    private void initializeFirstMoment() {
        firstMoment = new FirstMoment();
    }

    private void initializeSecondMoment(final boolean updateFirstMoment) {
        secondMoment = new SecondMoment(firstMoment, updateFirstMoment);
    }
}
