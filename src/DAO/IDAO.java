package DAO;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface IDAO<T> {
    void insert(T t);
    
    void update(T t, String[] params);
    
    void delete(UUID id);

    Optional<T> select(UUID id);

}
