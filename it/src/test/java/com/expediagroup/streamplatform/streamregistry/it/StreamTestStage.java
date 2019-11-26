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
package com.expediagroup.streamplatform.streamregistry.it;

import static com.expediagroup.streamplatform.streamregistry.core.handlers.IdentityHandler.DEFAULT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.expediagroup.streamplatform.streamregistry.graphql.client.InsertStreamMutation;
import com.expediagroup.streamplatform.streamregistry.graphql.client.StreamQuery;
import com.expediagroup.streamplatform.streamregistry.graphql.client.StreamsQuery;
import com.expediagroup.streamplatform.streamregistry.graphql.client.UpdateStreamMutation;
import com.expediagroup.streamplatform.streamregistry.graphql.client.UpdateStreamStatusMutation;
import com.expediagroup.streamplatform.streamregistry.graphql.client.UpsertStreamMutation;
import com.expediagroup.streamplatform.streamregistry.graphql.client.type.StreamKeyInput;
import com.expediagroup.streamplatform.streamregistry.graphql.client.type.StreamKeyQuery;
import com.expediagroup.streamplatform.streamregistry.it.helpers.AbstractTestStage;

public class StreamTestStage extends AbstractTestStage {

  @Override
  public void create() {
    Object data = client.getData(factory.insertStreamMutationBuilder().build());

    InsertStreamMutation.Insert Insert = ((InsertStreamMutation.Data) data).getStream().getInsert();

    assertThat(Insert.getSpecification().getDescription().get(), is(factory.description));
  }

  @Override
  public void update() {
    Object data = client.getData(factory.updateStreamMutationBuilder().build());

    UpdateStreamMutation.Update Update = ((UpdateStreamMutation.Data) data).getStream().getUpdate();

    assertThat(Update.getSpecification().getDescription().get(), is(factory.description));
  }

  @Override
  public void upsert() {
    Object data = client.getData(factory.upsertStreamMutationBuilder().build());

    UpsertStreamMutation.Upsert upsert = ((UpsertStreamMutation.Data) data).getStream().getUpsert();

    assertThat(upsert.getSpecification().getDescription().get(), is(factory.description));
  }

  @Override
  public void updateStatus() {
    client.getData(factory.upsertStreamMutationBuilder().build());
    Object data = client.getData(factory.updateStreamStatusBuilder().build());

    UpdateStreamStatusMutation.UpdateStatus update =
        ((UpdateStreamStatusMutation.Data) data).getStream().getUpdateStatus();

    assertThat(update.getSpecification().getDescription().get(), is(factory.description));
    assertThat(update.getStatus().get().getAgentStatus().get("skey").asText(), is("svalue"));
  }

  @Override
  public void queryByKey() {

    StreamKeyInput input = factory.streamKeyInputBuilder().build();

    try {
      client.getData(StreamQuery.builder().key(input).build());
    } catch (RuntimeException e) {
      assertEquals(e.getMessage(), "No value present");
    }

    client.getData(factory.upsertStreamMutationBuilder().build());

    StreamQuery.Data after = (StreamQuery.Data) client.getData(StreamQuery.builder().key(input).build());

    assertEquals(after.getStream().getByKey().getKey().getName(), input.name());

    assertEquals(after.getStream().getByKey().getSchema().getSpecification().getType(), DEFAULT);

  }

  @Override
  public void queryByRegex() {

    setFactorySuffix("query_by_regex");

    StreamKeyQuery query = StreamKeyQuery.builder().nameRegex("stream_name.*").build();

    StreamsQuery.Data before = (StreamsQuery.Data) client.getData(StreamsQuery.builder().key(query).build());

    client.invoke(factory.upsertStreamMutationBuilder().build());

    StreamsQuery.Data after = (StreamsQuery.Data) client.getData(StreamsQuery.builder().key(query).build());

    assertEquals(after.getStream().getByQuery().size(), before.getStream().getByQuery().size() + 1);
  }

  @Override
  public void createRequiredDatastoreState() {
    client.createSchema(factory);
  }
}