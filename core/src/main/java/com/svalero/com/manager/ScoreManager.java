package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.domain.Score;

import java.util.Comparator;

public class ScoreManager {

    private static final String PREFS_NAME = "adventurer_scores";
    private static final String KEY_COUNT = "count";
    private static final int MAX_SCORES = 10;

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static void saveScore(String name, int score) {
        Preferences prefs = getPrefs();
        Array<Score> scores = loadScores();

        if (name == null || name.trim().isEmpty()) {
            name = "Jugador";
        }

        scores.add(new Score(name.trim(), score));
        scores.sort(Comparator.comparingInt(Score::getScore).reversed());

        while (scores.size > MAX_SCORES) {
            scores.pop();
        }

        prefs.clear();
        prefs.putInteger(KEY_COUNT, scores.size);

        for (int i = 0; i < scores.size; i++) {
            prefs.putString("name_" + i, scores.get(i).getName());
            prefs.putInteger("score_" + i, scores.get(i).getScore());
        }

        prefs.flush();
    }

    public static Array<Score> loadScores() {
        Preferences prefs = getPrefs();
        int count = prefs.getInteger(KEY_COUNT, 0);

        Array<Score> scores = new Array<>();

        for (int i = 0; i < count; i++) {
            String name = prefs.getString("name_" + i, "Jugador");
            int score = prefs.getInteger("score_" + i, 0);
            scores.add(new Score(name, score));
        }

        scores.sort(Comparator.comparingInt(Score::getScore).reversed());
        return scores;
    }
}
