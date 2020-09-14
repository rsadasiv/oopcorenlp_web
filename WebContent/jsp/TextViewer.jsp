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
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<link href="css/medium/basic.css" rel="stylesheet" type="text/css" />
<title>TextViewer</title>
<style>
   pre {
      overflow-x: auto;
      background: #fff;
      white-space: pre-wrap;
      white-space: -moz-pre-wrap;
      white-space: -pre-wrap;
      white-space: -o-pre-wrap;
      word-wrap: break-word;
   }
</style>
</head>
<body>
<jsp:include page="include/nav.jsp" />
<pre>
	<%= request.getAttribute("Text") %>
</pre>
<jsp:include page="include/footer.jsp" />
</body>
</html>
