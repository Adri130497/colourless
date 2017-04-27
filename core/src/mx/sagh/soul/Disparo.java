package mx.sagh.soul;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Adrian on 26/04/2017.
 */

public class Disparo extends Objeto {

    private final float VELOCIDAD_X = 400;      // Velocidad horizontal (a la derecha)

    // Recibe la imagen
    public Disparo(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    // Mueve el personaje a la derecha
    public void mover(float delta) {
        float distancia = VELOCIDAD_X*delta;
        sprite.setX(sprite.getX()+distancia);
    }

    public boolean chocaCon(Slime slime) {
        return sprite.getBoundingRectangle().overlaps(slime.sprite.getBoundingRectangle());
    }

}
