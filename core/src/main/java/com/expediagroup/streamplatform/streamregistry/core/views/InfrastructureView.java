/**
 * Copyright (C) 2018-2024 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.streamplatform.streamregistry.core.views;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.expediagroup.streamplatform.streamregistry.model.Infrastructure;
import com.expediagroup.streamplatform.streamregistry.model.keys.InfrastructureKey;
import com.expediagroup.streamplatform.streamregistry.repository.InfrastructureRepository;

@Component
@RequiredArgsConstructor
public class InfrastructureView {
  private final InfrastructureRepository infrastructureRepository;

  public Optional<Infrastructure> get(InfrastructureKey key) {
    return infrastructureRepository.findById(key);
  }

  public Stream<Infrastructure> findAll(Predicate<Infrastructure> filter) {
    return infrastructureRepository.findAll().stream().filter(filter);
  }

  public boolean exists(InfrastructureKey key) {
    return get(key).isPresent();
  }
}
