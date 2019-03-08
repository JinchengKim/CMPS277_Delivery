package bftsmart.demo.delivery;

import bftsmart.demo.delivery.OOC.JOperation;
import bftsmart.demo.delivery.OOC.JServerDatabase;
/**
 * Created by lijin on 3/4/19.
 */
public class JTest {
    JTest(){

    }
    public static void main(String[] args){

        JServerDatabase db = new JServerDatabase();
        // create transcation1
        db.write("x", "1");

        // create transcation2


        // create transcation3

        // create transcation4
        System.out.println("====== JTest ======");
    }
}
