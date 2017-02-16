package mx.sagh.soul;

/**
 * Created by User on 15/02/2017.
 * Representa un elemento gráfico del juego
 */
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class Objeto
{
    protected Sprite sprite;    // Imagen

    public Objeto(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean contiene(Vector3 v) {
        return sprite.getBoundingRectangle().contains(v.x,v.y); //Para saber si se está tocando al topo
    }
}
