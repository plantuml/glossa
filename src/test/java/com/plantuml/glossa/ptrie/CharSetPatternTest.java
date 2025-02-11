package com.plantuml.glossa.ptrie;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CharSetPatternTest {

	/**
	 * Tests that adding valid characters works and that the {@code contains} method
	 * returns the correct result.
	 */
	@Test
	public void testAddAndContains() {
		CharSetPattern pattern = new CharSetPattern();

		// Add some valid characters
		pattern.addChar('A');
		pattern.addChar('z');
		pattern.addChar(' '); // lower bound (code 32)
		pattern.addChar((char) 128); // upper bound

		// Verify that the added characters are present
		assertTrue(pattern.contains('A'));
		assertTrue(pattern.contains('z'));
		assertTrue(pattern.contains(' '));
		assertTrue(pattern.contains((char) 128));

		// Verify that other characters are not present
		assertFalse(pattern.contains('B'));
		assertFalse(pattern.contains('b'));
		assertFalse(pattern.contains('0'));
	}

	/**
	 * Tests that adding a character outside the valid range throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void testInvalidAddChar() {
		CharSetPattern pattern = new CharSetPattern();

		// Character below 32
		assertThrows(IllegalArgumentException.class, () -> {
			pattern.addChar((char) 31);
		});

		// Character above 128
		assertThrows(IllegalArgumentException.class, () -> {
			pattern.addChar((char) 129);
		});
	}

	/**
	 * Tests that calling {@code contains} with a character outside the valid range
	 * throws an IllegalArgumentException.
	 */
	@Test
	public void testInvalidContains() {
		CharSetPattern pattern = new CharSetPattern();

		// Check with a character below 32
		assertFalse(pattern.contains((char) 31));

		// Check with a character above 128
		assertFalse(pattern.contains((char) 129));
	}

	/**
	 * Tests adding multiple characters (possibly duplicates) and verifies that each
	 * is present.
	 */
	@Test
	public void testMultipleAdditions() {
		CharSetPattern pattern = new CharSetPattern();

		List<Character> characters = Arrays.asList('A', 'B', 'C', '1', '2', '3', '#', '$');

		// Add each character (even if added multiple times)
		for (char ch : characters) {
			pattern.addChar(ch);
			pattern.addChar(ch); // Verify that duplicate additions do not cause issues
		}

		// Verify that all added characters are present
		for (char ch : characters) {
			assertTrue(pattern.contains(ch));
		}
	}

	/**
	 * Tests building a pattern from a string with individual characters. Pattern:
	 * 「abc」 should contain 'a', 'b', and 'c'.
	 */
	@Test
	public void testBuildWithSingleCharacters() {
		CharSetPattern pattern = CharSetPattern.build("「abc」");
		assertTrue(pattern.contains('a'));
		assertTrue(pattern.contains('b'));
		assertTrue(pattern.contains('c'));
		assertFalse(pattern.contains('d'));
	}

	/**
	 * Tests building a pattern from a string specifying a range. Pattern: 「a〜c」
	 * should contain 'a', 'b', and 'c'.
	 */
	@Test
	public void testBuildWithRange() {
		CharSetPattern pattern = CharSetPattern.build("「a〜c」");
		assertTrue(pattern.contains('a'));
		assertTrue(pattern.contains('b'));
		assertTrue(pattern.contains('c'));
		assertFalse(pattern.contains('d'));
	}

	/**
	 * Tests building a pattern that mixes individual characters and ranges.
	 * Pattern: 「-a〜z0〜9」 should contain '-' plus all lowercase letters from 'a' to
	 * 'z' and all digits from '0' to '9'.
	 */
	@Test
	public void testBuildWithMultipleRangesAndCharacters() {
		CharSetPattern pattern = CharSetPattern.build("「-a〜z0〜9」");

		// Check for the individual character.
		assertTrue(pattern.contains('-'));

		// Check for the range of lowercase letters.
		for (char c = 'a'; c <= 'z'; c++) {
			assertTrue(pattern.contains(c));
		}

		// Check for the range of digits.
		for (char c = '0'; c <= '9'; c++) {
			assertTrue(pattern.contains(c));
		}

		// Verify a character that is not included.
		assertFalse(pattern.contains('A'));
	}

	/**
	 * Tests that a pattern with missing delimiters throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void testBuildWithInvalidDelimiters() {
		// Pattern without the starting delimiter.
		assertThrows(IllegalArgumentException.class, () -> {
			CharSetPattern.build("abc」");
		});

		// Pattern without the closing delimiter.
		assertThrows(IllegalArgumentException.class, () -> {
			CharSetPattern.build("「abc");
		});
	}

	/**
	 * Tests that a pattern with an incomplete range (missing the end character)
	 * throws an IllegalArgumentException.
	 */
	@Test
	public void testBuildWithIncompleteRange() {
		// Pattern: 「a〜」 is invalid because the range operator is not followed by a
		// character.
		assertThrows(IllegalArgumentException.class, () -> {
			CharSetPattern.build("「a〜」");
		});
	}

	/**
	 * Tests that a pattern with an invalid range order throws an
	 * IllegalArgumentException. For example, a range where the start character is
	 * greater than the end character.
	 */
	@Test
	public void testBuildWithInvalidRangeOrder() {
		// Pattern: 「z〜a」 is invalid because 'z' is greater than 'a'.
		assertThrows(IllegalArgumentException.class, () -> {
			CharSetPattern.build("「z〜a」");
		});
	}

	/**
	 * Tests building a pattern with empty inner content. Pattern: 「」 should result
	 * in an empty character set.
	 */
	@Test
	public void testBuildWithEmptyInnerContent() {
		CharSetPattern pattern = CharSetPattern.build("「」");
		for (char ch = 32; ch <= 128; ch++) {
			assertFalse(pattern.contains(ch));
		}
	}

	/**
	 * Case 1: Test addRange when the entire range is in mask1. For example, the
	 * range from 'A' (65) to 'Z' (90) lies in mask1.
	 */
	@Test
	public void testAddRangeEntirelyInMask1() {
		CharSetPattern pattern = new CharSetPattern();
		pattern.addRange('A', 'Z'); // 'A' = 65, 'Z' = 90, all in mask1

		for (char c = 'A'; c <= 'Z'; c++) {
			assertTrue(pattern.contains(c));
		}
		// Verify that a character outside this range is not included.
		assertFalse(pattern.contains('a'));
	}

	/**
	 * Case 2: Test addRange when the entire range is in mask2. For example, the
	 * range from 'a' (97) to 'z' (122) lies in mask2.
	 */
	@Test
	public void testAddRangeEntirelyInMask2() {
		CharSetPattern pattern = new CharSetPattern();
		pattern.addRange('a', 'z'); // 'a' = 97, 'z' = 122, all in mask2

		for (char c = 'a'; c <= 'z'; c++) {
			assertTrue(pattern.contains(c));
		}
		// Verify that a character outside this range is not included.
		assertFalse(pattern.contains('A'));
	}

	/**
	 * Case 3: Test addRange when the range spans both mask1 and mask2. For example,
	 * the range from 'A' (65) to 'z' (122) spans both masks.
	 */
	@Test
	public void testAddRangeSpanningBothMasks() {
		CharSetPattern pattern = new CharSetPattern();
		pattern.addRange('A', 'z'); // 'A' = 65, 'z' = 122, spans both mask1 and mask2

		for (char c = 'A'; c <= 'z'; c++) {
			assertTrue(pattern.contains(c));
		}
		// Verify characters immediately outside the range are not included.
		// '@' (64) comes just before 'A', and '{' (123) comes just after 'z'.
		assertFalse(pattern.contains('@'));
		assertFalse(pattern.contains('{'));
	}

}
