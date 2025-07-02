# Red de Mensajería en Java

Este proyecto es un ejercicio práctico diseñado para aprender y aplicar conceptos fundamentales de Java, incluyendo clases, interfaces, excepciones, genéricos y programación concurrente. La implementación incluye una red de dispositivos que envían y reciben mensajes a través de un servidor de mensajes.

## Descarga del ejercicio

Para mayor facilidad de gestión, antes de empezar a trabajar, haz un fork de este repositorio y trabaja desde allí.
Una vez acabes, harás un PR de tu fork a este repositorio.

## 📚 Objetivos del Proyecto

1. **Clases y Primitivos**: Crear clases como `Message` y `Device` para modelar la lógica básica.
2. **Flujo de Control y Arreglos**: Implementar validaciones y lógica de historial de mensajes.
3. **System y Referencia vs. Valor**: Usar métodos de la clase `System` y demostrar el comportamiento de paso por referencia.
4. **Interfaces y Genéricos**: Implementar una interfaz `Exportable` y métodos genéricos para filtrar mensajes.
5. **Excepciones Personalizadas**: Crear y manejar excepciones como `InvalidMessageException`.
6. **Threads y Concurrencia**: Implementar un servidor de mensajes que utiliza hilos y sincronización.

## 🛠️ Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes y clases:

```
src/main/java/com/aroldev/messagingnetwork/
├── Device.java          - Abstract base class for devices
├── Phone.java           - Concrete device implementation  
├── Message.java         - Message data structure
├── MessageServer.java   - Thread-safe message server
├── Main.java            - Demonstration code
├── Enum/MessageType.java
├── Interfaces/Exportable.java
└── Exceptions/InvalidMessageException.java
```

## ⚙️ Configuración del Entorno

Asegúrate de tener instalado:

- **Java JDK 21** o superior.
- **Maven 3.9.10** o superior.


---

## 🎯 Tarea

### 1. Clases, Primitivos y Wrappers

- **Implementar la clase `Message`**: 
  - Debe tener los atributos `content` (texto), `timestamp` (un long), `type` (un `enum` con al menos `TEXT` y `SYSTEM`, crea el enum en el package `Enum/MessageType`)
  - Implementa los getters y setters
  - Implementa un metodo `formatDate` que retorna un String con la fecha del mensaje en el siguiente formato: `2021-02-12 12:44:08`.
- **Define una clase abstracta `Device`**:  
  - Debe tener un nombre, un ID (`int`)
  - Implementa los getters y setters correpondientes.
  - Define los métodos abstractos `sendMessage(Message msg)` y `receiveMessage(Message msg)`.

### 2. Flujo de control y arreglos

- **Implementa la clase concreta `Phone`**:  
  - Debe extender `Device`
  - En el constructor, acepta un parametro más `capacity`, un `int`.
  - Crea un historial de mensajes con una array.
  - Implementa el método `sendMessage`. Este debe pintar el mensaje por pantalla y añadirlo al historial de mensajes. Los mensajes se guardan de forma cíclica: cuando ya no tiene más espacio se usa el espacio del primero recibido.
  - Implementa un método `getMessages` que retorna el array de mensajes.
- **Validaciones en `receiveMessage`**:  
  - No se deben almacenar mensajes vacíos (contenido nulo, vacío o solo espacios).
  - Si el tipo es `SYSTEM`, solo se almacena si el remitente es exactamente `"SYSTEM"`. Si viene de otro dispositivo, se ignora.

### 3. Interfaces y genéricos

- **Crea una interfaz `Exportable`**:  
  - Define un método `export`.
  - Esta clase debe definir un tipo genérico. Por ejemplo, si la instancio con un `Exportable<String>` el `export` me debe retornar un String.
- **Implementación en `Phone`**:
  - Haz que la clase `Phone` implemente `Exportable`. 
  - El método `export()` debe devolver el historial de mensajes como un `List<String>`.
- **Método genérico**:  
  Implementa un método que filtre mensajes de tipo `TEXT` usando genéricos.

### 4. Excepciones personalizadas

- **Excepción `InvalidMessageException`**:  
  Lanza esta excepción si el contenido del mensaje es nulo o demasiado corto. Maneja la excepción al enviar mensajes y muestra el error con `System.err`.

### 5. Hilos y concurrencia

- **Clase `MessageServer`**:  
  Implementa `Runnable`, usa una cola compartida (`Queue<Message>`) y en el método `run()` procesa los mensajes con `Thread.sleep(1000)`. Usa `synchronized` para gestionar el acceso concurrente.


## 🚢 Entrega

- Cuando hayas acabado, haz un pull requests con los cambios para que un instructor lo pueda revisar.
- Títula este PR con tu `<nombre.apellido>`.

## 📝 Notas

- Este proyecto es un ejercicio educativo. No se recomienda su uso en producción.
- Contribuciones y mejoras son bienvenidas. Por favor, abre un **issue** o **pull request** en GitHub.