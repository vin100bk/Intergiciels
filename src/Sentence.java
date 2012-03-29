public class Sentence implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String data;

	public Sentence() {

		data = new String("");
	}

	public void write(String text) {

		data = text;
	}

	public String read() {

		return data;
	}

}
