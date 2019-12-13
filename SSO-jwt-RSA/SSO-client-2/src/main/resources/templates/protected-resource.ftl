<!DOCTYPE html>
<html lang="en">
<head>
    <title>Protected Resource Service</title>
</head>
<body>

<#--<#if Request.username??>
    <h2>欢迎用户‘${Request.username!}’登录本地client2系统</h2>
    <a href="/logout">退出</a>
<#else>
    <h2>本地client2系统，未登录</h2>
    <a href="http://10.0.2.229:8080/login?redirect=http://10.0.2.229:8882/protected-resource">请登录</a>
</#if>-->

<#if Request.username??>
    <h2><span style="color: red">本地</span>client2系统</h2>
    <h2>欢迎用户‘${Request.username!}’登录系统</h2>
    <a href="/logout">退出</a>
<#else>
    <h2><span style="color: red">本地</span>client2系统，未登录</h2>
    <a href="http://native.yanxiaoping.top:8080/login?redirect=http://native.yanxiaoping.top:8882/protected-resource">请登录</a>
</#if>

</body>
</html>
