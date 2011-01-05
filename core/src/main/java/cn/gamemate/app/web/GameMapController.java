package cn.gamemate.app.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import cn.gamemate.app.domain.game.GameMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "gamemaps", formBackingObject = GameMap.class)
@RequestMapping("/gamemaps")
@Controller
public class GameMapController {
}
