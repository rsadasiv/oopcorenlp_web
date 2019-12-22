'use strict';
var properties = {};
var annotation = {};
var allAnnotations = [];
/*var allAnnotations = [
	"edu.stanford.nlp.ling.CoreAnnotations$DocIDAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$DocSourceTypeAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$DocTypeAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPBiberDimensionsAnnotation" ,
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdverbsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerblessSentencesAnnotation",

	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPLocationsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdjectivesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounHypernymsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFleschKincaidAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPUntaggedTopicsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPBiberAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordnetGlossAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPColorsAnnotation",

	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTopicsAnnotation",

	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTemporalNGramsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPCommonWordsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdjectiveCategoriesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPunctuationMarkAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSimileAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPGenderAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$CoreNlpGenderAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbGroupsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaRelatedNounsAnnotation",
	"edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbHypernymsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActionlessVerbsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordlessWordsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaCategoriesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNonAffirmativeAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPossessivesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPrepositionsAnnotation",

	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActorsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPronounAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaPageviewTopicsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFlavorsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdverbsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPUncommonWordsAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdverbCategoriesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbTenseAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaGlossAnnotation",

	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPeopleAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$CoreNlpSentimentAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdjectivesAnnotation",
	"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation"
]*/


var coreNLPAnnotationCategories = {
	"Metadata": [
		"edu.stanford.nlp.ling.CoreAnnotations$DocIDAnnotation",
		"edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation",
		"edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation",
		"edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation",
		"edu.stanford.nlp.ling.CoreAnnotations$DocTypeAnnotation",
		"edu.stanford.nlp.ling.CoreAnnotations$DocSourceTypeAnnotation"
	],
	"Document": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFleschKincaidAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$CoreNlpSentimentAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation"
	],
	"Diction": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPCommonWordsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordlessWordsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPUncommonWordsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTemporalNGramsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPunctuationMarkAnnotation"
		
	],
	"Lede": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPeopleAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDatesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbTenseAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPLocationsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSVOAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWhaAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPQuestionsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPQuotesAnnotation"
	],
	"GenderNumber": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPGenderAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$CoreNlpGenderAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPronounAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPossessivesAnnotation"
	],
	"Description": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSimileAnnotationList",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFlavorsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPColorsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdjectivesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdjectivesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdjectiveCategoriesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdverbsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdverbsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdverbCategoriesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPrepositionsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPrepositionCategoriesAnnotation"
	],
	"Semantics": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaPageviewTopicsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaRelatedNounsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaCategoriesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTopicsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounHypernymsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounGroupsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActionlessVerbsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbHypernymsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbGroupsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbnetGroupsAnnotation"
	],
	"Actors": [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActorsAnnotation"
	]
}

function setProperties() {
	//properties.corpus = "Corpora/Submissions";
	//properties.textDir = "Text";
	//properties.annotationDir = "Annotations";
	properties.docId = docId;
	properties.corpus = corpus;
	getAllAnnotations();
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
	let url = "GetDocumentAnalysisStats?Analysis=OOPCoreNLP&Corpus="+ properties.corpus+"&Document="+ properties.docId;
	$.ajax({
		dataType: "json",
        url: url, 
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
			let li = $('<li>');
			ul.append(li);
			li.attr("class", "list-group-item list-group-item-light");
			li.append("<span data-toggle='toolip' data-placement='right' title='"+annotationName+"' boundary='window'>"+annotationName+ ":</span>");
			let sul = $('<ul>');
			li.append(sul);
			sul.attr("class", "list-group list-group-flush");
			data[annotationName].forEach(function (listItem, listIndex) {
				let li = $('<li>');
				li.attr("class", "list-group-item list-group-item-light");
				li.append("<span data-toggle='toolip' data-placement='right' title='"+listItem+"' boundary='window'>"+listItem+ "</span>");
				sul.append(li);
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


function displayMetadataAnnotations(data, cb) {
	$('#metadata_div').empty();
	let ul = $('<ul>');
	ul.attr("class", "list-group list-group-flush");
	ul.appendTo('#metadata_div');
	coreNLPAnnotationCategories["Metadata"].forEach(function(annotationName, index) {
		ul.append(scalarAnnotationToLi(data, annotationName));
	});
	cb && typeof cb === 'function' && cb(data);
}

function displayDocumentAnnotations(data, cb) {
	$('#document_div').empty();
	let ul = mapScalarAnnotationToUl(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation");
	ul.appendTo($('#document_div'));
	ul.attr("class", "list-group list-group-flush");
	ul.append(scalarAnnotationToLi(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation"));
	ul.append(scalarAnnotationToLi(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFleschKincaidAnnotation"));
	ul.append(scalarAnnotationToLi(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$CoreNlpSentimentAnnotation"));
	ul.append(scalarAnnotationToLi(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation"));

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
	
	cb && typeof cb === 'function' && cb(data);
}

function displayUncategorizedAnnotations(data, cb) {
	$('#uncategorized_div').empty();
	let uncategorizedAnnotationsList = [];
	allAnnotations.forEach(function(annotation, allindex) {
		let found = false;
		for (let key in coreNLPAnnotationCategories) {
			coreNLPAnnotationCategories[key].forEach(function(categorizedAnnotation, annindex) {
				if (categorizedAnnotation === annotation) {
					found = true;
				}
			});
		};
		if (!found) {
			uncategorizedAnnotationsList.push(annotation);
		};
	});
	listMapAnnotationToUl(data, uncategorizedAnnotationsList, $('#uncategorized_div'));
	
	cb && typeof cb === 'function' && cb(data);
}


function displayActorsAnnotation(data, cb) {
	$('#actors_div').empty();
	listActorsAnnotationToUl(data, "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActorsAnnotation", $('#actors_div'));

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
		if (sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"] > paragraphIdx) {
			paragraphIdx = sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"];
			currentParagraphNode = $("<p>");
			currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
			currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
			currentParagraphNode.appendTo('#story_text');
		}
		let sentenceNode = $("<span>");
		sentenceNode.attr("id", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("ref", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("class", "sentence");
		//sentenceNode.text(sentence.text + " ");
		currentParagraphNode.append(sentenceNode);
		sentence.tokens.forEach(function(token, index) {
			tokenIdx++;
			sentenceNode.append(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].before);
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
						window.open("GetDocumentAnalysisStats?Analysis=OOPCoreNLP&Corpus="+ properties.corpus+"&Document="+docId+"&Scope=Sentence&Id="+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"], "_documentScores");
					}
				}(sentence));
			}
			else {
				tokenNode.click(function(tokenIdx) {
					return function() {
						window.open("GetDocumentAnalysisStats?Analysis=OOPCoreNLP&Corpus="+ properties.corpus+"&Document="+docId+"&Scope=Token&Id="+tokenIdx, "_documentScores");
					}
				}(tokenIdx));
			}
			tokenNode.attr("class", "token");
			tokenNode.text(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].originalText);
			sentenceNode.append(tokenNode);
			if (token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].after == '\n\n' || token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].after == '\n') {
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
		if (sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"] > paragraphIdx) {
			paragraphIdx = sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"];
			currentParagraphNode = $("<p>");
			currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
			currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
			currentParagraphNode.appendTo('#story_text');
		}
		let sentenceNode = $("<span>");
		sentenceNode.attr("id", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("ref", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("class", "sentence");
		//sentenceNode.text(sentence.text + " ");
		currentParagraphNode.append(sentenceNode);
		sentence.tokens.forEach(function(token, index) {
			tokenIdx++;
			sentenceNode.append(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].before);
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

			tokenNode.text(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].originalText);
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
	data.sentences.forEach(function(sentence, index) {
		if (sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"] > paragraphIdx) {
			paragraphIdx = sentence["edu.stanford.nlp.ling.CoreAnnotations$ParagraphIndexAnnotation"];
			currentParagraphNode = $("<p>");
			currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
			currentParagraphNode.attr("ref", "paragraph_"+paragraphIdx);
			currentParagraphNode.appendTo('#story_text');
		}
		let sentenceNode = {};
		let dataContent = getEditorAnnotationsSentence(sentence);
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
		sentenceNode.attr("id", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("ref", "sentence_"+sentence["edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation"]);
		sentenceNode.attr("class", "sentence");
		//sentenceNode.text(sentence.text + " ");
		currentParagraphNode.append(sentenceNode);
		sentence.tokens.forEach(function(token, index) {
			tokenIdx++;
			sentenceNode.append(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].before);
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

			tokenNode.text(token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].originalText);
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
	      "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation",
	      "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTemporalNGramsAnnotation",
	      "io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDolchAnnotation"
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
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordnetGlossAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaGlossAnnotation"
	];
	let retval = '<div class="popover-message">';
	for (let scoreName in token) {
		if (annotations.includes(scoreName)) {
			let lemma = token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].lemma;
			let glosses = token[scoreName][lemma];
			retval += ("<p>" + glosses[0] + "</p>");
		}
	}
	retval += "</div>";
	return retval;
}

function getEditorAnnotationsSentence(sentence) {
	let annotations = {
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerblessSentencesAnnotation" : 0
	};
	let annotationsDesc = {
			"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFleschKinkiadAnnotation" : .7
		};
	let retval = '<div class="popover-message">';
	for (let scoreName in sentence) {
		if (Object.keys(annotations).includes(scoreName)) {
			if (sentence[scoreName] > annotations[scoreName]) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
		else if (Object.keys(annotationsDesc).includes(scoreName)) {
			if (sentence[scoreName] < annotations[scoreName]) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
	}
	if (sentence.tokens.length > 50) {
		retval += ("<p>" + "sentence.tokens.length" + "</p>");
	}
	retval += "</div>";
	return retval;
}

function getEditorAnnotationsToken(token) {
	let annotations = [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWordlessWordsAnnotation"
	];
	let annotationsLemma = [
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdverbsAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPointlessAdjectivesAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNonAffirmativeAnnotation",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAngliciseAnnotation"
	];
	let subAnnotations = {
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPTemporalNGramsAnnotation" : "novel",
		"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPBiberAnnotation" : "PASS"
	};
	let retval = '<div class="popover-message">';
	for (let scoreName in token) {
		if (annotations.includes(scoreName)) {
			if (token[scoreName] > 0) {
				retval += ("<p>" + getAnnotationDisplayName(scoreName) + "</p>");
			}
		}
		else if (annotationsLemma.includes(scoreName)) {
			let lemma = token["edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation"].lemma;		
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
