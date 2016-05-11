<%@include file="include.jsp" %>

<html>
<head>

    <title>DexChat</title>
    <script type="text/javascript" src="/js/login.js"></script>

</head>
<body>
<div class="container">
    <form action="/registration" method="POST" name="form" id="form" class="form-horizontal" role="form">

        <h2>DexChat</h2>
        <p>Welcome to Dex world</p>

        <div class="form-group">
            <label for="login" class="control-label col-sm-2">Enter login : </label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="login" placeholder="login" id="login"
                       title="Enter your login"/>
            </div>
        </div>

        <div class="form-group">
            <label for="pass" class="control-label col-sm-2">Enter pass : </label>
            <div class="col-sm-10">
                <input class="form-control" type="password" name="pass" placeholder="pass" id="pass"/>
            </div>
        </div>

        <div class="checkbox">
            <label><input type="checkbox"> Remember me</label>
        </div>

        <input class="btn btn-default" type="button" name="Sign in" value="Sign in" onclick="return false"
               id="Sign_in"/>

        <input class="btn btn-default" type="submit" name="Sign up" value="Sign up" id="Sign_up"/>
    </form>
</div>

</body>
</html>
