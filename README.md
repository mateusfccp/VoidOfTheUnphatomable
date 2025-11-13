# Void of the Unfathomable

*Void of the Unfathomable* es un juego rogue-like de exploración de mazmorras en 2D, ambientado en un mundo oscuro y 
misterioso. Los jugadores asumen el papel de un aventurero que se adentra en una mazmorra finita y con pocos peligros,
enemigos y tesoros no muy ocultos.

Fue hecho como proyecto final para la materia de Algoritmo y Estructura de Datos en la Universidad Adventista del Plata.

## Dependencias

- Necesitás del Java (JDK) 25 para compilar y correr el juego.
- Necesitás de una terminal avanzada (soporte a ANSI y raw mode) para correr el juego.

## Compilación

Se puede compilar el juego con `gradlew build` en la terminal, desde la carpeta raíz del proyecto. Un archivo JAR será 
generado en `build/libs/`.

## Ejecución

Despues de compilado, se puede correr el juego con `java -jar <nombre_del_jar>`.

### Sugerencias

- Se sugiere utilizar una fuente cuadrada en vez de rectangular para una mejor experiencia visual. En la página
  [The Old PC Font Resource](https://int10h.org/oldschool-pc-fonts) tenés varias opciones gratuitas. Durante
  el desarollo y pruebas fue usada la fuente
  [Px437 TridentEarly 8x8](https://int10h.org/oldschool-pc-fonts/fontlist/font?tridentearly_8x8).

### Controles

- Flechas direccionales: mover cursor en menus, mover al personaje
- ESC: salir del juego y shop
- Enter: interactuar con la interfaz
- I: abrir/cerrar inventario
- C: activar habilidad especial de arma
