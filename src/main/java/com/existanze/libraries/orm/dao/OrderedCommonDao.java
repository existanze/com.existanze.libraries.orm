package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.OrderedBean;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 23:35
 */
public interface OrderedCommonDao<T extends OrderedBean> extends CommonDao<T> {

    public T moveUp(T bean);
    public T moveDown(T bean);

}
