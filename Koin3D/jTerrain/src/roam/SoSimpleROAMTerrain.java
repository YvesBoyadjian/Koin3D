
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Ter� vykreslovan algoritmem ROAM.
/// \file SoSimpleROAMTerrain.h
/// \author Radek Barto�- xbarto33
/// \date 25.08.2005
///
/// Uzel grafu sc�y reprezentuj��ter� vykreslovan algoritmem ROAM. Pro
/// pouit�je teba uzlu pedadit uzel s koordin�y typu \p SoCordinate3
/// obsahuj��vkovou mapu ter�u a nastavit jej�rozm�y. Velikost vkov�/// mapy mus�bt �vercov�o velikosti strany 2^n + 1, kde n je cel�kladn�/// �slo.
//////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2006 Radek Barton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
///////////////////////////////////////////////////////////////////////////////

package roam;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoTextureEnabledElement;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jterrain.profiler.PrProfiler;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSimpleROAMTerrain extends SoShape {

  //SO_NODE_HEADER(SoSimpleROAMTerrain);
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSimpleROAMTerrain.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSimpleROAMTerrain.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSimpleROAMTerrain.class); }    	  	
	// End SO_NODE_HEADER
  

	  /* Staticke konstanty. */
	    /// Vchoz�hodnota pro chybu triangulace v pixelech.
	  static final int DEFAULT_PIXEL_ERROR = 6;
	    /// Vchoz�hodnota pro maxim�n�po�t trojheln� v triangulaci.
	  static final int DEFAULT_TRIANGLE_COUNT = 5000;

	  
    /* Metody */
    /** Run-time inicializace t�y.
    Tuto metodu je teba zavolat ped vytvoen� jak�oliv instance t�y
    ::SoSimpleROAMTerrain. */
  public static void initClass() {
  /* Inicializace tridy. */
	  SoSubNode.SO_NODE_INIT_CLASS(SoSimpleROAMTerrain.class, SoShape.class, "Shape");
	  SO_ENABLE(SoGLRenderAction.class, SoMaterialBindingElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoCoordinateElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoTextureCoordinateElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoTextureEnabledElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoLightModelElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewVolumeElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewportRegionElement.class);
  SO_ENABLE(SoGetBoundingBoxAction.class, SoCoordinateElement.class);
}

  
  
    /** Konstruktor.
    Vytvo�instanci t�y ::SoSimpleROAMTerrain, inicializuje ob�prioritn�    fronty. */
  public SoSimpleROAMTerrain() {
	  coords = null;
	  texture_coords = null;
	  normals = null;
	  view_volume = null;	  
	  viewport_region = null;
	  triangle_tree = null;
	  tree_size = 0;
	  level = 0;
	  lambda = 0.0f;
	  split_queue = null;
	  merge_queue = null;
	  is_texture = false;
	  is_normals = false;
	  map_size = 2;
	  pixel_error = DEFAULT_PIXEL_ERROR;
	  triangle_count = DEFAULT_TRIANGLE_COUNT;
	  is_frustrum_culling = true;
	  is_freeze = false;
	  map_size_sensor = null;
	  pixel_error_sensor = null;
	  triangle_count_sensor = null;
	  frustrum_culling_sensor = null;
	  freeze_sensor = null;
	
	  /* Inicializace tridy. */
	  nodeHeader.SO_NODE_CONSTRUCTOR(SoSimpleROAMTerrain.class);

	  /* Inicializace poli */
	  nodeHeader.SO_NODE_ADD_FIELD(mapSize,"mapSize", (2));
	  nodeHeader.SO_NODE_ADD_FIELD(pixelError,"pixelError", (DEFAULT_PIXEL_ERROR));
	  nodeHeader.SO_NODE_ADD_FIELD(triangleCount,"triangleCount", (DEFAULT_TRIANGLE_COUNT));
	  nodeHeader.SO_NODE_ADD_FIELD(frustrumCulling,"frustrumCulling", (true));
	  nodeHeader.SO_NODE_ADD_FIELD(freeze,"freeze", (false));

	  /* Vytvoreni senzoru. */
	  map_size_sensor = new SoFieldSensor(SoSimpleROAMTerrain::mapSizeChangedCB, this);
	  pixel_error_sensor = new SoFieldSensor(SoSimpleROAMTerrain::pixelErrorChangedCB, this);
	  triangle_count_sensor = new SoFieldSensor(SoSimpleROAMTerrain::triangleCountChangedCB, this);
	  frustrum_culling_sensor = new SoFieldSensor(SoSimpleROAMTerrain::frustrumCullingChangedCB, this);
	  freeze_sensor = new SoFieldSensor(SoSimpleROAMTerrain::freezeChangedCB, this);

	  /* Napojeni senzoru na pole */
	  map_size_sensor.attach(mapSize);
	  pixel_error_sensor.attach(pixelError);
	  triangle_count_sensor.attach(triangleCount);
	  frustrum_culling_sensor.attach(frustrumCulling);
	  freeze_sensor.attach(freeze);

	  /* Inicializace internich struktur. */
	  split_queue = new SbROAMSplitQueue();
	  merge_queue = new SbROAMMergeQueue();
	}
  
  
    /* Pole. */
    /// Velikost (vka i �ka) vkov�mapy ter�u.
  public final SoSFInt32 mapSize = new SoSFInt32();
    /// Chyba zobrazen�v pixelech.
  public final SoSFInt32 pixelError = new SoSFInt32();
    /// Maxim�n�po�t trojheln� v triangulaci.
  public final SoSFInt32 triangleCount = new SoSFInt32();
    /// P�nak oez���ter�u mimo zorn�pole.
  public final SoSFBool frustrumCulling = new SoSFBool();
    /// P�nak "zmrazen� vykreslov��ter�u.
  public final SoSFBool freeze = new SoSFBool();
  
    /* Metody */
    /** Vykreslen�aktu�n�triangulace.
    Na z�lad�vstupn�h dat ve vkov�map� pozici a sm�u kamery a dal�h
    vlastnost�v prvn� sput��vybuduje bin�n�strom trojheln�, pot�-
    a tak�v dal�h sput��h - rozd�uje a spojuje trojheln�y a diamanty v
    prioritn�h front�h tak, aby se dos�lo minim�n�triangulace. N�ledn�    tuto triangulaci vykresl�
    \param action Objekt nesouc�informace o grafu sc�y. */


private void GL_SEND_VERTEX(GL2 gl2, int index) {
	int vertex_index = index; 

  if (is_texture) 
    gl2.glTexCoord2fv(texture_coords[vertex_index].getValueRead(),0); 
  if (is_normals) 
    gl2.glNormal3fv(normals[vertex_index].getValueRead(),0); 
  gl2.glVertex3fv(coords[vertex_index].getValueRead(),0);
}
static boolean first_run = true;

  public  void GLRender(SoGLRenderAction action) {
  if (!shouldGLRender(action))
  {
    return;
  }

  /* Ziskani informaci z grafu sceny. */
  SoState state = action.getState();
  view_volume = SoViewVolumeElement.get(state);
  view_volume.getViewVolumePlanes(planes);
  viewport_region = SoViewportRegionElement.get(state);

  /* Pri prvnim prubehu se vygeneruje strom trojuhelniku */
  if (first_run) 
  {
    PrProfiler.PR_START_PROFILE("preprocess");
  	first_run = false;

    /* Pracujeme pouze s tridimenzionalnimi geometrickymi souradnicemi
    a dvoudimenzionalnimi texturovymi souradnicemi. */
    assert(SoCoordinateElement.getInstance(state).is3D() &&
      (SoTextureCoordinateElement.getInstance(state).getDimension() == 2));

    /* Ziskani vrcholu a texturovych souradnic. */
    coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
    texture_coords = SoTextureCoordinateElement.getInstance(state).
      getArrayPtr2();
    normals = SoNormalElement.getInstance(state).getArrayPtr();

    /* Vypocet levelu jako 2 * log2(map_size - 1) */
    int tmp_size = map_size - 1;
    level = 0;
    while (tmp_size > 1)
    {
      level += 2;
      tmp_size >>= 1;
    }

    /* Zjisteni poctu trojuhelniku ve stromu jako 2^(level + 1) - 1. */
    tree_size = (1 << (level + 1)) - 1;
    
    //YB
    if(tree_size < 3) tree_size = 3; //TODO

    /* Vypocet indexu rohovych vertexu mapy. */
    int top_left = 0;
    int top_right = map_size;
    int bottom_right = top_right * top_right - 1;
    int bottom_left = bottom_right - top_right-- + 1; // !! vedlejsi efekt

    /* Alokace trojuhelniku a vytvoreni stromu. */
    triangle_tree = new Array<SbROAMTriangle>(SbROAMTriangle.class,new SbROAMTriangle[tree_size]);
    triangle_tree.set(1, new SbROAMTriangle(top_left, bottom_right, top_right, 1));
    triangle_tree.set(2, new SbROAMTriangle(bottom_right, top_left, bottom_left, 1));
    initTriangle(triangle_tree, 1);
    initTriangle(triangle_tree, 2);

    /* Dva koreny binarniho stromu trojuhelniku. */
    SbROAMTriangle triangle_1 = triangle_tree.get(1);
    SbROAMTriangle triangle_2 = triangle_tree.get(2);

    SbROAMSplitQueueTriangle root_1 =
      new SbROAMSplitQueueTriangle(triangle_1, computePriority(triangle_1));
    SbROAMSplitQueueTriangle root_2 =
      new SbROAMSplitQueueTriangle(triangle_2, computePriority(triangle_2));

    /* Jejich vzajemne propojeni. */
    root_1.base = root_2;
    root_2.base = root_1;

    /* Vlozeni korenu do fronty trojuhelniku. */
    split_queue.add(root_1);
    split_queue.add(root_2);

    PrProfiler.PR_STOP_PROFILE("preprocess");
  }

  if (!is_freeze)
  {
    /* Aktualizace lambdy pro aktualni okno. */
    lambda = (view_volume.getNearDist() *
      viewport_region.getViewportSizePixels().getValue()[1]) /
      (view_volume.getHeight());

    /* Prepocitani priority v prioritni fronte na rozdeleni. */
    int size = split_queue.size();
    for (int I = 1; I <= size; ++I)
    {
      SbROAMSplitQueueTriangle triangle = (split_queue).operator_square_bracket(I);
      triangle.setPriority(this.computePriority(triangle.triangle));
    }
    split_queue.buildHeap(null, null);

    /* Prepocitani priority v prioritni fronte na spojeni. */
    size = merge_queue.size();
    for (int J = 1; J <= size; ++J)
    {
      SbROAMMergeQueueDiamond diamond = (merge_queue).operator_square_bracket(J);

      float priority = SbROAMSplitQueueTriangle.PRIORITY_MIN;
      float first_priority = diamond.first.getPriority();
      float second_priority = diamond.second.getPriority();
      priority = Math.max(priority, first_priority);
      priority = Math.max(priority, second_priority);

      if (diamond.third != null)
      {
        float third_priority = diamond.third.getPriority();
        float fourth_priority = diamond.fourth.getPriority();
        priority = Math.max(priority, third_priority);
        priority = Math.max(priority, fourth_priority);
      }
      diamond.setPriority(priority);
    }
    merge_queue.buildHeap(null, null);

    /* Smycka generovani triangulace pro konstantni pocet trojuhelniku. */
    while (true)
    {
      if ((split_queue.size() > triangle_count) ||
        (split_queue.getMax().getPriority() < pixel_error))
      {
        if (merge_queue.size() > 0)
        {
          if (split_queue.getMax().getPriority() <=
            merge_queue.getMin().getPriority())
          {
            break;
          }
          else
          {
            merge(merge_queue.getMin());
          }
        }
        else
        {
          break;
        }
      }
      else
      {
        /* Ziskani a rozdeleni trojuhelniku. */
        SbROAMSplitQueueTriangle parent = split_queue.getMax();
        if (parent.triangle.level == level)
        {
          parent.setPriority(split_queue, SbROAMSplitQueueTriangle.PRIORITY_MIN);
        }
        else
        {
          final SbROAMSplitQueueTriangle[] left_child = new SbROAMSplitQueueTriangle[1];
          final SbROAMSplitQueueTriangle[] right_child = new SbROAMSplitQueueTriangle[1];
          forceSplit(parent, left_child, right_child);
        }
      }
    }
  }

  /* Inicializace vykreslovani. */
  beginSolidShape(action);
  SoNormalBindingElement.Binding norm_bind =
    SoNormalBindingElement.get(state);
  final SoMaterialBundle mat_bundle = new SoMaterialBundle(action);

  this.is_texture = (SoTextureEnabledElement.get(state) &&
   SoTextureCoordinateElement.getType(state) !=
   SoTextureCoordinateElement.CoordType.NONE);
  this.is_normals = (this.normals != null && SoLightModelElement.get(state) !=
    SoLightModelElement.Model.BASE_COLOR);

  mat_bundle.sendFirst();

  if (!is_normals)
  {
    norm_bind = SoNormalBindingElement.Binding.OVERALL;
  }
  
  GL2 gl2 = action.getCacheContext();

  if (norm_bind == SoNormalBindingElement.Binding.OVERALL)
  {
	  gl2.glNormal3f(0.0f, 0.0f, 1.0f);
  }

  /* Vykresleni trojuhelniku. */
  gl2.glBegin(GL2.GL_TRIANGLES);
  for (int I = 1; I <= split_queue.size(); ++I)
  {
    SbROAMTriangle triangle = (split_queue).operator_square_bracket(I).triangle;
    int vertex_index;

    /* Vykresleni trojuhelniku. */
    GL_SEND_VERTEX(gl2,triangle.first);
    GL_SEND_VERTEX(gl2,triangle.apex);
    GL_SEND_VERTEX(gl2,triangle.second);
  }
  gl2.glEnd();

  endSolidShape(action);
  mat_bundle.destructor();
}
  
  
    /** Vytvoen�vech trojheln� vkov�mapy.
    Ze vstupn�vkov�mapy vytvo�seznam trojheln� na nejvy�rovni
    detail pro �ly detekce koliz�a podobn�
    \param action Objekt nesouc�informace o grafu sc�y. */

private void SEND_VERTEX(int ind,final SoPrimitiveVertex vertex) { int index = (ind); 
   vertex.setPoint(coords[index]); 
   vertex.setTextureCoords(texture_coords[index]); 
   vertex.setNormal(normals[index]); 
   shapeVertex(vertex);
}
  
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.nodes.SoShape#generatePrimitives(jscenegraph.database.inventor.actions.SoAction)
	 */
	@Override
  protected void generatePrimitives(SoAction action) {
    final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
    int index;

    SoState state = action.getState();
    coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
    texture_coords = SoTextureCoordinateElement.getInstance(state).
      getArrayPtr2();
    normals = SoNormalElement.getInstance(state).getArrayPtr();

    /* Brutal-force vygenerovani triangle-stripu vyskove mapy. */
    for (int Y = 0; Y < (map_size - 1); ++Y)
    {
      beginShape(action, TriangleShape.QUAD_STRIP);

      /* Prvni dva vrcholy pasu. */
      SEND_VERTEX((Y + 1) * map_size,vertex);
      SEND_VERTEX(Y * map_size,vertex);

      for (int X = 1; X < map_size; ++X)
      {
        /* Dalsi vrcholy pasu. */
        SEND_VERTEX(((Y + 1)  * map_size) + X,vertex);
        SEND_VERTEX((Y * map_size) + X,vertex);
      }
      endShape();
    }
    vertex.destructor(); // java port
  }

  
  
  
    /** Vpo�t ohrani�n�ter�u.
    Vypo�e rozm�y a sted kv�ru, kter ohrani�je cel ter�.
    \param action Objekt nesouc�informace o grafu sc�y.
    \param box Vypo�en kv�r ohrani�j��ter�.
    \param center Vypo�en sted kv�ru ohrani�j��o ter�. */
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.nodes.SoShape#computeBBox(jscenegraph.database.inventor.actions.SoAction, jscenegraph.database.inventor.SbBox3f, jscenegraph.database.inventor.SbVec3f)
	 */
	@Override
  public void computeBBox(SoAction action, final SbBox3f box,
      final SbVec3f center) {
		  /* Vypocet ohranicujiciho kvadru a jeho stredu. */
		  SoState state = action.getState();
		  final SoCoordinateElement coords = SoCoordinateElement.getInstance(state);
		  int map_size = mapSize.getValue();

		  /* Vypocet ohraniceni podle dvou rohu vyskove mapy. */
		  SbVec3f min = coords.get3(0);
		  SbVec3f max = coords.get3(map_size * map_size - 1);
		  max.setValue(2, (max.getValueRead()[1] - min.getValueRead()[1]) * 0.5f);
		  min.setValue(2, -max.getValueRead()[2]);
		  box.setBounds(min, max);

		  center.copyFrom( box.getCenter());
		}

  
  
    /** Inicializace bin�n�o stromu trojheln�.
    Rekurzivn�inicializuje vechny trojheln�y bin�n�o stromu trojheln�,
    vypo�t�jejich norm�y (pro plon�nebo vpo�t plynul�o st�ov��.
    \param index Index trojheln�u, kter inicializovat.
    \param triangle_tree Ukazatel na koen bin�n�o stromu trojheln�. */
  protected void initTriangle(Array<SbROAMTriangle> triangle_tree, int index) {
  /******************************************************************************
  * SoSimpleROAMTerrain - protected
  ******************************************************************************/

    /* Rodicovsky trojuhelnik, ktery se inicializuje */
    final SbROAMTriangle parent = triangle_tree.get(index); //ref

    /* Vrcholy rodicovskeho trojuhelniku. */
    final SbVec3f  first = coords[parent.first]; //ref
    final SbVec3f  second = coords[parent.second]; //ref
    final SbVec3f  apex = coords[parent.apex]; //ref

    /* Dokud neni spodni patro stromu, inicializace potomku. */
    if (parent.level < level)
    {
      /* Naleznuti potomku. */
      int left_index = (index << 1) + 1;
      int right_index = left_index + 1;
      SbROAMTriangle left_child = triangle_tree.get(left_index); //ref
      SbROAMTriangle right_child = triangle_tree.get(right_index); //ref

      /* Nastaveni vrcholu leveho potomka */
      left_child.first = parent.apex;
      left_child.second = parent.first;
      left_child.apex = (parent.first + parent.second) >> 1;
      left_child.level = parent.level + 1;

      /* Nastaveni vrcholu praveho potomka */
      right_child.first = parent.second;
      right_child.second = parent.apex;
      right_child.apex = (parent.first + parent.second) >> 1;
      right_child.level = parent.level + 1;

      /* Inicializace potomku. */
      initTriangle(triangle_tree, left_index);
      initTriangle(triangle_tree, right_index);

      /* Vypocet "podpadku" jako maximum chyby obou potomku + rozdil vysky
      vrcholu s pravym uhlem a prumene vysky obou vrcholu prepony. */
      parent.error = Math.max(left_child.error, right_child.error) +
        Math.abs(apex.getValueRead()[2] - (first.getValueRead()[2] + second.getValueRead()[2]) * 0.5f);

      /* Vypocet polomeru kuloplochy ohranicujici trojuhelnik. */
      final SbVec3f left_apex = coords[left_child.apex]; //ref
      final SbVec3f right_apex = coords[right_child.apex]; //ref
      float left_radius = (apex.operator_minus(left_apex)).length() + left_child.radius;
      float right_radius = (apex.operator_minus(right_apex)).length() + right_child.radius;
      parent.radius = Math.max(left_radius, right_radius);
    }
    else
    {
      /* Trojuhelniky na dne stromu maji nulovou metriku. */
      parent.error = SbROAMSplitQueueTriangle.PRIORITY_MIN;
      parent.radius = 0.0f;
    }
  }

  
  
  
    /** Test na viditelnost trojheln�u.
    Otestuje trojheln� definovan body \p first, \p second a \p apex proti
    pohledov�u t�esu. Nenach�i-li se �n z bodu v pohledu kamery, vr��    \p false. Pou��zjednodueny, ale do tech rozm� roz�eny
    Cohen-Shuterland algoritmus, a proto me vr�it \p true i kdy je
    trojheln� mimo pohled kamery.
    \param first Prvn�vrchol testovan�o trojheln�u.
    \param second Druh vrchol testovan�o trojheln�u.
    \param apex Tet�vrchol testovan�o trojheln�u.
    \return Vr��\p false nen�li trojheln� ur�t�v pohledu kamery, je-li
    v pohledu nebo nejde-li rozhodnout vrat�\p true. */
  protected  boolean isInViewVolume( SbVec3f first,  SbVec3f
      second, SbVec3f apex) {
		  /* Priznaky polorovin, ve kterych se nachazeji jednotlive vrcholy
		  trojuhelniku. */
		  int first_flags = 0x00;
		  int second_flags = 0x00;
		  int apex_flags = 0x00;
		  int mask = 0x01;

		  /* Pro vsech sest orezavacich rovin otestujeme kazdy vrchol trojuhelniku
		  na pritomnost v poloprostoru. */
		  for (int I = 0; I < 6; ++I, mask <<= 1)
		  {
		    if (!planes.get(I).isInHalfSpace(first))
		    {
		      first_flags |= mask;
		    }
		    if (!planes.get(I).isInHalfSpace(second))
		    {
		      second_flags |= mask;
		    }
		    if (!planes.get(I).isInHalfSpace(apex))
		    {
		      apex_flags |= mask;
		    }
		  }

		  /* Je-li nektery z vrcholu primo v pohledu kamery vykreslime vzdy. */
		  if ((first_flags & second_flags & apex_flags) == 0x00)
		  {
		    return true;
		  }
		  else
		  {
		    /* Jsou-li vsechny vrcholy mimo, vratime vysledek potencialni viditelnosti
		    trojuhelniku. */
		    return !((first_flags & second_flags)!=0 && (second_flags & apex_flags)!=0
		      && (apex_flags & first_flags)!=0);
		  }
		}

  
  
    /** Vpo�t priority trojheln�u v triangulaci.
    Na z�lad�pozice a orientace kamery vypo�e prioritu trojheln�u
    \p triangle v triangulaci.
    \param triangle Trojheln�, jeho priorita se m�spo�tat.
    \return Vr��vypo�enou prioritu trojheln�u. */
  protected float computePriority( SbROAMTriangle triangle) {
		  /* Ziskani vrcholu trojuhelniku a pozice kamery. */
		  SbVec3f camera_position = view_volume.getProjectionPoint();
		  final SbVec3f first = coords[triangle.first]; //ref
		  final SbVec3f second = coords[triangle.second]; //ref
		  final SbVec3f apex = coords[triangle.apex]; //ref

		  /* Vzdalenost kamery od spolecneho vrcholu obou trojuhelniku. */
		  float distance = (camera_position.operator_minus(apex)).length();

		  /* Je-li kamera prilis blizko trojuhelniku, vyresleni vzdy. */
		  if (distance < triangle.radius)
		  {
		    return SbROAMSplitQueueTriangle.PRIORITY_MAX;
		  }
		  else
		  {
		    /* Neni-li tak blizko, zjisteni jestli je trojuhelnik videt. */
		    if (!this.is_frustrum_culling || this.isInViewVolume(first, second,
		      apex))
		    {
		      return lambda * triangle.error / (distance - triangle.radius);
		    }
		    else
		    {
		      return SbROAMSplitQueueTriangle.PRIORITY_MIN; // trojuhelnik neni videt
		    }
		  }
		}

  
  
    /** Vm�a sousedn�o trojheln�u.
    Zjist� kterm sousedem je trojheln� \p old_neighbour, a zam��ho za
    nov trojheln� \p new_neighbour.
    \param triangle Trojheln�, u kter�o vm�u prov�t.
    \param old_neighbour Pvodn�soused trojheln�u.
    \param new_neighbour Nov soused trojheln�u. */
  
  protected void reconnectNeighbour(SbROAMSplitQueueTriangle triangle,
      SbROAMSplitQueueTriangle old_neighbour,
      SbROAMSplitQueueTriangle new_neighbour) {
		  /* Vymena spravneho souseda trojuhelniku. */
		  if (triangle != null)
		  {
		    if (triangle.left == old_neighbour)
		    {
		      triangle.left = new_neighbour;
		    }
		    else if (triangle.right == old_neighbour)
		    {
		      triangle.right = new_neighbour;
		    }
		    else if (triangle.base == old_neighbour)
		    {
		      triangle.base = new_neighbour;
		    }
		  }
		}

  
  
  
    /** Z�ladn�rozd�en�trojheln�u.
    Vyjme trojheln� \p parent z prioritn�fronty trojheln�
    na rozd�en� Nalezne jeho potomky v bin�n� stromu trojheln�,
    naalokuje jejich reprezentace v prioritn�front�a vlo�je do n�
    Propoj�oba potomky mezi sebou a s okoln�i trojheln�y krom�b�ov�o
    souseda otce. Aby bylo toto mono prov�t zp�n� vr��oba potomky
    \p left_child a \p right_child. Reprezentaci otce neuvol�je,
    protoe je tak�teba.
    \param parent Otec, kter se m�rozd�it.
    \param left_child Lev potomek rozd�en�o otce.
    \param right_child Prav potomek rozd�en�o otce. */
  protected void simpleSplit(SbROAMSplitQueueTriangle parent,
      final SbROAMSplitQueueTriangle[] left_child,
      final SbROAMSplitQueueTriangle[] right_child) {
		  /* Odstraneni rodice z fronty trojuhelniku na rodeleni a diamondu
		  na spojeni. */
		  split_queue.remove(parent);
		  Destroyable.delete(merge_queue.remove(parent));

		  /* Ziskani potomku rozdelovaneho trojuhelniku. */
		  int child_index = ((Array.minus(parent.triangle, triangle_tree)) << 1) + 1;
		  SbROAMTriangle left_triangle = triangle_tree.get(child_index); // ref
		  SbROAMTriangle right_triangle = triangle_tree.get(child_index + 1); //ref

		  /* Vytvoreni a vraceni potomku.  */
		  left_child[0] = new SbROAMSplitQueueTriangle(left_triangle,
		    computePriority(left_triangle));
		  right_child[0] = new SbROAMSplitQueueTriangle(right_triangle,
		    computePriority(right_triangle));

		  /* Vlozeni potomku do fronty. */
		  split_queue.add(left_child[0]);
		  split_queue.add(right_child[0]);

		  /* Propojeni potomku. */
		  left_child[0].left = right_child[0];
		  right_child[0].right = left_child[0];
		  left_child[0].base = parent.left;
		  reconnectNeighbour(parent.left, parent, left_child[0]);
		  right_child[0].base = parent.right;
		  reconnectNeighbour(parent.right, parent, right_child[0]);
		}
  
  
  
    /** Obecn�rozd�en�trojheln�u.
    Kompletn�rozd��trojheln� \p parent z prioritn�fronty trojheln�
    na rozd�en� Pro vlastn�rozd�en�vol�::simpleSplit. Na rozd�en�    trojheln�y napoj�vledek rozd�en�b�ov�o souseda, kter me bt
    d�e d�en rekurzivn� Rozd�en�o otce dealokuje.
    \param parent Otec, kter se m�rozd�it.
    \param left_child Lev potomek rozd�en�o otce.
    \param right_child Prav potomek rozd�en�o otce. */
  protected void forceSplit(SbROAMSplitQueueTriangle parent,
      SbROAMSplitQueueTriangle[] left_child,
      SbROAMSplitQueueTriangle[] right_child) {

  /* Rozdeleni rodice. */
  simpleSplit(parent, left_child, right_child);

  /* Promenne pro vytvoreni diamondu. */
  float first_priority = parent.getPriority();
  float second_priority = SbROAMSplitQueueTriangle.PRIORITY_MIN;
  SbROAMSplitQueueTriangle first = left_child[0]; // ptr
  SbROAMSplitQueueTriangle second = right_child[0]; // ptr
  SbROAMSplitQueueTriangle third = null; // ptr
  SbROAMSplitQueueTriangle fourth = null; //ptr

  /* Ma-li rodic bazoveho souseda. */
  if (parent.base != null)
  {
    /* Je-li tento na stejne urovni ve stromu trojuhelniku. */
    if (parent.triangle.level == parent.base.triangle.level)
    {
      /* Rozdeli se take bazovy soused. */
      SbROAMSplitQueueTriangle base = parent.base; // ptr
      final SbROAMSplitQueueTriangle[] left_base = new SbROAMSplitQueueTriangle[1]; // ptr
      final SbROAMSplitQueueTriangle[] right_base = new SbROAMSplitQueueTriangle[1]; //ptr
      simpleSplit(base, left_base, right_base);

      /* Propojeni diamondu. */
      left_base[0].right = right_child[0];
      right_child[0].left = left_base[0];
      right_base[0].left = left_child[0];
      left_child[0].right = right_base[0];

      /* Nastaveni priorit a trojuhelniku diamondu. */
      second_priority = base.getPriority();
      third = left_base[0];
      fourth = right_base[0];

      base.destructor();
    }
    else
    {
      /* Rozdeli se take bazovy soused. */
      SbROAMSplitQueueTriangle base = parent.base; // ptr
      final SbROAMSplitQueueTriangle[] left_base = new SbROAMSplitQueueTriangle[1]; //ptr
      final SbROAMSplitQueueTriangle[] right_base = new SbROAMSplitQueueTriangle[1]; //ptr
      forceSplit(base, left_base, right_base);

      /* Nalezeni a rozdeleni propojky. */
      SbROAMSplitQueueTriangle link = left_base[0].base == parent ? left_base[0] :
        right_base[0]; // ptr
      final SbROAMSplitQueueTriangle[] left_link = new SbROAMSplitQueueTriangle[1]; // ptr
      final SbROAMSplitQueueTriangle[] right_link = new SbROAMSplitQueueTriangle[1]; // ptr
      simpleSplit(link, left_link, right_link);

      /* Propojeni diamondu. */
      left_link[0].right = right_child[0];
      right_child[0].left = left_link[0];
      right_link[0].left = left_child[0];
      left_child[0].right = right_link[0];

      /* Nastaveni priorit a trojuhelniku diamondu. */
      second_priority = link.getPriority();
      third = left_link[0];
      fourth = right_link[0];

      link.destructor();
    }
  }

  parent.destructor();

  /* Priprava reprezentace diamondu v prioritni fronte. */
  float priority = Math.max(first_priority, second_priority);
  SbROAMMergeQueueDiamond diamond = new SbROAMMergeQueueDiamond(first, second,
    third, fourth, priority); //ptr
  first.diamond = diamond;
  second.diamond = diamond;

  /* Nejde-li od degradovany diamond. */
  if (third != null)
  {
    third.diamond = diamond;
    fourth.diamond = diamond;
  }

  /* Vlozeni diamondu do fronty na rozdeleni. */
  merge_queue.add(diamond);
}
  
  
  
    /** Spojen�poloviny diamant.
    Spoj�dva trojheln�y \p left_child a \p right_child diamantu do jednoho
    rodi�vsk�o trojheln�u \p parent. Me vytvoit nov�diamanty.
    \param left_child Lev trojheln� z diamatu.
    \param right_child Prav trojheln� z diamantu.
    \param parent Vsledn rodi�vsk trojheln�. */
  protected void halfMerge(SbROAMSplitQueueTriangle  left_child,
      SbROAMSplitQueueTriangle  right_child,
      SbROAMSplitQueueTriangle[] parent) {

  int parent_index = (Array.minus(left_child.triangle, triangle_tree) - 1) / 2;
  SbROAMTriangle parent_triangle = triangle_tree.get(parent_index); // ptr

  /* Vlozeni otce do fronty na rozdeleni a odstraneni potomku */
  parent[0] = new SbROAMSplitQueueTriangle(parent_triangle,
    computePriority(parent_triangle));
  split_queue.remove(left_child);
  split_queue.remove(right_child);
  split_queue.add(parent[0]);

  /* Napojeni otce na sousedy. */
  parent[0].left = left_child.base;
  reconnectNeighbour(left_child.base, left_child, parent[0]);
  parent[0].right = right_child.base;
  reconnectNeighbour(right_child.base, right_child, parent[0]);

  /* Neni-li posledni uroven. */
  int level = parent_triangle.level;
  if (level > 1)
  {
    /* Sousede a uroven spojeneho trojuhelniku. */
    SbROAMSplitQueueTriangle left_neighbour = parent[0].left; // ptr
    SbROAMSplitQueueTriangle right_neighbour = parent[0].right; // ptr

    /* Muze-li vzniknout novy nedegradovany diamond. */
    if ((left_neighbour != null) && (right_neighbour != null))
    {
      /* Je-li rodic ze stejne urovne jako jeho nejblizsi sousede. */
      if ((left_neighbour.triangle.level == level) &&
        (right_neighbour.triangle.level == level) &&
        (left_neighbour.left.triangle.level == level))
      {
        /* Ctyri trojuhelniky noveho diamodnu */
        SbROAMSplitQueueTriangle first = null; // ptr
        SbROAMSplitQueueTriangle second = null; // ptr
        SbROAMSplitQueueTriangle third = null; //ptr
        SbROAMSplitQueueTriangle fourth = null; //ptr

        /* Je-li nove vytvoreny trojuhelnik pravym synem. */
        if (((parent_index - 1) % 2)!=0)
        {
          first = right_neighbour;
          second = parent[0];
          third = left_neighbour;
          fourth = left_neighbour.left;
        }
        else // je levym synem
        {
          first = parent[0];
          second = left_neighbour;
          third = right_neighbour.right;
          fourth = right_neighbour;
        }

        /* Nalezeni otcovskych trojuhelniku. */
        int left_index = (Array.minus(left_neighbour.triangle, triangle_tree) - 1) / 2;
        int right_index = (Array.minus(right_neighbour.triangle, triangle_tree) - 1) / 2;
        SbROAMTriangle first_triangle = triangle_tree.get(left_index); // ptr
        SbROAMTriangle second_triangle = triangle_tree.get(right_index); // ptr

        /* Vznik noveho diamondu. */
        float first_priority = computePriority(first_triangle);
        float second_priority = computePriority(second_triangle);
        float priority = Math.max(first_priority, second_priority);
        SbROAMMergeQueueDiamond new_diamond =
          new SbROAMMergeQueueDiamond(first, second, third, fourth, priority); // ptr
        first.diamond = new_diamond;
        second.diamond = new_diamond;
        third.diamond = new_diamond;
        fourth.diamond = new_diamond;

        /* Vlozeni diamondu do fronty na rozdeleni. */
        merge_queue.add(new_diamond);
      }
    }
    else
    {
      /* Muze-li vzniknout degradovany diamond. */
      if ((left_neighbour != null) || (right_neighbour != null))
      {
        /* Muze-li vzniknout s levym sousedem. */
        if (left_neighbour != null)
        {
          if (left_neighbour.triangle.level == level)
          {
            /* Nalezeni otcovskeho trojuhelniku. */
            int first_index = (parent_index - 1) / 2;
            SbROAMTriangle first_triangle = triangle_tree.get(first_index); //ptr

            /* Vznik noveho diamondu. */
            SbROAMMergeQueueDiamond new_diamond =
              new SbROAMMergeQueueDiamond(parent[0], left_neighbour, null, null, 
              computePriority(first_triangle)); // ptr
            parent[0].diamond = new_diamond;
            left_neighbour.diamond = new_diamond;

            /* Vlozeni diamondu do fronty na rozdeleni. */
            merge_queue.add(new_diamond);
          }
        }
        else // muze vzniknout s pravym sousedem
        {
          if (right_neighbour.triangle.level == level)
          {
            /* Nalezeni otcovskeho trojuhelniku. */
            int first_index = (parent_index - 1) / 2;
            SbROAMTriangle first_triangle = triangle_tree.get(first_index); // ptr

            /* Vznik noveho diamondu. */
            SbROAMMergeQueueDiamond new_diamond =
              new SbROAMMergeQueueDiamond(right_neighbour, parent[0], null, null,
              computePriority(first_triangle)); // ptr
            right_neighbour.diamond = new_diamond;
            parent[0].diamond = new_diamond;

            /* Vlozeni diamondu do fronty na rozdeleni. */
            merge_queue.add(new_diamond);
          }
        }
      }
    }
  }
}

  
  
  
  
    /** Spojen�diamantu.
    Spoj�diamant \p diamond z prioritn�fronty diamant na spojen� a
    vytvo�tak dva trojheln�y z pvodn�h �y v prioritn�front�    trojheln� na rozd�en�
    \param diamond Diamant z prioritn�fronty na rozd�en� */
  protected void merge(SbROAMMergeQueueDiamond diamond) {
    /* Otcove, kteri vzniknou spojenim diamondu. */
    final SbROAMSplitQueueTriangle[] first_parent = new SbROAMSplitQueueTriangle[1]; //ptr
    final SbROAMSplitQueueTriangle[] second_parent = new SbROAMSplitQueueTriangle[1]; //ptr

    /* Spojeni prvni poloviny diamondu */
    halfMerge(diamond.first, diamond.second, first_parent);

    /* Uvolneni potomku z pameti. */
    diamond.first.destructor();
    diamond.second.destructor();

    /* Neni-li diamond degradovany. */
    if (diamond.third != null)
    {
      /* Spojeni druhe poloviny diamondu a propojeni polovin. */
      halfMerge(diamond.third, diamond.fourth, second_parent);
      second_parent[0].base = first_parent[0];
      first_parent[0].base = second_parent[0];

      /* Uvolneni potomku z pameti. */
      diamond.third.destructor();
      diamond.fourth.destructor();
    }

    /* Odstraneni stareho diamondu. */
    merge_queue.remove(diamond);
    diamond.destructor();
  }

  
  
    /* Callbacky. */
    /** Callback zm�y pole \p ::mapSize.
    Pi zm��hodnoty pole \p ::mapSize nastav�jeho intern�reprezentaci novou
    hodnotu.
    \param instance Ukazatel na instanci t�y \p SoSimpleROAMTerrain.
    \param sensor Senzor, kter callback vyvolal. */
  protected static void mapSizeChangedCB(Object _instance, SoSensor sensor) {
		  /* Aktualizace vnitrni hodnoty pole. */
		  SoSimpleROAMTerrain instance =
		    (SoSimpleROAMTerrain)(_instance);
		  instance.map_size = instance.mapSize.getValue();
		  first_run = true; //YB
		  instance.touch(); //YB
		}

  
  
    /** Callback zm�y pole \p ::pixelError.
    Pi zm��hodnoty pole \p ::pixelError nastav�jeho intern�reprezentaci
    novou hodnotu.
    \param instance Ukazatel na instanci t�y \p SoSimpleROAMTerrain.
    \param sensor Senzor, kter callback vyvolal. */
  protected static void pixelErrorChangedCB(Object _instance, SoSensor sensor) {
		  /* Aktualizace vnitrni hodnoty pole. */
		  SoSimpleROAMTerrain instance =
		    (SoSimpleROAMTerrain)(_instance);
		  instance.pixel_error = instance.pixelError.getValue();
		}

  
  
    /** Callback zm�y pole \p ::triangleCount.
    Pi zm��hodnoty pole \p ::triangleCount nastav�jeho intern�reprezentaci
    novou hodnotu.
    \param instance Ukazatel na instanci t�y \p SoSimpleROAMTerrain.
    \param sensor Senzor, kter callback vyvolal. */
  protected static void triangleCountChangedCB(Object _instance, SoSensor sensor) {
		  /* Aktualizace vnitrni hodnoty pole. */
		  SoSimpleROAMTerrain instance =
		    (SoSimpleROAMTerrain)(_instance);
		  instance.triangle_count = instance.triangleCount.getValue();
		}

  
  
    /** Callback zm�y pole \p ::frustrumCulling.
    Pi zm��hodnoty pole \p ::frustrumCulling nastav�jeho intern�    reprezentaci novou hodnotu.
    \param instance Ukazatel na instanci t�y \p SoSimpleROAMTerrain.
    \param sensor Senzor, kter callback vyvolal. */
  protected static void frustrumCullingChangedCB(Object _instance, SoSensor sensor) {
		  /* Aktualizace vnitrni hodnoty pole. */
		  SoSimpleROAMTerrain instance =
		    (SoSimpleROAMTerrain)(_instance);
		  instance.is_frustrum_culling = instance.frustrumCulling.getValue();
		}

  
    /** Callback zm�y pole \p ::freeze.
    Pi zm��hodnoty pole \p ::freeze nastav�jeho intern�reprezentaci novou
    hodnotu.
    \param instance Ukazatel na instanci t�y \p SoSimpleROAMTerrain.
    \param sensor Senzor, kter callback vyvolal. */
  protected static void freezeChangedCB(Object _instance, SoSensor sensor) {
		  /* Aktualizace vnitrni hodnoty pole. */
		  SoSimpleROAMTerrain instance =
		    (SoSimpleROAMTerrain)(_instance);
		  instance.is_freeze = instance.freeze.getValue();
		}

    /* Zkratky elementu. */
    /// Body vykov�mapy.
  protected SbVec3f[] coords;
    /// Texturov�souadnice pro body vkov�mapy.
  protected SbVec2f[] texture_coords;
    /// Norm�y.
  protected SbVec3f[] normals;
    /// Pohledov�t�eso.
  protected SbViewVolume view_volume; //ptr
    /// Vykreslovac�okno.
  protected SbViewportRegion viewport_region; //ptr
    /// Roviny pohledoveho telesa
  protected final Array<SbPlane> planes = new Array<>(SbPlane.class,new SbPlane[6]);
    /* Datove polozky. */
    /// Bin�n�strom trojheln�.
  protected Array<SbROAMTriangle> triangle_tree; //ptr
    /// Velikost bin�n�o stromu trojheln�.
  protected int tree_size;
    /// Po�t rovn�bin�n�o stromu trojheln�.
  protected int level;
    /// Konstanta udavaj��po�t pixel na jeden radi� zorn�o pole.
  protected float lambda;
    /// Fronta trojheln� na rozd�en�
  protected SbROAMSplitQueue split_queue; //ptr
    /// Fronta diamant pro spojen�
  protected SbROAMMergeQueue merge_queue; //ptr
    /// P�nak pouit�textury.
  protected boolean is_texture;
    /// P�nak pouit�norm�.
  protected boolean is_normals;
    /* Interni pole. */
    /// Velikost (vka i �ka) vkov�mapy ter�u.
  protected int map_size;
    /// Chyba zobrazen�v pixelech.
  protected int pixel_error;
    /// Maxim�n�po�t trojheln� v triangulaci.
  protected int triangle_count;
    /// P�nak oez���ter�u mimo zorn�pole.
  protected boolean is_frustrum_culling;
    /// P�nak "zmrazen� vykreslov��ter�u.
  protected boolean is_freeze;
    /* Sensory. */
    /// Senzor pole \p ::mapSize.
  protected SoFieldSensor map_size_sensor; //ptr
    /// Senzor pole \p ::pixelError.
  protected SoFieldSensor pixel_error_sensor; //ptr
    /// Senzor pole \p ::triangleCount.
  protected SoFieldSensor triangle_count_sensor; //ptr
    /// Senzor pole \p ::frustrumCulling.
  protected SoFieldSensor frustrum_culling_sensor; //ptr
    /// Senzor pole \p ::freeze.
  protected SoFieldSensor freeze_sensor; //ptr
    /* Metody */
    /** Destruktor.
    Zprivatizov�, aby se zabr�ilo ruen�uzlu, nebo Inventor uvol�je pam�    ve vlastn�reii. */
  public void destructor() {
  /******************************************************************************
  * SoSimpleROAMTerrain - private
  ******************************************************************************/
    /* Uvolneni internich struktur. */
    Array.destructor(triangle_tree);
    for (int I = 1; I <= split_queue.size(); ++I)
    {
       (split_queue).operator_square_bracket(I).destructor();

    }
    split_queue.destructor();
    for (int J = 1; J <= merge_queue.size(); ++J)
    {
       (merge_queue).operator_square_bracket(J).destructor();
    }
    merge_queue.destructor();
  }
}
