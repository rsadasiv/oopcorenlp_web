function makeActorCloud(selectedActor, annotationName, svgName) {
	$.when( 
			$.ajax(
				{
					dataType: "json",
					//url: "rest/browse/Corpora/"+getProperties()["corpus"]+"/"+getProperties()["docId"]+"/OOP"
					url: "rest/api/ActorAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Actor="+encodeURI(selectedActor)+"&Annotation="+annotationName+"&Format=Cloud"
				}
			)
		).done(
				function(rawData) {
//					let data = [];
//					for (const actor of rawData.OOPActorsAnnotation) {
//						if (actor.canonicalName == selectedActor && actor.attributes.hasOwnProperty(annotationName)) {
//							for (const annotation of Object.keys(actor.attributes[annotationName])) {
//								if (isNumber(actor.attributes[annotationName][annotation]) || isString(actor.attributes[annotationName][annotation])) {
//									data.push({"text": annotation, "size": actor.attributes[annotationName][annotation]});
//								}
//								else {
//									data.push({"text": annotation, "size": 0});
//								}
//							}
//							data.sort((a, b) => (parseFloat(a.size) < parseFloat(b.size)) ? 1 : -1);

						    d3.wordcloud()
						    .size([400, 200])
						    .selector(svgName)
						    .scale('linear')
						    .spiral('rectangular')
						    .words($.extend(true, [], rawData.slice(0,50)))
						    .start();
//						}
//					}
				});
		
}