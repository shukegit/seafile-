$(function() {
	var loginUrl = '/user/login';
	
	var ipUrl = '/user/'
	
	
	
	
	
	
	
	$('#login').click(function() {
		var username = $('#username').val();
		var password = $('#password').val();
		var formData = new FormData();
		formData.append('username', username);
		formData.append('password', password);
		$.ajax({
			url : loginUrl, 
			beforeSend: function(request) {  
				request.setRequestHeader("ip", "192.10.1.2");  
            },
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				console.log("返回状态：" + data.status);
				if(data.status) {
					console.log("返回码：" + data.data.data);
					//第一个data是ServiceResponse，第二个data是map对象，第三个data是map里的一个key，这个key就是请求成功后
					//seafile传递过来的token
//					var arr = JSON.parse(data.data.data);
//					console.log(arr.token);
					setCookie("token",data.data.data);
					alert("登录成功！您在其它设备的登录账号将被迫下线");
					window.location.href = '/page/upload';
				} else {
					alert("登录失败:" + data.message);
				}
			}
		});
	});
	
	function setCookie(cname,cvalue,exdays)
	{
	  var d = new Date();
	  d.setTime(d.getTime()+(exdays*24*60*60*1000));
	  var expires = "expires="+d.toGMTString();
	  document.cookie = cname + "=" + cvalue + "; " + expires;
	}
	
	

	
	
});