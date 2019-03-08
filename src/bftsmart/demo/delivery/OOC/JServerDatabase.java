package bftsmart.demo.delivery.OOC;

import bftsmart.demo.bftmap.MapOfMaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lijin on 3/4/19.
 */
public class JServerDatabase extends MapOfMaps {
    private String tableName = "db_jdeliver";
    private Map<String, byte[]> dataEntries = new HashMap<String, byte[]>();
    public Map<Integer, JCacheDatabase> transcations = new HashMap<Integer, JCacheDatabase>();
    public int version = 0;


    public JServerDatabase(){
        dataEntries.put(tableName, new byte[]{});
        this.addTable(tableName, dataEntries);
    }

    public void write(String k, String v){
        this.addData(tableName, k, v.getBytes());
    }

    public String read(String k){
        byte[] v = this.getEntry(tableName, k);
        return v.toString();
    }

    public JCacheDatabase getTranscation(int tn){
        if (transcations.containsKey(tn)){
            return transcations.get(tn);
        }else {
            System.out.println("getTranscation error");
            return null;
        }
    }

    public void commitTranscation(JCacheDatabase db){
        this.version += 1;
        if (transcations.containsKey(this.version)){
            System.out.println("commitTranscation error");
            return;
        }

        this.transcations.put(this.version, db);
        db.commit();
    }

    public JTransactionExecutor begin(JTranscation txn){
        return new JTransactionExecutor(this, txn);
    }
}


class JCacheDatabase{
    public Map<String, String> copies = new HashMap<String, String>();
    public Set<String> readSet = new HashSet<String>();
    public JServerDatabase db = new JServerDatabase();

    JCacheDatabase(JServerDatabase db) {
        this.db = db;
    }

    public void write(String k, String v){
        copies.put(k, v);
    }

    public String read(String k){
        this.readSet.add(k);
        if (copies.containsKey(k)){
            return copies.get(k);
        }

        return db.read(k);
    }

    public void commit(){
        for(String k:copies.keySet()){
            db.write(k, copies.get(k));
        }
    }

    public void abort(){
        copies = new HashMap<String, String>();
    }

    public Set<String> getWriteSet(){
        return this.copies.keySet();
    }

    public Set<String> getReadSet(){
        return this.readSet;
    }

}