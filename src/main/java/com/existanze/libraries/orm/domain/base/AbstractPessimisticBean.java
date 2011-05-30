package com.existanze.libraries.orm.domain.base;

import com.existanze.libraries.orm.domain.PessimisticBean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 20:22
 */
@MappedSuperclass
public abstract class AbstractPessimisticBean implements PessimisticBean {


    @Column(name = "deleted")
    @Basic(optional = true)
    private Long deleted;


    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }

    public boolean hasBeenDeleted(){
        return this.deleted != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPessimisticBean)) return false;

        AbstractPessimisticBean that = (AbstractPessimisticBean) o;

        if (deleted != null ? !deleted.equals(that.deleted) : that.deleted != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return deleted != null ? deleted.hashCode() : 0;
    }
}

