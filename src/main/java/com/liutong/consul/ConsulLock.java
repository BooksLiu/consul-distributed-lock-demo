package com.liutong.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;

/**
 * @文件名称： ConsulLock
 * @包路径： com.liutong.consul
 * @版权所有：
 * @类描述：
 * @创建人： liutong
 * @创建时间： 2017/09/26 - 14:12
 * @修改记录：
 */
public class ConsulLock {

    private ConsulClient client;

    private String sessionId;


    public ConsulLock(ConsulClient client) {
        this.client = client;
    }

    public boolean lock(String lockName, String lockValue, boolean block) {
        if (sessionId != null) {
            throw new RuntimeException(sessionId + " - Already locked!");
        }
        sessionId = createSession(lockName);
        PutParams params = new PutParams();
        params.setAcquireSession(sessionId);
        while (true) {
            if (client.setKVValue(lockName, lockValue, params).getValue()) {
                return true;
            } else {
                if (block) {
                    continue;
                } else return false;
            }
        }
    }

    public void unlock(String lockName) {
        PutParams putParams = new PutParams();
        putParams.setReleaseSession(sessionId);
        client.setKVValue(lockName, "", putParams).getValue();
        client.sessionDestroy(sessionId, null);
    }

    private String createSession(String sessionName) {
        NewSession newSession = new NewSession();
        newSession.setName(sessionName);
        return client.sessionCreate(newSession, null).getValue();
    }
}
