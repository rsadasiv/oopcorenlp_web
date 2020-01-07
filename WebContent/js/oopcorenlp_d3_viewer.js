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
							annotators[key] = 0;
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
						if (annotation.startsWith("io") && !annotation.endsWith("Aggregate")) {
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
					let sortedTokenIdx = raw_data["io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation"]["tokenCount"]-tokenIdx;
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


//top left table
function showAggregateScore(selectedAnnotation, div) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					for (let annotation of Object.keys(data)) {
						if (annotation == selectedAnnotation) {
							if (isObject(data[annotation])) {
								//$('#aggregateScore').empty();
								div.empty();
								
								let tb = $('<table>');
								tb.attr("class", "table table-sm");
								//tb.appendTo($('#aggregateScore'));
								tb.appendTo(div);
								let tbody = $('<tbody>');
								tbody.appendTo(tb);
								let scoreProperties = ["raw", "count", "normalized"];
								//for (let prop of Object.keys(data[annotation]["scoreStats"]["score"])) {
								for (let prop of scoreProperties) {
									let tr = $('<tr>');
									tr.appendTo(tbody);
									let th = $('<th>');
									th.appendTo(tr);
									th.attr("scope", "row");
									th.text(prop);
									let td = $('<td>');
									td.appendTo(tr);
									td.text(data[annotation]["scoreStats"]["score"][prop]);
								}
								let statsProperties = ["median", "mean", "min", "max", "stddev"];
								//for (let prop of Object.keys(data[annotation]["scoreStats"]["stats"])) {
								for (let prop of statsProperties) {
									let tr = $('<tr>');
									tr.appendTo(tbody);
									let th = $('<th>');
									th.appendTo(tr);
									th.attr("scope", "row");
									th.text(prop);
									let td = $('<td>');
									td.appendTo(tr);
									td.text(data[annotation]["scoreStats"]["stats"][prop]);
								}
							}
						}
					}
				});
}

//middle data table, based on document
function showAggregateScores(selectedAnnotation, div) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					for (let annotation of Object.keys(data)) {
						if (annotation == selectedAnnotation) {
							if (isObject(data[annotation])) {
								let scoreCount = data[annotation].scoreStats.score.count;
								$.when(
										$.ajax(
											{
												dataType: "json",
												url: getCorpusAggregatesUrl(selectedAnnotation)
											}
										)
									).done(
											function(corpusData) {
								
												//$('#aggregateScores').empty();
												div.empty();
				
												for (let subAnnotationScore of data[annotation]["aggregatedScores"]) {
													let tb = $('<table>');
													tb.attr("class", "table table-sm table-bordered");
													//tb.appendTo($('#aggregateScores'));
													tb.appendTo(div);
													let thead = $('<thead>');
													
													thead.appendTo(tb);
													let headerRow = $('<tr>');
													headerRow.appendTo(thead);
													let thSubAnnotationName = $('<th>');
													thSubAnnotationName.attr("class", "table-dark");
													thSubAnnotationName.appendTo(headerRow);
													thSubAnnotationName.attr("scope", "column");
													thSubAnnotationName.text(subAnnotationScore["name"]);
													let scoreNames = ["raw", "normalized", "count"];
													let aggregateScoreNames = ["rank", "percentage", "percentile"];
													for (let prop of scoreNames) {
													//for (let prop of Object.keys(subAnnotationScore["score"])) {
														let th = $('<th>');
														th.appendTo(headerRow);
														th.attr("scope", "column");
														th.text(prop);
													}
													for (let prop of aggregateScoreNames) {
													//for (let prop of Object.keys(subAnnotationScore["aggregateScore"])) {
														let th = $('<th>');
														th.appendTo(headerRow);
														th.attr("scope", "column");
														th.text(prop);
													}
													let tbody = $('<tbody>');
													tbody.appendTo(tb);
													let scoreRow = $('<tr>');
													scoreRow.appendTo(tbody);
													let thSubAnnotationScore = $('<th>');
													thSubAnnotationScore.appendTo(scoreRow);
													thSubAnnotationScore.attr("scope", "row");
													thSubAnnotationScore.text("score");
				
													for (let prop of scoreNames) {
													//for (let prop of Object.keys(subAnnotationScore["score"])) {
														let td = $('<td>');
														td.appendTo(scoreRow);
														td.text(subAnnotationScore["score"][prop]);
													}
				
													for (let prop of aggregateScoreNames) {
													//for (let prop of Object.keys(subAnnotationScore["aggregateScore"])) {
														let td = $('<td>');
														td.appendTo(scoreRow);
														td.text(subAnnotationScore["aggregateScore"][prop]);
													}
													//showAggregateSubAnnotationScores(selectedAnnotation, subAnnotationScore["name"], tbody);
													for (let corpusSubAnnotationScore of corpusData["detailScores"]) {
														if (subAnnotationScore.name == corpusSubAnnotationScore.name) {
															let rows = ["median", "mean", "min", "max", "stddev"];
															let columns = ["raw", "normalized", "count", "rank", "percentage", "percentile"];
															for (let rowName of rows) {
																let tr = $('<tr>');
																tr.appendTo(tbody);
																let th = $('<th>');
																th.appendTo(tr);
																th.attr("scope", "column");
																th.text(rowName);
																for (let columnName of columns) {
																	let td = $('<td>');
																	td.appendTo(tr);
																	td.text(corpusSubAnnotationScore[columnName]["scoreStats"]["stats"][rowName]);
																}
															}
														}
													}
												}
											});
								}
							}
						}
					});
}

//middle data table, based on corpus
//get the number of entries from the document
//sort the corpus subAnnotation detailScores by normalized.scoreStats.score.normalized 
function showCorpusAggregateScores(selectedAnnotation, div) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					for (let annotation of Object.keys(data)) {
						if (annotation == selectedAnnotation) {
							if (isObject(data[annotation])) {
								let scoreCount = data[annotation].scoreStats.score.count;
								$.when(
										$.ajax(
											{
												dataType: "json",
												url: getCorpusAggregatesUrl(selectedAnnotation)
											}
										)
									).done(
											function(corpusData) {
												div.empty();
												//sort
												let sortedDetailScores = [];
												for (let subAnnotationScore of corpusData["detailScores"]) {
													sortedDetailScores.push(subAnnotationScore);
												}
												sortedDetailScores.sort((a, b) => (parseFloat(a.normalized.scoreStats.score.normalized) < parseFloat(b.normalized.scoreStats.score.normalized) ? 1 : -1));
												for (let subAnnotationScore of sortedDetailScores.slice(0, scoreCount)) {
													let tb = $('<table>');
													tb.attr("class", "table table-sm table-bordered");
													//tb.appendTo($('#aggregateScores'));
													tb.appendTo(div);
													let thead = $('<thead>');
													thead.appendTo(tb);
													let headerRow = $('<tr>');
													headerRow.appendTo(thead);
													
													let thSubAnnotationName = $('<th>');
													thSubAnnotationName.attr("class", "table-dark");
													thSubAnnotationName.appendTo(headerRow);
													thSubAnnotationName.attr("scope", "column");
													thSubAnnotationName.text(subAnnotationScore["name"]);
													
													let scoreProperties = ["raw", "normalized", "count", "rank", "percentage", "percentile"];
													//let scoreProperties = ["z", "percentile", "percentage", "rank", "count", "normalized", "raw"];
													for (let prop of scoreProperties) {
														let th = $('<th>');
														th.appendTo(headerRow);
														th.attr("scope", "column");
														th.text(prop);
													}
													

													
													let tbody = $('<tbody>');
													tbody.appendTo(tb);
													let scoreRow = $('<tr>');
													scoreRow.appendTo(tbody);
													
													let thSubAnnotationScore = $('<th>');
													thSubAnnotationScore.appendTo(scoreRow);
													thSubAnnotationScore.attr("scope", "row");
													thSubAnnotationScore.text("score");
													//find this subAnnotation name in data[annotation].aggregatedScores
													for (let aggregatedScore of data[annotation]["aggregatedScores"]) {

														if (aggregatedScore.name == subAnnotationScore.name) {
															//console.log("matched");
															//console.log(aggregatedScore);
															//let scoreNames = ["count", "normalized", "raw"];
															let scoreNames = ["raw", "normalized", "count"];
															for (let prop of scoreNames) {
																let td = $('<td>');
																td.appendTo(scoreRow);
																td.text(aggregatedScore["score"][prop]);
															}
															//let aggregateScoreNames = ["z","percentile","percentage","rank"];
															let aggregateScoreNames = ["rank", "percentage", "percentile"];
															for (let prop of aggregateScoreNames) {
																let td = $('<td>');
																td.appendTo(scoreRow);
																td.text(aggregatedScore["aggregateScore"][prop]);
															}
														}
													}


													//showAggregateSubAnnotationScores(selectedAnnotation, subAnnotationScore["name"], tbody);
													//let rows = ["total", "median", "mean", "min", "max", "stddev"];
													let rows = ["median", "mean", "min", "max", "stddev"];
													//let columns = ["z","percentile","percentage","rank","count","normalized","raw"];
													let columns = ["raw", "normalized", "count", "rank", "percentage", "percentile"];
													for (let rowName of rows) {
														let tr = $('<tr>');
														tr.appendTo(tbody);
														let th = $('<th>');
														th.appendTo(tr);
														th.attr("scope", "column");
														th.text(rowName);
														
														for (let columnName of columns) {
															let td = $('<td>');
															td.appendTo(tr);
															if (rowName == "total") {
																if (columnName == "rank" || columnName == "percentage" || columnName == "percentile") {
																	td.text("");
																}
																else {
																	td.text(subAnnotationScore[columnName]["scoreStats"]["score"]["normalized"]);
																}
															}
															else {
																td.text(subAnnotationScore[columnName]["scoreStats"]["stats"][rowName]);
															}
														}

													}

												}
											});

							}
						}
					}
				});
}




//top right table, based on corpus
function showCorpusAggregateScore(selectedAnnotation, div) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(selectedAnnotation)
				}
			)
		).done(
				function(data) {
//					$('#corpusAggregateScore').empty();
//					$('#corpusAggregateScores').empty();
//					$('#corpusAggregateScoresDelta').empty();
					div.empty();
					
					let tb = $('<table>');
					tb.attr("class", "table table-sm");
					//tb.appendTo($('#corpusAggregateScore'));
					tb.appendTo(div);
					let tbody = $('<tbody>');
					tbody.appendTo(tb);
					let scoreProperties = ["raw", "count", "normalized"];
					for (let prop of scoreProperties) {
					//for (let prop of Object.keys(data["scoreStats"]["score"])) {
						let tr = $('<tr>');
						tr.appendTo(tbody);
						let td = $('<td>');
						td.appendTo(tr);
						td.text(data["scoreStats"]["score"][prop]);
						let th = $('<th>');
						th.appendTo(tr);
						th.attr("scope", "row");
						th.text(prop);

					}
					let statsProperties = ["median", "mean", "min", "max", "stddev"];
					for (let prop of statsProperties) {
					//for (let prop of Object.keys(data["scoreStats"]["stats"])) {
						let tr = $('<tr>');
						tr.appendTo(tbody);
						let td = $('<td>');
						td.appendTo(tr);
						td.text(data["scoreStats"]["stats"][prop]);
						let th = $('<th>');
						th.appendTo(tr);
						th.attr("scope", "row");
						th.text(prop);

					}
				});
}

//for a given annotation/subannotation in the document, add the matching corpus scores to the table
function showAggregateSubAnnotationScores(selectedAnnotation, subAnnotationName, tbody) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(selectedAnnotation)
				}
			)
		).done(
				function(data) {
					for (let subAnnotationScore of data["detailScores"]) {
						if (subAnnotationScore.name == subAnnotationName) {
							let rows = ["median", "mean", "min", "max", "stddev"];
							let columns = ["raw", "normalized", "count", "rank", "percentage", "percentile"];
							for (let rowName of rows) {
								let tr = $('<tr>');
								tr.appendTo(tbody);
								let th = $('<th>');
								th.appendTo(tr);
								th.attr("scope", "column");
								th.text(rowName);
								for (let columnName of columns) {
									let td = $('<td>');
									td.appendTo(tr);
									td.text(subAnnotationScore[columnName]["scoreStats"]["stats"][rowName]);
								}
							}
						}
					}
				});
}

//for a given annotation/subannotation in the corpus, add the matching document scores to the table
function showCorpusAggregateSubAnnotationScores(selectedAnnotation, subAnnotationName, tbody) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(selectedAnnotation)
				}
			)
		).done(
				function(data) {
					for (let subAnnotationScore of data["detailScores"]) {
						if (subAnnotationScore.name == subAnnotationName) {
							let rows = ["median", "mean", "min", "max", "stddev"];
							let columns = ["raw", "normalized", "count", "rank", "percentage", "percentile"];
							for (let rowName of rows) {
								let tr = $('<tr>');
								tr.appendTo(tbody);
								let th = $('<th>');
								th.appendTo(tr);
								th.attr("scope", "column");
								th.text(rowName);
								for (let columnName of columns) {
									let td = $('<td>');
									td.appendTo(tr);
									td.text(subAnnotationScore[columnName]["scoreStats"]["stats"][rowName]);
								}
							}
						}
					}
				});
}


function showCorpusAggregateScoresDelta(selectedAnnotation, subAnnotationScore) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getCorpusAggregatesUrl(selectedAnnotation)
				}
			)
		).done(
				function(data) {
					for (let subAnnotationAggregateScore of data["detailScores"]) {
						if (subAnnotationScore.name == subAnnotationAggregateScore.name) {
//							if (isObject(data[annotation])) {


								//for (let subAnnotationScore of data[annotation]["aggregatedScores"]) {
									let tb = $('<table>');
									tb.attr("class", "table table-sm table-bordered");
									tb.appendTo($('#corpusAggregateScores'));
									let tbody = $('<tbody>');
									tbody.appendTo(tb);
									let tr = $('<tr>');
									tr.appendTo(tbody);
									let th = $('<th>');
									th.appendTo(tr);
									th.attr("scope", "row");
									th.text(subAnnotationAggregateScore["name"]);
									let td = $('<td>');
									td.appendTo(tr);

//									for (let prop of Object.keys(subAnnotationScore["normalized"]["scoreStats"]["score"])) {
//										let tr = $('<tr>');
//										tr.appendTo(tbody);
//										let th = $('<td>');
//										th.appendTo(tr);
//										th.attr("scope", "row");
//										th.text(prop);
//										let td = $('<td>');
//										td.appendTo(tr);
//										td.text(subAnnotationScore["normalized"]["scoreStats"]["score"][prop]);
//									}
									for (let prop of Object.keys(subAnnotationAggregateScore[aggregateName]["scoreStats"]["stats"])) {
										let tr = $('<tr>');
										tr.appendTo(tbody);
										let th = $('<td>');
										th.appendTo(tr);
										th.attr("scope", "row");
										th.text(prop);
										let td = $('<td>');
										td.appendTo(tr);
										td.text(subAnnotationScore[aggregateName]["scoreStats"]["stats"][prop]);
										
									}
									//spacer rows
									let tr1 = $('<tr>');
									tr1.appendTo(tbody);
									let th1 = $('<td>');
									th1.appendTo(tr1);
									th1.attr("scope", "row");
									th1.text('&nbsp;');
									
									let tr2 = $('<tr>');
									tr2.appendTo(tbody);
									let th2 = $('<td>');
									th2.appendTo(tr2);
									th2.attr("scope", "row");
									th2.text('&nbsp;');
								//}
							//}
						}
					}
				});
}

function showSubAnnotationScores(subAnnotationIdx) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					for (let dannotation of Object.keys(data)) {
						if (annotation == dannotation) {
							if (isObject(data[annotation])) {

								let idx = 0;
								for (let subAnnotation of data[annotation]["aggregateScores"]) {
									console.log(idx + " : " + subAnnotationIdx);
									console.log(data[annotation]["aggregateScores"]);
									if (idx == subAnnotationIdx) {

										$('#subAnnotationScores').empty();
										
										let tb = $('<table>');
										tb.attr("class", "table table-sm");
										tb.appendTo($('#subAnnotationScores'));
										let tbody = $('<tbody>');
										tbody.appendTo(tb);
										let properties = ["name", "raw", "normalized", "rank", "percentile", "percentage"];
										for (let prop of properties) {
											let tr = $('<tr>');
											tr.appendTo(tbody);
											let th = $('<th>');
											th.appendTo(tr);
											th.attr("scope", "row");
											th.text(prop);
											let td = $('<td>');
											td.appendTo(tr);
											td.text(subAnnotation[prop]);
										}
									}
									idx++;
								}

							}
						}
					}
				});
}



function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}


//&Annotation=io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation
function makeSentenceBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl() + "&Scope=Sentences&Annotation="+annotationName+(getNormalizeSelection()?"&Aggregate=Normalized":"")
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
					url: getBaseUrl() + "&Scope=Tokens&Annotation="+annotationName
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
									let tokenCount = raw_data["io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation"]["tokenCount"];
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
									//console.log(data);
									drawTokensBarChart(data, svgName, getAnnotationDisplayName(annotationName), showSentenceTextForTokenId);
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
					url: getBaseUrl() + "&Scope=Tokens&Annotation="+annotationName
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
									let tokenCount = raw_data["io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation"]["tokenCount"];
									let data = [];
									for (let [key, val] of rawData[annotationName].entries()) {
				//						if (key == scoreName) {
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
				//						}
				//						else {
				//							data.push({"name": scoreName, "value": 0});
				//						}
									}
									//console.log(data);
									drawTokensBarChart(data, svgName, scoreName, showSentenceTextForTokenId);
								})
							});
					
		
}

function makeAggregateBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(rawData) {
					let subAnnotationAttribute = "normalized";
					let data = [];
					for (let annotation of Object.keys(rawData)) {

						if (annotation == annotationName) {
							if (isObject(rawData[annotation])) {
								for (let subAnnotation of rawData[annotationName]["aggregateScores"]) {
									data.push({"name": subAnnotation["name"], "value": subAnnotation[subAnnotationAttribute]});
								}
							}
						}
					}
					drawBarChart(data, svgName, getAnnotationDisplayName(annotationName), showSubAnnotationScores);
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
		if ((height - y(d.value)) < 0) {
			return 0;
		}
		else {
			return height - y(d.value);
		}
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
