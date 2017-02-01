package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import uy.aguita.pillo.game.Assets;

/**
 * Created by ewe on 1/24/17.
 */
public class FeteCharacter extends Actor {
    private Animation animation;
    private TextureRegion regTex;
    private float stateTime;

    public FeteCharacter(){
        animation = Assets.instance.trivia.fete;
        stateTime = animation.getAnimationDuration(); // we start in last frame so we wont animate
    }

    public void doFete(){
        stateTime = 0;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        regTex = animation.getKeyFrame(stateTime, false);
        batch.draw(regTex.getTexture(),
                this.getX(), this.getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth() ,this.getHeight(),
                this.getScaleX(), this.getScaleY(),
                this.getRotation(),
                regTex.getRegionX(), regTex.getRegionY(),
                regTex.getRegionWidth(), regTex.getRegionHeight(), false,false);
    }


}
