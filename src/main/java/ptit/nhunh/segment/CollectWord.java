package ptit.nhunh.segment;

import ptit.nhunh.segment.WordProcess;

public class CollectWord {
	public static void main(String[] args) {
		new CollectWord().process();
	}
	
	public void process() {
		new WordProcess().segment("comment", "Scomment");
	}
}
