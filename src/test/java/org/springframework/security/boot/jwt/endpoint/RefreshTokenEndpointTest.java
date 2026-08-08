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
package org.springframework.security.boot.jwt.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RefreshTokenEndpoint}.
 *
 * <p>The endpoint is currently a {@link RestController} scaffold with no
 * HTTP mappings. The tests therefore verify that:</p>
 * <ul>
 *   <li>it can be instantiated via the default constructor,</li>
 *   <li>its declared annotations are the ones Spring component scanning
 *       expects ({@code @RestController}).</li>
 * </ul>
 *
 * <p>Once concrete handler methods are added, additional tests covering
 * request / response behaviour should be appended here.</p>
 *
 * @since 3.0.0
 */
class RefreshTokenEndpointTest {

    /**
     * The default constructor must produce a non-null instance that
     * Spring's component scanner can pick up.
     */
    @Test
    void shouldInstantiateViaDefaultConstructor() {
        RefreshTokenEndpoint endpoint = new RefreshTokenEndpoint();
        assertNotNull(endpoint);
    }

    /**
     * The class must carry the {@link RestController} stereotype so that
     * the surrounding Spring Boot application registers it.
     */
    @Test
    void shouldCarryRestControllerAnnotation() {
        assertTrue(
                RefreshTokenEndpoint.class.isAnnotationPresent(RestController.class),
                "RefreshTokenEndpoint must be annotated with @RestController");
    }

    /**
     * Construction must be repeatable so that multiple endpoint instances
     * can be created (for example during integration-test scaffolding).
     */
    @Test
    void shouldBeRepeatablyConstructible() {
        RefreshTokenEndpoint first = new RefreshTokenEndpoint();
        RefreshTokenEndpoint second = new RefreshTokenEndpoint();

        assertNotNull(first);
        assertNotNull(second);
    }
}