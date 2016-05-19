<%@include file="include.jsp" %>

<html>
<head>
    <title>DexChat</title>

    <script type="text/javascript" async="" src="http://www.google-analytics.com/ga.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
    <link media="all" type="text/css" rel="stylesheet" href="http://bootsnipp.com/css/fullscreen.css">

    <link href="/css/home.css" type="text/css" rel="stylesheet"></link>

    <script type="text/javascript" src="/js/home.js"></script>

</head>
<body>

<span id="login" hidden><c:out value="${login}"/></span>
<span id="pass" hidden><c:out value="${pass}"/></span>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">DexChat</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#">Exit</a></li>
            </ul>
            <form class="navbar-form navbar-right" role="search" id="search_form">
                <div class="form-group input-group">
                    <input type="text" class="form-control" placeholder="Search new friends.." id="search_input">
          <span class="input-group-btn">
            <button class="btn btn-default" type="button" id="search_button">
              <span class="glyphicon glyphicon-search"></span>
            </button>
          </span>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span> My Account</a></li>
            </ul>
        </div>
    </div>
</nav>


<div class="content container-fluid bootstrap snippets">
    <div class="row row-broken">
        <div class="col-sm-3 col-xs-12">
            <div class="col-inside-lg decor-default chat" style="overflow: hidden; outline: none;" tabindex="5000">
                <div class="chat-users" id="chat-users_frame">
                    <h6>Friends</h6>
                    <!-- FRIENDS LIST -->

                </div>
            </div>
        </div>
        <div class="col-sm-9 col-xs-12">
            <div class="col-inside-lg decor-default chat" style="overflow: hidden; outline: none;" tabindex="5001">
                <!-- DIALOG  -->
                <div class="chat-body">
                    <h6 id="login_speaker">DexChat</h6>
                    <p style="display: none" id="empty_answer">Empty message list</p>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Search results</h4>
            </div>
            <div class="modal-body">
                <div class="chat-users" id="chat-users_model">
                    <h6>Friends</h6>
                    <p style="display: none" id="model_message">Incorrect friend login</p>
                    <h3 style="display: none" id="model_add_message">Friend was added</h3>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<footer>
    <div class="answer-add">
        <textarea placeholder="Write a message" id="answer_input"></textarea>
        <span class="answer-btn answer-btn-1"></span>
        <span class="answer-btn answer-btn-2" id="answer_button"></span>
    </div>
</footer>
</body>
</html>
