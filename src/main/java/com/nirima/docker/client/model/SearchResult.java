package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult implements Serializable {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResultItem implements Serializable
    {
        @JsonProperty("description")
        public String description;

        @JsonProperty("name")
        public String name;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("description", description)
                    .add("name", name)
                    .toString();
        }
    }

    @JsonProperty("num_results")
    public int numResults;

    @JsonProperty("query")
    public String query;

    @JsonProperty("results")
    public List<SearchResultItem> results;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("query", query)
                .add("results", results)
                .toString();
    }
}
