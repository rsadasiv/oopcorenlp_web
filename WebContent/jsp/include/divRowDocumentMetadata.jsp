		<div class="row">
			<div class="col-md-4 text-center"></div>
			<div class="col-md-4 text-center">
					<h1 class="text-capitalize" id="corpusDocumentTitle"><%=request.getAttribute("Title").toString().toLowerCase()%></h1>
					<h3 class="text-capitalize" id="corpusDocumentAuthor">by <%=request.getAttribute("Author").toString().toLowerCase()%></h3>
					<h5 class="text-capitalize" id="corpusDocumentDate"><%=request.getAttribute("Date").toString().toLowerCase()%></h5>
			</div>
			<div class="col-md-4"></div>
		</div>