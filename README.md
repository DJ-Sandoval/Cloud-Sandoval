# ☁️ NubeSandovalAPI

Sistema de almacenamiento en la nube autoalojado (Self-Hosted Cloud) desarrollado con **Spring Boot**, **React JS**, **MySQL** y desplegado en una **Raspberry Pi**.
El objetivo del proyecto es ofrecer una alternativa local y económica a servicios como Google Drive, Dropbox u OneDrive.

---

# 📌 Descripción del Proyecto

NubeSandovalAPI es una plataforma web que permite almacenar, organizar y gestionar archivos desde cualquier dispositivo conectado a la red local o internet.

La solución está pensada principalmente para hogares o pequeñas organizaciones que desean:

* Evitar pagos mensuales de servicios cloud.
* Tener control total de sus datos.
* Centralizar archivos familiares o personales.
* Acceder remotamente a sus documentos, imágenes y respaldos.

---

# 🚨 Problemática

Actualmente muchas familias utilizan múltiples dispositivos para almacenar información:

* Computadoras
* Discos duros externos
* Memorias USB
* Teléfonos celulares

Esto genera problemas como:

* ❌ Desorganización de archivos.
* ❌ Riesgo de pérdida de información.
* ❌ Dependencia de memorias USB.
* ❌ Dificultad para acceder a archivos remotamente.
* ❌ Costos recurrentes por almacenamiento en la nube.

El caso de estudio se presenta en un hogar de la ciudad de Colima, donde el cliente decidió cancelar suscripciones a servicios cloud debido al incremento constante de precios.

---

# ✅ Justificación

La implementación de una nube casera utilizando una Raspberry Pi representa una solución:

* Económica
* Escalable
* Segura
* Accesible
* Fácil de mantener

La plataforma permitirá:

* Centralizar almacenamiento.
* Acceder remotamente a archivos.
* Organizar carpetas y subcarpetas.
* Etiquetar archivos.
* Buscar archivos rápidamente.
* Subir archivos grandes mediante chunks.
* Generar vistas previas de documentos e imágenes.
* Mantener privacidad total de los datos.

---

# 🎯 Objetivos

## Objetivo General

Desarrollar una plataforma web de almacenamiento en la nube autoalojada que permita administrar archivos de manera segura, accesible y organizada utilizando hardware económico.

## Objetivos Específicos

* Implementar autenticación de usuarios.
* Permitir carga y descarga de archivos.
* Organizar archivos mediante directorios.
* Implementar etiquetas para clasificación.
* Permitir búsquedas rápidas.
* Soportar subida de archivos grandes.
* Desplegar el sistema en una Raspberry Pi.

---

# 👥 Involucrados

| Rol                  | Descripción                            |
| -------------------- | -------------------------------------- |
| Cliente              | Propietario del proyecto y financiador |
| Usuarios principales | Miembros del hogar                     |
| Administrador        | Control total del sistema              |
| Desarrolladores      | Equipo encargado del software          |
| Usuarios futuros     | Posibles clientes futuros              |

---

# 🧩 Necesidades Detectadas

* Almacenamiento centralizado.
* Acceso remoto desde navegador.
* Organización jerárquica de carpetas.
* Gestión de etiquetas.
* Vista previa de archivos.
* Seguridad y autenticación.
* Búsqueda avanzada.
* Carga de archivos grandes.

---

# 🛠️ Tecnologías Utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate

## Frontend

* React JS
* Axios
* React Router

## Base de Datos

* MySQL

## Infraestructura

* Raspberry Pi 3/4/5
* Linux
* Nginx
* Node.js

## Almacenamiento

* Tarjeta SD
* Disco duro externo
* USB

---

# 🏗️ Arquitectura del Sistema

## Flujo General

1. El cliente realiza peticiones HTTPS desde React.
2. Spring Boot procesa la lógica de negocio.
3. MySQL almacena los metadatos.
4. Los archivos físicos se almacenan en la SD/disco conectado a Raspberry Pi.
5. El sistema puede accederse desde red local o internet.

---

# 🗂️ Modelo Entidad-Relación

## Entidades Principales

### Usuarios

Gestiona autenticación y propiedad de archivos/directorios.

### Directorios

Permite organización jerárquica mediante subdirectorios.

### Archivos

Almacena metadatos y referencias físicas de archivos.

### Etiquetas

Clasificación avanzada de archivos.

### Archivo_Etiqueta

Relación N:M entre archivos y etiquetas.

---

# 🔐 Funcionalidades Principales

## 👤 Usuarios

* Registro
* Login
* JWT Authentication

## 📁 Directorios

* Crear carpetas
* Subdirectorios
* Navegación jerárquica

## 📄 Archivos

* Subida de archivos
* Descarga
* Eliminación lógica
* Metadata
* Hash único

## 🏷️ Etiquetas

* Crear etiquetas
* Asociar archivos
* Filtrado avanzado

## 🔎 Búsquedas

* Por nombre
* Por etiquetas

## 🖼️ Vista Previa

* Imágenes
* PDFs
* Documentos

## 📦 Carga en Chunks

* Subida confiable de archivos grandes.

---

# 🔒 Seguridad

* Autenticación JWT.
* Contraseñas encriptadas.
* Validaciones backend.
* Control de acceso por usuario.
* Eliminación lógica de archivos.

---

# 📦 Estructura General del Proyecto

```bash
NubeSandovalAPI/
│
├── backend/
│   ├── src/
│   ├── controllers/
│   ├── services/
│   ├── repositories/
│   └── entities/
│
├── frontend/
│   ├── components/
│   ├── pages/
│   ├── services/
│   └── routes/
│
├── storage/
│   ├── uploads/
│   └── previews/
│
└── database/
```

---

# 📡 Despliegue

El sistema está pensado para desplegarse directamente en una Raspberry Pi conectada vía Ethernet al router.

## Servicios ejecutándose en Raspberry Pi

* Spring Boot API
* MySQL
* Nginx
* Node.js
* Frontend React

---

# 💡 Beneficios Esperados

* 💰 Ahorro económico.
* 🔒 Mayor privacidad.
* 📁 Mejor organización.
* 🌎 Acceso remoto.
* ☁️ Experiencia similar a Google Drive.
* 🖥️ Control total del almacenamiento.

---

# 🚀 Futuras Mejoras

* Compartir archivos mediante enlaces.
* Sincronización automática.
* Aplicación móvil.
* Integración con Docker.
* Kubernetes.
* Backup automático.
* Notificaciones en tiempo real.
* Versionado de archivos.

---

# 👨‍💻 Autor

**Jose Armando Sandoval Santana**
Ingeniería en Sistemas
Backend Developer — Spring Boot & Arquitectura de Software

---

# 📄 Licencia

Proyecto académico y de uso personal/familiar.
