package bftsmart.demo.delivery.OOC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijin on 3/4/19.
 */
public class JTranscation {
    public List<JOperation> oprs = new ArrayList<JOperation>();
    public int id;
    JTranscation(List<JOperation> oprs, int id){
        this.id = id;
        for (JOperation opr:oprs){
            this.oprs.add(opr);
        }
    }
    JTranscation(JOperation[] oprs){
        for (JOperation opr:oprs){
            this.oprs.add(opr);
        }
    }
}
