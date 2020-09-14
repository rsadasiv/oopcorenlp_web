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

function makeCloud(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getDocumentAnnotationsUrl(annotationName)+"&Format=D3Cloud"
				}
			)
		).done(
				function(data) {
					drawCloud(data.slice(0,50), svgName);
				}
		);
}


function drawCloud(data, svgName) {
	let svg = d3.select(svgName),
	margin = {
		top: 20,
		right: 20,
		bottom: 30,
		left: 50
	},
	width = +svg.attr("width") - margin.left - margin.right,
	height = +svg.attr("height") - margin.top - margin.bottom;
	
    d3.wordcloud()
    .size([width, height])
    .selector(svgName)
    .scale('linear')
    .spiral('rectangular')
    .words(data)
    .start();
}

