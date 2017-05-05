package mx.itesm.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Aldo on 05/05/2017.
 */

public class TheThing extends Objeto {
    public mx.itesm.soul.EstadoLogro estado;
    // Movimiento
    private final float VELOCIDAD_X = 10;

    private Animation<TextureRegion> spriteAnimado1, spriteAnimado2, spriteAnimado3;
    private float timerAnimacion1, timerAnimacion2, timerAnimacion3;
    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;


    public TheThing(Texture sink1, Texture sink2, Texture standing, float x, int y) {
        TextureRegion texturaCompleta1 = new TextureRegion(sink1);
        TextureRegion[][] texturaPersonaje1 = texturaCompleta1.split(425,245);
        TextureRegion texturaCompleta2 = new TextureRegion(sink2);
        TextureRegion[][] texturaPersonaje2 = texturaCompleta2.split(447,245);
        TextureRegion texturaCompleta3 = new TextureRegion(standing);
        TextureRegion[][] texturaPersonaje3 = texturaCompleta3.split(447,245);

        //INICIANDO
        spriteAnimado1 = new Animation(0.15f, texturaPersonaje2[0][3], texturaPersonaje2[0][2], texturaPersonaje2[0][1], texturaPersonaje2[0][0], texturaPersonaje1[0][2], texturaPersonaje1[0][1]);
        spriteAnimado1.setPlayMode(Animation.PlayMode.LOOP);

        //STANDING
        spriteAnimado2 = new Animation(0.2f, texturaPersonaje3[0][0], texturaPersonaje3[0][1], texturaPersonaje3[0][2], texturaPersonaje3[0][1]);
        spriteAnimado2.setPlayMode(Animation.PlayMode.LOOP);

        //MOVING
        spriteAnimado3 = new Animation(0.15f, texturaPersonaje2[0][0], texturaPersonaje2[0][1], texturaPersonaje2[0][2], texturaPersonaje2[0][3], texturaPersonaje2[0][2], texturaPersonaje2[0][1]);
        spriteAnimado3.setPlayMode(Animation.PlayMode.LOOP);

        sprite = new Sprite(texturaPersonaje2[0][3]);    // QUIETO
        sprite.setPosition(x,y);
    }


    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        TextureRegion region;
        switch (estadoMovimiento) {
            case MOV_IZQUIERDA:
            case MOV_DERECHA:
                timerAnimacion3 += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado3.getKeyFrame(timerAnimacion3);
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case QUIETO:
                timerAnimacion2 += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado2.getKeyFrame(timerAnimacion2);
                if (sprite.getX()>Pantalla.ANCHO/2) {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case INICIANDO:
                timerAnimacion1 += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado1.getKeyFrame(timerAnimacion1);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
        }
    }


    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case INICIANDO:
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                moverHorizontal(mapa);
                break;
        }
    }



    private void moverHorizontal(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        float nuevaX = sprite.getX();
        // ¿Quiere ir a la Derecha?
        if ( estadoMovimiento==EstadoMovimiento.MOV_DERECHA) {
            // Obtiene el bloque del lado derecho. Asigna null si puede pasar.
            int x = (int) ((sprite.getX() + 32) / 32);   // Convierte coordenadas del mundo en coordenadas del mapa
            int y = (int) (sprite.getY() / 32);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(x, y);
            if (celdaDerecha != null) {
                Object tipo = (String) celdaDerecha.getTile().getProperties().get("tipo");
                if (!"ladrillo".equals(tipo)) {
                    celdaDerecha = null;  // Puede pasar
                }
            }
            if ( celdaDerecha==null) {
                // Ejecutar movimiento horizontal
                nuevaX += VELOCIDAD_X;
                // Prueba que no salga del mundo por la derecha
                if (nuevaX >= 0) {
                    sprite.setX(nuevaX);
                }
            }
        }
        if ( estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA || estadoMovimiento==EstadoMovimiento.INICIANDO) {
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
