package com.game.mancala.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.game.mancala.datamodel.GameDataEntity;

public interface GameRepository extends CrudRepository<GameDataEntity, Integer>  {
 
	List<GameDataEntity> findAll();
	
}
