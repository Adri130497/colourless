package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by User on 27/03/2017.
 */

public class Slime extends Objeto{
    public EstadoLogro estado;
    // Movimiento
    private final float VELOCIDAD_X = 10;
    private float xOriginal;

    private Animation<TextureRegion> spriteAnimado;         // Animación caminando, en reposo y parándose
    private float timerAnimacion;
    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;


    public Slime(Texture textura, float x, float y) {
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(160,81);
        spriteAnimado = new Animation(0.15f, texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4], texturaPersonaje[0][5], texturaPersonaje[0][4], texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x,y);
        xOriginal = sprite.getX();
    }


    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        TextureRegion region;
        switch (estadoMovimiento) {
            case MOV_IZQUIERDA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado.getKeyFrame(timerAnimacion);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case QUIETO:
            case INICIANDO:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado.getKeyFrame(timerAnimacion);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
        }
    }


    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case MOV_IZQUIERDA:
                moverHorizontal(mapa);
                break;
        }
    }



    private void moverHorizontal(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        float nuevaX = sprite.getX();
        if ( estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
            int xIzq = (int) ((sprite.getX()) / 32);
            int y = (int) (sprite.getY() / 32);
            // Obtiene el bloque del lado izquierdo. Asigna null si puede pasar.
            TiledMapTileLayer.Cell celdaIzquierda = capa.getCell(xIzq, y);
            if (celdaIzquierda != null) {
                Object tipo = (String) celdaIzquierda.getTile().getProperties().get("tipo");
                if (!"ladrillo".equals(tipo)) {
                    celdaIzquierda = null;  // Puede pasar
                }
            }
            if ( celdaIzquierda==null) {
                // Prueba que no salga del mundo por la izquierda
                nuevaX -= VELOCIDAD_X;
                if (nuevaX >= 0) {
                    sprite.setX(nuevaX);
                }
            }
        }
    }

    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

}
