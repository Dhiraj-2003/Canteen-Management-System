package com.dhiraj.canteen.Service;

import com.dhiraj.canteen.DAO.UserRepo;
import com.dhiraj.canteen.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo theUserRepo;

    @Transactional
    public void addUser(User theUser){
        theUserRepo.save(theUser);
    }

    public User getUserByEmail(String emailId){
        User user = theUserRepo.findUserByEmail(emailId);
        return user;
    }

}
