package bftsmart.demo.delivery;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class JMessageType {
    public static int SINGLE_OP = 1;
    public static int MULTI_OPS = 2;
    public static int WRITE_SET = 3;
}

public class JMessage {

    public static JOperation parseOpFromStream(ByteArrayInputStream in) throws IOException {
        int id = new DataInputStream(in).readInt();
        int type = new DataInputStream(in).readInt();
        String key = new DataInputStream(in).readUTF();
        String val = new DataInputStream(in).readUTF();
        return new JOperation(id, type, key, val);
    }

    public static ByteArrayOutputStream assembleOpIntoStream(JOperation op) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(JMessageType.SINGLE_OP);
        dos.writeInt(op.trans_id);
        dos.writeInt(op.type.type);
        dos.writeUTF(op.key);
        dos.writeUTF(op.val);
        return out;
    }

    public static List<JOperation> parseOpsFromStream(ByteArrayInputStream in) throws IOException {
        List<JOperation> ops = new ArrayList<>();
        DataInputStream input = new DataInputStream(in);
        int size = new DataInputStream(in).readInt();
        for (int i = 0; i < size; ++i){
            int id = new DataInputStream(in).readInt();
            int type = new DataInputStream(in).readInt();
            String key = new DataInputStream(in).readUTF();
            String val = new DataInputStream(in).readUTF();
            ops.add(new JOperation(id, type, key, val));
        }
        return ops;
    }

    public static ByteArrayOutputStream assembleOpsIntoStream(List<JOperation> ops)throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(JMessageType.MULTI_OPS);
        dos.writeInt(ops.size());
        for (JOperation op : ops){
            dos.writeInt(op.trans_id);
            dos.writeInt(op.type.type);
            dos.writeUTF(op.key);
            dos.writeUTF(op.val);
        }
        return out;
    }

}
