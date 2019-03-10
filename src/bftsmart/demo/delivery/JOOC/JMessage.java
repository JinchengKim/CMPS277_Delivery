package bftsmart.demo.delivery.JOOC;

import bftsmart.demo.delivery.OOC.JOperation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijin on 3/10/19.
 */
class JMessageType{
    static int OPR = 1;
    static int OPRS = 2;
}
public class JMessage {
    public static JOperation getOprFromStream(ByteArrayInputStream in) throws IOException {
        int type = new DataInputStream(in).readInt();
        int tid = new DataInputStream(in).readInt();

        String key = new DataInputStream(in).readUTF();
        String val = new DataInputStream(in).readUTF();
        return new JOperation(type, tid, key, val);
    }

    public static ByteArrayOutputStream generateOpr2Stream(JOperation opr) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(JMessageType.OPR);
        dos.writeInt(opr.type);
        dos.writeInt(opr.tid);
        dos.writeUTF(opr.key);
        dos.writeUTF(opr.val);
        return out;
    }


    public static List<JOperation> getOprsFromStream(ByteArrayInputStream in) throws IOException {

        List<JOperation> oprs = new ArrayList<JOperation>();
        DataInputStream input = new DataInputStream(in);
        int size = new DataInputStream(in).readInt();
        for (int i = 0 ; i < size; i++){
            int type = new DataInputStream(in).readInt();
            int tid = new DataInputStream(in).readInt();
            String key = new DataInputStream(in).readUTF();
            String val = new DataInputStream(in).readUTF();
            oprs.add(new JOperation(type, tid, key, val));
        }
        return oprs;
    }

    public static ByteArrayOutputStream generateOprs2Stream(List<JOperation> oprs) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(JMessageType.OPRS);
        dos.writeInt(oprs.size());
        for (JOperation opr:oprs){
            dos.writeInt(opr.type);
            dos.writeInt(opr.tid);
            dos.writeUTF(opr.key);
            dos.writeUTF(opr.val);
        }
        return out;
    }
}
