<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">


  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <name>Spring Security</name>

  <!-- Bootstrap core CSS -->


  <!-- Custom styles for this template -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<div class="container" style="width: 300px;">
  <form action="/login/index.jsp" method="post">
    <h2 class="form-signin-heading">Please sign in</h2>
    <input type="text" class="form-control" name="username" placeholder="Email address" required autofocus value="admin">
    <input type="password" class="form-control" name="password" placeholder="Password" required value="admin">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
  </form>
</div>

</body>
</html>
