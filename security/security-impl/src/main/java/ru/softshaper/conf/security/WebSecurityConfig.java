package ru.softshaper.conf.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.softshaper.conf.cache.CacheManagerConfig;
import ru.softshaper.conf.db.JooqConfig;
import ru.softshaper.services.security.RestAuthenticationEntryPoint;
import ru.softshaper.services.security.SHAPasswordEncoder;
import ru.softshaper.services.security.token.RestTokenAuthenticationFilter;
import ru.softshaper.services.security.token.TokenAuthenticationManager;
import ru.softshaper.services.security.token.storage.DBTokenStorage;
import ru.softshaper.services.security.token.storage.TokenStorage;
import ru.softshaper.storage.jooq.tables.daos.RolesDao;
//import ru.softshaper.storage.jooq.tables.daos.RolesDao;
//import ru.softshaper.storage.jooq.tables.daos.UsersDao;
//import ru.softshaper.storage.jooq.tables.daos.UsersRolesDao;
//import ru.softshaper.storage.jooq.tables.pojos.Roles;
//import ru.softshaper.storage.jooq.tables.pojos.Users;
//import ru.softshaper.storage.jooq.tables.pojos.UsersRoles;
import ru.softshaper.storage.jooq.tables.daos.UsersDao;
import ru.softshaper.storage.jooq.tables.daos.UsersRolesDao;
import ru.softshaper.storage.jooq.tables.pojos.Roles;
import ru.softshaper.storage.jooq.tables.pojos.Users;
import ru.softshaper.storage.jooq.tables.pojos.UsersRoles;

/**
 * WebSecurityConfig for security
 */
@Configuration
@Import({ CacheManagerConfig.class, JooqConfig.class })
@ComponentScan({"ru.softshaper.services.security","ru.softshaper.staticcontent.organizations","ru.softshaper.staticcontent.sec"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * DataSource
	 */
	@Autowired
	private DataSource dataSource;
	/**
	 * UserDetailsServiceImpl
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * UsersDao
	 */
	 @Autowired
	 private UsersDao usersRepository;

	/**
	 * RolesDao
	 */
	 @Autowired
	 private RolesDao roleRepository;

	/**
	 * UsersRolesDao
	 */
	 @Autowired
	 private UsersRolesDao usersRolesDao;

	/**
	 * TokenAuthenticationManager
	 */
	@Autowired
	private TokenAuthenticationManager tokenAuthenticationManager;

	/**
	 * @return TokenStorage
	 */
	@Bean
	public TokenStorage tokenStorage() {
		return new DBTokenStorage();
	}

	/**
	 * @return RestAuthenticationEntryPoint
	 */
	@Bean
	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	/**
	 * configureGlobalSecurity
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	/**
	 * @param auth
	 * @throws Exception
	 */
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.userDetailsService(userDetailsService);
	// auth.authenticationProvider(authenticationProvider());
	// }

	/**
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SHAPasswordEncoder();
	}

	/**
	 * @return DaoAuthenticationProvider
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	/**
	 * @param defaultFilterProcessesUrl
	 * @return
	 */
	public RestTokenAuthenticationFilter restTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		RestTokenAuthenticationFilter restTokenAuthenticationFilter = new RestTokenAuthenticationFilter(
				defaultFilterProcessesUrl);
		restTokenAuthenticationFilter.setAuthenticationManager(tokenAuthenticationManager);
		return restTokenAuthenticationFilter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.
	 * springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO CRLF enable
		HttpBasicConfigurer<HttpSecurity> httpBasic = http.httpBasic();
		httpBasic.authenticationEntryPoint(new RestAuthenticationEntryPoint());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.csrf().disable();
		http.headers().frameOptions().disable();// CRLF sameOrigin
		// http.addFilterAfter(restTokenAuthenticationFilter("/workflow/**"),
		// UsernamePasswordAuthenticationFilter.class);//todo
		http.addFilterAfter(restTokenAuthenticationFilter("/rest/pr/**/*"), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(restTokenAuthenticationFilter("/rest/reportservice/report"),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/*", "/rest/pb/**/*", "/rest/pb/**/**/*", "/rest/pb/login/login", "/app/**", "/workflow/**","/ws","/img/**")
				.permitAll()
				// .antMatchers("/rest/pb/login/login").permitAll()
				.antMatchers("/rest/pr/**/*").fullyAuthenticated().anyRequest().authenticated()
				// .formLogin()
				// .loginPage("/login/index.jsp")
				// .failureUrl("/page401.htm")
				// .successHandler(getTokenAuthenticationSuccessHandler(
				// "/app/index.jsp"))
				// .permitAll()
				.and().logout().deleteCookies(RestTokenAuthenticationFilter.HEADER_SECURITY_TOKEN).permitAll();		
		
		 //createBaseUser("ashek","ashek","alexander","lopos","alex","admin","test");
		// createBaseUser("test_user","A123456b");
	}

  private void createBaseUser(String user, String passs) {
    // if(users!=null){
    // for(String user:users){
    List<Users> admins = usersRepository.fetchByUsername(user);
    if (admins != null) {
      usersRepository.delete(admins);
      List<ru.softshaper.storage.jooq.tables.pojos.UsersRoles> userRoles = usersRolesDao
          .fetchByFromId(admins.stream().map(e -> e.getId()).collect(Collectors.toList()).toArray(new Long[0]));
      if (userRoles != null) {
        usersRolesDao.delete(userRoles);
      }
    }

    if (admins == null || admins.isEmpty()) {
      // Roles role = new Roles(null, "ROLE_ADMIN");
		 //roleRepository.insert(role);
		   List<ru.softshaper.storage.jooq.tables.pojos.Roles> roles = roleRepository.fetchByRole("ROLE_ADMIN");
		 final Users entity = new Users(null, passwordEncoder().encode(passs),
				 user);
		 usersRepository.insert(entity);
		 UsersRoles usersRoles = new UsersRoles(null, entity.getId(),
		     roles.get(0).getId());
		 usersRolesDao.insert(usersRoles);
		 }
		 //}
		//}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter# authenticationManagerBean()
	 */
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * @return MutableAclService
	 */
	@Bean
	public MutableAclService aclService() {
		AuditLogger auditLogger = new ConsoleAuditLogger();
		PermissionGrantingStrategy permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(auditLogger);
		AclAuthorizationStrategy authorizationStrategy = new AclAuthorizationStrategyImpl(new GrantedAuthority() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_ADMIN";
			}

		});
		AclCache cache = new SpringCacheBasedAclCache(new ConcurrentMapCache("aclCache"), permissionGrantingStrategy,
				authorizationStrategy);
		LookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, cache, authorizationStrategy, auditLogger);
		return new JdbcMutableAclService(dataSource, lookupStrategy, cache);
	}
}
