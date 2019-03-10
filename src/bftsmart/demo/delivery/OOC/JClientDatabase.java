package bftsmart.demo.delivery.OOC;

import bftsmart.demo.bftmap.BFTMap;
import bftsmart.demo.bftmap.BFTMapRequestType;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lijin on 3/4/19.
 */
public class JClientDatabase extends BFTMap {
    private String tableName = "db_jdeliver";
    private Map<String, byte[]> dataEntries = new HashMap<String, byte[]>();
    ByteArrayOutputStream out = null;
    public JClientDatabase(int id, String tableName){
        super(id);
        dataEntries.put(tableName, new byte[]{});
        this.tableName = tableName;
        this.put(tableName, dataEntries);
    }
    // commit
    // abort
    // write
    // read

    public List<String> getKeys(String tableName){
        try {
            out = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(BFTMapRequestType.GET);
            dos.writeUTF(tableName);

            byte[] rep = KVProxy.invokeUnordered(out.toByteArray());
            ByteArrayInputStream bis = new ByteArrayInputStream(rep) ;
            ObjectInputStream in = new ObjectInputStream(bis) ;
            Map<String,byte[]> table = (Map<String,byte[]>) in.readObject();
            in.close();
            List<String> ret = new ArrayList<String>();
            Iterator itr = table.keySet().iterator();
            while (itr.hasNext())
                ret.add(itr.next() + "");
            return ret;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BFTMap.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(BFTMap.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }



    public List<String> readAllKey(){
        return this.getKeys(tableName);
    }

    public byte[] write(String k, String v){
        byte[] originVal = this.getEntry(tableName, k);
        String val = originVal.toString() + v;
        return this.putEntry(tableName, k, val.getBytes());
    }

    public String read(String k){
        byte[] v = this.getEntry(tableName, k);
        return v.toString();
    }


}
