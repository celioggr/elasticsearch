/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest;

import org.elasticsearch.ingest.processor.RemoveProcessor;
import org.hamcrest.CoreMatchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IngestMustacheRemoveProcessorIT extends AbstractMustacheTests {

    public void testRemoveProcessorMustacheExpression() throws Exception {
        RemoveProcessor.Factory factory = new RemoveProcessor.Factory(templateService);
        Map<String, Object> config = new HashMap<>();
        config.put("field", "field{{var}}");
        RemoveProcessor processor = factory.create(config);
        assertThat(processor.getField().execute(Collections.singletonMap("var", "_value")), CoreMatchers.equalTo("field_value"));
    }
}
