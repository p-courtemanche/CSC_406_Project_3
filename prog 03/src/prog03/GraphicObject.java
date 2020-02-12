package prog03;

import java.awt.Color;

import processing.core.*; 

//NEW
//	The class is entirely new, but it's > 90% made up of code from the BoxV1V2 and
//	EllipseV1V2 classes.

/**	This abstract class is the parent class for all classes of graphic objects.  
 *  It stores all the instance variable describing the position-dimension-color-motion
 *  attributes of a graphic object.  It defines that abstract draw and update methods that 
 * 	its subclasses must implement
 * 
 * @author jyh and Paige Courtemanche
 *
 */
public abstract class GraphicObject implements ApplicationConstants
{
	//-------------------------------------
	//	Class constants
	//-------------------------------------

	/**	Minimum width of an object
	 * 
	 */
	public final static float MIN_WIDTH = 0.05f;
	
	/**	Maximum width of an object
	 * 
	 */
	public final static float MAX_WIDTH = 1.0f;
	
	/**	Minimum height of an object
	 * 
	 */
	public final static float MIN_HEIGHT = 0.05f;
	
	/**	Maximum height of an object
	 * 
	 */
	public final static float MAX_HEIGHT = 1.0f;
	
	/**	Minimum speed of an object
	 * 
	 */
	final static float MIN_SPEED = 0.05f;
	
	/**	Maximum speed of an object
	 * 
	 */
	public final static float MAX_SPEED = 1.0f;
	
	/**	Minimum (unsigned) spin of an object
	 * 
	 */
	public final static float MIN_SPIN = PApplet.PI/6;
	
	/**	Maximum (unsigned) spin of an object
	 * 
	 */
	public final static float MAX_SPIN = PApplet.PI;
	
	
	/** The amount of times the object has been clicked
	 * 
	 */
	public int hits = 0;
	
	/** Boolean that tracks whether an object is selected or not
	*
	*/
	public boolean selected = false;
	
	
	
	//-------------------------------------
	//	Instance variables
	//	As explained in an earlier version, in an ideal OOP implementation we would want all the instance
	//	variables to be private and only be accessible through setter and getter methods.
	//	I generally want you to develop good code, but here I feel that all the added code would detract from the 
	//	main purpose of the course, which is animation.  So, I declare all my instance variables "protected"
	//	in the parent class.
	//-------------------------------------

	/**	x coordinate of the object's center (in world coordinates)
	 * 
	 */
	protected float x_;
	
	/**	y coordinate of the object's center (in world coordinates)
	 * 
	 */
	protected float y_;
	
	/**	Orientation of the object (in rad)
	 * 
	 */
	protected float angle_;
	
	/**	width of the object (in world units)
	 * 
	 */
	protected float width_;
	
	/**	height of the object (in world units)
	 * 
	 */
	protected float height_;
		
	/**	Horizontal component of the object's velocity vector (in world units per second)
	 * 
	 */
	protected float vx_;
	
	/**	Vertical component of the object's velocity vector (in world units per second)
	 * 
	 */
	protected float vy_;
	

	/**	Spin of the object (in rad/s)
	 * 
	 */
	protected float spin_;
	
	/**	These floats are combined to form the color of the object
	 * 
	 */
	float red_;
	float green_;
	float blue_;
	float alpha_;
	
	

	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param red		the value of the red component for the object's color
	 * @param green     the value of the green component for the object's color
	 * @param blue      the value of the blue component for the object's color
	 * @param alpha     the opacity of the object
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public GraphicObject(float x, float y, float angle, float width, float height, 
						   float red, float green, float blue, float alpha, float vx, float vy, float spin)
	{
		x_ = x;
		y_ = y;
		angle_ = angle;
		width_ = width;
		height_ = height;
		red_ = red; 
		green_ = green;
		blue_ = blue;
		alpha_ = alpha;
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
	}
	
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public GraphicObject() {
		x_ = (float) (Math.random()*(XMAX-XMIN) + XMIN);
		y_ = (float) (Math.random()*(YMAX-YMIN) + YMIN);
		angle_ = (float)(Math.random()*2*Math.PI);
		width_ = (float) (Math.random()*(MAX_WIDTH-MIN_WIDTH) + MIN_WIDTH);;
		height_ = (float) (Math.random()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);
		//color_ = opacity << 24  | randomByte_() << 16  | randomByte_() << 8 | randomByte_(); // 0xFF0000FF;
		//color_ = 0xA0<<24 | 0xFF<<16 | 0x80<<8 | 0x00; // 0xFF0000FF;
		red_ = randColor_(); // a random value is chosen to represent to eight bits
		green_ = randColor_();
		blue_ = randColor_();
		alpha_ = 255; //fully opaque
		double heading = 2*Math.PI*Math.random();
		double v = (float) (Math.random()*(MAX_SPEED-MIN_SPEED) + MIN_SPEED);
		vx_ = (float)(v * Math.cos(heading));
		vy_ = (float)(v * Math.sin(heading));
		spin_ = (float) (Math.random()*(MAX_SPIN-MIN_SPIN) + MIN_SPIN);
	}

	/**	Renders this object.
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	public void draw(PApplet app) {
		app.pushMatrix();
		
		//	common to all GraphicObject objects
		app.translate(x_, y_);
		app.rotate(angle_);
		app.fill(red_, green_, blue_, alpha_);
		
		//if the object is selected, then it gets an outline
		if (selected == false) { 
			app.noStroke();
		} else {
			app.strokeWeight(DRAW_IN_PIXELS_SCALE * 5);
			app.stroke(0);
		}
		
		//	Specific to the subclass
		draw_(app);
				
		app.popMatrix();
	}

	/**	Declare the method that subclasses must implement
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	protected abstract void draw_(PApplet app);
	
	
	//	Not really new.  I just wanted to attract your attention to the fact that the update code
	//	being the same for both types, I don't need to have a separate implementation for
	//	the different subclasses.  However, since in Java all methods are implicitly virtual,
	//	if a child class implements it own update method overriding this one, the sbuclass's 
	//	update method will be the one invoked.
	/**	Updates the position and orientation of the object 
	 * @param dt	Time elapsed since the last invocation of this method
	 */
	public void update(float dt) {
		x_ += vx_ * dt;
		y_ += vy_ * dt;
		angle_ += spin_ * dt;
		
		//	right-edge bump
		if (x_ > XMAX)
		{
			x_ = XMAX;
			vx_ = -vx_;
		}
		//	left-edge bump
		else if (x_ < XMIN)
		{
			x_ = XMIN;
			vx_ = -vx_;
		}
		
		//	top-edge bump
		if (y_ > YMAX)
		{
			y_ = YMAX;
			vy_ = -vy_;
		}
		//	bottom-edge bump
		else if (y_ < YMIN)
		{
			y_ = YMIN;
			vy_ = -vy_;
		}
		
	}	
	
	/** This function increases the recorded number of hits an object has and decreases and object's opacity
	 * 
	 * @return If the number of hits is still less than the maximum number of hits
	 */
	public boolean hit() {
		hits++;
		// decreases the opacity of the object by 33%
		alpha_ -= 85; 
		return (hits < 3);
	}

	/** Checks whether a mouse click is on an object or not
	 * 
	 * @param x		the coordinate of a mouse click on the x-axis
	 * @param y		the coordinate of a mouse click on the y-axis
	 * @return 		boolean describing whether the mouse click is on an object or not
	 */
	public abstract boolean isInside(float x, float y);
	
	
//	public void select() {
//		selected = true;
//	}
//	
//	
//	public void deselect() {
//		selected = false;
//	}
	
	/** Switches the value of the boolean select
	 *  If true, it becomes false, and vice versa
	 */
	public void toggleSelected() {
		selected = !selected;
	}
	
	/** States whether a function is selected or not
	 * 
	 * @return The status of the boolean variable selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**	Utility random generator 
	 * 
	 * @return	random unsigned float value [0-255]
	 */
	private static float randColor_() {
		return (float)(256*Math.random());
	}
	
}
