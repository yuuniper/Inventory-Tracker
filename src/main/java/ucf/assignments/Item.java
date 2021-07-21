/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import javafx.beans.property.SimpleStringProperty;

public class Item {


        private final SimpleStringProperty name;
        private final SimpleStringProperty serialNumber;
        private final SimpleStringProperty money;


        public Item(String name, String serialNumber, String money) {
            this.name = new SimpleStringProperty(name);
            this.serialNumber = new SimpleStringProperty(serialNumber);
            this.money = new SimpleStringProperty(money);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String nameInput) {
            name.set(nameInput);
        }

        public String getSerialNumber() {
            return serialNumber.get();
        }

        public void setSerialNumber(String serialNumberInput) {
            serialNumber.set(serialNumberInput);
        }

        public String getMoney() {
            return money.get();
        }

        public void setMoney(String moneyInput) {
            money.set(moneyInput);
        }
    }

