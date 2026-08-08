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

import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoint placeholder dedicated to issuing refreshed JSON Web Tokens
 * (JWTs).
 *
 * <p>This class is a deliberately minimal {@link RestController} scaffold:
 * it carries the {@code @RestController} stereotype so that Spring component
 * scanning picks it up, but it does not yet declare any HTTP mapping because
 * the refresh-token negotiation contract is still under design. Once the
 * contract is finalised, concrete handler methods (for example
 * {@code @PostMapping("/refresh")}) will be added here.</p>
 *
 * <p>Until then, the controller exists to:</p>
 * <ul>
 *   <li>reserve a stable bean name so other modules can inject it,</li>
 *   <li>document the intended location of the refresh-token endpoint, and</li>
 *   <li>keep the test suite honest by providing a concrete class to cover.</li>
 * </ul>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see RestController
 */
@RestController
public class RefreshTokenEndpoint {

	/**
	 * Default, no-argument constructor required by Spring's component
	 * scanner. Open to subclasses that want to add concrete endpoints
	 * later.
	 */
	public RefreshTokenEndpoint() {
	}

}