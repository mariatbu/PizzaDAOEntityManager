package DAO;

import java.util.Optional;
import java.util.UUID;

import Objects.Ingredient;
import root.Configuration;
import root.EntityManager;

public class IngredientDAO implements IDAO<Ingredient> {
    
    @Override
    public void insert(Ingredient ingredient){
        String sql = """
                    INSERT INTO ingredients(id, ingredientname, price)
                    VALUES
                    (?,?,?); """;
        UUID uuid = ingredient.getId();
        byte[] ingredientId = UuidAdapter.getBytesFromUUID(uuid);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(ingredient, "sql", (statement, entity)->{
            statement.setBytes(1, ingredientId);
            statement.setString(2, ingredient.getName());
            statement.setDouble(3, ingredient.getPrice());
        }).save();
    }

    @Override
    public void update(Ingredient ingredient, String[] params){
        String sql = """
                    UPDATE ingredients
                    SET ingredientname = ?, price = ?
                    WHERE id = ?;""";
        UUID uuid = ingredient.getId();
        byte[] ingredientId = UuidAdapter.getBytesFromUUID(uuid);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(ingredient, "sql", (statement, entity)->{
            statement.setString(1, ingredient.getName());
            statement.setDouble(2, ingredient.getPrice());
            statement.setBytes(3, ingredientId);
        }).save();
    }

    @Override
    public void delete(UUID id){
        String sql = """
                    DELETE FROM ingredients
                    WHERE id = ?;""";
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        byte[] ingredientId = UuidAdapter.getBytesFromUUID(id);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(ingredient, "sql", (statement, entity)->{
            statement.setBytes(1, ingredientId);
        }).save();
    }

    @Override
    public Optional<Ingredient> select(UUID id){
        Ingredient ingredient = new Ingredient();
        String sql = """ 
                    SELECT id, ingredientname, price 
                    FROM ingredients
                    WHERE id=?""";
        Ingredient ingredientResult = EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(ingredient, "sql", (statement, entity)->{
            statement.setBytes(1, UuidAdapter.getBytesFromUUID(id));
        })
        .select(Ingredient.class, (resultSet, entity) -> {
            entity.setId(UuidAdapter.getUUIDFromBytes(resultSet.getBytes("id")));
            entity.setName(resultSet.getString("ingredientname"));
            entity.setPrice(resultSet.getDouble("price"));
         });

        return Optional.of(ingredientResult);
    }

}
