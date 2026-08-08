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

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * Reactive JWT authentication filter that intercepts incoming WebFlux
 * requests and delegates them to a {@link ReactiveAuthenticationManager}.
 *
 * <p>This filter is intentionally minimal: it extends Spring Security's
 * {@link AuthenticationWebFilter} so that it inherits the framework's
 * standard conversion between HTTP authentication requests and
 * {@code Authentication} objects, while leaving the actual JWT parsing,
 * signature verification and principal resolution to a configurable
 * {@code ReactiveAuthenticationManager} supplied at construction time.</p>
 *
 * <p>The filter is wired up by the surrounding application &mdash; typically
 * inside a {@code SecurityWebFilterChain} bean &mdash; and once registered
 * it will:</p>
 * <ol>
 *   <li>convert each inbound exchange into an authentication request,</li>
 *   <li>delegate to the configured manager,</li>
 *   <li>populate the reactive {@code SecurityContext} on success.</li>
 * </ol>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AuthenticationWebFilter
 * @see ReactiveAuthenticationManager
 */
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

	/**
	 * Constructs a new JWT authentication filter that delegates to the
	 * supplied {@link ReactiveAuthenticationManager}.
	 *
	 * @param authenticationManager the reactive manager responsible for
	 *                              validating the JWT and resolving the
	 *                              principal; must not be {@code null}.
	 */
	public JwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

}