package org.example.expert.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.generate-test-data", havingValue = "true")
public class TestDataGeneratorRunner implements CommandLineRunner {
    private final TestDataGenerator testDataGenerator;

    public TestDataGeneratorRunner(TestDataGenerator testDataGenerator) {
        this.testDataGenerator = testDataGenerator;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Generating test data...");
        testDataGenerator.generateUsers(1000000);
        System.out.println("Test data generation completed.");
    }
}
