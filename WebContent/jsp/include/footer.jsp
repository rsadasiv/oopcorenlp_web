<nav class="navbar-light bg-light" aria-label="breadcrumb">
	<ol class="breadcrumb  navbar-light bg-light">
		<li class="breadcrumb-item navbar-light bg-light"><a href="index.html">Home</a></li>
		<li class="breadcrumb-item navbar-light bg-light"><a href="CorpusDocumentsViewer?Corpus=<%=request.getParameter("Corpus")%>"><%=request.getParameter("Corpus")%></a></li>
		<li class="breadcrumb-item navbar-light bg-light" aria-current="page"><%=request.getParameter("Document")%></li>
	</ol>
</nav>