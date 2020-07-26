		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
				<p>
					<h1 class="text-capitalize"><%=request.getAttribute("Title").toString().toLowerCase()%></h1>
					<h3 class="text-capitalize">by <%=request.getAttribute("Author").toString().toLowerCase()%></h3>
					<h5 class="text-capitalize"><%=request.getAttribute("Date").toString().toLowerCase()%></h5>
				</p>
			</div>
			<div class="col-md-4"></div>
		</div>