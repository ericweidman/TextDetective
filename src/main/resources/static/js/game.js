$(document).ready(function() {
              setTimeout(function() {
        $('#fixedfooter').addClass('animated fadeIn');
    }, 5000);
    $.ajax({
        url: '/intro',
        method: "GET",
        dataType: 'text',
        success: function(intro) {
            setTimeout(function() {
                var table = document.getElementById("gameText");
                var row = table.insertRow(-1);
                row.innerHTML = '<p>' + intro + '</p>';
                $(row).addClass('animated fadeIn');
            }, 2000);
        },
        error: function(error) {
            alert('Nope');
        }
    })
});

var jscon = {
    urls: {
        userAction: '/user-action'
    }
};

function userAction(action) {
    if (action === 'save'|| action === 'exit' || action === 'logout'
   || action === 'savegame' || action === 'save game' || action === 'quit') {
        $.ajax({
            url: jscon.urls.userAction,
            method: "POST",
            contentType: 'application/json; charset=utf-8',
            data: action,
            dataType: 'text',
            success: function(data) {
                $('body').addClass('animated fadeOut');
                setTimeout(function() {
                    window.location.href = "index.html"
                }, 3000)
            },
            error: function(error) {
                console.log(error);
                alert('This fail');
            }
        });
    }
    $.ajax({
        url: jscon.urls.userAction,
        method: "POST",
        contentType: 'application/json; charset=utf-8',
        data: action,
        dataType: 'text',
        success: function(response) {
            setTimeout(function() {
                var table = document.getElementById("gameText");
                var row = table.insertRow(-1);
                row.innerHTML = '<p>' + response + '</p>';
                var gameText = document.getElementById('gameText');
                gameText.scrollTop = gameText.scrollHeight;
                $(row).addClass('animated fadeIn');
            }, 1000);
        },
    });
}

$('#userAction').submit(function(event) {
    event.preventDefault();
    var fewSeconds = 2.5;
    var action = $('input[name=userAction]').val();
    var table = document.getElementById("gameText");
    var row = table.insertRow(-1);
    row.innerHTML = '<p>' + action + '</p>';
    var gameText = document.getElementById('gameText');
    gameText.scrollTop = gameText.scrollHeight;
    userAction(action);
    this.reset();
    document.getElementById('submitAction').disabled = true;
       setTimeout(function(){
         document.getElementById('submitAction').disabled = false;
     }, fewSeconds*1000);
});
