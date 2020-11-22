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

function createSentenceTextInDivCb(div) {
	return function(sentenceIdx) {
		$.when(
				$.ajax(
					{
						dataType: "json",
						url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP/sentences/"+sentenceIdx+"/text"
					}
				)
			).done(
					function(raw_data) {
						 $(div).html("<a target=\"_blank\" href=\"" + getSentenceRef(sentenceIdx) + "\">" + raw_data + "</a>");
					}
			);
	}
}

function makeSentenceBarChart(annotationName, subannotationName, svgName, window) {
	let baseUrl = "rest/api/SentencesAnnotationSubannotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation=OOPWordsAnnotation&Subannotation="+subannotationName+"&Format=D3";
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: baseUrl
				}
			)
		).done(
				function(rawData) {
					drawBarChart(rawData, svgName, "value", createSentenceTextInDivCb(svgName+"Text"));
					setLink(svgName+"DataLink", baseUrl);
				});
		
}


function drawBarChart(data, svgName, valueName, onclick) {
	console.log(svgName);
	console.log(data);
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
		return d.id;
	}));
	y.domain([0, d3.max(data, function (d) {
				return d[valueName];
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
	.attr("text-anchor", "end");

	g.selectAll(".bar")
	.data(data)
	.enter().append("rect")
	.attr("class", "bar")
	.attr("x", function (d) {
		return x(d.id);
	})
	.attr("y", function (d) {
		return y(d[valueName]);
	})
	.attr("width", x.bandwidth())
	.attr("height", function (d) {
		if ((height - y(d[valueName])) < 0) {
			return 0;
		}
		else {
			return height - y(d[valueName]);
		}
	})
	.on("click", function(d, i) {
		d3.select(svgName).selectAll(".bar").style("fill", "steelblue");
        d3.select(this).style("fill", "black");
        onclick(i);
	});

}