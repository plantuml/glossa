package com.plantuml.glossa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GlossaTagTest {

	@Test
	void testToString() {
		GlossaTag tag = new GlossaTag("text");
		tag.addMetadata("color", "red");
		tag.addMetadata("bold");
		String expected = "《text〡bold〡color〓red》";
		assertEquals(expected, tag.toString());
	}

}
