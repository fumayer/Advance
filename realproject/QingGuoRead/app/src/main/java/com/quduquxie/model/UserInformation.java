package com.quduquxie.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/1/4.
 * Created by crazylei.
 */

public class UserInformation implements Serializable {
    public User user;
    public Message messages;
    public ArrayList<String> likes;
}
