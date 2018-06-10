<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="favicon.ico">

        <title>Edit Avatar</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">

        <!-- Custom styles for this template -->
        <link href="/public/signin.css" rel="stylesheet">

        <script src="https://unpkg.com/vue@2.5.16/dist/vue.js"></script>
        <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    </head>
    <body>
        <form id="app" class="form-signin" style="max-width: 560px">
            <div class="form-group">
                <label for="name">Avatar Image</label>
                <input type="file" id="image" name="image" class="form-control" aria-describedby="imageHelp" ref="avatar_file_input" required>
            </div>
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" class="form-control" aria-describedby="nameHelp" placeholder="Avatar Name" v-model.trim="avatar.name" required autofocus>
            </div>

            <button class="btn btn-lg btn-primary btn-block mt-4" v-on:click="onSave">{{ button_text }}</button>
            <p class="mt-5 mb-3 text-muted text-center">&copy; 2018</p>
        </form>

        <script>
            const app = new Vue(
            {
                el: '#app',
                data:
                {
                    avatar:
                    {
                        id: '',
                        name: '',
                        image_id: ''
                    },
                },
                computed:
                {
                    title()
                    {
                        return this.avatar.id ? "Edit Avatar" : "Create Avatar";
                    },
                    button_text()
                    {
                        return this.avatar.id ? "Save" : "Create";
                    }
                },
                methods:
                {
                    onSave: function(event)
                    {
                        console.log("TODO onSave")
                        
                        event.preventDefault();

                        const data = new FormData();
                        data.set('file', app.$refs.avatar_file_input.file);
                        data.set('json',
                        {
                            id: this.avatar.id,
                            name: this.avatar.name,
                            image_id: this.avatar.image_id
                        });
                        
                        axios.post("/admin/avatars/do-upsert", data)
                        .then(response =>
                        {
                            console.log(response.data);
                        }).catch(error =>
                        {
                            alert(error.response.data.message);
                        });
                    }
                }
            });
        </script>
    </body>
</html>