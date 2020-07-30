/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import java.util.Objects;

import jscenegraph.coin3d.inventor.base.SbString;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoProto SoProto.h Inventor/misc/SoProto.h
  \brief The SoProto class handles PROTO definitions.

  SoProto and SoProtoInstance are mostly internal classes. They're
  designed to read and handle VRML97 PROTOs. However, it's possible to
  define your PROTOs in C++. You must define your PROTO in a char
  array, and read that char array using SoInput::setBuffer() and
  SoDB::readAllVRML(). Example:

  \code

  char myproto[] =
  "#VRML V2.0 utf8\n"
  "PROTO ColorCube [\n"
  "  field SFColor color 1 1 1\n"
  "  field SFVec3f size 1 1 1\n"
  "]\n"
  "{\n"
  "  Shape {\n"
  "    appearance Appearance {\n"
  "      material Material {\n"
  "        diffuseColor IS color\n"
  "      }\n"
  "    }\n"
  "    geometry Box { size IS size }\n"
  "  }\n"
  "}\n"
  "ColorCube { color 1 0 0 size 2 1 1 }\n";

  SoInput in;
  in.setBuffer((void*) myproto, strlen(myproto));
  SoVRMLGroup * protoroot = SoDB::readAllVRML(&in);

  \endcode

  Now you can create new instances of the ColorCube PROTO using
  SoProto::findProto() and SoProto::createProtoInstance(). If you want
  to insert PROTO instances into your scene graph, you should insert
  the node returned from SoProtoInstance::getRootNode().

  See
  http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.8
  for more information about PROTOs in VRML97.

*/

// *************************************************************************

public class SoProto extends SoNode {

	static public interface SoFetchExternProtoCB {
		SoProto invoke(SoInput in,
            final String[] urls,
            final int numurls,
            Object closure);
	}

static final SoType soproto_type = new SoType();

static SbList <SoProto[]> protolist; //ptr;
static SoFetchExternProtoCB soproto_fetchextern_cb = null; //ptr
static Object soproto_fetchextern_closure = null; //ptr
static Object soproto_mutex; //ptr

	private SoProtoP pimpl;

	/*!
  Constructor.
*/
public SoProto( boolean externproto)
{
  pimpl = new SoProtoP();
  pimpl.externurl = null;
  if (externproto) {
    pimpl.externurl = new SoMFString();
  }
  pimpl.fielddata = new SoFieldData();
  pimpl.defroot = new SoGroup();
  pimpl.defroot.ref();
  pimpl.extprotonode = null;

  //CC_MUTEX_LOCK(soproto_mutex);
  final SoProto[] that = new SoProto[1];
  that[0] = this;
  protolist.insert(that, 0);
  //CC_MUTEX_UNLOCK(soproto_mutex);
}

	
/*!
  Returns the PROTO name.
*/
public SbName getProtoName()
{
  return new SbName(pimpl.name);
}

// Documented in superclass. Overridden to read Proto definition.
public boolean readInstance(SoInput in, short flags)
{
  final SbName protoname = new SbName();

  final char[] c = new char[1];
  boolean ok = in.read(protoname, true);
  if (ok) {
    pimpl.name.copyFrom(protoname);
    ok = this.readInterface(in);
  }
  if (!ok) {
    SoReadError.post(in, "Error parsing PROTO interface.");
  }
  else if (null == pimpl.externurl) {
    ok = in.read(c) && c[0] == '{';
    if (ok) ok = this.readDefinition(in);
  }
  else {
    ok = pimpl.externurl.read(in, new SbName("EXTERNPROTO URL"));
    if (ok) {
      SoProto proto = soproto_fetchextern_cb.invoke(in,
                                               pimpl.externurl.getValues(0),
                                               pimpl.externurl.getNum(),
                                               soproto_fetchextern_closure);
      if (proto == null) {
        SoReadError.post(in, "Error reading EXTERNPROTO definition.");
        ok = false;
      }
      else {
        ok = this.setupExtern(in, proto);
      }
    }
  }
  return ok;
}

//
// Reads the definition
//
public boolean readDefinition(SoInput in)
{
  Boolean ok = true;
  final SoBase[] child = new SoBase[1]; //ptr
  in.pushProto(this);

  while (ok) {
    ok = SoBase.read(in, child, SoNode.getClassTypeId());
    if (ok) {
      if (child[0] == null) {
        if (in.eof()) {
          ok = false;
          SoReadError.post(in, "Premature end of file");
        }
        break; // finished reading, break out
      }
      else {
        pimpl.defroot.addChild((SoNode) child[0]);
      }
    }
  }
  in.popProto();
  final char[] c = new char[1];
  return ok && in.read(c) && c[0] == '}';
}


private static SoProto
soproto_fetchextern_default_cb(SoInput in,
                               final String[] urls,
                               int numurls,
                               Object closure)
{
  if (numurls == 0) return null;
  String filename = urls[0];
  String name = "";

  int nameidx = SbString.find(filename,"#");
  if (nameidx >= 1) {
    String tmp = filename;
    filename = SbString.getSubString(tmp, 0, nameidx-1);
    name = SbString.getSubString(tmp, nameidx+1);
  }

  if (!in.pushFile(filename)) {
    SoReadError.post(in, "Unable to find EXTERNPROTO file: ``"+filename+"''");
    return null;
  }

  SoSeparator root = SoDB.readAll(in);
  if (root == null) {
    // Take care of popping the file off the stack. This is a bit
    // "hack-ish", but its done this way instead of loosening the
    // protection of SoInput::popFile().
    if (Objects.equals(in.getCurFileName(),filename)) {
      final char[] dummy = new char[1];
      while (!in.eof() && in.get(dummy)) { }

      assert(in.eof());

      // Make sure the stack is really popped on EOF. Popping happens
      // when attempting to read when the current file in the stack is
      // at EOF.
      boolean gotchar = in.get(dummy);
      if (gotchar) in.putBack(dummy[0]);
    }

    SoReadError.post(in, "Unable to read EXTERNPROTO file: ``"+filename+"''");
    return null;
  }
  else {
    root.ref();
    SoProto foundproto = null; //ptr

    final SoSearchAction sa = new SoSearchAction();
    sa.setType(SoProto.getClassTypeId());
    sa.setSearchingAll(true);
    sa.setInterest(SoSearchAction.Interest.ALL);
    sa.apply(root);

    final SoPathList pl = sa.getPaths();

    if (pl.getLength() == 1) {
      foundproto = (SoProto) pl.operator_square_bracket(0).getTail();
      if (name.length() != 0 && name != foundproto.getProtoName().getString()) {
        foundproto = null;
      }
    }
    else if (name.length() != 0) {
      int i;
      for (i = 0; i < pl.getLength(); i++) {
        SoProto proto = (SoProto) pl.operator_square_bracket(i).getTail();
        if (name == proto.getProtoName().getString()) break;
      }
      if (i < pl.getLength()) {
        foundproto = (SoProto) pl.operator_square_bracket(i).getTail();
      }
    }
    sa.reset(); // clear paths in action.
    if (foundproto != null) foundproto.ref();
    root.unref();
    if (foundproto != null) foundproto.unrefNoDelete();
    
    Destroyable.delete(sa);
    return foundproto;
  }

  // just in case to fool stupid compilers
  //return null; no more stupid compiler in java !
}


public static void setFetchExternProtoCallback(SoFetchExternProtoCB cb,
                                     Object closure)
{
  if (cb == null) {
    soproto_fetchextern_cb = SoProto::soproto_fetchextern_default_cb;
    soproto_fetchextern_closure = null;
  }
  else {
    soproto_fetchextern_cb = cb;
    soproto_fetchextern_closure = closure;
  }
}


/*!
  Adds a ROUTE for this PROTO definition.
*/
public void addRoute(SbName fromnode, SbName fromfield,
                  SbName tonode, SbName tofield)
{
  pimpl.routelist.append(fromnode);
  pimpl.routelist.append(fromfield);
  pimpl.routelist.append(tonode);
  pimpl.routelist.append(tofield);
}

//
// Reads the interface
//
public boolean readInterface(SoInput in)
{
  boolean ok = pimpl.fielddata.readFieldDescriptions(in, this, 4, pimpl.externurl == null);
  if ( ok ) {
    int numfields = pimpl.fielddata.getNumFields();
    for (int i = 0; i < numfields; i++) {
      SoField f = pimpl.fielddata.getField(this, i);
      switch ( SoField.FieldType.fromValue(f.getFieldType()) ) {
      case NORMAL_FIELD:
      case EXPOSED_FIELD:
        f.setDefault(true);
      }
    }
  }
  return ok;
}


public boolean setupExtern(SoInput in, SoProto externproto)
{
  assert(externproto != null);
  pimpl.extprotonode = externproto;
  pimpl.extprotonode.ref();
  return true;
}

/*!
  Adds an IS reference for this PROTO definition.
*/
public void addISReference(SoNode container,
                        final SbName fieldname,
                        final SbName interfacename)
{
  assert(container.isOfType(SoNode.getClassTypeId()));
  SoNode[] dummy = new SoNode[1];
  dummy[0] = container;
  pimpl.isnodelist.append(/*container*/dummy);
  pimpl.isfieldlist.append(fieldname);
  pimpl.isnamelist.append(interfacename);
}

// doc in parent
public static void initClass()
{
  //CC_MUTEX_CONSTRUCT(soproto_mutex); TODO
  soproto_type.copyFrom(SoType.createType(SoNode.getClassTypeId(),
                                    new SbName("SoProto"), null,
                                    (short)SoNode.nextActionMethodIndex));
  SoNode.nextActionMethodIndex++;
  protolist = new SbList<SoProto[]>();

  //coin_atexit((coin_atexit_f*) soproto_cleanup, CC_ATEXIT_NORMAL);
  // this will set a default callback
  SoProto.setFetchExternProtoCallback(null, null);
}

}
