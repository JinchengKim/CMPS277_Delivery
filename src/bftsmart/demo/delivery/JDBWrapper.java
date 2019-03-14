package bftsmart.demo.delivery;
import java.util.*;

class JDBWrapper {
    public Map<String, String> copies = new HashMap<>();
    public Set<String> read_set = new HashSet<>();
    public JServer JServer;
    public int trans_id;
    public JDBWrapper(JServer JServer, int trans_id){
        this.JServer = JServer;
        this.trans_id = trans_id;
    }

    @Override
    protected void finalize(){
        try{
            super.finalize();
        }catch (Throwable e){

        }
    }

    public String read(String key){ // todo: where to use read
        this.read_set.add(key);
        if (this.copies.containsKey(key)) {
            return this.copies.get(key);
        } else {
            return this.JServer.readFromUniqueDB(key);
        }
    }

    public boolean write(String key, String val){
        this.copies.put(key, val);
        return true;
    }

    public void abort(){
        this.copies.clear();
        this.read_set.clear();
//        System.out.println("Abort succeed.");
    }

    public boolean commit(){
        for (String key : this.copies.keySet())
            this.JServer.writeToUniqueDB(key,  this.copies.get(key));
        return true;
    }

    public List<JOperation> generateOps(){
        List<JOperation> ops = new ArrayList<>();
        for(String k:this.copies.keySet()){
            JOperation op = new JOperation(trans_id, JOperationType.WRITE, k, this.copies.get(k));
//            System.out.println("generateOps" + op.toString());
            ops.add(op);
        }
        return ops;
    }
    public Set<String> getReadSet() {
        return this.read_set;
    }

    public Set<String> getWriteSet(){
        return this.copies.keySet();
    }

}