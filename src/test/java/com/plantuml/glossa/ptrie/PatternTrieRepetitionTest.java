package com.plantuml.glossa.ptrie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PatternTrieRepetitionTest {

	
	@Test
	public void testPatternTrieMatching01() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("he〸lo");
		assertEquals("", trie.getLongestMatchStartingIn("heo", 0));
		assertEquals("helo", trie.getLongestMatchStartingIn("helo", 0));
		assertEquals("hello", trie.getLongestMatchStartingIn("hello", 0));
		assertEquals("helllo", trie.getLongestMatchStartingIn("helllo", 0));
	}
	
}
