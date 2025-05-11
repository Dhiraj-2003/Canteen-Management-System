package com.dhiraj.canteen.Service;

import com.dhiraj.canteen.DAO.UserRepo;
import com.dhiraj.canteen.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    // Add or update user
    @Transactional
    public void saveUser(User user) {
        userRepo.save(user);
    }

    // Fetch user by email
    public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }
}
