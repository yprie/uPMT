/*****************************************************************************
 * DescriptionEntretien.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

public class DescriptionInterview implements Serializable, Cloneable{
	
	private Descripteme mDescripteme;
	private LinkedList<MomentExperience> mMoments;
	private LocalDate mDateInterview;
	private String mParticipant;
	private String mComment;
	private String mName;
	
	@Override
	public boolean equals(Object o) {
		boolean ret=false;
		try {
			DescriptionInterview d = (DescriptionInterview)o;

			
			if(mDescripteme==null && d.mDescripteme==null) ret=true;
			else if(d.mDescripteme.equals(mDescripteme)) ret=true;
			//System.out.println("a "+ret);
			
				
			if(mMoments==null && d.mMoments==null) ret=true;
			else if(!(ret && d.mMoments.equals(mMoments))) ret=false;
			//System.out.println("b "+ret);

			
			if(mDateInterview==null && d.mDateInterview==null) ret=true;
			else if(!(ret && d.mDateInterview.equals(mDateInterview))) ret=false;
			//System.out.println("c "+ret);
			

			
			if(mParticipant==null && d.mParticipant==null) ret=true;
			else if(!(ret && d.mParticipant.equals(mParticipant))) ret=false;
			//System.out.println("d "+ret);

			
			if(mComment==null && d.mComment==null) ret=true;
			else if(!(ret && d.mComment.equals(mComment))) ret=false;
			//System.out.println("e "+ret);

			
			if(mName==null && d.mName==null) ret=true;
			else if(!(ret && d.mName.equals(mName))) ret=false;
			//System.out.println("f "+ret);
			
		}catch (Exception e) {ret = false;
		//System.out.println("erreur "+ret);
		}
		return ret;
	}
	

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
	
	public DescriptionInterview(Descripteme d,String mNameEntretiens){
		this.mMoments = new LinkedList<MomentExperience>();
		this.mName = mNameEntretiens;
		this.mDescripteme = d;
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public Descripteme getDescripteme() {
		return mDescripteme;
	}

	public void setDescripteme(Descripteme mDescripteme) {
		this.mDescripteme = mDescripteme;
	}

	public void setMoments(LinkedList<MomentExperience> mMoments) {
		this.mMoments = mMoments;
	}

	public void addMoment(MomentExperience m){
		this.mMoments.add(m);
		m.setRow(0);
		updateMomentsPos();
	}
	
	public void addMoment(int index, MomentExperience m){
		this.mMoments.add(index, m);
		updateMomentsPos();
	}
	
	public void removeMomentExp(MomentExperience m) {
		this.mMoments.remove(m);
		updateMomentsPos();
	}
	
	
	private void updateMomentsPos() {
		for(int i=0; i<mMoments.size(); i++) {
		//System.out.println("Moment "+mMoments.get(i).getNom()+", id:"+mMoments.get(i).getID()+":["+i+";0]");
			mMoments.get(i).setGridCol(i);
			mMoments.get(i).setRow(0);
			mMoments.get(i).setParent(null);
			mMoments.get(i).updateSubMomentPos();
		}
	}
	
	public LinkedList<MomentExperience> getMoments(){
		return this.mMoments;
	}
	
	public void setDateInterview(LocalDate ld){
		mDateInterview = ld;
	}
	public LocalDate getDateInterview(){
		return mDateInterview;
	}
	public void setParticipant(String s){
		mParticipant = s;
	}
	public String getParticipant(){
		return mParticipant;
	}
	public void setComment(String s){
		mComment = s;
	}

	public String getComment(){
		return mComment;
	}
	public String toString(){
		return this.mName;
	}
	
	public int getNumberOfMoments() {
		return mMoments.size();
	}

	
}
