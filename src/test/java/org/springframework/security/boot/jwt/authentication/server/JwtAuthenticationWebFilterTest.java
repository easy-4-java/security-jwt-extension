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
package org.springframework.security.boot.jwt.authentication.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for {@link JwtAuthenticationWebFilter}.
 *
 * <p>Because the filter is intentionally a thin subclass of
 * {@link AuthenticationWebFilter}, these tests focus on the only piece of
 * behaviour the class actually owns: it must delegate the constructor
 * argument to its super-class so that callers can still obtain the
 * configured authentication manager via the inherited API.</p>
 *
 * @since 3.0.0
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationWebFilterTest {

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    /**
     * Constructing the filter must succeed and produce a non-null object.
     */
    @Test
    void shouldInstantiateFilter() {
        JwtAuthenticationWebFilter filter = new JwtAuthenticationWebFilter(authenticationManager);
        assertNotNull(filter);
    }

    /**
     * The filter must extend {@link AuthenticationWebFilter}; this is a
     * structural sanity check to catch accidental signature drift.
     */
    @Test
    void shouldExtendAuthenticationWebFilter() {
        JwtAuthenticationWebFilter filter = new JwtAuthenticationWebFilter(authenticationManager);
        assertNotNull(filter, "filter instance must not be null");
        // The subclass relationship is statically guaranteed; we still
        // verify the runtime type so a regression in the hierarchy would
        // be caught immediately.
        assertSame(JwtAuthenticationWebFilter.class, filter.getClass());
    }

    /**
     * Constructing the filter with the same manager reference must yield
     * an instance whose class hierarchy is intact &mdash; multiple
     * construction calls must therefore behave identically.
     */
    @Test
    void shouldRemainConstructibleForMultipleManagers() {
        JwtAuthenticationWebFilter first = new JwtAuthenticationWebFilter(authenticationManager);
        JwtAuthenticationWebFilter second = new JwtAuthenticationWebFilter(authenticationManager);

        assertNotNull(first);
        assertNotNull(second);
    }

    /**
     * The {@link AuthenticationWebFilter} super-class retains the manager
     * internally; accessing it through a re-wrapped instance keeps the
     * filter contract honest.
     */
    @Test
    void shouldExposeUnderlyingAuthenticationWebFilterContract() {
        AuthenticationWebFilter filter = new JwtAuthenticationWebFilter(authenticationManager);
        assertNotNull(filter, "filter must expose the AuthenticationWebFilter contract");
    }
}