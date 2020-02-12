package prog03;

import java.awt.Point;
//import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import processing.core.*; 

/**	This is a version of the animation project that uses inheritance (both the Box and
 *  Ellipse classes derive from GraphicObject).  
 *  
 * @author jyh and Paige Courtemanche
 *
 */
public class AnimationApp extends PApplet implements ApplicationConstants {

	/**	Variable keeping track of the last time the update method was invoked.
	 * 	The different between current time and last time is sent to the update
	 * 	method of each object. 
	 */
	int lastUpdateTime;
	
	/**	List storing references to all graphic objects in the app.  
	 */
	private ArrayList<GraphicObject> objectList_;

	/**	Index for calls to the draw() callback function
	 */
	int frameCount = 0;	
	
	/** Integer keeping track of the player's score
	 * Cannot go below zero
	 */
	int score = 0;
	
	/**	The desired rendering frame rate
	 * 
	 */
	final int RENDERING_FRAME_RATE = 60;
	
	/**	Ratio of update frames to rendering frames, when there is need for fine
	 * simulation.
	 */
	final int ANIMATION_TO_RENDERING_RATIO = 5;	

	
	/**	First instance method invoked by Processing.
	 * 	Creates the canvas at dimensions specified
	 */
	public void settings() {
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	/** Second method invoked by processing.
	 * 	Performs all sorts of allocations and initializations prior to entering
	 *  callback-driven main loop.
	 */
	public void setup() {
		if (BAIL_OUT_IF_ASPECT_RATIOS_DONT_MATCH) {
			if (Math.abs(WORLD_HEIGHT - PIXEL_TO_WORLD*WINDOW_HEIGHT) > 1.0E5) {
				System.out.println("World and Window aspect ratios don't match");
				System.exit(1);
			}
		}
		
		objectList_ = new ArrayList<GraphicObject>();
		//	Object creation
		//					            x    y   o    w     h    color       vx    vy       spin
		//objectList_.add(new Ellipse(-1.0f, -1, 0, 0.6f, 0.3f, 0xFFFF00FF, -0.5f, 0.2f, 2*PApplet.PI));
		//objectList_.add(new Ellipse(randX, randY, 0, randW, randH, 0xFFFF00FF, randVX, randVY, 0));
		for (int k=0; k<3; k++) {
			objectList_.add(new Ellipse());
		}
		//				       x    y  o    w     h    color       vx    vy       spin
		//objectList_.add(new Box(1.5f, 0, 0, 0.5f, 0.2f, 0xFF2200FF, 1.5f, -0.5f, -PApplet.PI));
		//objectList_.add(new Box(randX, randY, 0, randW, randH, 0xFF2200FF, randVX, randVY, 0));
		for (int k=0; k<3; k++) {
			objectList_.add(new Box());
		}
		
		frameRate(RENDERING_FRAME_RATE*ANIMATION_TO_RENDERING_RATIO);
		lastUpdateTime = millis();
	}

	/**	Callback function that displays the scene.
	 * 
	 */
	public void draw() {
		if (frameCount % ANIMATION_TO_RENDERING_RATIO == 0) {
			pushMatrix();
			//	Move to World reference frame before anything else
			translate(WORLD_X, WORLD_Y);
			scale(DRAW_IN_WORLD_UNITS_SCALE, -DRAW_IN_WORLD_UNITS_SCALE); // y points up

			//=========================================

			background(127);

			//	stroke 1 pixel wide
			strokeWeight(DRAW_IN_PIXELS_SCALE*1);

			//for (GraphicObject obj : objectList_)
			for (GraphicObject obj: objectList_)
			{
				obj.draw(this);
			}
			popMatrix();
			
			// depicts the extra credit score
			textSize(20);
			fill(0);
			text("Score: " + score, 10, 30);
			
		}
		
		update();
		frameCount++;
	}  
	
	/** Function that finds the location where the mouse click was released
	 * The score and object both change depending on the click
	 */
	public void mouseReleased() {
		// Keeps track of whether the mouse click is on empty space or not
		// Becomes true if there is an object at the coordinate of the mouse click
		boolean inside = false;
		
		// coordinate of the mouse click in terms of the world
		float[] coordinate = pixelToWorld(mouseX, mouseY);
		
		// The placement of each object in the list is checked and then compared to where the mouse was released 
		for (int i = objectList_.size() - 1; i >= 0; i--) {
			GraphicObject obj = objectList_.get(i);
		
			if (obj.isInside(coordinate[0], coordinate[1])) { 
				inside = true;
				score++;
				
				// If the mouse was left-clicked where an object was,
				// Then the score and the object's hits both increase
				if (mouseButton == LEFT) {
					if (!obj.hit()) {
						score += 5;
						objectList_.remove(i);
					}
				}
				// If the mouse was right-clicked where an object was, then that object is selected
				else {
					obj.toggleSelected();
				}					
			}
		}
		// If the left click was outside an object, score decreases
		if (inside == false) {
			score -= 10;
			
			// Score can never be less than zero
			if (score < 0) {
				score = 0;
			}
		}
	}
	
	/** This function deletes all selected objects when the 'A' key is pressed
	 * The 'delete' key is not used because professor Herve said that the delete key would not work on my mac
	 */
	public void keyReleased() { 
		//This is not case sensitive
		if (key == 'A' || key == 'a') {
			
			for (int i = objectList_.size() - 1; i >= 0; i--)
			{
				GraphicObject obj = objectList_.get(i);
				if(obj.isSelected() == true) {
					objectList_.remove(i);
				}
			}
		}
	}
	
	
	/**	This method is used for animation (update the sate of the various objects
	 * in the world (whether visible or not).
	 */
	public void update() {
		//  update the state of the objects ---> physics
		int time = millis();
		float dt = (time - lastUpdateTime)*0.001f;
		
		//NEW
		for (GraphicObject obj : objectList_)
		{
			obj.update(dt);
		}


		lastUpdateTime = time;
	}
	
	/**	Converts pixel coordinates to world coordinates.  Used e.g. to
	 * place the location of a mouse click in the world. 
	 * @param ix		pixel coordinate along the x axis
	 * @param iy		pixel coordinate along the y axis
	 * @return
	 */
	public float[] pixelToWorld(int ix, int iy) {
		float []pt = {(ix-WORLD_X)*PIXEL_TO_WORLD,
					  -(iy-WORLD_Y)*PIXEL_TO_WORLD};
		return pt;
	}

	
	/**	Converts world coordinates into pixel coordinates. 
	 * @param x		pixel coordinate along the x axis
	 * @param y		pixel coordinate along the y axis
	 * @return
	 */
	public int[] worldToPixel(float x, float y) {
		int []pt = {(int)Math.round(WORLD_X + x*WORLD_TO_PIXEL),
					(int)Math.round(WORLD_Y - y*WORLD_TO_PIXEL)};
		return pt;
	}

	
	static public void main(String[] passedArgs) {
		PApplet.main("prog03.AnimationApp");
	}
}
