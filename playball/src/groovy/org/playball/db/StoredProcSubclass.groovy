package org.playball.db

import org.springframework.jdbc.core.SqlParameter
import javax.sql.DataSource
import org.springframework.jdbc.object.StoredProcedure

class StoredProcSubclass extends StoredProcedure {
    public StoredProcSubclass(DataSource ds) {
      super(ds, "salesByBookId")
      setFunction true
      declareParameter(new SqlParameter("book_id", java.sql.Types.INTEGER))
      compile()
    }

    public Map execute(Integer bookId) {
      Map inputs = new HashMap()
      inputs.put("book_id", bookId)
      return super.execute(inputs)
    }
}
