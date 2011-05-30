package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.PessimisticBean;
import com.existanze.libraries.orm.util.JpaUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * User: fotis
 * Date: 07/05/11
 * Time: 21:22
 */
public abstract class JpaPessimisticCommonDao<T extends PessimisticBean> extends JpaCommonDao<T> implements CommonPessimisticDao<T>{

    protected JpaPessimisticCommonDao(Class<T> clazz) {
        super(clazz);
    }

    protected JpaPessimisticCommonDao(Class<T> clazz, DefaultSortOrder order) {
        super(clazz, order);
    }


    @Override
    @Transactional(readOnly = true)
    public int count(Map<String, Object> filter) {
        return JpaUtils.countPessimistic(getEntityManager(), getClazz(), filter);
    }


    @Override
    @Transactional(readOnly = true)
    public List<T> findAllPageable(int firstRow, int numberOfRows, String orderColumn, String orderType, Map<String,Object> filter) {

        String s = JpaUtils.buildQuery(
            "SELECT b FROM " + getClazz().getName() + " b WHERE b.deleted IS NULL",
            "b",
            filter);

        StringBuilder sb = new StringBuilder(s);



        if(!StringUtils.isEmpty(orderColumn)){
            sb.append(" ORDER BY b."+orderColumn);
        }else{
           sb.append(" ORDER BY b.id ");
        }

        if(!StringUtils.isEmpty(orderType)){
            sb.append(" "+orderType);
        }


        Query query = getEntityManager().createQuery(
                sb.toString()
                );

        JpaUtils.setParameters(query,filter);

        query.setFirstResult(firstRow);
        query.setMaxResults(numberOfRows);

        List resultList = query.getResultList();

        return resultList;

    }

    @Override
    @Transactional
    public void delete(T bean) {
        delete(bean.getId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        JpaUtils.pessimisticDelete(getEntityManager(),getClazz(),id);
    }

    @Override
    @Transactional
    public T findById(Integer id){
        return JpaUtils.findPessimisticById(getEntityManager(),getClazz(),id);
    }

    @Override
    @Transactional
    public List<T> findAll() {
        return (List<T>) JpaUtils.<T>findPessimisticAll(getEntityManager(), getClazz(),getOrder());
    }

    @Override
    @Transactional(readOnly =true)
    public List<T> findAll(int firstRow, int limit) {
        return (List<T>)JpaUtils.<T>findPessimisticAll(getEntityManager(),getClazz(),firstRow,limit,getOrder());
    }

    @Transactional
    @Override
    public List<T> findAllIncludeDeleted() {
        return (List<T>) JpaUtils.findAll(getEntityManager(), getClazz(),getOrder());
    }

    @Transactional
    @Override
    public List<T> findDeleted() {
        return (List<T>) JpaUtils.findDeleted(getEntityManager(),getClazz());
    }

    @Transactional
    @Override
    public T findDeletedById(Integer id){
        return JpaUtils.findDeletedById(getEntityManager(),getClazz(),id);
    }
}
