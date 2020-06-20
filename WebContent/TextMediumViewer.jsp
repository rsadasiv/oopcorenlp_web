<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%= request.getAttribute("Title") %> by <%= request.getAttribute("Author") %>; Online Short Story; Out of Print Magazine; <%= request.getAttribute("Date") %>.</title>
<meta name="description" content="">
<meta name="keywords" content="<%= request.getAttribute("Author") %>, <%= request.getAttribute("Title") %>, short story online, fiction, magazine.">
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" 
	integrity="sha256-rByPlHULObEjJ6XQxW/flG2r+22R5dKiAoef+aXWfik=" 
	crossorigin="anonymous" />
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.theme.min.css" 
	integrity="sha256-AjyoyaRtnGVTywKH/Isxxu5PXI0s4CcE0BzPAX83Ppc=" 
	crossorigin="anonymous" />
<link rel="apple-touch-icon" sizes="180x180" href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="images/favicon-16x16.png">
<link rel="manifest" href="images/site.webmanifest">
<style>
.logo {
	width: 311px;
	height: 68px;
	display: block;
	text-indent:-6000px;
	margin: 25px auto 59px auto;
	cursor: pointer;
}
p {    
    --x-height-multiplier: 0.375;
    --baseline-multiplier: 0.17;
    font-family: Georgia,Cambria,"Times New Roman",Times,serif;
    letter-spacing: .01rem;
    font-weight: 400;
    font-style: normal;
    font-size: 21px;
    line-height: 1.58;
    letter-spacing: -.003em;
    color: rgba(0,0,0,.84);
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
}
</style>
</head>
<body>
<a href="index.html"><img src="images/OOP/logo.png" class="logo" /></a>
<div class="container">
  <div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
	<p>
		<h1 class="text-capitalize"><%= request.getAttribute("Title").toString().toLowerCase() %></h1>
		<h3 class="text-capitalize">by <%= request.getAttribute("Author").toString().toLowerCase() %></h3>
		<h5 class="text-capitalize"><%= request.getAttribute("Date").toString().toLowerCase() %></h5>
	</p>
	<p>&nbsp;</p>
	<%= request.getAttribute("Text") %>
	</div>
	</div class="col-md-2"></div>
  </div>
</div>


</body></html>