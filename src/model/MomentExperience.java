/*****************************************************************************
 * MomentExperience.java
 *****************************************************************************
 * Copyright 锟� 2017 uPMT
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import application.Main;
import javafx.scene.paint.Color;
import utils.MainViewTransformations;
import utils.MomentID;

public class MomentExperience implements Serializable, Cloneable {

	private Date mDate;
	private String mDuration;
	private String mName;
	//private String mDescripteme;
	private LinkedList<Descripteme> mDescriptemes;
	private LinkedList<Category> mCategories;
	private LinkedList<Record> mRecords;
	private LinkedList<MomentExperience> mSubMoments;
	private int mParentMomentID=-1;
	private int mParentCol=-1;
	private int mGridCol;
	private String mColor = "#FFFFFF";
	private Property mCurrentProperty =null;
	private int mID;
	private int mRow;
	private transient SimpleDateFormat mFormater = new SimpleDateFormat("HH:mm:ss");
	
	
	public Object clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Object) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public MomentExperience(String mName,int row,int col){
		this.mSubMoments = new LinkedList<MomentExperience>();
		this.mRecords = new LinkedList<Record>();
		this.mCategories = new LinkedList<Category>();
		this.mDescriptemes = new LinkedList<Descripteme>();
		this.mName = mName;
		this.mRow = row;
		this.mGridCol = col;
		this.mDate = new Date();
		try {
			this.mDate = this.mFormater.parse("00:00:00");
		} catch (ParseException e) {
		}
		mID=MomentID.generateID();
	}
	
	
	public MomentExperience() {
		this("New Moment", 0,0);
	}


	public int getID() {
		return mID;
	}
	
	protected void setID(int id) {
		this.mID = id;
	}
	
	public Date getDate() {
		return mDate;
	}
	
	public String getDateString() {
		return mFormater.format(mDate);
	}

	public void setDate(String mDate) throws ParseException {
		try {
			this.mDate = this.mFormater.parse(mDate);
		} catch (ParseException e) {
			throw e;
		}
	}
	
	public String getDuration() {
		return mDuration;
	}

	public void setDuration(String d) {
		mDuration = d;
	}


	public String getName() {
		return mName;
	}

	public void setName(String n) {
		this.mName = n;
	}

	public LinkedList<Descripteme> getDescriptemes() {
		return mDescriptemes;
	}

	public void setDescriptemes(LinkedList<Descripteme> d) {
		this.mDescriptemes = d;
	}
	
	public void addDescripteme(String d) {
		this.mDescriptemes.add(new Descripteme(d));
	}

	public LinkedList<Category> getCategories() {
		return mCategories;
	}

	public void setCategories(LinkedList<Category> caracteristiques) {
		this.mCategories = caracteristiques;
	}

	public LinkedList<Record> getRecords() {
		return mRecords;
	}

	public void setRecords(LinkedList<Record> r) {
		this.mRecords = r;
	}

	public int getGridCol() {
		return mGridCol;
	}

	public void setGridCol(int mGridCol) {
		this.mGridCol = mGridCol;
	}
	

	public LinkedList<MomentExperience> getSubMoments() {
		return mSubMoments;
	}
	
	public void addSousMoment(MomentExperience m) {
		if(!this.mSubMoments.contains(m)) {
			m.setParent(this);
			this.mSubMoments.add(m);
			updateSubMomentPos();
		}
	}
	
	public void addSubMoment(int index, MomentExperience m) {
		if(!this.mSubMoments.contains(m)) {
			m.setParent(this);
			this.mSubMoments.add(index, m);
			updateSubMomentPos();
		}
	}
	
	public MomentExperience removeSubMoment(MomentExperience m) {
		return removeSubMoment(m.getGridCol());
	}
	
	public MomentExperience removeSubMoment(int index) {
		MomentExperience m = this.mSubMoments.remove(index);
		m.setParent(null);
		updateSubMomentPos();
		return m;
	}
	
	public boolean hasParent() {
		return (this.mParentMomentID>=0);
	}
	
	protected void updateSubMomentPos() {
		for(int i=0; i<mSubMoments.size(); i++) {
		//System.out.println("Moment "+mSubMoments.get(i).getNom()+", id:"+mSubMoments.get(i).getID()+" ["+i+";0]");
			mSubMoments.get(i).setParent(this);
			mSubMoments.get(i).setGridCol(i);
			mSubMoments.get(i).setRow(mRow+1);
			mSubMoments.get(i).updateSubMomentPos();
			mSubMoments.get(i).setParentCol(this.mParentCol);
		}
	}
	
	public int getParentCol() {
		if(!this.hasParent()) {
			return this.mGridCol;
		}
		else return this.mParentCol;
	}
	
	public void setParent(MomentExperience m) {
		if(m!=null) {
			this.mParentMomentID = m.getID();
			this.mParentCol = m.getParentCol();
			setRow(m.getRow()+1);
			this.setGridCol(m.getGridCol());
		}
		else{
			this.mParentMomentID=-1;
			this.mParentCol = this.mGridCol;
			setRow(0);
		}
	}
	
	protected void setParentCol(int col) {
		this.mParentCol = col;
	}
	
	public int getRow() {return mRow;}
	public void setRow(int r) {mRow = r;}
	
	public int getParentID() {
		return this.mParentMomentID;
	}
	
	public MomentExperience getParent(Main main) {
		return MainViewTransformations.getMomentByID(this.mParentMomentID, main);
	}
	
	public void setSubMoments(LinkedList<MomentExperience> sm) {
		this.mSubMoments = sm;
	}

	@Override
	public boolean equals(Object arg0) {
		try {
			MomentExperience e = (MomentExperience)arg0;
			return this.getID()==e.getID();
		}catch(Exception e) {
			return false;
		}
	}

	public String getColor() {
		return mColor;
	}

	public void setColor(String mColor) {
		this.mColor = mColor;
	}
	
	public void setCurrentProperty(Property n) {
		mCurrentProperty = n;
	}
	
	public Property getCurrentProperty() {
		return mCurrentProperty;
	}


}
