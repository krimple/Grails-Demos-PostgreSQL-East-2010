package org.playball.service

import org.springframework.jdbc.object.SqlFunction
import org.springframework.jdbc.core.JdbcTemplate
import org.playball.db.StoredProcSubclass
import javax.annotation.PostConstruct
import java.sql.Types


class StoredProcedureRunnerService {

  boolean transactional = true
    
  def dataSource
  def template
  def spcaller
  def sqlFunction


  @PostConstruct 
  def init() {
      int [] params = new int[1];
      params[0]=java.sql.Types.INTEGER;

      template = new JdbcTemplate(dataSource)
      spcaller = new StoredProcSubclass(dataSource)
      sqlFunction = new SqlFunction(dataSource,
                      "select salesByBookId(?)", params)
      sqlFunction.compile()

  }

  // automatically set up by name (Spring injected) on startup
  def sumBookSalesByBookIdWithJdbcTemplate(def bookId) {
      template.queryForInt("select salesByBookId(?)", bookId)
  }

  // use Spring JDBC's StoredProcedure class (see bottom of this class for definition)
  def sumBookSalesByBookIdWithSPSubclass(int bookId) {
      def map = spcaller.execute(bookId, bookId)
      map['#result-set-1']['result'][0]
  }

  def sumBookSalesByBookIdSqlFunction(int bookId) {

      def result
      try {
        result = sqlFunction.run(bookId)
      } catch (org.springframework.dao.TypeMismatchDataAccessException e) {
        log.error "Result type cannot be inferred - no result so returning 0"
        0
      }
      result
  }

  def sumBookSalesByBookIdGSQL(int bookId) {
      def gsql = new groovy.sql.Sql(dataSource)
      def results = gsql.rows("select salesByBookId(${bookId})")
      results["salesbybookid"][0]
  }
}
