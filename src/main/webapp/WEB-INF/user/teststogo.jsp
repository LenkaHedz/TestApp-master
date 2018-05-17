<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/util/head.jsp" %>

<html>
<body>
<%@ include file="/util/nav.jsp" %>

<div class="container-fluid">
    <div class="row content">

        <%@ include file="/util/sidebar.jsp" %>

            <div class="container text-center">
                <div class="row">
                    <div class="col-lg-6">

                        <i><fmt:message key="pasttest.count"/> "${requestScope.allTestsCount}"</i>
                        <br>
                        <br>

                        <ul class="list-group">
                            <c:forEach var="test" items="${requestScope.testList}">
                                <li class="list-group-item">
                                    <button class="btn btn-info" data-toggle="collapse" data-target="#${test.id}">
                                        <c:out value="${test.category}"/> :  <c:out value="${test.name}"/>
                                    </button>

                                    <div id="${test.id}" class="collapse">
                                       <p>
                                          <form method="GET" action="${pageContext.request.contextPath}/gotest">
                                             <fmt:message key="pasttest.category"/> <c:out value="${test.category}"/>
                                             <br>
                                             <fmt:message key="pasttest.name"/> <c:out value="${test.name}"/>
                                             <br>
                                             <fmt:message key="pasttest.description"/> <c:out value="${test.description}"/>
                                             <br>
                                             <button class="btn btn-info" type="submit"> <fmt:message key="pasttest.pastTest"/></button>
                                          </form>
                                       </p>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
            </div>
        </div>
</div>

    <%@ include file="/util/footer.jsp" %>
</body>
</html>