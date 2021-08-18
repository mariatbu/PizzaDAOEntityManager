package root;
import java.sql.ResultSet;

import Objects.Entity;

public interface IEntityManager {
    
    public void save();
    public <T> IEntityManager addStatement(final T entity, String sql, Statement<T> statement);
    public <T> IEntityManager addRangeStatement(final Iterable<T> iterable, String sql, Statement<T> statement);
    public <T extends Entity> T Select(ResultSet resultSet, T entity); //TODO: meter un ResultSet como argumento
    //TODO: ejecutar el select
}
