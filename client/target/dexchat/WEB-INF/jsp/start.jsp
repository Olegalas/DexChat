<%@include file="include.jsp" %>

<html>
<head>

    <title>DexChat</title>
    <script type="text/javascript" src="/js/web_socket.js"></script>
    <script type="text/javascript" src="/js/login.js"></script>

</head>
<body>
<div class="container">
    <form action="/registration" method="POST" name="form" id="form" class="form-horizontal" role="form">

        <h2>DexChat</h2>
        <p>Welcome to Dex world</p>

        <div class="alert alert-danger" style="display: none" id="mainAlert">
            <strong>Sorry!</strong> Incorrect login or pass
        </div>

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

        <button type="button" class="btn btn-info" data-toggle="collapse" data-target="#demo" id="forgotPass">Forgot pass?</button>

        <div id="demo" class="collapse">

            <label for="email" class="control-label col-sm-2">Enter email : </label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="email" placeholder="email" id="email"/>
            </div>

            <label for="loginEmail" class="control-label col-sm-2">Enter login : </label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="loginEmail" placeholder="login" id="loginEmail"/>
            </div>

            <input class="btn btn-default" type="submit" name="Send email" onclick="return false" value="Send pass on your email" id="Send_email"/>
        </div>
    </form>
</div>

</body>
</html>
