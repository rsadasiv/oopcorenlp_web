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

function makeDocumentAnnotationDonutChart(annotationName, svgName, filter = 0) {
	let baseUrl = "rest/api/DocumentAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName;
	$.ajax(baseUrl)
	.then(
		function(data) {
			drawVegaDonutChart(data.filter(datum => datum.value > filter), svgName, annotationName)
		}
	);	
}

function drawVegaDonutChart(data, svgName, title) {
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple donut chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "title": title,
			  "width": 200,
			  "height": 200,
			  "mark": {"type": "arc", "innerRadius": 50},
			  "encoding": {
			    "theta": {"field": "value", "type": "quantitative"},
			    "color": {"field": "name", "type": "nominal"}
			  },
			  "view": {"stroke": null}
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}

function makeMyersBriggsPie(data, svgName, title) {
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple donut chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "title": title,
			  "width": 100,
			  "height": 100,
			  "mark": {"type": "arc"},
			  "encoding": {
			    "theta": {"field": "value", "type": "quantitative"},
			    "color": {"field": "name", "type": "nominal"}
			  },
			  "view": {"stroke": null}
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}

function makeSentenceAnnotationBarChart(annotationName, svgName) {
	let baseUrl = "rest/api/SentencesAnnotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName;
	$.ajax(baseUrl)
	.then(
		function(data) {
			drawVegaBarChart(data, svgName, annotationName)
		}
	)	
}

function makeSentenceAnnotationSubannotationBarChart(annotationName, subannotationName, svgName) {
	let baseUrl = "rest/api/SentencesAnnotationSubannotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName+"&Subannotation="+subannotationName;

	$.ajax(baseUrl)
	.then(
		function(data) {
			drawVegaBarChart(data, svgName, subannotationName)
		}
	)	
}

function getAnnotationData(annotationNames) {
	let promise = $.Deferred();
	let annotationData = new Array();
	let attempts = 0;
	annotationNames.forEach(function(annotationName) {
		let baseUrl = "rest/api/SentencesAnnotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName;
		$.ajax(baseUrl)
		.then(
			function(data) {
				annotationData = annotationData.concat(data)
				attempts = attempts + 1;
				if (attempts == annotationNames.length) {
					promise.resolve(annotationData);
				}
			},
			function(similarity) {
				attempts = attempts + 1;
				if (attempts == annotationNames.length) {
					promise.resolve(annotationData);
				}
			}
		)	
	})
	return promise.promise();
}

function makeSentenceAnnotationStackedBarChart(annotationNames, svgName, title) {
	getAnnotationData(annotationNames)
	.then(
		function(data) {
			drawVegaStackedBarChart(data, svgName, title)
		}
	)	
}

function drawVegaBarChart(data, svgName, title) {
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple bar chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "width": 400,
			  "height": 200,
			  "title": title,
			  "mark": "bar",
			  "encoding": {
			    "x": {"field": "id", "type": "nominal", "axis": {"labels": false}},
			    "y": {"field": "value", "type": "quantitative"}
			  }
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
}

function drawVegaStackedBarChart(data, svgName, title) {
	console.log(data);
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple bar chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "width": 400,
			  "height": 200,
			  "title": title,
			  "mark": "bar",
			  "encoding": {
			    "x": {"field": "id", "type": "nominal", "axis": {"labels": false}},
			    "y": {"field": "value", "type": "quantitative"},
			    "color": {"field": "name", "type": "nominal"}
			  }
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}

