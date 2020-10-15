/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import java.util.Objects;

import jscenegraph.coin3d.inventor.base.SbString;
import jscenegraph.coin3d.inventor.engines.SoNodeEngine;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.*;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.Destroyable;
import jscenegraph.port.StringArray;
import jscenegraph.port.Util;

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
            final StringArray urls,
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
                               final StringArray urls,
                               int numurls,
                               Object closure)
{
  if (numurls == 0) return null;
  String filename = urls.getO(0);
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
      if (name.length() != 0 && !name.equals(foundproto.getProtoName().getString())) {
        foundproto = null;
      }
    }
    else if (name.length() != 0) {
      int i;
      for (i = 0; i < pl.getLength(); i++) {
        SoProto proto = (SoProto) pl.operator_square_bracket(i).getTail();
        if (name.equals(proto.getProtoName().getString())) break;
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


// doc in parent
public SoType getTypeId()
{
  return soproto_type;
}

// doc in parent
public static SoType getClassTypeId()
{
  return soproto_type;
}


/*!
  Returns the PROTO definition named \a name or NULL if not found.
*/
public static SoProto findProto(final SbName name)
{
  SoProto ret = null;
  //CC_MUTEX_LOCK(soproto_mutex);
  if (protolist != null) {
    int n = protolist.getLength();
    SoProto[][] ptr = new SoProto[n][]; 
    ptr = protolist.getArrayPtr(ptr);
    for (int i = 0; (ret == null) && (i < n); i++) {
      if (ptr[i][0].getProtoName().operator_equal_equal(name)) ret = ptr[i][0];
    }
  }
  //CC_MUTEX_UNLOCK(soproto_mutex);
  return ret;
}

/*!
  Creates an instance of the PROTO.
*/
public SoProtoInstance createProtoInstance()
{
  if (pimpl.extprotonode != null) {
    return pimpl.extprotonode.createProtoInstance();
  }
  SoProtoInstance inst = new SoProtoInstance(this, pimpl.fielddata);
  inst.ref();
  inst.setRootNode(this.createInstanceRoot(inst));
  return inst;
}

  static SoNode
  soproto_find_node(SoNode root, SbName name, final SoSearchAction sa)
  {
    sa.setName(name);
    sa.setInterest(SoSearchAction.Interest.FIRST);
    sa.setSearchingAll(true);

    sa.apply(root);

    SoNode ret = null;

    if (sa.getPath() != null) {
      ret = (SoFullPath.cast(sa.getPath())).getTail();
    }
    sa.reset();
    return ret;
  }

  //
// Used to check for fieldname. Wil first test "<name>", then "set_<name>",
// and then "<name>_changed".
//
  static SbName
  soproto_find_fieldname(SoNode node, final SbName name)
  {
    if (node.getField(name) != null) return name;
    String test;
    if (Util.strncmp("set_", name.getString(), 4) == 0) {
      test = name.getString() + 4;
      if (node.getField(test) != null) return new SbName(test);
    }
    test = name.getString();
    // test for "_changed" at the end of the fieldname
    if (test.length() > 8) { // 8 == strlen("_changed")
      test = test.substring(0, test.length() - 9);
      if (node.getField(test) != null) return new SbName(test);
    }
    return name;
  }

  //
// Used to check for outputname. Wil first test "<name>", then "set_<name>",
// and then "<name>_changed".
//
  static SbName
  soproto_find_outputname(SoNodeEngine node, final SbName name)
  {
    if (node.getOutput(name) != null) return name;
    String test;
    if (Util.strncmp("set_", name.getString(), 4) == 0) {
      test = name.getString() + 4;
      if (node.getOutput(new SbName(test)) != null) return new SbName(test);
    }
    test = name.getString();
    // test for "_changed" at the end of the fieldname
    if (test.length() > 8) { // 8 == strlen("_changed")
      test = test.substring(0, test.length() - 9);
      if (node.getOutput(new SbName(test))!=null) return new SbName(test);
    }
    return name;
  }

//
// helper function to find field. First test the actual fieldname,
// then set set_<fieldname>, then <fieldname>_changed.
//
  static SoField
  soproto_find_field(SoNode node, final SbName fieldname)
  {
    SoField field = node.getField(fieldname);

    if (field == null) {
      if (Util.strncmp(fieldname.getString(), "set_", 4) == 0) {
        SbName newname = new SbName(fieldname.getString().substring(4));// + 4;
        field = node.getField(newname);
      }
      else {
        String str = fieldname.getString();
        int len = str.length();
      final String CHANGED = "_changed";
      int changedsize = (CHANGED.length()+1) - 1;

        if (len > changedsize && Util.strcmp(str.substring(len-changedsize),
                CHANGED) == 0) {
          String substr = str.substring(0, len-(changedsize+1));
          SbName newname = new SbName(substr);
          field = node.getField(newname);
        }
      }
    }
    return field;
  }

//
// Create a root node for a PROTO instance
//
private SoNode createInstanceRoot(SoProtoInstance inst) {
  if (pimpl.extprotonode != null) {
    return pimpl.extprotonode.createInstanceRoot(inst);
  }

  SoNode root; //ptr
  if (pimpl.defroot.getNumChildren() == 1)
  root = pimpl.defroot.getChild(0);
  else root = pimpl.defroot;

  SoNode cpy; // ptr
  cpy = root.copy(false);
  cpy.ref();
  this.connectISRefs(inst, root, cpy);

  int n = pimpl.routelist.getLength() / 4;
  final SoSearchAction sa = new SoSearchAction();

  for (int i = 0; i < n; i++) {
    SbName fromnodename = new SbName(pimpl.routelist.operator_square_bracket(i*4));
    SbName fromfieldname = new SbName(pimpl.routelist.operator_square_bracket(i*4+1));
    SbName tonodename = new SbName(pimpl.routelist.operator_square_bracket(i*4+2));
    SbName tofieldname = new SbName(pimpl.routelist.operator_square_bracket(i*4+3));

    SoNode fromnode = soproto_find_node(cpy, fromnodename, sa); //ptr
    SoNode tonode = soproto_find_node(cpy, tonodename, sa); //ptr

    if (fromnode != null && tonode != null) {
      SoField from = soproto_find_field(fromnode, fromfieldname); //ptr
      SoField to = soproto_find_field(tonode, tofieldname); //ptr
      SoEngineOutput output = null; //ptr
      if (from == null && fromnode.isOfType(SoNodeEngine.getClassTypeId())) {
        output = ((SoNodeEngine) fromnode).getOutput(fromfieldname);
      }

      if (to != null && (from != null || output != null)) {
        boolean notnotify = false;
        boolean append = false;
        if (output != null || from.getFieldType() == SoField.FieldType.EVENTOUT_FIELD.getValue()) {
          notnotify = true;
        }
        if (to.getFieldType() == SoField.FieldType.EVENTIN_FIELD.getValue()) append = true;

        // Check that there exists a field converter, if one is needed.
        SoType totype = to.getTypeId();
        SoType fromtype = from != null ? from.getTypeId() : output.getConnectionType();
        if (totype.operator_not_equal(fromtype)) {
          SoType convtype = SoDB.getConverter(fromtype, totype);
          if (convtype.operator_equal_equal(SoType.badType())) {
            continue;
          }
        }

        boolean ok;
        if (from != null) ok = to.connectFrom(from, notnotify, append);
        else ok = to.connectFrom(output, notnotify, append);
        // Both known possible failure points are caught above.
        assert(ok);// && "unexpected connection error");

      }
    }
  }

  cpy.unrefNoDelete();

  sa.destructor(); // java port

  return cpy;
}

  static SoNode
  locate_node_copy(SoNode searchfor, SoNode org, SoNode cpy)
  {
    if (org == null) return null;
    if (cpy == null) return null;

    if (org.getTypeId().operator_not_equal(cpy.getTypeId())) return null;
    if (org == searchfor) return cpy;

  final SoFieldData fd = org.getFieldData();
  final SoFieldData fd2 = cpy.getFieldData();

    int n = fd != null ? fd.getNumFields() : 0;
    int n2 = fd2 != null ? fd2.getNumFields() : 0;

    if (n != n2) {
      // should never happen (in theory)
      SoDebugError.postWarning("SoProto::locate_node_copy",
              "SoFieldData mismatch in PROTO scene.");
      return null;
    }

    int i;

    SoType sosftype = SoSFNode.getClassTypeId(SoSFNode.class);
    for (i = 0; i < n; i++) {
      SoField orgf = fd.getField(org, i);
      if (orgf.getTypeId().operator_equal_equal(sosftype)) {
        SoNode orgnode = ((SoSFNode) orgf).getValue();
        if (orgnode != null) {
          SoField cpyf = fd2.getField(cpy, i);
          if (cpyf.getTypeId() == sosftype) {
            SoNode found = locate_node_copy(searchfor, orgnode, ((SoSFNode) cpyf).getValue());
            if (found != null) return found;
          }
          else {
            SoDebugError.postWarning("SoProto::locate_node_copy",
                    "SoField mismatch in PROTO scene.");
            return null;
          }
        }
      }
    }

    SoChildList cl = org.getChildren();
    if (cl != null) {
      SoChildList cl2 = cpy.getChildren();
      n = Math.min(cl.getLength(), cl2.getLength());
      for (i = 0; i < n; i++) {
        SoNode found = locate_node_copy(searchfor, (cl).operator_square_bracket(i), (cl2).operator_square_bracket(i));
        if (found != null) return found;
      }
    }
    return null;
  }

//
// Connects all IS references for the a new instance
//
  public void connectISRefs(SoProtoInstance inst, SoNode src, SoNode dst)
  {
    if (pimpl.externurl != null) {
    SoDebugError.postWarning("SoProto::connectISRefs",
            "EXTERNPROTO URL fetching is not yet supported.");
    return;
  }

  final int n = pimpl.isfieldlist.getLength();

    for (int i = 0; i < n; i++) {
      SoNode node = pimpl.isnodelist.operator_square_bracket(i)[0]; //ptr

      SbName fieldname = new SbName(pimpl.isfieldlist.operator_square_bracket(i));
      fieldname = soproto_find_fieldname(node, fieldname);
      SoField dstfield = node.getField(fieldname); //ptr
      SoEngineOutput eventout = null; //ptr

      if (dstfield == null) {
        if (node.isOfType(SoNodeEngine.getClassTypeId())) {
          fieldname = soproto_find_outputname((SoNodeEngine)node, fieldname);
          eventout = ((SoNodeEngine)node).getOutput(fieldname);
        }
        if (eventout == null) {
//#if COIN_DEBUG
          SoDebugError.postWarning("SoProto::connectISRefs",
                  "Destionation field '"+fieldname.getString()+"' is not found in node type '"+node.getTypeId().getName().getString()+"'. "+
          "Unable to resolve IS reference.");
//#endif // COIN_DEBUG
          continue; // skip to next field
        }
      }

      boolean isprotoinstance = false;
      if (node.isOfType(SoProtoInstance.getClassTypeId())) {
        node = ((SoProtoInstance) node).getRootNode();
        isprotoinstance = true;
      }
      SbName iname = new SbName(pimpl.isnamelist.operator_square_bracket(i));

      node = locate_node_copy(node, src, dst);

      if (node == null) {
        SoDebugError.postWarning("SoProto::connectISRefs",
                "Unable to find '"+fieldname.getString()+"' from '"+iname.getString()+"' in '"+pimpl.name.getString()+"' PROTO");
        continue;
      }

      if (dstfield != null) {
        if (isprotoinstance) {
          node = SoProtoInstance.findProtoInstance(node);
          assert(node != null);
        }
        dstfield = node.getField(fieldname);
      }
      else {
        assert(node.isOfType(SoNodeEngine.getClassTypeId()));
        eventout = ((SoNodeEngine)node).getOutput(fieldname);
      }
      assert(dstfield != null || eventout != null);
      SoField srcfield = inst.getField(iname); //ptr
      if (srcfield != null) {
        // if destination field is an eventOut field, or an EngineOutput,
        // reverse the connection, since we then just need to route the
        // events to the srcfield.
        if (eventout != null) {
          srcfield.connectFrom(eventout);
        }
        else if (dstfield.getFieldType() == SoField.FieldType.EVENTOUT_FIELD.getValue()) {
          srcfield.connectFrom(dstfield);
        }
        else {
          // We make bidirectional connections for regular fields.  That way
          // you can modify the fields in the scene graph and have their PROTO
          // instances written to file with the updated values.  The alternative
          // is to have to locate the PROTO instance object yourself, and
          // modify the fields on it directly - 20040115 larsa
          srcfield.setDefault(false);
          dstfield.connectFrom(srcfield);
//#if 0 // start of problematic code
//          // this piece of code causes problems when writing PROTO
//          // instances, since the PROTO instance is counted once for
//          // each IS connection. The code is enabled for now, but I'll
//          // investigate more if this bidirectional connection is really
//          // necessary and if we should handle this case when counting
//          // write references. pederb, 2005-11-15
//
//          // update 2005-12-16, pederb:
//          // This bidirectional thingie also causes bugs when importing
//          // gator_1.wrl (the connections are not set up correctly, or
//          // are messed up). It seems like we probably should disable
//          // this code since it causes a lot of problems.
//
//          // propagate value immediately, before setting up reverse connection
//          dstfield->evaluate();
//          srcfield->connectFrom(dstfield, FALSE, TRUE);
//          // propagate value immediately, so we can tag field as default
//          srcfield->evaluate();
//          if ( srcisdefault ) srcfield->setDefault(TRUE);
//#endif // end of problemetic code
        }
      }
      else {
        assert(dstfield != null);
        SoEngineOutput output = null;
//        if (inst.isOfType(SoNodeEngine.getClassTypeId())) { TODO java port
//          output = ((SoNodeEngine) inst).getOutput(iname);
//        }
        if (output != null) {
          dstfield.connectFrom(output);
        }
//#if COIN_DEBUG
      else {
          SoDebugError.postWarning("SoProto::connectISRefs",
                  "Source field or engine output '"+iname.getString()+"' is not found in node type '"+node.getTypeId().getName().getString()+"'. "+
          "Unable to resolve IS reference.");
        }
//#endif // COIN_DEBUG
      }
    }
  }


/*!
  Adds a reference for this PROTO definition.
*/
  public void addReference(final SbName name, SoBase base)
  {
    pimpl.refdict.put(name.getString(), base);
  }

/*!
  Removes a reference for this PROTO definition.
*/
  public void removeReference(final SbName name)
  {
    pimpl.refdict.remove(name.getString());
  }

  public boolean setupExtern(SoInput in, SoProto externproto)
  {
    assert(externproto != null);
    pimpl.extprotonode = externproto;
    pimpl.extprotonode.ref();
    return true;
  }
}
