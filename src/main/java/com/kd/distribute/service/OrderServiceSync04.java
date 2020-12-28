package com.kd.distribute.service;

import com.kd.distribute.dao.KdOrderItemMapper;
import com.kd.distribute.dao.KdOrderMapper;
import com.kd.distribute.dao.KdProductMapper;
import com.kd.distribute.model.KdOrder;
import com.kd.distribute.model.KdOrderItem;
import com.kd.distribute.model.KdProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kdaddy@163.com
 * @date 2020/12/27 22:49
 * 基于Lock锁的操作
 */
@Service
@Slf4j
public class OrderServiceSync04 {

    @Resource
    private KdOrderMapper orderMapper;
    @Resource
    private KdOrderItemMapper orderItemMapper;
    @Resource
    private KdProductMapper productMapper;
    //购买商品id
    private int purchaseProductId = 100100;
    //购买商品数量
    private int purchaseProductNum = 1;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    private Lock lock = new ReentrantLock();

    public  Integer createOrder() throws Exception{
        try{
            lock.lock();
            TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
            KdProduct product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product==null){
                platformTransactionManager.rollback(transaction);
                throw new Exception("购买商品："+purchaseProductId+"不存在");
            }

            //商品当前库存
            Integer currentCount = product.getCount();
            log.info(Thread.currentThread().getName()+"库存数"+currentCount);
            //校验库存
            if (purchaseProductNum > currentCount){
                platformTransactionManager.rollback(transaction);
                throw new Exception("商品"+purchaseProductId+"仅剩"+currentCount+"件，无法购买");
            }

            //在数据库中完成减量操作
            productMapper.updateProductCount(purchaseProductNum,"kd",new Date(),product.getId());
            //生成订单
            KdOrder order = new KdOrder();
            order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
            order.setOrderStatus(1);//待处理
            order.setReceiverName("kdaddy");
            order.setReceiverMobile("13311112222");
            order.setTimeCreated(new Date());
            order.setTimeModified(new Date());
            order.setCreateUser("kdaddy");
            order.setUpdateUser("kdaddy");
            orderMapper.insertSelective(order);

            KdOrderItem orderItem = new KdOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setPurchasePrice(product.getPrice());
            orderItem.setPurchaseNum(purchaseProductNum);
            orderItem.setCreateUser("kdaddy");
            orderItem.setTimeCreated(new Date());
            orderItem.setTimeModified(new Date());
            orderItem.setUpdateUser("kdaddy");
            orderItemMapper.insertSelective(orderItem);
            platformTransactionManager.commit(transaction);
            return order.getId();
        }finally {
            lock.unlock();
        }
    }
}
