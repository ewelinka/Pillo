package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import uy.aguita.pillo.game.Assets;

/**
 * Created by ewe on 1/17/17.
 */
public class Tooth extends Actor {
    public static final String TAG = Bacteria.class.getName();
    private float stateTime;
    public Animation animation;
    public Animation animationBad;
    private TextureRegion regTex;
    private boolean isOk;

    public Tooth(){
        regTex = Assets.instance.toothGame.tooth;
        setSize(regTex.getRegionWidth(),regTex.getRegionHeight());
        //setPosition(0-getWidth()/2,0-getHeight()/2);
        setPosition(-71,-56); //hardcoded!!
        stateTime = 0;
        isOk = true;
        animation  = Assets.instance.toothGame.toothGood;
        animationBad = Assets.instance.toothGame.toothBad;

//        this.setTouchable(Touchable.enabled);
//        // to move around
//        this.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                setPosition(getX()+deltaX,getY()+deltaY);
//                Gdx.app.log(TAG," x "+getX()+" y "+getY());
//            }
//
//
//        });
    }

    @Override
    public void act(float delta){
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(isOk) {
            regTex = animation.getKeyFrame(stateTime, true);
        }
        else{
            regTex = animationBad.getKeyFrame(stateTime,false);
        }
        //regTex = Assets.instance.toothGame.tooth;
        batch.draw(regTex.getTexture(),
                this.getX(), this.getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth() ,this.getHeight(),
                this.getScaleX(), this.getScaleY(),
                this.getRotation(),
                regTex.getRegionX(), regTex.getRegionY(),
                regTex.getRegionWidth(), regTex.getRegionHeight(), false,false);
    }

    public void setHurt(){
        stateTime = 0;
        isOk = false;
    }

    public void setOk(){
        stateTime = 0;
        isOk = true;
    }

    public void setOk(boolean isOkNow){
        isOk = isOkNow;
    }

    public boolean isHurt(){
        return !isOk;
    }

    public float getStateTime(){
        return stateTime;
    }
}
