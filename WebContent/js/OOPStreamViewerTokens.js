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


function showSentenceTextForTokenId(tokenIdx) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(raw_data) {
					let sortedTokenIdx = raw_data["OOPTokenCountAnnotation"]-tokenIdx;
					let currentTokenId = 0;
					for (const sentence of raw_data.sentences) {
						if (tokenIdx < (currentTokenId+sentence.tokens.length)) {
							$("#sentenceText").html(sentence.text);
							return;
						}
						currentTokenId=currentTokenId+sentence.tokens.length;
					}
				}
		);
}


function makeTokenBarChart(annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getTokenAnnotationsUrl(annotationName)
				}
			)
		).done(
				function(rawData) {
					$.when(
							$.ajax(
								{
									dataType: "json",
									url: getBaseUrl()
								}
							)
						).done(
								function(raw_data) {
									let tokenCount = raw_data["OOPTokenCountAnnotation"];
									let data = [];
									for (let [index, val] of rawData[annotationName].entries()) {

										if (isNumber(val) || isString(val)) {
											
											data.push({"name": tokenCount-index, "value": val});
										}
										else if (isObject(val)) {
											let s = 0;
											for(let key in val){
												if (val[key] && isNumber(val[key])) {
													s = (s + Number(val[key]));
												}
											}
											
											data.push({"name": tokenCount-index, "value": s});
										}
										else {
											
											data.push({"name": tokenCount-index, "value": 0});
										}
				
									}

									drawTokensBarChart(JSON.parse(JSON.stringify(data)), svgName, getAnnotationDisplayName(annotationName), showSentenceTextForTokenId);
								})
				});
		
}

function makeTokenScoreBarCharts(annotationName, svgName) {
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
				function(data) {
					let list = JSON.parse(JSON.stringify(data[annotationName]));;
					list.sort((a, b) => (parseFloat(a.value) < parseFloat(b.value)) ? 1 : -1);
					let idx = 2;
					list.slice(0,5).forEach(function(key, index) {
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
					url: getTokenAnnotationScoresUrl(annotationName, scoreName)
				}
			)
		).done(
				function(rawData) {
					$.when(
							$.ajax(
								{
									dataType: "json",
									url: getBaseUrl()
								}
							)
						).done(
								function(raw_data) {
									let tokenCount = raw_data["OOPTokenCountAnnotation"];
									let data = [];
									for (let [key, val] of rawData[annotationName].entries()) {

										if (isNumber(val) || isString(val)) {
											data.push({"name": tokenCount-key, "value": val});
										}
										else if (isObject(val)) {
											let s = 0;
											for(let key in val){
												if (key == scoreName) {
													if (val[key] && isNumber(val[key])) {
														s = (s + Number(val[key]));
													}
												}
											}
											data.push({"name": tokenCount-key, "value": s});
										}
										else {
											data.push({"name": tokenCount-key, "value": 0});
										}
									}

									drawTokensBarChart(JSON.parse(JSON.stringify(data)), svgName, scoreName, showSentenceTextForTokenId);
								})
							});
					
		
}



function drawTokensBarChart(data, svgName, yLabel, onclick) {
//    data = data.sort(function (a, b) {
//        return d3.ascending(Number(a.value), Number(b.value));
//    });
//    console.log(data);
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
		return d.value;
	}));
	y.domain([0, d3.max(data, function (d) {
				return d.name;
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
		return y(d.name);
	})
	.attr("width", x.bandwidth())
	.attr("height", function (d) {
		return d.value;
	});
	g.selectAll(".bar")
	.on("click", function(d, i) {
		d3.select(svgName).selectAll(".bar").style("fill", "steelblue");
        d3.select(this).style("fill", "black");
        //onclick(d3.select(this).attr("y"));
        onclick(i);
	});

}