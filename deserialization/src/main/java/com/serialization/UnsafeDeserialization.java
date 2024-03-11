package com.serialization;

import com.data.Persona;

import java.io.*;
/*
Problemas Comunes de Seguridad en Java
Inyección SQL: la inyección de comandos SQL a través de la entrada del usuario puede comprometer la seguridad
de la base de datos.

Cross-Site Scripting (XSS): Si tu aplicación Java se utiliza en el backend para sitios web, es vulnerable
a XSS si los datos del usuario se insertan en las páginas web sin la debida validación y escape.

Deserialización Insegura: La deserialización de objetos sin verificar puede permitir a un atacante ejecutar
 código arbitrario.

Exposición de Datos Sensibles: No proteger adecuadamente los datos sensibles, como contraseñas y tokens de acceso,
 puede llevar a su exposición.

Configuración Incorrecta de Seguridad: La falta de una configuración de seguridad adecuada,
como el uso de protocolos inseguros o permisos excesivos, puede abrir brechas de seguridad.


Deserialización Insegura
La deserialización insegura ocurre cuando los datos no confiables se utilizan para reconstruir
un objeto en una aplicación.
 */

public class UnsafeDeserialization {

  public static void writeToFile(Persona persona) {

    try (FileOutputStream fileOut = new FileOutputStream("persona.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
      out.writeObject(persona);
      System.out.println("Objeto Persona ha sido serializado en archivo: " + persona);
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  public static void readObjectUnsafe() {

    Persona persona = null;
    try (FileInputStream fileIn = new FileInputStream("persona.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn)) {
      persona = (Persona) in.readObject();
      System.out.println("(Unsafe) Objeto Persona ha sido deserializado desde archivo: " + persona);
    } catch (IOException i) {
      i.printStackTrace();
      return;
    } catch (ClassNotFoundException c) {
      System.out.println("Clase Persona no encontrada.");
      c.printStackTrace();
      return;
    }
  }

  public static void readObjectSafe() {

    try (FileInputStream fileIn = new FileInputStream("persona.ser");
         ValidatingObjectInputStream in = new ValidatingObjectInputStream(fileIn)) {
      Persona persona = (Persona) in.readObject();
      System.out.println("(Safe) Objeto Persona ha sido deserializado de forma segura: " + persona);
    } catch (IOException i) {
      i.printStackTrace();
    } catch (ClassNotFoundException c) {
      System.out.println("Clase no encontrada.");
      c.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {

    Persona persona = new Persona("Juan Perez", 30);
    writeToFile(persona);
    readObjectUnsafe();
    readObjectSafe();


  }
}

