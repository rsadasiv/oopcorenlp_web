package io.outofprintmagazine.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JsonSort {


	public static void sort(JsonNode tree) {

		if (tree.isObject()) {
			sortObject(tree);
		} else if (tree.isArray()) {
			sortArray(tree);
		}

	}

	private static void sortArray(JsonNode tree) {

		for (JsonNode jsonNode : tree) {
			sort(jsonNode);
		}
		List<JsonNode> asList = new ArrayList<JsonNode>();
		Iterator<JsonNode> fieldsIter = tree.elements();
		while (fieldsIter.hasNext()) {
			asList.add(fieldsIter.next());
		}
		Collections.sort(asList, new JsonNodeComparator());
		((ArrayNode) tree).removeAll();
		((ArrayNode) tree).addAll(asList);
	}

	private static void sortObject(JsonNode tree) {
		List<String> asList = new ArrayList<String>();
		Iterator<String> fieldsIter = tree.fieldNames();
		while (fieldsIter.hasNext()) {
			asList.add(fieldsIter.next());
		}
		Collections.sort(asList);
		LinkedHashMap<String, JsonNode> map = new LinkedHashMap<String, JsonNode>();
		for (String f : asList) {

			JsonNode value = tree.get(f);
			sort(value);
			map.put(f, value);
		}
		((ObjectNode) tree).removeAll();
		((ObjectNode) tree).setAll(map);
	}

	// private static void gen(JsonNode tree, JsonGenerator generator)
	// throws JsonGenerationException, IOException {
	//
	// if (tree.isObject()) {
	// genObject(tree, generator);
	// } else if (tree.isArray()) {
	// genArray(tree, generator);
	// } else if (tree.isValueNode()) {
	// genValue(tree, generator);
	// }
	//
	// }
	//
	// private static void genValue(JsonNode tree, JsonGenerator generator)
	// throws JsonGenerationException, IOException {
	// generator.writeString(tree.asText());
	// }
	//
	// private static void genArray(JsonNode tree, JsonGenerator generator)
	// throws JsonGenerationException, IOException {
	// generator.writeStartArray();
	// List<JsonNode> list = Lists.newArrayList(tree.elements());
	// for (JsonNode jsonNode : list) {
	// gen(jsonNode, generator);
	// }
	//
	// generator.writeEndArray();
	// }
	//
	// private static void genObject(JsonNode tree, JsonGenerator generator)
	// throws IOException, JsonGenerationException {
	// generator.writeStartObject();
	//
	// for (Iterator<String> iterator = tree.fieldNames(); iterator.hasNext();)
	// {
	// String f = iterator.next();
	// JsonNode fValue = tree.get(f);
	// generator.writeFieldName(f);
	// gen(fValue, generator);
	// }
	//
	// generator.writeEndObject();
	// }

	public static class JsonNodeComparator implements Comparator<JsonNode> {
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
				return compObject(o1, o2);
			} else if (o1.isArray() && o2.isArray()) {
				return compArray(o1, o2);
			} else if (o1.isValueNode() && o2.isValueNode()) {
				return compValue(o1, o2);
			} else {
				return 1;
			}
		}

		private int compValue(JsonNode o1, JsonNode o2) {

			if (o1.isNull()) {
				return -1;
			}

			if (o2.isNull()) {
				return 1;
			}

			if (o1.isNumber() && o2.isNumber()) {
				return o1.decimalValue().compareTo(o2.decimalValue());
			}

			return o1.asText().compareTo(o2.asText());
		}

		private int compArray(JsonNode o1, JsonNode o2) {

			int c = ((ArrayNode) o1).size() - ((ArrayNode) o2).size();
			if (c != 0) {
				return c;
			}
			for (int i = 0; i < ((ArrayNode) o1).size(); i++) {
				c = compare(o1.get(i), o2.get(i));
				if (c != 0) {
					return c;
				}
			}

			return 0;
		}

		private int compObject(JsonNode o1, JsonNode o2) {

			String id1 = o1.get("id") == null ? null : o1.get("id").asText();
			String id2 = o2.get("id") == null ? null : o2.get("id").asText();
			if (id1 != null) {
				int c = id1.compareTo(id2);
				if (c != 0) {
					return c;
				}
			}
			int c = ((ObjectNode) o1).size() - ((ObjectNode) o2).size();
			if (c != 0) {
				return c;
			}

			Iterator<String> fieldNames1 = ((ObjectNode) o1).fieldNames();
			Iterator<String> fieldNames2 = ((ObjectNode) o2).fieldNames();
			for (; fieldNames1.hasNext();) {
				String f = fieldNames1.next();

				c = f.compareTo(fieldNames2.next());
				if (c != 0) {
					return c;
				}

				JsonNode n1 = o1.get(f);
				JsonNode n2 = o2.get(f);
				c = compare(n1, n2);
				if (c != 0) {
					return c;
				}
			}
			return 0;
		}
	}
}