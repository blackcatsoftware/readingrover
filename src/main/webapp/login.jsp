
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">

    <title>Login to Reading Rover!</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">

    <!-- Custom styles for this template -->
    <link href="signin.css" rel="stylesheet">
  </head>

  <body>
    <%@ include file="rr-svg.html" %>

    <form action="/public/do-login" method="post" class="form-signin">
      <div class="text-center mb-4">
        
        <svg class="Logo" fill="var(--logo-background)">
            <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#SVGReadingRoverLogoBackground">
            </use>            
        </svg>
          
        <h1 class="h3 mb-3 font-weight-normal">Reading Rover</h1>
        <p>Making reading fun for kids!</p>
      </div>

      <div class="form-label-group">
        <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
        <label for="username">Username</label>
      </div>

      <div class="form-label-group">
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        <label for="password">Password</label>
      </div>

      <div class="custom-control custom-checkbox mb-3">
        <input type="checkbox" class="custom-control-input" id="remember" name="remember" value="remember-me" checked>
        <label class="custom-control-label" for="remember">Remember me</label>
      </div>
      
      <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      <p class="mt-5 mb-3 text-muted text-center">&copy; 2018</p>
    </form>
  </body>
</html>
