package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class VehicleSimulator2D extends JPanel implements KeyListener, ActionListener
{
	private Vehicle2D vehicle;
	private final Timer timer;
	private BlockTerrain terrain;
	private boolean forward, backward;
	private boolean showDebugInfo = true;
	private String currentPreset = "6 wheels";

	public VehicleSimulator2D()
	{
		vehicle = createSixWheelVehicle(100, 100);
		currentPreset = "6 wheels";
		terrain = new BlockTerrain(800, 600);
		setBackground(new Color(135, 206, 235));
		setFocusable(true);
		addKeyListener(this);
		timer = new Timer(16, this);
		timer.start();
	}

	private Vehicle2D createThreeWheelVehicle(double x, double y)
	{
		Vehicle2D v = new Vehicle2D(x, y);
		v.wheels.clear();
		v.wheels.add(new Wheel2D(-20, 8, false, true));
		v.wheels.add(new Wheel2D(0, 8, false, true));
		v.wheels.add(new Wheel2D(20, 8, false, false));
		v.width = 64;
		return v;
	}

	private Vehicle2D createTwoWheelVehicle(double x, double y)
	{
		Vehicle2D v = new Vehicle2D(x, y);
		v.wheels.clear();
		v.wheels.add(new Wheel2D(-20, 8, false, true));
		v.wheels.add(new Wheel2D(20, 8, false, false));
		v.width = 64;
		return v;
	}

	private Vehicle2D createSixWheelVehicle(double x, double y)
	{
		Vehicle2D v = new Vehicle2D(x, y);
		v.wheels.clear();
		v.wheels.add(new Wheel2D(-24, 8, false, true));
		v.wheels.add(new Wheel2D(-12, 8, false, true));
		v.wheels.add(new Wheel2D(0, 8, false, true));
		v.wheels.add(new Wheel2D(12, 8, false, false));
		v.wheels.add(new Wheel2D(24, 8, false, false));
		v.width = 80;
		return v;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		terrain.draw(g2);
		vehicle.draw(g2);

		if(showDebugInfo)
		{
			g2.setColor(Color.BLACK);
			g2.drawString("Pitch: "+String.format("%.1f", Math.toDegrees(vehicle.pitch))+"°", 10, 20);
			g2.drawString("Velocity: "+String.format("%.1f", Math.sqrt(vehicle.vx*vehicle.vx+vehicle.vy*vehicle.vy)), 10, 40);
			g2.drawString("Position: ("+(int)vehicle.x+", "+(int)vehicle.y+")", 10, 60);
			g2.drawString("Wheel Contacts: "+vehicle.getWheelContacts(), 10, 80);
			g2.drawString("Controls: W/S - Accelerate, Q/E - Power, R - Reset, D - Debug, 1-3 - Preset", 10, 100);
			g2.drawString("Power: x"+String.format("%.2f", vehicle.enginePowerScale), 10, 120);
			g2.drawString("Preset: "+currentPreset, 10, 140);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		vehicle.update(forward, backward, terrain);
		repaint();
	}

	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if(code==KeyEvent.VK_W) forward = true;
		if(code==KeyEvent.VK_S) backward = true;
		if(code==KeyEvent.VK_D) showDebugInfo = !showDebugInfo;
		if(code==KeyEvent.VK_1)
		{
			vehicle = createTwoWheelVehicle(100, 100);
			currentPreset = "2 wheels";
		}
		if(code==KeyEvent.VK_2)
		{
			vehicle = createThreeWheelVehicle(100, 100);
			currentPreset = "3 wheels";
		}
		if(code==KeyEvent.VK_3)
		{
			vehicle = createSixWheelVehicle(100, 100);
			currentPreset = "6 wheels";
		}
		if(code==KeyEvent.VK_Q) vehicle.enginePowerScale = Math.max(0.2, vehicle.enginePowerScale-0.1);
		if(code==KeyEvent.VK_E) vehicle.enginePowerScale = Math.min(3.0, vehicle.enginePowerScale+0.1);
		if(code==KeyEvent.VK_R)
		{
			vehicle = createSixWheelVehicle(100, 100);
			currentPreset = "6 wheels";
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if(code==KeyEvent.VK_W) forward = false;
		if(code==KeyEvent.VK_S) backward = false;
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("2D Vehicle Simulator - Minecraft Style");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(new VehicleSimulator2D());
		frame.setVisible(true);
	}
}

class Vehicle2D
{
	double x, y;
	double vx, vy;
	double pitch;
	double angularVel;
	double filteredPitch = 0.0;

	// Physics constants
	double gravity = 0.3;
	double damping = 0.95;
	double angularDamping = 0.8;
	double maxPitch = Math.toRadians(70);

	// Traction/drag tuning
	private static final double MU_STATIC = 0.92;
	private static final double MU_KINETIC = 0.85;
	private static final double ROLLING_RESIST = 0.02;
	private static final double SLOPE_DRAG = 0.5;
	private static final double MAX_STEP_PX = 24.0;

	public double enginePowerScale = 1.0;

	List<Wheel2D> wheels = new ArrayList<>();
	Color bodyColor = new Color(200, 100, 50);
	int width = 48;
	int height = 24;

	public Vehicle2D(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.pitch = 0;
		wheels.add(new Wheel2D(-16, 8, false, true));
		wheels.add(new Wheel2D(16, 8, false, false));
	}

	public void update(boolean forward, boolean backward, BlockTerrain terrain)
	{
		vy += gravity;

		double drivePower = 0;
		if(forward) drivePower += 1.2;
		if(backward) drivePower -= 0.8;
		drivePower *= enginePowerScale;

		applyWheelForces(drivePower, terrain);

		vx *= damping;
		vy *= damping;
		angularVel *= angularDamping;

		x += vx;
		y += vy;
		pitch += angularVel;

		resolveGroundPenetration(terrain);
		pitch = Math.max(-maxPitch, Math.min(maxPitch, pitch));

		// Keep in bounds
		if(x < 0)
		{
			x = 0;
			vx = 0;
		}
		if(x > 800)
		{
			x = 800;
			vx = 0;
		}
		if(y > 600)
		{
			y = 600;
			vy = 0;
			vx *= 0.8;
			angularVel *= 0.8;
		}
	}

	private void applyWheelForces(double drivePower, BlockTerrain terrain)
	{
		filteredPitch += 0.5*(pitch-filteredPitch);
		final double cosF = Math.cos(filteredPitch);
		final double sinF = Math.sin(filteredPitch);
		final double sinFGrade = (Math.abs(sinF) < 0.5)?0.0: sinF;

		double totalForceX = 0;
		double totalForceY = 0;
		double torque = 0;

		boolean anySupport = false;
		double sumContactWeight = 0.0;
		double refGroundY = Double.POSITIVE_INFINITY;
		boolean hasRefGround = false;

		for(Wheel2D w : wheels)
		{
			double wx = x+Math.cos(pitch)*w.localX-Math.sin(pitch)*w.localY;
			double wy = y+Math.sin(pitch)*w.localX+Math.cos(pitch)*w.localY;
			TerrainCollision c = terrain.checkWheelCollision(wx, wy, w.radius);
			if(c!=null&&c.isColliding) sumContactWeight += 1.0;
			if(w.onGround||w.hysteresisContact)
			{
				hasRefGround = true;
				refGroundY = Math.min(refGroundY, w.groundContactY);
			}
		}
		if(sumContactWeight <= 1e-6) sumContactWeight = 1.0;

		int supportCount = 0;

		for(Wheel2D wheel : wheels)
		{
			double wheelX = x+Math.cos(pitch)*wheel.localX-Math.sin(pitch)*wheel.localY;
			double wheelY = y+Math.sin(pitch)*wheel.localX+Math.cos(pitch)*wheel.localY;
			wheel.worldX = wheelX;
			wheel.worldY = wheelY;

			TerrainCollision collision = terrain.checkWheelCollision(wheelX, wheelY, wheel.radius);
			boolean realCollision = (collision!=null&&collision.isColliding);

			// FIXED: Better step climbing logic
			if(realCollision&&hasRefGround)
			{
				double stepUpHeight = Math.max(0.0, refGroundY-collision.contactY);
				// Only allow climbing if the step is reasonably connected to current ground
				if(stepUpHeight > MAX_STEP_PX+0.1||!isStepConnectedToGround(wheelX, refGroundY, terrain))
					realCollision = false;
			}

			boolean hysteresisSupport = false;
			double supportScale = 1.0;

			if(!realCollision&&wheel.onGround)
			{
				double wheelBottom = wheelY+wheel.radius;
				double holdSkin = 5.0;
				if(wheelBottom >= wheel.groundContactY-holdSkin&&wheelBottom <= wheel.groundContactY+holdSkin)
				{
					wheel.outOfContactFrames++;
					wheel.hysteresisContact = true;
					hysteresisSupport = true;
					supportScale = Math.max(0.18, 1.0-0.15*wheel.outOfContactFrames);
					collision = new TerrainCollision(true, wheel.groundContactY);
				}
			}

			if(realCollision||hysteresisSupport)
			{
				supportCount++;
				anySupport = true;

				if(realCollision)
				{
					wheel.onGround = true;
					wheel.outOfContactFrames = 0;
					wheel.hysteresisContact = false;
					wheel.groundContactY = collision.contactY;
				}
				else wheel.onGround = false;

				double penetration = (wheelY+wheel.radius)-collision.contactY;
				double springSupport = Math.max(0, penetration)*1.5;
				double damperSupport = (Math.max(0, penetration)-wheel.prevPenetration)*3.0;
				double weightedNormal = gravity*1.5*(1.0/sumContactWeight);
				double upSupport = (springSupport+damperSupport+weightedNormal)*supportScale;

				double vForward = vx*cosF-vy*sinF;
				double desiredTangential = (wheel.drive?drivePower*0.6: 0.0)+(-vForward*ROLLING_RESIST);
				double gradeOppose = Math.max(0.0, -Math.signum(vForward)*sinFGrade);
				desiredTangential += -Math.abs(vForward)*SLOPE_DRAG*gradeOppose;

				double maxStatic = MU_STATIC*upSupport;
				double maxKinetic = MU_KINETIC*upSupport;
				double tangentialApplied = (Math.abs(desiredTangential) <= maxStatic)?
						desiredTangential: Math.signum(desiredTangential)*maxKinetic;

				double fx = cosF*tangentialApplied;
				double fy = -sinF*tangentialApplied-upSupport;

				wheel.lastFx = fx;
				wheel.lastFy = fy;
				wheel.penetration = Math.max(0, penetration);
				wheel.prevPenetration = wheel.penetration;

				totalForceX += fx;
				totalForceY += fy;
				torque += (wheel.localX*fy-wheel.localY*fx)*0.008;
			}
			else
			{
				wheel.onGround = false;
				wheel.hysteresisContact = false;
				wheel.outOfContactFrames++;
				wheel.lastFx = 0;
				wheel.lastFy = 0;
				wheel.penetration = 0;
				wheel.prevPenetration = 0;
			}
		}

		if(anySupport)
		{
			vx += totalForceX;
			vy += totalForceY;
			angularVel += torque;

			if(Math.abs(vy) < 0.25) vy = 0;
			vy *= 0.88;
			if(supportCount >= 2) angularVel *= 0.9;
			else angularVel *= 0.95;
		}

		adjustPitchToTerrain();
	}

	// NEW: Check if a step is actually connected to the current ground
	private boolean isStepConnectedToGround(double wheelX, double currentGroundY, BlockTerrain terrain)
	{
		// Check if there's a continuous path from current ground to the step
		int steps = 5;
		double stepSize = 8.0;
		double checkY = currentGroundY;

		for(int i = 0; i < steps; i++)
		{
			TerrainCollision col = terrain.checkWheelCollision(wheelX, checkY-16, 1);
			if(col!=null&&col.isColliding)
			{
				// Found connected ground
				return true;
			}
			checkY -= stepSize;
			if(checkY < currentGroundY-MAX_STEP_PX)
				break;
		}
		return false;
	}

	private void resolveGroundPenetration(BlockTerrain terrain)
	{
		double maxPenetration = 0.0;
		boolean contact = false;

		for(Wheel2D wheel : wheels)
		{
			double wheelX = x+Math.cos(pitch)*wheel.localX-Math.sin(pitch)*wheel.localY;
			double wheelY = y+Math.sin(pitch)*wheel.localX+Math.cos(pitch)*wheel.localY;
			wheel.worldX = wheelX;
			wheel.worldY = wheelY;

			TerrainCollision collision = terrain.checkWheelCollision(wheelX, wheelY, wheel.radius);
			if(collision!=null&&collision.isColliding)
			{
				wheel.onGround = true;
				wheel.groundContactY = collision.contactY;
				double penetration = (wheelY+wheel.radius)-collision.contactY;
				if(penetration > 0)
				{
					maxPenetration = Math.max(maxPenetration, penetration);
					contact = true;
					wheel.penetration = penetration;
				}
			}
			else
			{
				wheel.onGround = false;
				wheel.penetration = 0;
			}
		}

		if(contact&&maxPenetration > 0)
		{
			y -= (maxPenetration+0.5);
			if(vy > 0) vy = 0;
			angularVel *= 0.9;
		}
	}

	private void adjustPitchToTerrain()
	{
		if(wheels.size() < 2) return;

		// Find the lowest (most supported) and highest wheels
		Wheel2D lowestWheel = null;
		Wheel2D highestWheel = null;
		double minHeight = Double.POSITIVE_INFINITY;
		double maxHeight = Double.NEGATIVE_INFINITY;

		for(Wheel2D wheel : wheels)
		{
			if(wheel.onGround||wheel.hysteresisContact)
			{
				if(wheel.groundContactY < minHeight)
				{
					minHeight = wheel.groundContactY;
					lowestWheel = wheel;
				}
				if(wheel.groundContactY > maxHeight)
				{
					maxHeight = wheel.groundContactY;
					highestWheel = wheel;
				}
			}
		}

		// If we have at least one supported wheel, adjust pitch based on terrain slope
		if(lowestWheel!=null&&highestWheel!=null&&lowestWheel!=highestWheel)
		{
			double heightDiff = highestWheel.groundContactY-lowestWheel.groundContactY;
			double wheelBase = Math.abs(highestWheel.localX-lowestWheel.localX);

			if(wheelBase > 1.0)
			{
				double terrainSlope = Math.atan2(heightDiff, wheelBase);

				// NEW: Consider movement direction when adjusting pitch
				// When reversing, we want the pitch to align with the terrain in the opposite direction
				double movementDirection = Math.signum(vx);
				if(Math.abs(vx) < 0.5) movementDirection = 0; // Stationary or very slow

				// Only adjust pitch if we're moving significantly or if the slope is gentle
				if(Math.abs(vx) > 1.0||Math.abs(terrainSlope) < Math.toRadians(30))
				{
					// Smooth adjustment towards terrain slope
					double pitchDiff = terrainSlope-pitch;
					double adjustment = pitchDiff*0.1; // Reduced from 0.08 to 0.1 for more responsiveness

					// NEW: When reversing, be more conservative with pitch adjustments
					if(movementDirection < 0&&pitch > Math.toRadians(20))
					{
						// When reversing at high positive pitch, allow quicker recovery
						adjustment *= 1.5;
					}

					pitch += Math.max(-0.05, Math.min(0.05, adjustment));
				}
			}
		}
		else if(lowestWheel!=null)
		{
			// Only one wheel supported - gently level the vehicle
			double levelingForce = -pitch*0.02;
			pitch += Math.max(-0.01, Math.min(0.01, levelingForce));
		}

		// NEW: Additional stabilization when reversing on slopes
		if(Math.abs(vx) > 0.5)
		{
			// Reduce pitch when moving to prevent getting stuck at extreme angles
			double pitchReduction = -pitch*0.02*Math.abs(vx)*0.1;
			pitch += pitchReduction;
		}
	}

	public int getWheelContacts()
	{
		int contacts = 0;
		for(Wheel2D wheel : wheels) if(wheel.onGround) contacts++;
		return contacts;
	}

	public void draw(Graphics2D g2)
	{
		AffineTransform old = g2.getTransform();
		g2.translate(x, y);
		g2.rotate(pitch);

		g2.setColor(bodyColor);
		g2.fillRect(-width/2, -height/2, width, height);

		g2.setColor(new Color(100, 50, 20));
		for(int i = -1; i <= 1; i++)
		{
			int blockX = i*16;
			g2.drawRect(-width/2+blockX, -height/2, 16, 16);
		}

		g2.setColor(Color.RED);
		g2.fillOval(-8, -20, 16, 16);
		g2.setColor(Color.BLUE);
		g2.fillRect(-4, -4, 8, 12);

		for(Wheel2D wheel : wheels)
		{
			AffineTransform wheelTransform = g2.getTransform();
			g2.translate(wheel.localX, wheel.localY);

			if(wheel.drive) g2.setColor(new Color(150, 0, 0));
			else g2.setColor(new Color(80, 80, 80));

			if(wheel.climbProgress > 0.1) g2.setColor(new Color(255, 165, 0));

			g2.fillOval((int)-wheel.radius, (int)-wheel.radius, (int)(wheel.radius*2), (int)(wheel.radius*2));
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillOval((int)(-wheel.radius/2), (int)(-wheel.radius/2), (int)wheel.radius, (int)wheel.radius);
			g2.setTransform(wheelTransform);
		}

		g2.setTransform(old);

		g2.setColor(Color.CYAN);
		g2.drawLine((int)x, (int)y, (int)(x+vx*8), (int)(y+vy*8));

		for(Wheel2D wheel : wheels)
			if(wheel.onGround||wheel.hysteresisContact)
			{
				g2.setColor(wheel.onGround?Color.GREEN: Color.YELLOW);
				g2.fillOval((int)wheel.worldX-3, (int)wheel.groundContactY-3, 6, 6);

				if(wheel.penetration > 0.1)
				{
					g2.setColor(Color.RED);
					g2.drawLine((int)wheel.worldX, (int)wheel.worldY,
							(int)wheel.worldX, (int)(wheel.groundContactY-wheel.radius));
				}
			}
	}
}

class Wheel2D
{
	double localX, localY;
	boolean steerable;
	boolean drive;
	double radius = 8;

	double lastFx, lastFy;
	double worldX, worldY;
	boolean onGround = false;
	double groundContactY;
	double penetration = 0;
	double weightShare = 0.5;
	double prevPenetration = 0.0;

	boolean hysteresisContact = false;
	int outOfContactFrames = 0;
	double climbProgress = 0.0;

	public Wheel2D(double localX, double localY, boolean steerable, boolean drive)
	{
		this.localX = localX;
		this.localY = localY;
		this.steerable = steerable;
		this.drive = drive;
	}
}

class BlockTerrain
{
	private int width, height;
	private boolean[][] blocks;
	private static final int BLOCK_SIZE = 16;
	private static final double CONTACT_SKIN = 2.5;

	private Color[] blockColors = {
			new Color(80, 110, 50),
			new Color(120, 85, 60),
			new Color(100, 100, 100),
			new Color(41, 41, 41),
	};

	public BlockTerrain(int width, int height)
	{
		this.width = width;
		this.height = height;
		generateTerrain();
	}

	private void generateTerrain()
	{
		int cols = width/BLOCK_SIZE+1;
		int rows = height/BLOCK_SIZE+1;
		blocks = new boolean[cols][rows];

		int groundLevel = height/BLOCK_SIZE-3;

		for(int x = 0; x < cols; x++)
		{
			int heightVariation = (int)(Math.sin(x*0.2)*2);
			int currentGround = groundLevel+heightVariation;

			for(int y = currentGround; y < rows; y++)
				blocks[x][y] = true;

			// Add some platforms and obstacles
			if(x > 30&&x < 40)
				for(int y = groundLevel-1; y < groundLevel+3; y++)
					blocks[x][y] = true;
			if(x > 45&&x < 50)
				blocks[x][groundLevel-10] = true;
		}
	}

	public TerrainCollision checkWheelCollision(double wheelX, double wheelY, double wheelRadius)
	{
		int colLeft = (int)Math.floor((wheelX-wheelRadius)/BLOCK_SIZE);
		int colRight = (int)Math.floor((wheelX+wheelRadius)/BLOCK_SIZE);
		colLeft = Math.max(0, colLeft);
		colRight = Math.min(blocks.length-1, colRight);
		if(colLeft > colRight) return new TerrainCollision(false, 0);

		double wheelBottom = wheelY+wheelRadius;
		double bestContactY = Double.NEGATIVE_INFINITY;
		boolean found = false;

		for(int col = colLeft; col <= colRight; col++)
		{
			int maxRow = Math.min(blocks[0].length-1, (int)Math.floor((wheelBottom+CONTACT_SKIN)/BLOCK_SIZE));
			boolean prevSolid = false;
			for(int row = 0; row <= maxRow; row++)
			{
				boolean solid = blocks[col][row];
				if(solid&&!prevSolid)
				{
					double topY = row*BLOCK_SIZE;
					if(topY <= wheelBottom+CONTACT_SKIN&&topY > bestContactY)
					{
						bestContactY = topY;
						found = true;
					}
				}
				prevSolid = solid;
			}
		}

		if(found&&wheelBottom >= bestContactY-CONTACT_SKIN)
			return new TerrainCollision(true, bestContactY);

		return new TerrainCollision(false, 0);
	}

	public void draw(Graphics2D g2)
	{
		g2.setColor(new Color(135, 206, 235));
		g2.fillRect(0, 0, width, height);

		for(int x = 0; x < blocks.length; x++)
		{
			int blockLevel = -1;
			for(int y = 0; y < blocks[0].length; y++)
				if(blocks[x][y])
				{
					int worldX = x*BLOCK_SIZE;
					int worldY = y*BLOCK_SIZE;

					if(blockLevel < 2) blockLevel++;
					if(y==blocks[0].length-1) blockLevel = 3;

					Color blockColor = blockColors[blockLevel];
					g2.setColor(blockColor);
					g2.fillRect(worldX, worldY, BLOCK_SIZE, BLOCK_SIZE);
				}
		}
	}
}

class TerrainCollision
{
	boolean isColliding;
	double contactY;

	public TerrainCollision(boolean isColliding, double contactY)
	{
		this.isColliding = isColliding;
		this.contactY = contactY;
	}
}
