/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HomePageControllerTest {

    public static List<String> serialNumbersList = new ArrayList<>();
    static Item test1 = new Item("Biscuits", "1234567890", "$3.88");

    // Parameters for validate_if_input_is_true
    static Stream<Arguments> generateDataForCorrectInputs(){

        return Stream.of(
                Arguments.of("Oreos", "$3.88", "1234567890", serialNumbersList),
                Arguments.of("Li", "45.90873", "asx789jkq0", serialNumbersList)
        );
    }

    // Check Addition input - correct inputs
    @ParameterizedTest
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
    @MethodSource("generateDataForIncorrectInputs")
    void validateInput_is_false(String name, String money, String serialNumber, List<String> serialNumbersList) {
        HomePageController test = new HomePageController();
       serialNumbersList.add("heatwaves7");
        assertFalse(test.validateInput(name, money, serialNumber, serialNumbersList));
    }

    // Parameters for editNameTrue
    static Stream<Arguments> generateDataForCorrectEditNameInputs(){

        return Stream.of(
                Arguments.of("Lettuce", "$3.88", "1234567890", serialNumbersList, test1)
        );
    }
    @ParameterizedTest
    @MethodSource("generateDataForCorrectEditNameInputs")
    void editNameTrue(String nameEdited, String money, String serialNumber, List<String> serialNumbersList, Item item) {
        HomePageController test = new HomePageController();
        assertTrue(test.editName(nameEdited, item.getMoney(),
                item.getSerialNumber(), serialNumbersList, item));
    }

    // Parameters for editNameFalse
    static Stream<Arguments> generateDataForIncorrectEditNameInputs(){

        return Stream.of(
                Arguments.of("O", "$3.88", "1234567890", serialNumbersList, test1)
        );
    }
    @ParameterizedTest
    @MethodSource("generateDataForIncorrectEditNameInputs")
    void editNameFalse(String nameEdited, String money, String serialNumber, List<String> serialNumbersList, Item item) {
        HomePageController test = new HomePageController();
        assertFalse(test.editName(nameEdited, item.getMoney(),
                item.getSerialNumber(), serialNumbersList, item));
    }

    // Parameters for editSerialNumberTrue
    static Stream<Arguments> generateDataForCorrectEditSerialNumberInputs(){

        return Stream.of(
                Arguments.of("Oreos", "$3.88", "123456789j", serialNumbersList, test1, test1.getSerialNumber())
        );
    }
    @ParameterizedTest
    @MethodSource("generateDataForCorrectEditSerialNumberInputs")
    void editSerialNumberTrue(String name, String money, String serialNumberEdited, List<String> serialNumbersList,
                              Item item, String originalSerialNumber) {
        HomePageController test = new HomePageController();
        assertTrue(test.editSerialNumber(name, item.getMoney(), serialNumberEdited, serialNumbersList,
                item, originalSerialNumber));
    }

    // Parameters for editSerialNumberFalse
    static Stream<Arguments> generateDataForIncorrectEditSerialNumberInputs(){

        return Stream.of(
                Arguments.of("Oreos", "$3.88", "applestick!", serialNumbersList, test1, test1.getSerialNumber()),
                Arguments.of("Oreos", "$3.88", "appl@stic!", serialNumbersList, test1, test1.getSerialNumber()),
                Arguments.of("Oreos", "$3.88", "applestic", serialNumbersList, test1, test1.getSerialNumber())
                //Arguments.of("Oreos", "$3.88", "1234567890", serialNumbersList, test1, test1.getSerialNumber())

        );
    }
    @ParameterizedTest
    @MethodSource("generateDataForIncorrectEditSerialNumberInputs")
    void editSerialNumberFalse(String name, String money, String serialNumberEdited, List<String> serialNumbersList,
                               Item item,  String originalSerialNumber) {
        HomePageController test = new HomePageController();
        assertFalse(test.editSerialNumber(name, item.getMoney(),
                serialNumberEdited, serialNumbersList, item, originalSerialNumber));
    }
}