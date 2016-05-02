<%@include file="include.jsp" %>

<html>
<head>
    <title>DexChat</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="/js/login.js"></script>

</head>
<body>

<form action="/registration" method="post" name="form" id="form">

    <label for="login">Enter login</label>
    <input type="text" name="login" placeholder="login" id="login" title="Enter your login" /></br></br>

    <label for="pass">Enter pass</label>
    <input type="password" name="pass" placeholder="pass" id="pass" /></br></br>

    <input type="button" name="Sign in" value="Sign in" onclick="return false" id="Sign_in"/>

    <input type="submit" name="Sign up" value="Sign up" id="Sign_up"/>
</form>


</body>
</html>
