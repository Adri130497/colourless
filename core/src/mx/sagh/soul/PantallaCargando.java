package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by roberto on 13/03/17.
 */

class PantallaCargando extends Pantalla
{
    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.2f;
    private Sprite spriteCargando;
    private Animation<TextureRegion> spriteAnimado;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;

    // AssetManager
    private AssetManager manager;

    private ColourlessSoul juego;
    private Pantallas siguientePantalla;
    private Texto texto;

    private Texture texturaCargando1;
    private Texture texturaCargando2;

    public PantallaCargando(ColourlessSoul juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando1 = new Texture(Gdx.files.internal("cargando/loadingSprite1.png"));
        texturaCargando2 = new Texture(Gdx.files.internal("cargando/loadingSprite2.png"));
        TextureRegion texturaCompleta1 = new TextureRegion(texturaCargando1);
        TextureRegion[][] texturaPersonaje1 = texturaCompleta1.split(1280,800);
        TextureRegion texturaCompleta2 = new TextureRegion(texturaCargando2);
        TextureRegion[][] texturaPersonaje2 = texturaCompleta2.split(1280,800);
        spriteAnimado = new Animation(TIEMPO_ENTRE_FRAMES, texturaPersonaje1[0][0], texturaPersonaje1[0][1], texturaPersonaje2[0][0], texturaPersonaje2[0][1]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        spriteCargando = new Sprite(texturaPersonaje1[0][0]);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        texto = new Texto();
    }

    // Carga los recursos de la siguiente pantalla
    private void cargarRecursosSigPantalla() {
        manager = juego.getAssetManager();

        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case NIVEL_1:
                cargarRecursosNivel1();
                break;
            case AJUSTES:
                cargarRecursosAjustes();
                break;
            case EXTRAS:
                cargarRecursosExtra();
                break;
            case CREDITOS:
                cargarRecursosCredits();
                break;
            case LOGROS:
                cargarRecursosLogro();
                break;
            case GAMEOVER:
                cargarRecursosGameOver();
                break;
            case GANASTE:
                cargarRecursosGanaste();
                break;
            case TUTORIAL:
                cargarRecursosTutorial();
                break;


        }
    }

    private void cargarRecursosTutorial() {
        manager.load("FondosTutorial/howTo1.png", Texture.class);
        manager.load("FondosTutorial/howTo6.png", Texture.class);
        manager.load("FondosTutorial/howToSprites.png", Texture.class);
        manager.load("FondosTutorial/howToSprites2.png", Texture.class);
        manager.load("FondosTutorial/turnPage.mp3", Sound.class);
    }

    private void cargarRecursosGanaste() {
        manager.load("fondoMadera.png", Texture.class);
        manager.load("Next.png", Texture.class);
        manager.load("mainMenuButton.png", Texture.class);
    }

    private void cargarRecursosGameOver() {
        manager.load("GameOverGris.png", Texture.class);
        manager.load("restartButton.png", Texture.class);
        manager.load("mainMenuButton.png", Texture.class);
        manager.load("click.mp3", Sound.class);
    }

    private void cargarRecursosLogro() {
        manager.load("fondoMenu.png", Texture.class);
        manager.load("click.mp3", Sound.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("replayButton.png", Texture.class);
        manager.load("achievsScreen1.png", Texture.class);
    }

    private void cargarRecursosCredits() {
        manager.load("fondoMenu.png", Texture.class);
        manager.load("wind.mp3", Sound.class);
        manager.load("creditos.png", Texture.class);

    }

    private void cargarRecursosExtra() {
        manager.load("fondoMenu.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("click.mp3", Sound.class);
        manager.load("creditsButton.png", Texture.class);
        manager.load("howToButton.png", Texture.class);
        manager.load("achievementsButton.png", Texture.class);
    }

    private void cargarRecursosAjustes() {
        manager.load("controlsButton.png", Texture.class);
        manager.load("controlsChange.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("changeButtonL.png", Texture.class);
        manager.load("controlsButton.png", Texture.class);
        manager.load("controlsChange.png", Texture.class);
        manager.load("controlsTouch.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("wind.mp3", Sound.class);
        manager.load("click.mp3", Sound.class);
        manager.load("fondoMenu.png", Texture.class);
    }

    private void cargarRecursosNivel1() {
        manager.load("FondosPantalla/fondoGris1.jpg", Texture.class);
        manager.load("FondosPantalla/fondoGris2.jpg", Texture.class);
        manager.load("mapaColourless.tmx",TiledMap.class );
        manager.load("PezGiro/pezGiro.png",Texture.class);
        manager.load("PezGiro/pezVanish.png",Texture.class);
        manager.load("SpritesPocion/pocionBNSprites.png", Texture.class);
        manager.load("SpritesPocion/pocionOroSprites.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("bite1.mp3", Sound.class);
        manager.load("pauseButton.png", Texture.class);
        manager.load("click.mp3", Sound.class);
        manager.load("KaiSprites/kaiRestingSprite.png", Texture.class);
        manager.load("KaiSprites/kaiWalkingSprite.png", Texture.class);
        manager.load("KaiSprites/kaiJumpingSprite.png", Texture.class);
        manager.load("KaiSprites/kaiGotHitSprite.png", Texture.class);
        manager.load("KaiSprites/kaiFallingSprite.png", Texture.class);
        manager.load("SpritesBarraVida/vida1.png", Texture.class);
        manager.load("SpritesBarraVida/vida2.png", Texture.class);
        manager.load("SpritesBarraVida/vida3.png", Texture.class);
        manager.load("SpritesBarraVida/vida4.png", Texture.class);
        manager.load("SpritesBarraVida/vida5.png", Texture.class);
        manager.load("SpritesBarraVida/vida6.png", Texture.class);
        manager.load("SpritesBarraVida/vida7.png", Texture.class);
        manager.load("SpritesBarraVida/vidaFull.png", Texture.class);
        manager.load("drop.png", Texture.class);
        manager.load("lluvia2.pe", ParticleEffect.class);
        manager.load("padBack.png", Texture.class);
        manager.load("padKnob.png", Texture.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("wind.mp3", Sound.class);
        manager.load("SpritesSlime/slimePiso.png",Texture.class);
        manager.load("powerDown.mp3", Sound.class);
        manager.load("potion.mp3", Sound.class);
        manager.load("upButton.png",Texture.class);
        manager.load("fondoFinNivel.png", Texture.class);
        manager.load("gamePaused.png", Texture.class);
        manager.load("resumeButton.png", Texture.class);
        manager.load("restartButton.png", Texture.class);
        manager.load("settingsButton.png", Texture.class);
        manager.load("mainMenuButton.png", Texture.class);
        manager.load("fondoMadera.png", Texture.class);
        manager.load("replayButton.png", Texture.class);
        manager.load("menuButton.png", Texture.class);
        manager.load("nextLevel.png",Texture.class);

    }

    private void cargarRecursosMenu() {
        manager.load("extrasButton.png", Texture.class);
        manager.load("fondoMenu.png", Texture.class);
        manager.load("loadButton.png", Texture.class);
        manager.load("NewGame.png", Texture.class);
        manager.load("settingsButton1.png", Texture.class);
        manager.load("startButton.png", Texture.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("wind.mp3", Sound.class);
        manager.load("click.mp3", Sound.class);
    }

    @Override
    public void render(float delta) {
        TextureRegion region;
        borrarPantalla(0.5f, 0.5f, 0.5f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        // Actualizar

        timerAnimacion += Gdx.graphics.getDeltaTime();
        region = spriteAnimado.getKeyFrame(timerAnimacion);
        batch.draw(region,spriteCargando.getX(),spriteCargando.getY());
        batch.end();
        // Actualizar carga
        actualizarCargaRecursos();
    }

    private void actualizarCargaRecursos() {
        if (manager.update()) { // Terminó?
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new PantallaMenu(juego));   // 100% de carga
                    break;
                case NIVEL_1:
                    juego.setScreen(new PantallaPrincipal(juego));   // 100% de carga
                    break;
                case AJUSTES:
                    juego.setScreen(new PantallaAjustes(juego));   // 100% de carga
                    break;
                case EXTRAS:
                    juego.setScreen(new PantallaExtras(juego));   // 100% de carga
                    break;
                case CREDITOS:
                    juego.setScreen(new PantallaCreditos(juego));   // 100% de carga
                    break;
                case LOGROS:
                    juego.setScreen(new PantallaLogros(juego));   // 100% de carga
                    break;
                case GAMEOVER:
                    juego.setScreen(new PantallaGameOver(juego));   // 100% de carga
                    break;
                case GANASTE:
                    juego.setScreen(new PantallaGanaNivel(juego));   // 100% de carga
                    break;
                case TUTORIAL:
                    juego.setScreen(new PantallaTutorial(juego));   // 100% de carga
                    break;


            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando1.dispose();
        texturaCargando2.dispose();
    }
}
