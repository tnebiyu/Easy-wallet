//package com.nebiyu.Kelal.services.jwt;
//
//import com.nebiyu.Kelal.model.User;
//import com.nebiyu.Kelal.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user == null){
//            throw new UsernameNotFoundException("User not found", null);
//        }
//        return new org.springframework.security.core.userdetails.User(user, user.getPassword(), new ArrayList<>());
//    }
//
//    public  Optional<User> loadUserByEmail(String email) throws UsernameNotFoundException {
//
//       Optional<User> user = userRepository.findByEmail(email);
//        if (user == null){
//            throw new UsernameNotFoundException("User not found", null);
//        }
//        return user;
//    }
//    public List<User> getAllUsers(){
//        return userRepository.findAll();
//    }
//}
