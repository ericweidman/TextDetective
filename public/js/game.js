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
                row.innerHTML = intro;
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
        savegame: '/save-game',
        userAction: '/user-action'
    }
};

function userAction(action) {
    if (action === 'save') {
        $.ajax({
            url: jscon.urls.savegame,
            method: "POST",
            success: function(data) {
                $('body').addClass('animated fadeOut');
                setTimeout(function() {
                    window.location.href = "index.html"
                }, 3000)
            },
            error: function(error) {
                console.log(error);
                alert("Fail!");
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
                row.innerHTML = response;
                $(row).addClass('animated fadeIn');
            }, 1000);
        },
    });
}

$('#userAction').submit(function(event) {
    event.preventDefault();
    var fewSeconds = 1;
    var action = $('input[name=userAction]').val();
    var table = document.getElementById("gameText");
    var row = table.insertRow(-1);
    row.innerHTML = '<p>' + action + '</p>';
    userAction(action);
    this.reset();
    document.getElementById('submitAction').disabled = true;
       setTimeout(function(){
         document.getElementById('submitAction').disabled = false;
     }, fewSeconds*1000);
});
