<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Stored Procedure Tester</title>
    </head>
    <body>
        <div class="body">
          <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
          </g:if>

          <h1>Stored Procedure tester</h1>

          <g:form method="post">
                <g:textField name="bookId" value="${bookId}" /> <br/>
                <g:actionSubmit class="submit" action="template" value="Template" />
                <g:actionSubmit class="submit" action="spsubclass" value="Subclass" />
                <g:actionSubmit class="submit" action="sqlfunction" value="SQL Function" />
                <g:actionSubmit class="submit" action="gsql" value="Groovy SQL" />

          </g:form>

          <g:if test="${result}">
            Result is: ${result}
          </g:if>
        </div>
    </body>
</html>
