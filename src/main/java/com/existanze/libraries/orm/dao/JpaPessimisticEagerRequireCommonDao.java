package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.PessimisticBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 26/05/11
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class JpaPessimisticEagerRequireCommonDao<T extends PessimisticBean> extends JpaPessimisticCommonDao<T> {


    protected JpaPessimisticEagerRequireCommonDao(Class clazz) {
        super(clazz);
    }

    protected JpaPessimisticEagerRequireCommonDao(Class<T> clazz, DefaultSortOrder order) {
        super(clazz,order);
    }

    public void loadEager(List<T> beans){
        if(beans ==null || beans.size()  <=0){
            return;
        }

        for(T s : beans){
            loadEager(s);
        }
    }

    public abstract void loadEager(T bean);



    @Override
    @Transactional(readOnly = true)
    public T findById(Integer id) {
        T byId = super.findById(id);//To change body of overridden methods use File | Settings | File Templates.
        loadEager(byId);
        return byId;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllIncludeDeleted() {
        List<T> list = super.findAllIncludeDeleted();//To change body of overridden methods use File | Settings | File Templates.

        loadEager(list);
        return list;

    }


    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllPageable(int firstRow, int numberOfRows, String orderColumn, String orderType, Map<String, Object> filter) {
        List<T> list = super.findAllPageable(firstRow, numberOfRows, orderColumn, orderType, filter);

        loadEager(list);
        return list;
    }


    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(int firstRow, int limit) {
        List<T> list = super.findAll(firstRow, limit);//To change body of overridden methods use File | Settings | File Templates.
        loadEager(list);
        return list;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        List<T> list = super.findAll();//To change body of overridden methods use File | Settings | File Templates.
        loadEager(list);
        return list;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional(readOnly = true)
    public List<T> findDeleted() {
        List<T> list = super.findDeleted();//To change body of overridden methods use File | Settings | File Templates.
        loadEager(list);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public T findDeletedById(Integer id) {
        T byId = super.findDeletedById(id);//To change body of overridden methods use File | Settings | File Templates.
        loadEager(byId);
        return byId;
    }
}
