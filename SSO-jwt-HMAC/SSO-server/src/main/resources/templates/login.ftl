<!DOCTYPE html>
<html lang="en">
<head>
    <title>SSO服务端</title>
</head>
<body>
<form method="POST" action="/login?redirect=${RequestParameters.redirect!}">
    <h2>登录页面</h2>
    <input name="username" type="text" placeholder="用户名" autofocus="autofocus"/>
    <input name="password" type="password" placeholder="密 码"/>
    <div>(try xieshaoping)</div>
    <div style="color: red">${error!}</div>
    <br/>
    <button type="submit">登 录</button>
</form>
</body>
</html>
