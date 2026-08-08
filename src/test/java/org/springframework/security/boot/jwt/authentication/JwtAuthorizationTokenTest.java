/*
 * Copyright (c) 2018-present, easy-4-java (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.boot.jwt.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link JwtAuthorizationToken}.
 *
 * <p>The class exercises the public contract of the authorization-tier
 * token: its two construction paths, the immutable-trusted rule, the
 * credential-erasure behaviour and the optional sign / longitude / latitude
 * accessors.</p>
 *
 * @since 3.0.0
 */
class JwtAuthorizationTokenTest {

    private static final String PRINCIPAL = "user-42";
    private static final String CREDENTIALS = "raw-jwt";

    /**
     * The two-arg constructor must produce an unauthenticated token that
     * still exposes the credentials that were supplied.
     */
    @Test
    void shouldExposePrincipalAndCredentialsAndRemainUnauthenticated() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);

        assertSame(PRINCIPAL, token.getPrincipal());
        assertSame(CREDENTIALS, token.getCredentials(), "credentials must survive two-arg construction");
        assertFalse(token.isAuthenticated(), "two-arg constructor must not mark token as trusted");
    }

    /**
     * The three-arg constructor must mark the token as trusted and
     * propagate the supplied authorities, while still allowing the caller
     * to read back the principal.
     */
    @Test
    void shouldMarkAsAuthenticatedWhenAuthoritiesAreProvided() {
        List<SimpleGrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        JwtAuthorizationToken token =
                new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS, authorities);

        assertTrue(token.isAuthenticated(), "three-arg constructor must mark token as trusted");
        assertSame(PRINCIPAL, token.getPrincipal());
        assertEquals(authorities, token.getAuthorities());
    }

    /**
     * Calling {@code setAuthenticated(true)} is rejected by the override.
     */
    @Test
    void shouldRejectSetAuthenticatedTrue() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> token.setAuthenticated(true));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("Cannot set this token to trusted"));
    }

    /**
     * {@code setAuthenticated(false)} is permitted and demotes a trusted
     * token to an unauthenticated state.
     */
    @Test
    void shouldAllowSetAuthenticatedFalse() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(
                PRINCIPAL, CREDENTIALS,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        token.setAuthenticated(false);

        assertFalse(token.isAuthenticated());
    }

    /**
     * Explicit {@link JwtAuthorizationToken#eraseCredentials()} must
     * null out the credentials while leaving the principal intact.
     */
    @Test
    void shouldEraseCredentialsButRetainPrincipal() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);

        token.eraseCredentials();

        assertNull(token.getCredentials(), "credentials must be erased");
        assertSame(PRINCIPAL, token.getPrincipal(), "principal must survive erasure");
    }

    /**
     * Sign accessor must default to {@code null} and round-trip values.
     */
    @Test
    void shouldRoundTripSignAccessor() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);
        assertNull(token.getSign());

        token.setSign("abc-123");
        assertEquals("abc-123", token.getSign());
    }

    /**
     * Longitude accessor must default to {@code 0.0} and round-trip
     * values, including out-of-range values that the class itself accepts.
     */
    @Test
    void shouldRoundTripLongitudeAccessor() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);
        assertEquals(0.0d, token.getLongitude());

        token.setLongitude(121.4737d);
        assertEquals(121.4737d, token.getLongitude(), 1.0e-9);
    }

    /**
     * Latitude accessor must default to {@code 0.0} and round-trip values.
     */
    @Test
    void shouldRoundTripLatitudeAccessor() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(PRINCIPAL, CREDENTIALS);
        assertEquals(0.0d, token.getLatitude());

        token.setLatitude(31.2304d);
        assertEquals(31.2304d, token.getLatitude(), 1.0e-9);
    }

    /**
     * The trusted constructor should preserve the principal even when
     * {@code credentials} are passed as {@code null}.
     */
    @Test
    void shouldAcceptNullCredentialsInTrustedConstructor() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(
                PRINCIPAL, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        assertTrue(token.isAuthenticated());
        assertSame(PRINCIPAL, token.getPrincipal());
        assertNull(token.getCredentials());
    }

    /**
     * An empty authority list is acceptable for the trusted constructor.
     */
    @Test
    void shouldAcceptEmptyAuthoritiesCollection() {
        JwtAuthorizationToken token = new JwtAuthorizationToken(
                PRINCIPAL, CREDENTIALS, Collections.emptyList());

        assertTrue(token.isAuthenticated());
        assertNotNull(token.getAuthorities());
        assertTrue(token.getAuthorities().isEmpty());
    }
}