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

function makeSentenceBarChart(annotationName, subannotationName, svgName) {
	let baseUrl = "rest/api/SentencesAnnotationSubannotationScalarText?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation=OOPWordsAnnotation&Subannotation="+subannotationName;
	
	$.ajax(baseUrl)
	.then(
		function(data) {
			drawVegaBarChart(data, svgName);
		}
	)	
}

function drawVegaBarChart(data, svgName, title) {
	let baseUrl = "OOPDocumentViewer?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"#sentence_"
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple bar chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "width": 800,
			  "height": 400,
			  "title": title,
			  "mark": "bar",
			  "transform": [
				  {"calculate": "'" + baseUrl + "' + datum.id", "as": "url" }
			  ],
			  "encoding": {
			    "x": {"field": "id", "type": "nominal", "axis": {"labels": false}},
			    "y": {"field": "value", "type": "quantitative"},
			    "href": {"field": "url", "type": "nominal"},
			    "tooltip": [
			    	{"field": "text", "type": "nominal"}
			    ]
			  }
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
}
