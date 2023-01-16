/*
 * The Nheengatu Project : a free Java library for HTML  abstraction.
 *
 * Project Info:  http://www.aryjr.com/nheengatu/
 * Project Lead:  Ary Rodrigues Ferreira Junior
 *
 * (C) Copyright 2005, 2006 by Ary Junior
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.aryjr.nheengatu.util;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.aryjr.nheengatu.css2.Style;
import com.aryjr.nheengatu.css2.StyleSheet;
import com.aryjr.nheengatu.html.Tag;
import com.lowagie.text.Font;

/**
 * 
 * Receive all tags and execute the doXXX methods for each one.
 * 
 * @version $Id: TagsManager.java,v 1.2 2009/05/25 21:07:16 eeh Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TagsManager {
	private static ThreadLocal<TagsManager> instance = new ThreadLocal<TagsManager>();

	public ArrayList<GraphicsState> states = new ArrayList<GraphicsState>();

	public TagsManager() {
		states.add(new GraphicsState());
	}

	public static TagsManager getInstance() {
		if (instance.get() == null) {
			instance.set(new TagsManager());
		}
		return instance.get();
	}
	
	public GraphicsState getLastState() {
		if (states.size() == 0)
			return null;
		return states.get(states.size() - 1);
	}

	public void checkTag(final Tag tag) {
		// TODO <center> is deprecated in HTML 4!!!
		final GraphicsState state = new GraphicsState(getLastState());
		states.add(state);
		if (tag.getPropertyValue("bgcolor") != null) {
			state.setBgcolor(tag.getPropertyValue("bgcolor"));
		}
		if (tag.getPropertyValue("align") != null) {
			state.setAlign(tag.getPropertyValue("align"));
		}
		if (tag.getPropertyValue("valign") != null) {
			state.setValign(tag.getPropertyValue("valign"));
		}
		// TODO do not forget the text position!!!
		if (tag.getPropertyValue("style") != null) {
			state.setStyle(new Style(tag.getPropertyValue("style")));
		}
		// TODO <font> and <basefont> are deprecated in HTML 4!!!
		if (tag.getName().equalsIgnoreCase("font")) {
			state.setFont(tag);
		}
		if (tag.getName().equalsIgnoreCase("link")) {
			final StyleSheet ss = StyleSheet.getInstance();
			ss.processCSSFile(tag.getPropertyValue("href"));
		}
		if (checkTag(tag.getName())) {
			doTag(tag);
		}
	}

	public Font getFont() {
		return getLastState().getFont();
	}

	public Color getBgcolor() {
		return getLastState().getBgcolor();
	}

	public int getAlign() {
		return getLastState().getAlign();
	}

	public int getValign() {
		return getLastState().getValign();
	}

	public float getSpacingAfter() {
		return getLastState().getSpacingAfter();
	}

	public float getSpacingBefore() {
		return getLastState().getSpacingBefore();
	}

	public float getTextIndent() {
		return getLastState().getTextIndent();
	}

	public float getMarginLeft() {
		return getLastState().getMarginLeft();
	}

	public String getListStyleType() {
		return getLastState().getListStyleType();
	}

	public void back() {
		states.remove(states.size() - 1);
	}

	public GraphicsState getPreviousState(final int ind) {
		return (GraphicsState) states.get(states.size() - (ind + 1));
	}

	private boolean checkTag(final String tagName) {
		try {
			final Method[] methods = TagsManager.class.getMethods();
			for (Method element : methods) {
				if (element.getName().equalsIgnoreCase("do" + tagName.toUpperCase()))
					return true;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void doTag(final Tag tag) {
		try {
			final Method method = TagsManager.class.getMethod("do" + tag.getName().toUpperCase(), new Class[] {});
			method.invoke(this);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void doB() {
		GraphicsState previousState = getPreviousState(1);
		if (previousState != null) {
			combineHTMLTags(previousState, Font.BOLD);
		} else {
			getLastState().getFont().setStyle(Font.BOLD);	
		}
	}

	public void doSTRONG() {
		doB();
	}

	public void doBIG() {
		getLastState().getFont().setSize(20);
	}

	public void doCENTER() {
		getLastState().setAlign("center");
	}

	public void doH1() {
		getLastState().getFont().setSize(16);
		getLastState().getFont().setStyle(Font.BOLD);
		getLastState().setSpacingBefore(8f);
		getLastState().setSpacingAfter(8f);
	}

	public void doH2() {
		getLastState().getFont().setSize(14);
		getLastState().getFont().setStyle(Font.BOLD);
		getLastState().setSpacingBefore(7f);
		getLastState().setSpacingAfter(7f);
	}

	public void doH3() {
		getLastState().getFont().setSize(12);
		getLastState().getFont().setStyle(Font.BOLD);
		getLastState().setSpacingBefore(6f);
		getLastState().setSpacingAfter(6f);
	}

	public void doH4() {
		getLastState().getFont().setSize(12);
		getLastState().getFont().setStyle(Font.UNDERLINE);
		getLastState().setSpacingBefore(6f);
		getLastState().setSpacingAfter(6f);
	}

	public void doH5() {
		getLastState().getFont().setSize(11);
		getLastState().getFont().setStyle(Font.BOLD);
	}

	public void doH6() {
		getLastState().getFont().setSize(11);
		getLastState().getFont().setStyle(Font.BOLD);
	}

	public void doI() {
		GraphicsState previousState = getPreviousState(1);
		if (previousState != null) {
			combineHTMLTags(previousState, Font.ITALIC);
		} else {
			getLastState().getFont().setStyle(Font.ITALIC);
		}
	}

	public void doEM() {
		doI();
	}

	public void doU() {
		GraphicsState previousState = getPreviousState(1);
		if (previousState != null) {
			combineHTMLTags(previousState, Font.UNDERLINE);
		} else {
			getLastState().getFont().setStyle(Font.UNDERLINE);
		}
	}
	
	private void combineHTMLTags(GraphicsState previousState, int currentStyle) {
		Font previousFont = previousState.getFont();
		Font currentFont = getLastState().getFont();
		currentFont.setStyle(currentStyle);
		Font difference = currentFont.difference(previousFont);
		
		getLastState().getFont().setStyle(difference.getStyle());
	}
}

