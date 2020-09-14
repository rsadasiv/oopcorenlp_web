'use strict';
//Load Brat libraries
var bratLocation = 'brat-master';
head.js(
	    // External libraries
	    bratLocation + '/client/lib/jquery.min.js',
	    bratLocation + '/client/lib/jquery.svg.min.js',
	    bratLocation + '/client/lib/jquery.svgdom.min.js',

	    // brat helper modules
	    bratLocation + '/client/src/configuration.js',
	    bratLocation + '/client/src/util.js',
	    bratLocation + '/client/src/annotation_log.js',
	    bratLocation + '/client/lib/webfont.js',

	    // brat modules
	    bratLocation + '/client/src/dispatcher.js',
	    bratLocation + '/client/src/url_monitor.js',
	    bratLocation + '/client/src/visualizer.js'
);

var webFontURLs = [
    bratLocation + '/static/fonts/Astloch-Bold.ttf',
    bratLocation + '/static/fonts/PT_Sans-Caption-Web-Regular.ttf',
    bratLocation + '/static/fonts/Liberation_Sans-Regular.ttf'
];

var properties = {};

function getProperties() {
	return properties;
}

function setProperties() {
	properties.docId = docId;
	properties.corpus = corpus;
}

function posEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
	    if (tokens.length > 0 && typeof tokens[0].pos !== 'undefined') {
	      for (let i = 0; i < tokens.length; i++) {
	        let token = tokens[i];
	        let pos = token.pos;
	        if (!entityTypesSet[pos]) {
	        	entityTypesSet[pos] = pos;
	        	entityTypes.push(
	            	{
	            		type: pos,
	            		labels : [pos],
	            		bgColor: posColor(pos),
	            		borderColor: 'darken'
	            	}
	            );
	        }
	      }
	    }
	}
	return entityTypes;
}

function posEntities(data) {
	let currentEntities = [];
	let currentText = '';
	let entityTypesSet = {};
	let entityTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
	    if (tokens.length > 0 && typeof tokens[0].pos !== 'undefined') {
	      for (let i = 0; i < tokens.length; i++) {
	          let token = tokens[i];
	          let pos = token.pos;
	          let begin = parseInt(currentText.length);
	          currentText += token["originalText"];
	          let end = parseInt(currentText.length);
	          currentText += token["after"];
	          if (posColor(pos) != '#E3E3E3') {
	        	  currentEntities.push(['POS_' + sentI + '_' + i, pos, [[begin, end]]]);
	          }
	      }
	    }
	    //currentText += '\n';
	}
	return {text: currentText, entities: currentEntities};
}

function getBaseUrl() {
	return "GetDocumentScores?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Scores=STANFORD";
	//return "Corpora/"+getProperties()["corpus"]+"/OOP_"+getProperties()["docId"]+".json";
}

function displayPos(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
				Util.embed(
					"story_text",
					{
    					entity_types: posEntityTypes(data)
					},
					posEntities(data)
    			);
			}
		);
}

function sentimentEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI]; 
		let sentiment = sentence.sentiment.toUpperCase().replace("VERY", "VERY ");
		if (!entityTypesSet[sentiment]) {
			entityTypesSet[sentiment] = sentiment;
	    	entityTypes.push(
	        	{
	        		type: sentiment,
	        		labels : [sentiment],
	        		bgColor: sentimentColor(sentiment),
	        		borderColor: 'darken'
	        	}
	        );
	    }
	}
	return entityTypes;
}

function sentimentEntities(data) {
	let currentEntities = [];
	let currentText = '';

	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let tokens = sentence.tokens;	
	    if (tokens.length > 0) {
			let begin = parseInt(currentText.length);
			let sentiment = sentence.sentiment.toUpperCase().replace("VERY", "VERY ");
			for (let i = 0; i < tokens.length; i++) {
				let token = tokens[i];	          
				currentText += token["originalText"];
				currentText += token["after"];
			}
			//end does not work - probably due to non-ascii characters
			//let end = parseInt(currentText.length)-1;
			//if ((end - begin) > 129 ) {
			//	end = begin + 129;
			//}
			//if (sentiment != "" && end>begin && tokens.length > 2 && tokens.length < 15) {
				currentEntities.push(['SENTIMENT_' + sentI, sentiment, [[begin, begin+sentiment.length]]]);
			//}
	    }
	    //currentText += '\n';
	}
	return {text: currentText, entities: currentEntities};
}

function displaySentiment(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
					Util.embed(
							"story_text",
							{
								entity_types: sentimentEntityTypes(data)
							},
							sentimentEntities(data)
					);
			}
		);
}

function nerEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
	    if (tokens.length > 0 && typeof tokens[0].pos !== 'undefined') {
	        for (let i = 0; i < tokens.length; i++) {
	            let ner = tokens[i].ner || 'O';
	            if (ner == 'O') continue;
				entityTypesSet[ner] = ner;
		    	entityTypes.push(
		        	{
		        		type: ner,
		        		labels : [ner],
		        		bgColor: nerColor(ner),
		        		borderColor: 'darken'
		        	}
		        );
	          }
	    }
	}
	return entityTypes;
}

function nerEntities(data) {
	let currentEntities = [];
	let currentText = '';
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	

        for (let i = 0; i < tokens.length; i++) {
        	let token = tokens[i]
        	let begin = parseInt(currentText.length);
			currentText += token["originalText"];
			currentText += token["after"];
            var ner = token.ner || 'O';
            if (ner != 'O') {
				currentEntities.push(['NER_' + sentI + '_' + i, ner, [[begin, currentText.length]]]);
            }
	    }
	}
	return {text: currentText, entities: currentEntities};
}

function displayNer(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
					Util.embed(
							"story_text",
							{
								entity_types: nerEntityTypes(data)
							},
							nerEntities(data)
					);
			}
		);
}

function nerPersonEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
	    if (tokens.length > 0 && typeof tokens[0].pos !== 'undefined') {
	        for (let i = 0; i < tokens.length; i++) {
	            let ner = tokens[i].ner || 'O';
	            if (ner == 'O') continue;
				entityTypesSet[ner] = ner;
		    	entityTypes.push(
		        	{
		        		type: ner,
		        		labels : [ner],
		        		bgColor: nerColor(ner),
		        		borderColor: 'darken'
		        	}
		        );
	          }
	    }
	}
	return entityTypes;
}

function nerPersonEntities(data) {
	let currentEntities = [];
	let currentText = '';
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	

        for (let i = 0; i < tokens.length; i++) {
        	let token = tokens[i]
        	let begin = parseInt(currentText.length);
			currentText += token["originalText"];
			currentText += token["after"];
            var ner = token.ner || 'O';
            if (ner == "PERSON" || ner == "LOCATION") {
				currentEntities.push(['NER_' + sentI + '_' + i, ner, [[begin, currentText.length]]]);
            }
	    }
	}
	return {text: currentText, entities: currentEntities};
}

function displayNerPerson(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
					Util.embed(
							"story_text",
							{
								entity_types: nerPersonEntityTypes(data)
							},
							nerPersonEntities(data)
					);
			}
		);
}


function parseEntityTypes(data) {
	let entityTypes = [];
	return entityTypes;
}

function parseEntities(data) {
	let currentEntities = [];
	let currentText = '';
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
		currentText+=sentence["parse"];
		currentText+='\n';
        for (let i = 0; i < tokens.length; i++) {
        	let token = tokens[i];
			currentText += token["originalText"];
			currentText += token["after"];
	    }
        currentText+="\n\n";
	}
	return {text: currentText, entities: currentEntities};
}

function displayParse(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
					Util.embed(
							"story_text",
							{
								entity_types: parseEntityTypes(data)
							},
							parseEntities(data)
					);
			}
		);
}

function corefEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = [	            	
		{
			type: 'COREF',
			labels: ['COREF'],
			bgColor: '#FFE000',
			borderColor: 'darken'
		}
	];

	let relationTypes = [
		{
			type: 'coref',
			labels: ['coref']
		}
	];
	return {entity_types: entityTypes, relation_types: relationTypes};
}

function corefEntities(data) {
	let currentEntities = [];
	let currentText = '';
	let entityTypesSet = {};
	let entityTypes = [];
	let currentRelations = [];
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;	
        for (let i = 0; i < tokens.length; i++) {
        	let token = tokens[i]
			currentText += token["originalText"];
			currentText += token["after"];
        }
	}
	Object.keys(data.corefs).forEach( function (clusterId) {
		let chain = data.corefs[clusterId];
		if (chain.length > 1) {
			for (let i = 0; i < chain.length; ++i) {
				let mention = chain[i];
				let id = 'COREF' + mention.id;
				let tokens = data.sentences[mention.sentNum - 1].tokens;
				currentEntities.push(
						[
							id, 
							'Mention',
							[[
								tokens[mention.startIndex - 1].characterOffsetBegin,
								tokens[mention.endIndex - 2].characterOffsetEnd-2
							]] 
						]
				);
	          if (i > 0) {
	        	  let lastId = 'COREF' + chain[i - 1].id;
	        	  currentRelations.push(
	        			  [
	        				  'COREF' + chain[i-1].id + '_' + chain[i].id,
	        				  'coref',
	                          [
	                        	  ['governor', lastId],
	                        	  ['dependent', id]    
	                        ] 
	        			]
	        	);
	          }
	        }
	    }
	  }
	);
	return {text: currentText, entities: currentEntities, relations: currentRelations};
}

function displayCoref(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
				Util.embed(
					"story_text",
					corefEntityTypes(data),
					corefEntities(data)
    			);
			}
		);
}

function depEntityTypes(data) {
	let entityTypesSet = {};
	let entityTypes = posEntityTypes(data);
	let relationTypesSet = {};
	let relationTypes = [];
	//for each sentence
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let deps = sentence['basicDependencies'];
		for (let i = 0; i < deps.length; i++) {
	        let dep = deps[i];
	        let governor = dep.governor - 1;
	        let dependent = dep.dependent - 1;
	        if (governor != -1) {
	        	if (!relationTypesSet[dep.dep]) {
	        		relationTypesSet[dep.dep] = true;
	        		relationTypes.push(
	        				{
	        					type: dep.dep,
	        					labels: [dep.dep]
	        				}
	        		)
	        	}
	        }
		}
	}
	return {entity_types: entityTypes, relation_types: relationTypes};
}

function depEntities(data) {
	let currentEntities = posEntities(data)["entities"];
	let currentText = '';
	let entityTypesSet = {};
	let entityTypes = [];
	let currentRelations = [];
	for (let sentI = 0; sentI < data.sentences.length; ++sentI) {
		let sentence = data.sentences[sentI];
		let index = sentence.index;
		let tokens = sentence.tokens;
		let deps = sentence['basicDependencies'];
        for (let i = 0; i < tokens.length; i++) {
        	let token = tokens[i]
			currentText += token["originalText"];
			currentText += token["after"];
        }
        var relations = [];
        // Format: [${ID}, ${TYPE}, [[${ARGNAME}, ${TARGET}], [${ARGNAME}, ${TARGET}]]]
        for (var i = 0; i < deps.length; i++) {
          var dep = deps[i];
          var governor = dep.governor - 1;
          var dependent = dep.dependent - 1;
          if (governor != -1) {
      		currentRelations.push(['dep' + '_' + sentI + '_' + i, dep.dep, [['governor', 'POS_' + sentI + '_' + governor], ['dependent', 'POS_' + sentI + '_' + dependent]]]);
          }
        }
	}

	return {text: currentText, entities: currentEntities, relations: currentRelations};
}

function displayDep(divId) {
	$.when( 
		$.ajax(
			{
				dataType: "json",
				url: getBaseUrl()
			}
		)
	).done(
			function(data) {
				Util.embed(
					"story_text",
					depEntityTypes(data),
					depEntities(data)
    			);
			}
		);
}

head.ready(function() {
//    Util.embed(
//        // id of the div element where brat should embed the visualisations
//    		"story_text",
//        // object containing collection data
//        collData,
//        // object containing document data
//        docData
//        );
    
    //displayPos("story_text");
	//displaySentiment("story_text");
	//displayNer("story_text");
	//displayParse("story_text");
	//displayCoref("story_text");
	//displayDep("story_text");
});


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


/**
 * A reverse map of PTB tokens to their original gloss
 */
var tokensMap = {
  '-LRB-': '(',
  '-RRB-': ')',
  '-LSB-': '[',
  '-RSB-': ']',
  '-LCB-': '{',
  '-RCB-': '}',
  '``': '"',
  '\'\'': '"',
};

/**
 * A mapping from part of speech tag to the associated
 * visualization color
 */
function posColor(posTag) {
  if (posTag.startsWith('N')) {
    return '#A4BCED';
  } else if (posTag.startsWith('V') || posTag.startsWith('M')) {
    return '#ADF6A2';
  } else if (posTag.startsWith('P')) {
    return '#CCDAF6';
  } else if (posTag.startsWith('I')) {
    return '#FFE8BE';
  } else if (posTag.startsWith('R') || posTag.startsWith('W')) {
    return '#FFFDA8';
  } else if (posTag.startsWith('D') || posTag === 'CD') {
    return '#CCADF6';
  } else if (posTag.startsWith('J')) {
    return '#FFFDA8';
  } else if (posTag.startsWith('T')) {
    return '#FFE8BE';
  } else if (posTag.startsWith('E') || posTag.startsWith('S')) {
    return '#E4CBF6';
  } else if (posTag.startsWith('CC')) {
    return '#FFFFFF';
  } else if (posTag === 'LS' || posTag === 'FW') {
    return '#FFFFFF';
  } else {
    return '#E3E3E3';
  }
}

/**
 * A mapping from named entity tag to the associated
 * visualization color
 */
function nerColor(nerTag) {
  if (nerTag === 'PERSON') {
    return '#FFCCAA';
  } else if (nerTag === 'ORGANIZATION') {
    return '#8FB2FF';
  } else if (nerTag === 'MISC') {
    return '#F1F447';
  } else if (nerTag === 'LOCATION') {
    return '#95DFFF';
  } else if (nerTag === 'DATE' || nerTag === 'TIME' || nerTag === 'SET') {
    return '#9AFFE6';
  } else if (nerTag === 'MONEY') {
    return '#FFFFFF';
  } else if (nerTag === 'PERCENT') {
    return '#FFA22B';
  } else {
    return '#E3E3E3';
  }
}


/**
 * A mapping from sentiment value to the associated
 * visualization color
 */
function sentimentColor(sentiment) {
  if (sentiment === "VERY POSITIVE") {
    return '#00FF00';
  } else if (sentiment === "POSITIVE") {
    return '#7FFF00';
  } else if (sentiment === "NEUTRAL") {
    return '#FFFF00';
  } else if (sentiment === "NEGATIVE") {
    return '#FF7F00';
  } else if (sentiment === "VERY NEGATIVE") {
    return '#FF0000';
  } else {
    return '#E3E3E3';
  }
}


