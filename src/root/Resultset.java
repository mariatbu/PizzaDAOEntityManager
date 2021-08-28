package root;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Resultset<T> {
    public void run(ResultSet resultSet, T entity) throws SQLException;
}
