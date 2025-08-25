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
│     ├── WebAplication.java   # Clase principal
│     ├── Httpserverasync.java   # Clase servidor
│     ├── Service.java   # servicios
│     ├── httpRequest.java   # Peticiones
│     └── httpResponse.java  # Respuestas
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
mvn clean compile
```

### 2. Ejecutar
```bash
java -cp target/classes com.mycompany.httpserver1.WebApplication
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
### `/Index`
Devuelve index.html

Ejemplo:
```
http://localhost:35000/index
```
Respuesta:
```
index.html
```
### Archivos Estáticos
El servidor sirve archivos de la carpeta `resources`:
```
http://localhost:35000/style.css
http://localhost:35000/images/logo.png
```

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

