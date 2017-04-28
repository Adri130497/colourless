package mx.sagh.soul;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

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

    public boolean tocoSlime(Slime slime){
        int x = (int)sprite.getX()-30;
        int y = (int)sprite.getY()+30;
        int width = (int)sprite.getWidth()-30;
        int height = (int)sprite.getHeight()-30;

        Rectangle r = slime.sprite.getBoundingRectangle();
        if(x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y){
            if(PantallaAjustes.prefs.getBoolean("Sounds",true))
            slime.sprite.setX(0);
            slime.setEstadoMovimiento(Slime.EstadoMovimiento.QUIETO);
            return true;
        }
        return false;
    }
}
