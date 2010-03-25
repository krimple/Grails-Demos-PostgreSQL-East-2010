package org.playball.model.mapping

class Book {

    String title
    BigDecimal price

    static hasMany = [collaborations: Collaboration]

    static mapping = {
      table 'book'
      id column: 'book_id'
      collaborations column: 'book_id'
    }
}
