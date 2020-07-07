package base.services.impl;

import base.commands.IngredientCommand;
import base.converters.IngredientCommandToIngredient;
import base.converters.IngredientToIngredientCommand;
import base.model.Ingredient;
import base.model.Recipe;
import base.repositories.RecipeRepository;
import base.repositories.UnitOfMeasureRepository;
import base.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            throw new RuntimeException(String.format("Recipe with id: %d not found.", recipeId));
        }

        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> optionalIngredient = recipe.getIngredients().stream()
                .filter(Objects::nonNull)
                .filter(ingredient -> ingredientId.equals(ingredient.getId()))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException(String.format("Ingredient with id: %d not found.", ingredientId));
        }

        return optionalIngredient.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Long recipeId = command.getRecipeId();
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            throw new RuntimeException(String.format("Recipe with id: %d not found.", recipeId));
        }
        Recipe recipe = optionalRecipe.get();
        Optional<Ingredient> optionalIngredient = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> command.getId().equals(ingredient.getId()))
                .findFirst();

        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            ingredient.setAmount(command.getAmount());
            ingredient.setDescription(command.getDescription());
            ingredient.setUom(unitOfMeasureRepository
                    .findById(command.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("Unit of measure not found.")));
        } else {
            recipe.addIngredient(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
        .filter(ingredient -> command.getId().equals(ingredient.getId()))
        .findFirst()
        .orElseThrow(RuntimeException::new));
    }
}
