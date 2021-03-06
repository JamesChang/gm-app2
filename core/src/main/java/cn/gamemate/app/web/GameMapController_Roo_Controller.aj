// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.web;

import cn.gamemate.app.domain.game.GameMap;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect GameMapController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String GameMapController.create(@Valid GameMap gameMap, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("gameMap", gameMap);
            return "gamemaps/create";
        }
        uiModel.asMap().clear();
        gameMap.persist();
        return "redirect:/gamemaps/" + encodeUrlPathSegment(gameMap.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String GameMapController.createForm(Model uiModel) {
        uiModel.addAttribute("gameMap", new GameMap());
        return "gamemaps/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String GameMapController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("gamemap", GameMap.findGameMap(id));
        uiModel.addAttribute("itemId", id);
        return "gamemaps/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String GameMapController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("gamemaps", GameMap.findGameMapEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) GameMap.countGameMaps() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("gamemaps", GameMap.findAllGameMaps());
        }
        return "gamemaps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String GameMapController.update(@Valid GameMap gameMap, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("gameMap", gameMap);
            return "gamemaps/update";
        }
        uiModel.asMap().clear();
        gameMap.merge();
        return "redirect:/gamemaps/" + encodeUrlPathSegment(gameMap.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String GameMapController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("gameMap", GameMap.findGameMap(id));
        return "gamemaps/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String GameMapController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        GameMap.findGameMap(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/gamemaps";
    }
    
    @ModelAttribute("gamemaps")
    public Collection<GameMap> GameMapController.populateGameMaps() {
        return GameMap.findAllGameMaps();
    }
    
    String GameMapController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
