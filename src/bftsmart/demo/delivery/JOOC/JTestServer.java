package bftsmart.demo.delivery.JOOC;

import bftsmart.demo.bftmap.BFTMapRequestType;
import bftsmart.demo.bftmap.BFTMapServer;
import bftsmart.demo.bftmap.MapOfMaps;
import bftsmart.demo.delivery.OOC.JOperation;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import sun.jvm.hotspot.opto.MachNode;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lijin on 3/9/19.
 */
public class JTestServer extends DefaultSingleRecoverable {

    MapOfMaps tableMap = null;
    JOOCLayer occlayer;
    ServiceProxy KVProxy = null;
    final String tableName = "test_table";

    //ServiceReplica replica = null;
    //private ReplicaContext replicaContext;

    //The constructor passes the id of the server to the super class
    public JTestServer(int id) {

        tableMap = new MapOfMaps();
        new ServiceReplica(id, this, this);
        KVProxy = new ServiceProxy(id, "config");
        tableMap.addTable(tableName, new HashMap<String, byte[]>());

        occlayer = new JOOCLayer(this);
    }

    public static void main(String[] args){
        if(args.length < 1) {
            System.out.println("Use: java BFTMapServer <processId>");
            System.exit(-1);
        }
        new BFTMapServer(Integer.parseInt(args[0]));
    }


    public Map<String, byte[]> getCopyDatabase(){
        return tableMap.getTable(tableName);
    }

    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(command);
            ByteArrayOutputStream out = null;
            byte[] reply = null;
            int cmd = new DataInputStream(in).readInt();

            if (cmd == JMessageType.OPR){
                JOperation opr = JMessage.getOprFromStream(in);
                switch (opr.type){
                    case OperationType.WRITE:
                        break;
                    case OperationType.READ:
                        break;
                    case OperationType.COMMIT:
                        break;
                    case OperationType.ABORT:
                        break;
                    default:
                        break;
                }
            }else{
                List<JOperation> oprs = JMessage.getOprsFromStream(in);

            }

            return reply;
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(command);
            ByteArrayOutputStream out = null;
            byte[] reply = null;
            int cmd = new DataInputStream(in).readInt();

            if (cmd == JMessageType.OPR){
                JOperation opr = JMessage.getOprFromStream(in);
            }else{
                List<JOperation> oprs = JMessage.getOprsFromStream(in);
            }

            return reply;
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public byte[] getSnapshot() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(tableMap);
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
            tableMap = (MapOfMaps) in.readObject();
            in.close();
            bis.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BFTMapServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
