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
var properties = {};


function getProperties() {
	return properties;
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

function getBaseUrl() {
	return "GetDocumentAnalysisScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scope=Document"
	//return "Corpora/"+getProperties()["corpus"]+"/OOP_"+getProperties()["docId"]+".json";
}

function getSentenceAnnotationsUrl(annotationName) {
	return "GetDocumentAnalysisScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scope=SentencesAnnotation&Annotation="+annotationName
}

function getSentenceAnnotationsScoresUrl(annotationName, scoreName) {
	return "GetDocumentAnalysisScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scope=SentencesAnnotation&Annotation="+annotationName+"&Subannotation="+scoreName
}

function getTokenAnnotationsUrl(annotationName) {
	return "GetDocumentAnalysisScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scope=TokensAnnotation&Annotation="+annotationName
}

function getTokenAnnotationScoresUrl(annotationName, scoreName) {
	return "GetDocumentAnalysisScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scope=TokensAnnotation&Annotation="+annotationName+"&Subannotation="+scoreName
}

function getAnnotationDisplayName(annotationName) {
	let endIdx = annotationName.lastIndexOf("Annotation");
	if (endIdx == -1) {
		endIdx = annotationName.length;
	}
	return annotationName.substring(annotationName.lastIndexOf("$")+1, endIdx);
}
