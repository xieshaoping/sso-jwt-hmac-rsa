<!DOCTYPE html>
<html lang="en">
<head>
    <title>Protected Resource Service</title>
</head>
<body>

<#if Request.username??>
    <h2><span style="color: red">本地</span>client1系统</h2>
    <h2>欢迎用户‘${Request.username!}’登录系统</h2>
    <a href="/logout">退出</a>
<#else>
    <h2><span style="color: red">本地</span>client1系统，未登录</h2>
    <a href="http://native.yanxiaoping.top:8080/login?redirect=http://native.yanxiaoping.top:8881/protected-resource">请登录</a>
</#if>

</body>
</html>
