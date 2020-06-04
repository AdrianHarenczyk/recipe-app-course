package base.controllers;

import base.model.Recipe;
import base.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IndexControllerTest {
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    IndexController indexController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    void getIndexPage() {
        //GIVEN
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        Recipe recipe = new Recipe();
        recipe.setId(2L);
        recipes.add(recipe);

        String shouldReturnString = "index";

        when(recipeService.getRecipes()).thenReturn(recipes);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //WHEN
        String viewName = indexController.getIndexPage(model);

        //THEN
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        assertEquals(shouldReturnString, viewName);
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());
    }
}