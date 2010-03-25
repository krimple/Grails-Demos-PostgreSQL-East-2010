package org.playball.model.mapping

class Sale implements Serializable {

    BigDecimal salePrice
    int quantity
    int sale_id
  
    static belongsTo = [book: Book]

    static constraints = {
    }

    static mapping = {
      table 'sale'
      id composite: ['sale_id', 'book']
      version false      
      salePrice column: 'sale_price'
      
    }

    String toString() {
      "${sale_id}:${bookId}, ${quantity} @ ${salePrice}"
    }
}
