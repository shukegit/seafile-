$(function() {
	var loginUrl = '/user/login';
	
	$('#log').click(function() {
		var username = $('#usn').val();
		var password = $('#psw').val();
		var formData = new FormData();
		formData.append('username', username);
		formData.append('password', password);
		$.ajax({
			url : loginUrl, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				console.log("返回状态：" + data.status);
				if(data.status) {
					//第一个data是ServiceResponse，第二个data是map对象，第三个data是map里的一个key，这个key就是请求成功后
					//seafile传递过来的token
//					console.log(data.data.data);
//					var arr = JSON.parse(data.data.data);
//					console.log(arr.token);
//					setCookie("token",arr.token, 1);
					window.location.href = '/page/upload';
				} else {
					alert("登录失败:" + data.message);
				}
			}
		});
	});
}