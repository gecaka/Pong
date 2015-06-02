package pong;

/* Java imports */
import java.util.Random;
import static pong.Pong.DISPLAY_HEIGHT;
import static pong.Pong.DISPLAY_WIDTH;

/**
 *
 * @author Georgi Getsov
 */

public class Ball extends BaseObject{
    
    protected float velX, velY;
    protected int radius;
    
    public Ball( float x_pos, float y_pos, int radiusIn ){
        
        super( x_pos, y_pos, radiusIn, radiusIn );
        
        radius = radiusIn;
        
        initMoveDirection();
        
    }
    
    protected void update(){
        
        moveBall();
        
    }
    
    protected void initMoveDirection(){
        
    Random rand = new Random();
    
    int direction_x = rand.nextInt( DISPLAY_WIDTH + 1 );
    int direction_y = rand.nextInt( DISPLAY_HEIGHT + 1 );   
    
    double angle = Math.atan2( direction_x, direction_y );

    float scaleX = ( float ) Math.cos( angle );
    float scaleY = ( float ) Math.sin( angle );

    velX = scaleX * speed; //speed is already defined
    velY = scaleY * speed;
    
    }
    
    protected void recalculateMoveDirection(){
        velX = -velX;
        velY = -velY;
    }
    
    protected void moveBall(){
        
        x += velX;
        y += velY;
        
    }
    
}
