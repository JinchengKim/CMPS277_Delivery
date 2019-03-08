package bftsmart.demo.delivery.OOC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lijin on 3/4/19.
 */

public class JDatabaseManager {


    // db
    private JServerDatabase db = new JServerDatabase();

    // cache
    public Map<String, String> copies = new HashMap<String, String>();

    // read write set
    public Set<String> readSet = new HashSet<String>();


    // transcations
    public Map<Integer, JTranscation> transMap = new HashMap<Integer, JTranscation>();


    // TODO
    // pass a manager owner, then we can get manager info
    JDatabaseManager(){

    }

    public void handlerTranscation(JTranscation transcation){
        this.transMap.put(transcation.id, transcation);


    }




}
