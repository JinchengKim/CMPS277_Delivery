package bftsmart.demo.delivery.application;

import bftsmart.demo.delivery.OOC.JClientDatabase;

import java.io.Console;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by lijin on 3/8/19.
 */
public class JConsumer {
    private int id;
    private String name;
    private static String tableName = "db_jdeliver";

    public void searchMerchant(){

    }

    public void order(int mid){

    }

    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: java JCourier <process id>");
            System.exit(-1);
        }
        int cid = Integer.parseInt(args[0]);
        JClientDatabase db = new JClientDatabase(cid, tableName);

        Console console = System.console();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("select a command : 1.searchMerchant");
            System.out.println("select a command : 2.order of a Merchant");
            System.out.println("select a command : 11.exist");
            int cmd = sc.nextInt();
            switch (cmd){
                case 1:
                    System.out.println("All the Merchant Id:");
                    List<String> merchantID = db.readAllKey();
                    for (String id:merchantID){
                        System.out.print(id + " ");
                    }
                    System.out.println();
                    break;
                case 2:
                    //
                    String key = console.readLine("Enter the Merchant Id: ");
                    boolean keyExists = db.containsKey(tableName, key);
                    if(keyExists) {
                        Date date = new Date();
                        String val = "" + cid + date.getTime() + ",";
                        byte[] resultBytes = db.write(key, "");
                        System.out.println("The value received from GET is: " + new String(resultBytes));
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