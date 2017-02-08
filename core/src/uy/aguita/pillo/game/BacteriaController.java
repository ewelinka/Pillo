package uy.aguita.pillo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import uy.aguita.pillo.game.objects.Bacteria;
import uy.aguita.pillo.game.objects.Tooth;
import uy.aguita.pillo.util.AudioManager;

import java.util.ArrayList;

/**
 * Created by ewe on 1/17/17.
 */
public class BacteriaController {
    public static final String TAG = BacteriaController.class.getName();

    private int actualScore;
    private Stage stage;
    private Bacteria b1;
    private Bacteria b2;
    private Bacteria b3;
    private Bacteria b4;
    private ArrayList<Bacteria> bacterias;
    public Tooth tooth;
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private Circle c = new Circle();
    private int lifeLost;

    private int bacteriaMargin;


    public BacteriaController(Stage stage){
        this.stage = stage;
        actualScore = 0;
        lifeLost = 0;
        bacteriaMargin = 20;
        addTooth();
        createBacterias();

    }

    public void createBacterias(){
        bacterias = new ArrayList<Bacteria>();
        b1 = new Bacteria(1,this);
        stage.addActor(b1);
        bacterias.add(b1);
        b2 =new Bacteria(2,this);
        stage.addActor(b2);
        bacterias.add(b2);
        b3 = new Bacteria(3,this);
        stage.addActor(b3);
        bacterias.add(b3);
        b4 = new Bacteria(4,this);
        stage.addActor(b4);
        bacterias.add(b4);


    }

    public void addTooth(){
        tooth = new Tooth();
        stage.addActor(tooth);
    }

    public void onBacteriaKilled(){
        actualScore+=1;
    }

    public void resetBacterias(){
        for (int i =0;i<bacterias.size();i++) {
            bacterias.get(i).resetBacteria();
        }
    }

    public void stopBacterias(){
        for (int i =0;i<bacterias.size();i++) {
            bacterias.get(i).stopBacteria();
        }
    }

    public void startBacterias(){
        for (int i =0;i<bacterias.size();i++) {
            bacterias.get(i).startBacteria();
        }
    }

    public int getActualScore(){ return actualScore;}


    public boolean hasCollisions(){
        r1.set(tooth.getX(), tooth.getY(), tooth.getWidth(), tooth.getHeight());
        for (int i =0;i<bacterias.size();i++){
            Bacteria currentBacteria = bacterias.get(i);
            if(currentBacteria.hasActions()){ // check just the moving bacterias
                c.set(currentBacteria.getX()+45,currentBacteria.getY()+45,35);

                if (Intersector.overlaps(c, r1))
                {
                    onCollision();
                    return true;
                }
            }

        }
        return false;
    }

    private void onCollision(){
        Gdx.app.log(TAG, "collision!!");
        AudioManager.instance.play(Assets.instance.sounds.bad);
        stopBacterias();
        tooth.setHurt();



    }

    public void setToothOk(boolean isok)
    {
        tooth.setOk(isok);
    }

    public void resetTooth(){
        tooth.setOk();
        lifeLost+=1;
    }

    public int getLifeLost(){ return  lifeLost;}
}
