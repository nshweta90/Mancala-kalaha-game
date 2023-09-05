package com.game.mancala.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.game.mancala.datamapper.SmallPitDetalsConverter;
import com.game.mancala.model.SmallPit;

class SmallPitDetalsConverterTests {

	SmallPitDetalsConverter converter = new SmallPitDetalsConverter();

	@Test
	void test_whenPitListProvided_stringvalueGenerated() {
		List<SmallPit> pitList = List.of(new SmallPit(0, 6), new SmallPit(0, 6), new SmallPit(0, 6));
		String expected = "0-6;0-6;0-6";
		assertEquals(expected, converter.convertToDatabaseColumn(pitList));

	}

	@Test
	void test_whenEmptyPitListProvided_stringvalueGenerated() {
		assertNull(converter.convertToDatabaseColumn(null));
	}

	@Test
	void test_whenstringvalueProvided_PitListGenerated() {
		List<SmallPit> expected = List.of(new SmallPit(0, 6), new SmallPit(0, 6), new SmallPit(0, 6));
		String pitListString = "0-6;0-6;0-6";
		
		List<SmallPit> listConversion = converter.convertToEntityAttribute(pitListString);
		IntStream.range(0,expected.size()).forEach(p -> {
			Assertions.assertEquals(expected.get(p).getId(), listConversion.get(p).getId()); 
			Assertions.assertEquals(expected.get(p).getStoneCount(), listConversion.get(p).getStoneCount()); 
		});
		
		
	}

	@Test
	void test_whenNullStringvalueProvided_PitListGenerated() {
		String pitListString = "";
		assertEquals(0, converter.convertToEntityAttribute(pitListString).size());
		assertEquals(0, converter.convertToEntityAttribute(null).size());

	}
}
