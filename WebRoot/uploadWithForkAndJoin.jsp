<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/fileinput.css" media="all" rel="stylesheet" type="text/css" />

<script src="js/jquery-1.11.2.js"></script>
<script src="js/fileinput.min.js" type="text/javascript"></script>
<script src="js/fileinput_locale_zh.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>

</head>

<body>

	<div class="container kv-main" style=" width: 830px;height: 300px;margin-top: 200px;">

		<form enctype="multipart/form-data">
		<c:if test="${empty loopCount}"><font size="15" color="black">默认循环次数:8</font>
		</c:if>
		<c:if test="${!empty loopCount}"><font size="15" color="black">当前循环次数:${loopCount}</font>
		</c:if>

			<input id="file-1" class="file" type="file"  data-min-file-count="1"> <br>
		</form>

		<hr>

		<hr>
		<br>
	</div>
</body>
</html>
<script>
	$("#file-1").fileinput({
		language : 'zh',
		uploadUrl : 'uploadWithForkAndJoin', // you must set a valid URL here else you will get an error
		allowedFileExtensions : [ 'xls', 'jpg', 'png', 'gif' ],
	//	maxFileCount : 3, //同时最多上传3个文件
		//allowedFileTypes: ['image', 'video', 'flash'],  这是允许的文件类型 跟上面的后缀名还不是一回事
		slugCallback : function(filename) {
			return filename.replace('(', '_').replace(']', '_');
		}
	});

	$("#file-1").on("fileuploaded", function(event, data, previewId, index) {
		top.location.href = "processorWithFAJ.jsp";
	});
</script>