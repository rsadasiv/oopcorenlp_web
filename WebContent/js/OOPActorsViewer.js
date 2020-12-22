function makeActorCloud(selectedActor, svgName) {
	let baseUrl = "rest/api/ActorAnnotation?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Actor="+encodeURI(selectedActor)+"&Annotation="
	let data = []
	$.when($.ajax({dataType: "json", url: baseUrl+"OOPNounsAnnotation"})
	)
	.done(function(OOPNounsAnnotation) {
		data = data.concat(OOPNounsAnnotation);
		$.when(	$.ajax({dataType: "json", url: baseUrl+"OOPVerbsAnnotation"}))
		.done(function(OOPVerbsAnnotation) {
			data = data.concat(OOPVerbsAnnotation);
			$.when(	$.ajax({dataType: "json", url: baseUrl+"OOPAdjectivesAnnotation"}))
			.done(function(OOPAdjectivesAnnotation) {
				data = data.concat(OOPAdjectivesAnnotation);
				$.when( $.ajax({dataType: "json", url: baseUrl+"OOPAdverbsAnnotation"}))
				.done(function(OOPAdverbsAnnotation) {
					data = data.concat(OOPAdverbsAnnotation);
					drawVegaCloud(data, svgName)
				})
			})
		})
	})
}

function drawVegaCloud(data, svgName) {

	let chartSpec = {
	  "$schema": "https://vega.github.io/schema/vega/v5.json",
	  "description": "Character's attributes.",
	  "width": 800,
	  "height": 400,
	  "padding": 0,
	  "title": "",
	  "data": [
	    {
	      "name": "table",
	      "values": data,
		  "transform": [
	        {
	          "type": "formula", "as": "angle",
	          "expr": "[-45, -30, 0, 30, 45, 90][~~(random() * 6)]"
	        }
		  ],
	    }
	  ],		  
	  "scales": [
	    {
	      "name": "color",
	      "type": "ordinal",
	      "domain": {"data": "table", "field": "name"},
	      "range": {"scheme": "category20b"}
	    }
	  ],

	  "marks": [
	    {
	      "type": "text",
	      "from": {"data": "table"},
	      "encode": {
	        "enter": {
	          "text": {"field": "name"},
	          "align": {"value": "center"},
	          "baseline": {"value": "alphabetic"},
	          "fill": {"scale": "color", "field": "name"}
	        },
	        "update": {
	          "fillOpacity": {"value": 1}
	        },
	        "hover": {
	          "fillOpacity": {"value": 0.5}
	        }
	      },
	      "transform": [
	        {
	          "type": "wordcloud",
	          "size": [800, 400],
	          "rotate": {"field": "datum.angle"},
	          "font": "serif",
	          "fontSize": {"field": "datum.value"},
	          "fontSizeRange": [18, 96],
	          "padding": 2
	        }
	      ]
	    }
	  ]
	}
;
	vegaEmbed(svgName, chartSpec, {"renderer": "svg"});	
	
}	