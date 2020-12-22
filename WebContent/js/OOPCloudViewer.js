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

function makeBar(annotationName, svgName) {
	let baseUrl = "rest/api/DocumentAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName;
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: baseUrl 
				}
			)
		).done(
				function(data) {
					drawVegaBar(data, svgName);
				}
		);
}

function drawVegaBar(data, svgName, title) {
	let chartSpec = {
			  "$schema": "https://vega.github.io/schema/vega-lite/v4.json",
			  "description": "A simple bar chart with embedded data.",
			  "data": {
			    "values": data
			  },
			  "width": 800,
			  "height": 400,
			  "title": title,
			  "mark": {"type": "bar"},
			  "encoding": {
			    "x": {"field": "id", "type": "nominal", "axis": {"labels": false}},
			    "y": {"field": "value", "type": "quantitative"},
			    "tooltip": [
			        {"field": "name", "type": "nominal"},
			        {"field": "value", "type": "quantitative"}
			      ]
			  }
			};	
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
}


function makeCloud(annotationName, svgName) {
	let baseUrl = "rest/api/DocumentAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName;
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: baseUrl 
				}
			)
		).done(
				function(data) {
					drawVegaCloud(data, svgName);
				}
		);
}

function drawVegaCloud(data, svgName, title) {
	let chartSpec = {
	  "$schema": "https://vega.github.io/schema/vega/v5.json",
	  "description": "",
	  "width": 800,
	  "height": 400,
	  "padding": 0,
	  "title": title,
	  "data": [
	    {
	      "name": "table",
	      "values": data,
		  "transform": [
	        {
	          "type": "formula", "as": "angle",
	          "expr": "[-45, -30, 0, 30, 45, 90][~~(random() * 6)]"
	        }
		  ],
	    }
	  ],		  
	  "scales": [
	    {
	      "name": "color",
	      "type": "ordinal",
	      "domain": {"data": "table", "field": "name"},
	      "range": {"scheme": "category20b"}
	    }
	  ],

	  "marks": [
	    {
	      "type": "text",
	      "from": {"data": "table"},
	      "encode": {
	        "enter": {
	          "text": {"field": "name"},
	          "align": {"value": "center"},
	          "baseline": {"value": "alphabetic"},
	          "fill": {"scale": "color", "field": "name"}
	        },
	        "update": {
	          "fillOpacity": {"value": 1}
	        },
	        "hover": {
	          "fillOpacity": {"value": 0.5}
	        }
	      },
	      "transform": [
	        {
	          "type": "wordcloud",
	          "size": [800, 400],
	          "rotate": {"field": "datum.angle"},
	          "font": "serif",
	          "fontSize": {"field": "datum.value"},
	          "fontSizeRange": [18, 96],
	          "padding": 2
	        }
	      ]
	    }
	  ]
	};
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});
}	


