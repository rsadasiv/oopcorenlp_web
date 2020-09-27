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

function displayFactCheckerTextAnnotations(div) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP"
				}
			)
		).done(
			function (data) {
				let tokenIdx = 0;
				data.sentences.forEach(function(sentence, index) {
					sentence.tokens.forEach(function(token, index) {
						let tokenNode = $("#token_"+tokenIdx);
						let dataContent = getFactCheckerAnnotations(token);
						if (dataContent.length > '<div class="popover-message"></div>'.length) {
							tokenNode.css("text-decoration", "underline");
							tokenNode.attr("data-toggle", "popover");
							tokenNode.attr("data-placement", "top");
							tokenNode.attr("data-html", "true");
							tokenNode.attr("data-content", dataContent);
							tokenNode.attr("trigger", "click");
						}
						tokenIdx++;
					});
				});
				$("[data-toggle=popover]").popover();
			})
}


function getFactCheckerAnnotations(token) {
	let annotations = [
		"OOPWordnetGlossAnnotation",
		"OOPWikipediaGlossAnnotation"
	];
	let retval = '<div class="popover-message">';
	for (let scoreName in token) {
		if (annotations.includes(scoreName)) {
			let lemma = token["TokensAnnotation"].lemma;
			let glosses = token[scoreName];
			retval += ("<p>" + glosses + "</p>");
		}
	}
	retval += "</div>";
	return retval;
}
