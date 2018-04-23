package com.example.sunjie.knowledgepointshareproject.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunjie on 2018/4/23.
 */

public class ChessFactory {
    private static ChessFactory chessFactory = new ChessFactory();

    private ChessFactory() {
    }

    public static ChessFactory getInstance() {
        return chessFactory;
    }

    private Map<Character, AbsChessman> chessmanMap = new HashMap<>();

    public AbsChessman factory(char c) {

        if (chessmanMap != null) {
            AbsChessman chessman = chessmanMap.get(c);
            if (chessman == null) {

                switch (c) {
                    case 'B':
                        chessman = new BlackChessman();
                        break;
                    case 'W':
                        chessman = new WhiteChessman();
                        break;
                    default:
                        System.err.println("非法");
                        break;
                }
                if (chessman != null) {
                    chessmanMap.put(c, chessman);
                }
            }

            return chessman;
        }
        return null;
    }
}
