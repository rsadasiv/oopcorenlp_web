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

let coreNLPAnnotationCategories = {
	"Metadata": [
		"DocIDAnnotation",
		"DocTitleAnnotation",
		"AuthorAnnotation",
		"DocDateAnnotation",
		"DocTypeAnnotation",
		"DocSourceTypeAnnotation"
	],
	"Document": [
		"OOPCharCountAnnotation",
		"OOPSyllableCountAnnotation",
		"OOPTokenCountAnnotation",
		"OOPWordCountAnnotation",
		"OOPSentenceCountAnnotation",
		"OOPParagraphCountAnnotation",
		"OOPVerblessSentencesAnnotation",
		"OOPFleschKincaidAnnotation",
		"CoreNlpSentimentAnnotation",
		"VaderSentimentAnnotation"
	],
	"Diction": [
		"OOPWordsAnnotation",
		"OOPFunctionWordsAnnotation",
		"OOPCommonWordsAnnotation",
		"OOPUncommonWordsAnnotation",
		"OOPWordlessWordsAnnotation",
		"OOPTemporalNGramsAnnotation",
		"OOPNonAffirmativeAnnotation",
		"OOPAngliciseAnnotation",
		"OOPAmericanizeAnnotation"
	],
	"Lede": [
		"OOPWhoAnnotation",
		"OOPWhatAnnotation",
		"OOPWhenAnnotation",
		"OOPWhereAnnotation",
		"OOPWhyAnnotation",
		"OOPHowAnnotation",
		"OOPIfAnnotation",
		"OOPBecauseAnnotation",
		"OOPPeopleAnnotation",
		"OOPDatesAnnotation",
		"OOPLocationsAnnotation",
		"OOPQuotesAnnotation",
		"OOPLikeAnnotation",
		"OOPAsAnnotation"		
	],
	"GenderNumber": [
		"OOPGenderAnnotation",
		"CoreNlpGenderAnnotation",
		"OOPPronounAnnotation",
		"OOPPossessivesAnnotation"
	],
	"Description": [
		"OOPAdjectivesAnnotation",
		"OOPPointlessAdjectivesAnnotation",
		"OOPAdverbsAnnotation",
		"OOPPointlessAdverbsAnnotation",
		"OOPFlavorsAnnotation",
		"OOPColorsAnnotation",
	],
	"Semantics": [
		"OOPTopicsAnnotation",
		"OOPNounsAnnotation",
		"OOPNounGroupsAnnotation",
		"OOPNounHypernymsAnnotation",
		"OOPVerbTenseAnnotation",			
		"OOPVerbsAnnotation",
		"OOPActionlessVerbsAnnotation",
		"OOPVerbGroupsAnnotation",
		"OOPVerbnetGroupsAnnotation",
		"OOPVerbHypernymsAnnotation",
		"OOPPrepositionsAnnotation",
		"OOPPrepositionCategoriesAnnotation",
		"OOPAdjectiveCategoriesAnnotation",
		"OOPAdverbCategoriesAnnotation",
		"OOPPunctuationMarkAnnotation",
		"OOPBiberAnnotation",
		"OOPBiberDimensionsAnnotation"
	],
	"SemanticsDocument": [
		"OOPWikipediaPageviewTopicsAnnotation",
		"OOPWikipediaCategoriesAnnotation"
	],
	"Contextual": [
		"OOPActorsAnnotation",
		"OOPSettingsAnnotation"
	]
}

function displayAnnotations() {
	$.ajax({
		dataType: "json",
		url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP",
        async: true,
        success: function(data) {
            displayMetadataAnnotations(data);
            displayDocumentAnnotations(data);
            displayDictionAnnotations(data);
            displayLedeAnnotations(data);
            displayGenderNumberAnnotations(data);
            displayDescriptionAnnotations(data);
            displaySemanticsAnnotations(data);
            displayUncategorizedAnnotations(data);
            displayTextAnnotation(data);
        	$('[data-toggle="popover"]').popover();
        	$('[data-toggle="tooltip"]').tooltip();
        }
    });
}

function getAnnotation(properties, cb) {
	//let url = properties.corpus + "/" + properties.annotationDir + "/" + properties.docId + ".json";

	$.ajax({
		dataType: "json",
		url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP",
        async: true,
        success: cb
    });
}


function listMapAnnotationToUl(data, annotation_list, div) {
	annotation_list.forEach(function(annotationName, index) {
		let endIdx = annotationName.lastIndexOf("Annotation");
		if (endIdx == -1) {
			endIdx = annotationName.length;
		}
		if (isNumber(data[annotationName])) {
			let ul = $('<ul>');
			div.append(ul);
			ul.attr("class", "list-group list-group-flush");
			ul.append(scalarAnnotationToLi(data, annotationName));
		}
		else if (isArray(data[annotationName])) {
			let displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
			let scoreTotal = data[annotationName].length;
			div.append(createAccordionButtonNoColorPicker(displayName, scoreTotal, annotationName));

			data[annotationName].forEach(function (listItem, listIndex) {
				let d = $("<div>");
				d.attr("class", "container-fluid collapse");
				d.attr("id", displayName+"_div");
				div.append(d);
				let ul = $('<ul>');
				d.append(ul);
				ul.attr("class", "list-group list-group-flush");
				//ul.append(scalarAnnotationToLi(data[annotationName], annotation, annotationName));
				let li = $('<li>');
				li.attr("class", "list-group-item list-group-item-light");
				li.append("<span data-toggle='toolip' data-placement='right' title='"+listItem["name"]+"' boundary='window'>"+listItem["name"]+ "</span>");
				ul.append(li);
			});

		}
		else if (isObject(data[annotationName])) {
			let displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
			let scoreTotal = 0;
			let list = Object.keys(data[annotationName]);
			list.sort((a, b) => (parseFloat(data[annotationName][a]) < parseFloat(data[annotationName][b])) ? 1 : -1);

			for (let key in data[annotationName]) {
				scoreTotal = scoreTotal + Number(data[annotationName][key]);
			}
			if (scoreTotal == 0) {
				div.append(createAccordionButtonNoColorPicker(displayName, scoreTotal, annotationName));
			}
			else {
				div.append(createAccordionButton(displayName, scoreTotal, annotationName));
				list.forEach(function(annotation, index) {
					let d = $("<div>");
					d.attr("class", "container-fluid collapse");
					d.attr("id", displayName+"_div");
					div.append(d);
					let ul = $('<ul>');
					d.append(ul);
					ul.attr("class", "list-group list-group-flush");
					ul.append(scalarAnnotationToLi(data[annotationName], annotation, annotationName));
				});
			}
		}
	});
}

function listMapAnnotationToUlNoColorPicker(data, annotation_list, div) {
	annotation_list.forEach(function(annotationName, index) {
		let endIdx = annotationName.lastIndexOf("Annotation");
		if (endIdx == -1) {
			endIdx = annotationName.length;
		}

		if (isNumber(data[annotationName])) {
			let ul = $('<ul>');
			div.append(ul);
			ul.attr("class", "list-group list-group-flush");
			ul.append(scalarAnnotationToLiNoColorPicker(data, annotationName));
		}
		else if (isArray(data[annotationName])) {
			let displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
			let scoreTotal = data[annotationName].length;
			div.append(createAccordionButtonNoColorPicker(displayName, scoreTotal, annotationName));

			data[annotationName].forEach(function (listItem, listIndex) {
				let d = $("<div>");
				d.attr("class", "container-fluid collapse");
				d.attr("id", displayName+"_div");
				div.append(d);
				let ul = $('<ul>');
				d.append(ul);
				ul.attr("class", "list-group list-group-flush");
				//ul.append(scalarAnnotationToLi(data[annotationName], annotation, annotationName));
				let li = $('<li>');
				li.attr("class", "list-group-item list-group-item-light");
				li.append("<span data-toggle='toolip' data-placement='right' title='"+listItem["name"]+"' boundary='window'>"+listItem["name"]+ "</span>");
				ul.append(li);
			});

		}
		else if (isObject(data[annotationName])) {
			let displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
			let scoreTotal = 0;
			let list = Object.keys(data[annotationName]);
			list.sort((a, b) => (parseFloat(data[annotationName][a]) < parseFloat(data[annotationName][b])) ? 1 : -1);

			for (let key in data[annotationName]) {
				scoreTotal = scoreTotal + Number(data[annotationName][key]);
			}
			div.append(createAccordionButtonNoColorPicker(displayName, scoreTotal, annotationName));
			list.forEach(function(annotation, index) {
				if (data[annotationName][annotation] != 1) {
					let d = $("<div>");
					d.attr("class", "container-fluid collapse");
					d.attr("id", displayName+"_div");
					div.append(d);
					let ul = $('<ul>');
					d.append(ul);
					ul.attr("class", "list-group list-group-flush");
					
					ul.append(scalarAnnotationToLiNoColorPicker(data[annotationName], annotation, annotationName));
				}
			});
		}
	});
}

function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}

function createAccordionButton(displayName, score, annotationName) {
	let d1 = $("<div class='container-fluid accordion'/>");
	let d2 = $("<div class='card'/>");
	d1.append(d2);
	let d3 = $("<div class='card-header'/>");
	d2.append(d3);
	let h5 = $("<h5 class='mb-0'/>");
	d3.append(h5);
	let btn = $("<button class='btn btn-link' type='button' data-toggle='collapse'  aria-expanded='true'/>");
	btn.attr("data-target", "#"+displayName+"_div");
	btn.attr("aria-controls", "collapse"+displayName);
	btn.text(displayName +": "+score);
	h5.append(btn);
	let colors = [
		"bg-highlight1",
		"bg-highlight2",
		"bg-highlight3",
		"bg-highlight4",
		"bg-highlight5"
	]
	colors.forEach(function(color, index) {
		let cp = $("<div>");
		cp.addClass("ui-colorpicker-swatch")
		cp.addClass(color);
		cp.click(function() {
			getAnnotation(getProperties(), function(annotationName) {
				return function(d) {
					highlightTokenAnnotation(d, annotationName, color);
				}
			}(annotationName));
		})
		h5.prepend(cp);
	});
	return d1;
}

function createAccordionButtonNoColorPicker(displayName, score, annotationName) {
	let d1 = $("<div class='container-fluid accordion'/>");
	let d2 = $("<div class='card'/>");
	d1.append(d2);
	let d3 = $("<div class='card-header'/>");
	d2.append(d3);
	let h5 = $("<h5 class='mb-0'/>");
	d3.append(h5);
	let btn = $("<button class='btn btn-link' type='button' data-toggle='collapse'  aria-expanded='true'/>");
	btn.attr("data-target", "#"+displayName+"_div");
	btn.attr("aria-controls", "collapse"+displayName);
	btn.text(displayName +": "+score);
	h5.append(btn);
	return d1;
}

function mapAnnotationToUl(data, annotationGroupName) {
	let list = Object.keys(data);
	list.sort((a, b) => (parseFloat(data[a]) < parseFloat(data[b])) ? 1 : -1);
	let ul = $('<ul>');
	ul.attr("class", "list-group list-group-flush");
	list.forEach(function(annotationName, index) {
		ul.append(scalarAnnotationToLi(data, annotationName, annotationGroupName));

	});
	return ul;
}

function mapScalarAnnotationToUl(data, annotationGroupName) {
	let list = Object.keys(data[annotationGroupName]);
	//list.sort((a, b) => (parseFloat(data[a]) < parseFloat(data[b])) ? 1 : -1);
	list.sort((a, b) => (a > b) ? 1 : -1);
	let ul = $('<ul>');
	ul.attr("class", "list-group list-group-flush");
	list.forEach(function(annotationName, index) {
		ul.append(scalarAnnotationToLi(data[annotationGroupName], annotationName, annotationGroupName));

	});
	return ul;
}

function scalarAnnotationToLi(data, annotationName, annotationGroupName) {
	let li = $('<li>');
	li.attr("class", "list-group-item list-group-item-light");
	let displayName = annotationName;
	let fullName = annotationName;
	if (annotationGroupName) {
		fullName = annotationGroupName+"."+annotationName;
	}
	else {
		let endIdx = annotationName.lastIndexOf("Annotation");
		if (endIdx == -1) {
			endIdx = annotationName.length;
		}
		displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
	}
	let dataValue = data[annotationName];
	if (!Number.isNaN(Number(dataValue)) && dataValue.indexOf(".") != -1) {
		dataValue = Number(dataValue).toFixed(4);
	}
	let lbl = ""
	li.append("<span data-toggle='toolip' data-placement='right' title='"+fullName+"' boundary='window'>"+displayName+ "</span>: "+ dataValue);
	let colors = [
		"bg-highlight1",
		"bg-highlight2",
		"bg-highlight3",
		"bg-highlight4",
		"bg-highlight5"
	]
	colors.forEach(function(color, index) {
		let cp = $("<div>");
		cp.addClass("ui-colorpicker-swatch")
		cp.addClass(color);
		cp.click(function() {
			getAnnotation(getProperties(), function(annotationName) {
				return function(d) {
					highlightTokenAnnotation(d, fullName, color);
				}
			}(annotationGroupName));
		})
		li.prepend(cp);
	});
	return li;
}

function scalarAnnotationToLiNoColorPicker(data, annotationName, annotationGroupName) {
	let li = $('<li>');
	li.attr("class", "list-group-item list-group-item-light");
	let displayName = annotationName;
	let fullName = annotationName;
	if (annotationGroupName) {
		fullName = annotationGroupName+"."+annotationName;
	}
	else {
		let endIdx = annotationName.lastIndexOf("Annotation");
		if (endIdx == -1) {
			endIdx = annotationName.length;
		}
		displayName = annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
	}
	let dataValue = data[annotationName];
	if (!Number.isNaN(Number(dataValue)) && dataValue.indexOf(".") != -1) {
		dataValue = Number(dataValue).toFixed(4);
	}
	let lbl = ""
	li.append("<span data-toggle='toolip' data-placement='right' title='"+fullName+"' boundary='window'>"+displayName+ "</span>: "+ dataValue);

	return li;
}


function displayMetadataAnnotations(data, cb) {
	$('#metadata_div').empty();
	let ul = $('<ul>');
	ul.attr("class", "list-group list-group-flush");
	ul.appendTo('#metadata_div');
	coreNLPAnnotationCategories["Metadata"].forEach(function(annotationName, index) {
		ul.append(scalarAnnotationToLiNoColorPicker(data["metadata"], annotationName));
	});
	cb && typeof cb === 'function' && cb(data);
}

function displayDocumentAnnotations(data, cb) {
	$('#document_div').empty();
	//let ul = mapScalarAnnotationToUl(data, "OOPDocumentLengthAnnotation");
	let ul = $('<ul>');
	ul.appendTo($('#document_div'));
	ul.attr("class", "list-group list-group-flush");
	coreNLPAnnotationCategories["Document"].forEach(function(annotationName, index) {
		ul.append(scalarAnnotationToLiNoColorPicker(data, annotationName));
	});

	cb && typeof cb === 'function' && cb(data);
}

function displayDictionAnnotations(data, cb) {
	$('#diction_div').empty();
	listMapAnnotationToUl(data, coreNLPAnnotationCategories["Diction"], $('#diction_div'));

	cb && typeof cb === 'function' && cb(data);
}

function displayLedeAnnotations(data, cb) {
	$('#lede_div').empty();
	listMapAnnotationToUlNoColorPicker(data, coreNLPAnnotationCategories["Lede"], $('#lede_div'));

	cb && typeof cb === 'function' && cb(data);
}

function displayGenderNumberAnnotations(data, cb) {
	$('#gendernumber_div').empty();
	listMapAnnotationToUl(data, coreNLPAnnotationCategories["GenderNumber"], $('#gendernumber_div'));

	cb && typeof cb === 'function' && cb(data);
}

function displayDescriptionAnnotations(data, cb) {
	$('#description_div').empty();
	listMapAnnotationToUl(data, coreNLPAnnotationCategories["Description"], $('#description_div'));

	cb && typeof cb === 'function' && cb(data);
}

function displaySemanticsAnnotations(data, cb) {
	$('#semantics_div').empty();
	listMapAnnotationToUl(data, coreNLPAnnotationCategories["Semantics"], $('#semantics_div'));
	listMapAnnotationToUlNoColorPicker(data, coreNLPAnnotationCategories["SemanticsDocument"], $('#semantics_div'));
	cb && typeof cb === 'function' && cb(data);
}


function displayUncategorizedAnnotations(data, cb) {
	$('#uncategorized_div').empty();
	let uncategorizedAnnotationsList = [];
	let skipAnnotations = ["corefs", "quotes", "sentences", "metadata"];
	let allAnnotations = Object.keys(data);
	allAnnotations.forEach(function(annotation, allindex) {
		let found = false;
		if (skipAnnotations.includes(annotation)) {
			found = true;
		}
		else {
			for (let key in coreNLPAnnotationCategories) {
				coreNLPAnnotationCategories[key].forEach(function(categorizedAnnotation, annindex) {
					if (categorizedAnnotation === annotation) {
						found = true;
					}
				});
			};
		}
		if (!found) {
			uncategorizedAnnotationsList.push(annotation);
		};
	});
	listMapAnnotationToUl(data, uncategorizedAnnotationsList, $('#uncategorized_div'));

	cb && typeof cb === 'function' && cb(data);
}



function highlightTokenAnnotation(data, fullName, color) {
	let annotationName = fullName;
	let annotationValue = "";
	if (fullName.lastIndexOf("$") < fullName.lastIndexOf(".")) {
		annotationName = fullName.substring(0, fullName.lastIndexOf("."));
		annotationValue = fullName.substring(fullName.lastIndexOf(".")+1);
	}
	//fix whitespace in fullName?
	if($("#colorGuide_"+fullName.replace(/\./g, "_").replace(/\$/g, "s")).length != 0) {
		$("#colorGuide_"+fullName.replace(/\./g, "_").replace(/\$/g, "s")).remove();
	}
	else {
		let p = $("<p id='colorGuide_"+fullName.replace(/\./g, "_").replace(/\$/g, "s")+"'>");
		p.addClass(color);
		p.text(fullName);
		p.click(function() {
					highlightTokenAnnotation(data, fullName, color);
				}
		);
		$("#colorGuide").append(p);
	}
	let tokenIdx = 0;
	data.sentences.forEach(function(sentence, index) {
		sentence.tokens.forEach(function(token, index) {
			if (token.hasOwnProperty(annotationName)) {
				if (!(isNaN(token[annotationName])) && (token[annotationName] == 0)) {
					//skip it
				}
				else if (annotationValue === "" || token[annotationName].hasOwnProperty(annotationValue)) {
					$("#token_"+tokenIdx).toggleClass(color);
				}
			}
			tokenIdx++;
		});
	});
}

function displayTextAnnotation(data, cb) {
	let paragraphIdx = -1;
	let currentParagraphNode = null;
	let tokenIdx = 0;
	let leftCol = null;
	let centerCol = null;
	let rightCol = null;
	data.sentences.forEach(function(sentence, index) {
		sentence.tokens.forEach(function(token, index) {
			let tokenNode = $("#token_"+tokenIdx);
			if (index === sentence.tokens.length -1) {
				tokenNode.click(function(sentence) {
					return function() {
						window.open("rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP/sentences/"+sentence.SentenceIndexAnnotation, "_documentScores");
					}
				}(sentence));
			}
			else {
				tokenNode.click(function(tokenIdx) {
					return function() {
						window.open("rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP/tokens/"+tokenIdx, "_documentScores");
					}
				}(tokenIdx));
			}
			tokenIdx++;
		});
	});

	cb && typeof cb === 'function' && cb(data);
}