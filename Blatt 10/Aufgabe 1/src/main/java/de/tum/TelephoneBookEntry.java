package de.tum;

/**
 * A simple Person
 * @author Hannes Dorfmann
 */
public class TelephoneBookEntry {

  private String name;
  private String number;

  public TelephoneBookEntry(String name, String number) {
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
