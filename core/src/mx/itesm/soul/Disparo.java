package mx.itesm.soul;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Adrian on 26/04/2017.
 */

public class Disparo extends Objeto {

    private final float VELOCIDAD_X = 600;      // Velocidad horizontal (a la derecha)
    // Recibe la imagen
    public Disparo(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    // Mueve el personaje a la derecha
    public void mover(float delta, int i) {
        float distancia = VELOCIDAD_X*delta*i;
        sprite.setX(sprite.getX()+distancia);
    }

    public boolean chocaCon(Objeto enemigo) {
        if (enemigo instanceof Slime) {
            Slime slime = (Slime) enemigo;
            return sprite.getBoundingRectangle().overlaps(slime.sprite.getBoundingRectangle());
        }
        else if (enemigo instanceof TheThing) {
            TheThing slime = (TheThing) enemigo;
            return sprite.getBoundingRectangle().overlaps(slime.sprite.getBoundingRectangle());
        }
        return false;
    }
}
