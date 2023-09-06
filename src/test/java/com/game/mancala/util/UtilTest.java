package com.game.mancala.util;

import java.util.List;

import com.game.mancala.datamapper.SmallPitDetalsConverter;
import com.game.mancala.model.SmallPit;

public class UtilTest {
	
	public static List<SmallPit> createPitList(String string) {		 
		SmallPitDetalsConverter converter = new SmallPitDetalsConverter(); 
		return converter.convertToEntityAttribute(string);
	}

	public static String createPitListString(List<SmallPit> pitList) {
		SmallPitDetalsConverter converter = new SmallPitDetalsConverter(); 
		return converter.convertToDatabaseColumn(pitList);
	}

}
