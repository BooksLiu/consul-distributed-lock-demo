package com.liutong.consul;

import com.liutong.model.TestModel;

/**
 * @文件名称： ConsulLockTest
 * @包路径： com.liutong.consul
 * @版权所有：
 * @类描述：
 * @创建人： liutong
 * @创建时间： 2017/09/26 - 14:59
 * @修改记录：
 */
public class ConsulLockTest implements Runnable {

    private TestModel model;
    private ConsulLock lock;

    public ConsulLockTest(ConsulLock lock, TestModel model) {
        this.model = model;
        this.lock = lock;
    }

    @Override
    public void run() {
        long i = System.currentTimeMillis();
        lock.lock("myLock", "", true);
        model.setNum(model.getNum() + 1);
        System.out.println("num is " + model.getNum());
        lock.unlock("myLock");
        System.out.println("总共耗时 " + (System.currentTimeMillis() - i) + " 毫秒");
    }
}
