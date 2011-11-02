package cn.gamemate.app.domain.game;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.gamemate.app.domain.attr.ChoiceField;
import cn.gamemate.app.domain.event.EventCenter;

@Configuration
class DefaultGameDefinition {
	private static Logger logger = LoggerFactory.getLogger(EventCenter.class);

	@Bean
	public Game war3() {
		Game game = new Game();
		game.setId(1);
		game.setName("war3normal");
		Map<String, String> races = new HashMap<String, String>();
		races.put("Random", "96");
		races.put("Human", "65");
		races.put("Orc", "66");
		races.put("NightElf", "68");
		races.put("Undead", "72");
		game.addUserAttribute("race", new ChoiceField(races, "种族", false, "96",
				"Race"));
		Map<String, String> colors = new HashMap<String, String>();
		colors.put("#FF0303", "0");
		colors.put("#0042FF", "1");
		colors.put("#1CE6B9", "2");
		colors.put("#540081", "3");
		colors.put("#FFFC01", "4");
		colors.put("#FE8A0E", "5");
		colors.put("#20C000", "6");
		colors.put("#E55BB0", "7");
		colors.put("#959697", "8");
		colors.put("#7EBFF1", "9");
		game.addUserAttribute("color", new ChoiceField(colors, "颜色", false,
				null, "Color"));
		game.setPhysicalGame(PhysicalGame.findPhysicalGame(2));
		Game.war3 = game;
		return game;
	}

	@Bean
	public Game dota() {
		Game game = new Game();
		game.setId(2);
		game.setName("dota");
		game.setPhysicalGame(PhysicalGame.findPhysicalGame(2));
		Game.dota = game;
		return game;
	}

	@Bean
	public Game sc() {
		Game game = new Game();
		game.setId(3);
		game.setName("sc");
		Map<String, String> scRaces = new HashMap<String, String>();
		scRaces.put("Random", "6");
		scRaces.put("Zerg", "0");
		scRaces.put("Terran", "1");
		scRaces.put("Protoss", "2");
		game.addUserAttribute("race", new ChoiceField(scRaces, "种族", false,
				"6", "Race"));
		game.setPhysicalGame(PhysicalGame.findPhysicalGame(1));
		Game.sc = game;
		return game;
	}
	
	@Bean
	public Game lol() {
		Game game = new Game();
		game.setId(5);
		game.setName("lol");
		game.setPhysicalGame(PhysicalGame.findPhysicalGame(3));
		Game.lol = game;
		return game;
	}
	
	@Bean
	public Game cs() {
		Game game = new Game();
		game.setId(6);
		game.setName("CS");
		game.setPhysicalGame(PhysicalGame.findPhysicalGame(4));
		Game.cs = game;
		return game;
	}

}
