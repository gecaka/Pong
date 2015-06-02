package pong;

/* Library imports */
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/* My imports */
import pong.engine.Program;
import pong.engine.Shader;

/**
 *
 * @author Georgi Getsov
 */

public class Pong {

    public static final int DISPLAY_WIDTH = 800;
    public static final int DISPLAY_HEIGHT = 600;
    public static final int MAX_FPS = 60;
    
    private int VAO;
    
    private Paddle first_player_paddle_bottom;
    private Paddle first_player_paddle_left;
    private Paddle second_player_paddle_top;
    private Paddle second_player_paddle_right;
    
    private Ball ball;
    
    public Pong(){
        
    }
    
    private void initDisplay(){
        
        try{
            
            Display.setDisplayMode( new DisplayMode( DISPLAY_WIDTH,DISPLAY_HEIGHT ) );
            Display.create();
            Display.setTitle( "2 Player Pong" );
        
        } catch( LWJGLException e ){
        
            AL.destroy();
            System.exit( 0 );
        
        }
    }
    
    private void initGL(){
        
        glClearColor( 0f, 0f, 0f, 1.0f );
        VAO = glGenVertexArrays();
        glBindVertexArray( VAO );
        
        Shader vertex_shader = new Shader( "src/pong/engine/shaders/vertex_shade.txt", GL_VERTEX_SHADER );
        Shader fragment_shader = new Shader( "src/pong/engine/shaders/fragment_shade.txt", GL_FRAGMENT_SHADER );
        
        new Program( vertex_shader, fragment_shader ).useProgram();
        
    }
    
    private void initGameObjects(){
        
        int paddle_width = 128;
        int paddle_height = 16;
        
        first_player_paddle_bottom = new Paddle( ( DISPLAY_WIDTH / 2f ) - ( paddle_width / 2 ), DISPLAY_HEIGHT - ( paddle_height ), paddle_width, paddle_height );
        first_player_paddle_left = new Paddle( 0f, ( DISPLAY_HEIGHT / 2f ) - ( paddle_width / 2 ), paddle_height, paddle_width );
        second_player_paddle_right = new Paddle( DISPLAY_WIDTH - paddle_height, ( DISPLAY_HEIGHT / 2f)-( paddle_width / 2 ), paddle_height, paddle_width );
        second_player_paddle_top = new Paddle( ( DISPLAY_WIDTH / 2f ) - ( paddle_width / 2 ), 0f, paddle_width, paddle_height );
        
        int ball_radius = 8;
        
        ball = new Ball( DISPLAY_WIDTH / 2f, DISPLAY_HEIGHT / 2f, ball_radius );
        
    }
    
    public void start(){
        
        initDisplay();
        initGL();
        initGameObjects();
        gameLoop();
        
    }
    
    private void gameLoop(){
    
        while( !Display.isCloseRequested() ){
            
            update();
            render();
            
            Display.update();
            Display.sync( MAX_FPS );
        
        }
        
        cleanUp();
        
    }

    private void update(){
        
        inputPool();
        
        first_player_paddle_bottom.update();
        first_player_paddle_left.update();
        second_player_paddle_top.update();
        second_player_paddle_right.update();
        
        ball.update();
        
        checkCollisionWithBall(first_player_paddle_bottom);
        checkCollisionWithBall(first_player_paddle_left);
        checkCollisionWithBall(second_player_paddle_top);
        checkCollisionWithBall(second_player_paddle_right);
    }
    
    private void render(){
        
        glClear( GL_COLOR_BUFFER_BIT );
        glViewport(0, 0, DISPLAY_WIDTH,DISPLAY_HEIGHT);
        
        first_player_paddle_bottom.render();
        first_player_paddle_left.render();
        second_player_paddle_right.render();
        second_player_paddle_top.render();
        
        ball.render();
        
    }
    
    public void inputPool(){
            
        if( Keyboard.next() ){
                
            /* PLAYER 1 CONTROLS */
            
                if( Keyboard.getEventKey() == Keyboard.KEY_UP ){
                    if( Keyboard.getEventKeyState() ){
                        first_player_paddle_left.moveUp( true );
                    }else{
                        first_player_paddle_left.moveUp( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_DOWN ){
                    if( Keyboard.getEventKeyState() ){
                        first_player_paddle_left.moveDown( true );
                    }else{
                        first_player_paddle_left.moveDown( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_LEFT ){
                    if( Keyboard.getEventKeyState() ){
                        first_player_paddle_bottom.moveLeft( true );
                    }else{
                        first_player_paddle_bottom.moveLeft( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_RIGHT ){
                    if( Keyboard.getEventKeyState() ){
                        first_player_paddle_bottom.moveRight( true );
                    }else{
                        first_player_paddle_bottom.moveRight( false );
                    }
                }
                
            /* PLAYER 2 CONTROLS */    
                
                if( Keyboard.getEventKey() == Keyboard.KEY_W ){
                    if( Keyboard.getEventKeyState() ){
                        second_player_paddle_right.moveUp( true );
                    }else{
                        second_player_paddle_right.moveUp( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_S ){
                    if( Keyboard.getEventKeyState() ){
                        second_player_paddle_right.moveDown( true );
                    }else{
                        second_player_paddle_right.moveDown( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_A ){
                    if( Keyboard.getEventKeyState() ){
                        second_player_paddle_top.moveLeft( true );
                    }else{
                        second_player_paddle_top.moveLeft( false );
                    }
                }
                
                if( Keyboard.getEventKey() == Keyboard.KEY_D ){
                    if( Keyboard.getEventKeyState() ){
                        second_player_paddle_top.moveRight( true );
                    }else{
                        second_player_paddle_top.moveRight( false );
                    }
                }            
        }
    }
    
    public void checkCollisionWithBall(Paddle paddleObj){
        
        if( ball.getBottomBounds() > paddleObj.getTopBounds() && ball.getTopBounds() < paddleObj.getBottomBounds() ){
                    
            if( ball.getRightBounds() > paddleObj.getLeftBounds() && ball.getLeftBounds() < paddleObj.getRightBounds() ){
                
                ball.recalculateMoveDirection();
            
            }
        }
        
    } 
     
    
    public static void main(String[] args) {
        Pong new_game = new Pong();
        new_game.start();
    }
    
    private void cleanUp(){
        
        AL.destroy();
        Display.destroy();
        
    }
    
}
