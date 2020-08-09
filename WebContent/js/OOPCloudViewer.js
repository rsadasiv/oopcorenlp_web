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
					url: getBaseUrl()
				}
			)
		).done(
				function(rawData) {
					let data = [];
					if (isObject(rawData[annotationName])) {
						for (const annotation of Object.keys(rawData[annotationName])) {
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
						data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);
						let tdata = $.extend(true, [], data.slice(0,50));
						drawCloud(data.slice(0,50), svgName, getAnnotationDisplayName(annotationName));
					}
				});
		
}

function makeTable(annotationName, divName) {
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
						data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);
						let tdata = $.extend(true, [], data.slice(0,50));
						showCloudData(tdata, divName);
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
