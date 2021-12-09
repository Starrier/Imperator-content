package org.starrier.imperator.content.component;


import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.starrier.imperator.content.component.constant.ProfileConstant.ONE;

/**
 * @author Starrier
 * @date 2019/05/13
 */
@Slf4j
@Component
public class PassCaseMetric {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private Gauge passCaseGauge = Gauge.builder("pass.cases.gauge", atomicInteger, AtomicInteger::get)
            .tag("service", "demo")
            .description("pass cases gauge of demo")
            .register(new SimpleMeterRegistry());
    private AtomicInteger passCases = Metrics.gauge("pass.cases.gauge.value", init(), atomicInteger);

    private List<Tag> init() {
        ArrayList<Tag> list = Lists.newArrayListWithExpectedSize(ONE);
        list.add(new ImmutableTag("service", "demo"));
        return list;
    }

    public void handleMetrics() {
        if (0 == (System.currentTimeMillis() % 2)) {
            passCases.addAndGet(100);
            log.info("ADD + " + passCaseGauge.measure() + " : " + passCases);
        } else {
            if (passCases.addAndGet(-100) < 0) {
                passCases.set(1);
            }
            log.info("DECR - " + passCaseGauge.measure() + " : " + passCases);
        }
    }

}