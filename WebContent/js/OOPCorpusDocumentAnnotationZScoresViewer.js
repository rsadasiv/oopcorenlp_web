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


function makeAnnotationsZScoresBarChart(targetCorpusName, annotation, aggregateName, svgName) {
	let baseUrl = "rest/api/CorpusDocumentAnnotationSubannotationsZScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="+getProperties()["selectedCorpus"]+"&Annotation="+annotation+"&AggregateName="+aggregateName;
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: baseUrl
				}
			)
		).done(
				function(rawData) {
					drawVegaBarChart(rawData, annotation, svgName, "value")				
				});	
}

function getSimilarity(annotation) {
	let promise = $.Deferred();
	let paras = new Array();
	let attempts = 0;
	let baseUrl = "rest/api/CorpusDocumentAnnotationSubannotationsSimilarity?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="
	$.ajax("rest/browse/Corpora")
	.then(function(data) {
		data.forEach(function(corpusName) {
			$.ajax(baseUrl+corpusName+"&Annotation="+annotation)
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
					if (attempts.length == data.length) {
						promise.resolve(paras);
					}
				}
			)	
		})
	})
	return promise.promise();
}

function drawSimilarity(annotation, svgName) {
	getSimilarity(annotation)
	.then(function(paras) {
		paras.sort((a, b) => (b.value - a.value));
		paras.forEach(function(similarity) {
			$(svgName).append("<p>"+similarity.name+": " +similarity.value.toFixed(10)+"</p>");	
		})
	})
}

function drawVegaBarChart(data, annotation, svgName, valueName) {
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
		  "encoding": {
		    "y": {"field": "name", "type": "nominal", "axis": {"labelAngle": 0}, "sort": "value"},
		    "x": {"field": "value", "type": "quantitative"},
		    "tooltip": [
		    	{"field": "name", "type": "nominal"},
		    	{"field": "value", "type": "quantitative"},
		    	{"field": "documentScore", "type": "quantitative"},
		    	{"field": "corpusMin", "type": "quantitative"},
		    	{"field": "corpusMedian", "type": "quantitative"},
		    	{"field": "corpusMean", "type": "quantitative"},
		    	{"field": "corpusMax", "type": "quantitative"},
		    	{"field": "corpusStddev", "type": "quantitative"},			    	
		    ],
		  }
		};	
	vegaEmbed(svgName, chartSpec);	
	
}
