<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<HTML>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<head>
<base href="<%=basePath%>">
<TITLE>test</TITLE>
<style type="text/css">
.prog-border {
	height: 40px;
	width: 400px;
	background: #fff;
	border: 1px solid #000;
	margin: 0;
	padding: 0;
}

.prog-bar {
	height: 35px;
	margin: 2px;
	padding: 0px;
	background: #178399;
	font-size: 10pt;
}

.align-center {
	position: fixed;
	left: 32%;
	top: 37%;
	margin-left: width/2;
	margin-top: height/2;
}

body {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 50pt;
}
</style>

</head>
<script type="text/javascript" src="js/jquery-1.11.2.js"></script> 
<script type="text/javascript" >


$(document).ready(function() {
	setTimeout("go()",3000);//定时调用
});

function go() 	{
	$.ajax({
	     type: 'POST',
	     url: 'processorWithFAJ',
	    success: function(data){

			if(data.indexOf("finished")!=-1){
				var r=data.split(",");
				var a='<%=basePath%>attachment/result_';
				a+=r[1];
				a+=".xls";
				$("#status").html("");
				$("#show_msg").html(r[2]);               
				$("#hide_div").css('display','block');    
				$("#stop").css('display','none');    
				window.myform.action=a;
			}else{
				$("#status").html(data);
				setTimeout("go()",5000);//定时调用
			}
	    },
	    dataType: 'html'

	});
} 

		


	

	

</script>

<body >

	
	
	
	<div class="align-center">
	
		<!-- 这里显示进度条 -->
		<div id="status">
			请稍等 正在处理文件...
		</div>
		
		<div id="stop">
			<form action="StopServlet"  method="post">
	   		 <input id="mysubmit" type="submit" style='font-size:40px;width: 458px;height: 80px;' value="停止程序">
	   </form>
		</div>
		
		
	</div>
		   <br>
	    <br>
	    

	
	<div class="align-center" id="hide_div" style="display:none">
	   <div id="show_msg">
	   </div>
	   


	   <form action="" id="myform" post="get">
	   		 <input id="mysubmit" type="submit" style='font-size:40px;width: 458px;height: 80px;' value="分析成功,请点击下载">
	   </form>
	</div>
</body>
</html>