/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoQuadMesh;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class Arche {

//Création d'une arche au moyen d'un maillage quadrangulaire

static float vertexPositions[][] =
{ {-13.0f,  0.0f, 1.5f},{-10.3f, 13.7f, 1.2f},
  { -7.6f, 21.7f, 1.0f},{ -5.0f, 26.1f, 0.8f},
  { -2.3f, 28.2f, 0.6f},{ -0.3f, 28.8f, 0.5f},
  {  0.3f, 28.8f, 0.5f},{  2.3f, 28.2f, 0.6f},
  {  5.0f, 26.1f, 0.8f},{  7.6f, 21.7f, 1.0f},
  { 10.3f, 13.7f, 1.2f},{ 13.0f,  0.0f, 1.5f},
  {-10.0f,  0.0f, 1.5f},{ -7.9f, 13.2f, 1.2f},
  { -5.8f, 20.8f, 1.0f},{ -3.8f, 25.0f, 0.8f},
  { -1.7f, 27.1f, 0.6f},{ -0.2f, 27.6f, 0.5f},
  {  0.2f, 27.6f, 0.5f},{  1.7f, 27.1f, 0.6f},
  {  3.8f, 25.0f, 0.8f},{  5.8f, 20.8f, 1.0f},
  {  7.9f, 13.2f, 1.2f},{ 10.0f,  0.0f, 1.5f},
  {-10.0f,  0.0f,-1.5f},{ -7.9f, 13.2f,-1.2f},
  { -5.8f, 20.8f,-1.0f},{ -3.8f, 25.0f,-0.8f},
  { -1.7f, 27.1f,-0.6f},{ -0.2f, 27.6f,-0.5f},
  {  0.2f, 27.6f,-0.5f},{  1.7f, 27.1f,-0.6f},
  {  3.8f, 25.0f,-0.8f},{  5.8f, 20.8f,-1.0f},
  {  7.9f, 13.2f,-1.2f},{ 10.0f,  0.0f,-1.5f},
  {-13.0f,  0.0f,-1.5f},{-10.3f, 13.7f,-1.2f},
  { -7.6f, 21.7f,-1.0f},{ -5.0f, 26.1f,-0.8f},
  { -2.3f, 28.2f,-0.6f},{ -0.3f, 28.8f,-0.5f},
  {  0.3f, 28.8f,-0.5f},{  2.3f, 28.2f,-0.6f},
  {  5.0f, 26.1f,-0.8f},{  7.6f, 21.7f,-1.0f},
  { 10.3f, 13.7f,-1.2f},{ 13.0f,  0.0f,-1.5f},
  {-13.0f,  0.0f, 1.5f},{-10.3f, 13.7f, 1.2f},
  { -7.6f, 21.7f, 1.0f},{ -5.0f, 26.1f, 0.8f},
  { -2.3f, 28.2f, 0.6f},{ -0.3f, 28.8f, 0.5f},
  {  0.3f, 28.8f, 0.5f},{  2.3f, 28.2f, 0.6f},
  {  5.0f, 26.1f, 0.8f},{  7.6f, 21.7f, 1.0f},
  { 10.3f, 13.7f, 1.2f},{ 13.0f,  0.0f, 1.5f} };

public static SoSeparator makeArch()
{ SoSeparator result = new SoSeparator();
  result.ref();
  SoMaterial myMaterial = new SoMaterial();
  myMaterial.diffuseColor.setValue(.78f,.57f,.11f);
  result.addChild(myMaterial);
  SoCoordinate3 myCoords = new SoCoordinate3();
  myCoords.point.setValues(0,/*60,*/vertexPositions);
  result.addChild(myCoords);
  SoQuadMesh myQuadMesh = new SoQuadMesh();
  myQuadMesh.verticesPerRow.setValue(12);
  myQuadMesh.verticesPerColumn.operator_assign(5);
  result.addChild(myQuadMesh);
  result.unrefNoDelete();
  return result;
}

}
