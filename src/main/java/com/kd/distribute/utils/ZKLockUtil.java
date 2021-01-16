package com.kd.distribute.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author kdaddy@163.com
 * @date 2021/1/16 10:25
 * @公众号 程序员老猫
 */
@Slf4j
@Service
public class ZKLockUtil implements AutoCloseable, Watcher {
    private ZooKeeper zooKeeper;
    private String zNode;

    public ZKLockUtil() throws Exception {
        this.zooKeeper = new ZooKeeper("localhost:2181",100000,this);
    }

    public boolean getLock(String businessCode){
        try {
            // 首先创建业务根节点，类比之前的redis锁的key以及mysql锁的businessCode
            Stat stat = zooKeeper.exists("/"+businessCode,false);

            if(stat == null){
                //表示创建一个业务根目录，此节点为持久节点，另外的由于在本地搭建的zk没有设置密码，所以采用OPEN_ACL_UNSAFE模式
                zooKeeper.create("/" +businessCode,businessCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            //创建该目录下的有序瞬时节点，假如我们的订单业务编号是"order"，那么第一个有序瞬时节点应该是/order/order_0000001
            zNode =zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            /**
             * 按照之前原理的时候的逻辑，
             * 我们会对所有的节点进行排序并且序号最小的那个节点优先获取锁，
             * 其他节点处于监听状态
             */
            //此处获取所有子节点，注：之前文章中提及的getData()，getChildrean(),exists()的第二个参数表示是否设置观察器，ture为设置，false表示不设置
            List<String> childrenNodes = zooKeeper.getChildren("/"+businessCode,false);
            //子节点排序
            Collections.sort(childrenNodes);
            //获取序号最小的子节点
            String minNode = childrenNodes.get(0);

            //如果创建的节点是最小序号的节点，那么就获得锁
            if(zNode.endsWith(minNode)){
                return true;
            }

            //否则监听前一个节点的情况
            /**
             * 到这里说明创建的zNode为第二个或者第三第四个等节点
             * 此处比较晦涩用代入法去理解
             * 如果zNode是第二个节点，那么监听的就是第一个最小节点，
             * 如果zNode是第三个节点，那么此时上一个节点就是循环中的当前那个节点。
             * 需要细品
             */
            String lastNode = minNode;
            for (String node : childrenNodes){
                //如果瞬时节点为非第一个节点，那么监听前一个节点
                if(zNode.endsWith(node)){
                    zooKeeper.exists("/"+businessCode+"/"+lastNode,true);
                    break;
                }else {
                    lastNode = node;
                }
            }
            //并发情况下wait方法让出锁，但是由于并发情景下，为了避免释放的时候错乱因此加上synchronized
            synchronized (this){
                wait();
            }
            //当被唤起的时候相当于轮到了，当前拿到了锁，所以return true
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //如果监听到节点被删除，那么则会通知下一个线程
        if(watchedEvent.getType() == Event.EventType.NodeDeleted){
            synchronized (this){
                notify();
            }
        }
    }

    @Override
    public void close() throws Exception {
        zooKeeper.delete(zNode,-1);
        zooKeeper.close();
        log.info("我已经释放了锁！");
    }
}
