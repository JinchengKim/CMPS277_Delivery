package bftsmart.demo.delivery.OOC;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Set;

/**
 * Created by lijin on 3/4/19.
 */
public class JTransactionExecutor {

    public JServerDatabase db;
    public JCacheDatabase cacheDB;
    public JTranscation trans;
    public int startVersion = 0;
    public JTransactionExecutor(JServerDatabase db, JTranscation trans){
        this.trans = trans;
        this.startVersion = this.db.version;
        this.cacheDB = new JCacheDatabase(db);
    }

    public void readPhase(){
        for (JOperation opr : this.trans.oprs){
            if (opr.type == JOperationEnum.READ)
                this.cacheDB.read(opr.key);
        }
    }

    public boolean validateWrite() {

        int finishVersion = this.db.version;
        for (int v = this.startVersion + 1; v < finishVersion + 1; ++v){
            JCacheDatabase tempCacheDB = this.db.getTranscation(v);
            Set<String> writeSet = tempCacheDB.getWriteSet();
            Set<String> readSet = this.cacheDB.getReadSet();
            for(String k: writeSet){
                if (readSet.contains(k)) return false;
            }
        }
        this.db.commitTranscation(this.cacheDB);
        return true;
    }


}
