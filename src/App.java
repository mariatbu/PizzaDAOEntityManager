import java.util.UUID;

import DAO.IngredientDAO;
import Objects.Ingredient;

public class App {
    public static void main(String[] args) throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("tomate");
        ingredient.setPrice(2.0);

        IngredientDAO ingredientDAO = new IngredientDAO();

        ingredientDAO.insert(ingredient);
        ingredientDAO.select(ingredient.getId());
        String [] params = new String[2];
        params[0] = "pimiento";
        ingredientDAO.update(ingredient, params);
        ingredientDAO.delete(ingredient.getId());
        
    }
}
