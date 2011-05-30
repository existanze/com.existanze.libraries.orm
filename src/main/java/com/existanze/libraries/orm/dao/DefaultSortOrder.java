package com.existanze.libraries.orm.dao;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 26/05/11
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSortOrder {
    private String field;
    private boolean direction;

    /**
     *
     * @param field
     * @param direction ASC if ture DESC if false
     */
    public DefaultSortOrder(String field, boolean direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }


    public String getDirection() {
        return direction ?"ASC":"DESC";
    }
}
