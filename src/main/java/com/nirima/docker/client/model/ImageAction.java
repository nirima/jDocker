package com.nirima.docker.client.model;

import java.io.Serializable;

/**
 * Actions (e.g: from 'remove an image' API)
 */
public class ImageAction implements Serializable {
    public static enum ActionType {
        Untagged,
        Deleted
    }

    public ImageAction(ActionType actionType, String imageId) {
        this.actionType = actionType;
        this.imageId = imageId;
    }

    public final ActionType actionType;
    public final String imageId;

}
