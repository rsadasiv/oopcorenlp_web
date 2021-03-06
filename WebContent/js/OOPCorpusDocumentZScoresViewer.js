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


function makeAnnotationsZScoresBarChart(targetCorpusName, aggregateName, svgName) {
	let baseUrl = "rest/api/CorpusDocumentAnnotationsZScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="+getProperties()["selectedCorpus"]+"&AggregateName="+aggregateName;
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: baseUrl
				}
			)
		).done(
				function(rawData) {
					drawVegaBarChart(rawData, svgName, "value")
				});
		
}

function getSimilarity() {
	let promise = $.Deferred();
	let paras = new Array();
	let attempts = 0;
	let baseUrl = "rest/api/CorpusDocumentAnnotationsSimilarity?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="
	$.ajax("rest/browse/Corpora")
	.then(function(data) {
		data.forEach(function(corpusName) {
			$.ajax(baseUrl+corpusName)
			.then(
				function(similarity) {
					paras.push({name: corpusName, value: similarity.value});
					attempts = attempts + 1;
					if (attempts == data.length) {
						promise.resolve(paras);
					}
				},
				function(similarity) {
					attempts = attempts + 1;
					if (attempts == data.length) {
						promise.resolve(paras);
					}
				}
			)	
		})
	})
	return promise.promise();
}

function drawSimilarity(svgName) {
	getSimilarity()
	.then(function(paras) {
		paras.sort((a, b) => (b.value - a.value));
		paras.forEach(function(similarity) {
			$(svgName).append("<p>"+similarity.name+": " +similarity.value.toFixed(10)+"</p>");	
		})
	})
}

function drawVegaBarChart(data, svgName, valueName) {
	console.log(svgName);
	console.log(valueName);
	console.log(data);
	let baseUrl = "OOPCorpusDocumentAnnotationZScoresViewer?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="+getProperties()["selectedCorpus"]+"&Annotation="
	let chartSpec = {
		  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
		  "data": {
		    "values": data
		  },
		  "title": "zScores",
		  //"width": "container",
		  //"height": "container",
		  "width": 800,
		  "mark": {"type": "bar"},
		  "transform": [
			  {"calculate": "'" + baseUrl + "' + datum.name", "as": "url" },
			  {"calculate": "datum.value", "as": "zScore" },
		],		  
		  "encoding": {
		    "y": {"field": "name", "type": "nominal", "axis": {"labelAngle": 0}, "sort": "zScore"},
		    "x": {"field": "zScore", "type": "quantitative"},
		    "href": {"field": "url", "type": "nominal"},
		    "tooltip": [
		    	{"field": "zScore", "type": "quantitative"},
		    	{"field": "documentScore", "type": "quantitative"},
		    	{"field": "corpusMin", "type": "quantitative"},
		    	{"field": "corpusMedian", "type": "quantitative"},
		    	{"field": "corpusMean", "type": "quantitative"},
		    	{"field": "corpusMax", "type": "quantitative"},
		    	{"field": "corpusStddev", "type": "quantitative"},			    	
		    ],
		  }
		};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}
