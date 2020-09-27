<nav class="navbar navbar-expand-lg navbar-light bg-light" style="padding-right: 100px">
	<a class="navbar-brand" href="index.html">
		<img src="images/favicon-32x32.png" width="32" height="32" alt="Home">
	</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div class="collapse navbar-collapse" id="navbarNavDropdown">
		<ul class="navbar-nav nav-pills">
            <li class="nav-item">
				<span class="navbar-brand navbar-text text-capitalize">
			   		<%=request.getAttribute("Title").toString().toLowerCase()%>, <%=request.getAttribute("Author").toString().toLowerCase()%>
			   	</span>
            </li>
        </ul>

		<ul class="navbar-nav ml-auto">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="metadataMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Metadata
				</a>
				<div class="dropdown-menu" aria-labelledby="metadataMenuLink">
		   			<span class="dropdown-item" id="corpusDocumentId">DocID: <%=request.getParameter("Document")%></span>
		   			<span class="dropdown-item" id="corpusDocumentTitle">Title: <%=request.getAttribute("Title").toString()%></span>
		   			<span class="dropdown-item" id="corpusDocumentAuthor">Author: <%=request.getAttribute("Author").toString()%></span>
		   			<span class="dropdown-item" id="corpusDocumentDate">Date: <%=request.getAttribute("Date").toString()%></span> 
		 			<a class="dropdown-item" target="_viewer" href="rest/browse/Corpora/<%=request.getParameter("Corpus")%>/<%=request.getParameter("Document")%>/PIPELINE">Pipeline info</a>
				</div>
			</li>		
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="readMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Read
				</a>
				<div class="dropdown-menu" aria-labelledby="readMenuLink">
		   			<a class="dropdown-item" href="TextViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Plain Text</a>
					<a class="dropdown-item" target="_blank" href="TextOutOfPrintViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Out Of Print</a>
					<a class="dropdown-item" href="TextMediumViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Medium</a>
					<a class="dropdown-item" target="_blank" href="Corpora/<%=request.getParameter("Corpus")%>/POLLY_<%=request.getParameter("Document")%>.mp3">Audio</a>
				</div>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="interactMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Interact
				</a>
				<div class="dropdown-menu" aria-labelledby="interactMenuLink">
					<a class="dropdown-item" href="OOPPullQuotesViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Pull Quotes</a>
					<a class="dropdown-item" href="OOPActorsViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Actors</a>		
					<a class="dropdown-item" href="OOPFactCheckerViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Fact Checker</a>
					<a class="dropdown-item" href="OOPEditorViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Editor</a>
				</div>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="visualizeMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Visualize
				</a>
				<div class="dropdown-menu" aria-labelledby="visualizeMenuLink">
					<a class="dropdown-item" href="OOPStoryArcViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Story Arc</a>
					<a class="dropdown-item" href="OOPCloudViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Word Clouds</a>
					<a class="dropdown-item" href="OOPStreamViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Sentences</a>
					<a class="dropdown-item" href="OOPStreamViewerTokens?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">Tokens</a>
				</div>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="analyzeMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Analyze
				</a>
				<div class="dropdown-menu" aria-labelledby="analyzeMenuLink">
					<a class="dropdown-item" href="OOPDocumentViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">OOPCoreNLP</a>
					<a class="dropdown-item" target="_viewer" href="rest/browse/Corpora/<%=request.getParameter("Corpus")%>/<%=request.getParameter("Document")%>/OOP">OOPCoreNLP json</a>
					<a class="dropdown-item" href="StanfordBratViewer?Corpus=<%=request.getParameter("Corpus")%>&Document=<%=request.getParameter("Document")%>">StanfordCoreNLP</a>
					<a class="dropdown-item" target="_viewer" href="rest/browse/Corpora/<%=request.getParameter("Corpus")%>/<%=request.getParameter("Document")%>/STANFORD">StanfordCoreNLP json</a>	
				</div>
			</li>												
		</ul>
	</div>
</nav>

