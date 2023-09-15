package com.game.mancala.datamapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.model.Game;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)  
public interface GameDataMapper {   	
	 
	public GameDataEntity mapBOToEntity(Game bo); 		
	public Game mapEntityToBO(GameDataEntity entity);	
}
