package com.game.mancala.datamapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.model.Player;

@Mapper(componentModel = "spring",uses = {GameDataMapper.class})
public interface PlayerDataMapper {
	
	@Mapping(target = "game", ignore = true)
	PlayerDataEntity mapBoToEntity(Player bo); 
	 
	Player mapEntityToBO(PlayerDataEntity entity);
 
}
