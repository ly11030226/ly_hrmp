package com.hrmp.model;

import com.hrmp.bean.RspMsg;

/**
 * Created by Ly on 2017/5/17.
 */

public interface IThreadListener {
    public void success(RspMsg rspMsg);

    public void fail(RspMsg rspMsg);
}
