package bftsmart.demo.delivery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JTranscation {
    public int tid;
    public List<JOperation> ops = new ArrayList<>();
    public JTranscation(){

    }
    public JTranscation(JOperation op){
        this.tid = op.trans_id;
        this.ops.add(op);
    }

    public JTranscation(List<JOperation> ops){
        this.tid = ops.get(0).trans_id;
        this.ops = ops;
    }

    public void addTransaction(JOperation op){
        this.ops.add(op);
    }

    public void addTransactions(List<JOperation> ops){
        for (JOperation op : ops){
            this.ops.add(op);
        }
    }

    public static JTranscation createSearchTranscation(int id, String name){
        JTranscation t = new JTranscation();
        t.tid = id;
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.COMMIT, "", ""));
        return t;
    }

    public static JTranscation createPickUpTranscation(int id, String name, String order){
        JTranscation t = new JTranscation();
        t.tid = id;
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.WRITE, name, order));
        t.addTransaction(new JOperation(t.tid, JOperationType.COMMIT, "", ""));
        return t;
    }

    public static JTranscation createOrderTranscation(int id, String name, String order){
        JTranscation t = new JTranscation();

        t.tid = id;
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.WRITE, name, order));
        t.addTransaction(new JOperation(t.tid, JOperationType.COMMIT, "", ""));
        return t;
    }


    public static JTranscation create5Transcation(int id, String name, String order){
        JTranscation t = new JTranscation();

        t.tid = id;
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.READ, name, ""));
        t.addTransaction(new JOperation(t.tid, JOperationType.WRITE, name, order));
        t.addTransaction(new JOperation(t.tid, JOperationType.WRITE, name, order));
        t.addTransaction(new JOperation(t.tid, JOperationType.COMMIT, "", ""));
        return t;
    }
}
