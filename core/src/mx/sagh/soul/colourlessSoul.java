package mx.sagh.soul;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class colourlessSoul extends Game {

	@Override
	public void create() {
		PantallaAjustes.estadoJugabilidad = PantallaAjustes.EstadoJugabilidad.TOUCH;
		setScreen(new PantallaMenu(this));
	}
}
