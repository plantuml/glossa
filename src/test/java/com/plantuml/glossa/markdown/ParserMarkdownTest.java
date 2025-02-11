package com.plantuml.glossa.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.plantuml.glossa.GlossaTag;
import com.plantuml.glossa.PeekerUtils;

class ParserMarkdownTest {

	@Test
	void testEmpty() {
		String wiki = "";
		List<GlossaTag> result = new ParserMarkdown().parse(PeekerUtils.peeker(Arrays.asList(wiki)));
		assertEquals("[]", result.toString());
	}

	@Test
	void testHello() {
		String wiki = "hello";
		List<GlossaTag> result = new ParserMarkdown().parse(PeekerUtils.peeker(Arrays.asList(wiki)));
		assertEquals("[《text〡content〓hello》]", result.toString());
	}

	@Test
	void testHelloBoldWorld() {
		String wiki = "**hello**world";
		List<GlossaTag> result = new ParserMarkdown().parse(PeekerUtils.peeker(Arrays.asList(wiki)));
		assertEquals("[《text〡bold〡content〓hello》, 《text〡content〓world》]", result.toString());
	}

	@Test
	void testHelloWorldBold() {
		String wiki = "hello**world**";
		List<GlossaTag> result = new ParserMarkdown().parse(PeekerUtils.peeker(Arrays.asList(wiki)));
		assertEquals("[《text〡content〓hello》, 《text〡bold〡content〓world》]", result.toString());
	}

	@Test
	void test3lines() {
		List<String> wiki = new ArrayList<>();
		wiki.add("hello**world**");
		wiki.add("ok *italic* and `i=42`");
		wiki.add("This text is ***really important***.");
		List<GlossaTag> result = new ParserMarkdown().parse(PeekerUtils.peeker(wiki));
		assertEquals(
				"[《text〡content〓hello》, 《text〡bold〡content〓world》, 《br》, 《text〡content〓ok 》, 《text〡content〓italic〡italic》, 《text〡content〓 and 》, 《text〡code〡content〓i=42》, 《br》, 《text〡content〓This text is 》, 《text〡bold〡content〓really important〡italic》, 《text〡content〓.》]",
				result.toString());
	}

}
