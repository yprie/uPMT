/*****************************************************************************
 * Descripteme.java
 *****************************************************************************
 * Copyright © 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package model;

import NewModel.IDescriptemeAdapter;

import java.io.Serializable;

public class Descripteme implements Serializable, Cloneable, IDescriptemeAdapter  {
	
	private String mText;
	
	public Object clone() {
		Descripteme ret = null;
	    try {
			ret = (Descripteme) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ret;
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			Descripteme d = (Descripteme) o;
			return d.mText.equals(mText);
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public Descripteme(String text){
		this.mText = text;
	}
	
	public void setTexte(String t){
		this.mText = t;
	}
	
	public String getTexte(){
		return this.mText;
	}

	public String toString(){
		return this.mText;
	}
}
