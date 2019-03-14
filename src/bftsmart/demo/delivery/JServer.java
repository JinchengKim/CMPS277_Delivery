package bftsmart.demo.delivery;

import bftsmart.demo.bftmap.BFTMapServer;
import bftsmart.demo.bftmap.MapOfMaps;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JServer extends DefaultSingleRecoverable {

    public MapOfMaps DB;
    public String tableName = "Master Lee";
    public JOCCLayer occLayer;
    public ServiceProxy KVProxy;
    public int rid;
    public boolean isMalicious = false;

    //ServiceReplica replica = null;
    //private ReplicaContext replicaContext;

    //The constructor passes the id of the server to the super class
    public JServer(int id) {
        init(id);
    }


    public JServer(int id, boolean isClientSide){
        init(id);
        if (isClientSide) KVProxy = new ServiceProxy(id, "config");
    }

    private void init(int id){
        DB = new MapOfMaps();
        DB.addTable(tableName, new HashMap<String, byte[]>());
        occLayer = new JOCCLayer(id, this);
        new ServiceReplica(id, this, this);
        for (int i = 0 ; i <= 100; i++){
            String k = i + "";
            DB.addData(tableName, k, k.getBytes());
        }
        rid = id;
    }

    public static void main(String[] args){
        if(args.length < 1) {
            System.out.println("Use: java BFTMapServer <processId>");
            System.exit(-1);
        }

        JServer s = new JServer(Integer.parseInt(args[0]));
        if (args.length >= 2)
            s.isMalicious = true;
    }

    public String readFromUniqueDB(String k){
        byte[] reply = this.DB.getEntry(tableName, k);
        if (reply == null) return null;
        return new String(reply);
    }

    public void writeToUniqueDB(String k, String v){
        this.DB.addData(tableName,k, v.getBytes());
    }

    public Map<String, String> getDBCopy(){
        Map<String, String> map = new HashMap<>();
        Map<String, byte[]> localMap = this.DB.getTable(tableName);
        for(String k:map.keySet()){
            map.put(k,new String(localMap.get(k)));
        }

        return map;
    }


    /*
        open interface for client
     */
    public byte[] recieveMessage(JOperation op) throws InterruptedException {
        // can't recieve msg here
        //System.out.println(KVProxy);

        if (KVProxy == null) return null;

        byte[] reply = null;
//        System.out.println("###################################");
//        System.out.println("get op:" + op.toString());
//        Random r = new Random();
//        Thread.sleep(r.nextInt(2));
        if(op.type == JOperationType.READ){
            String replyStr = occLayer.createOrUpdateExecutorByReadOp(op);
//            System.out.println("###################################");
//            System.out.println("READ: "+ replyStr);
            if (replyStr == null) return new byte[]{};
            return replyStr.getBytes();
        }else if(op.type == JOperationType.WRITE){
            occLayer.createOrUpdateExecutorByWriteOp(op);
        }else if(op.type == JOperationType.ABORT){
            occLayer.getExecutorByTn(op.trans_id).cache.abort();
        }else if(op.type == JOperationType.COMMIT){
            boolean succ = occLayer.createOrUpdateExecutorByCommitOp(op);
            if(!succ){
                occLayer.getExecutorByTn(op.trans_id).cache.abort();
            }else{
                reply = new byte[]{1};
            }

        }
        return reply;
    }


    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {

        try {
            if (isMalicious) Thread.sleep(100);
//            System.out.println("###################################");
//            System.out.println("Enter appExecuteOrdered");
            ByteArrayInputStream in = new ByteArrayInputStream(command);
            ByteArrayOutputStream out = null;
            byte[] reply = null;
            int cmd = new DataInputStream(in).readInt();
            if (cmd == JMessageType.SINGLE_OP){
                JOperation op = JMessage.parseOpFromStream(in);
//                System.out.println("###################################");
//                System.out.println("get op:" + op.toString());
                if(op.type == JOperationType.READ){
                    String replyStr = occLayer.createOrUpdateExecutorByReadOp(op);
//                    System.out.println("###################################");
//                    System.out.println("READ: "+ replyStr);
                    return replyStr.getBytes();
                }else if(op.type == JOperationType.WRITE){
                    occLayer.createOrUpdateExecutorByWriteOp(op);
                }else if(op.type == JOperationType.ABORT){
                    occLayer.getExecutorByTn(op.trans_id).cache.abort();
                }else if(op.type == JOperationType.COMMIT){
                    boolean succ = occLayer.createOrUpdateExecutorByCommitOp(op);
                    if(!succ){
                        occLayer.getExecutorByTn(op.trans_id).cache.abort();
                    }
                }
            }else{
//                System.out.println("################################### recieve other commit");
                List<JOperation> ops = JMessage.parseOpsFromStream(in);
                for(JOperation op:ops){
                    this.writeToUniqueDB(op.key, op.val);
//                    System.out.println("write key: "+ op.key + " to val: " + op.val);
                }
                return new byte[]{1};
            }
            return reply;
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        return appExecuteOrdered(command, msgCtx);
    }

    @Override
    public byte[] getSnapshot() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(DB);
            out.flush();
            bos.flush();
            out.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
            return new byte[0];
        }
    }

    @Override
    public void installSnapshot(byte[] state) {
        try {

            // serialize to byte array and return
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
            DB = (MapOfMaps) in.readObject();
            in.close();
            bis.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}