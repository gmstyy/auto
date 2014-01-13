
package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TotalSkeleton extends Skeleton {

	private Map<Pixel, PixGroup> groups = new HashMap<>();
	private Set<PixGroup> groupSet = new HashSet<>();
	private int radius = 1;

	public TotalSkeleton(int rgb, int width, int height) {
		super(rgb, width, height);
	}

	private int groupIndex = 0;

	@Override
	public void addPix(Pixel pix) {
		super.addPix(pix);
		Set<PixGroup> neighbour = new HashSet<>();
		for (int i = pix.getX() - radius; i <= pix.getX() + radius; i++) {
			if (i < 0 || i >= this.getWidth()) {
				continue;
			}
			for (int j = pix.getY() - radius; j <= pix.getY() + radius; j++) {
				if (j < 0 || j >= this.getHeight()) {
					continue;
				}
				if (this.pixArr[i][j] != null && groups.containsKey(this.pixArr[i][j])) {
					PixGroup group = groups.get(this.pixArr[i][j]);
					// if(!neighbour.contains(group)){
					neighbour.add(group);
					// }
					// neighbour.add();
				}
			}
		}
		//System.out.println(pix.getX() + "  " + pix.getY());
		if (neighbour.size() == 0) {
			PixGroup group = new PixGroup(groupIndex++);
			group.add(pix);
			groups.put(pix, group);
			groupSet.add(group);
		} else if (neighbour.size() == 1) {
			PixGroup group = neighbour.iterator().next();
			group.add(pix);
			groups.put(pix, group);
		} else if (neighbour.size() > 1) {
			Iterator<PixGroup> it = neighbour.iterator();
			PixGroup firstGroup = it.next();
			while (it.hasNext()) {
				PixGroup group = it.next();
				for (Pixel p : group) {
					groups.put(p, firstGroup);
				}
				firstGroup.addAll(group);
				groupSet.remove(group);
			}
		}
	}

	@Override
	public void stimulated(Neure neure, boolean flag) {
		int index = 0;
		for (PixGroup group : groupSet) {
			if (new Double(group.size()) / this.getPixSet().size() > 0.1) {
				Skeleton sk = new Skeleton(name + "-" + index++, this.width, this.height);
				sk.addAllPix(group);
				sk.setFrontSet(this.getFrontSet());
				sk.stimulated(null, true);
			}
		}
	}

	private class PixGroup extends HashSet<Pixel> {

		Integer index;

		public PixGroup(int index) {
			super();
			this.index = index;
		}

		@Override
		public int hashCode() {
			return index.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return index.equals(o);
		}
	}
}
