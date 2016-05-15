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

<div class="content container-fluid bootstrap snippets">
    <div class="row row-broken">
        <div class="col-sm-3 col-xs-12">
            <div class="col-inside-lg decor-default chat" style="overflow: hidden; outline: none;" tabindex="5000">
                <div class="chat-users">
                    <h6>Friends</h6>

                    <form class="navbar-form navbar-left" method="GET" role="search" id="search_form">
                        <div class="form-group">
                            <input type="text" name="q" class="form-control" placeholder="Search">
                        </div>
                        <button type="buttom" class="btn btn-default" data-toggle="modal" data-target="#myModal"
                                onclick="return false"><i class="glyphicon glyphicon-search"></i></button>
                    </form>


                    </br></br></br></br></br></br>
                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                            <div class="status online"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="mood">mood</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-9 col-xs-12 chat" style="overflow: hidden; outline: none;" tabindex="5001">
            <div class="col-inside-lg decor-default">
                <div class="chat-body">
                    <h6>DexChat</h6>
                    <div class="answer left">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar1.png" alt="User name">
                            <div class="status offline"></div>
                        </div>
                        <div class="name">Friend</div>
                        <div class="text">
                            Lorem ipsum dolor amet, consectetur adipisicing elit Lorem ipsum dolor amet, consectetur
                            adipisicing elit Lorem ipsum dolor amet, consectetur adiping elit
                        </div>
                        <div class="time">5 min ago</div>
                    </div>
                    <div class="answer right">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <div class="status offline"></div>
                        </div>
                        <div class="name">You</div>
                        <div class="text">
                            Lorem ipsum dolor amet, consectetur adipisicing elit Lorem ipsum dolor amet, consectetur
                            adipisicing elit Lorem ipsum dolor amet, consectetur adiping elit
                        </div>
                        <div class="time">5 min ago</div>
                    </div>
                    <div class="answer-add">
                        <input placeholder="Write a message">
                        <span class="answer-btn answer-btn-1"></span>
                        <span class="answer-btn answer-btn-2"></span>
                    </div>
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
                <div class="chat-users">
                    <h6>Friends</h6>


                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                        </div>
                        <div class="name">Fiend</div>
                        <div class="mood">mood</div>
                        <button type="button" class="btn btn-default">add</button>
                    </div>

                    <div class="user">
                        <div class="avatar">
                            <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                            <!-- status online; off; busy; offline-->
                        </div>
                        <div class="name">Fiend</div>
                        <div class="mood">mood</div>
                        <button type="button" class="btn btn-default">add</button>
                    </div>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>

</body>
</html>
