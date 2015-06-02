package pong;

/**
 *
 * @author Georgi Getsov
 */

public class Paddle extends BaseObject{
    
    protected boolean move_up = false;
    protected boolean move_down = false;
    protected boolean move_left = false;
    protected boolean move_right = false;
    
    public Paddle( float x_pos, float y_pos, int widthIn, int heightIn ){
        
        super( x_pos, y_pos, widthIn, heightIn );
        
    }
    
    protected void update(){
        
        movePaddle();
        
    }
    
    protected void movePaddle(){
        
        if( move_up ){
            
            if( y >= 0){
                dY( -speed );   
            }
        }
        
        if( move_down ){
            
            if( y <= Pong.DISPLAY_HEIGHT - height ){
                dY( speed );
            }
        }
        
        if( move_left ){
            
            if( x >= 0 ){
                dX( -speed );
            }
        }
        
        if( move_right ){
            
            if( x <= Pong.DISPLAY_WIDTH - width ){
                dX( speed );
            }
        }
        
    }
    
    public void moveUp( boolean up ){
        
        move_up = up;
    
    }
    
    public void moveDown( boolean down ){
        
        move_down = down;
    
    }
    
    public void moveLeft( boolean left ){
        
        move_left = left;
    
    }
    
    public void moveRight( boolean right ){
        
        move_right = right;
        
    }
    
}
