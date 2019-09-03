/**
 * Copyright (C) 2018-2019 Expedia, Inc.
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
package com.expediagroup.streamplatform.streamregistry.repository.avro;

import org.springframework.stereotype.Component;

import com.expediagroup.streamplatform.streamregistry.model.Domain;

@Component
public class AvroDomainConversion implements Conversion<Domain, Domain.Key, AvroDomain> {
  public static AvroKey avroKey(Domain.Key key) {
    return AvroKey
        .newBuilder()
        .setId(key.getName())
        .setType(AvroKeyType.DOMAIN)
        .setParent(null)
        .build();
  }

  public static Domain.Key modelKey(AvroKey key) {
    return new Domain.Key(key.getId());
  }

  @Override
  public AvroKey key(Domain.Key key) {
    return avroKey(key);
  }

  @Override
  public Class<AvroDomain> avroClass() {
    return AvroDomain.class;
  }

  @Override
  public Class<Domain> entityClass() {
    return Domain.class;
  }

  @Override
  public AvroKeyType keyType() {
    return AvroKeyType.DOMAIN;
  }
}
