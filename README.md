DESARROLLO
a) Descripción de la problemática
En la actualidad, el almacenamiento de archivos en la nube se ha convertido en una necesidad para muchas personas y familias. Sin embargo, los servicios como Google Drive, OneDrive o Dropbox representan un costo mensual recurrente que, a largo plazo, resulta significativo para hogares de ingresos medios o bajos.
El contexto de esta problemática se presenta en un hogar particular de la ciudad de Colima, donde un vecino decidió dejar de pagar suscripciones de almacenamiento en la nube debido al incremento constante de precios. Actualmente, este usuario almacena sus archivos importantes (documentos personales, fotos familiares, videos, proyectos y respaldos) en diversos dispositivos (computadora, discos duros externos y memorias USB), lo que genera desorganización, riesgo de pérdida de información y dificultad para acceder a sus archivos desde diferentes dispositivos.
La solución actual es manual y poco eficiente: copiar archivos entre dispositivos, utilizar correos electrónicos para enviarse documentos a sí mismo o depender de memorias USB que pueden dañarse o extraviarse. Esto genera pérdida de tiempo, riesgo de pérdida de datos y falta de accesibilidad remota.
Personas afectadas: Principalmente el jefe de familia y otros miembros del hogar que necesitan acceder a los mismos archivos desde sus teléfonos celulares o computadoras.
b) Justificación
Esta problemática puede resolverse eficientemente mediante una solución de software porque existe la posibilidad técnica y económica de crear una Nube Casera (Self-Hosted Cloud) utilizando hardware accesible como una Raspberry Pi 4/5, un disco duro o memoria USB de alta capacidad y software desarrollado específicamente para este propósito.
La implementación de esta plataforma web permitiría:

Centralizar todo el almacenamiento en un solo lugar.
Acceder a los archivos desde cualquier dispositivo con conexión a la red local o remota (vía internet).
Organizar archivos en carpetas y subcarpetas.
Etiquetar archivos para búsquedas avanzadas.
Subir archivos grandes mediante carga en chunks.
Generar vistas previas (previews) de documentos, PDFs e imágenes.
Evitar completamente los costos mensuales de suscripciones.

Beneficios esperados:

Ahorro económico significativo a mediano y largo plazo.
Mayor control y privacidad de los datos (no dependen de empresas externas).
Mejora en la organización y accesibilidad de la información familiar.
Experiencia similar a Google Drive pero de forma local y gratuita.

c) Identificación de involucrados

Cliente: Vecino (propietario del proyecto y quien solicitó el desarrollo). Es quien financia el hardware (Raspberry Pi, disco duro) y define los requerimientos principales.
Usuarios principales: Miembros del hogar (familiares). Usarán el sistema diariamente para subir, descargar, organizar y visualizar archivos.
Involucrados secundarios:
Desarrollador (yo y mi equipo).
Administrador del sistema (el cliente, quien tendrá acceso total).
Posibles futuros usuarios si se escala el proyecto a otros hogares o pequeñas empresas.


d) Necesidades detectadas
Las principales necesidades identificadas son:

Almacenamiento centralizado de archivos de diferentes tipos y tamaños.
Organización jerárquica mediante directorios y subdirectorios.
Sistema de etiquetas para facilitar la búsqueda y clasificación de archivos.
Acceso web desde cualquier dispositivo (computadora, celular o tablet).
Subida de archivos grandes de forma confiable (soporte para carga en chunks).
Vista previa de archivos (imágenes, PDFs, documentos de texto y Excel).
Búsqueda rápida de archivos por nombre o etiquetas.
Seguridad básica y control de acceso.

Nivel de interacción esperado con el sistema:






























UsuarioNivel de interacciónJustificaciónJefe de familia (Cliente)AltaUso diario, administrador del sistemaFamiliaresAltaAcceso frecuente a fotos, documentos y archivosUsuario ocasionalMediaAcceso esporádicoAdministrador técnicoAltaMantenimiento del servidor
