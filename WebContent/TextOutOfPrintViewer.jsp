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
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%= request.getAttribute("Title") %> by <%= request.getAttribute("Author") %>; Online Short Story; Out of Print Magazine; <%= request.getAttribute("Date") %>.</title>
<meta name="description" content="">
<meta name="keywords" content="<%= request.getAttribute("Author") %>, <%= request.getAttribute("Title") %>, short story online, fiction, magazine.">
<link href="css/OOP/styles.css" rel="stylesheet" type="text/css" />
<link rel="apple-touch-icon" sizes="180x180" href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="images/favicon-16x16.png">
<link rel="manifest" href="images/site.webmanifest">
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


</body></html>
