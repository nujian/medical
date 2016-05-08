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
    <title>order</title>
    <!-- Bootstrap core CSS -->
    <link href="/care/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="/care/css/main.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="..//care/assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/care/js/ie-emulation-modes-warning.js"></script>
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
            <li><a href="#">用户:${user.username}</a></li>
            <li><a href="/care/web/admin/logout">退出登录</a></li>
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
            <c:choose>
              <c:when test="${mainType==1}">
                <li class="active">
                  <a href="/care/web/admin/index/1">
                    <span class="glyphicon glyphicon-user" aria-hidden="true"></span>用户管理 <span class="sr-only">(current)</span>
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/2">
                    <span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>订单管理
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/3">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>提现管理
                  </a>
                </li>
                <li>
                  <a href="http://www.umeng.com/">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>项目统计
                  </a>
                </li>
              </c:when>
              <c:when test="${mainType==2}">
                <li>
                  <a href="/care/web/admin/index/1">
                    <span class="glyphicon glyphicon-user" aria-hidden="true"></span>用户管理 <span class="sr-only">(current)</span>
                  </a>
                </li>
                <li class="active">
                  <a href="/care/web/admin/index/2">
                    <span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>订单管理
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/3">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>提现管理
                  </a>
                </li>
                <li>
                  <a href="http://www.umeng.com/">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>项目统计
                  </a>
                </li>
              </c:when>
              <c:when test="${mainType==3}">
                <li>
                  <a href="/care/web/admin/index/1">
                    <span class="glyphicon glyphicon-user" aria-hidden="true"></span>用户管理 <span class="sr-only">(current)</span>
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/2">
                    <span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>订单管理
                  </a>
                </li>
                <li  class="active">
                  <a href="/care/web/admin/index/3">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>提现管理
                  </a>
                </li>
                <li>
                  <a href="http://www.umeng.com/">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>项目统计
                  </a>
                </li>
              </c:when>
              <c:otherwise>
                <li class="active">
                  <a href="/care/web/admin/index/1">
                    <span class="glyphicon glyphicon-user" aria-hidden="true"></span>用户管理 <span class="sr-only">(current)</span>
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/2">
                    <span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>订单管理
                  </a>
                </li>
                <li>
                  <a href="/care/web/admin/index/3">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>提现管理
                  </a>
                </li>
                <li>
                  <a href="http://www.umeng.com/">
                    <span class="glyphicon glyphicon-yen" aria-hidden="true"></span>项目统计
                  </a>
                </li>
              </c:otherwise>
            </c:choose>
          </ul>
        </div>
         <!-- 数据统计 start -->
           <c:choose>
                <c:when test="${mainType==1}">
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                  <h2 class="page-header">用户统计</h2>
                  <div class="row placeholders">
                    <div class="col-xs-6 col-sm-3 placeholder">
                      <h4>新增用户<span class="badge">42</span></h4>
                      <span class="text-muted">Something else</span>
                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">
                      <h4>新增护士<span class="badge">42</span></h4>
                      <span class="text-muted">Something else</span>
                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">
                      <h4>昨日订单<span class="badge">42</span></h4>
                      <span class="text-muted">Something else</span>
                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <%--<img data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="Generic placeholder thumbnail">--%>
                      <h4>昨日订单<span class="badge">42</span></h4>
                      <span class="text-muted">Something else</span>
                    </div>
                  </div>
              </c:when>
              <c:when test="${mainType==2}">
                  <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <h2 class="page-header">订单统计</h2>
                    <div class="row placeholders">
                      <div class="col-xs-6 col-sm-3 placeholder">
                        <h4>今日订单<span class="badge">42</span></h4>
                        <span class="text-muted">Something else</span>
                      </div>
                      <div class="col-xs-6 col-sm-3 placeholder">
                        <h4>昨日订单<span class="badge">42</span></h4>
                        <span class="text-muted">Something else</span>
                      </div>
                      <div class="col-xs-6 col-sm-3 placeholder">
                        <h4>上周订单<span class="badge">42</span></h4>
                        <span class="text-muted">Something else</span>
                      </div>
                      <div class="col-xs-6 col-sm-3 placeholder">
                          <%--<img data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="Generic placeholder thumbnail">--%>
                        <h4>上月订单<span class="badge">42</span></h4>
                        <span class="text-muted">Something else</span>
                      </div>
                   </div>
              </c:when>
            </c:choose>
          <!-- 数据统计end -->


          <!-- 订单管理 start  -->
          <c:choose>
            <c:when test="${mainType == 1}">
                  <!-- -->
                  <h2 class="sub-header">用户管理</h2>
                  <div class="table-responsive">
                    <table class="table table-striped">
                      <thead>
                      <tr>
                        <th>用户Id </th>
                        <th>用户名称</th>
                        <th>用户类型</th>
                        <th>联系电话</th>
                        <th>处理订单</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach items="${users}" var="user">
                        <tr>
                          <td>${user.id}</td>
                          <td>${user.username}</td>
                          <td>${user.userType}</td>
                          <td>${user.mobile}</td>
                          <td>
                            <div class="dropdown">
                              <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                处理
                                <span class="caret"></span>
                              </button>
                              <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                <li><a href="#">停单</a></li>
                                <li><a href="#">解封</a></li>
                              </ul>
                            </div>
                          </td>
                        </tr>
                      </c:forEach>
                      </tbody>
                    </table>

                    <!-- 分页管理 -->
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
                    <!-- 分页管理 -->

                    </div>
                    <!-- -->
            </c:when>
            <c:when test="${mainType == 2}">
                  <h2 class="sub-header">订单管理</h2>
                  <div class="table-responsive">
                    <table class="table table-striped">
                      <thead>
                      <tr>
                        <th>订单ID </th>
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
                      </tbody>
                    </table>

                    <!-- 分页管理 -->
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
                    <!-- 分页管理 -->

                    </div>
            </c:when>
            <c:when test="${mainType == 3}">
                    <%--<h2 class="sub-header">提现管理</h2>--%>
                    <%--<div class="table-responsive">--%>
                      <%--<table class="table table-striped">--%>
                        <%--<thead>--%>
                        <%--<tr>--%>
                          <%--<th>订单ID </th>--%>
                          <%--<th>下单人</th>--%>
                          <%--<th>接单人</th>--%>
                          <%--<th>订单金额</th>--%>
                          <%--<th>订单状态</th>--%>
                          <%--<th>处理订单</th>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--<c:forEach items="${orders}" var="order">--%>
                          <%--<tr>--%>
                            <%--<td>${order.id}</td>--%>
                            <%--<td>${order.user.username}</td>--%>
                            <%--<td>${order.nurse.username}</td>--%>
                            <%--<td>${order.cost}</td>--%>
                            <%--<td>${order.status}</td>--%>
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
                        <%--</c:forEach>--%>
                        <%--</tbody>--%>
                      <%--</table>--%>
            </c:when>
          </c:choose>
          <!-- 订单管理 end -->
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/care/js/jquery-1.7.1.js"></script>
    <script src="/care/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/care/js/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/care/js/ie10-viewport-bug-workaround.js"></script>
  

<div id="window-resizer-tooltip"><a href="http://v3.bootcss.com/examples/dashboard/#" title="Edit settings"></a><span class="tooltipTitle">Window size: </span><span class="tooltipWidth" id="winWidth"></span> x <span class="tooltipHeight" id="winHeight"></span><br><span class="tooltipTitle">Viewport size: </span><span class="tooltipWidth" id="vpWidth"></span> x <span class="tooltipHeight" id="vpHeight"></span></div></body></html>