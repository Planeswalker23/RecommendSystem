<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/29
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="tool.*" %>
<%@ page import="java.util.List,DAO.ChangePageServlet,recommend.*"%>
<%@ page import="org.apache.mahout.cf.taste.recommender.RecommendedItem" %>
<%@ page import="java.util.ArrayList,tool.*" %>
<%@ page import="static recommend.DB_io.getIdByUsername" %>
<%@ page import="entity.ClassifyArticle" %>
<!doctype html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en-US"> <![endif]-->
<!--[if IE 7]>    <html class="lt-ie9 lt-ie8" lang="en-US"> <![endif]-->
<!--[if IE 8]>    <html class="lt-ie9" lang="en-US"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en-US"> <!--<![endif]-->
<head>
    <!-- META TAGS -->
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>23 News</title>

    <link rel="shortcut icon" href="images/favicon.png" />
    <script src="js/jquery-3.3.1.min.js"></script>
    <style>

        #page{
            margin-left:160px;
        }

    </style>


    <!-- Style Sheet-->
    <link rel="stylesheet" href="style.css"/>
    <link rel='stylesheet' id='bootstrap-css-css'  href='css/bootstrap5152.css?ver=1.0' type='text/css' media='all' />
    <link rel='stylesheet' id='responsive-css-css'  href='css/responsive5152.css?ver=1.0' type='text/css' media='all' />
    <link rel='stylesheet' id='pretty-photo-css-css'  href='js/prettyphoto/prettyPhotoaeb9.css?ver=3.1.4' type='text/css' media='all' />
    <link rel='stylesheet' id='main-css-css'  href='css/main5152.css?ver=1.0' type='text/css' media='all' />
    <link rel='stylesheet' id='custom-css-css'  href='css/custom5152.html?ver=1.0' type='text/css' media='all' />


    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="js/html5.js"></script></script>
    <![endif]-->


</head>

<body>

<!-- Start of Header -->
<div class="header-wrapper">
    <header>
        <div class="container">


            <div class="logo-container">
                <!-- Website Logo -->
                <a href="articles-list.jsp"  title="23 News">
                    <img src="images/logo.png" alt="23 News">
                </a>

            </div>


            <!-- Start of Main Navigation -->
            <nav class="main-nav">
                <div class="menu-top-menu-container">
                    <ul id="menu-top-menu" class="clearfix">
                        <li><a href="articles-list.jsp">时下流行</a></li>
                        <li><a href="articles_type_society.jsp">社会</a></li>
                        <li><a href="articles_type_physical.jsp">体育</a></li>
                        <li><a href="articles_type_entertainment.jsp">娱乐</a></li>
                        <li><a href="articles_type_science.jsp">科技</a></li>
                        <li><a href="articles_type_cultural.jsp">人文</a></li>
                        <li><a href="articles_type_films.jsp">影视</a></li>
                        <li><a href="articles_type_education.jsp">教育</a></li>
                        <li><a href="articles_type_game.jsp">游戏</a></li>
                        <li><a href="articles-recommend.jsp">猜你喜欢</a></li>
                        <li><a href="" id="upload">上传</a></li>
                        <li><a href="regist.html" id="regist">注册</a></li>
                        <li><a href="login.jsp" id="login">登录</a></li>
                        <li><a href="" id="head" onclick="tomyjsp()"><img src="images/headlogo.png" style="margin-top: -2px;"></a></li>
                    </ul>
                </div>
            </nav>
            <!-- End of Main Navigation -->

        </div>
    </header>
</div>
<!-- End of Header -->
<%
    String username = "";
    int index = 0;
    int user_id = -1;
    int item_id = 0;
%>
<%


    username = (String)session.getAttribute("username");
    user_id = getIdByUsername(username);

    String item_id0 = request.getParameter("item_id");

    item_id = Integer.parseInt(item_id0);

    ClassifyArticle item = GetDetailContent.getContent(item_id);

    // List<RecommendedItem> rmItem = recommend.Similarity.recommender(user_id,16);
    //List<NewsList> as = new ArrayList<>();
    String title = item.getTitle();
%>
<!-- Start of Search Wrapper -->
<div class="search-area-wrapper">
    <div class="search-area container">
        <h3 class="search-header"><%=title%></h3>
    </div>
</div>
<!-- End of Search Wrapper -->

<!-- Start of Page Container -->
<div class="page-container" id="page">
    <div class="container">

        <div class="row">

            <!-- start of page content -->
            <div class="span8 main-listing">
                <%=
                    "<font style=\"font-size:20px;line-height: 45px;font-weight: normal;\">" + item.getContent() + "</font>"
                    //TurnToHtml.turn(item.getContent())
                %>
            </div>
            <!-- end of page content -->

        </div>
    </div>
</div>
<!-- End of Page Container -->
<div class="page-container">
    <div class="container">
    <span style="margin-left: 25%">
        <img id="img1" src="images/titlelike1.png" onclick="changeImgLike(this)">
    </span>
    <span style="margin-left: 400px">
        <img id="img2" src="images/titleunlike1.png" onclick="changeImgUnlike(this)">
    </span>
    </div>
</div>
<!-- Start of Footer -->
<footer id="footer-wrapper">
    <div id="footer" class="container">
        <div class="row">

            <div class="span3">
                <section class="widget">
                    <h3 class="title">网站使用指南</h3>
                    <div class="textwidget">
                        <p>1.注册成为本站会员</p>
                    </div>
                </section>
            </div>

            <div class="span3">
                <section class="widget">
                    <h3 class="title">&nbsp;&nbsp;</h3>
                    <div class="textwidget">
                        <p>2.浏览热门新闻或自己偏好的新闻</p>
                    </div>
                </section>
            </div>

            <div class="span3">
                <section class="widget">
                    <h3 class="title">&nbsp;&nbsp;</h3>
                    <div class="textwidget">
                        <p>3.进入猜你喜欢查看为您推荐的新闻</p>
                    </div>
                </section>
            </div>

            <div class="span3">
                <section class="widget">
                    <h3 class="title">&nbsp;&nbsp;</h3>
                    <div class="textwidget">
                        <p>4.查看本站为您建立的偏好模型</p>
                    </div>
                </section>
            </div>

        </div>
    </div>
    <!-- end of #footer -->

    <!-- Footer Bottom -->
    <div id="footer-bottom-wrapper">
        <div id="footer-bottom" class="container">
            <div class="row">
                <div class="span6">
                    <p class="copyright">
                    </p>
                </div>
                <div class="span6">
                </div>
            </div>
        </div>
    </div>
    <!-- End of Footer Bottom -->

</footer>
<!-- End of Footer -->


<a href="#top" id="scroll-top"></a>

<!-- script -->

<script>



    function loginsuccess() {
        var regist = document.getElementById('regist');
        regist.href = '';
        regist.innerHTML = '<%=username%>';

        var login = document.getElementById('login');
        login.href = 'logout.jsp';
        login.innerHTML = '注销';

        var upload = document.getElementById('upload');
        upload.href='upload.jsp';
        upload.onclick = function () {
        };
    }
    if('<%=username%>'!=''&&'<%=username%>'!='null')
    {
        loginsuccess();

        window.onload = function()
        {
            //alert("load");
            $.ajax({
                type:"POST",
                url:"ReadTimeServlet",
                data:{item_id:<%=item_id%>,user_id:<%=user_id%>,flag:"begin"},
                success:function(data,textStatus){
                console.log(data);
                }
            });
        }

        window.onbeforeunload = function(){
           // alert("unload");
             $.ajax({
                 type:"POST",
                 url:"ReadTimeServlet",
                 data:{item_id:<%=item_id%>,user_id:<%=user_id%>,flag:"end"},
                 success:function(data,textStatus){
                 console.log(data);
                 }
          });
        };
    }else{
         head.onclick = function () {
             alert("请先登录");
         }
         upload.onclick = function () {
             alert("使用上传功能请先登录");
         }
    }

    function changeImgLike(obj){
        var img = document.getElementById(obj.id);

        //console.log(img);
        if('<%=username%>'!=''&&'<%=username%>'!='null'){
            var s = obj.id;
            var n = s.indexOf('_');
            var id = s.substring(n+1);

            if (img.src.indexOf('like1') > 0) {
                img.src="images/titlelike2.png";
            } else {
                img.src="images/titlelike1.png";
            }
            $.ajax({
                type:"POST",
                url:"LogClickedLikeServlet",
                data:{item_id:<%=user_id%>,username:'<%=username%>'},
                success:function(data,textStatus){
                    console.log(data);
                }
            });
        }else{
            alert("使用点踩功能请先登录");
        }
    }

    function changeImgUnlike(obj){
        var img = document.getElementById(obj.id);

        if('<%=username%>'!=''&&'<%=username%>'!='null'){
            var s = obj.id;
            var n = s.indexOf('_');
            var id = s.substring(n+1);

            $.ajax({
                type:"POST",
                url:"LogClickedUnLikeServlet",
                data:{item_id:<%=user_id%>,username:'<%=username%>'},
                success:function(data,textStatus){
                    console.log(data);
                }
            });
            if (img.src.indexOf('unlike1') > 0) {
                img.src="images/titleunlike2.png";
            } else {
                img.src="images/titleunlike1.png";
            }
        }else{
            alert("使用点踩功能请先登录");
        }
    }

    function tomyjsp() {
        if('<%=username%>'!=''&&'<%=username%>'!='null') {
            head.href = 'my.jsp';
        }else{
            alert("请先登录");
        }
    }


</script>


</body>
</html>
