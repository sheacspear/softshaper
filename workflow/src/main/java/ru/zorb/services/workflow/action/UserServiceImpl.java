package ru.zorb.services.workflow.action;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("userService")
public class UserServiceImpl implements UserService {

  public List<String> resolveUsersForTask() {

    return Arrays.asList(new String[] { "alexander", "admin" });
  }

}
