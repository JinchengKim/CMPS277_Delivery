package bftsmart.demo.delivery.application;

import bftsmart.communication.SystemMessage;
import bftsmart.demo.delivery.OOC.JClientDatabase;

import java.io.Console;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by lijin on 3/8/19.
 */
public class JCourier {
    private int id;
    private String name;
    private static String tableName = "db_jdeliver";
    JCourier(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void searchMerchant(){

    }

    public void delivery(int mid, int cid){

    }

    public void searchOrder(int mid){

    }



    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: java JCourier <process id>");
            System.exit(-1);
        }

        int cid = Integer.parseInt(args[0]);
        JClientDatabase db = new JClientDatabase(cid, "db_jdeliver");
        Console console = System.console();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("select a command : 1.searchMerchant");
            System.out.println("select a command : 2.delivery");
            System.out.println("select a command : 3.searchOrder");
            System.out.println("select a command : 11.exist");
            int cmd = sc.nextInt();
            switch (cmd){
                case 1:
//                    searchMerchant
                    System.out.println("All the Merchant Id:");
                    List<String> merchantID = db.readAllKey();
                    for (String id:merchantID){
                        System.out.print(id + " ");
                    }
                    System.out.println();
                    break;
                case 2:
//                    delivery
                    break;
                case 3:
//                    searchOrder
                    String key = console.readLine("Enter the Merchant Id: ");
                    boolean keyExists = db.containsKey(tableName, key);
                    if(keyExists) {
                        Date date = new Date();
                        String val = "" + cid + date.getTime();
//                        byte[] resultBytes = db.write()
//                        System.out.println("The value received from GET is: " + new String(resultBytes));
                    } else
                        System.out.println("Merchant Id not found");

                    break;
                case 11:
                    System.out.println("system exist");
                    System.exit(-1);
                    break;
                default:
                    System.out.println("error input exist");
                    System.exit(-1);
                    break;
            }
        }

    }
}
