package com.game.mancala.datamapper;

import org.mapstruct.Mapper;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.model.Game;

@Mapper(componentModel = "spring") 
public interface GameDataMapper {  
	
	 
	public GameDataEntity mapBOToEntity(Game bo);
	
	 
	public Game mapEntityToBO(GameDataEntity entity);

}
