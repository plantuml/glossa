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
package com.plantuml.glossa.markdown;

import java.util.ArrayList;
import java.util.List;

import com.plantuml.glossa.GlossaTag;
import com.plantuml.glossa.Parser;
import com.plantuml.glossa.Peeker;
import com.plantuml.glossa.StringPeeker;
import com.plantuml.glossa.ptrie.PatternTrie;

public class ParserMarkdown implements Parser {

	// https://www.markdownguide.org/basic-syntax/
	
	private final PatternTrie style = new PatternTrie();

	public ParserMarkdown() {
		style.addPattern("***");
		style.addPattern("**");
		style.addPattern("*");
		style.addPattern("`");
	}

	@Override
	public List<GlossaTag> parse(Peeker<String> wiki) {

		final List<GlossaTag> result = new ArrayList<>();

		for (; wiki.peek(0) != null; wiki.jump()) {
			final StringPeeker line = new StringPeeker(wiki.peek(0));
			final StringBuilder pending = new StringBuilder();

			while (line.peek(0).length() > 0) {
				final String styleStart = style.getLongestMatchStartingIn(line.peek(0), 0);

				if (styleStart.equals("***")) {
					final int styleEnd = line.search("***", 4);
					if (styleEnd != -1) {
						addPending(result, pending);
						result.add(new GlossaTag("text").addMetadata("bold").addMetadata("italic")
								.addMetadata("content", line.peek(0).substring(3, styleEnd)));
						line.jump(styleEnd + 3);
						continue;

					}
				} else if (styleStart.equals("**")) {
					final int styleEnd = line.search("**", 3);
					if (styleEnd != -1) {
						addPending(result, pending);
						result.add(new GlossaTag("text").addMetadata("bold").addMetadata("content",
								line.peek(0).substring(2, styleEnd)));
						line.jump(styleEnd + 2);
						continue;

					}
				} else if (styleStart.equals("*")) {
					final int styleEnd = line.search("*", 2);
					if (styleEnd != -1) {
						addPending(result, pending);
						result.add(new GlossaTag("text").addMetadata("italic").addMetadata("content",
								line.peek(0).substring(1, styleEnd)));
						line.jump(styleEnd + 1);
						continue;

					}
				} else if (styleStart.equals("`")) {
					final int styleEnd = line.search("`", 2);
					if (styleEnd != -1) {
						addPending(result, pending);
						result.add(new GlossaTag("text").addMetadata("code").addMetadata("content",
								line.peek(0).substring(1, styleEnd)));
						line.jump(styleEnd + 1);
						continue;

					}
				}

				pending.append(line.charAt(0));
				line.jump(1);

			}
			addPending(result, pending);

			if (wiki.peek(1) != null)
				result.add(new GlossaTag("br"));

		}
		return result;
	}

	private static void addPending(final List<GlossaTag> result, final StringBuilder pending) {
		if (pending.length() > 0) {
			result.add(new GlossaTag("text").addMetadata("content", pending.toString()));
			pending.setLength(0);
		}
	}

}
