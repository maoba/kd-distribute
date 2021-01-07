package com.kd.distribute.service;


import com.kd.distribute.dao.DistributeLockMapper;
import com.kd.distribute.dao.KdOrderItemMapper;
import com.kd.distribute.dao.KdOrderMapper;
import com.kd.distribute.dao.KdProductMapper;
import com.kd.distribute.model.DistributeLock;
import com.kd.distribute.model.KdOrder;
import com.kd.distribute.model.KdOrderItem;
import com.kd.distribute.model.KdProduct;
import com.kd.distribute.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author kdaddy@163.com
 * @date 2021/1/7 22:03
 */
@Service
@Slf4j
public class RedisLockService {
    @Resource
    private KdOrderMapper orderMapper;
    @Resource
    private KdOrderItemMapper orderItemMapper;
    @Resource
    private KdProductMapper productMapper;
    @Resource
    private RedisLockUtil redisLockUtil;

    //购买商品id
    private int purchaseProductId = 100100;
    //购买商品数量
    private int purchaseProductNum = 1;

    private String ORDER_KEY = "order_key";

    public  Integer createOrder() throws Exception{
        log.info("进入了方法");
        boolean lock = redisLockUtil.lock(ORDER_KEY);
        if(lock) {
            try {
                log.info("拿到了锁");
                //此处为了手动演示并发，所以我们暂时在这里休眠1分钟
                Thread.sleep(6000);
                KdProduct product = productMapper.selectByPrimaryKey(purchaseProductId);
                if (product==null){
                    throw new Exception("购买商品："+purchaseProductId+"不存在");
                }

                //商品当前库存
                Integer currentCount = product.getCount();
                log.info(Thread.currentThread().getName()+"库存数"+currentCount);
                //校验库存
                if (purchaseProductNum > currentCount){
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
                return order.getId();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                redisLockUtil.releaseLock(ORDER_KEY);
            }
        };
        return null;
    }
}
