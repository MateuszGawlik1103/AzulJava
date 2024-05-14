package pl.edu.pw;

public class Exceptions {
	public static class AzulLineException extends Exception {
		public AzulLineException() {
			super();
		}

	}

	public static class AzulLineAlreadyFilledException extends AzulLineException {
		public AzulLineAlreadyFilledException() {
			super();
		}

	}

	public static class AzulLineDifferentColorsException extends AzulLineException {
		public AzulLineDifferentColorsException() {
			super();
		}

	}

	public static class AzulNameTakenException extends Exception {
		public AzulNameTakenException() {
			super();
		}
	}
}
