# Adventurer Platform

Videojuego 2D desarrollado con **Java** y **libGDX** para la asignatura de **Programación Multimedia y Dispositivos Móviles (PMDM)**.

## Descripción

Adventurer Platform es un videojuego de plataformas en 2D donde el jugador debe superar distintos niveles, recoger gemas y evitar enemigos hasta completar la aventura.

El proyecto ha sido desarrollado siguiendo el paradigma de **Programación Orientada a Objetos**, separando la lógica del juego, el renderizado, los niveles, los recursos y la interfaz en distintas clases especializadas.

---

# Características principales

- Personaje principal controlable
- Sistema de salto y gravedad
- 4 niveles diferentes
- Enemigos con comportamientos distintos
- Animaciones para jugador y NPCs
- Sistema de puntuación
- HUD con vidas, gemas, puntos y nivel actual
- Pantalla de Game Over
- Pantalla de victoria
- Menú principal
- Pantalla de instrucciones
- Configuración de música y sonido
- Menú de pausa durante la partida
- Ranking de puntuaciones persistente

---

# NPCs implementados

El juego incluye distintos enemigos con comportamientos propios:

- 🐸 Frog → enemigo con comportamiento reactivo (IA sencilla)
- 🦇 Bat → enemigo volador con movimiento horizontal
- 🐭 Mouse → enemigo terrestre que puede eliminarse saltando encima

---

# Funcionalidades extra implementadas

✔ Ranking persistente con Top puntuaciones  
✔ Menú de pausa durante la partida  
✔ Dos niveles adicionales (4 niveles en total)  
✔ IA sencilla en NPCs  
✔ Animaciones completas en personajes y enemigos  

---

# Tecnologías utilizadas

- Java
- libGDX
- Gradle
- Programación Orientada a Objetos

---

# Estructura del proyecto

```text
com.svalero.com
│
├── domain      → Entidades del juego
├── manager     → Lógica, niveles, renderizado, recursos, sonido...
├── screen      → Pantallas del juego
├── ui          → HUD e interfaz
└── util        → Constantes y utilidades
```

---

# Arquitectura del proyecto

El proyecto está organizado mediante distintos managers especializados:

- `LogicManager` → lógica principal del gameplay
- `RenderManager` → renderizado del juego
- `LevelManager` → carga y configuración de niveles
- `ResourceManager` → animaciones y recursos gráficos
- `SoundManager` → música y efectos de sonido
- `ScoreManager` → persistencia del ranking
- `HudRenderer` → interfaz del jugador

Esta separación permite mantener el código más limpio, reutilizable y fácil de mantener.

---

# Controles

| Tecla | Acción |
|---|---|
| ← / → | Mover personaje |
| SPACE | Saltar |
| ESC / P | Pausar partida |
| ENTER | Confirmar opciones |

---

# Objetivo del juego

El jugador debe avanzar por los distintos niveles recogiendo gemas y evitando enemigos hasta llegar a la bandera final y completar todos los niveles.

---
