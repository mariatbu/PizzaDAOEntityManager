package DAO;

import Objects.Pizza;
import Objects.Entity;
import root.EntityManager;
import root.Configuration;

import java.util.UUID;
import java.util.Optional;

public class PizzaDAO implements IDAO<Pizza> {
    
    @Override
    public void insert(Pizza pizza){
        String sql = """
                    INSERT INTO pizza(id, name, url)
                    VALUES
                    (?,?,?); """;
        UUID uuid = pizza.getId();
        byte[] pizzaId = UuidAdapter.getBytesFromUUID(uuid);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
            statement.setBytes(1, pizzaId);
            statement.setString(2, pizza.getName());
            statement.setString(3, pizza.getUrl());
        }).save();
    }

    @Override
    public void update(Pizza pizza, String[] params){
        String sql = """
                    UPDATE pizza
                    SET name = ?, url = ?
                    WHERE id = ?;""";
        UUID uuid = pizza.getId();
        byte[] pizzaId = UuidAdapter.getBytesFromUUID(uuid);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
            statement.setString(1, pizza.getName());
            statement.setString(2, pizza.getUrl());
            statement.setBytes(3, pizzaId);
        }).save();
    }

    @Override
    public void delete(UUID id){
        String sql = """
                    DELETE FROM pizza
                    WHERE id = ?;""";
        Pizza pizza = new Pizza();
        pizza.setId(id);
        byte[] pizzaId = UuidAdapter.getBytesFromUUID(id);
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
            statement.setBytes(1, pizzaId);
        }).save();
    }

    @Override
    public Optional<Pizza> select(UUID id){
        Pizza pizza = new Pizza();

        return Optional.of(pizza);
    }

}
