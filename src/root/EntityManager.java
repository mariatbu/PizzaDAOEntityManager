package root;
import java.util.ArrayList;
import java.util.List;

import Objects.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;


public class EntityManager implements IEntityManager{
    private List<IRunnables> runnables = new ArrayList<IRunnables>();
    private IConfiguration configuration = null;
    
    @Override
    public void save(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(
                this.configuration.getUrl(),
                this.configuration.getUser(),
                this.configuration.getPassword()
            );
            connection.setAutoCommit(false);
            for(IRunnables runnable: this.runnables){
                PreparedStatement statement = connection.prepareStatement(runnable.getSQL());
                runnable.run(statement);
                statement.executeUpdate();
            }
            connection.commit();
        }
        catch(SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
                runnables.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }        
    }

    @Override
    public <T> IEntityManager addStatement(T entity, String sql, Statement<T> statement) {
        IRunnables runnable = new Runnables<T>(sql, entity, statement);
        this.runnables.add(runnable);
        return this;
    }

    @Override
    public <T> IEntityManager addRangeStatement(Iterable<T> iterable, String sql, Statement<T> statement) {
        for(T t: iterable){
            IRunnables runable = new Runnables<T>(sql, t, statement);
            this.runnables.add(runable);
        }
        return this;
    }

    public static IEntityManager buildConnection(IConfiguration configuration){
        return new EntityManager(configuration);
    }

    private EntityManager(IConfiguration configuration){
        this.configuration = configuration;
    }

    public <T extends Entity> T Select(ResultSet resultSet, T entity){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(
                this.configuration.getUrl(),
                this.configuration.getUser(),
                this.configuration.getPassword()
            );
            connection.setAutoCommit(false);
            for(IRunnables runnable: this.runnables){
                PreparedStatement statement = connection.prepareStatement(runnable.getSQL());
                runnable.run(statement);
                resultSet = statement.executeQuery();
                while (resultSet.next()){
                    runnable.run(statement);
                }
                statement.close();
            }
            connection.commit();
        }
        catch(SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
                runnables.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 

        // try{
        //     //T objectResult = Instance.getInstanceOf(entity.getClass());
        //     // T objectResult;
        //     // Class<? extends Entity> classResult = entity.getClass();
        //     // objectResult = (T) classResult.getDeclaredConstructor().newInstance();
        //     T objectResult = entity.getClass().getDeclaredConstructor().newInstance();
        // }
        // catch(InstantiationException  | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception){
        //     System.out.println("Couldn't instantiate the object.");
        // }
        return entity;
        
        // Class<T> aClass=T.class();
        
        // return aClass.newInstance();

    }

}
