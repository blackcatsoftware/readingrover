<!doctype html>
<html lang="en">
    <head>
        <script src="https://unpkg.com/vue@2.5.16/dist/vue.js"></script>
        <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    </head>
    <body>
        <h1>Search Avatars</h1>

        <div id="app">
            <div>
                <form>
                    <input type="text" id="search-bar" name="search-bar" v-model="search_string" placeholder="Search text" />
                    <button @click="onSearch">Search</button>
                    <button @click="onCreate">Create New</button>
                </form>
            </div>

            <br/>
            
            <h3>Search Results</h3>
            <hr />
            <div>
                <div v-for="avatar in avatars">
                    Search Result
                </div>
            </div>
        </div>

        <script>
            const app = new Vue(
            {
                el: '#app',
                data:
                {
                    search_string: "",
                    avatars: [],
                },
                methods:
                {
                    onSearch: function(event)
                    {
                        event.preventDefault();

                        console.log("Search string: " + this.search_string);
                        axios.get("/admin/avatars/do-search",
                        {
                            params:
                            {
                                'search-string': this.search_string,
                            }
                        })
                        .then(response =>
                        {
                            console.log("Result: " + response.data.results);
                        })
                        .catch(error =>
                        {
                            alert(error.response.data.message);
                        });
                    },
                    onCreate: function(event)
                    {
                        event.preventDefault();
                        window.location.assign("edit");
                    }
                }
            });
        </script>
    </body>
</html>