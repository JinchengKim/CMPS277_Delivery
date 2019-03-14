package bftsmart.demo.delivery;

enum JOperationType {
    READ(1),WRITE(2),COMMIT(3),ABORT(4), CONSEN(5);
    int type;
    JOperationType(int type){this.type = type;}
}

public class JOperation {
    public JOperation(int trans_id, JOperationType type, String key, String val){
        this.type = type;
        this.key = key;
        this.val = val;
        this.trans_id = trans_id;
    }
    public JOperation(int trans_id, int type, String key, String val){
        if (type == 1){
            this.type = JOperationType.READ;
        }else if (type == 2){
            this.type = JOperationType.WRITE;
        }else if (type == 3){
            this.type = JOperationType.COMMIT;
        }else if (type == 4){
            this.type = JOperationType.ABORT;
        }else if (type == 5){
            this.type = JOperationType.CONSEN;
        }else{
            System.out.println("JOperation" + " Error");
        }

        this.key = key;
        this.val = val;
        this.trans_id = trans_id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tranid:").append(trans_id).append(";");
        sb.append("type:").append(type).append(";");
        sb.append("key:").append(key).append(";");
        sb.append("val:").append(val).append(";");
        return sb.toString();
    }

    public int trans_id;
    public JOperationType type;
    public String key, val;
}


