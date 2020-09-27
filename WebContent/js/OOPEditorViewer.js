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
					url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP"
				}
			)
		).done(
			function(data) {
				let paragraphIdx = -1;
				let tokenIdx = 0;
				let sentenceLength = [];
				let sentenceLengthMean = 0;
				let sentenceLengthStddev = 0;
				
				data.sentences.forEach(function(sentence, index) {
					sentenceLength.push(sentence.tokens.length);
				});
				sentenceLengthMean = math.mean(sentenceLength);
				sentenceLengthStddev = math.std(sentenceLength);
				
				data.sentences.forEach(function(sentence, index) {
					let sentenceNode = $("#sentence_"+sentence.SentenceIndexAnnotation);
					let dataContent = getEditorAnnotationsSentence(sentence, sentenceLengthMean+(sentenceLengthStddev*2));
					if (dataContent.length > '<div class="popover-message"></div>'.length) {
						sentenceNode.css("text-decoration", "line-through");
						sentenceNode.attr("data-toggle", "popover");
						sentenceNode.attr("data-placement", "top");
						sentenceNode.attr("data-html", "true");
						sentenceNode.attr("data-content", dataContent);
						sentenceNode.attr("trigger", "click");
					}
					sentence.tokens.forEach(function(token, index) {
						let tokenNode = $("#token_"+tokenIdx);
						let dataContent = getEditorAnnotationsToken(token);
						if (dataContent.length > '<div class="popover-message"></div>'.length) {
							tokenNode.css("text-decoration", "line-through");
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
