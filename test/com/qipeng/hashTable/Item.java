package com.qipeng.hashTable;

public class Item {

    private int value;

    public Item(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value / 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null || obj.getClass() != getClass())) {
            return false;
        }

        return ((Item) obj).value == value;
    }

    @Override
    public String toString() {
        return "Item{" + "value=" + value + '}';
    }
}
