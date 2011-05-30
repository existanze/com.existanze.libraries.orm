package com.existanze.libraries.orm.domain.base;

import com.existanze.libraries.orm.domain.OrderedBean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 23:33
 */

@MappedSuperclass
public abstract class AbstractOrderedBean implements OrderedBean {

    @Column(name="pos")
    @Basic(optional = false)
    private int position=0;

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int posistion) {
        this.position = posistion;
    }
}
