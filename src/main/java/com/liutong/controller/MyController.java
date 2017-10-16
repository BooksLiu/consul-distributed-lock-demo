package com.liutong.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.liutong.consul.ConsulLock;
import com.liutong.consul.ConsulLockTest;
import com.liutong.model.TestModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @文件名称： MyController
 * @包路径： com.liutong.controller
 * @版权所有：
 * @类描述：
 * @创建人： liutong
 * @创建时间： 2017/09/26 - 15:55
 * @修改记录：
 */
@RestController
@RequestMapping
public class MyController implements ApplicationContextAware {

    ApplicationContext context;

    private ExecutorService pool = Executors.newFixedThreadPool(5);

    private TestModel model = new TestModel();

    @Value("${consul.host}")
    private String host;

    @Value("${consul.port}")
    private int port;

    @RequestMapping("index")
    public String index() {
        ConsulClient client = new ConsulClient(host, port);
        for (int i = 0; i < 100; i++) {
            ConsulLockTest test = new ConsulLockTest(new ConsulLock(client), model);
            pool.execute(test);
        }
        return "aa";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
