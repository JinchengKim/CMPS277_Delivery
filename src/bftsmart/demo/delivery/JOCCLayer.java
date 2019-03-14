package bftsmart.demo.delivery;

import java.util.HashMap;
import java.util.Map;

public class JOCCLayer {

    public JServer replica;
    public  Map<Integer, JDBWrapper> wrapper_trans_map = new HashMap<>(); // key: tnc
    public  Map<Integer, JOCCExecutor> executor_trans_map = new HashMap<>(); // key: id
    public  int tnc = 0;

    public JOCCLayer(int id, JServer replica){

        this.replica = replica;
    }

    public String createOrUpdateExecutorByReadOp(JOperation op){
        if (!this.executor_trans_map.containsKey(op.trans_id)){ // already exists
            JDBWrapper empty_wrapper = new JDBWrapper(replica, op.trans_id);
            JOCCExecutor executor = new JOCCExecutor(empty_wrapper, this);
            executor_trans_map.put(op.trans_id, executor);
        }
        JOCCExecutor exist_executor = executor_trans_map.get(op.trans_id);
        return exist_executor.cache.read(op.key);
    }

    public void createOrUpdateExecutorByWriteOp(JOperation op){
        if (!this.executor_trans_map.containsKey(op.trans_id)){ // already exists
            JDBWrapper empty_wrapper = new JDBWrapper(replica, op.trans_id);
            JOCCExecutor executor = new JOCCExecutor(empty_wrapper, this);
            executor_trans_map.put(op.trans_id, executor);
        }
        JOCCExecutor exist_executor = executor_trans_map.get(op.trans_id);
        exist_executor.cache.write(op.key, op.val);
    }

    public boolean createOrUpdateExecutorByCommitOp(JOperation op){
        if (!this.executor_trans_map.containsKey(op.trans_id)){ // already exists
            JDBWrapper empty_wrapper = new JDBWrapper(replica, op.trans_id);
            JOCCExecutor executor = new JOCCExecutor(empty_wrapper, this);
            executor_trans_map.put(op.trans_id, executor);
        }
        JOCCExecutor exist_executor = executor_trans_map.get(op.trans_id);
        try {
            return exist_executor.validate();
        }catch (Exception e){
            //System.out.println(e);
            return false;
        }
    }

    public JOCCExecutor getExecutorByTn(int id){
        assert(executor_trans_map.containsKey(id)); // todo: try
        return executor_trans_map.get(id);
    }

    public JDBWrapper getCacheByTn(int trans_id){
        assert(wrapper_trans_map.containsKey(trans_id)); // todo: try
        return wrapper_trans_map.get(trans_id);
    }

    public boolean commitAfterValidate(JDBWrapper cache) {
        //assert(!wrapper_trans_map.containsKey(committed_trans_num)); // todo: try
        wrapper_trans_map.put(tnc, cache);
//        System.out.println("IN commit_after_reach_consensus()");
        if(cache.commit()){
//            System.out.println("Commit successed.");
            increaseInc();
            return true;
        }
//        System.out.println("-------Error: Can not commit.");
        return false;
    }

    public int getTnc(){
        return this.tnc;
    }

    public void increaseInc(){
        this.tnc++;
    }
}
