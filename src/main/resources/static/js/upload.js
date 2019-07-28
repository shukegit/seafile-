$(function() {

	var uploadFileUrl = '/file/uploadFile';
	var downoladFileUrl = '/file/downloadfile';
	var copyFileUrl = '/file/copyfile';
	var moveFileUrl = '/file/movefile';
	var deleteFileUrl = '/file/deletefile';
	var createUserUrl = '/user/createuser';
	var logoutUrl = '/user/logout';
	var getUsersUrl = '/user/getusers';
	$("#upload").click(function() {

		var filePath = '/Musics/';
		var repositoryId = '946c9d4b-8562-4c6f-970a-245f8c6d97a8';
		var seafile = $('#seafile')[0].files[0];
		var formData = new FormData();
		formData.append('filePath', filePath);
		formData.append('repositoryId', repositoryId);
		formData.append('seafile', seafile);
		$.ajax({
			url : uploadFileUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					// 第一个data是ServiceResponse，第二个data是map对象，第三个data是map里的一个key，这个key就是请求成功后
					// seafile传递过来的token
					console.log(data.data.data);
					alert("上传成功 : " + data.message);
				} else {
					alert("上传失败 : " + data.message);
				}
			}
		});
	});

	$(".cook").click(function() {
		alert(getCookie("token"));
	});
	function getCookie(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"));
		if (arr != null)
			return unescape(arr[2]);
		return '';
	}

	$('#download').click(function() {
		// $.getJSON(downoladFileUrl, function(data) {
		// if (data.status) {
		// window.location.href = data.data.data;
		// } else {
		// alert(data.data.data);
		// }
		// });
		var repositoryId = "946c9d4b-8562-4c6f-970a-245f8c6d97a8";
		var filePath = "/Musics/jsp.pdf";
		var formData = new FormData();
		formData.append("filePath", filePath);
		formData.append("repositoryId", repositoryId);
		$.ajax({
			url : downoladFileUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					window.location.href = data.data.data;
				} else {
					alert(data.message);
				}
			}
		});
	});

	$('#copy').click(function() {
		// $.getJSON(downoladFileUrl, function(data) {
		// if (data.status) {
		// window.location.href = data.data.data;
		// } else {
		// alert(data.data.data);
		// }
		// });
		var oldRepositoryId = "946c9d4b-8562-4c6f-970a-245f8c6d97a8";
		var oldFileCopyPath = "/Musics/jsp.pdf";
		var newRepositoryId = "435919f1-1edf-49ca-845b-9a1062e9c48b";
		var newFileCopyPath = "/dsad";
		var operation = "copy";
		var formData = new FormData();
		formData.append("oldRepositoryId", oldRepositoryId);
		formData.append("oldFileCopyPath", oldFileCopyPath);
		formData.append("newRepositoryId", newRepositoryId);
		formData.append("newFileCopyPath", newFileCopyPath);
		formData.append("operation", operation);
		$.ajax({
			url : copyFileUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					alert(data.data.data);
				} else {
					alert(data.message);
				}
			}
		});
	});
	$('#move').click(function() {
		// $.getJSON(downoladFileUrl, function(data) {
		// if (data.status) {
		// window.location.href = data.data.data;
		// } else {
		// alert(data.data.data);
		// }
		// });
		var oldRepositoryId = "946c9d4b-8562-4c6f-970a-245f8c6d97a8";
		var oldFileCopyPath = "/Musics/jsp.pdf";
		var newRepositoryId = "435919f1-1edf-49ca-845b-9a1062e9c48b";
		var newFileCopyPath = "/dsad";
		var operation = "move";
		var formData = new FormData();
		formData.append("oldRepositoryId", oldRepositoryId);
		formData.append("oldFileCopyPath", oldFileCopyPath);
		formData.append("newRepositoryId", newRepositoryId);
		formData.append("newFileCopyPath", newFileCopyPath);
		formData.append("operation", operation);
		$.ajax({
			url : moveFileUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					alert(data.data.data);
				} else {
					alert(data.message);
				}
			}
		});
	});

	$('#delete').click(function() {
		// $.getJSON(downoladFileUrl, function(data) {
		// if (data.status) {
		// window.location.href = data.data.data;
		// } else {
		// alert(data.data.data);
		// }
		// });
		var repositoryId = "946c9d4b-8562-4c6f-970a-245f8c6d97a8";
		var filePath = "/Musics/jsp.pdf";
		var formData = new FormData();
		formData.append("filePath", filePath);
		formData.append("repositoryId", repositoryId);
		$.ajax({
			url : deleteFileUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					alert("删除成功：" + data.data.data);
				} else {
					alert("删除失败: " + data.message);
				}
			}
		});
	});
	$('#createUser').click(function() {

		var username = $("#username").val();
		var password = $("#password").val();
		var formData = new FormData();
		formData.append("username", username);
		formData.append("password", password);
		$.ajax({
			url : createUserUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					alert(data.message);
				} else {
					alert(data.message);
				}
			}
		});
	});

	$('#logout').click(function() {
		$.ajax({
			url : logoutUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'GET',
			data : null,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					window.location.href = '/page/login';
				} else {
					alert(data.message);
				}
			}
		});
	});
	
	$('#getUsers').click(function() {
		$.ajax({
			url : getUsersUrl,
			beforeSend: function(request) {  
				request.setRequestHeader("Authorization", getCookie("token"));  
            }, 
			type : 'GET',
			data : null,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.status) {
					change(data.data);
				} else {
					alert(data.message);
				}
			}
		});
	});
	function change(data) {
		var tempHtml = '<tr><th>序号</th><th>用户名</th><th>密码</th><th>token</th><th>创建时间</th><th>删除</th></tr>';
		data.map(function(item, index) {
			var x = "'" + item.username + "'";
			var y = "'" + item.password + "'";
			tempHtml += '<tr><th>' + (index+1) + '</th><th>' + item.username + '</th><th>' + item.password + '</th><th>'
			+ item.token + '</th><th>' + item.createTime + '</th><th><button onclick="deleteUser(' + x + ',' + y + ');">删除</button></th></tr>';	
		});
		$('.table').html(tempHtml);
	}
	
	deleteUser = function(username, password) {
		alert(username + " " + password);
	};

});