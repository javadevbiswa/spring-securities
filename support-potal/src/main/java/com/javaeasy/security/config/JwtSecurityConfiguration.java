package com.javaeasy.security.config;

import static com.javaeasy.security.constant.JwtSecurityConstants.PUBLIC_URLS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.javaeasy.security.filter.JwtAccessDeniedHandler;
import com.javaeasy.security.filter.JwtAuthenticationEntryPoint;
import com.javaeasy.security.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthorizationFilter authorizationFilter;
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private UserDetailsService userPrincipal;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userPrincipal).passwordEncoder(bCryptPasswordEncoder).and()
				.authenticationEventPublisher(authenticationEventPublisher());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated().and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(authenticationEntryPoint).and()
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
		return new DefaultAuthenticationEventPublisher();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

}
