package org.playball.model.mapping

class Author {

    String firstName
    String lastName

    static hasMany = [collaborations: Collaboration]
    static mapping = {
      table 'author'
      id column: 'author_id'
      lastName column: 'lastname'
      firstName column: 'firstname'
    }

  
}
