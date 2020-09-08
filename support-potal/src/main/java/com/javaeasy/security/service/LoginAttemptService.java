package com.javaeasy.security.service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/*
 * Basically every login failure attempt will put into in Cache . Maximium 5 failure attempt can be done.
 * More than 5 Account will get locked
 * Cache will refresh at every 15 minutes.
 * 
 *  Once successfully logged into the application , then user will remove from the cache
 * 
 */
@Service
public class LoginAttemptService {

	private static final int MAXIMIUM_ATTEMPT = 5;
	private static final int INCREMENT_ATTEMPT = 1;

	private LoadingCache<String, Integer> cache;

	public LoginAttemptService() {
		cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(15)).maximumSize(100)
				.build(new CacheLoader<String, Integer>() {

					@Override
					public Integer load(String key) throws Exception {
						return Integer.valueOf(0);
					}

				});
	}

	public void evictUserfromLoginAttemptCache(String userName) {
		cache.invalidate(userName);
	}

	public void addUserIntoLoginAttemptCache(String userName) {

		Integer noOfAttempts = Integer.valueOf(0);

		try {
			noOfAttempts = cache.get(userName) + INCREMENT_ATTEMPT;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		cache.put(userName, noOfAttempts);
	}

	public boolean hasExceededMaximiumLoginAttempt(String userName) {
		try {
			return cache.get(userName) >= MAXIMIUM_ATTEMPT;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

}
