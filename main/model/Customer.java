package model;

// Represents a customer profile for the workout tracker, storing the user’s name and age.
public class Customer {

    private String name;
    private int age;

    // EFFECTS: initializes the various fields of the class
    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // EFFECTS: Returns the name of the customer
    public String getName() {
        return name;
    }

    // EFFECTS: Returns the age of the customer
    public int getUserAge() {
        return age;
    }
}
