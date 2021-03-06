package base.controllers;

import base.commands.RecipeCommand;
import base.model.Recipe;
import base.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/recipe/{id}/show")
    public String getRecipe(@PathVariable String id, Model model) {
        Long recipeId = Long.valueOf(id);
        Recipe recipe = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }

    @GetMapping("recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable("id") String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
        return "recipe/recipeform";
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return String.format("redirect:/recipe/%d/show", savedCommand.getId());
    }

    @RequestMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable("id") String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/index";
    }
}
