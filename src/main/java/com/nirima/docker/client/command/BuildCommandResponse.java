package com.nirima.docker.client.command;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.nirima.docker.client.model.EventStreamItem;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Collection;

public class BuildCommandResponse implements Serializable {
    private final Collection<EventStreamItem> items;
    public BuildCommandResponse(Collection<EventStreamItem> items) {
        this.items = items;
    }

    public Optional<String> imageId() {

        for(EventStreamItem item : items ) {
            if (item.getStream() != null && item.getStream().contains("Successfully built")) {
                String id =  StringUtils.substringAfterLast(item.getStream(), "Successfully built ").trim();
                return Optional.of(id);
            }
        }

        return Optional.absent();
    }

    public Collection<EventStreamItem> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return Joiner.on("\n").join(items);
    }
}
