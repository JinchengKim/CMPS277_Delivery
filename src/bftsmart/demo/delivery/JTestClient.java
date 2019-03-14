package bftsmart.demo.delivery;

import com.sun.codemodel.internal.JOp;

import java.io.Console;
import java.io.IOException;
import java.util.*;

import static bftsmart.demo.delivery.JOperationType.READ;
import static bftsmart.demo.delivery.JOperationType.WRITE;

/**
 * Created by lijin on 3/10/19.
 */
class JTestHelper{
    Set<Integer> processTranscationSet = new HashSet<Integer>();
    Set<String > keyset = new HashSet<>();
    Set<String > valueset = new HashSet<>();
    Set<Integer> used = new HashSet<Integer>();
    List<Integer> ids = new ArrayList<Integer>();
    int size = 1000000;
    int therhold = 1;
    int[] counter = new int[size];

    Random r = new Random();
    JTestHelper(){
        for (int i = 0 ; i < 100; i++)
            keyset.add(i + "");
        for (int i = 0 ; i < 100; i++)
            valueset.add(i + "Value");
        for (int i = 0 ; i < size; i++)
            ids.add(i);
    }

    public String getRandomKey(){
        int i = r.nextInt(99);
        for (String s:keyset){
            if (i == 0) return s;
            i--;
        }
        return "1";
    }

    public String getRandomValue(){
        int i = r.nextInt(99);
        for (String s:valueset){
            if (i == 0) return s;
            i--;
        }
        return "1";
    }

    public int getRandomTid(){
        int i = r.nextInt(processTranscationSet.size());
        for (Integer s:processTranscationSet){
            if (i == 0) return s;
            i--;
        }
        return 0;
    }

    public JOperation createReadOperation(int tid, String key){
        return new JOperation(tid, READ, key, "");
    }

    public JOperation createWriteOperation(int tid, String  key, String  value){
        return new JOperation(tid, WRITE, key, value);
    }

    public JOperation createCommitOperation(int tid){
        return new JOperation(tid, JOperationType.COMMIT, "", "");
    }

    public JOperation generateOperation(){
        int idx = r.nextInt(ids.size());
        int tid = (int) ids.get(idx);
        int oprType = r.nextInt(5);
        if (counter[tid] >= therhold){
//            System.out.println(tid + " " + counter[tid]);
            counter[tid] = 0;
            processTranscationSet.remove(tid);
            ids.set(idx, ids.get(ids.size() - 1));
            ids.remove(ids.size() - 1);
            return createCommitOperation(tid);
        }
//        else if (processTranscationSet.size() > 0 && oprType == 3){
//            tid = getRandomTid();
//            processTranscationSet.remove(tid);
//            return createCommitOperation(tid);
//        }
        else{
            processTranscationSet.add(tid);
            counter[tid]++;
            if (r.nextBoolean()){
                return createReadOperation(tid, getRandomKey());
            }else{
                return createWriteOperation(tid, getRandomKey(), getRandomValue());
            }
        }
    }


    public List<JOperation> generateOpration(){
        // count == 1 // all read
        // count == -1 // random
        int tid = 1;
        List<JTranscation> trans = new ArrayList<JTranscation>();
        for (int i = 1; i <= 100000; i++){
            int random = r.nextInt(5);
//            trans.add(JTranscation.createOrderTranscation(i,this.getRandomKey(), this.getRandomValue()));
//            trans.add(JTranscation.createSearchTranscation(i,this.getRandomKey()));
            if (random%2 == 0)
                trans.add(JTranscation.createSearchTranscation(i,this.getRandomKey()));
            else
                trans.add(JTranscation.createOrderTranscation(i,this.getRandomKey(), this.getRandomValue()));
        }

        Collections.shuffle(trans);
        List<JOperation> operations = new ArrayList<>();
        Map<Integer, List<JOperation>> transWithId = new HashMap<>();
        List<Integer> shuffleMap = new ArrayList<>();
        for (JTranscation t:trans){
            for (JOperation op:t.ops){
                shuffleMap.add(op.trans_id);
            }
            JOperation opp = t.ops.get(0);
            transWithId.put(opp.trans_id, t.ops);
        }
        Collections.shuffle(shuffleMap);
        for (int id:shuffleMap){
//            if (transWithId.get(id).size() ==1) {
//                System.out.println(transWithId.get(id).get(0).toString());
//            }
            if (transWithId.get(id).size() > 0) {
                operations.add(transWithId.get(id).get(0));
                transWithId.get(id).remove(0);
            }
        }
        return operations;
    }

}

public class JTestClient {
    private static int read_counter = 0;
    private static int commit_counter = 0;
    private static int write_counter = 0;
    private static long lantency = 0;
    private static long previous = 0;
    private static int success_transcation = 0;

    private static int transcation_count = 0;
    public static long getCurrentTime(){
        Date d = new Date();
        return d.getTime();
    }
//    public static void main(String[] args) throws IOException, InterruptedException {
//        if(args.length < 2) {
//            System.out.println("Usage: java BFTMapInteractiveClient <process id>");
//            System.exit(-1);
//        }
//
//        JServer replica = new JServer(Integer.parseInt(args[1]), true);
//        Console console = System.console();
//        Scanner sc = new Scanner(System.in);
//        JTestHelper helper = new JTestHelper();
//
//        previous = getCurrentTime();
//
//        while (true){
//            if (getCurrentTime() - previous >= 1000) {
//                System.out.println("----------------------------------------------------");
//                System.out.println();
//                System.out.println("handle " + transcation_count +" transcations");
//                System.out.println("with total lantency " + lantency + " ms");
//                System.out.println();
//                System.out.println("----------------------------------------------------");
//                transcation_count = 0;
//                lantency = 0;
//                previous = getCurrentTime();
//            }
//            JTranscation t = helper.generateTranscation();
//            byte[] reply;
//            long b;
//            for (JOperation opr:t.ops){
//                switch (opr.type){
//                    case READ:
//                        b = getCurrentTime();
//                        reply = replica.recieveMessage(opr);
//                        lantency = lantency +  getCurrentTime() - b;
//                        read_counter++;
//                        break;
//                    case WRITE:
//                        b = getCurrentTime();
//                        reply = replica.recieveMessage(opr);
//                        lantency = lantency +  getCurrentTime() - b;
//                        write_counter++;
//                        break;
//                    case COMMIT:
//                        b = getCurrentTime();
//                        replica.recieveMessage(opr);
//                        lantency = lantency +  getCurrentTime() - b;
//                        commit_counter++;
//
//                        break;
//                }
//            }
//            transcation_count ++;
//        }
//
////        while(true) {
////
////            System.out.println("select a command : 1. Send a read transcation");
////            System.out.println("select a command : 2. Send a write transcation");
////            System.out.println("select a command : 3. Send a commit transcation");
////            System.out.println("select a command : 4. Send a abort transcation");
////
////            System.out.println("select a command : 11. EXIT");
////
////            int cmd = sc.nextInt();
////            int tid = 0;
////            String idStr;
////            String key;
////            String val;
////            ByteArrayOutputStream output;
////            JOperation opr;
////            switch(cmd) {
////                //operations on the table
////                case 1:
////                    // read
////                    idStr = console.readLine("Enter the Transcation ID: ");
////                    tid = Integer.parseInt(idStr);
////                    key = console.readLine("Enter the key you want to read: ");
////                    opr = new JOperation(tid, READ, key, "");
//////                    output = JMessage.assembleOpIntoStream();
////                    byte[] reply = replica.recieveMessage(opr);
////
////                    System.out.println("Client send a read Transcation with id " + tid+ " to read: " + key );
////                    System.out.println("Get:" + new String(reply));
////                    break;
////
////                case 2:
////                    // write
////
////                    idStr = console.readLine("Enter the Transcation ID: ");
////                    tid = Integer.parseInt(idStr);
////                    key = console.readLine("Enter the key you want to write: ");
////                    val = console.readLine("Enter the vale you want to write: ");
////
////                    opr = new JOperation(tid, JOperationType.WRITE, key, val);
//////                    bftMap.KVProxy.invokeOrdered(output.toByteArray());
////                    replica.recieveMessage(opr);
////                    System.out.println("Client send a write Transcation with id " + tid+ " to write: " + key + " with value:" + val);
////                    break;
////
////                case 3:
////                    // commit
////                    idStr = console.readLine("Enter the Transcation ID: ");
////                    tid = Integer.parseInt(idStr);
////                    opr = new JOperation(tid, JOperationType.COMMIT, "", "");
////                    System.out.println(opr.toString());
////                    replica.recieveMessage(opr);
////                    System.out.println("Client send a commite Transcation with id " + tid);
////                    break;
////
////                case 4:
////                    // abort
////                    System.out.println("commit put function");
////                    break;
////                case 11:
////                    System.exit(-1);
////                default:
////                    System.out.println("Unknown input");
////                    System.exit(-1);
////            }
//
//    }

    // operation
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 2) {
            System.out.println("Usage: java BFTMapInteractiveClient <process id>");
            System.exit(-1);
        }

        JServer replica = new JServer(Integer.parseInt(args[1]), true);
        Console console = System.console();
        Scanner sc = new Scanner(System.in);
        JTestHelper helper = new JTestHelper();

        previous = getCurrentTime();
        int cou = 0;
        long time[] = new long[100000 + 1];

        List<JOperation> ops = helper.generateOpration();
        int index = 0;
        long begin = getCurrentTime();
        while (index < ops.size()- 1){
            JOperation op = ops.get(index);
            byte[] reply;
            if(time[op.trans_id] == 0) time[op.trans_id] = getCurrentTime();
            switch (op.type){
                case READ:
                    reply = replica.recieveMessage(op);
                    read_counter++;
                    break;
                case WRITE:
                    reply = replica.recieveMessage(op);
                    write_counter++;
                    break;
                case COMMIT:
                    reply = replica.recieveMessage(op);
                    commit_counter++;
                    if (reply != null){
                        if (time[op.trans_id] == 0)
                            System.out.print("ERROR");
                        lantency = lantency +  getCurrentTime() - time[op.trans_id];
                        success_transcation++;
                    }

                    break;
            }
            index++;
        }

        System.out.println("----------------------------------------------------");
        System.out.println();
        System.out.println("handle " + read_counter +" read operation");
        System.out.println("handle " + write_counter +" write operation");
        System.out.println("handle " + commit_counter +" commit operation");
        System.out.println("handle " + success_transcation +" transcations successfully");
        System.out.println("with total lantency " + lantency/success_transcation + " ms");
        System.out.println("run time is " + (getCurrentTime() - begin));

        System.out.println();
        System.out.println("----------------------------------------------------");

//        while(true) {
//
//            System.out.println("select a command : 1. Send a read transcation");
//            System.out.println("select a command : 2. Send a write transcation");
//            System.out.println("select a command : 3. Send a commit transcation");
//            System.out.println("select a command : 4. Send a abort transcation");
//
//            System.out.println("select a command : 11. EXIT");
//
//            int cmd = sc.nextInt();
//            int tid = 0;
//            String idStr;
//            String key;
//            String val;
//            ByteArrayOutputStream output;
//            JOperation opr;
//            switch(cmd) {
//                //operations on the table
//                case 1:
//                    // read
//                    idStr = console.readLine("Enter the Transcation ID: ");
//                    tid = Integer.parseInt(idStr);
//                    key = console.readLine("Enter the key you want to read: ");
//                    opr = new JOperation(tid, READ, key, "");
////                    output = JMessage.assembleOpIntoStream();
//                    byte[] reply = replica.recieveMessage(opr);
//
//                    System.out.println("Client send a read Transcation with id " + tid+ " to read: " + key );
//                    System.out.println("Get:" + new String(reply));
//                    break;
//
//                case 2:
//                    // write
//
//                    idStr = console.readLine("Enter the Transcation ID: ");
//                    tid = Integer.parseInt(idStr);
//                    key = console.readLine("Enter the key you want to write: ");
//                    val = console.readLine("Enter the vale you want to write: ");
//
//                    opr = new JOperation(tid, JOperationType.WRITE, key, val);
////                    bftMap.KVProxy.invokeOrdered(output.toByteArray());
//                    replica.recieveMessage(opr);
//                    System.out.println("Client send a write Transcation with id " + tid+ " to write: " + key + " with value:" + val);
//                    break;
//
//                case 3:
//                    // commit
//                    idStr = console.readLine("Enter the Transcation ID: ");
//                    tid = Integer.parseInt(idStr);
//                    opr = new JOperation(tid, JOperationType.COMMIT, "", "");
//                    System.out.println(opr.toString());
//                    replica.recieveMessage(opr);
//                    System.out.println("Client send a commite Transcation with id " + tid);
//                    break;
//
//                case 4:
//                    // abort
//                    System.out.println("commit put function");
//                    break;
//                case 11:
//                    System.exit(-1);
//                default:
//                    System.out.println("Unknown input");
//                    System.exit(-1);
//            }

    }
}
