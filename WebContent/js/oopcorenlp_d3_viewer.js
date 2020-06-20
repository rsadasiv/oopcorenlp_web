/*******************************************************************************
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
 ******************************************************************************/
'use strict';
var properties = {};


function setProperties() {
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
	return "Corpora/"+corpus+"/OOP_"+docId+".json";
}

function drawSentenceAnnotators(selectedAnnotation) {
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
					for (const sentence of data.sentences) {
						for (let [key, val] of Object.entries(sentence)) {
							if (!key.startsWith("text") && !annotation.startsWith("tokens")) {
								annotators[key] = 0;
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

function drawTokenAnnotators(selectedAnnotation) {
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
					for (const sentence of data.sentences) {
						for (const token of sentence.tokens) {
							for (let [key, val] of Object.entries(token)) {
								annotators[key] = 0;
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

function drawDocumentAnnotators(selectedAnnotation) {
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
						if (!annotation.startsWith("metadata") && !annotation.startsWith("sentences") && !annotation.startsWith("corefs") && !annotation.startsWith("quotes")) {
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

function showSentenceTextForTokenId(tokenIdx) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(raw_data) {
					let sortedTokenIdx = raw_data["OOPTokenCountAnnotation"]-tokenIdx;
					console.log("tokenIdx: " + tokenIdx);
					console.log("sortedTokenIdx: " + sortedTokenIdx)
					let currentTokenId = 0;
					for (const sentence of raw_data.sentences) {
						if (tokenIdx < (currentTokenId+sentence.tokens.length)) {
							$("#sentenceText").html(sentence.text);
							return;
						}
						currentTokenId=currentTokenId+sentence.tokens.length;
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


function makeSentenceBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "GetDocumentAnalysisScores?Corpus="+ corpus+"&Document="+ docId + "&Scope=SentencesAnnotation&Annotation="+annotationName
				}
			)
		).done(
				function(rawData) {
					let data = [];
					for (let [index, val] of rawData[annotationName].entries()) {
						if (isNumber(val) || isString(val)) {
							data.push({"name": index, "value": val});
						}
						else {
							data.push({"name": index, "value": 0});
						}
					}
					drawBarChart(data, svgName, getAnnotationDisplayName(annotationName), showSentenceText);
				});
		
}

function makeTokenBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "GetDocumentAnalysisScores?Corpus="+ corpus+"&Document="+ docId + "&Scope=TokensAnnotation&Annotation="+annotationName
				}
			)
		).done(
				function(rawData) {
					$.when(
							$.ajax(
								{
									dataType: "json",
									url: getBaseUrl()
								}
							)
						).done(
								function(raw_data) {
									let tokenCount = raw_data["OOPTokenCountAnnotation"];
									let data = [];
									for (let [index, val] of rawData[annotationName].entries()) {

										if (isNumber(val) || isString(val)) {
											
											data.push({"name": tokenCount-index, "value": val});
										}
										else if (isObject(val)) {
											let s = 0;
											for(let key in val){
												if (val[key] && isNumber(val[key])) {
													s = (s + Number(val[key]));
												}
											}
											
											data.push({"name": tokenCount-index, "value": s});
										}
										else {
											
											data.push({"name": tokenCount-index, "value": 0});
										}
				
									}
									console.log("makeTokenBarChart");
									console.log(data);
									drawTokensBarChart(JSON.parse(JSON.stringify(data)), svgName, getAnnotationDisplayName(annotationName), showSentenceTextForTokenId);
								})
				});
		
}

function makeTokenScoreBarCharts(annotationName, svgName) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					//need to sort this list
					//console.log(data[annotationName]);
					let list = Object.keys(data[annotationName]);
					list.sort((a, b) => (parseFloat(data[annotationName][a]) < parseFloat(data[annotationName][b])) ? 1 : -1);
					//console.log(list);
					let idx = 2;
					list.slice(0,5).forEach(function(key, index) {
						makeTokenScoreBarChart(annotationName, key, svgName + "_" + idx);
						idx++;
					});
				}
			);
}

function makeTokenScoreBarChart(annotationName, scoreName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "GetDocumentAnalysisScores?Corpus="+ corpus+"&Document="+ docId + "&Scope=TokensAnnotation&Annotation="+annotationName+"&Subannotation="+scoreName
				}
			)
		).done(
				function(rawData) {
					$.when(
							$.ajax(
								{
									dataType: "json",
									url: getBaseUrl()
								}
							)
						).done(
								function(raw_data) {
									let tokenCount = raw_data["OOPTokenCountAnnotation"];
									let data = [];
									for (let [key, val] of rawData[annotationName].entries()) {
										//if (key == scoreName) {
											if (isNumber(val) || isString(val)) {
												data.push({"name": tokenCount-key, "value": val});
											}
											else if (isObject(val)) {
												let s = 0;
												for(let key in val){
													if (key == scoreName) {
														if (val[key] && isNumber(val[key])) {
															s = (s + Number(val[key]));
														}
													}
												}
												data.push({"name": tokenCount-key, "value": s});
											}
											else {
												data.push({"name": tokenCount-key, "value": 0});
											}
										//}
										//else {
										//	data.push({"name": scoreName, "value": 0});
										//}
									}
									console.log(data);
									drawTokensBarChart(JSON.parse(JSON.stringify(data)), svgName, scoreName, showSentenceTextForTokenId);
								})
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
        onclick(i);
	});

}

function drawTokensBarChart(data, svgName, yLabel, onclick) {
	console.log(data);
//    data = data.sort(function (a, b) {
//        return d3.ascending(Number(a.value), Number(b.value));
//    });
//    console.log(data);
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
		return d.value;
	}));
	y.domain([0, d3.max(data, function (d) {
				return d.name;
	})]);


	
	g.append("g")
	.attr("transform", "translate(0," + height + ")")
	.call(d3.axisBottom(x).tickValues([]));

	g.append("g")
	.call(d3.axisLeft(y).tickValues([]))
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
		return x(d.value);
	})
	.attr("y", function (d) {
		return y(d.name);
	})
	.attr("width", x.bandwidth())
	.attr("height", function (d) {
		return d.value;
	});
	g.selectAll(".bar")
	.on("click", function(d, i) {
		d3.select(svgName).selectAll(".bar").style("fill", "steelblue");
        d3.select(this).style("fill", "black");
        //onclick(d3.select(this).attr("y"));
        onclick(i);
	});

}

function makeCloudNoTable(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isObject(rawData[annotationName])) {
						for (const annotation of Object.keys(rawData[annotationName])) {
						//for (let [key, val] of rawData[annotationName].entries()) {
							if (isNumber(rawData[annotationName][annotation]) || isString(rawData[annotationName][annotation])) {
								data.push({"text": annotation, "size": rawData[annotationName][annotation]});
							}
							else if (isArray(rawData[annotationName][annotation])) {
								data.push({"text": annotation, "size": rawData[annotationName][annotation].length});
							}
							else {
								data.push({"text": annotation, "size": 0});
							}
						}
						//console.log(data);
						data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);
						let tdata = $.extend(true, [], data.slice(0,10));
						//console.log(data.slice(0,50));
						drawSmallCloud(data.slice(0,10), svgName, getAnnotationDisplayName(annotationName));
						//showCloudData(tdata, "#tableViz");
					}
				});
		
}

function makeCloud(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isObject(rawData[annotationName])) {
						for (const annotation of Object.keys(rawData[annotationName])) {
						//for (let [key, val] of rawData[annotationName].entries()) {
							if (isNumber(rawData[annotationName][annotation]) || isString(rawData[annotationName][annotation])) {
								data.push({"text": annotation, "size": rawData[annotationName][annotation]});
							}
							else if (isArray(rawData[annotationName][annotation])) {
								data.push({"text": annotation, "size": rawData[annotationName][annotation].length});
							}
							else {
								data.push({"text": annotation, "size": 0});
							}
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

function drawSmallCloud(data, svgName, yLabel) {
    d3.wordcloud()
    .size([400, 200])
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
