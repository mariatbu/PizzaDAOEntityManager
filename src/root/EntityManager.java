package root;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Objects.Entity;

public class EntityManager implements IEntityManager {
    private List<IRunnables> runnables = new ArrayList<IRunnables>();
    private IConfiguration configuration = null;

    @Override
    public void save() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                this.configuration.getUrl(), 
                this.configuration.getUser(),
                this.configuration.getPassword()
            );
            connection.setAutoCommit(false);
            for (IRunnables runnable : this.runnables) {
                PreparedStatement statement = connection.prepareStatement(runnable.getSQL());
                runnable.run(statement);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (!connection.isClosed()) {
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
        for (T t : iterable) {
            IRunnables runable = new Runnables<T>(sql, t, statement);
            this.runnables.add(runable);
        }
        return this;
    }

    public static IEntityManager buildConnection(IConfiguration configuration) {
        return new EntityManager(configuration);
    }

    private EntityManager(IConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T extends Entity> T select(Class<T> clazz, Resultset<T> resultset) {
        Connection connection = null;
        T entity = null;
        try {
            connection = DriverManager.getConnection(this.configuration.getUrl(), this.configuration.getUser(),
                    this.configuration.getPassword());
            connection.setAutoCommit(false);
            IRunnables runnable = this.runnables.get(0);

            PreparedStatement statement = connection.prepareStatement(runnable.getSQL());

            ResultSet resultSetSQL = statement.executeQuery();

            while (resultSetSQL.next()) {
                entity = clazz.getConstructor().newInstance();
                resultset.run(resultSetSQL, entity);
            }

            connection.commit();
        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        } finally {

            this.runnables.clear();
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

}
