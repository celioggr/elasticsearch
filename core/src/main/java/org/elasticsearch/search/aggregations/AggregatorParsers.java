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
package org.elasticsearch.search.aggregations;

import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.unmodifiableMap;

/**
 * A registry for all the aggregator parser, also servers as the main parser for the aggregations module
 */
public class AggregatorParsers {
    public static final Pattern VALID_AGG_NAME = Pattern.compile("[^\\[\\]>]+");

    private final Map<String, Aggregator.Parser> aggParsers;
    private final Map<String, PipelineAggregator.Parser> pipelineAggregatorParsers;


    /**
     * Constructs the AggregatorParsers out of all the given parsers
     *
     * @param aggParsers
     *            The available aggregator parsers (dynamically injected by the
     *            {@link org.elasticsearch.search.SearchModule}
     *            ).
     */
    @Inject
    public AggregatorParsers(Set<Aggregator.Parser> aggParsers, Set<PipelineAggregator.Parser> pipelineAggregatorParsers,
            NamedWriteableRegistry namedWriteableRegistry) {
        Map<String, Aggregator.Parser> aggParsersBuilder = new HashMap<>(aggParsers.size());
        for (Aggregator.Parser parser : aggParsers) {
            aggParsersBuilder.put(parser.type(), parser);
            AggregatorBuilder<?> factoryPrototype = parser.getFactoryPrototypes();
            namedWriteableRegistry.registerPrototype(AggregatorBuilder.class, factoryPrototype);
        }
        this.aggParsers = unmodifiableMap(aggParsersBuilder);
        Map<String, PipelineAggregator.Parser> pipelineAggregatorParsersBuilder = new HashMap<>(pipelineAggregatorParsers.size());
        for (PipelineAggregator.Parser parser : pipelineAggregatorParsers) {
            pipelineAggregatorParsersBuilder.put(parser.type(), parser);
            PipelineAggregatorBuilder<?> factoryPrototype = parser.getFactoryPrototype();
            namedWriteableRegistry.registerPrototype(PipelineAggregatorBuilder.class, factoryPrototype);
        }
        this.pipelineAggregatorParsers = unmodifiableMap(pipelineAggregatorParsersBuilder);
    }

    /**
     * Returns the parser that is registered under the given aggregation type.
     *
     * @param type  The aggregation type
     * @return      The parser associated with the given aggregation type.
     */
    public Aggregator.Parser parser(String type) {
        return aggParsers.get(type);
    }

    /**
     * Returns the parser that is registered under the given pipeline aggregator
     * type.
     *
     * @param type
     *            The pipeline aggregator type
     * @return The parser associated with the given pipeline aggregator type.
     */
    public PipelineAggregator.Parser pipelineAggregator(String type) {
        return pipelineAggregatorParsers.get(type);
    }

    /**
     * Parses the aggregation request recursively generating aggregator factories in turn.
     *
     * @param parser    The input xcontent that will be parsed.
     * @param parseContext   The parse context.
     *
     * @return          The parsed aggregator factories.
     *
     * @throws IOException When parsing fails for unknown reasons.
     */
    public AggregatorFactories.Builder parseAggregators(XContentParser parser, QueryParseContext parseContext) throws IOException {
        return parseAggregators(parser, parseContext, 0);
    }


    private AggregatorFactories.Builder parseAggregators(XContentParser parser, QueryParseContext parseContext, int level)
            throws IOException {
        Matcher validAggMatcher = VALID_AGG_NAME.matcher("");
        AggregatorFactories.Builder factories = new AggregatorFactories.Builder();

        XContentParser.Token token = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token != XContentParser.Token.FIELD_NAME) {
                throw new ParsingException(parser.getTokenLocation(),
                        "Unexpected token " + token + " in [aggs]: aggregations definitions must start with the name of the aggregation.");
            }
            final String aggregationName = parser.currentName();
            if (!validAggMatcher.reset(aggregationName).matches()) {
                throw new ParsingException(parser.getTokenLocation(), "Invalid aggregation name [" + aggregationName
                        + "]. Aggregation names must be alpha-numeric and can only contain '_' and '-'");
            }

            token = parser.nextToken();
            if (token != XContentParser.Token.START_OBJECT) {
                throw new ParsingException(parser.getTokenLocation(), "Aggregation definition for [" + aggregationName + " starts with a ["
                        + token + "], expected a [" + XContentParser.Token.START_OBJECT + "].");
            }

            AggregatorBuilder<?> aggFactory = null;
            PipelineAggregatorBuilder<?> pipelineAggregatorFactory = null;
            AggregatorFactories.Builder subFactories = null;

            Map<String, Object> metaData = null;

            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token != XContentParser.Token.FIELD_NAME) {
                    throw new ParsingException(
                            parser.getTokenLocation(), "Expected [" + XContentParser.Token.FIELD_NAME + "] under a ["
                            + XContentParser.Token.START_OBJECT + "], but got a [" + token + "] in [" + aggregationName + "]",
                            parser.getTokenLocation());
                }
                final String fieldName = parser.currentName();

                token = parser.nextToken();
                if ("aggregations_binary".equals(fieldName)) {
                    if (subFactories != null) {
                        throw new ParsingException(parser.getTokenLocation(),
                                "Found two sub aggregation definitions under [" + aggregationName + "]",
                                parser.getTokenLocation());
                    }
                    XContentParser binaryParser = null;
                    if (token == XContentParser.Token.VALUE_STRING || token == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
                        byte[] source = parser.binaryValue();
                        binaryParser = XContentFactory.xContent(source).createParser(source);
                    } else {
                        throw new ParsingException(parser.getTokenLocation(),
                                "Expected [" + XContentParser.Token.VALUE_STRING + " or " + XContentParser.Token.VALUE_EMBEDDED_OBJECT
                                        + "] for [" + fieldName + "], but got a [" + token + "] in [" + aggregationName + "]");
                    }
                    XContentParser.Token binaryToken = binaryParser.nextToken();
                    if (binaryToken != XContentParser.Token.START_OBJECT) {
                        throw new ParsingException(parser.getTokenLocation(),
                                "Expected [" + XContentParser.Token.START_OBJECT + "] as first token when parsing [" + fieldName
                                        + "], but got a [" + binaryToken + "] in [" + aggregationName + "]");
                    }
                    subFactories = parseAggregators(binaryParser, parseContext, level + 1);
                } else if (token == XContentParser.Token.START_OBJECT) {
                    switch (fieldName) {
                    case "meta":
                        metaData = parser.map();
                        break;
                    case "aggregations":
                    case "aggs":
                        if (subFactories != null) {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "Found two sub aggregation definitions under [" + aggregationName + "]");
                        }
                        subFactories = parseAggregators(parser, parseContext, level + 1);
                        break;
                    default:
                        if (aggFactory != null) {
                            throw new ParsingException(parser.getTokenLocation(), "Found two aggregation type definitions in ["
                                    + aggregationName + "]: [" + aggFactory.type + "] and [" + fieldName + "]");
                        }
                        if (pipelineAggregatorFactory != null) {
                            throw new ParsingException(parser.getTokenLocation(), "Found two aggregation type definitions in ["
                                    + aggregationName + "]: [" + pipelineAggregatorFactory + "] and [" + fieldName + "]");
                        }

                        Aggregator.Parser aggregatorParser = parser(fieldName);
                        if (aggregatorParser == null) {
                            PipelineAggregator.Parser pipelineAggregatorParser = pipelineAggregator(fieldName);
                            if (pipelineAggregatorParser == null) {
                                throw new ParsingException(parser.getTokenLocation(),
                                        "Could not find aggregator type [" + fieldName + "] in [" + aggregationName + "]");
                            } else {
                                pipelineAggregatorFactory = pipelineAggregatorParser.parse(aggregationName, parser, parseContext);
                            }
                        } else {
                            aggFactory = aggregatorParser.parse(aggregationName, parser, parseContext);
                        }
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Expected [" + XContentParser.Token.START_OBJECT + "] under ["
                            + fieldName + "], but got a [" + token + "] in [" + aggregationName + "]");
                }
            }

            if (aggFactory == null && pipelineAggregatorFactory == null) {
                throw new ParsingException(parser.getTokenLocation(), "Missing definition for aggregation [" + aggregationName + "]",
                        parser.getTokenLocation());
            } else if (aggFactory != null) {
                assert pipelineAggregatorFactory == null;
            if (metaData != null) {
                    aggFactory.setMetaData(metaData);
            }

            if (subFactories != null) {
                    aggFactory.subAggregations(subFactories);
            }

                factories.addAggregator(aggFactory);
            } else {
                assert pipelineAggregatorFactory != null;
                if (subFactories != null) {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Aggregation [" + aggregationName + "] cannot define sub-aggregations",
                            parser.getTokenLocation());
                }
                if (metaData != null) {
                    pipelineAggregatorFactory.setMetaData(metaData);
                }
                factories.addPipelineAggregator(pipelineAggregatorFactory);
            }
        }

        return factories;
    }

}
