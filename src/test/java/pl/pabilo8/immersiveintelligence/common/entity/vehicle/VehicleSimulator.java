package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.util.math.MathHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class VehicleSimulator extends JPanel implements KeyListener, ActionListener
{
	private Vehicle vehicle;
	private final Timer timer;

	private boolean forward, backward, left, right;

	//Added a global list to store all vehicles
	public static List<Vehicle> allVehicles = new ArrayList<>();

	public VehicleSimulator()
	{
		vehicle = Vehicle.createPreset(1);
		allVehicles.add(vehicle); //Add the main vehicle to the global list
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);

		timer = new Timer(16, this);
		timer.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(Vehicle v : allVehicles)
			v.draw((Graphics2D)g); //Draw all vehicles
	}

	public void actionPerformed(ActionEvent e)
	{
		vehicle.update(forward, backward, left, right);
		repaint();
	}

	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if(code==KeyEvent.VK_W) forward = true;
		if(code==KeyEvent.VK_S) backward = true;
		if(code==KeyEvent.VK_A) left = true;
		if(code==KeyEvent.VK_D) right = true;
		if(code==KeyEvent.VK_TAB)
		{
			vehicle.toggleSteeringMode();
			System.out.println("Steering mode: "+vehicle.steeringMode);
		}
		if(code >= KeyEvent.VK_0&&code <= KeyEvent.VK_9)
		{
			allVehicles.clear();
			int preset = code-KeyEvent.VK_0;
			VehicleSimulator.allVehicles.add(vehicle = Vehicle.createPreset(preset));
			System.out.println("Switched to preset: "+preset);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if(code==KeyEvent.VK_W) forward = false;
		if(code==KeyEvent.VK_S) backward = false;
		if(code==KeyEvent.VK_A) left = false;
		if(code==KeyEvent.VK_D) right = false;
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Vehicle Wheel Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(new VehicleSimulator());
		frame.setVisible(true);
	}
}

class Vehicle
{
	double z, x, angle;
	double vz, vx, angularVel;

	double damping = 0.98;
	double angularDamping = 0.9;

	List<Wheel> wheels = new ArrayList<>();
	List<Box> boxes = new ArrayList<>();
	List<AttachmentPoint> attachmentPoints = new ArrayList<>();

	//store last-frame forces for debug drawing
	double totalFz, totalFx;

	//bounding box for drawing
	int width = 100;
	int height = 60;

	public String steeringMode = "normal";

	public Vehicle(double x, double z)
	{
		this.x = x;
		this.z = z;
		this.angle = 0;

		//4-wheel layout (2 front steerable, 2 rear drive)
		wheels.add(new Wheel(-20, -30, false, true));
		wheels.add(new Wheel(-20, -60, false, true));
		wheels.add(new Wheel(-20, 30, true, false));

		wheels.add(new Wheel(20, -30, false, true));
		wheels.add(new Wheel(20, -60, false, true));
		wheels.add(new Wheel(20, 30, true, false));

		addBox(0, 0, 100, 60, Color.WHITE);
	}

	public void toggleSteeringMode()
	{
		if(steeringMode.equals("normal"))
			steeringMode = "inverted";
		else
			steeringMode = "normal";
	}

	public Box addBox(double localX, double localZ, int boxWidth, int boxHeight, Color color)
	{
		Box box = new Box(localX, localZ, boxWidth, boxHeight, color);
		boxes.add(box);
		updateBoundingBox();
		return box;
	}

	public AttachmentPoint addAttachmentPoint(double localX, double localZ)
	{
		AttachmentPoint point = new AttachmentPoint(localX, localZ);
		attachmentPoints.add(point);
		return point;
	}

	private void updateBoundingBox()
	{
		//Find min/max extents from wheels and boxes
		double minZ = 0, maxZ = 0, minX = 0, maxX = 0;
		for(Wheel w : wheels)
		{
			if(w.localZ-5 < minZ) minZ = w.localZ-5;
			if(w.localZ+5 > maxZ) maxZ = w.localZ+5;
			if(w.localX-5 < minX) minX = w.localX-5;
			if(w.localX+5 > maxX) maxX = w.localX+5;
		}
		for(Box b : boxes)
		{
			if(b.localZ-(double)b.width/2 < minZ) minZ = b.localZ-(double)b.width/2;
			if(b.localZ+(double)b.width/2 > maxZ) maxZ = b.localZ+(double)b.width/2;
			if(b.localX-(double)b.height/2 < minX) minX = b.localX-(double)b.height/2;
			if(b.localX+(double)b.height/2 > maxX) maxX = b.localX+(double)b.height/2;
		}
		width = (int)Math.ceil(maxZ-minZ);
		height = (int)Math.ceil(maxX-minX);
	}

	public void update(boolean forward, boolean backward, boolean left, boolean right)
	{
		if(steeringMode.equals("normal"))
		{
			//Car-like steering
			double steerInput = 0;
			if(left) steerInput -= 1;
			if(right) steerInput += 1;

			double drivePower = 0;
			if(forward) drivePower += 1;
			if(backward) drivePower -= 1;

			double steerAngle = steerInput*Math.toRadians(45);

			for(Wheel w : wheels)
				if(w.steerable)
					w.currentSteerAngle = steerAngle;

			applyWheelForces(drivePower, drivePower, false);


		}
		else
		{
			//Tank-style steering: A/D set left/right drive, ignore steerable wheels
			double leftDrive = 0, rightDrive = 0;
			if(forward)
			{
				leftDrive += 1;
				rightDrive += 1;
			}
			else if(backward)
			{
				leftDrive -= 1;
				rightDrive -= 1;
			}
			else if(left)
				leftDrive -= 1;
			else if(right)
				rightDrive += 1;

			//Set all steerable wheels to 0 angle
			for(Wheel w : wheels)
				if(w.steerable)
					w.currentSteerAngle = 0;

			applyWheelForces(leftDrive, rightDrive, true);
		}

		//Update attached vehicles
		for(AttachmentPoint attachmentPoint : this.attachmentPoints)
			attachmentPoint.updateAttachedVehicle(this.x, this.z, this.angle);
	}

	private void applyWheelForces(double leftDrive, double rightDrive, boolean tankMode)
	{
		double totalForceZ = 0;
		double totalForceX = 0;
		double torque = 0;
		int numWheels = wheels.size();
		for(Wheel w : wheels)
		{
			//wheel position in world coordinates
			double worldWheelX = x+MathHelper.sin((float)angle)*w.localZ+MathHelper.cos((float)angle)*w.localX;
			double worldWheelZ = z+MathHelper.cos((float)angle)*w.localZ-MathHelper.sin((float)angle)*w.localX;
			//wheel orientation in world
			double wheelAngle = angle+w.currentSteerAngle;
			double sinA = MathHelper.sin((float)wheelAngle);
			double cosA = MathHelper.cos((float)wheelAngle);
			//velocity at wheel position (vehicle velocity + rotational component)
			double relX = worldWheelX-x;
			double relZ = worldWheelZ-z;
			double wheelVz = vz-angularVel*relX;
			double wheelVx = vx+angularVel*relZ;
			//project velocity onto wheel axes
			double vLat = -wheelVz*sinA+wheelVx*cosA;
			//drive force
			double driveForce = 0;
			if(w.drive)
				if(tankMode)
					if(leftDrive==rightDrive)
						driveForce = leftDrive;
					else if(w.localX > 0)
						driveForce = leftDrive*0.5;
					else
						driveForce = -rightDrive*0.5;
				else
					driveForce = leftDrive*0.5; //in car mode, leftDrive==rightDrive==drivePower
			//lateral friction (all wheels)
			double latFrictionCoef = w.drive?0.15: 0.5;
			double latFriction = -vLat*latFrictionCoef;
			//Clamp and normalize lateral friction
			double maxLateralForce = 1.0/numWheels;
			if(latFriction > maxLateralForce) latFriction = maxLateralForce;
			if(latFriction < -maxLateralForce) latFriction = -maxLateralForce;
			//total force in wheel's local axes
			double fx = -sinA*driveForce+cosA*latFriction;
			double fz = -cosA*driveForce-sinA*latFriction;
			w.lastFx = fx;
			w.lastFz = fz;
			w.worldX = worldWheelX;
			w.worldZ = worldWheelZ;
			totalForceX += fx;
			totalForceZ += fz;
			torque += (relZ*fx-relX*fz)*0.001;
		}
		totalFz = totalForceZ;
		totalFx = totalForceX;
		vz += totalForceZ;
		vx += totalForceX;
		angularVel += torque;
		vz *= damping;
		vx *= damping;
		angularVel *= angularDamping;
		if(Math.abs(angularVel) < 0.01)
			angularVel = 0;
		z += vz;
		x += vx;
		angle += angularVel;
	}

	public void draw(Graphics2D g2)
	{
		AffineTransform old = g2.getTransform();
		g2.translate(z, x);
		g2.rotate(angle);

		g2.setColor(Color.LIGHT_GRAY);
		g2.drawRect(-width/2, -height/2, width, height);

		//Draw boxes
		for(Box b : boxes)
		{
			g2.setColor(b.color);
			g2.fillRect((int)(b.localZ-b.width/2), (int)(b.localX-b.height/2), b.width, b.height);
		}

		//wheels and their local vectors
		for(Wheel w : wheels)
		{
			AffineTransform wheelTx = g2.getTransform();

			g2.translate(w.localZ, w.localX);
			g2.rotate(w.currentSteerAngle);

			if(w.drive)
				g2.setColor(Color.RED);
			else if(w.steerable)
				g2.setColor(Color.BLUE);
			else
				g2.setColor(Color.GRAY);

			int size = 10;
			g2.fillRect(-size/2, -size/2, size, size);

			//draw force vector (in wheel's local rotation)
			g2.setColor(Color.RED);
			g2.drawLine(0, 0, (int)(w.lastFz*200), (int)(w.lastFx*200));

			g2.setTransform(wheelTx);
		}

		for(AttachmentPoint attachmentPoint : attachmentPoints)
		{
			g2.setColor(attachmentPoint.isAttached()?Color.GREEN: Color.MAGENTA);
			int size = 6;
			g2.fillOval((int)(attachmentPoint.localZ-size/2), (int)(attachmentPoint.localX-size/2), size, size);
		}

		g2.setTransform(old);

		//draw vehicle velocity vector
		g2.setColor(Color.CYAN);
		g2.drawLine((int)z, (int)x, (int)(z+vz*50), (int)(x+vx*50));
	}

	public static Vehicle createPreset(int presetId)
	{
		Vehicle v;
		switch(presetId)
		{
			case 2: //Truck
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(-35, -40, true, false));
				v.wheels.add(new Wheel(35, -40, true, false));
				v.wheels.add(new Wheel(-35, 20, false, true));
				v.wheels.add(new Wheel(35, 20, false, true));
				v.wheels.add(new Wheel(-35, 45, false, true));
				v.wheels.add(new Wheel(35, 45, false, true));
				v.addBox(0, 0, 50, 80, Color.LIGHT_GRAY);
				v.steeringMode = "normal";
				v.damping = 0.98;
				v.angularDamping = 0.9;
				break;
			case 3: //Tank
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				for(int i = -60; i <= 60; i += 20)
				{
					v.wheels.add(new Wheel(-40, i, false, false)); //left track
					v.wheels.add(new Wheel(40, i, false, false));  //right track
				}
				v.wheels.get(v.wheels.size()-1).drive = true;
				v.wheels.get(v.wheels.size()-2).drive = true;

				v.addBox(0, 0, 60, 80, new Color(120, 120, 80));
				v.steeringMode = "inverted";
				v.damping = 0.74;
				v.angularDamping = 0.96;
				break;
			case 4: //Motorcycle
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(0, -20, true, false)); //front steer
				v.wheels.add(new Wheel(0, 20, false, true));  //rear drive
				v.addBox(0, 0, 40, 15, Color.DARK_GRAY);
				v.steeringMode = "normal";
				v.damping = 0.98;
				v.angularDamping = 0.9;
				break;
			case 5: //Bus
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(-40, -35, true, false));
				v.wheels.add(new Wheel(40, -35, true, false));
				v.wheels.add(new Wheel(-40, 0, false, true));
				v.wheels.add(new Wheel(40, 0, false, true));
				v.wheels.add(new Wheel(-40, 35, false, true));
				v.wheels.add(new Wheel(40, 35, false, true));
				v.addBox(0, 0, 50, 120, Color.YELLOW);
				v.steeringMode = "normal";
				v.damping = 0.98;
				v.angularDamping = 0.9;
				break;
			case 6: //Pushed field gun
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(-40, 0, true, true));
				v.wheels.add(new Wheel(40, 0, true, true));
				v.addBox(0, 0, 50, 120, Color.YELLOW);
				v.steeringMode = "inverted";
				v.damping = 0.4;
				v.angularDamping = 0.5;
				break;
			case 1: //Standard car
			default:
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(20, -30, true, false)); //front left steer
				v.wheels.add(new Wheel(-20, -30, true, false));  //front right steer
				v.wheels.add(new Wheel(20, 30, false, true));  //rear left drive
				v.wheels.add(new Wheel(-20, 30, false, true));   //rear right drive
				v.addBox(0, 0, 40, 60, Color.WHITE);
				v.steeringMode = "normal";
				v.damping = 0.98;
				v.angularDamping = 0.9;
				break;
			case 7: //Car with trailer
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(20, -30, true, false)); //front left steer
				v.wheels.add(new Wheel(-20, -30, true, false)); //front right steer
				v.wheels.add(new Wheel(20, 30, false, true)); //rear left drive
				v.wheels.add(new Wheel(-20, 30, false, true)); //rear right drive
				v.addBox(0, 0, 40, 60, Color.WHITE);
				//Attachment point for trailer
				v.addAttachmentPoint(0, 50);

				Vehicle trailer = new Vehicle(300, 460);
				trailer.wheels.clear();
				trailer.boxes.clear();
				AttachmentPoint trailerPoint = trailer.addAttachmentPoint(0, -40);
				trailer.wheels.add(new Wheel(20, 0, true, false)); //trailer wheels
				trailer.wheels.add(new Wheel(-20, 0, true, false));
				trailer.addBox(0, 0, 20, 20, Color.GRAY);
				VehicleSimulator.allVehicles.add(trailer);

				v.attachmentPoints.get(0).attachVehicle(trailer, trailerPoint);
				v.updateBoundingBox();
				return v;

			case 8: //Train (locomotive + 3 wagons)
				v = new Vehicle(300, 400);
				v.wheels.clear();
				v.boxes.clear();
				v.wheels.add(new Wheel(20, -30, true, true)); //locomotive wheels
				v.wheels.add(new Wheel(-20, -30, true, true));
				v.addBox(0, 0, 40, 80, Color.RED);
				v.addAttachmentPoint(0, 50); //Attachment point for first wagon

				Vehicle wagon1 = new Vehicle(300, 480);
				wagon1.wheels.clear();
				wagon1.boxes.clear();
				wagon1.wheels.add(new Wheel(20, -30, false, false));
				wagon1.wheels.add(new Wheel(-20, -30, false, false));
				wagon1.addBox(0, 0, 40, 60, Color.ORANGE);
				AttachmentPoint p1 = wagon1.addAttachmentPoint(0, -50);
				wagon1.addAttachmentPoint(0, 50); //Attachment point for second wagon
				VehicleSimulator.allVehicles.add(wagon1);

				Vehicle wagon2 = new Vehicle(300, 560);
				wagon2.wheels.clear();
				wagon2.boxes.clear();
				wagon2.wheels.add(new Wheel(20, -30, false, false));
				wagon2.wheels.add(new Wheel(-20, -30, false, false));
				wagon2.addBox(0, 0, 40, 60, Color.YELLOW);
				AttachmentPoint p2 = wagon2.addAttachmentPoint(0, -50);
				wagon2.addAttachmentPoint(0, 50); //Attachment point for third wagon
				VehicleSimulator.allVehicles.add(wagon2);

				Vehicle wagon3 = new Vehicle(300, 640);
				wagon3.wheels.clear();
				wagon3.boxes.clear();
				wagon3.wheels.add(new Wheel(20, -30, false, false));
				wagon3.wheels.add(new Wheel(-20, -30, false, false));
				AttachmentPoint p3 = wagon3.addAttachmentPoint(0, -50);
				wagon3.addBox(0, 0, 40, 60, Color.GREEN);
				VehicleSimulator.allVehicles.add(wagon3);

				v.attachmentPoints.get(0).attachVehicle(wagon1, p1);
				wagon1.attachmentPoints.get(1).attachVehicle(wagon2, p2);
				wagon2.attachmentPoints.get(1).attachVehicle(wagon3, p3);

				v.updateBoundingBox();
				return v;
		}
		v.updateBoundingBox();
		return v;
	}
}

class Wheel
{
	double localZ, localX;
	boolean steerable;
	boolean drive;
	double currentSteerAngle = 0;

	//debug info
	double lastFx, lastFz;
	double worldX, worldZ;

	public Wheel(double localX, double localZ, boolean steerable, boolean drive)
	{
		this.localZ = localZ;
		this.localX = localX;
		this.steerable = steerable;
		this.drive = drive;
	}
}

class Box
{
	double localZ, localX;
	int width, height;
	Color color;

	public Box(double localX, double localZ, int width, int height, Color color)
	{
		this.localZ = localZ;
		this.localX = localX;
		this.width = width;
		this.height = height;
		this.color = color;
	}
}

// Updated AttachmentPoint to support input and output attachment points
class AttachmentPoint
{
	double localX, localZ; // Position relative to the vehicle
	Vehicle attachedVehicle; // The vehicle attached to this point
	AttachmentPoint inputPoint; // Input point of the attached vehicle

	public AttachmentPoint(double localX, double localZ)
	{
		this.localX = localX;
		this.localZ = localZ;
	}

	public void attachVehicle(Vehicle vehicle, AttachmentPoint inputPoint)
	{
		this.attachedVehicle = vehicle;
		this.inputPoint = inputPoint;
	}

	public void detachVehicle()
	{
		this.attachedVehicle = null;
		this.inputPoint = null;
	}

	public boolean isAttached()
	{
		return attachedVehicle!=null&&inputPoint!=null;
	}

	public void updateAttachedVehicle(double parentX, double parentZ, double parentAngle)
	{

		if(isAttached())
		{
			// Calculate the position of the output attachment point in world coordinates
			double worldOutputX = parentX+MathHelper.sin((float)parentAngle)*localZ+MathHelper.cos((float)parentAngle)*localX;
			double worldOutputZ = parentZ+MathHelper.cos((float)parentAngle)*localZ-MathHelper.sin((float)parentAngle)*localX;

			// Calculate the direction vector from the attached vehicle to the attachment point
			double deltaX = worldOutputX-attachedVehicle.x;
			double deltaZ = worldOutputZ-attachedVehicle.z;
			double distance = Math.sqrt(deltaX*deltaX+deltaZ*deltaZ);

			// Pull the attached vehicle towards the attachment point
			if(distance > 0) // Avoid division by zero
			{
				double pullStrength = 0.5; // Adjust this value to control the pulling force
				deltaX *= pullStrength/distance;
				deltaZ *= pullStrength/distance;
				attachedVehicle.x += deltaX;
				attachedVehicle.z += deltaZ;
			}

			// Adjust the angle of the attached vehicle gradually
			double targetAngle = Math.atan2(attachedVehicle.z-worldOutputZ, worldOutputX-attachedVehicle.x)-1.57;
			double angleDifference = targetAngle-attachedVehicle.angle;
			// Normalize the angle difference to the range [-π, π]
			angleDifference = ((angleDifference+Math.PI)%(2*Math.PI)-Math.PI);
			attachedVehicle.angle += angleDifference*0.1; // Adjust this factor to control the rate of angle adjustment

			// Clamp the attached vehicle to the exact attachment point
			double inputLocalZ = inputPoint.localZ;
			double inputLocalX = inputPoint.localX;
			attachedVehicle.x = worldOutputX-MathHelper.sin((float)parentAngle)*inputLocalZ-MathHelper.cos((float)parentAngle)*inputLocalX;
			attachedVehicle.z = worldOutputZ-MathHelper.cos((float)parentAngle)*inputLocalZ+MathHelper.sin((float)parentAngle)*inputLocalX;

			// Propagate updates to any vehicles attached to the attached vehicle
			attachedVehicle.update(false, false, false, false);
		}
	}
}
