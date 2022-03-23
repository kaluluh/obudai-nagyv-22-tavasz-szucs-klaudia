package com.example.domain;

import lombok.Getter;

import java.util.Arrays;

public enum MenuItem {
    LISTALLGAMES(1),ADMYGAME(2),CREATEJOINREQUESTS(3),PROCESSINGMEMBERSHIP(4),QUIT(5),CREATEGAME(8);

    @Getter
    private int id;

    MenuItem(int id) {
        this.id = id;
    }

    public static MenuItem from(int id) throws IllegalArgumentException {
        return Arrays.stream(MenuItem.values())
                .filter(i -> i.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Error creating MenuItem from id: " + id));
    }
}
