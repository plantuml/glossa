package com.plantuml.glossa.ptrie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PatternTrieSimpleTest {

	/**
	 * Tests the basic functionality of add() and getLonguestMatchStartingIn() using
	 * a single word.
	 */
	@Test
	public void testAddAndGetLongestMatch() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("hello");

		// Test exact match
		assertEquals("hello", trie.getLongestMatchStartingIn("hello", 0));

		// Test match with additional trailing characters
		assertEquals("hello", trie.getLongestMatchStartingIn("hello world", 0));

		// Test match starting at an offset
		assertEquals("hello", trie.getLongestMatchStartingIn("say hello", 4));

		// Test no match when the prefix is not present
		assertEquals("", trie.getLongestMatchStartingIn("hi there", 0));
	}

	/**
	 * Tests that adding a string containing the null character ('\0') throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void testAddThrowsExceptionForStringWithNullChar() {
		PatternTrie trie = new PatternTrie();
		assertThrows(IllegalArgumentException.class, () -> {
			trie.addPattern("he\0llo");
		});
	}

	/**
	 * Tests getLonguestMatchStartingIn() with multiple words added to the trie. In
	 * this test the trie contains "he", "hello", and "helium".
	 */
	@Test
	public void testLongestMatchWithMultipleWords() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("he");
		trie.addPattern("hello");
		trie.addPattern("helium");

		// For input "hello", the longest match should be "hello"
		assertEquals("hello", trie.getLongestMatchStartingIn("hello", 0));

		// For input "helium", the longest match should be "helium"
		assertEquals("helium", trie.getLongestMatchStartingIn("helium", 0));

		assertEquals("he", trie.getLongestMatchStartingIn("helix", 0));
	}

	/**
	 * Tests getLonguestMatchStartingIn() when matching occurs at different offsets
	 * within the input string.
	 */
	@Test
	public void testLongestMatchWithOffset() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("hello");
		trie.addPattern("world");
		String text = "hello world";

		// Starting at index 0, the longest match should be "hello"
		assertEquals("hello", trie.getLongestMatchStartingIn(text, 0));

		// Starting at index 6, the longest match should be "world"
		assertEquals("world", trie.getLongestMatchStartingIn(text, 6));

		// Starting at an index with a space character, no word should match.
		assertEquals("", trie.getLongestMatchStartingIn(text, 5));
	}

	/**
	 * Tests the behavior of getLonguestMatchStartingIn() when the starting position
	 * is at the end of the input.
	 */
	@Test
	public void testLongestMatchAtEnd() {
		PatternTrie trie = new PatternTrie();
		trie.addPattern("end");
		String text = "the end";

		// Starting at index corresponding to "end"
		assertEquals("end", trie.getLongestMatchStartingIn(text, 4));

		// Starting at an index equal to the text length should return an empty string.
		assertEquals("", trie.getLongestMatchStartingIn(text, text.length()));
	}
}
