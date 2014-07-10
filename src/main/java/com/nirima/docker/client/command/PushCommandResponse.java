package com.nirima.docker.client.command;

import com.google.common.base.Joiner;
import com.nirima.docker.client.model.EventStreamItem;
import com.nirima.docker.client.model.PushEventStreamItem;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by magnayn on 10/07/2014.
 */
public class PushCommandResponse implements Serializable {
    private final Collection<PushEventStreamItem> items;
    public PushCommandResponse(Collection<PushEventStreamItem> items) {
        this.items = items;
    }

    public Collection<PushEventStreamItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return Joiner.on("\n").join(items);
    }
}
