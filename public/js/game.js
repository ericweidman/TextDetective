$(document).ready(function() {
    $('body').addClass('animated fadeIn');
});


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
            $('body').addClass('animated fadeOut');
            setTimeout(function() {
                window.location.href = "index.html"
            }, 1700)
        },

        error: function(error) {
            console.log(error);
            alert("Fail!");
        }
    });
})
