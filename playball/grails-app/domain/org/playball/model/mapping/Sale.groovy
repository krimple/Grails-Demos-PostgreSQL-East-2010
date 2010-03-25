package org.playball.model.mapping

class Sale {

    int saleId
    int bookId
    BigDecimal salePrice
    int quantity


    static constraints = {
    }

    static mapping = {
      table 'sale'

      id composite: ['sale_id', 'book_id']
      saleId column:'sale_id'
      bookId column: 'book_id'
      salePrice column: 'sale_price'
      
    }
}
