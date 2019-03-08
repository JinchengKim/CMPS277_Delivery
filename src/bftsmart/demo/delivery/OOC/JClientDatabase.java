package bftsmart.demo.delivery.OOC;

import bftsmart.demo.bftmap.BFTMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijin on 3/4/19.
 */
public class JClientDatabase extends BFTMap {
    private String tableName = "db_jdeliver";
    private Map<String, byte[]> dataEntries = new HashMap<String, byte[]>();
    public JClientDatabase(int id){
        super(id);
        dataEntries.put(tableName, new byte[]{});
//        this.addTable(tableName, dataEntries);
//        this.
//        this.put()
    }

//    public void write(String k, String v){
//        this.addData(tableName, k, v.getBytes());
//    }

    public String read(String k){
        byte[] v = this.getEntry(tableName, k);
        return v.toString();
    }
}
