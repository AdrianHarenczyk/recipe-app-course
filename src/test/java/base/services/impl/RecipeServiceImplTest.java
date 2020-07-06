package base.services.impl;

import base.converters.RecipeCommandToRecipe;
import base.converters.RecipeToRecipeCommand;
import base.model.Recipe;
import base.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getRecipesTest() {
        //GIVEN
        Set<Recipe> recipeData = new HashSet<>();
        Recipe recipe = new Recipe();
        recipeData.add(recipe);
        when(recipeRepository.findAll()).thenReturn(recipeData);
        //WHEN
        Set<Recipe> recipes = recipeService.getRecipes();
        //THEN
        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipesByIdTest() {
        //GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        //WHEN
        Recipe returnedRecipe = recipeService.findById(1L);
        //THEN
        assertNotNull(returnedRecipe);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }
}