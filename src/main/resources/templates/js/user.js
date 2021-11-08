window.sessionStorage;
if(sessionStorage.getItem("username"))
{
    var username = sessionStorage.getItem("username");
    $('#username').text(username);
    $('.basic').attr('href','project/PHP_basic.php');
    $('.mysql').attr('href','project/mysql.php');
    $('.oop').attr('href','project/oop.php');
    $.ajax({
        url: 'php/user.php',
        type: 'post',
        dataType: 'json',
        data: {
            username: username
        },
        success: function(result){
            $.each (result, function (key, item){
                $('#avatar').attr('src','images/'+item['avatar']);
                $('#email').html('Email: ' + item['email']);
                if(item['admin'] == 1)
                {
                    $('#username').html(username + '</br>(admin)');
                    $('#permission').attr('href','admin_manage.html');
                }
                else
                {
                    $('#username').html(username + '</br>(user)');
                    $('#permission').attr('href','edit.html');
                }
            });
        }
    });


}

else
{
    $('#username').html('Đăng nhập');
    $('#permission').attr('href','index.html');
    $('.dropdown-item').click(function(){
        alert('Đề nghị đăng nhập trước!');
        window.location.assign('index.html');
    });
}