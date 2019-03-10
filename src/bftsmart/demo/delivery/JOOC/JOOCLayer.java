package bftsmart.demo.delivery.JOOC;

import bftsmart.demo.delivery.OOC.JOperation;
import com.sun.codemodel.internal.JOp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijin on 3/9/19.
 */
public class JOOCLayer {
    public Map<Integer, JTranscationWrapper> wrapperMap = new HashMap<Integer, JTranscationWrapper>(); // key: tnc
    public Map<Integer, JOCCExecutor> executorMap = new HashMap<Integer, JOCCExecutor>(); // key: id
    public int dbVersion = 0;

    private JTestServer replica;
    JOOCLayer(JTestServer replica){
        this.replica = replica;
    }

    public void createOrUpdateExecutorByOpr(JOperation opr){
        if (!this.executorMap.containsKey(opr.tid)){
            JTranscationWrapper empty_wrapper = new JTranscationWrapper(replica.getCopyDatabase());
            JOCCExecutor executor = new JOCCExecutor(empty_wrapper, this);
            executorMap.put(opr.tid, executor);
        }
        JOCCExecutor executor = executorMap.get(opr.tid);
        executor.readPhase(opr);
    }


    public JTranscationWrapper getCacheByTranscation(int trans_id){
        assert(wrapperMap.containsKey(trans_id)); // todo: try
        return wrapperMap.get(trans_id);
    }

    public boolean commitAfterRreachConsensus(JTranscationWrapper cache){
        assert(!wrapperMap.containsKey(dbVersion)); // todo: try
        wrapperMap.put(dbVersion, cache);
        return cache.commit();
    }




    public void receiveFromNetwork(byte[] msg){
        // todo: parse msg from network
//        JOperation op = parse(msg);
//        addOperation(op);
    }

    // todo
    public void maintain(){

    }
}
