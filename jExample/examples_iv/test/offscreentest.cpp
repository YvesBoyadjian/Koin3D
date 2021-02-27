// ./coin-config --build it ./offscreentest.cpp 

#include <Inventor/SoDB.h>
#include <Inventor/SoInput.h>
#include <Inventor/actions/SoCallbackAction.h>
#include <Inventor/actions/SoSearchAction.h>
#include <Inventor/actions/SoGLRenderAction.h>
#include <Inventor/nodes/SoSeparator.h>
#include <Inventor/nodes/SoIndexedFaceSet.h>
#include <Inventor/nodes/SoCube.h>
#include <Inventor/nodes/SoCone.h>
#include <Inventor/nodes/SoCylinder.h>
#include <Inventor/nodes/SoSphere.h>
#include <Inventor/SbColor.h>
#include <Inventor/misc/SoState.h>
#include <Inventor/elements/SoViewportRegionElement.h>
#include <Inventor/SoOffscreenRenderer.h>
#include <Inventor/nodes/SoPerspectiveCamera.h>
#include <Inventor/nodes/SoOrthographicCamera.h>
#include <Inventor/nodes/SoDirectionalLight.h>
#include <assert.h>
#include <stdio.h>


int 
main(int argc, char **argv)
{
  // first argument should be inventor or vrml file,
  // second argument name of image file.
  assert(argc == 3);
  
  SoDB::init();

  SoInput input;
  SbBool ret = input.openFile(argv[1]);
  assert(ret);

  SoSeparator *root = SoDB::readAll(&input);
  //  FILE *fp = fopen(argv[2], "wb");
  if (root){ // && fp) {
    //SbViewportRegion vp(640,450); // set size of image here
    SbViewportRegion vp(4608,3258); // set size of image here
    //SbViewportRegion vp(2828,2000); // set size of image here
    //    SbViewportRegion vp(1414,1000); // set size of image here
    // SbViewportRegion vp(1000,1414); // set size of image here
    //         SbViewportRegion vp(3500,3500); // set size of image here
    // biggest on apollo08
    //     SbViewportRegion vp(3850,3850); // set size of image here
    //  SbViewportRegion vp(6515,4608);
    // SbViewportRegion vp(8504,8504) ; // 18.0 cm width at 1200
    //    SbViewportRegion vp(4104,4104) ; // 8.7cm width at 1200
    root->ref();
    SoSearchAction sa;
    sa.setInterest(SoSearchAction::FIRST);
       sa.setType(SoLight::getClassTypeId());
    sa.apply(root);
    // if no light source, insert one
    if (sa.getPath() == NULL) {
      //              root->insertChild(new SoDirectionalLight, 0);
		     printf("imposed light\n");
    }
    // if no camera, insert one
    sa.reset();
    sa.setInterest(SoSearchAction::FIRST);
    sa.setType(SoCamera::getClassTypeId());
    sa.apply(root);
    if (sa.getPath() == NULL) {
      printf("imposed camera\n");
      SoOrthographicCamera * cam = new SoOrthographicCamera;
      root->insertChild(cam, 0);
      cam->viewAll(root, vp);
      //     cam->scaleHeight(0.46);
      cam->focalDistance=7;     // was 7
      // good values
      //      cam->orientation.setValue(1,0,0,-1.27933953231703);
      //      cam->position.setValue( 0,1.9156525704423,0.574695771132691 );


      cam->orientation.setValue(0,0,1,0);
      cam->position.setValue( 0,0,4 );




      //      cam->.setValue( 15.5,-610.07333703717,434.923685363132 );
      //cam->position.setValue(SbVec3f( 0,0,0 ));
      cam->nearDistance = 0.1;
      cam->farDistance = 100.0;
    }

    SoOffscreenRenderer orrend(vp);
    SoGLRenderAction * gl = orrend.getGLRenderAction();
    //    gl->setTransparencyType(SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_BLEND);
    //gl->setTransparencyType(SoGLRenderAction::SORTED_OBJECT_BLEND);
    //gl->setTransparencyType(SoGLRenderAction::SCREEN_DOOR);
    gl->setSmoothing(TRUE);
    gl->setNumPasses(1); // 9 pass antialiasing
    {int passes;
      passes=gl->getNumPasses();
     
      printf("smoothing passes:%d\n",passes);
    }

    orrend.setBackgroundColor(SbColor(1.0f, 1.0f, 1.0f));


    orrend.render(root);
    orrend.writeToFile(argv[2],"png");
    //        orrend.writeToRGB(fp);
                    printf ("here\n");
		   
		    //        orrend.writeToPostScript(fp);
    root->unref();
    //    fclose(fp);
  }
  return 0;
}


