package com.dhiraj.canteen.DAO;

import com.dhiraj.canteen.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findUserByEmail(String emailId);
}
