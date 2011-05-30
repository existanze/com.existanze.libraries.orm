package com.existanze.libraries.orm.dao;

import com.existanze.libraries.orm.domain.OrderedBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 23:37
 */
public abstract class JpaOrderedCommonDao<T extends OrderedBean> extends JpaCommonDao<T> implements OrderedCommonDao<T> {


    protected JpaOrderedCommonDao(Class<T> clazz) {
        super(clazz);
    }


    @Transactional
    @Override
    public List<T> findAll() {

        Query query = getEntityManager().createQuery("SELECT a FROM " + getClazz().getName() + " a ORDER BY a.position");
        List resultList = query.getResultList();

        return resultList;

    }


    @Transactional
    @Override
    public T insert(T bean) {


        //we need to make sure it is being added to the end
        int count = count();
        bean.setPosition(count+1);
        super.insert(bean);

        return bean;
    }




    @Transactional
    @Override
    public T moveUp(T bean) {
        //if the position is 0 then there is no point
        if(bean.getPosition() == 0 ){
            return bean;
        }

        return switchPosition(bean,-1);

    }

    @Transactional
    @Override
    public T moveDown(T bean) {
        //if the position is 0 then there is no point
        if(bean.getPosition() == count()-1 ){
            return bean;
        }

        return switchPosition(bean,+1);

    }


    private T switchPosition(T bean, int direction){

        //this is simple, just switch it with the one above
        Query query = getEntityManager().createQuery("SELECT b FROM " + getClazz().getName() + " b WHERE b.position=:pos");
        query.setParameter("pos",Integer.valueOf(bean.getPosition()+(direction)));


        T above = (T)query.getSingleResult();

        above.setPosition(bean.getPosition());
        bean.setPosition(bean.getPosition()+(direction));

        return bean;
    }
}
