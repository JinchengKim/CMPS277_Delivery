package bftsmart.demo.delivery;
import java.util.*;

class JOCCState {
    public static int READ_PHASE = 1;
    public static int VALID_AND_WRITE = 2;
}

public class JOCCExecutor {
    public JDBWrapper cache;
    public JOCCLayer joccLayer;
    public int startTransNumber;
    public Queue<JDBWrapper> backup_queue = new LinkedList<>();

    public JOCCExecutor(JDBWrapper cache, JOCCLayer joccLayer){
        this.cache = cache;
        this.joccLayer = joccLayer;
        startTransNumber = joccLayer.getTnc();
    }

    public synchronized boolean validate() throws Exception{ // call until meet commit operation.
        if (this.cache.getWriteSet().size() == 0){ // read-only
            return true;
        }
        int finish_trans_number = joccLayer.getTnc();
        boolean valid = true, succ = true;
        for (int tn = this.startTransNumber + 1; tn <= finish_trans_number; ++tn) {
            JDBWrapper other_caches = joccLayer.getCacheByTn(tn);
            Set<String> read_set  = this.cache.getReadSet();
            Set<String> write_set =  other_caches.getWriteSet();
            for (String key : write_set){
                if (read_set.contains(key)) {
                    valid = false;
                    this.backup_queue.add(this.cache);
                    break;

                }
            }
        }
        if (valid){
//            System.out.println("###################################");
//            System.out.println("Replica " + this.joccLayer.replica.rid + " send commit to other Replicas");
            List<JOperation> ops = this.cache.generateOps();
            byte[] reply = joccLayer.replica.KVProxy.invokeOrdered(JMessage.assembleOpsIntoStream(ops).toByteArray());
            succ = reply != null;
            if (succ){
                joccLayer.commitAfterValidate(this.cache);
                return true;
            }
        }else{
            backup();
        }

        //System.out.println(valid + " " + succ + " ");
        return false;
    }

    private void backup(){
        System.out.println("Backup succeed.");
    }
}
