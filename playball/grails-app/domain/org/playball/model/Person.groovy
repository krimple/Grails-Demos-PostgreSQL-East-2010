package org.playball.model

class Person {

    static constraints = {
      firstName(blank:false)
      lastName(blank:false)
      dateOfBirth(max: new Date())
    }

    String firstName
    String lastName
    Date dateOfBirth
}
