package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * Created by ewe on 1/22/17.
 */
public class MatchButton extends Image {
    private float homeX,homeY;
    public MatchButton(TextureAtlas.AtlasRegion img) {
        super(img);
    }

    public void setHome(float x, float y){
        homeX = x;
        homeY = y;
    }

    public void goHome(){
        addAction(Actions.moveTo(homeX,homeY,1.0f));
    }
}
