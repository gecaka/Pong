/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong;

/* Java imports*/
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/* Library imports */
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/* My imports */
import pong.engine.Program;
import utils.Helper;
import static utils.Helper.cX;
import static utils.Helper.cY;

/**
 *
 * @author Georgi Getsov
 */

public class BaseObject {
    
    protected final static byte num_floats_pos = 3;
    protected final static byte num_floats_col = 4;
    protected final static byte float_byte_size = 4;
    protected final static byte stride_byte_size = ( num_floats_pos + num_floats_col ) * float_byte_size;
    
    protected float x, y, width, height;
    
    protected final static float speed = 5;
    
    protected float[] vertices;
    
    protected static byte[] indecies = {
        
        0,1,2, //bottom triangle
        
        2,3,0  //upper tringle
        
    };
    
    protected FloatBuffer attrib_buffer;
    protected ByteBuffer index_buffer; 
    
    protected int attrib_bufferID, index_bufferID;
    
    public BaseObject( float x_pos, float y_pos, int widthIn, int heightIn ){
        
        x = x_pos;
        y = y_pos;
        
        width = widthIn;
        height = heightIn;
        
        vertices = new float[]
        {
            cX( x ), cY( y ), 1f, //1 vertex position
            1f, 1f, 1f, 1f, // color

            cX( x ), cY( y + height ), 1f, //2
            1f, 1f, 1f, 1f,

            cX( x + width ), cY( y + height ), 1f, //3
            1f, 1f, 1f, 1f,

            cX( x + width ), cY( y ), 1f, //4
            1f, 1f, 1f, 1f
        }; 
        
        attrib_bufferID = glGenBuffers();
        index_bufferID = glGenBuffers();
        
    }
    
    protected void render(){
        
        vertices[0] = cX( x ); //vector 1 x
        vertices[1] = cY( y ); //vector 1 y
            
        vertices[7] = cX( x ); //vector 2 x
        vertices[8] = cY( y + height ); //vector 2 y 
            
        vertices[14] = cX( x + width ); //vector 3 x
        vertices[15] = cY( y + height ); //vector 3 y
            
        vertices[21] = cX( x + width ); //vector 4 x
        vertices[22] = cY( y ); //vector 4 y
        
        glBindBuffer( GL_ARRAY_BUFFER, attrib_bufferID );
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, index_bufferID );
    
        attrib_buffer = Helper.makeFloatBuffer( vertices );
        index_buffer = Helper.makeByteBuffer( indecies );
        
        //sets up gl for vertex buffer
        glBufferData( GL_ARRAY_BUFFER ,attrib_buffer, GL_STATIC_DRAW );
        
        int pos_atrr = glGetAttribLocation( Program.getCurrentProgram(), "position" ); // get the position atribute from the program
        int col_attr = glGetAttribLocation( Program.getCurrentProgram(), "color" );
        
        glEnableVertexAttribArray( pos_atrr );
        glEnableVertexAttribArray( col_attr );
        
        glVertexAttribPointer( pos_atrr, num_floats_pos, GL_FLOAT, false, stride_byte_size, 0 ); //last two atributes are calculated in bytes () //specify how the info(buffers) by the attribute should be read
        glVertexAttribPointer( col_attr, num_floats_col, GL_FLOAT, false, stride_byte_size, 12 ); // calc goes like 3 floats(vert)+4float(color) = 7 * sizeof(float)'4' = 7*4 = 28
        
        //set up gl for index buffer
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, index_buffer, GL_DYNAMIC_DRAW );
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 ); // have to unbind element buffer before calling drawEements !!!
        
        glDrawElements( GL_TRIANGLES, index_buffer );
        
    }
    
    protected void dX( float pixels ){
        
        x += pixels;
    }
    
    protected void dY( float pixels ){
        
        y += pixels;
    }
    
    public float getTopBounds(){    
        return y ;
    }
    
    public float getBottomBounds(){
        return y + height ;
    }
    
    public float getLeftBounds(){
        return x ;
    }
        
    public float getRightBounds(){
        return x + width ;
    }
    
}
