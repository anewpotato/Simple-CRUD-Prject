package com.react.dao;

import java.util.ArrayList;

import com.spring.react.dto.ContentDto;

public interface IDao {
	
	public ArrayList<ContentDto> listDao();
	public void writeDao(String mWriter,String mTitle, String mContent);
	public void upHit(String bId);
	public ContentDto viewDao(String strID);
	public void deleteDao(String bId);
	
}
