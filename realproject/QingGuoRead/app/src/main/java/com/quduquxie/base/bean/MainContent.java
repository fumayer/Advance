package com.quduquxie.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContent implements Serializable {

    public int index;
    public String name;
    public ArrayList<MainContentInformation> data;
}