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


function makeAnnotationsCVScoresBarChart(targetCorpusName, annotation, aggregateName, svgName) {
	let baseUrl = "rest/api/CorpusAnnotationSubannotationsCVScores?Corpus="+getProperties()["corpus"]+"&Annotation="+annotation+"&AggregateName="+aggregateName;
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

function drawVegaBarChart(data, annotation, svgName, valueName) {
	let chartSpec = {
		  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
		  "data": {
		    "values": data
		  },
		  "title": "Coefficient of Variance",
		  //"width": "container",
		  //"height": "container",
		  "width": 800,
		  "mark": {"type": "bar"},
		  "transform": [
			  {"calculate": "datum.value", "as": "cvScore" }
		  ],		  
		  "encoding": {
		    "y": {"field": "name", "type": "nominal", "axis": {"labelAngle": 0}, "sort": {"field": "cvScore", order: "ascending"}},
		    "x": {"field": "cvScore", "type": "quantitative"},
		    "tooltip": [
		    	{"field": "cvScore", "type": "quantitative"},	
		    ],
		  }
		};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}
