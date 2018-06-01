package guara;

import java.util.ArrayList;

import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
import us.ihmc.robotics.Axis;
//SimulationConstructionSet;
import us.ihmc.simulationconstructionset.FloatingJoint;
import us.ihmc.simulationconstructionset.GroundContactModel;
import us.ihmc.simulationconstructionset.GroundContactPoint;
import us.ihmc.simulationconstructionset.Joint;
import us.ihmc.simulationconstructionset.Link;
import us.ihmc.simulationconstructionset.PinJoint;
import us.ihmc.simulationconstructionset.Robot;
import us.ihmc.simulationconstructionset.UniversalJoint;
import us.ihmc.simulationconstructionset.util.LinearGroundContactModel;

public class GuaraRobot extends Robot
{

   private final ArrayList<GroundContactPoint> groundContactPoints = new ArrayList();
   public static final double // robot's body height

   hBodyZ = 0.36;

   public static final double // body data TO VERIFY

   lBodyX = 0.6, lBodyY = 0.4, lBodyZ = 0.1, mBody = 10.0, IxxBody = 0.1, IyyBody = 0.1, IzzBody = 0.1;

   public static final double // thigh data

   lThighX = 0.04, lThighY = 0.06, lThighZ = 0.15, mThigh = 5.63, IxxThigh = 0.077, IyyThigh = 0.077, IzzThigh = 0.003;

   public static final double // shank data

   lLegX = 0.04, lLegY = 0.06, lLegZ = 0.15, mLeg = 3.6, IxxLeg = 0.2, // 0.049,
         IyyLeg = 0.049, IzzLeg = 0.001;

   public static final double // foot data TO VERIFY

   lFootX = 0.08, lFootY = 0.06, lFootZ = 0.1, // 0.4,
         mFoot = 1.0, IxxFoot = 0.077, IyyFoot = 0.077, IzzFoot = 0.003;

   public static final double // gearmotor data

   hMotor = 0.12, rMotor = 0.034;

   public static final double // robot's data

   lRobot = 0.6, wRobot = 0.36,

         // joint's height

         hThigh = lThighZ + lLegZ + lFootZ, // hip
         hKnee = lLegZ + lFootZ, // knee
         hFoot = lFootZ; // ankle

   Joint rootJoint;

   public GuaraRobot()
   {
      super("Guara");
      rootJoint = new FloatingJoint("rootJoint", new Vector3D(0.0, 0.0, 0.0), this);
      Link bodyLink = Body();
      rootJoint.setLink(bodyLink);
      this.addRootJoint(rootJoint);
      bodyLink.addCoordinateSystemToCOM(0.25);

      // hip abduction/Adduction

      //leg 0 hip

      UniversalJoint abdHip0 = new UniversalJoint("abdHip0X", "abdHip0Y", new Vector3D(lRobot / 2, wRobot / 2, 0.0), this, Axis.X, Axis.Y);

      rootJoint.addJoint(abdHip0);

      //leg 0 thigh

      Link tigh0 = Thigh(0);
      abdHip0.setLink(tigh0);
      tigh0.addCoordinateSystemToCOM(0.25);

      //leg 0 knee

      PinJoint flexKnee0 = new PinJoint("flexKnee0", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);

      abdHip0.addJoint(flexKnee0);

      //leg 0 shank

      Link leg0 = Leg();

      flexKnee0.setLink(leg0);

      //leg 0 ankle

      PinJoint ankleFlex0 = new PinJoint("flexAnkle0", new Vector3D(0.0, 0.0, -lLegZ), this, Axis.Y);

      flexKnee0.addJoint(ankleFlex0);

      Link foot0 = Foot();
      ankleFlex0.setLink(foot0);

      //leg 1 hip

      UniversalJoint abdHip1 = new UniversalJoint("abdHip1X", "abdHip1Y", new Vector3D(-lRobot / 2, wRobot / 2, 0.0), this, Axis.X, Axis.Y);

      rootJoint.addJoint(abdHip1);

      //leg 1 thigh

      Link tigh1 = Thigh(1);
      abdHip1.setLink(tigh1);

      //leg 1 knee

      PinJoint flexKnee1 = new PinJoint("flexKnee1", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);

      abdHip1.addJoint(flexKnee1);

      //leg 1 shank

      Link leg1 = Leg();

      flexKnee1.setLink(leg1);

      //leg 1 ankle

      PinJoint ankleFlex1 = new PinJoint("ankleFlex1", new Vector3D(0.0, 0.0, -lLegZ), this, Axis.Y);

      flexKnee1.addJoint(ankleFlex1);

      Link foot1 = Foot();
      ankleFlex1.setLink(foot1);

      //leg 2 hip

      UniversalJoint abdHip2 = new UniversalJoint("abdHip2X", "abdHip2Y", new Vector3D(-lRobot / 2, -wRobot / 2, 0.0), this, Axis.X, Axis.Y);

      rootJoint.addJoint(abdHip2);

      //leg 2 thigh

      Link tigh2 = Thigh(2);
      abdHip2.setLink(tigh2);

      //leg 2 knee

      PinJoint flexKnee2 = new PinJoint("flexKnee2", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);

      abdHip2.addJoint(flexKnee2);

      //leg 2 shank

      Link leg2 = Leg();

      flexKnee2.setLink(leg2);

      //leg 2 ankle

      PinJoint ankleFlex2 = new PinJoint("ankleFlex2", new Vector3D(0.0, 0.0, -lLegZ), this, Axis.Y);

      flexKnee2.addJoint(ankleFlex2);

      Link foot2 = Foot();
      ankleFlex2.setLink(foot2);

      //leg 3 hip

      UniversalJoint abdHip3 = new UniversalJoint("abdHip3X", "abdHip3Y", new Vector3D(lRobot / 2, -wRobot / 2, 0.0), this, Axis.X, Axis.Y);

      rootJoint.addJoint(abdHip3);

      //leg3 thigh

      Link tigh3 = Thigh(3);
      abdHip3.setLink(tigh3);

      //leg 3 knee

      PinJoint flexKnee3 = new PinJoint("flexKnee3", new Vector3D(0.0, 0.0, -lThighZ), this, Axis.Y);

      abdHip3.addJoint(flexKnee3);

      //leg 3 shank

      Link leg3 = Leg();

      flexKnee3.setLink(leg3);

      //leg 3 ankle

      PinJoint ankleFlex3 = new PinJoint("ankleFlex3", new Vector3D(0.0, 0.0, -lLegZ), this, Axis.Y);

      flexKnee3.addJoint(ankleFlex3);

      Link foot3 = Foot();
      ankleFlex3.setLink(foot3);

      //add ground contact points to the robot feet

      GroundContactPoint gc0 = new GroundContactPoint("gc0", new Vector3D(0.0, 0.0, 0.0), this);
      ankleFlex0.addGroundContactPoint(gc0);
      groundContactPoints.add(gc0);

      GroundContactPoint gc1 = new GroundContactPoint("gc1", new Vector3D(0.0, 0.0, 0.0), this);
      groundContactPoints.add(gc1);
      ankleFlex1.addGroundContactPoint(gc1);

      GroundContactPoint gc2 = new GroundContactPoint("gc2", new Vector3D(0.0, 0.0, 0.0), this);
      ankleFlex2.addGroundContactPoint(gc2);
      groundContactPoints.add(gc2);

      GroundContactPoint gc3 = new GroundContactPoint("gc3", new Vector3D(0.0, 0.0, 0.0), this);
      ankleFlex3.addGroundContactPoint(gc3);
      groundContactPoints.add(gc3);
      //
      //instantiate ground contact model
      //
      GroundContactModel groundModel = new LinearGroundContactModel(this, 1422, 150.6, 50.0, 1000.0, this.getRobotsYoVariableRegistry());
      this.setGroundContactModel(groundModel);

   }

   /*
    * The x axis is red, the y axis is white, and the z axis is blue.
    */
   private Link Body()
   {
      Link ret = new Link("Body");
      ret.setMass(mBody);
      ret.setComOffset(0, 0, 0);
      ret.setMomentOfInertia(IxxBody, IyyBody, IzzBody);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      // linkGraphics.translate(0, 0, lThighZ + lLegZ + lFootZ);
      linkGraphics.addCube(4 * lBodyX / 5, 4 * lBodyY / 5, lBodyZ, YoAppearance.DarkRed());
      // linkGraphics.addCoordinateSystem(1);
      ret.setLinkGraphics(linkGraphics);

      // pernas 0 e 3 o motor de rolagem é para trás

      // perna 0
      linkGraphics.translate(lBodyX / 2 - hMotor, lBodyY / 2 - rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());

      // perna 3

      linkGraphics.identity();
      linkGraphics.translate(lBodyX / 2 - hMotor, -lBodyY / 2 + rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());

      // pernas 1 e 2 o motor de rolagem é para trás

      // perna 1

      linkGraphics.identity();
      linkGraphics.translate(-lBodyX / 2, lBodyY / 2 - rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());

      // perna 2

      linkGraphics.identity();
      linkGraphics.translate(-lBodyX / 2, -lBodyY / 2 + rMotor / 2, 0.0);
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());

      return ret;
   }

   private Link Thigh(int iPerna)
   {
      Link ret = new Link("Thigh");
      ret.setMass(mThigh);
      ret.setComOffset(0, 0, 0);// lThighZ / 2);
      ret.setMomentOfInertia(IxxThigh, IyyThigh, IzzThigh);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.translate(0, 0, -lThighZ); // coxa
      linkGraphics.addCube(lThighX, lThighY, lThighZ, YoAppearance.DarkSlateGrey());//
      linkGraphics.translate(0, 0, lThighZ); // juntas do quadril
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());
      linkGraphics.rotate(Math.PI / 2, Axis.Y);
      // linkGraphics.addCoordinateSystem(0.1);

      ret.setLinkGraphics(linkGraphics);

      return ret;
   }

   private Link Leg()
   {
      Link ret = new Link("Leg");
      ret.setMass(mLeg);
      ret.setComOffset(0.0, 0.0, lLegZ / 2);
      ret.setMomentOfInertia(IxxLeg, IyyLeg, IzzLeg);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      // linkGraphics.identity();
      // linkGraphics.addCoordinateSystem(0.5);
      linkGraphics.translate(0, 0, -lLegZ); // canela
      linkGraphics.addCube(lLegX, lLegY, lLegZ, YoAppearance.DarkSlateGrey());
      linkGraphics.translate(0, 0, +lLegZ); // canela
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());
      ret.setLinkGraphics(linkGraphics);

      return ret;
   }

   private Link Foot()
   {
      Link ret = new Link("Foot");
      ret.setMass(mFoot);
      ret.setComOffset(0.0, 0.0, lFootZ / 2);
      ret.setMomentOfInertia(0.0, IyyFoot, 0.0);
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.identity();
      linkGraphics.translate(0, 0, -lFootZ); // pé
      linkGraphics.addCube(lFootX, lFootY, lFootZ, YoAppearance.BlackMetalMaterial());
      linkGraphics.rotate(Math.PI / 2, Axis.X);
      linkGraphics.translate(0.0, lFootZ, 0.0);
      linkGraphics.translate(0.0, 0.0, -hMotor / 2);
      linkGraphics.addCylinder(hMotor, rMotor, YoAppearance.BlackMetalMaterial());
      ret.setLinkGraphics(linkGraphics);

      return ret;
   }

   double a2()
   {
      return lThighZ;
   }

   double a3()
   {
      return lLegZ;
   }

   double a4()
   {
      return lFootZ;
   }

}