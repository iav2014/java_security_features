package com.storage;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
/*
Cifrado de contraseñas:
PBEKeySpec es una especificación de clave utilizada en la criptografía de Java para la generación de claves basada en
contraseña en algoritmos de cifrado de contraseña (Password-Based Encryption, PBE). Es parte del paquete javax.crypto.spec.

La criptografía basada en contraseña utiliza contraseñas para generar claves criptográficas seguras que luego se pueden
utilizar para cifrar o descifrar datos. Dado que las contraseñas suelen ser más débiles que las claves criptográficas
generadas aleatoriamente (debido a su menor entropía y a que a menudo se eligen de una manera predecible por los usuarios),
 el proceso de PBE incluye técnicas como el uso de un "salt" (un valor aleatorio que se mezcla con la contraseña para
 producir claves únicas incluso para contraseñas idénticas entre diferentes instancias) y un proceso iterativo de hashing
 para fortalecer la generación de claves y hacerlas más resistentes a los ataques.

La seguridad de PBEKeySpec depende de varios factores:

La complejidad de la contraseña: Contraseñas más largas y complejas son más seguras.
El algoritmo de cifrado y el esquema PBE utilizado: Diferentes algoritmos tienen diferentes fortalezas;
algunos pueden estar obsoletos y ser vulnerables.
El tamaño de la clave generada: Claves más largas ofrecen una mayor seguridad.
El "salt" y el número de iteraciones: Un "salt" único y un número alto de iteraciones pueden hacer que la clave sea más
resistente a ataques de fuerza bruta y ataques de diccionario.
PBEKeySpec permite especificar todos estos parámetros. Por ejemplo, en el código que proporcioné anteriormente,
se utiliza el algoritmo PBKDF2WithHmacSHA1 para el hashing de la contraseña,
que es un algoritmo de derivación de clave basado en una función de hash criptográficamente segura (HmacSHA1)
 y está diseñado específicamente para ser computacionalmente costoso y resistente a ataques de fuerza bruta:

ejemplo:

PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, 65536, 128);
En este caso, 65536 es el número de iteraciones (lo que hace que el proceso de derivación de la clave sea más lento y, por
tanto, más resistente a ataques), y 128 es el tamaño de la clave generada.

Mientras se utilicen prácticas seguras como una contraseña fuerte, un "salt" único por contraseña, un número adecuado
de iteraciones, y algoritmos actualizados y seguros, PBEKeySpec puede ser considerado seguro para la generación de
claves basadas en contraseña. Sin embargo, es importante estar al tanto de los avances en criptografía y seguridad informática,
 ya que las recomendaciones pueden cambiar con el tiempo.
 
Se usa texto plano en vez de guardar en BBDD. que es mas rapido:

Almacenamiento Inseguro: Escribe directamente el nombre de usuario y la contraseña en un archivo unsafe.txt
en texto plano. Esto es inseguro porque cualquiera con acceso al archivo puede leer fácilmente la contraseña.

Almacenamiento Seguro: Genera un "salt" aleatorio para cada contraseña, la hashea utilizando PBKDF2
(una función de derivación de clave segura), y almacena el nombre de usuario, el hash de la contraseña,
y el salt (todos codificados en Base64 para facilitar el almacenamiento) en safe.txt.
Este método es mucho más seguro porque incluso si alguien accede al archivo, no podrá obtener las contraseñas originales sin invertir una cantidad significativa de tiempo y recursos computacionales.

Verificacion de contraseña segura:
Este programa primero lee el nombre de usuario y la contraseña ingresados por el usuario.
Luego, busca en el archivo safe.txt una entrada que coincida con el nombre de usuario ingresado.
Si encuentra una coincidencia, decodifica el salt almacenado, recalcula el hash de la contraseña ingresada usando el mismo salt, y compara el resultado con el hash almacenado. Si ambos hashes coinciden, la contraseña es correcta; de lo contrario, es incorrecta o el usuario no se encuentra en el archivo.

 */
public class storage {

  private static void almacenamientoInseguro(String usuario, String contrasena, String archivo) {
    try (FileWriter writer = new FileWriter(archivo)) {
      writer.write(usuario + ":" + contrasena);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private static void almacenamientoSeguro(String usuario, String contrasena, String archivo) {
    byte[] salt = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    byte[] hash = hashContrasena(contrasena, salt);

    String hashBase64 = Base64.getEncoder().encodeToString(hash);
    String saltBase64 = Base64.getEncoder().encodeToString(salt);

    try (FileWriter writer = new FileWriter(archivo)) {
      writer.write(usuario + ":" + hashBase64 + ":" + saltBase64);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void verificarContrasena(String usuario, String contrasena, String archivo) {
    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
      String linea;
      while ((linea = reader.readLine()) != null) {
        String[] partes = linea.split(":");
        if (partes[0].equals(usuario)) {
          String hashAlmacenado = partes[1];
          String saltAlmacenado = partes[2];

          byte[] salt = Base64.getDecoder().decode(saltAlmacenado);
          byte[] hashCalculado = hashContrasena(contrasena, salt);

          String hashCalculadoBase64 = Base64.getEncoder().encodeToString(hashCalculado);

          if (hashAlmacenado.equals(hashCalculadoBase64)) {
            System.out.println("La contraseña es correcta.");
            return;
          } else {
            System.out.println("Contraseña incorrecta.");
            return;
          }
        }
      }
      System.out.println("Usuario no encontrado.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static byte[] hashContrasena(String contrasena, byte[] salt) {
    try {
      PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, 65536, 128);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      return skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
  public static void main(String[] args) {
    String nombreUsuario = "pepito";
    String contrasena = "clavounclavito";

    System.out.println("nombreUsuario:"+nombreUsuario+" contraseña:"+contrasena);
    // Almacenamiento inseguro en un archivo
    almacenamientoInseguro(nombreUsuario, contrasena, "unsafe.txt");
    System.out.println("unsafe.txt creado de forma insegura");
    // Almacenamiento seguro en otro archivo
    almacenamientoSeguro(nombreUsuario, contrasena, "safe.txt");
    System.out.println("safe.txt creado de forma SEGURA");
    // Verificar la contraseña de forma segura
    verificarContrasena(nombreUsuario, contrasena, "safe.txt");

  }
}
