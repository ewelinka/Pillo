package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.game.BacteriaController;
import uy.aguita.pillo.util.AudioManager;
import uy.aguita.pillo.util.Constants;

/**
 * Created by ewe on 1/17/17.
 */
public class Bacteria extends Actor {
    public static final String TAG = Bacteria.class.getName();
    public float stateTime;
    public Animation animation;
    private int identifier;
    public float velocity;
    private TextureRegion reg;
    private BacteriaController bacteriaController;
    private boolean lookingLeft;


    public Bacteria(int identifier, BacteriaController bacteriaController){
        this.bacteriaController = bacteriaController;
        this.identifier = identifier;
        velocity = 1;
        setSize(90,90);
        setOrigin(getWidth()/2,getHeight()/2);
        setToTouch();
        setAnimationAndGo();
    }

    private void setAnimationAndGo(){
        switch(identifier){
            case 1:
                // from right side
                animation  = Assets.instance.toothGame.bacteria1;
                setRight();
                break;
            case 2:
                // from down
                animation  = Assets.instance.toothGame.bacteria2;
                setDown();
                break;
            case 3:
                //from up
                animation  = Assets.instance.toothGame.bacteria3;
                setTop();
                break;
            case 4:
                // from left
                animation  = Assets.instance.toothGame.bacteria4;
                setLeft();
                break;
        }
        stateTime = 0;

        if(getX()<0){
            lookingLeft =true;
        }else
            lookingLeft=false;

        //reg = Assets.instance.toothGame.bacteriaTexture;
        addAction(Actions.moveTo(0-getWidth()/2,0-getHeight()/2,10.0f/velocity));

    }

    @Override
    public void act(float delta){
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        reg = animation.getKeyFrame(stateTime, true);
        batch.draw(reg.getTexture(),
                this.getX(), this.getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth() ,this.getHeight(),
                this.getScaleX(), this.getScaleY(),
                this.getRotation(),
                reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), lookingLeft,false);
    }


    private void setToTouch(){
        this.setTouchable(Touchable.enabled);
        // to move around
        this.addListener(new ActorGestureListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                AudioManager.instance.play(Assets.instance.sounds.good);
                velocity+=0.1;
                bacteriaController.onBacteriaKilled();
//                Gdx.app.log(TAG,"new veloricty "+velocity +"(10/velocity))"+(10/velocity));
                resetBacteria();

            }

        });
    }

    public void stopBacteria(){
        this.setTouchable(Touchable.disabled);
        clearActions();
    }

    public void startBacteria(){
        this.setTouchable(Touchable.enabled);
        addAction(Actions.moveTo(0-getWidth()/2,0-getHeight()/2,10.0f/velocity));
    }

    public void resetBacteria(){
        this.setTouchable(Touchable.enabled);
        if(hasActions())
            clearActions();
        setAnimationAndGo();
    }

    private void setRight(){
        setPosition(Constants.VIEWPORT_WIDTH/2+100, MathUtils.random(-Constants.VIEWPORT_HEIGHT/2,Constants.VIEWPORT_HEIGHT/2));
    }

    private void setDown(){
        setPosition(MathUtils.random(-Constants.VIEWPORT_WIDTH/2,Constants.VIEWPORT_WIDTH/2),-Constants.VIEWPORT_HEIGHT/2 - 200);
    }

    private void setTop(){
        setPosition(MathUtils.random(-Constants.VIEWPORT_WIDTH/2,Constants.VIEWPORT_WIDTH/2),Constants.VIEWPORT_HEIGHT/2+100);
    }

    private void setLeft(){
        setPosition(-Constants.VIEWPORT_WIDTH/2 -200, MathUtils.random(-Constants.VIEWPORT_HEIGHT/2,Constants.VIEWPORT_HEIGHT/2));
    }







}
