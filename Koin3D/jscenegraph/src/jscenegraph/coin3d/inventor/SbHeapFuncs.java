/**
 * 
 */
package jscenegraph.coin3d.inventor;

/**
 * @author Yves Boyadjian
 *
 */
public class SbHeapFuncs {
	public interface Eval {
		float eval_func(Object obj);
	}

	public interface Get_index {
		int get_index_func(Object obj);
	}

	public interface Set_index {
		void set_index_func(Object obj, int index);
	}

	Eval eval_func;
	Get_index get_index_func;
	Set_index set_index_func;
	
	/**
	 * Constructor
	 * @param eval
	 * @param get_index
	 * @param set_index
	 */
	public SbHeapFuncs(Eval eval, Get_index get_index, Set_index set_index) {
		super();
		this.eval_func = eval;
		this.get_index_func = get_index;
		this.set_index_func = set_index;
	}
}
