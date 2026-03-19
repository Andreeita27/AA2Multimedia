package com.svalero.com.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Score {
    private final String name;
    private final int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
