package com.svalero.com.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy {

    public enum EnemyType {
        FROG,
        BAT,
        MOUSE
    }

    private Texture texture;
    @Getter
    private Rectangle bounds;
    private float speed;
    // Limites del movimiento de los enemigos
    private float minX;
    private float maxX;
    private int direction;
    // Para saber si el enemigo sigue vivo
    @Getter
    private boolean alive;
    @Getter
    private EnemyType type;

    // Para recordar la posición inicial de la rana
    private float initialX;
    private float initialY;

    public Enemy(Texture texture, float x, float y, float width, float height, float speed, float minX, float maxX, EnemyType type) {
        this.texture = texture;
        //Crea el rectangulo que representa el enemigo
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        // Todos empiezan moviendose hacia la derecha o hacia arriba
        this.direction = 1;
        this.alive = true;
        this.type = type;

        // Se guarda la posicion inicial para la ia de la rana
        this.initialX = x;
        this.initialY = y;
    }

    public void update(float delta, float playerX) {

        // Si esta muerto no se actualiza
        if (!alive) {
            return;
        }

        switch (type) {

            case FROG:
                updateFrog(delta, playerX);
                break;

            case BAT:
            case MOUSE:
                updateHorizontalEnemy(delta);
                break;
        }
    }

    private void updateFrog(float delta, float playerX) {
        // Centro de la rana para calcular la distancia real al jugador
        float frogCenterX = bounds.x + bounds.width / 2f;
        // Distancia horizontal entre la rana y el jugador
        float distanceToPlayer = Math.abs(playerX - frogCenterX);

        // Distancia mayor para que empiece a reaccionar antes
        float activationDistance = 260f;

        // Si el jugador esta cerca, la rana se mueve
        if (distanceToPlayer <= activationDistance) {
            //Movimiento vertical
            bounds.y += speed * direction * delta;

            // Cambia la direccion al llegar arriba
            if (bounds.y >= maxX) {
                bounds.y = maxX;
                direction = -1;
            }

            // Cambia la direccion al llegar abajo
            if (bounds.y <= minX) {
                bounds.y = minX;
                direction = 1;
            }
        } else {
            // Si el jugador se aleja, la rana vuelve a su posición inicial
            if (bounds.y > initialY) {
                bounds.y -= speed * delta;

                if (bounds.y <= initialY) {
                    bounds.y = initialY;
                }
            } else if (bounds.y < initialY) {
                bounds.y += speed * delta;

                if (bounds.y >= initialY) {
                    bounds.y = initialY;
                }
            }

            // Cuando vuelve a la posición inicial, se queda preparada para saltar hacia arriba
            if (bounds.y == initialY) {
                direction = 1;
            }
        }
    }

    private void updateHorizontalEnemy(float delta) {
        // Movimiento horizontal continuo
        bounds.x += speed * direction * delta;

        // Rebota en el limite izquierdo
        if (bounds.x <= minX) {
            bounds.x = minX;
            direction = 1;
        }

        // Rebota en el limite derecho
        if (bounds.x + bounds.width >= maxX) {
            bounds.x = maxX - bounds.width;
            direction = -1;
        }
    }

    public void draw(SpriteBatch batch) {
        // Solo se dibujan enemigos vivos
        if (alive) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public void kill() {
        // Marca el enemigo como muerto, ni se dibuja ni se actualiza
        alive = false;
    }
}
