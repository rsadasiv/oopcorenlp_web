package io.outofprintmagazine.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.util.JsonSort;

public class JsonSort_Test {

	public JsonSort_Test() {
		super();
	}
	
	@Test
	public void sortArray() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ar = mapper.createArrayNode();
		ar.add(
				mapper.createObjectNode()
				.put("name", "Arthur")
				.put("value", new BigDecimal(3))
		);
		ar.add(
				mapper.createObjectNode()
				.put("name", "Bertha")
				.put("value", new BigDecimal(0))
		);
		ar.add(
				mapper.createObjectNode()
				.put("name", "Zelda")
				.put("value", new BigDecimal(30))
		);

		JsonSort.sort(ar);
		Iterator<JsonNode> fieldIter = ar.elements();
		assertTrue(fieldIter.hasNext(), "array resized" );
		assertEquals("Zelda", fieldIter.next().get("name").asText(), "array not sorted");
	}
	
	@Test
	public void sortObject() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode o = mapper.createObjectNode();
		o.put("Bertha", new BigDecimal(3));
		o.put("Arthur", new BigDecimal(0));
		o.put("Zelda", new BigDecimal(30));
		JsonSort.sort(o);
		Iterator<String> fieldIter = o.fieldNames();
		assertTrue(fieldIter.hasNext(), "fields removed");
		assertEquals("Zelda", fieldIter.next(), "object not sorted");
	}

}
