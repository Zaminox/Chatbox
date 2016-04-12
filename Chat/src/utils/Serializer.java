package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Serializer {
	
	public static byte[] serializeObject(Object object) {
		byte[] serialized = null;
		
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(byteOut);
			
			try {
				out.writeObject(object);
				serialized = byteOut.toByteArray();
			} finally {
				out.close();
				byteOut.close();
			}
		} catch(Exception e) {}
		
		return serialized;
	}
	
	public static Object deserializeObject(byte[] serialized) {
		Object object = null;
		
		try {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(serialized);
			ObjectInput in = new ObjectInputStream(byteIn);
			
			try {
				object = in.readObject();
			} finally {
				in.close();
				byteIn.close();
			}
		} catch(Exception e) {}
		
		return object;
	}
	
}
