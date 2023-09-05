package com.game.mancala.datamapper;

import com.game.mancala.model.Pit;
import com.game.mancala.util.Constants;

import jakarta.persistence.AttributeConverter;

public class BigPitDetailsConverter implements AttributeConverter<Pit, Integer>{

	@Override
	public Integer convertToDatabaseColumn(Pit pit) { 		 
		
		return pit!=null ?  pit.getStoneCount():Constants.ZERO;
	}

	@Override
	public Pit convertToEntityAttribute(Integer dbData) {
		 return dbData!=null? new Pit(Constants.BIG_PIT_IDX, dbData):new Pit(Constants.BIG_PIT_IDX, Constants.ZERO); 
		 
	}

}
