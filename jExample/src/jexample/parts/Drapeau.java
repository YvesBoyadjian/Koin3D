/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTriangleStripSet;

/**
 * @author Yves Boyadjian
 *
 */
public class Drapeau {

//Création d'un drapeau au moyen d'une bande de triangles

static float vertexPositions[][] =
{ {  0, 12  ,    0},{  0,   15,    0},
  {2.1f, 12.1f,  -.2f},{2.1f, 14.6f,  -.2f},
  { 4,  12.5f,  -.7f},{  4, 14.5f,  -.7f},
  {4.5f, 12.6f,  -.8f},{4.5f, 14.4f,  -.8f},
  {  5, 12.7f,   -1},{  5, 14.4f,   -1},
  {4.5f, 12.8f, -1.4f},{4.5f, 14.6f, -1.4f},
  {  4, 12.9f, -1.6f},{  4, 14.8f, -1.6f},
  {3.3f, 12.9f, -1.8f},{3.3f, 14.9f, -1.8f},
  {  3, 13  , -2.0f},{  3, 14.9f, -2.0f},
  {3.3f, 13.1f, -2.2f},{3.3f, 15.0f, -2.2f},
  {  4, 13.2f, -2.5f},{  4, 15.0f, -2.5f},
  {  6, 13.5f, -2.2f},{  6, 14.8f, -2.2f},
  {  8, 13.4f,   -2},{  8, 14.6f,   -2},
  { 10, 13.7f, -1.8f},{ 10, 14.4f, -1.8f},
  { 12, 14  , -1.3f},{ 12, 14.5f, -1.3f},
  { 15, 14.9f, -1.2f},{ 15, 15  , -1.2f},
  {-.5f, 15,  0},{-.5f,0,  0},
  {  0,15,.5f},{  0,0,.5f},
  {  0, 15,-.5f},{  0,0,-.5f},
  {-.5f,15, 0},{-.5f,0, 0} };
static int numVertices[] = { 32,8 };

public static SoSeparator makePennant()
{ SoSeparator result = new SoSeparator();
  result.ref();
  SoCoordinate3 myCoords = new SoCoordinate3();
  myCoords.point.setValues(0, /*40,*/ vertexPositions);
  result.addChild(myCoords);
  SoTriangleStripSet s = new SoTriangleStripSet();
  s.numVertices.setValues(0, /*2,*/ numVertices);
  result.addChild(s);
  result.unrefNoDelete();
  return result;
}

}
