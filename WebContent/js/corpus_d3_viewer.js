'use strict';
var properties = {};


function setProperties() {
	//properties.corpus = "Corpora/Submissions";
	//properties.textDir = "Text";
	//properties.annotationDir = "Annotations";
	properties.docId = docId;
	properties.corpus = corpus;
}

function getProperties() {
	return properties;
}

function isString (value) {
	return typeof value === 'string' || value instanceof String;
}

function isNumber (value) {
	return typeof value === 'number' || isFinite(value);
}

function isObject (value) {
	return value && typeof value === 'object' && value.constructor === Object;
}

function isArray (value) {
	return Array.isArray(value);
}

function getBaseUrl() {
	return "GetDocumentAnalysisStats?Analysis=OOPCoreNLP&Corpus="+ properties.corpus+"&Document="+ properties.docId;
}

function getCorpusAggregatesUrl(selectedAnnotation) {
	return "Corpora/Published/Annotations/CORPUS/"+selectedAnnotation+".json";
}

function getNormalizeSelection() {
	return $("#normalized").is(':checked');
}


function drawAggregateAnnotators(selectedAnnotation) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {

					let annotators = {};
					for (const annotation of Object.keys(data)) {
						if (annotation.endsWith("Aggregate")) {
							if (isObject(data[annotation])) {
								annotators[annotation] = 0;
							}
						}
					}
					let sortedAnnotators = Object.keys(annotators);
					sortedAnnotators.sort();
					for (let key of sortedAnnotators) {
						let optionNode = $("<option>");
						optionNode.attr("value", key);
						if (key == selectedAnnotation) {
							optionNode.attr("selected","selected");
						}
						optionNode.text(getAnnotationDisplayName(key));
						$("#annotators").append(optionNode);
					}
				}
		);
}




function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}


function makeCloudNoTable(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(annotationName)
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isArray(rawData["detailScores"])) {
						for (const annotation of rawData["detailScores"]) {
							data.push({"text": annotation.name, "size": annotation.normalized.scoreStats.score.normalized});
						}
						console.log(data);
						data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);
						let tdata = $.extend(true, [], data.slice(0,50));
						//console.log(data.slice(0,50));
						drawCloud(data.slice(0,50), svgName, getAnnotationDisplayName(annotationName));
						//showCloudData(tdata, "#tableViz");
					}
				});
		
}

function makeCloud(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(annotationName)
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isArray(rawData["detailScores"])) {
						for (const annotation of rawData["detailScores"]) {
							data.push({"text": annotation.name, "size": annotation.normalized.scoreStats.score.normalized});
						}
						console.log(data);
						data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);
						let tdata = $.extend(true, [], data.slice(0,50));
						//console.log(data.slice(0,50));
						drawCloud(data.slice(0,50), svgName, getAnnotationDisplayName(annotationName));
						showCloudData(tdata, "#tableViz");
					}
				});
		
}

function drawCloud(data, svgName, yLabel) {
    d3.wordcloud()
    .size([800, 400])
    .selector(svgName)
    .scale('linear')
    .spiral('rectangular')
    .words(data)
    .start();
}

function showCloudData(data, divName ) {
	$(divName).empty();	
	let tb = $('<table>');
	tb.attr("class", "table table-dark table-striped table-sm");
	tb.appendTo($(divName));
	let tbody = $('<tbody>');
	tbody.appendTo(tb);
	for (let datum of data) {
		let tr = $('<tr>');
		tr.appendTo(tbody);
		let th = $('<th>');
		th.appendTo(tr);
		th.attr("scope", "row");
		th.text(datum.text);
		let td = $('<td>');
		td.appendTo(tr);
		td.text(datum.size);
	}	
}

function showSentenceText(sentenceIdx) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(raw_data) {
					 $("#sentenceText").html(raw_data.sentences[sentenceIdx].text);
				}
		);
}

function makeDocumentBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(annotationName)
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isArray(rawData["aggregatedScores"])) {
						for (const annotation of rawData["aggregatedScores"]) {
							data.push({"name": annotation.name, "value": annotation.score.normalized});
						}
						console.log(data);
						data.sort((a, b) => (parseFloat(a.value) < parseFloat(b.value)) ? 1 : -1);
						drawBarChart(data, svgName, getAnnotationDisplayName(annotationName), showSentenceText);
					}
				});	

		
}

function drawBarChart(data, svgName, yLabel, onclick) {
	$(svgName).empty();
	let svg = d3.select(svgName),
	margin = {
		top: 20,
		right: 20,
		bottom: 30,
		left: 50
	},
	width = +svg.attr("width") - margin.left - margin.right,
	height = +svg.attr("height") - margin.top - margin.bottom,
	g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	let x = d3.scaleBand()
		.rangeRound([0, width])
		.padding(0.1).align(0);

	let y = d3.scaleLinear()
		.rangeRound([height, 0]);
	
	
	x.domain(data.map(function (d) {
		return d.name;
	}));
	y.domain([0, d3.max(data, function (d) {
				return d.value;
	})]);
		
	g.append("g")
	.attr("transform", "translate(0," + height + ")")
	.call(d3.axisBottom(x).tickValues([]));

	g.append("g")
	.call(d3.axisLeft(y))
	.append("text")
	.attr("fill", "#000")
	.attr("transform", "rotate(-90)")
	.attr("y", 6)
	.attr("dy", "0.71em")
	.attr("text-anchor", "end")
	.text(yLabel);

	g.selectAll(".bar")
	.data(data)
	.enter().append("rect")
	.attr("class", "bar")
	.attr("x", function (d) {
		return x(d.name);
	})
	.attr("y", function (d) {
		return y(d.value);
	})
	.attr("width", x.bandwidth())
	.attr("height", function (d) {
		if ((height - y(d.value)) < 0) {
			return 0;
		}
		else {
			return height - y(d.value);
		}
	})
	.on("click", function(d, i) {
		d3.select(svgName).selectAll(".bar").style("fill", "steelblue");
        d3.select(this).style("fill", "black");
        $("#sentenceText").html(d.name);
	});

}