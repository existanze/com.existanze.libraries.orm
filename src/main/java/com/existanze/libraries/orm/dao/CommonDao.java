package com.existanze.libraries.orm.dao;


import com.existanze.libraries.orm.domain.SimpleBean;

import java.util.List;
import java.util.Map;

/**
 * This interface should be implemented by all daos
 *
 * User: fotis
 * Date: 30/04/11
 * Time: 01:33
 */
public interface CommonDao<T extends SimpleBean> {

    /**
     * Inserts the domain in the datastore and returned the
     * managed domain with the id
     *
     * throws @{DataIntegrityException} which hold database
     * constraints
     *
     *
     * @param bean
     * @return
     */
    public T insert(T bean);


    /**
     * Updates the domain in the datastore and returned the
     * managed domain with the id
     *
     * throws @{DataIntegrityException} which hold database
     * constraints
     *
     * @param bean
     * @return
     */
    public T update(T bean);



    public void delete(T bean);
    public void delete(Integer id);


    /**
     * Uses the @SimpleBean#getId() method to find the domain
     * in the datasource
     *
     * @param id
     * @return
     */
    public T findById(Integer id);

    public List<T> findAll();
    public List<T> findAll(int firstRow, int limit);
    public List<T> findAllPageable(int firstRow, int numberOfRows, String orderColumn, String orderType, Map<String, Object> filter);
    public int count();
    public int count(Map<String, Object> filter);


}
