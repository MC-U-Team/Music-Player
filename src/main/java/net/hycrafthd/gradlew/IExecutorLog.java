package net.hycrafthd.gradlew;

import java.io.InputStream;

public interface IExecutorLog {
	
	void log(InputStream inputstream);
	
	void print(String string);
	
}
