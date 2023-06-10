package org.poc.sample.stats;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public interface UnivaritateStatistic extends DoubleConsumer, DoubleSupplier {
    long getN();
    void merge(UnivaritateStatistic other);
}
