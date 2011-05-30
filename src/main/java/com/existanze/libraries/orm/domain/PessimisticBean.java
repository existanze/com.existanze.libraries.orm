package com.existanze.libraries.orm.domain;

/**
 *
 * When a domain is pessimistic then this means that it is removed from
 * the database using a "deleted" long column.
 *
 * Any class that joins with a PesssimisticBean should ignore these
 * beans in the joins.
 *
 * There is no "on delete restrict" database constraint in place.
 *
 *
 *
 * User: fotis
 * Date: 07/05/11
 * Time: 20:52
 */
public interface PessimisticBean extends SimpleBean {
    public void setDeleted(Long time);
    public boolean hasBeenDeleted();
}
