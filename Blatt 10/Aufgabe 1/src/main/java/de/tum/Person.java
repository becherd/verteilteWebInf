package de.tum;

/**
 * A simple Person
 * @author Hannes Dorfmann
 */
public class Person {

  private String name;
  private String number;

  public Person(String name, String number) {
    this.name = name;
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }
}
