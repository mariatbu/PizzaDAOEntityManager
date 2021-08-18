package root;
import Objects.Entity;

public class Instance<T extends Entity> {

    public static <T> Class<T>  accessibleConstructor(Class<?> type) throws InstantiationException, IllegalAccessException{
        return (Class<T>) type;
    }
}
