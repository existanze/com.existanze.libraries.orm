package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.SimpleBean;
import com.existanze.libraries.orm.util.JpaUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * User: fotis
 * Date: 30/04/11
 * Time: 01:49
 */
public abstract class JpaCommonDao<T extends SimpleBean> implements CommonDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(JpaCommonDao.class);

    private final Class<T> clazz;

    private DefaultSortOrder order;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    protected JpaCommonDao(Class<T> clazz) {
        this(clazz,
                new DefaultSortOrder("id",true));
    }

    protected JpaCommonDao(Class<T> clazz, DefaultSortOrder order) {
        this.clazz =clazz;
        this.order = order;
    }


    protected Class<T> getClazz(){
        return this.clazz;
    }

    @Override
    public int count(){
        return count(null);
    }

    @Override
    public int count(Map<String,Object> filter){
        return JpaUtils.count(getEntityManager(), this.clazz, filter);
    }


    @Transactional
    public T insert(T bean) {
        getEntityManager().persist(bean);
        return bean;    }

    @Transactional
    public T update(T bean) {
        T merge = getEntityManager().merge(bean);
        return merge;
    }

    @Transactional
    public void delete(T bean) {
        delete(bean.getId());
    }

    @Transactional
    public void delete(Integer id) {
        JpaUtils.delete(getEntityManager(),this.clazz,id);
    }

    @Transactional(readOnly = true)
    public T findById(Integer id) {
        return JpaUtils.findById(getEntityManager(),this.clazz,id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return (List<T>) JpaUtils.findAll(getEntityManager(), this.clazz,order);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(int firstRow, int limit) {
        return (List<T>) JpaUtils.findAll(getEntityManager(), this.clazz,firstRow,limit,order);
    }



    @Override
    @Transactional(readOnly = true)
    public List<T> findAllPageable(int firstRow, int numberOfRows, String orderColumn, String orderType, Map<String,Object> filter) {

        String s = JpaUtils.buildQuery(
                "SELECT b FROM " + clazz.getName() + " b",
                "b",
                filter);

        StringBuilder sb = new StringBuilder(s);


        if(!StringUtils.isEmpty(orderColumn)){
            sb.append(" ORDER BY b."+orderColumn);
        }else{
            sb.append(
                     " ORDER BY b.id "
            );
        }
        if(!StringUtils.isEmpty(orderType)){
            sb.append(" "+orderType);
        }



        Query query = entityManager.createQuery(sb.toString());

        JpaUtils.setParameters(query,filter);


        query.setFirstResult(firstRow);
        query.setMaxResults(numberOfRows);

        List resultList = query.getResultList();

        return resultList;

    }

    public DefaultSortOrder getOrder() {
        return order;
    }
}
