<%@include file="include.jsp" %>
<jsp:include page="web_socket.jsp" />

<html>
<head>
    <title>Registration</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="/js/registration.js"></script>
</head>
<body>

<form action="" method="POST" name="test" id="form">

    <label for="login">Enter login</label>
    <input type="text" name="login" placeholder="login" id="login" title="Enter your login" /></br></br>

    <label for="name">Enter name</label>
    <input type="text" name="name" placeholder="name" id="name" title="Enter your name" /></br></br>

    <label for="email">Enter email</label>
    <input type="text" name="email" placeholder="email" id="email" title="Enter your email" /></br></br>

    <label for="pass">Enter pass</label>
    <input type="password" name="pass" placeholder="pass" id="pass" /></br></br>

    <label for="rePass">Repeat pass</label>
    <input type="password" name="rePass" placeholder="Pass" id="rePass" /></br></br>

    <input type="button" name="Ready" value="Ready" onclick="return false" id="Ready"/>
</form>

</body>
</html>
