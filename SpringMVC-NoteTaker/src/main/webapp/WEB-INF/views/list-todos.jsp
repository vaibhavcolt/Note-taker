<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

        <div class = "container">
        <table class = "table table-striped">
            <caption></caption>

            <thread>
                <tr>
                    <th>Description</th>
                    <th>Date Created</th>
                    <th>Is Completed?</th>
                    <th></th>
                </tr>
            </thread>

            <tbody>
                <c:forEach items = "${todos}" var="todo">
                    <tr>
                        <td>${todo.desc}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy"
                                            value="${todo.targetDate}"/></td>
                        <td>${todo.done}</td>
                        <td>
                            <a href = "/completed-todo?id=${todo.id}" class = "btn btn-success"}>Completed</a>
                            <a href= "/update-todo?id=${todo.id}" class= "btn btn-info">Update</a>
                            <a href= "/delete-todo?id=${todo.id}" class= "btn btn-danger">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div>
            <a class= "btn btn-success" href="/add-todo">Add</a>
        </div>
    </div>

<%@ include file="common/footer.jspf" %>
