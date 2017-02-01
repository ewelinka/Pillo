package uy.aguita.pillo.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by ewe on 1/21/17.
 */
public class BacteriaRenderer  implements Disposable {
    public static final String TAG = BacteriaRenderer.class.getName();
    protected OrthographicCamera camera;
    protected SpriteBatch spriteBatch;


    @Override
    public void dispose() {

    }
}
