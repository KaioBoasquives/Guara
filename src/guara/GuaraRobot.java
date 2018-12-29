package guara;

import java.util.ArrayList;

//import us.ihmc.robotics.Axis;
import us.ihmc.euclid.Axis;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
//SimulationConstructionSet;
import us.ihmc.simulationconstructionset.FloatingJoint;
import us.ihmc.simulationconstructionset.GroundContactPoint;
import us.ihmc.simulationconstructionset.Joint;
import us.ihmc.simulationconstructionset.Link;
import us.ihmc.simulationconstructionset.PinJoint;
import us.ihmc.simulationconstructionset.Robot;
import us.ihmc.simulationconstructionset.UniversalJoint;
import us.ihmc.simulationconstructionset.util.LinearGroundContactModel;
import us.ihmc.simulationconstructionset.util.ground.FlatGroundProfile;

public class GuaraRobot extends Robot
{
   private final ArrayList<GroundContactPoint> groundContactPoints = new ArrayList();
   private final Joint rootJoint, abdFlexHip0, flexKnee0, flexAnkle0, abdFlexHip1, flexKnee1, flexAnkle1, abdFlexHip2, flexKnee2, flexAnkle2, abdFlexHip3,
         flexKnee3, flexAnkle3;
   public static final double // robot's body height
   hBodyZ = 0.36;
   public static final double // body data TO VERIFY
   lBodyX = 0.6, lBodyY = 0.4, lBodyZ = 0.1, mBody = 10.0, IxxBody = 0.1, IyyBody = 0.1, IzzBody = 0.1;
   public static final double // thigh data
   lThighX = 0.04 / 2, lThighY = 0.06 / 3, lThighZ = 0.15, mThigh = 5.63, IxxThigh = inertiaMoment(lThighY, lThighZ),
         IyyThigh = inertiaMoment(lThighX, lThighZ), IzzThigh = inertiaMoment(lThighX, lThighY);
   public static final double // shank data
   lShankX = 0.04 / 2, lShankY = 0.06 / 3, lShankZ = 0.15, mShank = 3.6, IxxShank = inertiaMoment(lShankY, lShankZ), IyyShank = inertiaMoment(lShankX, lShankZ),
         IzzShank = inertiaMoment(lShankX, lShankY);
   public static final double // foot data TO VERIFY
   lFootX = 0.08 / 3, lFootY = 0.06 / 3, lFootZ = 0.1, mFoot = 1.0, IxxFoot = inertiaMoment(lFootY, lFootZ), IyyFoot = inertiaMoment(lFootX, lFootZ),
         IzzFoot = inertiaMoment(lFootX, lFootY);
   public static final double // gearmotor data
   hMotor = 0.12 / 2, rMotor = 0.034 / 2;
   public static final double // robot's data
   lRobot = 0.6, wRobot = 0.36,
         // joint's height
         hThigh = lThighZ + lShankZ + lFootZ, // hip
         hKnee = lShankZ + lFootZ, // knee
         hFoot = lFootZ; // ankle

   private double abdHip, flexHip, flexKnee, flexAnkle;

   // Setting Variables for guara`s posture
   /*
    * Theta will be the Knee joint angle rotation Psi will be the Ankle joint
    * angle of rotation Phi will be the AbduHip joint Y axis angle of rotation
    * lThighZ is l1 e lShankZ is l2 length
    */

   public double theta, thetacount, psi, phiY, phiX, h;

   public GuaraRobot()
   {

      // legs are numbered: 0 front left; 1 hind left; 2 front right; 3 hind right
      super("Guara");

      h = lThighZ + lShankZ + lFootX + 0.1;
      //		h = Math.sqrt(2 * lThighZ * lShankZ * Math.cos(theta) + Math.pow(lThighZ, 2) + Math.pow(lShankZ, 2));

      // Setting Variables for guara`s posture;
      /*
       * phiX and phiY will be the abdFlexHip joint anklePositionVector and Y
       * axis angle of rotation thetaY will be the flexKnee joint rotation angle
       * psiY will be the flexAnkle joint rotation angle lThighZ for l1 e
       * lShankZ for l2
       */

      phiX = Math.PI / 12; // PhiX will be the abdFlexHip joint
      theta = -Math.PI / 4; // Theta will be the flexKnee joint rotation angle
      phiY = Math.asin(lShankZ * Math.sin(theta) / h); // PhiY will be the
      psi = Math.acos((lThighZ) * Math.sin(theta) / h); // Psi will be the

      thetacount = -theta;

      rootJoint = new FloatingJoint("rootJoint", new Vector3D(0.0, 0.0, 0.0), this);

      ((FloatingJoint) rootJoint).setPosition(0.0, 0.0, h);/// * hThigh
      /// */h*2);
      Link bodyLink = body();
      rootJoint.setLink(bodyLink);
      this.addRootJoint(rootJoint);
      bodyLink.addCoordinateSystemToCOM(0.5);

      // Hip Joint setup as Universal Joint from leg 0, and follow up joints,
      // knee and ankle
      abdFlexHip0 = new UniversalJoint("abdHip0", "flexHip0", new Vector3D(lRobot / 2, wRobot / 2, 0.0), this, Axis.X, Axis.Y);
      rootJoint.addJoint(abdFlexHip0);
      Link tigh0 = thigh(0);
      abdFlexHip0.setLink(tigh0);
      //      tigh0.addCoordinateSystemToCOM(0.25);

      flexKnee0 = new PinJoint("flexKnee0", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);
      abdFlexHip0.addJoint(flexKnee0);
      Link shank0 = shank();
      flexKnee0.setLink(shank0);
      //      shank0.addCoordinateSystemToCOM(0.25);

      flexAnkle0 = new PinJoint("flexAnkle0", new Vector3D(0.0, 0.0, -lShankZ), this, Axis.Y);
      flexKnee0.addJoint(flexAnkle0);
      Link foot0 = foot();
      flexAnkle0.setLink(foot0);
      //      foot0.addCoordinateSystemToCOM(0.25);

      // Hip Joint setup as Universal Joint from leg 1, and follow up joints,
      // knee and ankle
      abdFlexHip1 = new UniversalJoint("abdHip1", "flexHip1", new Vector3D(-lRobot / 2, wRobot / 2, 0.0), this, Axis.X, Axis.Y);
      rootJoint.addJoint(abdFlexHip1);
      Link tigh1 = thigh(1);
      abdFlexHip1.setLink(tigh1);
      //      tigh1.addCoordinateSystemToCOM(0.25);

      flexKnee1 = new PinJoint("flexKnee1", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);
      abdFlexHip1.addJoint(flexKnee1);
      Link shank1 = shank();
      flexKnee1.setLink(shank1);

      flexAnkle1 = new PinJoint("flexAnkle1", new Vector3D(0.0, 0.0, -lShankZ), this, Axis.Y);
      flexKnee1.addJoint(flexAnkle1);
      Link foot1 = foot();
      flexAnkle1.setLink(foot1);
      //      foot1.addCoordinateSystemToCOM(0.25);

      // Hip Joint setup as Universal Joint from leg 2, and follow up joints,
      // knee and ankle
      abdFlexHip2 = new UniversalJoint("abdHip2", "flexHip2", new Vector3D(-lRobot / 2, -wRobot / 2, 0.0), this, Axis.X, Axis.Y);
      rootJoint.addJoint(abdFlexHip2);
      Link tigh2 = thigh(2);
      abdFlexHip2.setLink(tigh2);
      //      tigh2.addCoordinateSystemToCOM(0.25);

      flexKnee2 = new PinJoint("flexKnee2", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);
      abdFlexHip2.addJoint(flexKnee2);
      Link shank2 = shank();
      flexKnee2.setLink(shank2);

      flexAnkle2 = new PinJoint("flexAnkle2", new Vector3D(0.0, 0.0, -lShankZ), this, Axis.Y);
      flexKnee2.addJoint(flexAnkle2);
      Link foot2 = foot();
      flexAnkle2.setLink(foot2);

      // Hip Joint setup as Universal Joint from leg 3, and follow up joints,
      // knee and ankle
      abdFlexHip3 = new UniversalJoint("abdHip3", "flexHip3", new Vector3D(lRobot / 2, -wRobot / 2, 0.0), this, Axis.X, Axis.Y);
      rootJoint.addJoint(abdFlexHip3);
      Link tigh3 = thigh(3);
      abdFlexHip3.setLink(tigh3);

      flexKnee3 = new PinJoint("flexKnee3", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);
      abdFlexHip3.addJoint(flexKnee3);
      Link shank3 = shank();
      flexKnee3.setLink(shank3);

      flexAnkle3 = new PinJoint("flexAnkle3", new Vector3D(0.0, 0.0, -lShankZ), this, Axis.Y);
      flexKnee3.addJoint(flexAnkle3);
      Link foot3 = foot();
      flexAnkle3.setLink(foot3);

      // Add ground contact points

      GroundContactPoint gcToe0 = new GroundContactPoint("gcToe0", new Vector3D(0.0, 0.0, -lFootZ), this);
      flexAnkle0.addGroundContactPoint(gcToe0);
      groundContactPoints.add(gcToe0);
      GroundContactPoint gcHeel0 = new GroundContactPoint("gcHeel0", new Vector3D(0.0, 0.0, 0.0), this);
      flexAnkle0.addGroundContactPoint(gcHeel0);
      groundContactPoints.add(gcHeel0);

      GroundContactPoint gcToe1 = new GroundContactPoint("gcToe1", new Vector3D(0.0, 0.0, -lFootZ), this);
      groundContactPoints.add(gcToe1);
      flexAnkle1.addGroundContactPoint(gcToe1);
      GroundContactPoint gcHeel1 = new GroundContactPoint("gcHeel1", new Vector3D(0.0, 0.0, 0.0), this);
      groundContactPoints.add(gcHeel1);
      flexAnkle1.addGroundContactPoint(gcHeel1);

      GroundContactPoint gcToe2 = new GroundContactPoint("gcToe2", new Vector3D(0.0, 0.0, -lFootZ), this);
      flexAnkle2.addGroundContactPoint(gcToe2);
      groundContactPoints.add(gcToe2);
      GroundContactPoint gcHeel2 = new GroundContactPoint("gcHeel2", new Vector3D(0.0, 0.0, 0.0), this);
      flexAnkle2.addGroundContactPoint(gcHeel2);
      groundContactPoints.add(gcHeel2);

      GroundContactPoint gcToe3 = new GroundContactPoint("gcToe3", new Vector3D(0.0, 0.0, -lFootZ), this);
      flexAnkle3.addGroundContactPoint(gcToe3);
      groundContactPoints.add(gcToe3);
      GroundContactPoint gcHeel3 = new GroundContactPoint("gcHeel3", new Vector3D(0.0, 0.0, 0.0), this);
      flexAnkle3.addGroundContactPoint(gcHeel3);
      groundContactPoints.add(gcHeel3);

      LinearGroundContactModel ground = new LinearGroundContactModel(this, this.getRobotsYoVariableRegistry());
      ground.setZStiffness(2000.0);
      ground.setZDamping(1500.0);
      ground.setXYStiffness(50000.0);
      ground.setXYDamping(2000.0);
      ground.setGroundProfile3D(new FlatGroundProfile());
      this.setGroundContactModel(ground);

      abdHip = 0.0;//Math.PI / 12;
      flexHip = Math.PI/12;
      flexKnee = Math.PI / 4;

      //hip joint angles

      ((UniversalJoint) abdFlexHip0).setInitialState(abdHip, 0, -flexHip, 0);
      ((UniversalJoint) abdFlexHip1).setInitialState(abdHip, 0, flexHip, 0);
      ((UniversalJoint) abdFlexHip2).setInitialState(abdHip, 0, flexHip, 0);
      ((UniversalJoint) abdFlexHip3).setInitialState(abdHip, 0, -flexHip, 0);

      //Knee Joint Angles

      ((PinJoint) flexKnee0).setInitialState(-flexKnee, 0);
      ((PinJoint) flexKnee1).setInitialState(flexKnee, 0);
      ((PinJoint) flexKnee2).setInitialState(flexKnee, 0);
      ((PinJoint) flexKnee3).setInitialState(-flexKnee, 0);

      //Ankle Joint angles

      ((PinJoint) flexAnkle0).setInitialState((-flexHip+flexKnee)/2, 0);
      ((PinJoint) flexAnkle1).setInitialState(-flexHip-flexKnee, 0);
      ((PinJoint) flexAnkle2).setInitialState(-flexHip-flexKnee, 0);
      ((PinJoint) flexAnkle3).setInitialState((-flexHip+flexKnee)/2, 0);

   }

   /*
    * x axis is red, y axis is white, and z axis is blue.
    */
   private Link body()
   {
      Link ret = new Link("Body");
      ret.setMass(mBody);
      ret.setComOffset(0, 0, 0);
      ret.setMomentOfInertia(IxxBody, IyyBody, IzzBody);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.addCube(4 * lBodyX / 5, lBodyY, lBodyZ / 2, YoAppearance.RGBColorFrom8BitInts(180, 76, 0));
      // linkGraphics.addCoordinateSystem(1);
      ret.setLinkGraphics(linkGraphics);
      // roll DOF gearmotor legs 0 and 3 is backward
      linkGraphics.translate(lBodyX / 2 - hMotor, lBodyY / 2 - rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      linkGraphics.identity();
      linkGraphics.translate(lBodyX / 2 - hMotor, -lBodyY / 2 + rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      // roll DOF gearmotor legs 1 and 2 is forward
      linkGraphics.identity();
      linkGraphics.translate(-lBodyX / 2, lBodyY / 2 - rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      linkGraphics.identity();
      linkGraphics.translate(-lBodyX / 2, -lBodyY / 2 + rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      return ret;
   }

   private Link thigh(int legNumber)
   {
      Link ret = new Link("Thigh");
      ret.setMass(mThigh);
      ret.setComOffset(0, 0, lThighZ / 2);
      ret.setMomentOfInertia(IxxThigh, IyyThigh, IzzThigh);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.translate(0, 0, -lThighZ); // coxa
      linkGraphics.addCube(lThighX, lThighY, lThighZ, YoAppearance.DarkSlateGrey());//
      linkGraphics.translate(0, 0, lThighZ); // juntas do quadril
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      // linkGraphics.addCoordinateSystem(0.1);
      ret.setLinkGraphics(linkGraphics);
      return ret;
   }

   private Link shank()
   {
      Link ret = new Link("Shank");
      ret.setMass(mShank);
      ret.setComOffset(0.0, 0.0, lShankZ / 2);
      ret.setMomentOfInertia(IxxShank, IyyShank, IzzShank);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.translate(0, 0, -lShankZ); // shank
      linkGraphics.addCube(lShankX, lShankY, lShankZ, YoAppearance.DarkSlateGrey());
      linkGraphics.translate(0, 0, +lShankZ); // shank
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      ret.setLinkGraphics(linkGraphics);
      return ret;
   }

   private Link foot()
   {
      Link ret = new Link("Foot");
      ret.setMass(mFoot);
      ret.setComOffset(0.0, 0.0, lFootZ / 2);
      ret.setMomentOfInertia(0.0, IyyFoot, 0.0);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.identity();
      linkGraphics.translate(0, 0, -lFootZ); // feet
      linkGraphics.addCube(lFootX, lFootY, lFootZ, YoAppearance.BlackMetalMaterial());
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, lFootZ, 0.0);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.RGBColorFrom8BitInts(70, 130, 180));
      ret.setLinkGraphics(linkGraphics);
      return ret;
   }

   public UniversalJoint getAbdFlexHip0()
   {
      return (UniversalJoint) abdFlexHip0;
   }

   public PinJoint getFlexKnee0()
   {
      return (PinJoint) flexKnee0;
   }

   public PinJoint getFlexAnkle0()
   {
      return (PinJoint) flexAnkle0;
   }

   public UniversalJoint getAbdFlexHip1()
   {
      return (UniversalJoint) abdFlexHip1;
   }

   public PinJoint getFlexKnee1()
   {
      return (PinJoint) flexKnee1;
   }

   public PinJoint getFlexAnkle1()
   {
      return (PinJoint) flexAnkle1;
   }

   static double inertiaMoment(double sideOne, double sideTwo)
   {
      return (Math.pow(sideOne, 2) + Math.pow(sideTwo, 2));
   }

   public UniversalJoint getAbdFlexHip2()
   {
      return (UniversalJoint) abdFlexHip2;
   }

   public PinJoint getFlexKnee2()
   {
      return (PinJoint) flexKnee2;
   }

   public PinJoint getFlexAnkle2()
   {
      return (PinJoint) flexAnkle2;
   }

   public UniversalJoint getAbdFlexHip3()
   {
      return (UniversalJoint) abdFlexHip3;
   }

   public PinJoint getFlexKnee3()
   {
      return (PinJoint) flexKnee3;
   }

   public PinJoint getFlexAnkle3()
   {
      return (PinJoint) flexAnkle3;
   }

   public double a2()
   {
      return lThighZ;
   }

   public double a3()
   {
      return lShankZ;
   }

   double a4()
   {
      return lFootZ;
   }

   public double theta()
   {
      return theta;
   }

   public GuaraRobot guaraRobot()
   {
      return this;
   }

   public double getAbdHip()
   {
      return abdHip;
   }

   public double getFlexHip()
   {
      return flexHip;
   }

   public double getFlexKnee()
   {
      return flexKnee;
   }

   public double getFlexAnkle()
   {
      return flexAnkle;
   }

   public Joint getRootJoint()
   {
      return rootJoint;
   }

}