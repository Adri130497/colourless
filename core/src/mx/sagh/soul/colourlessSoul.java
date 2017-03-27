package mx.sagh.soul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class colourlessSoul extends Game {

	private final AssetManager assetManager;

	public colourlessSoul(){
		assetManager=new AssetManager();
	}

	@Override
	public void create() {
		// Lo preparamos para que cargue mapas
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		// Pone la pantalla inicial (Splash)
		setScreen(new PantallaInicial(this));

		PantallaAjustes.estadoJugabilidad = PantallaAjustes.EstadoJugabilidad.TOUCH;

	}
	// Para que las otras pantallas usen el assetManager
	public AssetManager getAssetManager() {
		return assetManager;
	}
	@Override
	public void dispose() {
		super.dispose();
		assetManager.clear();
	}
}
