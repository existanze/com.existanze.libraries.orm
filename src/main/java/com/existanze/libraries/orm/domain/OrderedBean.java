package com.existanze.libraries.orm.domain;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 23:36
 */
public interface OrderedBean extends SimpleBean{

    public int getPosition();
    public void setPosition(int position);
}
