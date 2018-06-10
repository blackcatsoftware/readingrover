function hoverWhite(name)
{
    var el = document.getElementById("icon-" + name);

    el.addEventListener("mouseenter", function(event)
    {
       let images = event.target.getElementsByTagName("img");
       for (var idx = 0; idx < images.length; idx++)
       {
           images[idx].setAttribute("src", "images/icons/"+name+"-white.svg");    
       }
    });

    el.addEventListener("mouseleave", function(event)
    {
        let images = event.target.getElementsByTagName("img");
        for (var idx = 0; idx < images.length; idx++)
        {
            images[idx].setAttribute("src", "images/icons/"+name+".svg");
        }
    });
}

hoverWhite('book');
hoverWhite('bug');







