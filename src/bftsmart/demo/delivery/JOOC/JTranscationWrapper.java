package bftsmart.demo.delivery.JOOC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lijin on 3/9/19.
 */
public class JTranscationWrapper {

    public Map<String, byte[]> copies;
    public Set<String> read_set = new HashSet<String>();

    JTranscationWrapper(Map<String, byte[]> data){
        copies = data;
    }

    public byte[] read(String key){ // todo: where to use read
        this.read_set.add(key);
        if (this.copies.containsKey(key)) {
            return this.copies.get(key);
        } else {
            // todo
        }
        return new byte[0];
    }

    public boolean write(String key, byte[] val){
        this.copies.put(key, val);
        return true;
    }

    public void abort(){
        this.copies.clear();
        this.read_set.clear();
        System.out.println("Abort succeed.");
    }

    public boolean commit(){
//        for (String key : this.copies.keySet())

        return false;
    }

    public Set<String> get_read_set() {
        return this.read_set;
    }

    public Set<String> get_write_set(){
        return this.copies.keySet();
    }
}
