/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package com.plantuml.glossa.ptrie;

import java.util.HashMap;
import java.util.Map;

import com.plantuml.glossa.StringPeeker;

// https://en.wikipedia.org/wiki/CJK_Symbols_and_Punctuation
// Set:「abc」
// Range:「a〜c」
//One or more: 〸a
//Named group: a〔group1〡〸b〕c

// 〇	〈	〉	《	》	「	」	『	』
// 【	】	〒	〓	〔	〕	〖	〗	〘	〙	〚	〛 
// 〸	〹	〺
public class PatternTrie {

	private final Map<Character, PatternTrie> children = new HashMap<>();
	private final Map<SimplePattern, PatternTrie> simplePatterns = new HashMap<>();

	static class Record {
		final int length;
		final PatternTrie trie;

		public Record(int length, PatternTrie trie) {
			this.length = length;
			this.trie = trie;
		}
	}

//	@Override
//	public String toString() {
//		final StringBuilder sb = new StringBuilder("children=");
//		for (char ch : children.keySet())
//			if (ch == '\0')
//				sb.append("<ZERO>");
//			else
//				sb.append(ch);
//		sb.append(" patterns=");
//		sb.append(simplePatterns.toString());
//		return sb.toString();
//	}

	public void addPattern(String s) {
		if (s.indexOf('\0') != -1)
			throw new IllegalArgumentException();

		addInternal(this, new StringPeeker(s + "\0"));
	}

	private static void addInternal(PatternTrie current, StringPeeker input) {
		if (input.length() == 0)
			throw new UnsupportedOperationException();
		if (input.endsWith("\0") == false)
			throw new IllegalArgumentException();

		while (input.length() > 0) {
			final char added = input.charAt(0);

			if (added == '〸') {
				final char ch = input.charAt(1);
				final SimplePattern pattern = new RepetitionPattern(new SingleCharPattern(ch));
				final PatternTrie child = new PatternTrie();
				current.simplePatterns.put(pattern, child);
				input.jump(2);
				current = child;
			} else if (added == '〔') {
				final int end = input.indexOf('〕');
				if (end == -1)
					throw new UnsupportedOperationException("wip99");
				final String part = input.substring(0, end + 1);
				final int idx = part.indexOf('〡');
				if (idx == -1)
					throw new UnsupportedOperationException("wip98");
				final String part2 = part.substring(idx + 1, end);

				final PatternTrie externalGroup = new PatternTrie();
				externalGroup.addPattern(part2);

				final PatternTrie child = new PatternTrie();
				current.simplePatterns.put(new GroupPattern(externalGroup), child);
				input.jump(end + 1);
				current = child;

			} else if (added == '「') {
				final int end = input.indexOf('」');
				if (end == -1)
					throw new UnsupportedOperationException("wip80");
				final PatternTrie child = new PatternTrie();
				current.simplePatterns.put(CharSetPattern.build(input.substring(0, end + 1)), child);
				input.jump(end + 1);
				current = child;
			} else {
				final PatternTrie child = current.getOrCreate(added);
				input.jump(1);

				current = child;
			}
		}
	}

	private PatternTrie getOrCreate(Character added) {
		PatternTrie result = children.get(added);
		if (result == null) {
			result = new PatternTrie();
			children.put(added, result);
		}
		return result;
	}

	public String getLongestMatchStartingIn(String s, int pos) {
		return getLongestMatchStartingIn(this, s, pos);
	}

	private Record getCandidate(int pos, String s) {
		final PatternTrie child = children.get(s.charAt(pos));
		if (child != null)
			return new Record(1, child);

		for (Map.Entry<SimplePattern, PatternTrie> ent : simplePatterns.entrySet()) {
			final int matches = ent.getKey().matches(s, pos);
			if (matches > 0)
				return new Record(matches, ent.getValue());

		}
		return null;
	}

	private static String getLongestMatchStartingIn(PatternTrie current, String s, int pos) {
		String longestMatch = "";
		final StringBuilder result = new StringBuilder();
		while (current != null) {
			if (s.length() == pos)
				if (current.children.containsKey('\0'))
					return result.toString();
				else
					return longestMatch;

			final Record candidate = current.getCandidate(pos, s);

			if (candidate == null/* || child.children.size() == 0 */)
				if (current.children.containsKey('\0'))
					return result.toString();
				else
					return longestMatch;

			for (int i = 0; i < candidate.length; i++)
				result.append(s.charAt(pos++));

			if (candidate.trie.children.containsKey('\0'))
				longestMatch = result.toString();
			current = candidate.trie;
		}
		return longestMatch;

	}

}
