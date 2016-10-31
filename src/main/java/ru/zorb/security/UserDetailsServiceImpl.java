package ru.zorb.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import ru.zorb.storage.jooq.tables.daos.RolesDao;
import ru.zorb.storage.jooq.tables.daos.UsersDao;
import ru.zorb.storage.jooq.tables.daos.UsersRolesDao;
import ru.zorb.storage.jooq.tables.pojos.Roles;
import ru.zorb.storage.jooq.tables.pojos.Users;
import ru.zorb.storage.jooq.tables.pojos.UsersRoles;

/**
 * Created by sunchise on 25.04.16.
 */
/**
 * @author ashek
 *
 */
@Service
@ThreadSafe
public class UserDetailsServiceImpl implements UserDetailsService {
	/**
	 * UsersDao
	 */
	@Autowired
	private UsersDao users;

	/**
	 * UsersRolesDao
	 */
	@Autowired
	private UsersRolesDao usersRolesDao;

	/**
	 * RolesDao
	 */
	@Autowired
	private RolesDao rolesDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	@Cacheable("userRole")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails loadedUser;

		try {
			List<Users> clients = users.fetchByUsername(username);
			Preconditions.checkNotNull(clients);
			Preconditions.checkArgument(!clients.isEmpty());
			Users client = clients.get(0);
			Preconditions.checkNotNull(client);
	
			List<UsersRoles> rolses = usersRolesDao.fetchByFromId(client.getId());

			Long[] roles3 = new Long[rolses.size()];
			for (int i = 0; i < rolses.size(); i++) {
				roles3[i] = rolses.get(i).getId();
			}
			List<Roles> rolesR = rolesDao.fetchById(roles3);

			List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
			for (Roles role4 : rolesR) {
				result.add(new GrantedAuthority() {
					/**
					 *
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getAuthority() {
						return role4.getRole();
					}
				});
			}

			loadedUser = new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(),
					result);
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}
		return loadedUser;
	}
}
