package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.domain.Score;

import java.util.Comparator;

public class ScoreManager {

    // Nombre del archivo donde se guardan las puntuaciones
    private static final String PREFS_NAME = "adventurer_scores";
    // Clave que almacena cuantas puntuaciones hay guardadas
    private static final String KEY_COUNT = "count";
    // Numero max de puntuaciones que se conservan
    private static final int MAX_SCORES = 10;

    //Devuelve el sistema de preferencias de libgdx, guarda datos persistentes de manera simple
    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    // Guarda una puntuacion nueva en el ranking
    public static void saveScore(String name, int score) {
        Preferences prefs = getPrefs();
        // Carga las puntuaciones ya existentes
        Array<Score> scores = loadScores();

        // Si el jugador no pone nombre, se pone este por defecto
        if (name == null || name.trim().isEmpty()) {
            name = "Jugador";
        }

        // Añade la nueva puntuacion
        scores.add(new Score(name.trim(), score));
        // Ordena el ranking de mayor a menor
        scores.sort(Comparator.comparingInt(Score::getScore).reversed());

        // Mantiene unicamente las 10 mejores
        while (scores.size > MAX_SCORES) {
            scores.pop();
        }

        // Limpia los datos antiguos antes de guardar el ranking actualizado
        prefs.clear();
        // Guarda cuantas puntuaciones existen
        prefs.putInteger(KEY_COUNT, scores.size);

        // Guarda nombre y puntuacion del jugador
        for (int i = 0; i < scores.size; i++) {
            prefs.putString("name_" + i, scores.get(i).getName());
            prefs.putInteger("score_" + i, scores.get(i).getScore());
        }

        // Guardado definitivo
        prefs.flush();
    }

    // Carga las puntuaciones guardadas
    public static Array<Score> loadScores() {
        Preferences prefs = getPrefs();
        // Recupera cuantas puntuaciones hay almacenadas
        int count = prefs.getInteger(KEY_COUNT, 0);

        Array<Score> scores = new Array<>();

        // Reconstruye el ranking leyendo cada nombre y puntuacion
        for (int i = 0; i < count; i++) {
            String name = prefs.getString("name_" + i, "Jugador");
            int score = prefs.getInteger("score_" + i, 0);
            scores.add(new Score(name, score));
        }

        // Ordena de nuevo por si acaso
        scores.sort(Comparator.comparingInt(Score::getScore).reversed());
        return scores;
    }
}
