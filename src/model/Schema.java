/*****************************************************************************
 * Schema.java
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

import java.io.Serializable;
import java.util.LinkedList;

public class Schema extends Type implements Serializable, Cloneable{
	
	public Schema(String nom){
		super(nom);
	}

	/*public Schema clone() {
		Schema ret = new Schema(nom);
		ret.setDescription(description);
		ret.setCouleur(couleur);
		LinkedList<Type> retTypes = new LinkedList<Type>();
		for(int i=0;i<types.size();i++) {
			ret.addType(types.get(i).clone());
		}
		//ret.setDescriptemeTxt(new String(extract.getTexte()));
		return ret;
	}*/

}
