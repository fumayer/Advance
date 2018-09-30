package com.example.knowledgepointsharelib.algorithm.exhaustion;

/**
 * Created by sunjie on 2018/9/30.
 */

public class PoissonShareWine {

    private final static int bigGoblet = 12; //大瓶子容量
    private final static int midGoblet = 8;  // 中瓶子容量
    private final static int littleGoblet = 5; // 小瓶子容量
    private final static int targetValume = 11; // 目标量

    public static void main(String[] args) {
//         刚开始倒酒的大中小瓶子中的酒量
        poissonShareWine(12, 0, 0, targetValume);
    }
    /**
     1. 大瓶子只能倒入中瓶子
     2. 中瓶子只能倒入小瓶子
     3. 小瓶子只能倒入大瓶子
     4. 小瓶子只有在已经装满的情况下才能倒入大瓶子
     5. 若小瓶子被倒空，则无论中瓶子是否满，应马上从中瓶子倒入小瓶子
     6. 若中瓶子被倒空，则无论大瓶子是否满，应马上从大瓶子倒入中瓶子
     7. 没有规定中瓶子非空要怎么样，所以中瓶子非空，小瓶子有酒美满的情况下，就从中瓶子给小瓶子倒酒
     * @param currentBigValume    当前大瓶子量
     * @param currentMidValume    当前中瓶子量
     * @param currentLittleValume 当前小瓶子量
     * @param targetValume        目标量
     */
    private static void poissonShareWine(int currentBigValume, int currentMidValume, int currentLittleValume, int targetValume) {
        System.out.println("大瓶子剩余："+currentBigValume+"  中瓶子剩余："+currentMidValume+"  小瓶子剩余："+currentLittleValume);
        if (currentBigValume==targetValume||currentMidValume==targetValume||currentLittleValume==targetValume) {
            System.out.println("倒出了指定的酒量");
            return;
        }

//        小瓶子满，倒入大瓶子
        if (currentLittleValume==littleGoblet) {
//            下瓶子倒入大瓶子，考虑，大瓶子是否满，继续分酒
            if (currentBigValume+currentLittleValume<=bigGoblet) {
                poissonShareWine(currentBigValume+currentLittleValume,currentMidValume,0,targetValume);
            }else{
                poissonShareWine(bigGoblet,currentMidValume,currentLittleValume-(bigGoblet-currentBigValume),targetValume);
            }
        }
//        中瓶子空，大瓶子往中瓶子倒酒
        if (currentMidValume==0){
//            大瓶子倒入中瓶子，考虑中瓶子是否满
            if (currentBigValume>=midGoblet) {
                poissonShareWine(currentBigValume-midGoblet,midGoblet,currentLittleValume,targetValume);
            }else {
                poissonShareWine(0,currentBigValume,currentMidValume,targetValume);
            }
        }
//        中瓶子有酒，小瓶子没有满，中瓶子酒倒入小瓶子
        if (currentLittleValume!=littleGoblet&&currentMidValume>0){
//            中瓶子倒入小瓶子，考虑小瓶子是否满
            if (currentMidValume>=(littleGoblet-currentLittleValume)) {
                poissonShareWine(currentBigValume,currentMidValume-(littleGoblet-currentLittleValume),littleGoblet,targetValume);
            }else {
                poissonShareWine(currentBigValume,0,currentMidValume,targetValume);
            }
        }
    }
}
