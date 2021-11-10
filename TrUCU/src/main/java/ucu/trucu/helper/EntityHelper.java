package ucu.trucu.helper;

import java.sql.SQLException;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 * @param <K> Tipo de la clave primaria
 * @param <E> Tipo de la entidad
 */
public interface EntityHelper<K, E> {

    int create(E entity) throws SQLException;

    int update(K key, E entity) throws SQLException;

    boolean delete(K key) throws SQLException;

    Page<E> filter(int pageSize, int pageNumber, Filter filter);

}
