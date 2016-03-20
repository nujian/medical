<%@ page import="com.care.Constants" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<!-- saved from url=(0038)http://v3.bootcss.com/examples/signin/ -->
<html lang="zh-CN"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://v3.bootcss.com/favicon.ico">
    <title>login</title>
    <!-- Bootstrap core CSS -->
    <link href="/care/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="/care/css/signin.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/care/js/ie-emulation-modes-warning.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  <meta name="chromesniffer" id="chromesniffer_meta" content="{&quot;Bootstrap&quot;:-1}"><script type="text/javascript" src="chrome-extension://fhhdlnnepfjhlhilgmeepgkhjmhhhjkh/js/detector.js"></script></head>
  <body>
    <div class="container">
      <form class="form-signin" action="/care/web/admin/login" method="post">
          <h2 class="form-signin-heading">医疗项目管理后台</h2><br/>
          <label for="inputMobile" class="sr-only">手机号码</label>
          <input type="number" id="inputMobile" class="form-control" placeholder="请输入手机号码" required="" autofocus="" name="mobile"><br/>
          <label for="inputPassword" class="sr-only">密码</label>
          <input type="password" id="inputPassword" class="form-control" placeholder="请输入密码" required="" name="password">
          <div class="checkbox">
              <label>
                  <input type="checkbox" value="remember-me">记住我
              </label>
          </div>
          <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
      </form>
  </div> <!-- /container -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/care/js/ie10-viewport-bug-workaround.js"></script>
<div id="window-resizer-tooltip" style="display: block;"><a href="http://v3.bootcss.com/examples/signin/#" title="Edit settings"></a><span class="tooltipTitle">Window size: </span><span class="tooltipWidth" id="winWidth">1280</span> x <span class="tooltipHeight" id="winHeight">800</span><br><span class="tooltipTitle">Viewport size: </span><span class="tooltipWidth" id="vpWidth">1280</span> x <span class="tooltipHeight" id="vpHeight">684</span></div></body></html>