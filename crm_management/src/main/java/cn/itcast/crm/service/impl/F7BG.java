package cn.itcast.crm.service.impl;

/**
 * Created by ${joel} on 2017/9/27 0027.
 */
public class F7BG {
    //逢7必过游戏
    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            if (i < 11) {
                if (i % 7 == 0) {
                    System.out.print(i + "\t");
                }
            } else {
                if ((i / 10) % 7 == 0 || i % 7 == 0 || (i % 10) % 7 == 0) {
                    System.out.print(i + "\t");
                }
            }
        }
    }
}
