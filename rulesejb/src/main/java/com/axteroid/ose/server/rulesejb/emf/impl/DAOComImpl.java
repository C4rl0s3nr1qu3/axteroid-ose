package com.axteroid.ose.server.rulesejb.emf.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.rulesejb.emf.DAOCom;
import com.axteroid.ose.server.tools.constantes.EstadoRegType;
import com.axteroid.ose.server.tools.util.ListUtil;
import com.axteroid.ose.server.tools.util.MapUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public abstract class DAOComImpl<T extends Serializable> extends DAOEntityManagerCom implements DAOCom<T> {
	//private final static Logger log = Logger.getLogger(DAOComImpl.class);
	private static final Logger log = LoggerFactory.getLogger(DAOComImpl.class);
	protected Class<T> persistenceClass;
    
    @SuppressWarnings("unchecked")    
	public DAOComImpl() {
    	super();
    	try {    	
    		this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().
        		getGenericSuperclass()).getActualTypeArguments()[0];          
    		//log.info("DAOImpl ----> "+persistenceClass.getSimpleName());
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("DAOComImpl Exception \n"+errors);
		}
    }
    
    public void add(T object) {    	    	
        em.persist(object);   
        em.flush();
    }
    
    public void addWithOutAudit(T object) {
        em.persist(object);
    }

    public void update(T object) {
    	//log.info("em.isOpen() ----> "+em.isOpen()+"  update ----> "+persistenceClass.getSimpleName());  
    	em.merge(object);
    }
    
    public void updateWithOutAudit(T object) {
    	em.merge(object);
    }
    
    public void delete(Object id) {
        T o = em.find(persistenceClass, id);
        delete(o);
    }

    public void delete(T object) {
        if (object != null) {
            em.remove(object);
        }
    }

    public T findById(Object id) {
        return em.find(persistenceClass, id);
    }

    public List<T> findAll() {
        String query = "SELECT o FROM " + persistenceClass.getSimpleName() +" o ";      
        TypedQuery<T> tQuery = em.createQuery(query, persistenceClass);
        tQuery.getResultList();
        return tQuery.getResultList();
        //return (List<T>)em.createQuery(query).getResultList();
    }
    
    public List<T> findAllActive() {
        String query = "SELECT o FROM " + persistenceClass.getSimpleName() +" o " +
        				" WHERE o.estRegistro = "+EstadoRegType.ACTIVO.getCodigo();
        TypedQuery<T> tQuery = em.createQuery(query, persistenceClass);
        return tQuery.getResultList();
        //return (List<T>) em.createQuery(query, persistenceClass).getResultList();
    }

    public void refresh(List<T> result) {
        for (T o : result) {
            em.refresh(o);
        }
    }

    public void refresh(T o) {
        em.refresh(o);
    }

    public List<T> findByColumnName(String columnName, Object value) {
        String query = "SELECT o FROM " + persistenceClass.getSimpleName() + " o WHERE o." + columnName + " = :" + columnName;
        return em.createQuery(query, persistenceClass).setParameter(columnName, value).getResultList();
    }
       
    public List<T> findLikeColumnName(String columnName, String value) {
        String query = "SELECT o FROM " + persistenceClass.getSimpleName() + " o WHERE o." + columnName + " LIKE :" + columnName;
        StringBuffer sb = new StringBuffer();
        sb.append("%");
        sb.append(value);
        sb.append("%");
        return em.createQuery(query, persistenceClass).setParameter(columnName, sb.toString()).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> namedQuery(String queryName, List<Object> parameterValues) {
        Query query = em.createNamedQuery(queryName, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> namedQuery(String queryName, Map<String, Object> parameterValues) {
        Query query = em.createNamedQuery(queryName, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> query(String queryString, List<Object> parameterValues) {
        Query query = em.createQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> query(String queryString, Map<String, Object> parameterValues) {
        Query query = em.createQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> nativeQuery(String queryString, List<Object> parameterValues) {
        Query query = em.createNativeQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues) {
        Query query = em.createNativeQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> nativeQuery(String queryString, List<Object> parameterValues, String resultSetMapping) {
        Query query = em.createNativeQuery(queryString, resultSetMapping);
        return setParameters(query, parameterValues).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues, String resultSetMapping) {
        Query query = em.createNativeQuery(queryString, resultSetMapping);
        return setParameters(query, parameterValues).getResultList();
    }

    protected Query setParameters(Query query, List<Object> parameterValues) {
        int idx = 0;
        for (Object o : parameterValues) {
            query = query.setParameter(idx, o);
            idx++;
        }
        return query;
    }

    protected Query setParameters(Query query, Map<String, Object> parameterValues) {
        for (String key : parameterValues.keySet()) {
            query = query.setParameter(key, parameterValues.get(key));
        }
        return query;
    }
    
    @SuppressWarnings("unchecked")
	public List<T> findByMap(Map<String, Object> parameterValues,Map<String, String> mapOperador, List<String> lstOrder,List<String> lstJoin) 
    {
    	String query = "SELECT o FROM " + persistenceClass.getSimpleName() + " o ";
    	if (ListUtil.isNotBlankOrNull(lstJoin))
    	{
        	for (String key : lstJoin) 
        	{	
        		query = query + " LEFT JOIN o." + key + " ";
            }	
        	
    	}
    		
    	if (MapUtil.isNotBlankOrNull_O(parameterValues) )
    	{
    		query = query + " WHERE ";
    		
        	for (String key : parameterValues.keySet()) 
        	{	
        		if (MapUtil.isNotBlankOrNull(mapOperador) && StringUtil.isNotBlankOrNull(mapOperador.get(key)))
        			query = query + " o." + key + " " + mapOperador.get(key) + " :" + key + " AND";
        		else
        			query = query + " o." + key + " = :" + key + " AND";
            }	
        	query = query.substring(0,query.length()-3);
    	}
    	
    	if (ListUtil.isNotBlankOrNull(lstOrder))
		{
    		query = query + " ORDER BY ";
        	for (String key : lstOrder) 
        	{	
        		query = query + " o." + key +  ",";
            }	
        	query = query.substring(0,query.length()-1);
		}
    	Query q = em.createQuery(query, persistenceClass);
    	return setParameters(q, parameterValues).getResultList();
    }
    
    public List<T> findByMap(Map<String, Object> parameterValues) 
    {
    	return findByMap(parameterValues,null,null,null);
    }
    
    public T getByMap(Map<String, Object> parameterValues) 
    {
    	List<T> lst = findByMap(parameterValues);
    	
    	if (ListUtil.isNotBlankOrNull_T(lst))
    		return lst.get(0);
    	
    	return null;
    }
    public List<T> findByFiltro(Object filtro) {
		return null;
	}
    
    public void closeEntityManager() {    	
    	super.closeEntityManager();
    }
    
}
