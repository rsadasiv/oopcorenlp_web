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
			   		<%=request.getParameter("Corpus").toString().toLowerCase()%>
			   	</span>
            </li>
        </ul>

		<ul class="navbar-nav ml-auto">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="compareMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Compare
				</a>
				<div class="dropdown-menu" aria-labelledby="compareMenuLink">
					<a class="dropdown-item" href="OOPCorpusCVScoresViewer?Corpus=<%=request.getParameter("Corpus")%>">Annotation cvScores</a>
					<a class="dropdown-item" href="OOPCorpusAnnotationCVScoresViewer?Corpus=<%=request.getParameter("Corpus")%>">Subannotation cvScores</a>
					<a class="dropdown-item" href="CorpusDocumentsViewer?Corpus=<%=request.getParameter("Corpus")%>">Documents</a>
				</div>
			</li>															
		</ul>
	</div>
</nav>

