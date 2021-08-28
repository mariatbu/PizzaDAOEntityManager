package DAO;

import java.util.UUID;
import java.util.Optional;

public interface IDAO<T> {
    void insert(T t);
    
    void update(T t, String[] params);
    
    void delete(UUID id);

    Optional<T> select(UUID id);

}
