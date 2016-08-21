package edu.columbia.cs.psl.phosphor.struct.multid;

import java.io.IOException;
import java.io.Serializable;

import edu.columbia.cs.psl.phosphor.TaintUtils;
import edu.columbia.cs.psl.phosphor.runtime.LazyArrayObjTags;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import org.objectweb.asm.Type;

public final class MultiDTaintedLongArrayWithObjTag extends MultiDTaintedArrayWithObjTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1632040273380781122L;
	public long[] val;

	public MultiDTaintedLongArrayWithObjTag(LazyArrayObjTags taint, long[] val) {
		super(taint, Type.LONG);
		this.val = val;
	}

	@Override
	public Object getVal() {
		return val;
	}

	@Override
	public Object clone() {
		return new MultiDTaintedLongArrayWithObjTag((LazyArrayObjTags) taint.clone(), val.clone());
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
			stream.writeLong(val[i]);
		}
	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		int len = stream.readInt();
		val = new long[len];
		taint = new LazyArrayObjTags(new Taint[len]);
		for (int i = 0; i < len; i++) {
			if (TaintUtils.TAINT_THROUGH_SERIALIZATION)
				taint.taints[i] = (Taint) stream.readObject();
			val[i] = stream.readLong();
		}
	}
}
