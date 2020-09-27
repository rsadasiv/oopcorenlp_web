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

function createSentenceTextInDivCb(div, tokenCount) {
	return function(tokenIdx) {
		$.when(
				$.ajax(
					{
						dataType: "json",
						url: "rest/api/TokenIdxSentence?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&TokenIdx="+(tokenCount-tokenIdx)
					}
				)
			).done(
					function(data) {
						 $(div).html("<a target=\"_blank\" href=\"" + getSentenceRef(data.SentenceIndexAnnotation) + "\">" + data.text + "</a>");
					}
			);
	}
}

function invertList(data) {
	data.reverse();
	for (let i = 0; i < data.length; i++) {
	    data[i].id = i;
	}	
	return data;
}

function makeTokenBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "rest/api/TokensAnnotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName
				}
			)
		).done(
				function(data) {
					drawTokensBarChart(invertList(data), svgName, getAnnotationDisplayName(annotationName), createSentenceTextInDivCb("#sentenceText", data.length));
				}
		);
}

function makeTokenScoreBarCharts(annotationName, svgName) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: "rest/api/DocumentAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName
				}
			)
		).done(
				function(data) {
					let idx = 2;
					data.slice(0,5).forEach(function(key, index) {
						makeTokenScoreBarChart(annotationName, key.name, svgName + "_" + idx);
						idx++;
					});
				}
			);
}

function makeTokenScoreBarChart(annotationName, scoreName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "rest/api/TokensAnnotationSubannotationScalar?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation="+annotationName+"&Subannotation="+scoreName
				}
			)
		).done(
				function(data) {
					drawTokensBarChart(invertList(data), svgName, scoreName, createSentenceTextInDivCb("#sentenceText", data.length));
				}
		);
}



function drawTokensBarChart(data, svgName, yLabel, onclick) {
	let svg = d3.select(svgName),
	margin = {
		top: 20,
		right: 20,
		bottom: 30,
		left: 50
	},
	width = +svg.attr("width") - margin.left - margin.right,
	height = +svg.attr("height") - margin.top - margin.bottom,
	g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")"),

	x = d3.scaleBand()
		.rangeRound([0, width])
		.padding(0.1).align(0),

	y = d3.scaleLinear()
		.rangeRound([height, 0]);
	
	
	x.domain(data.map(function (d) {
		return d.value;
	}));
	y.domain([0, d3.max(data, function (d) {
				return d.id;
	})]);


	
	g.append("g")
	.attr("transform", "translate(0," + height + ")")
	.call(d3.axisBottom(x).tickValues([]));

	g.append("g")
	.call(d3.axisLeft(y).tickValues([]))
	.append("text")
	.attr("fill", "#000")
	.attr("transform", "rotate(-90)")
	.attr("y", 6)
	.attr("dy", "0.71em")
	.attr("text-anchor", "end")
	.text(yLabel);

	g.selectAll(".bar")
	.data(data)
	.enter().append("rect")
	.attr("class", "bar")
	.attr("x", function (d) {
		return x(d.value);
	})
	.attr("y", function (d) {
		return y(d.id);
	})
	.attr("width", x.bandwidth())
	.attr("height", function (d) {
		return d.value;
	});
	g.selectAll(".bar")
	.on("click", function(d, i) {
		d3.select(svgName).selectAll(".bar").style("fill", "steelblue");
        d3.select(this).style("fill", "black");
        onclick(i);
	});

}