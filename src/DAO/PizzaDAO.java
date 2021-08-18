package DAO;

import Objects.Pizza;
import Objects.Entity;
import root.EntityManager;
import root.Configuration;

import java.util.UUID;

public class PizzaDAO {
    
    public void insert(Pizza pizza){
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
           statement.setInt(1, entity.id);   
        }).save();
    }
}
