# Cloud-Sandoval

HomeCloud FS — Tu Nube Privada, Inteligente y Sin Límites
HomeCloud FS es una solución de software que convierte una computadora doméstica con almacenamiento externo en una nube privada de alto rendimiento. Está diseñada para usuarios que manejan grandes volúmenes de datos personales —fotos, documentos, proyectos— y buscan una alternativa gratuita, segura y sin restricciones a los servicios comerciales de suscripción mensual.

Esta API REST es el núcleo de gestión de HomeCloud FS. Administra de forma inteligente el sistema de archivos y metadatos, exponiendo toda la potencia del motor de búsqueda, etiquetado y control de versiones.

🧠 Más que un sistema de archivos
A diferencia de un simple share de red, HomeCloud FS implementa una arquitectura de dos capas:

Disco físico: para el almacenamiento binario puro de los archivos.

Base de datos relacional: para la gestión inteligente de metadatos, que habilita búsquedas avanzadas, etiquetado personalizado, control de versiones de archivos y una experiencia web interactiva.

🔐 Acceso Remoto Zero-Trust
El acceso remoto seguro se garantiza mediante una red overlay Zero-Trust (Tailscale), eliminando por completo la necesidad de abrir puertos en el router o configurar complejas VPNs. Tus datos viajan cifrados y solo son accesibles para ti y las personas que autorices.

🎯 El problema que resuelve
Saturación de almacenamiento local no gestionado: discos externos llenos de datos sin clasificar, duplicados o inaccesibles.

Dependencia de suscripciones costosas: librarte de las cuotas mensuales y límites artificiales de almacenamiento.

Pérdida de control sobre tus datos: con HomeCloud FS, eres el único propietario y administrador de tu información.

Con HomeCloud FS, el usuario recupera el control absoluto de sus datos, combinando la potencia de búsqueda de un gestor documental con la ubicuidad de una nube privada.

🧩 Para la sección "About" del repo (opcional, queda muy limpio)
REST API to manage a private cloud · Two-layer architecture (binary + metadata) · Advanced search, tagging & versioning · Zero-Trust remote access (Tailscale) · Free & self-hosted alternative to commercial clouds.


