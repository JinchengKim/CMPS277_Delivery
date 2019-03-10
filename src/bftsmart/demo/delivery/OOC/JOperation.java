package bftsmart.demo.delivery.OOC;

import bftsmart.demo.bftmap.BFTMapRequestType;

import java.io.*;

/**
 * Created by lijin on 3/4/19.
 */

class JOperationEnum{
    static final int WRITE = 1;
    static final int READ = 2;
    static final int COMMIT = 3;
    static final int ABORT = 4;
}

public class JOperation {
    public int type;
    public String key;
    public String val;
    public int tid;

    public JOperation(int type, int tid, String k, String v){
        this.type = type;
        this.tid = tid;
        this.key = k;
        this.val = v;
    }
}
