package org.pqkkkkk.my_day_server.data_builder;

import org.pqkkkkk.my_day_server.task.entity.Step;

public class StepTestDataBuilder {
    public static Step createValidStep() {
        return Step.builder()
            .stepTitle("Valid Step")
            .build();
    }
}
