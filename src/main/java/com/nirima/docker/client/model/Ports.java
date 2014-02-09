package com.nirima.docker.client.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ben on 16/12/13.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(using=Ports.Deserializer.class)
public class Ports {
    private final Map<String, Port> ports = new HashMap<String, Port>();
    private Port[] mapping;

    private Ports() { }

    private void addPort(Port port) {
        ports.put(port.getPort(), port);
    }

    private void addMapping(Port src, Port target) {
        Port p = ports.get(src);
        if (p==null) return;
//        p.addMapping(target);
    }

    public static class Port{
        private final String scheme;
        private final String port;

        public Port(String scheme_, String port_) {
            scheme = scheme_;
            port = port_;
        }

        public String getScheme() {
            return scheme;
        }

        public String getPort() {
            return port;
        }

        public static Port makePort(String full) {
            if (full == null) return null;
            String[] pieces = full.split("/");
            return new Port(pieces[1], pieces[0]);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("scheme", scheme)
                    .add("port", port)
                    .toString();
        }
    }

    public static class Deserializer extends JsonDeserializer<Ports> {
        @Override
        public Ports deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Ports out = new Ports();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<String> it = node.fieldNames(); it.hasNext();) {
                String pname = it.next();
                out.addPort(Port.makePort(pname));

            }
            return out;
        }

    }

}
