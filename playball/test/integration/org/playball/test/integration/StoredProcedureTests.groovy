package org.playball.test.integration

import grails.test.*

class StoredProcedureTests extends GrailsUnitTestCase {

    // automatically injected
    def storedProcedureRunnerService
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    public testSumBookSalesByBookIdWithJdbcTemplate() {
      def result = storedProcedureRunnerService.sumBookSalesByBookIdWithJdbcTemplate(80)
      assertNotNull result
    }

}
