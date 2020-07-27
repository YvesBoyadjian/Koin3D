/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import java.util.HashMap;
import java.util.Map;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoProtoP {

	  SoFieldData fielddata; //ptr
	  SoGroup defroot; //ptr
		final SbName name = new SbName();
	  final SbList <SoNode[]> isnodelist = new SbList<>(); // FIXME: consider using SoNodeList
	  final SbList <SbName> isfieldlist = new SbList<>();
	  final SbList <SbName> isnamelist = new SbList<>();
	  final Map<String,SoBase> refdict = new HashMap<>();
	  final SbList <SbName> routelist = new SbList<>();
	  SoMFString externurl; //ptr
	  SoProto extprotonode; //ptr
}
