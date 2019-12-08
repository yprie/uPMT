/*****************************************************************************
 * SchemaTransformations.java
 *****************************************************************************
 * Copyright � 2017 uPMT
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

import SchemaTree.Cell.Models.ICategoryAdapter;
import SchemaTree.Cell.Models.IPropertyAdapter;
import SchemaTree.Cell.Models.ITypeAdapter;

import java.util.LinkedList;
import controller.controller.TypeController;
import javafx.scene.control.TreeItem;
import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.Schema;

public abstract class SchemaTransformations {

	public static TreeItem<TypeController> SchemaToTreeView(Schema s){
		TreeItem<TypeController> schema;
		TypeController ss = new TypeController(s, null);
		schema = new TreeItem<TypeController>(ss);
		schema.setExpanded(true);
		
		for (Folder f : s.getFolders()) {
			addTypeTreeView(schema, f);			
		}
		return schema;
	}
	
	public static TreeItem<DescriptionInterview> EntretienToTreeView(LinkedList<DescriptionInterview> entretiens){
		TreeItem<DescriptionInterview> entres;
		entres = new TreeItem<DescriptionInterview>(new DescriptionInterview(null,"Interviews"));
		//entres = new TreeItem<DescriptionEntretien>(new DescriptionEntretien(null,main._langBundle.getString("interview")));
		entres.setExpanded(true);
		for (DescriptionInterview d : entretiens) {
			entres.getChildren().add(new TreeItem<DescriptionInterview>(d));
		}
		return entres;
	}

	private static void addTypeTreeView(TreeItem<TypeController> tree, ITypeAdapter t){
		
		if(t.isFolder()) {
			if(!((Folder)t).getCategories().isEmpty() || !((Folder)t).getFolders().isEmpty()){
				TypeController tc = new TypeController(t, tree.getValue().getType());
				TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
				node.setExpanded(true);
				tree.getChildren().add(node);
				
				for (ICategoryAdapter c : ((Folder)t).getCategories()) {
					addTypeTreeView(node, c);
				}
				for (Folder f : ((Folder)t).getFolders()) {
					addTypeTreeView(node, f);
				}
			}
			else{
				TypeController tc = new TypeController(t, tree.getValue().getType());
				TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
				tree.getChildren().add(node);
			}
		}
		else if(t.isCategory()) {
			if(!((Category)t).getProperties().isEmpty()) {
				TypeController tc = new TypeController(t, tree.getValue().getType());
				TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
				node.setExpanded(true);
				tree.getChildren().add(node);
				
				for (IPropertyAdapter p : ((Category)t).getProperties()) {
					addTypeTreeView(node, p);
				}
			}
			else{
				TypeController tc = new TypeController(t, tree.getValue().getType());
				TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
				tree.getChildren().add(node);
			}
		}
		else {
			TypeController tc = new TypeController(t, tree.getValue().getType());
			TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
			tree.getChildren().add(node);
		}
	}	
}
