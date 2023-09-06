package com.game.mancala.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
 	
import com.game.mancala.datamodel.PlayerDataEntity;

public interface PlayerRepository extends CrudRepository<PlayerDataEntity, Integer>  { 

	List<PlayerDataEntity> findAll();
	List<PlayerDataEntity> findByGameId(int i);
}
