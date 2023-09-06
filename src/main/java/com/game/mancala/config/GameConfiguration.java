package com.game.mancala.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;


@Configuration
@ConfigurationProperties(prefix = "mancala")
@Getter
@Setter
public class GameConfiguration {
	
	@Value("${playerCount:2}")
	private Integer numberOfPlayers;

	@Value("${stonesPerPit:6}")
	private Integer stonesPerPit;
		
}
