<!DOCTYPE html>
<head>
</head>

<body>
    <h1>Welcome to Reading Rover</h1>
    <div>Making reading fun for kids!</div>
    <br/>

    <form action="/public/do-login" method="post">
        <div>
            <label for="username">Username*</label><br/>
            <input type="text" name="username" required autofocus/>
        </div>
        <br/>
        
        <div>
            <label for="password">Password*</label><br/>
            <input type="password" name="password" required/>
        </div>
        <br/>
        
        <div>
            <input type="submit" value="Login"/>
        </div>
    </form>
</body>
</html>
