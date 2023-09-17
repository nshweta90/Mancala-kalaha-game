package com.game.mancala.datamapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.model.Player;

@Mapper(componentModel = "spring",uses = {GameDataMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayerDataMapper { 
	 
	PlayerDataEntity mapBoToEntity(Player bo); 
	 
	Player mapEntityToBO(PlayerDataEntity entity);
 
}
