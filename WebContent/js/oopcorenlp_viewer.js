'use strict';
var properties = {};
var annotation = {};
//var allAnnotations = [];
/*var allAnnotations = [
	"DocIDAnnotation",
	"DocTitleAnnotation",
	"DocSourceTypeAnnotation",
	"DocTypeAnnotation",
	"AuthorAnnotation",
	"DocDateAnnotation",
	"OOPBiberDimensionsAnnotation" ,
	"OOPPointlessAdverbsAnnotation",
	"OOPVerblessSentencesAnnotation",

	"OOPLocationsAnnotation",
	"OOPPointlessAdjectivesAnnotation",
	"OOPNounHypernymsAnnotation",
	"OOPSyllablesAnnotation",
	"OOPFleschKincaidAnnotation",
	"OOPUntaggedTopicsAnnotation",
	"OOPBiberAnnotation",
	"OOPVerbsAnnotation",
	"OOPWordnetGlossAnnotation",
	"OOPColorsAnnotation",

	"OOPNounsAnnotation",
	"OOPTopicsAnnotation",

	"OOPTemporalNGramsAnnotation",
	"OOPCommonWordsAnnotation",
	"OOPAdjectiveCategoriesAnnotation",
	"OOPPunctuationMarkAnnotation",
	"OOPSimileAnnotation",
	"OOPGenderAnnotation",
	"OOPWordsAnnotation",
	"CoreNlpGenderAnnotation",
	"OOPVerbGroupsAnnotation",
	"OOPWikipediaRelatedNounsAnnotation",
	"ParagraphIndexAnnotation",
	"OOPVerbHypernymsAnnotation",
	"OOPActionlessVerbsAnnotation",
	"OOPWordlessWordsAnnotation",
	"OOPWikipediaCategoriesAnnotation",
	"OOPNonAffirmativeAnnotation",
	"OOPPossessivesAnnotation",
	"OOPPrepositionsAnnotation",

	"OOPActorsAnnotation",
	"OOPPronounAnnotation",
	"OOPWikipediaPageviewTopicsAnnotation",
	"OOPFlavorsAnnotation",
	"OOPDocumentLengthAnnotation",
	"OOPAdverbsAnnotation",
	"OOPUncommonWordsAnnotation",
	"OOPAdverbCategoriesAnnotation",
	"OOPVerbTenseAnnotation",
	"OOPWikipediaGlossAnnotation",

	"OOPPeopleAnnotation",
	"CoreNlpSentimentAnnotation",
	"OOPAdjectivesAnnotation",
	"VaderSentimentAnnotation"
]*/


var coreNLPAnnotationCategories = {
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
		"OOPTopicsAnnotation",
		"OOPVerbTenseAnnotation",
		"OOPDatesAnnotation",
		"OOPLocationsAnnotation",
		"OOPQuotesAnnotation"
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
		"OOPLikeAnnotation",
		"OOPAsAnnotation",
		"OOPFlavorsAnnotation",
		"OOPColorsAnnotation",
	],
	"Semantics": [
		"OOPNounsAnnotation",
		"OOPNounGroupsAnnotation",
		"OOPNounHypernymsAnnotation",
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

function setProperties() {
	//properties.corpus = "Corpora/Submissions";
	//properties.textDir = "Text";
	//properties.annotationDir = "Annotations";
	properties.docId = docId;
	//properties.corpus = corpus;
	//getAllAnnotations();
}

function getProperties() {
	return properties;
}

function getAllAnnotations() {
	let url = "ListDocumentAnalyses?Corpus="+ properties.corpus+"&Document="+ properties.docId;
	$.when(
			$.ajax(
				{
					dataType: "json",
					url: url
				}
			)
		).done(
				function(data) {
					for (const analysis of data) {
						for (const analysisName of Object.keys(analysis)) {
							allAnnotations.push(analysisName);
						}
					}
				});

}

function getAnnotation(properties, cb) {
	//let url = properties.corpus + "/" + properties.annotationDir + "/" + properties.docId + ".json";

	$.ajax({
		dataType: "json",
		url: "Corpora/"+corpus+"/OOP_"+docId+".json",
        async: true,
        success: cb
    });
}

function isString (value) {
	return typeof value === 'string' || value instanceof String;
}

function isNumber (value) {
	return typeof value === 'number' || isFinite(value);
}

function isObject (value) {
	return value && typeof value === 'object' && value.constructor === Object;
}

function isArray (value) {
	return Array.isArray(value);
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
				let d = $("<div>");
				d.attr("class", "container-fluid collapse");
				d.attr("id", displayName+"_div");
				div.append(d);
				let ul = $('<ul>');
				d.append(ul);
				ul.attr("class", "list-group list-group-flush");
				ul.append(scalarAnnotationToLiNoColorPicker(data[annotationName], annotation, annotationName));
			});
		}
	});
}

function listActorsAnnotationToUl(data, annotationName, div) {

	let list = [];

	for(let key in data[annotationName]){
		list.push(data[annotationName][key]);
	}
	list.sort((a, b) => (a.importance > b.importance) ? -1 : 1);
	let actors = [];
	list.forEach(function (actor, index) {
		actors.push(actor.canonicalName);
	});

	actors.forEach(function(actorCanonicalName, index) {
		let actor = data[annotationName][actorCanonicalName];
		let divName = actor.canonicalName.replace(" ", "_");
		div.append(createAccordionButton(divName, actor.importance));
		let d = $("<div>");
		d.attr("class", "container-fluid collapse");
		d.attr("id", divName+"_div");
		div.append(d);
		let ul = $('<ul>');
		d.append(ul);
		ul.attr("class", "list-group list-group-flush");
		//let actorAttrs = Object.keys(actor);
		//sort attrs?
		let actorAttrs = [
			"canonicalName",
			"importance",
			"firstAppearance",
			"lastAppearance",
			"coreNlpGender",
			"oopgender",
			"quotes",
			"attributes"
		]
		actorAttrs.forEach(function(attrName, index) {
			if (attrName === "coreNlpSentiment" || attrName === "vaderSentiment") {
				//pass
			}
			else if (isArray(actor[attrName])) {
				let li = $('<li>');
				ul.append(li);
				li.attr("class", "list-group-item list-group-item-light");
				li.append("<span data-toggle='toolip' data-placement='right' title='"+attrName+"' boundary='window'>"+attrName+ ":</span>");
				let sul = $('<ul>');
				li.append(sul);
				sul.attr("class", "list-group list-group-flush");
				actor[attrName].forEach(function (listItem, listIndex) {
					let li = $('<li>');
					li.attr("class", "list-group-item list-group-item-light");
					li.append("<span data-toggle='toolip' data-placement='right' title='"+listItem+"' boundary='window'>"+listItem+ "</span>");
					sul.append(li);
				});

			}
			else if (isString(actor[attrName])) {
				ul.append(actorAnnotationToLi(actor, attrName, annotationName+"."+divName));
			}
			else if (isNumber(actor[attrName])) {
				ul.append(actorAnnotationToLi(actor, attrName, annotationName+"."+divName));
			}
			else if (isObject(actor[attrName])) {
				Object.keys(actor[attrName]).forEach(function (listItem, listIndex) {
					let li = $('<li>');
					ul.append(li);
					li.attr("class", "list-group-item list-group-item-light");
					li.append("<span data-toggle='toolip' data-placement='right' title='"+getAnnotationDisplayName(listItem)+"' boundary='window'>"+getAnnotationDisplayName(listItem)+ ":</span>");
					let sul = $('<ul>');
					li.append(sul);
					sul.attr("class", "list-group list-group-flush");
					let sublist = Object.keys(actor[attrName][listItem]);
					sublist.sort((a, b) => (parseFloat(actor[attrName][listItem][a]) < parseFloat(actor[attrName][listItem][b])) ? 1 : -1);
					sublist.forEach(function (sublistItem, sublistIndex) {
						let li = $('<li>');
						li.attr("class", "list-group-item list-group-item-light");
						li.append("<span data-toggle='toolip' data-placement='right' title='"+sublistItem+"' boundary='window'>"+sublistItem+": "+actor[attrName][listItem][sublistItem]+ "</span>");
						sul.append(li);
					});
				});
			}
		});
	});
}

function actorAnnotationToUl(data, annotationGroupName) {
	let list = Object.keys(data);
	list.sort((a, b) => (parseFloat(data[a]) < parseFloat(data[b])) ? 1 : -1);
	let ul = $('<ul>');
	ul.attr("class", "list-group list-group-flush");
	list.forEach(function(annotationName, index) {
		ul.append(actorAnnotationToLi(data, annotationName, annotationGroupName));

	});
	return ul;
}

function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}

function actorAnnotationToLi(data, annotationName, annotationGroupName) {
	let li = $('<li>');
	li.attr("class", "list-group-item list-group-item-light");
	let displayName = annotationName;
	let fullName = annotationName;
	if (annotationGroupName) {
		fullName = annotationGroupName+"."+annotationName;
	}
	else {
		displayName = getAnnotationDisplayName(annotationName);
	}
	let dataValue = data[annotationName];

	//format differently if scalar, array, object
	//filter out sentiment for now

	li.append("<span data-toggle='toolip' data-placement='right' title='"+fullName+"' boundary='window'>"+displayName+ "</span>: "+ dataValue);
	//where should this click go?
		li.click(function() {
			alert(fullName);
		});
	//}
	return li;
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
		"bg-primary",
		"bg-secondary",
		"bg-success",
		"bg-danger",
		"bg-warning",
		"bg-info"
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
		h5.append(cp);
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
		"bg-primary",
		"bg-secondary",
		"bg-success",
		"bg-danger",
		"bg-warning",
		"bg-info"
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
		li.append(cp);
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
	listMapAnnotationToUl(data, coreNLPAnnotationCategories["Lede"], $('#lede_div'));

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


function displayActorsAnnotation(data, cb) {
	$('#actors_div').empty();
	listActorsAnnotationToUl(data, "OOPActorsAnnotation", $('#actors_div'));

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
			tokenIdx++;
			if (token.hasOwnProperty(annotationName)) {
				if (!(isNaN(token[annotationName])) && (token[annotationName] == 0)) {
					//skip it
				}
				else if (annotationValue === "" || token[annotationName].hasOwnProperty(annotationValue)) {
					$("#token_"+tokenIdx).toggleClass(color);
				}
			}
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
		if (sentence["ParagraphIndexAnnotation"] > paragraphIdx) {
			paragraphIdx = sentence["ParagraphIndexAnnotation"];
			currentParagraphNode = $("<p>");
			currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
			currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
			currentParagraphNode.appendTo('#story_text');
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
			let tokenNode = $("<span>");
			tokenNode.attr("id", "token_"+tokenIdx);
			tokenNode.attr("ref", "token_"+tokenIdx);
			//tokenNode.attr("data-toggle", "popover");
			//tokenNode.attr("data-placement", "top");
			//tokenNode.attr("data-html", "true");
			//tokenNode.attr("data-content", getTokenAnnotation(token));
			if (index === sentence.tokens.length -1) {
				tokenNode.click(function(sentence) {
					return function() {
						window.open("GetDocumentAnalysisScores?Corpus="+corpus+"&Document="+docId+"&Scope=Sentence&Id="+sentence["SentenceIndexAnnotation"], "_documentScores");
					}
				}(sentence));
			}
			else {
				tokenNode.click(function(tokenIdx) {
					return function() {
						window.open("GetDocumentAnalysisScores?Corpus="+corpus+"&Document="+docId+"&Scope=Token&Id="+tokenIdx, "_documentScores");
					}
				}(tokenIdx));
			}
			tokenNode.attr("class", "token");
			tokenNode.text(token["TokensAnnotation"].originalText);
			sentenceNode.append(tokenNode);
			if (token["TokensAnnotation"].after == '\n\n' || token["TokensAnnotation"].after == '\n') {
				sentenceNode.append($("<p>"));
			}
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

	cb && typeof cb === 'function' && cb(data);
}

function displayFactCheckerTextAnnotations(data) {
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
			currentParagraphNode.appendTo('#story_text');
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
			let tokenNode = $("<span>");
			let dataContent = getFactCheckerAnnotations(token);
			if (dataContent.length > '<div class="popover-message"></div>'.length) {
				tokenNode.attr("id", "token_"+tokenIdx);
				tokenNode.attr("ref", "token_"+tokenIdx);
				tokenNode.attr("data-toggle", "popover");
				tokenNode.attr("data-placement", "top");
				tokenNode.attr("data-html", "true");
				tokenNode.attr("data-content", dataContent);
				tokenNode.attr("trigger", "click");
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
}

function displayEditorTextAnnotations(data) {
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
			currentParagraphNode.appendTo('#story_text');
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
}

//unused
function getSentenceAnnotation(token) {
	let omit_annotations = [
	      "tokens",
	      "sentenceIndex",
	      "paragraphIndex",
	      "text",
	      "OOPSyllablesAnnotation",
	      "OOPTemporalNGramsAnnotation",
	      "OOPDolchAnnotation"
	];
	let list = [];
	for (let key in token) {
		if (! omit_annotations.includes(key)) {
			list.push(key);
		}
	};
	list.sort((a, b) => (a > b) ? 1 : -1);

	let retval = "";
	list.forEach(function(scoreName, index) {
		retval+=("<br/>" + token[scoreName] + "<br/>");
	});
	return retval;
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
			if (token[scoreName] > 0) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
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

//getDocumentAnnotations()
//getDocumentAnnotationValues(annotationName)
//getSentences()
//getSentence(sentenceIdx)
//displayAnnotationSelectList(annotations)
//onSelectAnnotation(annotationName)
//displayAnnotationValueSelectList(annotationValues)
//onSelectAnnotationValue(annotationValue)
//drawSentenceSelectList(sentences)
//onSelectSentence(sentenceIdx)
//displayBarChart(annotationName, annotationValue, sentenceIdx)
//onClickBar(annotationName, annotationValue)

function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}

function displayAnnotationSelectList(data, cb) {
	$("#annotationSelectList").empty();
	let excludeAnnotationGroups = [
		"Metadata",
		"Document",
		"Actors"
	];
	let excludeAnnotations = [];
	excludeAnnotationGroups.forEach(function(annotationGroupName, index) {
		coreNLPAnnotationCategories[annotationGroupName].forEach(function(annotationName, index) {
			excludeAnnotations.push(annotationName);
		});
	});

	let annotationList = [];
	for(let key in data) {
		if (!excludeAnnotations.includes(key)) {
			annotationList.push(key);
		}
	}
	annotationList.sort((a, b) => (a > b) ? 1 : -1);

	annotationList.forEach(function(annotationName, index) {
		let btn = $("<button>");
		btn.addClass("dropdown-item");
		btn.attr("type", "button");
		btn.attr("id", "annotationSelectListItem_"+ getAnnotationDisplayName(annotationName));
		btn.text(annotationName);
		btn.click(function() {
			getAnnotation(getProperties(), function(annotationName) {
				return function(d) {
					displayAnnotationValueSelectList(d, annotationName);
				}
			}(annotationName));
		})
		$("#annotationSelectList").append(btn);
	});

	cb && typeof cb === 'function' && cb(data);

}

function displayAnnotationValueSelectList(data, annotationName, cb) {
	$("#annotationValueSelectList").empty();
	let allbtn = $("<button>");
	allbtn.addClass("dropdown-item");
	allbtn.attr("type", "button");
	allbtn.attr("id", "annotationValueSelectListItem_all");
	allbtn.text(annotationName);
	allbtn.click(function() {
		getAnnotation(getProperties(), function(annotationName) {
			return function(param) {
				alert(annotationName);
				//$("#annotationSelectListItem_"+ getAnnotationDisplayName(annotationName)).addClass("active");
			}
		}("all"));
	})
	$("#annotationValueSelectList").append(allbtn);


	let list = Object.keys(data);
	list.sort((a, b) => (parseFloat(data[a]) < parseFloat(data[b])) ? 1 : -1);

	list.forEach(function(annotationValueName, index) {
		let btn = $("<button>");
		btn.addClass("dropdown-item");
		btn.attr("type", "button");
		btn.attr("id", "annotationValueSelectListItem_"+ getAnnotationDisplayName(annotationName + "." + annotationValueName));
		btn.text(annotationValueName);
		btn.click(function() {
			getAnnotation(getProperties(), function(annotationValueName) {
				return function(param) {
					alert(annotationValueName);
					//$("#annotationSelectListItem_"+ getAnnotationDisplayName(annotationName)).addClass("active");
				}
			}(annotationValueName));
		})
		$("#annotationValueSelectList").append(btn);
	});

	cb && typeof cb === 'function' && cb(data);

}
