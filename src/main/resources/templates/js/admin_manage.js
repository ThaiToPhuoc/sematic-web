
$.ajax({
    url : 'php/admin_manage.php',
    type : 'post',
    dataType : 'json',
    success : function (result){
        var html = '';
        html += '<div class="container">';
        html += '<div class="item item1">username</div>';
        html += '<div class="item item1">password</div>';
        html += '<div class="item item1">email</div>';
        html += '<div class="item item1">avatar</div>';
        html += '<div class="item item1">delete</div>';
        html += '</div>';

        $.each (result, function (key, item){
            html += '<div class="container">';
                html += '<div class="item">';
                html += item['username'];
                html += '</div>';
                html += '<div class="item">';
                html += item['pass'];
                html += '</div>';
                html += '<div class="item">';
                html += item['email'];
                html += '</div>';
                html += '<div class="item"><img src="images/';
                html += item['avatar'];
                html += '" style="height:30px;width:auto;">';
                html += '</div>';
                html += '<div class="item delete">';
                html += '<button><span class="fa fa-trash"></span></button>';
                html += '</div>';
            html += '</div>';
        });
        $('#content').html(html);
    }
});