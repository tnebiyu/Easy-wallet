package com.nebiyu.Kelal.super_admin.service;

import com.nebiyu.Kelal.super_admin.SuperAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    SuperAdminRepo superAdminRepo;
    
}
