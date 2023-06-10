import org.poc.sample.stats.DoubleSummaryStatistics;
import org.poc.sample.stats.StatisticalSummary;
import org.poc.sample.stats.config.Statistic;
import org.poc.sample.stats.config.SummaryStatisticsConfig;
import org.poc.sample.stats.config.VarianceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public final class DoubleSummaryStatisticsTest {

    private final double TOLERANCE = 1e-10;

    @Test
    public void testSummary() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, -1.0);
        StatisticalSummary summary = DoubleSummaryStatistics.withDefault()
                .of(data)
                .toStatisticalSummary();

        Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
        Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
        Assertions.assertEquals(3.7d, summary.getVariance(), TOLERANCE);
    }

    @Test
    public void testSummaryOnDoubleStream() {
        List<Double> data = Arrays.asList(3.0, 2.0, -1.0, 4.0, 1.0);
        {
            StatisticalSummary summary = data.parallelStream().collect(DoubleSummaryStatistics.summarize());

            Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
            Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
        }

        {
            StatisticalSummary summary = data.stream().collect(DoubleSummaryStatistics.summarize());

            Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
            Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
        }

    }

    @Test
    public void testSumOnly() {
        SummaryStatisticsConfig config = SummaryStatisticsConfig.newBuilder()
                .only(Statistic.Sum)
                .build();
        StatisticalSummary summary = DoubleSummaryStatistics.withCustom(config)
                .addValue(2.0)
                .addValue(4.0)
                .addValue(-1.0)
                .addValue(3.0)
                .addValue(1.0)
                .toStatisticalSummary();

        Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
        // We are only computing Sum
        Assertions.assertEquals(Double.NaN, summary.getVariance());
    }

    @Test
    public void testExcludeSum() {
        SummaryStatisticsConfig config = SummaryStatisticsConfig.newBuilder()
                .excluding(Statistic.Sum)
                .build();
        StatisticalSummary summary = DoubleSummaryStatistics.withCustom(config)
                .addValue(3.0)
                .addValue(-1.0)
                .addValue(2.0)
                .addValue(1.0)
                .addValue(4.0)
                .toStatisticalSummary();

        Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
        Assertions.assertEquals(3.7d, summary.getVariance(), TOLERANCE);
        Assertions.assertEquals(Double.NaN, summary.getSum()); // Sum is excluded
    }

    @Test
    public void testMeanOnly() {
        SummaryStatisticsConfig config = SummaryStatisticsConfig.newBuilder()
                .only(Statistic.Mean)
                .build();
        StatisticalSummary summary = DoubleSummaryStatistics.withCustom(config)
                .of(Arrays.asList(-1.0, 1.0, 2.0, 3.0, 4.0))
                .toStatisticalSummary();

        Assertions.assertEquals(Double.NaN, summary.getSum());
        Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
    }

    @Test
    public void testSumAndMeanOnDoubleStream() {
        List<Double> data = Arrays.asList(4.0, -1.0, 1.0, 3.0, 2.0);

        SummaryStatisticsConfig config = SummaryStatisticsConfig.newBuilder()
                .only(Statistic.Mean, Statistic.Sum)
                .build();
        {
            StatisticalSummary summary = data.stream().collect(DoubleSummaryStatistics.summarize(config));
            Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
            Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
            Assertions.assertEquals(Double.NaN, summary.getVariance());
        }

        {
            StatisticalSummary summary = data.parallelStream().collect(DoubleSummaryStatistics.summarize(config));
            Assertions.assertEquals(9.0d, summary.getSum(), TOLERANCE);
            Assertions.assertEquals(1.8d, summary.getMean(), TOLERANCE);
            Assertions.assertEquals(Double.NaN, summary.getVariance());
        }
    }

    @Test
    public void testPopulationVariance() {
        SummaryStatisticsConfig config = SummaryStatisticsConfig.newBuilder()
                .only(Statistic.Variance)
                .withVarianceConfig(VarianceConfig.Population)
                .build();
        StatisticalSummary summary = DoubleSummaryStatistics.withCustom(config)
                .addValue(1.0)
                .addValue(-100.333333333333)
                .addValue(3.14)
                .addValue(5.6666667)
                .addValue(-7.2)
                .toStatisticalSummary();

        Assertions.assertEquals(Double.NaN, summary.getSum());
        Assertions.assertEquals(Double.NaN, summary.getMean());
        Assertions.assertEquals(1650.2976, summary.getVariance(), 1e-4);
    }
}
