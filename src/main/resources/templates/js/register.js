//Hiển thị avatar
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        
        reader.onload = function(e) {
        $('#avatar_img').attr('src', e.target.result);
        }
        
        reader.readAsDataURL(input.files[0]); // convert to base64 string
    }
    }

    $("#file").change(function() {
    readURL(this);
    });
    
    $('#register').click(function ()
    {
        var username = $('#username').val();
        var pass = $('#pass').val();
        var repass = $('#repass').val();
        var email = $('#email').val();
        
        // Kiểm tra dữ liệu có null hay không
        if ($.trim(username) == ''){
            alert('Bạn chưa nhập tên đăng nhập');
            return false;
        }

        if ($.trim(pass) == ''){
            alert('Bạn chưa nhập password');
            return false;
        }

        if ($.trim(repass) == ''){
            alert('Bạn chưa nhập lại mật khẩu');
            return false;
        }
    
        if ($.trim(email) == ''){
            alert('Bạn chưa nhập email');
            return false;
        }

        if($.trim(pass) != $.trim(repass))
        {
            alert('nhập lại mật khẩu phải đúng!');
            return false;
        }

        var file_data = $('#file').prop('files')[0];

        if(!file_data)
        {
            alert('Bạn chưa up avatar!');
            return false;
        }
        //lấy ra kiểu file
        var type = file_data.type;
        //Xét kiểu file được upload
        var match = ["image/gif", "image/png", "image/jpg","image/jpeg",];
        
        if (type == match[0] || type == match[1] || type == match[2]|| type == match[3]) {
            //khởi tạo đối tượng form data
            var form_data = new FormData();
            //thêm files vào trong form data
            form_data.append('username',username);
            form_data.append('pass',pass);
            form_data.append('email',email);
            form_data.append('file', file_data);
        }
        else{
            alert('Đề nghị nhập đúng định dạng file ảnh: gif, png, jpg, jpeg!');
            return false;
        }

        $.ajax({
            url: 'php/register.php',
            type: 'post',
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'text',
            data: form_data,
            success: function(result)
            {
                if(result == 'success')
                {
                    alert('Đăng ký thành công!');
                    window.location.assign('index.html');
                }
                else
                    alert(result);
            }
        });

    });