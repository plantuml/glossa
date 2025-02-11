package com.plantuml.glossa.ptrie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PatternTrieRangeTest {

	
	@Test
	public void testPatternTrieMatching01() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("he");
		trie.addPattern("hello");
		trie.addPattern("helium");
		trie.addPattern("z「a〜c」「a〜e」ou");
		assertEquals("zaaou", trie.getLongestMatchStartingIn("zaaou", 0));
		assertEquals("zaeou", trie.getLongestMatchStartingIn("zaeou", 0));
		assertEquals("", trie.getLongestMatchStartingIn("zaou", 0));
	}
	
	@Test
	public void testPatternTrieMatching() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("he");
		trie.addPattern("hello");
		trie.addPattern("helium");
		trie.addPattern("「a〜c」ou");

		// Test matching for patterns "he", "hello", and "helium"
		// For input "hello", the longest complete match should be "hello"
		assertEquals("hello", trie.getLongestMatchStartingIn("hello", 0));

		// For input "helium", the longest complete match should be "helium"
		assertEquals("helium", trie.getLongestMatchStartingIn("helium", 0));

		// For input "hel", only the pattern "he" is complete, so the match should be
		// "he"
		assertEquals("he", trie.getLongestMatchStartingIn("hel", 0));

		// Test matching of the pattern with range "「a〜c」ou":
		// This pattern should match any string starting with 'a', 'b' or 'c' followed
		// by "ou".
		// For input "aou", the match should be "aou".
		assertEquals("aou", trie.getLongestMatchStartingIn("aou", 0));

		// For input "bou", the match should be "bou".
		assertEquals("bou", trie.getLongestMatchStartingIn("bou", 0));

		// For input "cou", the match should be "cou".
		assertEquals("cou", trie.getLongestMatchStartingIn("cou", 0));

		// For input "dou", no match should be found (since 'd' is not in the range
		// a〜c).
		assertEquals("", trie.getLongestMatchStartingIn("dou", 0));

		// Test matching with an offset:
		// For the string "say hello", starting at index 4, the longest match should be
		// "hello".
		assertEquals("hello", trie.getLongestMatchStartingIn("say hello", 4));
	}

}
