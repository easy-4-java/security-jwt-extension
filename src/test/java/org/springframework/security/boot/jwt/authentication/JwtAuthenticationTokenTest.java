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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link JwtAuthenticationToken}.
 *
 * <p>Verifies the public contract of the token, including:</p>
 * <ul>
 *   <li>initial unauthenticated/trusted state produced by each constructor,</li>
 *   <li>principal/credentials accessor behaviour,</li>
 *   <li>the immutable-authenticated rule,</li>
 *   <li>credential erasure, and</li>
 *   <li>the optional request-side hint accessors (sign, longitude, latitude).</li>
 * </ul>
 *
 * @since 3.0.0
 */
class JwtAuthenticationTokenTest {

    private static final String USERNAME = "alice";
    private static final String TOKEN = "jwt-bearer-string";

    /**
     * The unauthenticated constructor must expose the principal/credentials
     * it received and report {@code isAuthenticated() == false}.
     */
    @Test
    void shouldExposePrincipalAndCredentialsAndRemainUnauthenticated() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);

        assertSame(USERNAME, token.getPrincipal(), "principal must be returned as-is");
        assertSame(TOKEN, token.getCredentials(), "credentials must be returned as-is");
        assertFalse(token.isAuthenticated(), "two-arg constructor must not mark the token as trusted");
        assertTrue(token.getAuthorities().isEmpty(), "no authorities supplied, so the list must be empty");
    }

    /**
     * The trusted constructor must mark the token as authenticated and
     * propagate any supplied authorities.
     */
    @Test
    void shouldMarkAsAuthenticatedWhenAuthoritiesAreProvided() {
        List<SimpleGrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN, authorities);

        assertTrue(token.isAuthenticated(), "three-arg constructor must mark the token as trusted");
        assertSame(USERNAME, token.getPrincipal());
        assertSame(TOKEN, token.getCredentials());
        assertEquals(authorities, token.getAuthorities());
    }

    /**
     * Calling {@code setAuthenticated(true)} must always fail &mdash; the
     * only way to mark the token as trusted is the three-arg constructor.
     */
    @Test
    void shouldRejectSetAuthenticatedTrue() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> token.setAuthenticated(true));
        assertTrue(ex.getMessage().contains("Cannot set this token to trusted"));
    }

    /**
     * Calling {@code setAuthenticated(false)} is allowed but the token
     * stays unauthenticated &mdash; effectively a no-op after the fact.
     */
    @Test
    void shouldAllowSetAuthenticatedFalseAsNoOp() {
        JwtAuthenticationToken trusted = new JwtAuthenticationToken(
                USERNAME, TOKEN,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        trusted.setAuthenticated(false);

        assertFalse(trusted.isAuthenticated(), "setAuthenticated(false) must reset trust");
    }

    /**
     * {@link JwtAuthenticationToken#eraseCredentials()} must null out the
     * credentials while leaving the principal untouched.
     */
    @Test
    void shouldEraseCredentialsButRetainPrincipal() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);

        token.eraseCredentials();

        assertNull(token.getCredentials(), "credentials must be erased");
        assertSame(USERNAME, token.getPrincipal(), "principal must survive credential erasure");
    }

    /**
     * The optional sign accessors must round-trip the value provided by the
     * caller and default to {@code null}.
     */
    @Test
    void shouldRoundTripSignAccessor() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);
        assertNull(token.getSign(), "default sign value must be null");

        token.setSign("sig-value");
        assertEquals("sig-value", token.getSign());
    }

    /**
     * Longitude accessor must default to {@code 0.0} and round-trip
     * arbitrary values.
     */
    @Test
    void shouldRoundTripLongitudeAccessor() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);
        assertEquals(0.0d, token.getLongitude(), "default longitude must be 0.0");

        token.setLongitude(116.404d);
        assertEquals(116.404d, token.getLongitude(), 1.0e-9);
    }

    /**
     * Latitude accessor must default to {@code 0.0} and round-trip
     * arbitrary values, including negative ones.
     */
    @Test
    void shouldRoundTripLatitudeAccessor() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, TOKEN);
        assertEquals(0.0d, token.getLatitude(), "default latitude must be 0.0");

        token.setLatitude(-39.9042d);
        assertEquals(-39.9042d, token.getLatitude(), 1.0e-9);
    }

    /**
     * Sanity check: an empty authority list is acceptable and results in
     * a trusted token with no authorities.
     */
    @Test
    void shouldAcceptEmptyAuthoritiesCollection() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(
                USERNAME, TOKEN, Collections.<GrantedAuthority>emptyList());

        assertTrue(token.isAuthenticated());
        assertTrue(token.getAuthorities().isEmpty());
    }

    /**
     * The two-argument constructor with {@code null} credentials must
     * still produce an unauthenticated token that exposes the same null
     * value through its accessor.
     */
    @Test
    void shouldTolerateNullCredentialsInUnauthenticatedConstructor() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(USERNAME, null);

        assertFalse(token.isAuthenticated());
        assertNull(token.getCredentials());
        assertSame(USERNAME, token.getPrincipal());
    }
}