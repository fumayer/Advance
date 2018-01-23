package com.example.sj.app2.fragmentconn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sunjie on 2018/1/23.
 */

public class Functions {
    private Functions() {
    }

    private List<HashMap> maxLists = new ArrayList<>();
    private static Functions instance;
    private HashMap<String, FunNoParamsNoResult> noParamsNoResultHashMap;
    private HashMap<String, FunNoParamsHasResult> noParamsHasResultHashMap;

    public static Functions getInstance() {
        if (instance == null) {
            synchronized (Functions.class) {
                if (instance == null) {
                    instance = new Functions();
                }
            }
        }
        return instance;
    }

    public static abstract class Function {
        private String name;

        public Function(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static abstract class FunNoParamsNoResult extends Function {
        public FunNoParamsNoResult(String name) {
            super(name);
        }

        abstract void function();
    }

    public static abstract class FunNoParamsHasResult<Result> extends Function {
        public FunNoParamsHasResult(String name) {
            super(name);
        }

        abstract Result function();
    }

    /**
     * 添加到集合
     *
     * @param function
     */
    public void addFunction(Function function) {
        if (function instanceof FunNoParamsNoResult) {
            if (noParamsNoResultHashMap == null) {
                noParamsNoResultHashMap = new HashMap<>();
                maxLists.add(noParamsNoResultHashMap);
            }
            noParamsNoResultHashMap.put(function.getName(), (FunNoParamsNoResult) function);
        } else if (function instanceof FunNoParamsHasResult) {
            if (noParamsHasResultHashMap == null) {
                noParamsHasResultHashMap = new HashMap<>();
                maxLists.add(noParamsHasResultHashMap);

            }
            noParamsHasResultHashMap.put(function.getName(), (FunNoParamsHasResult) function);
        }
    }

    public void invokeFunction(String key) {
        if (noParamsNoResultHashMap != null) {
            noParamsNoResultHashMap.get(key).function();
        }
    }

    public <Result> Result invokeFunction(String key, Class<Result> c) {
        if (noParamsHasResultHashMap != null) {
            FunNoParamsHasResult hasResult = noParamsHasResultHashMap.get(key);
            if (hasResult != null) {
                if (c != null) {
                    return c.cast(hasResult.function());
                } else {
                    return (Result) hasResult.function();
                }
            }
        }
        return null;
    }

    public void removeFunction(String name) {

        if (maxLists != null) {
            for (int i = 0, length = maxLists.size(); i < length; i++) {
                HashMap map = maxLists.get(i);
                if (map != null) {
                    if (map.size() > 0) {
                        map.remove(name);
                    }
                }
            }
        }

    }


}
