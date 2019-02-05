/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.fuse.boosters.rest.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.builder.RouteBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ElasticsearchSearchRequestTest extends ElasticsearchBaseTest {

    @Test
    public void testBulkIndex() throws Exception {
        List<Map<String, String>> documents = new ArrayList<>();
        Map<String, String> document1 = createIndexedData("1");
        Map<String, String> document2 = createIndexedData("2");

        documents.add(document1);
        documents.add(document2);

        List<?> indexIds = template.requestBody("direct:bulk_index", documents, List.class);
        assertNotNull("indexIds should be set", indexIds);
        assertCollectionSize("Indexed documents should match the size of documents", indexIds, documents.size());
    }

    @Test
    public void bulkIndexListRequestBody() throws Exception {
        String prefix = createPrefix();

        // given
        List<Map<String, String>> request = new ArrayList<>();
        final HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put("id", prefix + "baz");
        valueMap.put("content", prefix + "hello");
        request.add(valueMap);
        // when
        @SuppressWarnings("unchecked")
        List<String> indexedDocumentIds = template.requestBody("direct:bulk_index", request, List.class);

        // then
        assertThat(indexedDocumentIds, notNullValue());
        assertThat(indexedDocumentIds.size(), equalTo(1));
    }

    @Test
    public void tesSearchIndex() throws Exception {

        // given
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        // when
        SearchHits response = (SearchHits) template.requestBody("direct:searchRequest", request);

        // then
        assertThat(response, notNullValue());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {

                from("direct:bulk_index").routeId("bulk_index")
                        .to("elasticsearch-rest://elasticsearch?operation=BulkIndex&indexName=twitter&indexType=tweet");

                from("direct:searchRequest").routeId("searchRequest")
                        .to("elasticsearch-rest://elasticsearch?operation=Search");
            }
        };
    }
}
