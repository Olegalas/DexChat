<%@include file="include.jsp" %>

<html>
<head>
    <title>Registration</title>
    <script type="text/javascript" src="/js/web_socket.js"></script>
    <script type="text/javascript" src="/js/registration.js"></script>
</head>
<body>
<div class="container">
    <form action="" method="POST" name="test" id="form" class="form-horizontal" role="form">

        <h2>Registration</h2>
        <p>Please, fill up following fields</p>

        <div class="alert alert-danger" style="display: none" id="mainAlert">
            <strong>Sorry!</strong> Login or email has already used
        </div>

        <div class="form-group">

            <div class="alert alert-danger" style="display: none" id="loginAlert">
                <strong>Incorrect!</strong> On english. Min - 4 Max - 20. Without special symbols. Must be not empty
            </div>

            <label for="login" class="control-label col-sm-2">Enter login</label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="login" placeholder="login" id="login"
                       title="Enter your login"/></br></br>
            </div>
        </div>

        <div class="form-group">

            <div class="alert alert-danger" style="display: none" id="nameAlert">
                <strong>Incorrect!</strong> On english. Min - 4 Max - 20. Without special symbols. Must be not empty
            </div>

            <label for="name" class="control-label col-sm-2">Enter name</label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="name" placeholder="name" id="name" title="Enter your name"/></br></br>
            </div>
        </div>

        <div class="form-group">

            <div class="alert alert-danger" style="display: none" id="emailAlert">
                <strong>Incorrect!</strong> Enter your email correctly. Must be not empty
            </div>

            <label for="email" class="control-label col-sm-2">Enter email</label>
            <div class="col-sm-10">
                <input class="form-control" type="text" name="email" placeholder="email" id="email" title="Enter your email"/></br></br>
            </div>
        </div>

        <div class="form-group">

            <div class="alert alert-danger" style="display: none" id="passAlert">
                <strong>Incorrect!</strong> On english. Min - 4 Max - 20. Without special symbols. Must be not empty
            </div>

            <label for="pass" class="control-label col-sm-2">Enter pass</label>
            <div class="col-sm-10">
                <input class="form-control" type="password" name="pass" placeholder="pass" id="pass"/></br></br>
            </div>
        </div>

        <div class="form-group">

            <div class="alert alert-danger" style="display: none" id="repassAlert">
                <strong>Incorrect!</strong> Your passwords are different.
            </div>

            <label for="rePass" class="control-label col-sm-2">Repeat pass</label>
            <div class="col-sm-10">
                <input class="form-control" type="password" name="rePass" placeholder="repass" id="rePass"/></br></br>
            </div>
        </div>

        <input class="btn btn-default" type="button" name="Ready" value="Ready" onclick="return false" id="Ready"/>
    </form>
</div>
</body>
</html>
