package falling.mit;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MainGame implements ApplicationListener {
	
	private OrthographicCamera camera;
	private SpriteBatch pincel;
	
	private Texture spriteCuadrado;
	private Texture spriteAsteroide;
	private Texture fondo;
	
	private long ultimaAltura;
	private int altura=0;
	
	BitmapFont fuente;
	
	private Rectangle cuadrado;
	private Rectangle asteroide;
	long ultimoAsteroide;
	Array<Rectangle> asteroides;
	Iterator<Rectangle> iterAsteroide;
	
	int x = 1024;
	int y = 768;
	
	@Override
	public void create() {		
		
		camera = new OrthographicCamera();
		pincel = new SpriteBatch();
		
		fuente = new BitmapFont();
		
		Texture.setEnforcePotImages(false);
		
		spriteCuadrado = new Texture(Gdx.files.internal("cuadrado.png"));
		spriteAsteroide = new Texture(Gdx.files.internal("asteroide.png"));
		fondo = new Texture(Gdx.files.internal("fondo.jpg"));
		
		cuadrado = new Rectangle();
		
		camera.setToOrtho(false, x, y);
		
		cuadrado.width = 32;
		cuadrado.height = 32;
		cuadrado.x = x/2 - 32/2;
		cuadrado.y = y/4;
		
		asteroides = new Array<Rectangle>();
	
	}
	
	private void lanzarAsteroide() {
		asteroide = new Rectangle();
		asteroide.width = 32;
		asteroide.height = 32;
		asteroide.x = MathUtils.random(0, x - 32);
		asteroide.y = y + 32;
		ultimoAsteroide = TimeUtils.millis();
		asteroides.add(asteroide);
		altura++;	
	}


	@Override
	public void dispose() {
		pincel.dispose();
		spriteCuadrado.dispose();
		spriteAsteroide.dispose();
		asteroides.clear();
	}

	@Override
	public void render() {		
		Gdx.gl10.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		pincel.setProjectionMatrix(camera.combined);
		
		pincel.begin();
			pincel.draw(fondo, 0, 0);
			pincel.draw(spriteCuadrado, cuadrado.x, cuadrado.y);
			fuente.draw(pincel, "Altura: " + altura, x-150, y-20);
			for (Rectangle asteroide : asteroides)
				pincel.draw(spriteAsteroide, asteroide.x, asteroide.y);
		pincel.end();
		
		if (Gdx.input.isTouched()){
			Vector3 posicion = new Vector3();
			posicion.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			if (posicion.x > x/2 )
			cuadrado.x += 10;
			else
			cuadrado.x -= 10;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			cuadrado.x += 10;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			cuadrado.x -= 10;
		
		if (cuadrado.x < 0)
			cuadrado.x = 0;
		if (cuadrado.x > x-32)
			cuadrado.x = x-32;
		
		if (TimeUtils.millis() - ultimoAsteroide > 35)
		lanzarAsteroide();	
			
		iterAsteroide = asteroides.iterator();
		while (iterAsteroide.hasNext()){
			Rectangle asteroide = iterAsteroide.next();
			asteroide.y -= 10;
			if (asteroide.y + 32 < y-768)
				iterAsteroide.remove();
			
			if (asteroide.overlaps(cuadrado)){
				iterAsteroide.remove();
				altura = 0;
			}
		}
		
		
			
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
