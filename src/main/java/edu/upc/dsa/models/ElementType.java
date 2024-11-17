package edu.upc.dsa.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ElementType {
    DOOR, WALL, BRIDGE, POTION, SWORD, COIN, GRASS, TREE;

    @JsonCreator
    public static ElementType fromString(String key) {
        return key == null ? null : ElementType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
