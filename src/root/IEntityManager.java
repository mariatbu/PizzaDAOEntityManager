package root;

import Objects.Entity;

public interface IEntityManager {
    public void save();
    public <T> IEntityManager addStatement(final T entity, String sql, Statement<T> statement);
    public <T> IEntityManager addRangeStatement(final Iterable<T> iterable, String sql, Statement<T> statement);
    public <T extends Entity> T select(Class<T> clazz, Resultset<T> resultset); 
}
