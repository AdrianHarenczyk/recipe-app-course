package base.services.impl;

import base.model.Recipe;
import base.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void getRecipes() {
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
}