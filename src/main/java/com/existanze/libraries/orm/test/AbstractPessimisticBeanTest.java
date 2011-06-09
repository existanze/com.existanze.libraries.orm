package com.existanze.libraries.orm.test;

import com.existanze.libraries.orm.domain.PessimisticBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 22:57
 */
public abstract class AbstractPessimisticBeanTest {

    private PessimisticBean bean;

    public AbstractPessimisticBeanTest(PessimisticBean bean) {
        this.bean = bean;

    }

    @Test
    public void testIsDeleted(){

        this.bean.setDeleted(System.currentTimeMillis());
        Assert.assertTrue(this.bean.hasBeenDeleted());

    }
}
