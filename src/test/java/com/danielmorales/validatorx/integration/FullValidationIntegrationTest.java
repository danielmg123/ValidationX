package com.danielmorales.validatorx.integration;

import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import com.danielmorales.validatorx.pipeline.DefaultValidationPipeline;
import com.danielmorales.validatorx.pipeline.ValidationPipeline;
import com.danielmorales.validatorx.core.ValidationProfileRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates a more complete (integration-like) scenario 
 * that uses annotations, a validation pipeline, 
 * and custom rule sets from the registry.
 */
class FullValidationIntegrationTest {

    static class Address {
        @NotNull(message = "Street cannot be null.")
        String street;

        @NotNull(message = "City cannot be null.")
        String city;

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }
    }

    static class Customer {
        @NotNull(message = "Customer name is required.")
        String name;

        @NotNull(message = "Must have an address.")
        Address address;

        public Customer(String name, Address address) {
            this.name = name;
            this.address = address;
        }
    }

    @BeforeAll
    static void setupProfile() {
        // Register an additional rule set that is separate from annotations
        // For instance, "nonEmptyName" that ensures name length > 0
        ValidationProfileRegistry.registerProfile("nonEmptyName", builder ->
            builder.isNotNull("name", "Name must not be null!")
                   .hasLengthBetween("name", 1, 100, "Name cannot be empty")
        );
    }

    @Test
    void testFullValidation_withValidData() {
        // Valid objects
        Address address = new Address("123 Main St", "Albuquerque");
        Customer customer = new Customer("John Doe", address);

        // 1) Create a pipeline using the "nonEmptyName" ruleSet
        final boolean[] successCalled = { false };
        ValidationPipeline<Customer> pipeline =
                new DefaultValidationPipeline<Customer>()
                        .validateRequest(customer)
                        .withRuleSet("nonEmptyName")
                        .onFailure(result -> fail("Should not fail for valid data."))
                        .onSuccess(() -> successCalled[0] = true);

        // 2) Execute the pipeline
        pipeline.execute();

        // 3) We expect success
        assertTrue(successCalled[0], "Success callback should be triggered");
    }

    @Test
    void testFullValidation_withInvalidData() {
        // Create objects that fail both annotation-based checks and custom profile checks
        Address address = new Address(null, null);  // fails @NotNull
        Customer customer = new Customer("", address);

        final ValidationResult[] captured = new ValidationResult[1];
        ValidationPipeline<Customer> pipeline =
                new DefaultValidationPipeline<Customer>()
                        .validateRequest(customer)
                        .withRuleSet("nonEmptyName")
                        .onFailure(result -> captured[0] = result)
                        .onSuccess(() -> fail("Should not succeed with invalid data."));

        pipeline.execute();

        assertNotNull(captured[0], "Failure callback should be invoked");
        assertTrue(captured[0].hasErrors(), "Should have multiple annotation-based errors");
        // Check how many or which fields are failing:
        // - Customer.name is ""
        // - Address.street is null
        // - Address.city is null
        assertTrue(captured[0].getErrors().size() >= 3,
                "Expected at least 3 errors (name empty, street null, city null)");
    }

    @Test
    void testDeeplyNestedValidation() {
        class Country {
            @NotNull(message = "Country name cannot be null.")
            String name;
    
            Country(String name) {
                this.name = name;
            }
        }
    
        class NestedAddress { 
            @NotNull(message = "Street cannot be null.")
            String street;
    
            @NotNull(message = "City cannot be null.")
            String city;
    
            @NotNull(message = "Country cannot be null.")
            Country country;
    
            NestedAddress(String street, String city, Country country) {
                this.street = street;
                this.city = city;
                this.country = country;
            }
        }
    
        class NestedCustomer { 
            @NotNull(message = "Customer name is required.")
            String name;
    
            @NotNull(message = "Must have an address.")
            NestedAddress address;
    
            NestedCustomer(String name, NestedAddress address) {
                this.name = name;
                this.address = address;
            }
        }
    
        Country country = new Country(null);
        NestedAddress address = new NestedAddress("123 Main St", "Albuquerque", country);
        NestedCustomer customer = new NestedCustomer("John Doe", address);
    
        ValidationResult result = Validator.check(customer).validate();
        assertTrue(result.hasErrors(), "Should detect missing country name");
        assertEquals(1, result.getErrors().size());
        assertEquals("Country name cannot be null.", result.getErrors().get(0).getMessage());
    }
    

    @Test
    void testWithoutPipeline_justAnnotations() {
        // An alternative "integration" check using only annotation-based validation
        Address address = new Address(null, "SomeCity");
        Customer customer = new Customer(null, address);

        ValidationResult result = Validator.check(customer).validate();
        assertTrue(result.hasErrors(), "Should fail because name is null and address.street is null");
    }
}