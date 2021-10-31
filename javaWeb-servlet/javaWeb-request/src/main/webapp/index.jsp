<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>

<h1>login</h1>


<div style="text-align:center">
    <form action="http://localhost:8080/javaWeb_request_war/login" method="post">
        username:<input type="text" name="username"><br>
        pwd:<input type="password" name="password"><br>
        hobbys:
        <input type="checkbox" name="hobbys" value="girls">girls
        <input type="checkbox" name="hobbys" value="coding">code
        <input type="checkbox" name="hobbys" value="singing">singsing
        <input type="checkbox" name="hobbys" value="movies">movies
        <br>
        <input type="submit">
    </form>
</div>
<body>
</body>
</html>
