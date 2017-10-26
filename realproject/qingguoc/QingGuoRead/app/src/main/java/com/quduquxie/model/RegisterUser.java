package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/28.
 * Created by crazylei.
 */

public class RegisterUser implements Serializable {
    public User user;
    public int is_new;
    public String token;
    public int is_uploaded;
}
