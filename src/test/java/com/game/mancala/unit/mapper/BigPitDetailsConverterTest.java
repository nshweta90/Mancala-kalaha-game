package com.game.mancala.unit.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.game.mancala.datamapper.BigPitDetailsConverter;
import com.game.mancala.model.Pit;
import com.game.mancala.util.Constants;

class BigPitDetailsConverterTest {

	BigPitDetailsConverter converter = new BigPitDetailsConverter();

	@Test
	void test_WhenPitisProvided_StoneCountIsSetasInt() {
		Pit bigpit = new Pit(Constants.BIG_PIT_IDX, Constants.ZERO);
		Assertions.assertEquals(Constants.ZERO, converter.convertToDatabaseColumn(bigpit));
	}

	@Test
	void test_WhenNullPitisProvided_StoneCountIsInitializedToZero_ForPlayer() {
		Assertions.assertEquals(Constants.ZERO, converter.convertToDatabaseColumn(null));
	}

	@Test
	void test_WhenNullisProvided_pitWithZeroStonecountIsReturned() {

		Assertions.assertEquals(Constants.ZERO, converter.convertToEntityAttribute(null).getStoneCount());
	}
	
	@Test
	void test_WhenCountisProvided_pitWithCorrectIDisCreated() {
		
		Pit pit = converter.convertToEntityAttribute(Constants.ZERO) ;
		
		Assertions.assertEquals(Constants.BIG_PIT_IDX,pit.getId() );
		Assertions.assertEquals(Constants.ZERO,pit.getStoneCount() );
	}

}
