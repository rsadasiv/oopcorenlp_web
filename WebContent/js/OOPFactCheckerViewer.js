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
					url: getBaseUrl()
				}
			)
		).done(
			function (data) {
				let paragraphIdx = -1;
				let currentParagraphNode = null;
				let tokenIdx = 0;
				let leftCol = null;
				let centerCol = null;
				let rightCol = null;
				data.sentences.forEach(function(sentence, index) {
					if (sentence["ParagraphIndexAnnotation"] > paragraphIdx) {
						paragraphIdx = sentence["ParagraphIndexAnnotation"];
						currentParagraphNode = $("<p>");
						currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
						currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
						currentParagraphNode.appendTo(div);
					}
					let sentenceNode = $("<span>");
					sentenceNode.attr("id", "sentence_"+sentence["SentenceIndexAnnotation"]);
					sentenceNode.attr("ref", "sentence_"+sentence["SentenceIndexAnnotation"]);
					sentenceNode.attr("class", "sentence");
					//sentenceNode.text(sentence.text + " ");
					currentParagraphNode.append(sentenceNode);
					sentence.tokens.forEach(function(token, index) {
						tokenIdx++;
						sentenceNode.append(token["TokensAnnotation"].before);
						let tokenNode = {};
						let dataContent = getFactCheckerAnnotations(token);
						if (dataContent.length > '<div class="popover-message"></div>'.length) {
							tokenNode = $("<u>");
							tokenNode.attr("id", "token_"+tokenIdx);
							tokenNode.attr("ref", "token_"+tokenIdx);
							tokenNode.attr("data-toggle", "popover");
							tokenNode.attr("data-placement", "top");
							tokenNode.attr("data-html", "true");
							tokenNode.attr("data-content", dataContent);
							tokenNode.attr("trigger", "click");
						}
						else {
							tokenNode = $("<span>");
						}
						tokenNode.attr("class", "token");
			
						tokenNode.text(token["TokensAnnotation"].originalText);
						sentenceNode.append(tokenNode);
					});
					//let sentenceNodeGlyph = $("<span>");
					//sentenceNodeGlyph.attr("class", "ui-icon ui-icon-info");
					//sentenceNodeGlyph.attr("data-toggle", "popover");
					//sentenceNodeGlyph.attr("data-placement", "top");
					//sentenceNodeGlyph.attr("data-html", "true");
					//sentenceNodeGlyph.attr("data-content", getSentenceAnnotation(sentence));
					//sentenceNodeGlyph.click(function(sentence) {
					//	return function() {
					//		window.open("GetDocumentAnalysisStats?Analysis=OOPCoreNLP&Corpus="+ properties.corpus+"&Document="+docId+"&Scope=Sentence&Id="+sentence.sentenceIndex, "_documentScores");
					//	}
					//}(sentence));
					//currentParagraphNode.append(sentenceNodeGlyph);
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
