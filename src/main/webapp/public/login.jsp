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

    <script src="https://unpkg.com/vue@2.5.16/dist/vue.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
  </head>

  <body>
    <%@ include file="rr-svg.html" %>

    <form id="app" class="form-signin">
      <div class="text-center mb-4">
        
        <svg class="Logo">
            <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#SVGReadingRoverLogo">
            </use>            
        </svg>
          
        <h1 class="h3 mb-3 font-weight-normal">Reading Rover</h1>
        <p>Making reading fun for kids!</p>
      </div>

      <div class="form-label-group">
        <input type="text" id="username" name="username" class="form-control" placeholder="Username" v-model.trim="auth.username" required autofocus>
        <label for="username">Username</label>
      </div>

      <div class="form-label-group">
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" v-model.trim="auth.password" required>
        <label for="password">Password</label>
      </div>

      <div class="custom-control custom-checkbox mb-3">
        <input type="checkbox" class="custom-control-input" id="remember" name="remember" v-model="auth.remember_me" checked>
        <label class="custom-control-label" for="remember">Remember me</label>
      </div>
      
      <button class="btn btn-lg btn-primary btn-block" v-on:click="login">Sign in</button>

      <div class="mt-5 mb-3 text-muted text-center">
        <a class="text-primary" href="/public/signup">Create an Account</a>
      </div>

      <p class="mt-5 mb-3 text-muted text-center">&copy; 2018</p>
    </form>

    <script>
      const app = new Vue(
      {
        el: '#app',
        data:
        {
          auth:
          {
            username: '',
            password: '',
            remember_me: true
          }
        },
        methods:
        {
          login: function(event)
          {
            if (! event.target.form.checkValidity())
            {
              return;
            }

            event.preventDefault();

            axios.post('/public/do-login',
            {
              data:
              {
                username: this.auth.username,
                password: this.auth.password,
                remember_me: this.auth.remember_me
              }
            })
            .then(response =>
            {
              window.location.assign(this.getOptionalRequestURL("fwd", "/"));
            })
            .catch(error =>
            {
              alert(error.response.data.message);
            });
          },
          getOptionalRequestURL(key, default_value) 
          {
            let params = (new URL(document.location)).searchParams;
            let request_url = params.get(key);
            
            return request_url || default_value;
          },
        }
      })
    </script>
  </body>
</html>
