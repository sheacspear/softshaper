package ru.softshaper.services.security.token.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.services.security.token.Token;
import ru.softshaper.storage.jooq.tables.daos.SecTokenDao;
import ru.softshaper.storage.jooq.tables.pojos.SecToken;

/**
 * Created by Sunchise on 06.07.2016.
 */
public class DBTokenStorage implements TokenStorage {

	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Autowired
	private SecTokenDao tokenRepository;

	@Autowired
	private DbTokenConverter tokenConverter;

	@Override
	@Cacheable("token")
	public Token getToken(String tokenId) {
		SecToken tokenBean = tokenRepository.fetchOneById(tokenId);
		return tokenBean == null ? null : tokenConverter.toSecurityToken(tokenBean);
	}

	@Override
	@CacheEvict(cacheNames = { "token" }, allEntries = true)
	public void addToken(Token token) {
		actualizeCurToken(token.getLogin());
		SecToken tokenBean = tokenConverter.toDbToken(token);
		tokenRepository.insert(tokenBean);
	}

	public void actualizeCurToken(String login) {
		List<SecToken> oldTokenBeans = tokenRepository.fetchByLogin(login);
		if (oldTokenBeans != null && !oldTokenBeans.isEmpty()) {
			Set<SecToken> oldTokenBeansSet = new HashSet<SecToken>();
			oldTokenBeansSet.addAll(oldTokenBeans);
			Set<SecToken> delTokens = new HashSet<SecToken>();
			if (oldTokenBeans.size() > 4) {
				SecToken olderToken = oldTokenBeansSet.stream()
						.min((token1, token2) -> token1.getExpirationDate().compareTo(token2.getExpirationDate())).get();
				delTokens.add(olderToken);
				oldTokenBeansSet.remove(olderToken);
			}
			List<SecToken> del = oldTokenBeansSet.stream()
					.filter((s) -> System.currentTimeMillis() >= s.getExpirationDate().getTime()).collect(Collectors.toList());
			if (del != null) {
				delTokens.addAll(del);
			}
			if (delTokens != null && !delTokens.isEmpty()) {
				tokenRepository.delete(delTokens);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.softshaper.security.token.storage.TokenStorage#removeToken(ru.softshaper.security.
	 * token.Token)
	 */
	@Override
	@CacheEvict(cacheNames = { "token" }, allEntries = true)
	public void removeToken(Token token) {
		tokenRepository.deleteById(token.getId());
	}
}
