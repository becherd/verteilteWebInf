package de.tum;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * The main class to start the java app
 *
 * @author Hannes Dorfmann
 */
public class Main {

  public static void main(String[] args) {

    System.out.println("Hello World!");

    int count = 10000000;

    Person[] persons = new Person[count];

    // Generate random persons
    for (int i = 0; i < count; i++) {
      persons[i] = new Person("Person" + i, RandomStringUtils.randomNumeric(10));
    }

    // test java map

  }


  private void testJavaMap(Person [] persons){

  }
}
