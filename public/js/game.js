$(document).ready(function() {
    $('body').addClass('animated fadeIn');
});


var jscon = {
    urls: {
        savegame: '/save-game',
        userAction: '/user-action'
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

function userAction(action) {
    $.ajax({
        url: jscon.urls.userAction,
        method: "POST",
        contentType: 'application/json; charset=utf-8',
        data: action,
        dataType: 'text',
        success: function(response) {

        },
    });
}

$('#userAction').submit(function(event) {
    event.preventDefault();
    var action = $('input[name=userAction]').val();
    userAction(action);
    this.reset();
});
