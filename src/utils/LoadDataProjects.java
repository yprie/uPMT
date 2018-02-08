/*****************************************************************************
 * LoadDataProjects.java
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

package utils;

import java.util.LinkedList;

import model.Projet;

public class LoadDataProjects {
	private static LoadDataProjects m_instance;
	
	private LinkedList<Projet> projets;
	
	private LoadDataProjects(){
		
	}
	public static LoadDataProjects instance(){
		if(m_instance==null){
			m_instance = new LoadDataProjects();
		}
		return m_instance;
	}
	
	public LinkedList<Projet> getProjets() {
		return projets;
	}
	public void setProjets(LinkedList<Projet> projets) {
		this.projets = projets;
	}
	
	
	
}
