package org.playball.model.mapping

class Collaboration implements Serializable {

    static belongsTo = [author: Author, book: Book]

    static mapping = {
      table 'bookauthors'
      id composite: ['author', 'book']
      author column: 'author_id'
      book column: 'book_id'
    }
}
