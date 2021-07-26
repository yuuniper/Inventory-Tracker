/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinderTest {

    @Test
    void searchTable_true_name() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "1234567890", "$3.88");

        assertTrue(test.searchTable("Biscuits", test1));
    }
    @Test
    void searchTable_true_lowerCase_name() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "1234567890", "$3.88");

        assertTrue(test.searchTable("biscuits", test1));
    }

    @Test
    void searchTable_true_half_Completed_name() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "1234567890", "$3.88");

        assertTrue(test.searchTable("Bis", test1));
    }

    @Test
    void searchTable_true_half_Completed_name_lowerCase() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "1234567890", "$3.88");

        assertTrue(test.searchTable("bis", test1));
    }

    @Test
    void searchTable_true_serialNumber() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertTrue(test.searchTable("abcdefgh78", test1));
    }

    @Test
    void searchTable_true_half_serialNumber() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertTrue(test.searchTable("abcdef", test1));
    }

    @Test
    void searchTable_true_serialNumber_uppercase() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertTrue(test.searchTable("ABCDEFGH", test1));
    }
    @Test
    void searchTable_true_money() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertTrue(test.searchTable("$3.88", test1));
    }

    @Test
    void searchTable_true_money_without_dollar_sign() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertTrue(test.searchTable("3.88", test1));
    }

    @Test
    void searchTable_false_money() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertFalse(test.searchTable("$4.88", test1));
    }

    @Test
    void searchTable_false_serial_number() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertFalse(test.searchTable("abcdegh", test1));
    }

    @Test
    void searchTable_false_serial_name() {
        Finder test = new Finder();

        Item test1 = new Item("Biscuits", "abcdefgh78", "$3.88");

        assertFalse(test.searchTable("BisL", test1));
    }
}