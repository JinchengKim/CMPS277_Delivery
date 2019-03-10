package bftsmart.demo.delivery.JOOC;

import bftsmart.demo.bftmap.BFTMap;
import bftsmart.demo.bftmap.BFTMapRequestType;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by lijin on 3/10/19.
 */
public class JClientServer{
    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: java BFTMapInteractiveClient <process id>");
            System.exit(-1);
        }
        BFTMap bftMap = new BFTMap(Integer.parseInt(args[0]));

    }
}
//        public static void main(String[] args) throws IOException {
//            if(args.length < 1) {
//                System.out.println("Usage: java BFTMapInteractiveClient <process id>");
//                System.exit(-1);
//            }
//
//            BFTMap bftMap = new BFTMap(Integer.parseInt(args[0]));
//
//            Console console = System.console();
//            Scanner sc = new Scanner(System.in);
//
//
//            while(true) {
//
//                System.out.println("select a command : 1. Read Transcation");
//                System.out.println("select a command : 2. Write Transcation");
//                System.out.println("select a command : 3. CREATE A NEW TABLE OF TABLES");
//                System.out.println("select a command : 4. CREATE A NEW TABLE OF TABLES");
//                System.out.println("select a command : 11. EXIT");
//
//                int cmd = sc.nextInt();
//
//                switch(cmd) {
//                    case OperationType.READ:
//                        break;
//                    case OperationType.WRITE:
//                        break;
//                    case OperationType.ABORT:
//                        break;
//                    case OperationType.COMMIT:
//                        break;
//                }
//            }
//        }
//}
