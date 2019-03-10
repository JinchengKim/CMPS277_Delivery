package bftsmart.demo.delivery.JOOC;

import bftsmart.demo.delivery.OOC.JOperation;

import java.util.*;

/**
 * Created by lijin on 3/9/19.
 */
class OccState{
    public static int READ_PHASE = 1;
    public static int VALID_AND_WRITE = 2;
}

public class JOCCExecutor {
    public JTranscationWrapper cache;
    public JOOCLayer occLyaer;
    public int startVersion;
    public Queue<JTranscationWrapper> backupQueue = new LinkedList<JTranscationWrapper>();
    public JOCCExecutor(JTranscationWrapper cache, JOOCLayer occLyaer){
        this.cache = cache;
        this.occLyaer = occLyaer;
        this.startVersion  = occLyaer.dbVersion;
    }

    public void readPhase(JOperation op){
        if (op.type == OperationType.READ) {
            this.cache.read(op.key);
        }else if (op.type == OperationType.WRITE){
            this.cache.write(op.key, op.val);
        }else if (op.type == OperationType.COMMIT){
            validate();
        }
    }


    public boolean validate(){ // call until meet commit operation.
        if (this.cache.get_write_set().size() == 0){ // read-only
            return true;
        }
        int finishVersion = occLyaer.dbVersion;
        boolean valid = true, succ = true;
        for (int tn = this.startVersion + 1; tn <= finishVersion; ++tn) {
            JTranscationWrapper other_caches = occLyaer.getCacheByTranscation(tn);
            Set<String> read_set  = this.cache.get_read_set();
            Set<String> write_set =  other_caches.get_write_set();
            for (String key : write_set){
                if (read_set.contains(key)) {
                    valid = false;
                    this.backupQueue.add(this.cache);
                    break;

                }
            }
        }
        if (valid){
            succ = replicateAndWritePhase();
            if (succ){
                ++occLyaer.dbVersion;
                occLyaer.commitAfterRreachConsensus(this.cache);
            }
        }
        if (!valid || !succ){
            backup();
        }
        return succ;
    }

    public boolean replicateAndWritePhase(){
        // todo
        return true;
    }

    private void backup(){
        // At this time, simply use abort to implement backup.
        while(this.backupQueue.peek() != null){
            JTranscationWrapper _cache = this.backupQueue.poll();
            _cache.abort();
        }
        System.out.println("Backup succeed.");
    }
}
