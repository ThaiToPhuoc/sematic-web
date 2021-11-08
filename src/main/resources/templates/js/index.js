$('#continue').click(function(){
	window.sessionStorage;
	sessionStorage.setItem("username","");
});

$('#login').click(function ()
		{
		
			var username = $('#username').val();
			var pass = $('#pass').val();
		
			// Kiểm tra dữ liệu có null hay không
			if ($.trim(username) == ''){
				alert('Bạn chưa nhập tên đăng nhập');
				return false;
			}
		
			if ($.trim(pass) == ''){
				alert('Bạn chưa nhập password');
				return false;
			}
		
			$.ajax({
				url : 'php/login_check.php',
				type : 'post',
				dataType : 'text',
				data : {
					username : username,
					pass : pass
				},
				success : function (result)
				{
					if(result == 'success!')
					{
						window.sessionStorage;
						sessionStorage.setItem("username", username);
						window.location.assign('User.html');
					}
					else
					{
						alert(result);
					}
				}
			});
		
			return false;
		});