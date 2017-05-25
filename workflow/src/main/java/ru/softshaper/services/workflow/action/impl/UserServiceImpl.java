package ru.softshaper.services.workflow.action.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import ru.softshaper.services.workflow.action.UserService;

@Component("userService")
public class UserServiceImpl implements UserService {

  public List<String> resolveUsersForTask() {

    return Arrays.asList(new String[] { "alexander", "admin" });
  }

}
