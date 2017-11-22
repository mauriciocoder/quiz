package com.bon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class OAuth2ServerConfiguration {
    @Bean
    public JwtAccessTokenConverter getTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        //  for asymmetric signing/verification use
        //  tokenConverter.setKeyPair(...);
        tokenConverter.setSigningKey("aTokenSigningKey");
        tokenConverter.setVerifierKey("aTokenSigningKey");
        return tokenConverter;
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/user/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/user/**").access("#oauth2.hasScope('read')");
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends
            AuthorizationServerConfigurerAdapter {

        @Override
        public void configure(ClientDetailsServiceConfigurer clients)
                throws Exception {
            String RESOURCE_ID = "RESOURCE_ID"; // TODO: check if that makes sense!!!
            clients
                    .inMemory()
                    .withClient("aClient")
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("USER")
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID)
                    .secret("aSecret");
        }
    }

}