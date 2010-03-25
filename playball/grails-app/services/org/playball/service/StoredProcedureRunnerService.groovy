package org.playball.service

import groovy.sql.Sql
import org.springframework.jdbc.object.StoredProcedure
import javax.sql.DataSource
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.object.SqlFunction
import org.springframework.jdbc.core.JdbcTemplate

class StoredProcedureRunnerService {

    def dataSource
    boolean transactional = true

  // automatically set up by name (Spring injected) on startup
  def sumBookSalesByBookIdWithJdbcTemplate(def bookId) {
    def template = new JdbcTemplate(dataSource)
    template.queryForInt("select salesByBookId(${bookId})")
  }

  // use Spring JDBC's StoredProcedure class (see bottom of this class for definition)
  def sumBookSalesByBookIdWithSPSubclass(int bookId) {
    def spcaller = new StoredProcSubclass(dataSource)
    def map = spcaller.execute(bookId)
    // result is in entry '#result-set-1, with field 'result' which is a list, taking entry 0
    // confusing, no?
    map['#result-set-1']['result'][0]
  }

  def sumBookSalesByBookIdSqlFunction(int bookId) {
    def sqlFunction = new SqlFunction(dataSource, "select salesByBookId(${bookId})")
    sqlFunction.compile()
    def result
    try {
      result = sqlFunction.run()
    } catch (org.springframework.dao.TypeMismatchDataAccessException e) {
      log.error "Result type cannot be inferred - no result so returning 0"
      0
    }
    result
  }

  def sumBookSalesByBookIdGSQL(int bookId) {

    def gsql = new groovy.sql.Sql(dataSource)
    def results = gsql.rows("select salesByBookId(${bookId})")
    // result is in the 'sales by book id' result hash entry, as a list of fields,
    // but only the first field is satisfied (that's the value)
    results["salesbybookid"][0]
  }


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
}
