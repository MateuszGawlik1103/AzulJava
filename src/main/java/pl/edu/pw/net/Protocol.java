package pl.edu.pw.net;

public enum Protocol {
	HELLO,
	UPDATEGAME,
	TAKEFROMCENTER,
	TAKEFROMFACTORY;
	@Override
	public String toString() {
		return this.name();
	}
}
