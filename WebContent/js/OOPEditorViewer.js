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

function displayEditorTextAnnotations(div) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					url: getBaseUrl()
				}
			)
		).done(
			function(data) {
				let paragraphIdx = -1;
				let currentParagraphNode = null;
				let tokenIdx = 0;
				let leftCol = null;
				let centerCol = null;
				let rightCol = null;
				let sentenceLength = [];
				let sentenceLengthMean = 0;
				let sentenceLengthStddev = 0;
				
				data.sentences.forEach(function(sentence, index) {
					sentenceLength.push(sentence.tokens.length);
				});
				sentenceLengthMean = math.mean(sentenceLength);
				sentenceLengthStddev = math.std(sentenceLength);
				
				data.sentences.forEach(function(sentence, index) {
					if (sentence["ParagraphIndexAnnotation"] > paragraphIdx) {
						paragraphIdx = sentence["ParagraphIndexAnnotation"];
						currentParagraphNode = $("<p>");
						currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
						currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
						currentParagraphNode.appendTo(div);
					}
					let sentenceNode = {};
					let dataContent = getEditorAnnotationsSentence(sentence, sentenceLengthMean+(sentenceLengthStddev*2));
					if (dataContent.length > '<div class="popover-message"></div>'.length) {
						sentenceNode = $("<s>");
						sentenceNode.attr("id", "token_"+tokenIdx);
						sentenceNode.attr("ref", "token_"+tokenIdx);
						sentenceNode.attr("data-toggle", "popover");
						sentenceNode.attr("data-placement", "top");
						sentenceNode.attr("data-html", "true");
						sentenceNode.attr("data-content", dataContent);
						sentenceNode.attr("trigger", "click");
					}
					else {
						sentenceNode = $("<span>");
					}
					sentenceNode.attr("id", "sentence_"+sentence["SentenceIndexAnnotation"]);
					sentenceNode.attr("ref", "sentence_"+sentence["SentenceIndexAnnotation"]);
					sentenceNode.attr("class", "sentence");
					//sentenceNode.text(sentence.text + " ");
					currentParagraphNode.append(sentenceNode);
					sentence.tokens.forEach(function(token, index) {
						tokenIdx++;
						sentenceNode.append(token["TokensAnnotation"].before);
						let tokenNode = {};
						let dataContent = getEditorAnnotationsToken(token);
						if (dataContent.length > '<div class="popover-message"></div>'.length) {
							tokenNode = $("<s>");
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
				});
			
				$("[data-toggle=popover]").popover();
			});
}

function getEditorAnnotationsSentence(sentence, normalSentenceLength) {
	let annotations = {
		"OOPVerblessSentencesAnnotation" : 0
	};
	let annotationsDesc = {
		"OOPFleschKincaidAnnotation" : .8
	};
	
	let retval = '<div class="popover-message">';
	for (let scoreName in sentence) {
		if (Object.keys(annotations).includes(scoreName)) {
			if (Number(sentence[scoreName]) > Number(annotations[scoreName])) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
		else if (Object.keys(annotationsDesc).includes(scoreName)) {
			if (Number(sentence[scoreName]) < Number(annotationsDesc[scoreName])) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
	}
	if (sentence.tokens.length > normalSentenceLength) {
		retval += ("<p>" + "sentence.tokens.length" + "</p>");
	}
	retval += "</div>";
	return retval;
}

function getEditorAnnotationsToken(token) {
	let annotations = [
		"OOPWordlessWordsAnnotation"
	];
	let annotationsLemma = [
		"OOPPointlessAdverbsAnnotation",
		"OOPPointlessAdjectivesAnnotation",
		"OOPNonAffirmativeAnnotation",
		"OOPAngliciseAnnotation"
	];
	let subAnnotations = {
		"OOPTemporalNGramsAnnotation" : "novel",
		"OOPBiberAnnotation" : "PASS",
		"OOPBiberAnnotation" : "EX"
	};
	let retval = '<div class="popover-message">';
	for (let scoreName in token) {
		if (annotations.includes(scoreName)) {
			console.log(token);
			if (isNumber(token[scoreName])) {
				if (token[scoreName] > 0) {
					retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
				}
			}
			else if (isString(token[scoreName])) {
				retval += ("<p>" + token[scoreName] + "</p>");
			}				
		}
		else if (annotationsLemma.includes(scoreName)) {
			let lemma = token["TokensAnnotation"].lemma;
			if (token[scoreName][lemma] > 0) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
		else if (Object.keys(subAnnotations).includes(scoreName)) {
			if (Object.keys(token[scoreName]).includes(subAnnotations[scoreName])) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + ": " + subAnnotations[scoreName] + "</p>");
			}
		}
	}
	retval += "</div>";
	return retval;
}
