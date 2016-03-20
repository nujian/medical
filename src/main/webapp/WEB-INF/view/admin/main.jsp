<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.care.Constants" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<!-- saved from url=(0041)http://v3.bootcss.com/examples/dashboard/ -->
<html lang="zh-CN"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://v3.bootcss.com/favicon.ico">
    <title>main</title>
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="../css/main.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="../js/ie-emulation-modes-warning.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  <meta name="chromesniffer" id="chromesniffer_meta" content="{&quot;jQuery&quot;:&quot;1.11.3&quot;,&quot;Bootstrap&quot;:-1}"><script type="text/javascript" src="chrome-extension://fhhdlnnepfjhlhilgmeepgkhjmhhhjkh/js/detector.js"></script></head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="http://v3.bootcss.com/examples/dashboard/#">Care</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="http://v3.bootcss.com/examples/dashboard/#">用户:nujian</a></li>
            <li><a href="http://v3.bootcss.com/examples/dashboard/#">退出登录</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li><a href="http://v3.bootcss.com/examples/dashboard/#"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>用户管理 <span class="sr-only">(current)</span></a></li>
            <li class="active"><a href="http://v3.bootcss.com/examples/dashboard/#"><span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>订单管理</a></li>
            <li><a href="http://v3.bootcss.com/examples/dashboard/#"><span class="glyphicon glyphicon-yen" aria-hidden="true"></span>提现管理</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h2 class="page-header">订单统计</h2>
          <div class="row placeholders">
            <div class="col-xs-12 col-sm-6 placeholder">
              <%--<a href="#">昨日订单 <span class="badge">42</span></a>--%>
              <%--<img src="http://img0.imgtn.bdimg.com/it/u=3853558770,1177640338&fm=21&gp=0.jpg" class="img-responsive" alt="Generic placeholder thumbnail">--%>
              <h4>昨日订单<span class="badge">42</span></h4>
              <%--<span class="text-muted">120</span>--%>
            </div>
            <div class="col-xs-12 col-sm-6 placeholder">
              <%--<img src="http://img0.imgtn.bdimg.com/it/u=3853558770,1177640338&fm=21&gp=0.jpg" class="img-responsive" alt="Generic placeholder thumbnail">--%>
              <h4>今日订单<span class="badge">42</span></h4>
              <%--<span class="text-muted">8</span>--%>
            </div>
          </div>
          <h2 class="sub-header">订单管理</h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号 </th>
                  <th>下单人</th>
                  <th>接单人</th>
                  <th>订单金额</th>
                  <th>订单状态</th>
                  <th>处理订单</th>
                </tr>
              </thead>
              <tbody>

              <c:forEach items="${orders}" var="order">
                <tr>
                  <td>${order.id}</td>
                  <td>${order.user.username}</td>
                  <td>${order.nurse.username}</td>
                  <td>${order.cost}</td>
                  <td>${order.status}</td>
                  <td>
                    <div class="dropdown">
                      <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                        处理
                        <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                        <li><a href="#">修改订单</a></li>
                        <li><a href="#">删除订单</a></li>
                      </ul>
                    </div>
                  </td>
                </tr>
              </c:forEach>

                <%--<tr>--%>
                  <%--<td>1</td>--%>
                  <%--<td>nujian</td>--%>
                  <%--<td>顾影</td>--%>
                  <%--<td>200</td>--%>
                  <%--<td>已完成</td>--%>
                  <%--<td>--%>
                    <%--<div class="dropdown">--%>
                      <%--<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">--%>
                        <%--处理--%>
                        <%--<span class="caret"></span>--%>
                      <%--</button>--%>
                      <%--<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">--%>
                        <%--<li><a href="#">修改订单</a></li>--%>
                        <%--<li><a href="#">删除订单</a></li>--%>
                      <%--</ul>--%>
                    <%--</div>--%>
                  <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td>2</td>--%>
                  <%--<td>nujian</td>--%>
                  <%--<td>顾影</td>--%>
                  <%--<td>200</td>--%>
                  <%--<td>已完成</td>--%>
                  <%--<td>--%>
                    <%--<div class="dropdown">--%>
                      <%--<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">--%>
                        <%--处理--%>
                        <%--<span class="caret"></span>--%>
                      <%--</button>--%>
                      <%--<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">--%>
                        <%--<li><a href="#">修改订单</a></li>--%>
                        <%--<li><a href="#">删除订单</a></li>--%>
                      <%--</ul>--%>
                    <%--</div>--%>
                  <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td>3</td>--%>
                  <%--<td>nujian</td>--%>
                  <%--<td>顾影</td>--%>
                  <%--<td>200</td>--%>
                  <%--<td>已完成</td>--%>
                  <%--<td>--%>
                    <%--<div class="dropdown">--%>
                      <%--<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">--%>
                        <%--处理--%>
                        <%--<span class="caret"></span>--%>
                      <%--</button>--%>
                      <%--<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">--%>
                        <%--<li><a href="#">修改订单</a></li>--%>
                        <%--<li><a href="#">删除订单</a></li>--%>
                      <%--</ul>--%>
                    <%--</div>--%>
                  <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td>4</td>--%>
                  <%--<td>nujian</td>--%>
                  <%--<td>顾影</td>--%>
                  <%--<td>200</td>--%>
                  <%--<td>已完成</td>--%>
                  <%--<td>--%>
                    <%--<div class="dropdown">--%>
                      <%--<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">--%>
                        <%--处理--%>
                        <%--<span class="caret"></span>--%>
                      <%--</button>--%>
                      <%--<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">--%>
                        <%--<li><a href="#">修改订单</a></li>--%>
                        <%--<li><a href="#">删除订单</a></li>--%>
                      <%--</ul>--%>
                    <%--</div>--%>
                  <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td>5</td>--%>
                  <%--<td>nujian</td>--%>
                  <%--<td>顾影</td>--%>
                  <%--<td>200</td>--%>
                  <%--<td>已完成</td>--%>
                  <%--<td>--%>
                    <%--<div class="dropdown">--%>
                      <%--<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">--%>
                        <%--处理--%>
                        <%--<span class="caret"></span>--%>
                      <%--</button>--%>
                      <%--<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">--%>
                        <%--<li><a href="#">修改订单</a></li>--%>
                        <%--<li><a href="#">删除订单</a></li>--%>
                      <%--</ul>--%>
                    <%--</div>--%>
                  <%--</td>--%>
                <%--</tr>--%>
              <%----%>
              </tbody>
            </table>
            <nav>
              <ul class="pagination">
                <li>
                  <a href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
                </li>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li>
                  <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                  </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="../js/jquery-1.7.1.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="../js/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../js/ie10-viewport-bug-workaround.js"></script>
  

<div id="window-resizer-tooltip"><a href="http://v3.bootcss.com/examples/dashboard/#" title="Edit settings"></a><span class="tooltipTitle">Window size: </span><span class="tooltipWidth" id="winWidth"></span> x <span class="tooltipHeight" id="winHeight"></span><br><span class="tooltipTitle">Viewport size: </span><span class="tooltipWidth" id="vpWidth"></span> x <span class="tooltipHeight" id="vpHeight"></span></div></body></html>