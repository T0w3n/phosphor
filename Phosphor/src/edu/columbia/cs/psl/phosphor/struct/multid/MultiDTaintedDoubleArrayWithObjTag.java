package edu.columbia.cs.psl.phosphor.struct.multid;

import java.io.IOException;
import java.io.Serializable;

import edu.columbia.cs.psl.phosphor.TaintUtils;
import edu.columbia.cs.psl.phosphor.runtime.LazyArrayObjTags;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import org.objectweb.asm.Type;

public final class MultiDTaintedDoubleArrayWithObjTag extends MultiDTaintedArrayWithObjTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2983786880082730018L;
	public double[] val;

	public MultiDTaintedDoubleArrayWithObjTag(LazyArrayObjTags taint, double[] val) {
		super(taint, Type.DOUBLE);
		this.val = val;
	}

	@Override
	public Object getVal() {
		return val;
	}

	@Override
	public Object clone() {
		return new MultiDTaintedDoubleArrayWithObjTag((LazyArrayObjTags) taint.clone(), val.clone());
	}

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		if (val == null) {
			stream.writeObject(null);
			return;
		}
		stream.writeInt(val.length);
		for (int i = 0; i < val.length; i++) {
			if (TaintUtils.TAINT_THROUGH_SERIALIZATION)
				stream.writeObject(taint.taints == null ? null : taint.taints[i]);
			stream.writeDouble(val[i]);
		}
	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		int len = stream.readInt();
		val = new double[len];
		taint = new LazyArrayObjTags(new Taint[len]);
		for (int i = 0; i < len; i++) {
			if (TaintUtils.TAINT_THROUGH_SERIALIZATION)
				taint.taints[i] = (Taint) stream.readObject();
			val[i] = stream.readDouble();
		}
	}
}
