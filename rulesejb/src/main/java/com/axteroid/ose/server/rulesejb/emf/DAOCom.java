package com.axteroid.ose.server.rulesejb.emf;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DAOCom<T extends Serializable> {
    public void add(T object);
    
    public void addWithOutAudit(T object);

    public void update(T object);
    
    public void updateWithOutAudit(T object);
    
    public void delete(Object id);

    public void delete(T object);

    public T findById(Object id);

    public List<T> findAll();
    
    public List<T> findAllActive();

    public void refresh(List<T> result);

    public void refresh(T o);

    public List<T> findByColumnName(String columnName, Object value);

    public List<T> findLikeColumnName(String columnName, String value);

    public List<T> namedQuery(String queryName, List<Object> parameterValues);

    public List<T> namedQuery(String queryName, Map<String, Object> parameterValues);

    public List<T> query(String queryString, List<Object> parameterValues);

    public List<T> query(String queryString, Map<String, Object> parameterValues);

    public List<T> nativeQuery(String queryString, List<Object> parameterValues);

    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues);

    public List<T> nativeQuery(String queryString, List<Object> parameterValues, String resultSetMapping);

    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues, String resultSetMapping);
    
    public List<T> findByMap(Map<String, Object> parameterValues) ;
    
    public T getByMap(Map<String, Object> parameterValues); 
    
    public List<T> findByFiltro(Object filtro) ;
}
