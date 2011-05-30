package com.existanze.libraries.orm.util;


import com.existanze.libraries.orm.dao.DefaultSortOrder;
import com.existanze.libraries.orm.domain.PessimisticBean;
import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * User: fotis
 * Date: 30/04/11
 * Time: 02:05
 */
public class JpaUtils {

    private static Logger logger = LoggerFactory.getLogger(JpaUtils.class);


    public static <T extends SimpleBean> int count(EntityManager em, Class<T> clazz){
        return JpaUtils.count(em, clazz, null);
    }

    public static <T extends SimpleBean> int count(EntityManager em, Class<T> clazz, Map<String,Object> filter){
        return JpaUtils.count(
                            em,
                            clazz,
                            "a",
                            "SELECT COUNT(a) FROM " + clazz.getName() +" a",
                            filter);
    }

    public static <T extends PessimisticBean> int countPessimistic(EntityManager em, Class<T> clazz){
        return JpaUtils.countPessimistic(em, clazz,null);
    }

    public static <T extends PessimisticBean> int countPessimistic(EntityManager em, Class<T> clazz,Map<String,Object> filter){
        return JpaUtils.count(
                            em,
                            clazz,
                            "s",
                            "SELECT COUNT(s) FROM "+clazz.getName()+" s WHERE s.deleted IS NULL ",
                            filter);

    }




    public static <T extends SimpleBean> int count(EntityManager em, Class<T> clazz,String alias, String q,Map<String,Object> filter){



        String s = JpaUtils.buildQuery(
                q,
                alias,
                filter);


        logger.trace("Created query ({})",s);
        Query query=em.createQuery(s);

        JpaUtils.setParameters(query,filter);

        Number countResult=(Number) query.getSingleResult();
        return countResult.intValue();
    }

    public static String buildQuery(String query, String alias, Map<String,Object> filter){

        if(filter == null){
            return query;
        }


        StringBuilder s = new StringBuilder(query);



        Iterator<String> iterator = filter.keySet().iterator();


        //check if WHERE
        if(!StringUtils.contains(query, "WHERE")){
            if(iterator.hasNext()){
                String next = iterator.next();
                s.append(" WHERE "+alias+"."+next+"=:"+StringUtils.replace(next,".","_"));
            }

        }

        while(iterator.hasNext()){
            String next = iterator.next();
            s.append(" AND "+alias+"."+next+"=:"+StringUtils.replace(next,".","_"));

        }

        return s.toString();
    }

    public static void setParameters(Query query, Map<String,Object> filter){

        if(filter == null){
            return;
        }

        Iterator<String> iterator = filter.keySet().iterator();

        while(iterator.hasNext()){
            String next = iterator.next();
            query.setParameter(StringUtils.replace(next,".","_"), filter.get(next));
        }
    }


    public static <T extends SimpleBean> List<?> findAll(EntityManager em, Class<T> clazz,DefaultSortOrder order){

        Query query = em.createQuery(
                "SELECT c FROM "+clazz.getName()+" c ORDER BY c."+order.getField()+" "+order.getDirection());


        List resultList = query.getResultList();

        return resultList;

    }

     public static <T extends SimpleBean> List<?> findAll(EntityManager em, Class<T> clazz,int firstRow, int limit,DefaultSortOrder order){


        Query query = em.createQuery(
                "SELECT c FROM "+clazz.getName()+" c ORDER BY c."+order.getField()+" "+order.getDirection());
        query.setFirstResult(firstRow);
        query.setMaxResults(limit);

        List resultList = query.getResultList();

        return resultList;

    }



    public static <T extends PessimisticBean> List<?> findPessimisticAll(EntityManager em, Class<T> clazz,DefaultSortOrder order){

        Query query = em.createQuery(
                "SELECT c FROM " + clazz.getName() + " c WHERE c.deleted IS NULL ORDER BY c."+order.getField()+" "+order.getDirection());

        List resultList = query.getResultList();

        return resultList;

    }

    public static <T extends PessimisticBean> List<?> findPessimisticAll(EntityManager em, Class<T> clazz, int firstRow, int limit, DefaultSortOrder order){

        Query query = em.createQuery(
                "SELECT c FROM " + clazz.getName() + " c WHERE c.deleted IS NULL ORDER BY c."+order.getField()+" "+order.getDirection());

        query.setFirstResult(firstRow);
        query.setMaxResults(limit);
        List resultList = query.getResultList();

        return resultList;

    }

    public static <T extends PessimisticBean> List<?> findDeleted(EntityManager em, Class<T> clazz){

        Query query = em.createQuery(
                "SELECT c FROM " + clazz.getName() + " c WHERE c.deleted IS NOT NULL ORDER BY c.id"
        );

        List resultList = query.getResultList();

        return resultList;

    }

    public static <T extends PessimisticBean> T findDeletedById(EntityManager em, Class<T> clazz,Integer id){

        Query query = em.createQuery(
                "SELECT c FROM "+clazz.getName()+" c WHERE c.deleted IS NOT NULL and c.id=:id"
        );
        query.setParameter("id",id);

        T ret =null;
        try {
            ret = (T)query.getSingleResult();

        }catch (NoResultException e) {
            logger.warn("Couldn't find {} by using id = {}", clazz, id);
        }

        return ret;
    }




    public static <T extends PessimisticBean> T findPessimisticById(EntityManager em, Class<T> clazz, Integer id){

        Query query = em.createQuery(
                String.format("SELECT a FROM "+clazz.getName()+" a WHERE a.id=:id AND a.deleted IS NULL",
                        clazz.getName())
        );

        query.setParameter("id",id);

        T ret= null;
        try {
            ret = (T) query.getSingleResult();

        } catch (NoResultException e) {
            logger.warn("Couldn't find {} by using id = {}",clazz,id);
        }

        return ret;
    }

    public static <T extends SimpleBean> T findById(EntityManager em, Class<T> clazz, Integer id){

        Query query = em.createQuery(
                String.format("SELECT a FROM "+clazz.getName()+" a WHERE a.id=:id",
                        clazz.getName())
        );

        query.setParameter("id",id);

        T ret= null;
        try {
            ret = (T) query.getSingleResult();

        } catch (NoResultException e) {
            logger.warn("Couldn't find {} by using id = {}",clazz,id);
        }

        return ret;
    }



    public static <T extends SimpleBean> void delete(EntityManager em, Class<T> clazz, Integer id){

        T byId = null;

        byId = JpaUtils.findById(em, clazz, id);

        if(byId == null){
            throw new NoSuchElementException(clazz+" with id = ("+id+") doesn't exist");
        }

        em.remove(byId);
    }

    public static <T extends PessimisticBean> void pessimisticDelete(EntityManager em, Class<T> clazz, Integer id){

        T byId = null;

        byId = JpaUtils.findPessimisticById(em, clazz, id);

        if(byId == null){
            throw new NoSuchElementException(clazz+" with id = ("+id+") doesn't exist");
        }

        /*
        * This method runs in a @Transaction dao method
        * that is how this gets updated
         */
        byId.setDeleted(System.currentTimeMillis());
    }

}
