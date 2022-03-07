package recipes.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.entities.Recipe;
import recipes.entities.User;
import recipes.services.RecipeService;
import recipes.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/recipe")
@RestController()
public class RecipeController {
    final RecipeService recipeService;
    final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.findRecipeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody Recipe recipeBody,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUserByEmail(userDetails.getUsername());
        recipeBody.setUser(user);
        Recipe recipe = recipeService.saveRecipe(recipeBody);
        user.getRecipes().add(
                recipe
        );
        userService.save(user);

        return Map.of("id", recipe.getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipeBody, @AuthenticationPrincipal UserDetails details) {
        Recipe recipe = recipeService
                .findRecipeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!details.getUsername().equals(recipe.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        recipe.setName(recipeBody.getName());
        recipe.setCategory(recipeBody.getCategory());
        recipe.setDescription(recipeBody.getDescription());
        recipe.setIngredients(recipeBody.getIngredients());
        recipe.setDirections(recipeBody.getDirections());

        recipeService.saveRecipe(recipe);
    }


    @GetMapping("/search")
    public List<Recipe> findRecipes(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String category) {
        if (name != null && category == null) {
            return recipeService.findRecipesByName(name);
        } else if (name == null && category != null) {
            return recipeService.findRecipesByCategory(category);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        Recipe recipe = recipeService
                .findRecipeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!details.getUsername().equals(recipe.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        recipeService.deleteRecipeById(id);
    }


}
