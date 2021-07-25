/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HomePageControllerTest {

    public static List<String> serialNumbersList = new ArrayList<String>();

    // Parameters for validate_if_input_is_true
    static Stream<Arguments> generateDataForCorrectInputs(){

        return Stream.of(
                Arguments.of("Oreos", "$3.88", "1234567890", serialNumbersList),
                Arguments.of("Li", "45.90873", "asx789jkq0", serialNumbersList)
        );
    }

    // Check Addition input - correct inputs
    @ParameterizedTest
    //@CsvSource({"Oreos, $3.88, 1234567890, serialNumbersList", "Li, 45.90873, asx789jkq0, serialNumbersList"})
    @MethodSource("generateDataForCorrectInputs")
    void validateInput_is_true(String name, String money, String serialNumber, List<String> serialNumbersList) {
        HomePageController test = new HomePageController();
        assertTrue(test.validateInput(name, money, serialNumber, serialNumbersList));
    }

   // Parameters for validate_if_input_is_false
    static Stream<Arguments> generateDataForIncorrectInputs(){

        return Stream.of(
                Arguments.of("Oreos", "$3.88.00", "1234567890", serialNumbersList),
                Arguments.of("Li", "haha", "asx789jkq0", serialNumbersList),
                Arguments.of("R", "$45.00", "asx789jkq0", serialNumbersList),
                Arguments.of("Lan", "46", "asx789jkq!", serialNumbersList),
                Arguments.of("Li", "$48.098", "asx789j", serialNumbersList),
                Arguments.of("Lucky", "$45.90", "heatwaves7", serialNumbersList)
        );
    }

    // Check Addition input - incorrect inputs
    @ParameterizedTest
    //@CsvSource({"Oreos, $3.88.00, 1234567890", "Li, haha, asx789jkq0", "R, $45.00, asx789jkq0", "Lan, 46, asx@89jkq!", "Li, $48.098, asx789j", "Lucky, heatwaves7, $45.90"})
    @MethodSource("generateDataForIncorrectInputs")
    void validateInput_is_false(String name, String money, String serialNumber, List<String> serialNumbersList) {
        HomePageController test = new HomePageController();
        //serialNumbersList.add(duplicate);
       serialNumbersList.add("heatwaves7");
        assertFalse(test.validateInput(name, money, serialNumber, serialNumbersList));
    }
}