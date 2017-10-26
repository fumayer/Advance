package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class RecoveredPassword implements Serializable {
    public User user;
    public int is_new;
    public String token;
    public int refound_sec;
    public int is_uploaded;
}
