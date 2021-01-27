package com.qipeng.hashTable;

import java.util.Objects;

public class Person implements Comparable<Person> {
    private int age;
    private float height;
    private String name;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    @Override
    public int hashCode() {
        // 所有成员变量都参与运算
        int hashCode = Integer.hashCode(age);
        hashCode = hashCode * 31 + Float.hashCode(height);
        hashCode = hashCode * 31 + (name != null ? name.hashCode() : 0);

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Person person = (Person)obj;

        return person.age == age
                && person.height == height
                // (person.name == null ? name == null : person.name.equals(name))
                && (Objects.equals(person.name, name));
    }

    @Override
    public int compareTo(Person o) {
        return this.age - o.age;
    }
}
