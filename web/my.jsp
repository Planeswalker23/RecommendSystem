
<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tool.*,entity.*,recommend.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>个人资料</title>
	<link rel="shortcut icon" href="images/favicon.png" />


	<link href="css/style.css" rel='stylesheet' type='text/css'/>
	<link href="css/popuo-box.css" rel="stylesheet" type="text/css" media="all" />
	<script src="https://cdn.bootcss.com/Chart.js/2.7.2/Chart.bundle.js"></script>
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<!-- CSS -->
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="assets/css/form-elements.css">
    <link rel="stylesheet" href="assets/css/style.css">
	





		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="keywords" content="Accessible Profile Widget Responsive Widget,Login form widgets, Sign up Web forms , Login signup Responsive web form,Flat Pricing table,Flat Drop downs,Registration Forms,News letter Forms,Elements" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
			<script src="js/jquery.min.js"></script>
			<script>$(document).ready(function(c) {
			$('.alert-close').on('click', function(c){
				$('.main-agile').fadeOut('slow', function(c){
					$('.main-agile').remove();
				});
			});	  
		});
		</script>
</head>
<body>

    <%!
        String name = "";
        User user = null;
    %>

    <%
        name = (String)session.getAttribute("username");
        user = tool.GetUserInfor.getUserInforByName(name);
    %>

	<h1 style="color: white;margin-top: 30px;">个人资料</h1>
	<div class="main-agileits" style="margin-top: 30px;">
		<div class="right-wthree" style="margin-top: 20px">
			<img src="images/headpicture.png" alt="image" />
		</div>
		<div class="left-w3ls">
			<ul class="address">
				<li>
					<ul class="address-text">
						<li><b>姓名 </b></li>
						<li><%=user.getUsername()%></li>
					</ul>
				</li>
				<li>
					<ul class="address-text">
						<li><b>性别 </b></li>
						<li><a href=""><%=user.getGender()%></a></li>
					</ul>
				</li>
				<li>
					<ul class="address-text">
						<li><b>生日 </b></li>
						<li><a href=""><%=user.getBirthday()%></a></li>
					</ul>
				</li>
                <li>
                    <ul class="address-text">
                        <li><b>邮箱 </b></li>
                        <li><a href=""><%=user.getE_mail()%></a></li>
                    </ul>
                </li>
			</ul>
			
			<div class="button">
				<a href="#small-dialog" class="play-icon popup-with-zoom-anim">修改个人信息</a>
			</div>
			<div id="small-dialog" class="mfp-hide w3ls_small_dialog wthree_pop">		
				<div class="agileits_modal_body">
					<form role="form" action="ChangeUserInforServlet" method="post" class="login-form">
                    	<div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入用户名" name="username">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="请输入密码" name="password">
                        	<!--<input type="password" name="password" placeholder="Password..." class="form-password form-control" id="form-password">-->
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入你的故乡，如浙江杭州" name="hometown">
                        </div>
                        <div class="form-group">
                            <input type="date" class="form-control" name="date">
                        </div>
                        <div class="form-group">
                            请选择你的性别：&nbsp;
                            <label><input type="radio" name="gender" value="male">男</label>&nbsp;&nbsp;&nbsp;
                            <label><input type="radio" name="gender" value="female">女</label>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入你的邮箱，如123456789@qq.com" name="email">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入你的电话，如12345678900" name="phonenumber">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入你的职业，如学生" name="profession">
                        </div>
                        <div class="form-group">
                            请至少选择一项兴趣:&nbsp;
                            <label><input type="checkbox" name="hobby" value="sport">运动</label>&nbsp;
                            <label><input type="checkbox" name="hobby" value="movie">电影</label>&nbsp;
                            <label><input type="checkbox" name="hobby" value="entertainment">娱乐</label>&nbsp;
                            <label><input type="checkbox" name="hobby" value="military">军事</label>&nbsp;
                            <label><input type="checkbox" name="hobby" value="read">阅读</label>&nbsp;
                        </div>
	                    <button type="submit" class="btn">确认修改</button>
	                </form>
				</div>
			</div>


			<!-- //pop-up-box -->
			<script src="js/jquery.magnific-popup.js" type="text/javascript"></script>
			<script>
			$(document).ready(function() {
				$('.popup-with-zoom-anim').magnificPopup({
				type: 'inline',
				fixedContentPos: false,
				fixedBgPos: true,
				overflowY: 'auto',
				closeBtnInside: true,
				preloader: false,
				midClick: true,
				removalDelay: 300,
				mainClass: 'my-mfp-zoom-in'
				});
			});
			</script>

		</div>
			<div class="clear"></div>
	</div>
	<br>
	<h2 style="color: white">兴趣模型</h2>
	<div style="width:700px;height:400px;margin-left: 300px;">
		<canvas id="myChart" width="500" height="300"></canvas>
		<%--chart--%>
		<script>
            var ctx = document.getElementById("myChart");
            var myChart = new Chart(ctx, {
                type: 'polarArea',
                data: {
                    labels: ["社会", "体育", "娱乐", "科技", "人文", "影视","教育","游戏"],
                    datasets: [{
                        label: '# of Votes',
                        data: [<%=GetUserInfor.getUserLikeModeFromDb(DB_io.getIdByUsername(name))%>],
                        backgroundColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 129, 74, 1)',
                            'rgba(25, 19, 64, 1)',
                            'rgba(255, 59, 64, 1)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 129, 74, 1)',
                            'rgba(25, 19, 64, 1)',
                            'rgba(255, 59, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero:true
                            }
                        }]
                    }
                }
            });
		</script>
	</div>
</body>
</html>