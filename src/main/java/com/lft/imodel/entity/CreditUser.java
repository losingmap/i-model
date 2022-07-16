package com.lft.imodel.entity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface CreditUser extends UserDetails {

    Object getUser();

    void setUser(Object user);
}
