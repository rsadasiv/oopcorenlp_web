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


function makeAnnotationsZScoresBarChart(targetCorpusName, annotation, svgName) {
	let baseUrl = "rest/api/CorpusDocumentAnnotationSubannotationsTfidf?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TargetCorpus="+getProperties()["selectedCorpus"]+"&Annotation="+annotation;
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
					setLink(svgName+"DataLink", baseUrl);
				});
		
}

function drawVegaBarChart(data, annotation, svgName, valueName) {
	console.log(svgName);
	console.log(valueName);
	console.log(data);
	let baseUrl = "OOPStreamSubannotationViewer?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotation+"&Subannotation="
	let chartSpec = {
		  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
		  "data": {
		    "values": data
		  },
		  "title": "Tfidf",
		  //"width": "container",
		  //"height": "container",
		  "width": 800,
		  "mark": {"type": "bar"},
		  "transform": [
			  {"calculate": "'" + baseUrl + "' + datum.name", "as": "url" },
			  {"calculate": "datum.value", "as": "tfidf" },
		],		  
		  "encoding": {
		    "y": {"field": "name", "type": "nominal", "axis": {"labelAngle": 0}, "sort": "tfidf"},
		    "x": {"field": "tfidf", "type": "quantitative"},
		    "href": {"field": "url", "type": "nominal"},
		    "tooltip": [
		    	{"field": "tfidf", "type": "quantitative"},
		    	//{"field": "documentScore", "type": "quantitative"},
		    	//{"field": "corpusMin", "type": "quantitative"},
		    	//{"field": "corpusMedian", "type": "quantitative"},
		    	//{"field": "corpusMean", "type": "quantitative"},
		    	//{"field": "corpusMax", "type": "quantitative"},
		    	//{"field": "corpusStddev", "type": "quantitative"},			    	
		    ],
		  }
		};	
	vegaEmbed(svgName, chartSpec);	
	
}
