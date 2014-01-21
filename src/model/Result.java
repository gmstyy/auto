
package model;

public class Result extends Neure {

	private boolean train;

	@Override
	public void stimulated(Neure neure, boolean enhance) {
		Recognition rec = (Recognition) neure;
		Skeleton sk = rec.getCurrentSk();
		if (train) {
			if (enhance) {
				if (rec.getValue().equals(sk.getValue())) {
					rec.feedback(this, true);
				} else {
					rec.feedback(this, false);
				}
				System.out.println("  " + sk.getName() + " " + rec.getName() + " " + rec.getScore());
			} else {
				if (rec.getValue().equals(sk.getName())) {
					rec.feedback(this, true);
				}
			}
		}
	}

	public boolean isTrain() {
		return train;
	}

	public void setTrain(boolean train) {
		this.train = train;
	}

	@Override
	public void feedback(Neure neure, boolean enhance) {
		// TODO Auto-generated method stub
	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub
	}
}
