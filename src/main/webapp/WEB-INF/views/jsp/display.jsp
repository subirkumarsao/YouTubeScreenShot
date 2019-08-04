<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to Spring Web MVC project</title>
</head>

<body>
    <h1>Screen shots</h1>
</body>

<c:forEach items="${images}" var="image">
   <div>
   		<img src="${image.imageUrl}">
   		<p>${image.text}</p>
   </div>
</c:forEach>
