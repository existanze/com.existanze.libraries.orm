package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.PessimisticBean;

import java.util.List;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 19:35
 */
public interface CommonPessimisticDao<T extends PessimisticBean> extends CommonDao<T> {

    public List<T> findAllIncludeDeleted();
    public List<T> findDeleted();
    public T findDeletedById(Integer id);

}
