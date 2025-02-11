/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 *
 */
package com.plantuml.glossa;

public class StringPeeker {

	private final String content;

	private int position;

	public StringPeeker(String content) {
		this.content = content;
	}

	public int indexOf(char ch) {
		return content.indexOf(ch, position) - position;
	}

	public String substring(int beginIndex, int endIndex) {
		return content.substring(position + beginIndex, position + endIndex);
	}

	public int length() {
		return content.length() - position;
	}

	public boolean endsWith(String end) {
		return content.endsWith(end);
	}

	public char charAt(int pos) {
		return this.content.charAt(position + pos);
	}

	public String peek(int ahead) {
		if (position + ahead > content.length())
			return "";
		return content.substring(position + ahead);
	}

	public void jump(int step) {
		position += step;
	}

	public int search(String searched, int ahead) {
		for (int i = position + ahead; i < content.length() - searched.length() + 1; i++)
			if (content.substring(i).startsWith(searched))
				return i - position;

		return -1;
	}

}
