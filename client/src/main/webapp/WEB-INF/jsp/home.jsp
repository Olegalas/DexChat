<%@include file="include.jsp" %>
<jsp:include page="web_socket.jsp" />

<html>
<head>
    <title>DexChat</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="/js/home.js"></script>
</head>
<body>
    <h1>Home <c:out value="${login}"/></h1>
    <span id="login"><c:out value="${login}"/></span>
    <span id="pass"><c:out value="${pass}"/></span>
</body>
</html>
