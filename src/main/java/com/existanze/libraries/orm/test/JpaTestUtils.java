package com.existanze.libraries.orm.test;

import com.existanze.libraries.orm.dao.CommonDao;
import com.existanze.libraries.orm.dao.CommonPessimisticDao;
import com.existanze.libraries.orm.domain.PessimisticBean;
import com.existanze.libraries.orm.domain.SimpleBean;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

/**
 * User: fotis
 * Date: 08/05/11
 * Time: 22:07
 */
public class JpaTestUtils {

    private static Logger logger = LoggerFactory.getLogger(JpaTestUtils.class);


    public static <T extends SimpleBean> void assertNoFindById(CommonDao<T> dao, int id){
        T byId = dao.findById(id);
        Assert.assertNull(byId);
    }


    public static <T extends SimpleBean> void assertDelete(CommonDao<T> dao, Integer id, int expectedSize){

        T byId = dao.findById(id);
        dao.delete(byId);
        JpaTestUtils.assertSize(dao.findAll(), expectedSize);

    }

    public static <T extends SimpleBean> void assertDeleteById(CommonDao<T> dao, Integer id, int expectedSize){

        dao.delete(id);
        JpaTestUtils.assertSize(dao.findAll(), expectedSize);

    }


    public static <T extends SimpleBean> void assertDeleteRestrict(CommonDao<T> dao, Integer id,int expectedSize){

        T byId = dao.findById(id);

        boolean ex = false;

        try {
            dao.delete(byId);
            Assert.fail();
        } catch (DataIntegrityViolationException e) {
            ex =true;
        }

        Assert.assertTrue(ex);
        JpaTestUtils.assertSize(dao.findAll(), expectedSize);
    }


    public static <T extends SimpleBean> void assertDeleteByIdRestrict(CommonDao<T> dao, Integer id,int expectedSize){

        boolean ex = false;

        try {
            dao.delete(id);
            Assert.fail();
        } catch (DataIntegrityViolationException e) {
            ex =true;
        }

        Assert.assertTrue(ex);
        JpaTestUtils.assertSize(dao.findAll(), expectedSize);
    }


    /**
     * This method abstracts the testing of a pessimistic delete
     *
     * @param dao A  to use for doing CRUD operations
     * @param deleteId The id of the item that will be deleted
     * @param index The index in the @{link CommonPessimisticDao#findAll()} array
     * @param originalSize The original size of the data in the database
     * @param fullSize The size of all the items in the database including the deleted items
     * @param order The order of the keys as they should be returned
     * @param <T>
     */
    public static <T extends PessimisticBean> void assertPessimisticDelete(CommonPessimisticDao<T> dao, Integer deleteId, int index,int originalSize, int fullSize,Integer[] order){


        List<T> originalAll = dao.findAll();
        logger.trace("void assertPessimisticDelete(order) Original {} ",originalAll);
        JpaTestUtils.assertSize(originalAll, originalSize);


        JpaTestUtils.assertSize(dao.findAllIncludeDeleted(), fullSize);


        T bean = originalAll.get(index);
        dao.delete(bean);

        List<T> all = dao.findAll();
        logger.trace("void assertPessimisticDelete(order) All after delete {}",all);
        JpaTestUtils.assertSize(all, originalSize - 1);


        List<T> allIncludeDeleted = dao.findAllIncludeDeleted();

        logger.trace("void assertPessimisticDelete(order) All include deleted");
        JpaTestUtils.assertSize(allIncludeDeleted, fullSize);

        logger.trace("void assertPessimisticDelete(order) Checking all agains {}",order);
        JpaTestUtils.assertIdOrder(all, order);


        //make sure that the deleted property is on the bean
        for(T t : allIncludeDeleted ){

            if(t.getId().equals(deleteId)){
                Assert.assertTrue(t.hasBeenDeleted());
                break;
            }
        }

    }

    /**
     * This method abstracts the testing of a pessimistic delete
     *
     * @param dao A  to use for doing CRUD operations
     * @param deleteId The id of the item that will be deleted
     * @param originalSize The original size of the data in the database
     * @param fullSize The size of all the items in the database including the deleted items
     * @param order The order of the keys as they should be returned
     * @param <T>
     */
    public static <T extends PessimisticBean> void assertPessimisticDeleteById(CommonPessimisticDao<T> dao, Integer deleteId, int originalSize, int fullSize,Integer[] order){


        List<T> originalAll = dao.findAll();
        JpaTestUtils.assertSize(originalAll, originalSize);


        JpaTestUtils.assertSize(dao.findAllIncludeDeleted(), fullSize);

        dao.delete(deleteId);

        List<T> all = dao.findAll();
        JpaTestUtils.assertSize(all, originalSize - 1);


        List<T> allIncludeDeleted = dao.findAllIncludeDeleted();
        JpaTestUtils.assertSize(allIncludeDeleted, fullSize);
        JpaTestUtils.assertIdOrder(all, order);


        //make sure that the deleted property is on the bean
        for(T t : allIncludeDeleted ){

            if(t.getId().equals(deleteId)){
                Assert.assertTrue(t.hasBeenDeleted());
                break;
            }
        }

    }


    public static <T extends PessimisticBean> void assertPessimisticDeleteDeleted(CommonPessimisticDao<T> dao,Integer id){


        boolean ex = true;
        try {
            dao.delete(id);
            Assert.fail();

        } catch (Exception e) {
            ex = true;

        }
        Assert.assertTrue(ex);


    }

    public static <T extends PessimisticBean> void assertPessimisticUpdateDeleted(CommonPessimisticDao<T> dao, Integer id, int originalSize){

        T byId = dao.findDeletedById(id);
        byId.setDeleted(null);

        JpaTestUtils.assertSize(dao.findAll(), originalSize);

        dao.update(byId);

        List<T> all = dao.findAll();
        JpaTestUtils.assertSize(all, originalSize+1);

        //make sure the bean is not deleted anymore
        for(T t : all){
            if(t.getId().equals(id)){
                Assert.assertFalse(t.hasBeenDeleted());
            }
        }


        T byId1 = dao.findById(id);
        Assert.assertNotNull(byId1);


    }


    /**
     * The following asserts that a unique data contraint in the database is
     * kept
     *
     * @param dao The CommonDao<T> to user
     * @param uniqueBean A bean which contains the unique values you wish to test for
     * @param newBean A bean to be inserted afterwards, to make sure that the sequence is working
     * @param nextId The Id that should be generated
     * @param size The size of the list after the update has occured with the second bean
     * @param <T>
     */
    public static <T extends SimpleBean> void assertInsertUnique(CommonDao<T> dao, T uniqueBean, T newBean,Integer nextId, int size){

        T insert = null;

        boolean ex =false;
        try {
            insert = dao.insert(uniqueBean);
            Assert.fail();
        } catch (DataIntegrityViolationException e) {
            logger.debug("Expected exception here");
            ex=true;
        }

        Assert.assertTrue(ex);
        Assert.assertNull(insert);
        Assert.assertEquals(uniqueBean.getId(),Integer.valueOf(nextId));

        /**
         * We need to test that the id is increased when
         * something fails because we are using table based
         * id generation, which creates the id before persisting
         * the object, so that ID is lost.
         *
         */
        T insert1 = dao.insert(newBean);
        Assert.assertEquals(Integer.valueOf(nextId + 1), insert1.getId());


        //assume that this works because it is being tested above
        List<T> all = dao.findAll();
        Assert.assertEquals(size+1 , all.size());

    }

    public static <T extends SimpleBean> void assertUpdateUnique(CommonDao<T> dao,T updateBean){


        T insert = null;
        boolean ex =false;
        try {

            insert = dao.update(updateBean);
            Assert.fail();

        } catch (DataIntegrityViolationException e) {
            logger.debug("Expected exception here");
            ex=true;
        }

        Assert.assertTrue(ex);
        Assert.assertNull(insert);

    }

    public static <T> void assertSize(List<T> all, int size){
        Assert.assertNotNull(all);
        Assert.assertEquals(size,all.size());

    }

    public static <T extends SimpleBean> void assertIdOrder(List<T> all, Integer[] order){

        logger.trace("void assertIdOrder(order) of {} to {} ",all,order);
        Assert.assertEquals(all.size(),order.length);

        for(int i=0; i < all.size(); i++){
            Assert.assertEquals(all.get(i).getId(),order[i]);
        }

    }



}
