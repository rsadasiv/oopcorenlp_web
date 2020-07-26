<!-- *******************************************************************************
 * Copyright (C) 2020 Ram Sadasiv
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************** -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<link href="css/OOP/styles.css" rel="stylesheet" type="text/css" />
<title>TextOutOfPrintViewer</title>
</head>
<body>
	<div id="wrapper">
	  <div id="content-cont"> <a href="index.html"><span class="logo">	Out of Print: the short story online</span></a>
	    <div id="top-bar">
	      <h5><span><%= request.getAttribute("Title") %></span> by <%= request.getAttribute("Author") %></h5>
	      <a href="index.html">HOME</a></div>
		<div id="main-text-cont2">
			<%= request.getAttribute("Text") %>
		</div>
		<div id="top-bar">
	      <a href="index.html">HOME</a></div>
	  </div>
	</div>
</body>
</html>
