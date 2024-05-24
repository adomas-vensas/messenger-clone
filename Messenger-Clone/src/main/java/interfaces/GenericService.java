package interfaces;

import java.util.List;

public interface GenericService<T> {
    T findById(Long id);
    List<T> getAll();
    void save (T entity);
    T update (T entity);
    void delete(Long id);
}
