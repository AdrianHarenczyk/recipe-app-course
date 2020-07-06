package base.services.impl;

import base.commands.IngredientCommand;
import base.converters.IngredientToIngredientCommand;
import base.model.Recipe;
import base.repositories.RecipeRepository;
import base.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

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
}
