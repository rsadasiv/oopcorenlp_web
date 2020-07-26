package io.outofprintmagazine.web.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JsonSort {


	public static void sort(JsonNode tree) {

		if (tree.isObject()) {
			sortObject((ObjectNode)tree);
		} else if (tree.isArray()) {
			sortArray((ArrayNode)tree);
		}

	}
	
	private static void sortArray(ArrayNode ar) {
		List<JsonNode> asList = new ArrayList<JsonNode>();
		for (JsonNode n : ar) {
			asList.add(n);
		}
		Collections.sort(asList, new PhraseAnnotationComparator());

		ar.removeAll();
		ar.addAll(asList);
	}

	private static void sortObject(ObjectNode tree) {
		ObjectMapper mapper = new ObjectMapper();
		List<JsonNode> asList = new ArrayList<JsonNode>();
		Iterator<String> fieldsIter = tree.fieldNames();
		while (fieldsIter.hasNext()) {
			String fieldName = fieldsIter.next();
			asList.add(
					mapper.createObjectNode()
					.put("name", fieldName)
					.put("value", new BigDecimal(tree.get(fieldName).asText()))
			);
		}
		Collections.sort(asList, new PhraseAnnotationComparator());
		tree.removeAll();
		for (JsonNode n : asList) {
			tree.put(n.get("name").asText(), n.get("value").asText());
		}
	}


	public static class PhraseAnnotationComparator implements Comparator<JsonNode> {
		public int compare(JsonNode o1, JsonNode o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}

			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			if (o1.isObject() && o2.isObject()) {
				int retval = new BigDecimal(o2.get("value").asText()).compareTo(new BigDecimal(o1.get("value").asText()));
				if (retval == 0) {
					retval = o1.get("name").asText().compareTo(o2.get("name").asText());
				}
				return retval;
			} else {
				return 1;
			}
		}
	}


	
	public static void sortActors(JsonNode tree) {
		List<JsonNode> asList = new ArrayList<JsonNode>();
		Iterator<JsonNode> iter = tree.elements();
		while (iter.hasNext()) {
			asList.add(iter.next());
		}
		Collections.sort(asList, new ActorsNodeComparator());
		((ArrayNode) tree).removeAll();
		((ArrayNode) tree).addAll(asList);
	}
	
	
	public static class ActorsNodeComparator implements Comparator<JsonNode> {
		public int compare(JsonNode o1, JsonNode o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}

			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			if (o1.isObject() && o2.isObject()) {
				int retval = new BigDecimal(o2.get("importance").asText("0")).compareTo(new BigDecimal(o1.get("importance").asText("0")));
				if (retval == 0) {
					retval = o1.get("canonicalName").asText().compareTo(o2.get("canonicalName").asText());
				}
				return retval;
			} else {
				return 1;
			}
		}
	}
	
	
}