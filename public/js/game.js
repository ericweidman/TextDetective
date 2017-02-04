var jscon = {

    urls: {
        savegame: '/save-game',

    }
};

$('#savegame').click(function() {
    $.ajax({
        url: jscon.urls.savegame,
        method: "POST",
        success: function(data) {
            window.location.replace("index.html");
        },
        error: function(error) {
            console.log(error);
            alert("Fail!");
        }
    });
})
