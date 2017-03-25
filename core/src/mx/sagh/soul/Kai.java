package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by User on 17/03/2017.
 */

public class Kai extends Objeto{
    private final float VELOCIDAD_X = 4;      // Velocidad horizontal

    private Animation<TextureRegion> spriteAnimado, spriteReposo;           // Animación caminando, en reposo y parándose
    private float timerAnimacion, timerAnimacion2;                                           // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;

    // Salto
    private EstadoSalto estadoSalto = EstadoSalto.EN_PISO;
    private float alturaSalto;  // altura actual, inicia en cero
    private float yOriginal;

    public Kai(Texture texturaCaminando, Texture texturaReposo, float x, float y) {
        TextureRegion texturaCompleta = new TextureRegion(texturaCaminando);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(120,120);
        spriteAnimado = new Animation(0.15f, texturaPersonaje[0][0], texturaPersonaje[0][1],
                texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4],
                texturaPersonaje[0][5], texturaPersonaje[0][6], texturaPersonaje[0][7],
                texturaPersonaje[0][8], texturaPersonaje[0][9], texturaPersonaje[0][10]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);

        texturaCompleta =  new TextureRegion(texturaReposo);
        texturaPersonaje = texturaCompleta.split(120,120);
        spriteReposo = new Animation(0.25f, texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2],texturaPersonaje[0][1]);
        spriteReposo.setPlayMode(Animation.PlayMode.LOOP);

        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        timerAnimacion2 = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
        // Salto
        alturaSalto = 0;
    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        TextureRegion region;
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                region = spriteAnimado.getKeyFrame(timerAnimacion);
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case QUIETO:
            case INICIANDO:
                timerAnimacion2 += Gdx.graphics.getDeltaTime();
                region = spriteReposo.getKeyFrame(timerAnimacion2);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
        }
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento y estadoSalto
    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                moverHorizontal(mapa);
                break;
        }
        switch (estadoSalto) {
            case SUBIENDO:
            case BAJANDO:
                moverVertical(mapa);
                break;
        }
    }

    // Realiza el salto
    private void moverVertical(TiledMap mapa) {
        float delta = Gdx.graphics.getDeltaTime()*200;
        switch (estadoSalto) {
            case SUBIENDO:
                sprite.setY(sprite.getY()+delta);
                alturaSalto += delta;
                if (alturaSalto>=sprite.getHeight()) {
                    estadoSalto = EstadoSalto.BAJANDO;
                }
                break;
            case BAJANDO:
                sprite.setY(sprite.getY()-delta);
                alturaSalto -= delta;
                if (alturaSalto<=0) {
                    estadoSalto = EstadoSalto.EN_PISO;
                    alturaSalto = 0;
                    sprite.setY(yOriginal);
                }
                break;
        }
    }

    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes
    private void moverHorizontal(TiledMap mapa) {
        // Obtiene la primer capa del mapa (en este caso es la única)
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        // Ejecutar movimiento horizontal
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
                if (nuevaX <= PantallaPrincipal.ANCHO_MAPA - sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
            }
        }
        // ¿Quiere ir a la izquierda?
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


    // Revisa si toca una moneda
    public boolean recolectarItems(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1); //puedes recuperar una capa del mapa
        int x = (int)(sprite.getX()/32);
        int y = (int)(sprite.getY()/32);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "pez".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        x = (int)(sprite.getX()/32)+3;
        y = (int)(sprite.getY()/32);
        celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "pez".equals(tipo)) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        x = (int)(sprite.getX()/32);
        y = (int)(sprite.getY()/32)+1;
        celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "pez".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        x = (int)(sprite.getX()/32)+1;
        y = (int)(sprite.getY()/32)+1;
        celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "pez".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        return false;
    }

    private void actualizarItems(float delta, TiledMapTileLayer capa, int x, int y, TiledMapTileLayer.Cell celda) {

        //imgCredits.setColor(1,1,1,imgCredits.getColor().a-0.01f);

        capa.setCell(x,y,celda);

    }

    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    // Inicia el salto
    public void saltar() {

        if (estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO) {
            // inicia
            estadoSalto = EstadoSalto.SUBIENDO;
            yOriginal = sprite.getY();
            alturaSalto = 0;
        }
    }

    public boolean esAlcanzado(TiledMap mapa, OrthographicCamera camara) {
        return (camara.position.x-Pantalla.ANCHO/2+sprite.getWidth()/2) >= (sprite.getX()+sprite.getWidth());
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

    public enum EstadoSalto {
        SUBIENDO,
        BAJANDO,
        EN_PISO
    }
}
