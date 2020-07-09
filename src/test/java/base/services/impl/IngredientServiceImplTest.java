package base.services.impl;

import base.commands.IngredientCommand;
import base.converters.IngredientCommandToIngredient;
import base.converters.IngredientToIngredientCommand;
import base.converters.UnitOfMeasureCommandToUnitOfMeasure;
import base.converters.UnitOfMeasureToUnitOfMeasureCommand;
import base.model.Ingredient;
import base.model.Recipe;
import base.repositories.RecipeRepository;
import base.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientServiceImpl ingredientService;

    IngredientToIngredientCommand ingredientToIngredientCommand;

    IngredientCommandToIngredient ingredientCommandToIngredient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService =
                new IngredientServiceImpl(recipeRepository, unitOfMeasureRepository,
                        ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        //GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredientOne = new Ingredient();
        Ingredient ingredientTwo = new Ingredient();
        Ingredient ingredientThree = new Ingredient();
        ingredientOne.setId(1L);
        ingredientTwo.setId(2L);
        ingredientThree.setId(3L);

        recipe.addIngredient(ingredientOne);
        recipe.addIngredient(ingredientTwo);
        recipe.addIngredient(ingredientThree);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        //WHEN
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 2L);
        //THEN
        assertNotNull(ingredientCommand);
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveRecipeCommand() {
        // GIVEN
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(3L);

        // WHEN
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        // THEN
        assertEquals(3L, savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void testDeleteIngredient() {
        // GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        recipe.addIngredient(ingredient);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        // WHEN
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        ingredientService.deleteIngredient(1L, 3L);

        // THEN
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}