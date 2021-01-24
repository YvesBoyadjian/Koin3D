package org.ode4j.demo;

import org.ode4j.drawstuff.DrawStuff;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;

import static org.ode4j.drawstuff.DrawStuff.*;


import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;
import org.ode4j.ode.DGeom.DNearCallback;

import javax.swing.*;

import static org.ode4j.drawstuff.DrawStuff.*;

public class DemoBuggy2 extends DrawStuff.dsFunctions {


        // some constants
        //#define SIDE (0.5f)	// side length of a box
        //#define MASS (1.0)	// mass of a box
        private static final float SIDE = 0.5f;
        private static final float MASS = 82.0f;


        // dynamics and collision objects
        private static DWorld world;
        private static DBody[] body = new DBody[2];
        private static DGeom[] geom = new DGeom[2];
//        private static DDoubleBallJoint ball;
//        private static DLMotorJoint[] lmotor = new DLMotorJoint[2];
//        private static DAMotorJoint[] amotor = new DAMotorJoint[2];
        private static DHingeJoint/*DHinge2Joint*/ joint;

        private static DSpace space;
        private static DJointGroup contactgroup;
        private static DPlane plane;
    private static double angle;

        // start simulation - set viewpoint

        private static float[] xyz = {1.0382f,-1.0811f,1.4700f};
        private static float[] hpr = {135.0000f,-19.5000f,0.0000f};
        @Override
        public void start()
        {
            dsSetViewpoint (xyz,hpr);
            System.out.println ("Press 'q,a,z' to control one axis of lmotor connectiong two bodies. (q is +,a is 0, z is -)");
            System.out.println ("Press 'w,e,r' to control one axis of lmotor connectiong first body with world. (w is +,e is 0, r is -)");
        }


        // called when a key pressed
        @Override
        public void command (char cmd)
        {
//		if (cmd == 'q' || cmd == 'Q') {
//			lmotor[0].setParamVel(0);
//			lmotor[0].setParamVel2(0);
//			lmotor[0].setParamVel3(10.1);
//		} else if (cmd == 'a' || cmd == 'A') {
//			lmotor[0].setParamVel(0);
//			lmotor[0].setParamVel2(0);
//			lmotor[0].setParamVel3(0);
//		} else if (cmd == 'z' || cmd == 'Z') {
//			lmotor[0].setParamVel(0);
//			lmotor[0].setParamVel2(0);
//			lmotor[0].setParamVel3(-10.1);
//		} else if (cmd == 'w' || cmd == 'W') {
//			lmotor[1].setParamVel(10.1);
//			lmotor[1].setParamVel2(0);
//			lmotor[1].setParamVel3(0);
//		} else if (cmd == 'e' || cmd == 'E') {
//			lmotor[1].setParamVel(0);
//			lmotor[1].setParamVel2(0);
//			lmotor[1].setParamVel3(0);
//		} else if (cmd == 'r' || cmd == 'R') {
//			lmotor[1].setParamVel(-10.1);
//			lmotor[1].setParamVel2(0);
//			lmotor[1].setParamVel3(0);
//		}
//            if (cmd == 'q' || cmd == 'Q') {
//                amotor[0].setParamVel(0);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(10.1);
//            } else if (cmd == 'a' || cmd == 'A') {
//                amotor[0].setParamVel(0);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(0);
//            } else if (cmd == 'z' || cmd == 'Z') {
//                amotor[0].setParamVel(0);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(-10.1);
//            } else if (cmd == 'w' || cmd == 'W') {
//                amotor[0].setParamVel(10.1);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(0);
//            } else if (cmd == 'e' || cmd == 'E') {
//                amotor[0].setParamVel(0);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(0);
//            } else if (cmd == 'r' || cmd == 'R') {
//                amotor[0].setParamVel(-10.1);
//                amotor[0].setParamVel2(0);
//                amotor[0].setParamVel3(0);
//            }
        }



        private static void nearCallback (Object data, DGeom o1, DGeom o2)
        {
            // exit without doing anything if the two bodies are connected by a joint
            DBody b1 = o1.getBody();
            DBody b2 = o2.getBody();

            if((o1 == plane && o2 == geom[1]) || (o2 == plane && o1 == geom[1])) {
                int ii=0;
            }

            DContactBuffer cb = new DContactBuffer(10);
            int numc =OdeHelper.collide (o1,o2,10,cb.getGeomBuffer()/* sizeof(dContactGeom) */ );
            for(int i=0;i<numc;i++){
                DContact contact = cb.get(i);
                contact.surface.mode = OdeConstants.dContactSoftERP | OdeConstants.dContactSoftCFM | OdeConstants.dContactApprox1 |
                        OdeConstants.dContactSlip1 | OdeConstants.dContactSlip2;
                contact.surface.mu = OdeConstants.dInfinity;
                contact.surface.slip1 = 0;// 0.1;
                contact.surface.slip2 = 0;//0.1;
                contact.surface.soft_erp = 0.96;
                contact.surface.soft_cfm = 1e-5;
                contact.surface.rho = 0;
                contact.surface.rho2 = 0;
                DJoint c = OdeHelper.createContactJoint (world,contactgroup,contact);
                c.attach (b1,b2);
            }
        }

        // simulation loop
        private static DGeom.DNearCallback nearCallback = new DGeom.DNearCallback() {

            @Override
            public void call(Object data, DGeom o1, DGeom o2) {
                nearCallback(data, o1, o2);

            }

        };
        private static void simLoop (boolean pause)
        {
            if (!pause) {
                OdeHelper.spaceCollide(space,null,nearCallback);
                world./*quick*/step (0.0005);
                contactgroup.empty ();
            }

            DVector3C sides2 = ((DBox)geom[1]).getLengths();
            DVector3C sides1 = sides2;//geom[0].getLengths();
            dsSetTexture (DrawStuff.DS_TEXTURE_NUMBER.DS_WOOD);
            dsSetColor (1,1,0);
            dsDrawBox (body[0].getPosition(), body[0].getRotation(), sides1);
            dsSetColor (0,1,1);
            dsDrawBox (body[1].getPosition(), body[1].getRotation(), sides2);

            angle += 0.0003;
            joint.setAxis (Math.sin(angle),Math.cos(angle),0);
            joint.setParamVel (10);
            //System.out.println(body[0].getPosition().get2());
        }


        /**
         * @param args args
         */
        public static void main (String[] args)
        {
            //SwingUtilities.invokeLater();
            new org.ode4j.demo.DemoBuggy2().demo(args);
        }


        private void demo(String[] args) {

            // create world
            OdeHelper.initODE2(0);
            contactgroup = OdeHelper.createJointGroup();
            world = OdeHelper.createWorld();
            world.setAutoDisableFlag(false);
            world.setGravity(0,0,-9.81);
            world.setERP(0.2);
            world.setCFM(1e-5);
            world.setContactMaxCorrectingVel(0.5);
            world.setContactSurfaceLayer(0.01);
            space = OdeHelper.createHashSpace ();
            DMass m = OdeHelper.createMass();
            m.setBox (1,SIDE,SIDE,SIDE);
            m.adjust (MASS);

            plane = OdeHelper.createPlane(space,0.0,0.01,1,0);
            //DBody planeBody = OdeHelper.createBody (world);

            //plane.setBody(planeBody);

            body[0] = OdeHelper.createBody (world);
            body[0].setMass (m);
            body[0].setPosition (0,0,1.0);
            geom[0] = OdeHelper.createSphere(space,SIDE/2.);//OdeHelper.*/createBox(space,SIDE,SIDE,SIDE);
            body[1] = OdeHelper.createBody (world);
            body[1].setMass (m);
            body[1].setPosition (0,0,2.0);
            body[1].setMaxAngularSpeed(0);
            geom[1] = OdeHelper.createBox(space,SIDE,SIDE,SIDE);

            geom[0].setBody (body[0]);
            geom[1].setBody (body[1]);

            joint = OdeHelper.createHingeJoint(world,null);
            joint.attach(body[1],body[0]);
            joint.setAnchor(body[0].getPosition());
            //joint.setAxis (1,0,1);
            joint.setAxis (0,1,0);
            joint.setParamVel (10);
            joint.setParamFMax (10);

//		ball = OdeHelper.createDBallJoint (world);
//		ball.attach (body[0],body[1]);
//		ball.setAnchor1 (0,0,0);
//		ball.setAnchor2 (0,0,-1);

//            lmotor[0] = OdeHelper.createLMotorJoint (world,null);
//            lmotor[0].attach (body[1],body[0]);
//		lmotor[1] = OdeHelper.createLMotorJoint (world,null);
//		lmotor[1].attach (body[0],null);
//            amotor[0] = OdeHelper.createAMotorJoint (world,null);
//            amotor[0].attach (body[1],body[0]);
            //amotor[1] = OdeHelper.createAMotorJoint (world,null);
            //amotor[1].attach (body[0], null);

//            for (int i=0; i<1; i++) {
//                amotor[i].setNumAxes( 3);
//                amotor[i].setAxis(0,1,1,0,0);
//                amotor[i].setAxis(1,1,0,1,0);
//                amotor[i].setAxis(2,1,0,0,1);
//                amotor[i].setParamFMax(0.00001*9999);
//                amotor[i].setParamFMax2(0.00001*9999);
//                amotor[i].setParamFMax3(0.00001*99999999);
//
//                amotor[i].setParamVel(0);
//                amotor[i].setParamVel2(0);
//                amotor[i].setParamVel3(0);
//
//                if(i==0) {
//                    lmotor[i].setNumAxes(3);
//                    lmotor[i].setAxis(0, 1, 1, 0, 0);
//                    lmotor[i].setAxis(1, 1, 0, 1, 0);
//                    lmotor[i].setAxis(2, 1, 0, 0, 1);
//
//                    lmotor[i].setParamFMax(0.0001);
//                    lmotor[i].setParamFMax2(0.0001);
//                    lmotor[i].setParamFMax3(0.0001*999999);
//                }
//            }

            // run simulation
            dsSimulationLoop (args,352,288,this);

//		contactgroup.destroy();
//		space.destroy ();
//		world.destroy ();
//		OdeHelper.closeODE();
        }


        @Override
        public void step(boolean pause) {
            simLoop(pause);
        }


        @Override
        public void stop() {
            contactgroup.destroy();
            space.destroy ();
            world.destroy ();
            OdeHelper.closeODE();
            System.exit(0);
        }
    }
