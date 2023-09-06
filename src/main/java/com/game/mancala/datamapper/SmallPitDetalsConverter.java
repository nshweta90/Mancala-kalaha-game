package com.game.mancala.datamapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.game.mancala.model.SmallPit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SmallPitDetalsConverter implements AttributeConverter<List<SmallPit>, String> {

	private static final String SPLIT_CHAR = ";";
	private static final String VALUE_SEPARATOR = "-";
	
	@Override
	public String convertToDatabaseColumn(List<SmallPit> pitList) {

		String result = null;
		if (pitList != null) {
			result = pitList.stream().map(p -> p.getId() + VALUE_SEPARATOR + p.getStoneCount())
					.collect(Collectors.joining(SPLIT_CHAR));
		}
		return result;
	}

	@Override
	public List<SmallPit> convertToEntityAttribute(String dbData) { 
		
		List<SmallPit> pitList = new ArrayList<>();		
		List<String> strList = (dbData!=null && !dbData.isEmpty())? Arrays.asList(dbData.split(SPLIT_CHAR)):new ArrayList<>();	 
		
		strList.stream().forEach(s->{			
			String[] temp = s.split(VALUE_SEPARATOR);
			pitList.add(new SmallPit(Integer.valueOf(temp[0]), Integer.valueOf(temp[1])));				
		});
		
		return pitList;
	}

}
