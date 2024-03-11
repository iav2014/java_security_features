package com.serialization;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ValidatingObjectInputStream extends ObjectInputStream {

  public ValidatingObjectInputStream(InputStream in) throws IOException {
    super(in);
  }

  @Override

  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

    if (!desc.getName().equals("com.data.Persona")) {
      throw new ClassNotFoundException("No se permiten tipos de objetos inesperados durante la deserialización: " + desc.getName());
    }
    return super.resolveClass(desc);
  }
}
