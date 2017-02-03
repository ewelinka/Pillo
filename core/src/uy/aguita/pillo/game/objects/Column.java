package uy.aguita.pillo.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import uy.aguita.pillo.game.Assets;

/**
 * Created by ewe on 2/2/17.
 */
public class Column extends Actor {
    private TextureRegion regTex;
    private Animation anim;
    private int columnNr;
    private float stateTime;

    public Column(int nr){
        columnNr = nr;
        init();

    }


    private void init(){
        stateTime = 0 ;
        if(columnNr == 1){
            anim = Assets.instance.trivia.column1;
            setSize(109,386);
        }else{
            anim = Assets.instance.trivia.column2;
            setSize(114,431);
        }

    }

    @Override
    public void act(float delta){
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        regTex = anim.getKeyFrame(stateTime, true);
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
