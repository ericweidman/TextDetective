$(document).ready(function() {
    $('body').addClass('animated fadeIn');
});

var jscon = {

    urls: {
        newUser: '/create-user',

    }
};

function newUser(user) {
    $.ajax({
        url: jscon.urls.newUser,
        method: "POST",
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(user),
        dataType: 'text',
        success: function(data) {
            $('#newUser').addClass('animated fadeOut');
            setTimeout(function() {
              $('#move').animate({
                  'marginTop': '+=150px'
              }, 2000);
            }, 500)
            setTimeout(function() {
                $('#move').addClass('animated fadeOut');
            }, 4500)
            setTimeout(function() {
                window.location.href = "game.html"
            }, 5000)
        },
        error: function(error) {
            console.log("Add User", error);
            console.log(user);
            alert('Username taken.')
        }
    });
}

$('#newUser').submit(function(event) {
    event.preventDefault();
    var user = {};
    user.userName = $('input[name=userName]').val();
    user.pin = $('input[name=pin]').val();
    newUser(user);
});
