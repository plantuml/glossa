package com.plantuml.glossa.ptrie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PatternTrieNamedGroupTest {

	@Test
	public void testPatternNamedGroup() {

		PatternTrie trie = new PatternTrie();
		trie.addPattern("a〔group1〡〸b〕c");
		assertEquals("", trie.getLongestMatchStartingIn("heo", 0));
		assertEquals("abc", trie.getLongestMatchStartingIn("abc", 0));
		assertEquals("abbc", trie.getLongestMatchStartingIn("abbc", 0));
		assertEquals("abbbc", trie.getLongestMatchStartingIn("abbbc", 0));

	}

}
