package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * Created by ewe on 1/22/17.
 */
public class MatchButton extends ImageButton {
    private float homeX,homeY;
    public MatchButton(ImageButtonStyle style) {
        super(style);
    }

    public void setHome(float x, float y){
        homeX = x;
        homeY = y;
    }

    public void goHome(){
        addAction(Actions.moveTo(homeX,homeY,1.0f));
    }
}
