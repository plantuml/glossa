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

public class CharSetPattern implements SimplePattern {

	private long mask1 = 0L;
	private long mask2 = 0L;
	private final String display;

	public CharSetPattern(String display) {
		this.display = display;
	}

	public CharSetPattern() {
		this(null);
	}

	@Override
	public String toString() {
		if (display == null)
			return super.toString();
		return display;
	}

	public static CharSetPattern build(String pattern) {
		if (pattern.length() < 2 || pattern.charAt(0) != '「' || pattern.charAt(pattern.length() - 1) != '」')
			throw new IllegalArgumentException("Pattern must start with 「 and end with 」.");

		final String inner = pattern.substring(1, pattern.length() - 1);

		if (inner.endsWith("〜"))
			throw new IllegalArgumentException("Range operator '〜' must be followed by a character.");

		final CharSetPattern result = new CharSetPattern(pattern);

		for (int i = 0; i < inner.length(); i++) {
			final char start = inner.charAt(i);

			if (i + 1 < inner.length() && inner.charAt(i + 1) == '〜') {
				i += 2;
				final char end = inner.charAt(i);
				result.addRange(start, end);
			} else {
				result.addChar(start);
			}
		}

		return result;
	}

	public void addChar(char ch) {
		final int offset = getOffset(ch);
		if (ch < 32 || ch > 128)
			throw new IllegalArgumentException("Bad char : " + ch);

		if (offset < 64)
			mask1 |= (1L << offset);
		else
			mask2 |= (1L << (offset - 64));

	}

	public void addRange(char start, char end) {
		if (start > end)
			throw new IllegalArgumentException("Invalid range: '" + start + "' is greater than '" + end + "'.");

		if (start < 32 || end > 128)
			throw new IllegalArgumentException("Characters must be in the range 32 to 128.");

		final int startOffset = start - 32;
		final int endOffset = end - 32;

		// Case 1: Entire range is in mask1 (offsets 0 to 63)
		if (endOffset < 64) {
			int length = endOffset - startOffset + 1;
			long rangeMask = (length == 64 ? -1L : ((1L << length) - 1)) << startOffset;
			mask1 |= rangeMask;
		}
		// Case 2: Entire range is in mask2 (offsets 64 and above)
		else if (startOffset >= 64) {
			int adjustedStart = startOffset - 64;
			int adjustedEnd = endOffset - 64;
			int length = adjustedEnd - adjustedStart + 1;
			long rangeMask = ((1L << length) - 1) << adjustedStart;
			mask2 |= rangeMask;
		}
		// Case 3: Range spans both mask1 and mask2
		else {
			// For mask1: add bits from startOffset up to bit 63
			int length1 = 64 - startOffset;
			long maskPart1 = (length1 == 64 ? -1L : ((1L << length1) - 1)) << startOffset;
			mask1 |= maskPart1;

			// For mask2: add bits from 0 up to (endOffset - 64)
			int adjustedEnd = endOffset - 64;
			int length2 = adjustedEnd + 1;
			long maskPart2 = ((1L << length2) - 1);
			mask2 |= maskPart2;
		}
	}

	public boolean contains(char ch) {
		final int offset = getOffset(ch);
		if (offset > 128)
			return false;
		if (offset < 64)
			return (mask1 & (1L << offset)) != 0;
		else
			return (mask2 & (1L << (offset - 64))) != 0;
	}

	private int getOffset(char ch) {
		return ch - 32;
	}

	@Override
	public int matches(String s, int pos) {
		if (s.length() == pos)
			return 0;
		if (this.contains(s.charAt(pos)))
			return 1;
		return 0;

	}

}
