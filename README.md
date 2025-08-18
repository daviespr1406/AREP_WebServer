# Arep Server
## David Santiago Espinosa Rojas

Un servidor HTTP simple y asíncrono en **Java**, capaz de atender múltiples clientes mediante hilos.  
Soporta peticiones básicas `GET` y `POST`, rutas dinámicas y la entrega de archivos estáticos.

---

## Requisitos

- **Java 17 o superior**
- Sistema operativo compatible con Java (Windows, Linux o macOS).

---

## Estructura del Proyecto

```
HttpServerAsync/
│── src/main/java/com/mycompany/httpserver1/
│     ├── Httpserverasync.java   # Clase principal
│
│── src/main/resources/
│     ├── index.html             # Página por defecto
│     ├── style.css              # Estilos
│     └── images/                # Carpeta de imágenes
```

---

## Compilación y Ejecución

### 1. Compilar
```bash
javac -d src/main/java/com/mycompany/httpserver1 src/main/java/com/mycompany/httpserver1/Httpserverasync.java
```

### 2. Ejecutar
```bash
java -cp src/main/java/com/mycompany/httpserver1 com.mycompany.httpserver1.Httpserverasync
```

El servidor escucha en **puerto 35000**.  

Abrir en el navegador:

```
http://localhost:35000/
```

---

## Rutas Disponibles

### `/`  
Entrega el archivo `index.html` desde `src/main/resources`.

### `/hello?name=Juan`
Devuelve un saludo dinámico.

Ejemplo:
```
http://localhost:35000/hello?name=Juan
```
Respuesta:
```
Hello, Juan!
```

### `/hellopost` (POST)
Ejemplo de manejo de peticiones POST.  
Actualmente responde con un mensaje de confirmación.

### Archivos Estáticos
El servidor sirve archivos de la carpeta `resources`:
```
http://localhost:35000/style.css
http://localhost:35000/images/logo.png
```

---

## Manejo de Tokens en la Petición

El servidor procesa la primera línea de la petición HTTP usando **`StringTokenizer`**:

```java
StringTokenizer tokens = new StringTokenizer(requestLine);
String method = tokens.nextToken();
String rawPath = tokens.nextToken(); 
```

- `method` → tipo de petición (`GET`, `POST`, etc).  
- `rawPath` → ruta solicitada.  

Con esta información se enruta la solicitud a:
- `handleHello`  
- `handleHelloPost`  
- `serveStaticFile`

---

## Ejemplo en Navegador

1. Levantar el servidor.
2. Abrir en el navegador:

```
http://localhost:35000/
http://localhost:35000/hello?name=Santi
http://localhost:35000/style.css
```

---

