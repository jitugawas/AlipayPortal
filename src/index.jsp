<html>
<body>
	<h2>Hello World!</h2>
	<%
	    out.println(new java.util.Date());
	%>

	<form method="POST" action="uploadFile" enctype="multipart/form-data">
	Merchant Transaction Id:<input type="text" name="mcTransactionId"> <br>
	Photo file to upload: <input type="file" name="file"><br> 
	Press here to upload the file!<input type="submit" value="Upload">
	</form>
	<br><br>
	
	
	---------------------------------------------------
	<form method="GET" action="getFile">
	Merchant Transaction Id:<input type="text" name="mcTransactionId"> 
	Download File <input type="submit" value="Download">
	</form>
	<br><br>
	

</body>
</html>
