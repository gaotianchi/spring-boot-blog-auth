package com.gaotianchi.auth.service.impl;

import com.gaotianchi.auth.dao.ClientDao;
import com.gaotianchi.auth.entity.Client;
import com.gaotianchi.auth.service.ClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Set;


/**
 * 客户端表(Client)表服务实现类
 *
 * @author gaotianchi
 * @since 2024-11-24 20:35:24
 */
@Service("clientService")
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        Assert.notNull(clientDao, "clientRepository cannot be null");
        this.clientDao = clientDao;
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);              // Custom authorization grant type
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);      // Custom client authentication method
    }

    public static RegisteredClient toRegisteredClient(Client client) {

        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                client.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
                client.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
                client.getScopes());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(
                client.getPostLogoutRedirectUris());

        return RegisteredClient.withId(client.getClientId())
                .id(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientName(client.getClientName())
                .clientIdIssuedAt(client.getClientIdIssuedAt().toInstant())
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(method ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(method))))
                .authorizationGrantTypes(grantTypes ->
                        authorizationGrantTypes.forEach(type ->
                                grantTypes.add(resolveAuthorizationGrantType(type))))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .scopes(scopes -> scopes.addAll(clientScopes))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(client.getRequireProofKey() == 1)
                        .requireAuthorizationConsent(client.getRequireAuthorizationConsent() == 1)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(client.getAccessTokenTimeLive()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(client.getRefreshTokenTimeToLive()))
                        .reuseRefreshTokens(client.getReuseRefreshTokens() == 1)
                        .build())
                .build();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientDao.selectClientById(Integer.valueOf(id));
        return toRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientDao.selectClientByClientIdOrClientName(clientId);
        return toRegisteredClient(client);
    }
}
