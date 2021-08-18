import Objects.Pizza;

public class App {
    public static void main(String[] args) throws Exception {
        Pizza pizza = new Pizza();
        pizza.id=10;
        EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
           statement.setInt(1, entity.id);   
        }).save();


       Pizza p =  EntityManager
        .buildConnection(Configuration.getConfiguration())
        .addStatement(pizza, "sql", (statement, entity)->{
            statement.setInt(1, entity.id);
        }).Select<Pizza>((resulset,entidad)->{
            entidad.id = resulset.getString("name")
        });
    }
}
