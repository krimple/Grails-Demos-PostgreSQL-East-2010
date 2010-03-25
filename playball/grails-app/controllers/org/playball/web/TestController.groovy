package org.playball.web

class TestController {

    def storedProcedureRunnerService

    def index = {
      render(view: "index", model: [bookId: params.bookId != null ? params.bookId : 0])
    }

    def template = {
      def safeBookId = parseInt(params.bookId)

      def model = [result:
                    storedProcedureRunnerService.sumBookSalesByBookIdWithJdbcTemplate(safeBookId),
                    bookId: safeBookId]
      render(view: "index", model: model)
    }

    def spsubclass = {
      def safeBookId = parseInt(params.bookId)

      def model = [result:
                    storedProcedureRunnerService.sumBookSalesByBookIdWithSPSubclass(safeBookId),
                    bookId: safeBookId]
      render(view: "index", model: model)
    }

    def sqlfunction = {
      def safeBookId = parseInt(params.bookId)

      def model = [result:
                    storedProcedureRunnerService.sumBookSalesByBookIdSqlFunction(safeBookId),
                    bookId: safeBookId]
      render(view: "index", model: model)
    }

    def gsql = {
      def safeBookId = parseInt(params.bookId)

      def model = [result:
                    storedProcedureRunnerService.sumBookSalesByBookIdGSQL(safeBookId),
                    bookId: safeBookId]
      render(view: "index", model: model)
    }

    def parseInt(def val) {
      if (val == null) 0
      def safeVal = 0
      try {
        safeVal = Integer.parseInt(val)
      } catch (Exception e) {
        // throw away all invalid values...
      }
      safeVal
    }
}
