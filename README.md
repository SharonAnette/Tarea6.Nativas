# Tarea6Nativas 📱✨
Aplicación Android nativa desarrollada en Kotlin que implementa un sistema de Realidad Aumentada (RA) usando SceneView. Permite detectar planos con la cámara, cargar y proyectar modelos 3D (.glb), e interactuar con ellos mediante gestos multitáctiles (mover, escalar y rotar). 

## 📦 Contenido del Proyecto
MainActivity.kt – Configuración principal de la RA, detección de planos, carga de modelos y lógica de interacción.
activity_main.xml – Layout con la vista principal de RA (ArSceneView).
/assets/Fly.glb – Modelo 3D de ejemplo en formato GLB.

## 🎯 Funcionalidades
Detección de planos en tiempo real usando la cámara del dispositivo.
Carga dinámica de modelos 3D (.glb) desde la carpeta de assets.
Colocación del modelo sobre un plano detectado con un solo toque.

### Interacción completa por gestos:
Arrastrar: Mueve el modelo sobre el plano usando un dedo.
Escalar: Ajusta el tamaño del modelo con gesto de pellizco (pinch).
Rotar: Gira el modelo sobre el eje Y con dos dedos (rotate).
Compatibilidad con dispositivos ARCore: Solo dispositivos soportados pueden ejecutar el modo RA.

## 🗂️ Estructura del Código
1. MainActivity.kt
- Inicializa ArSceneView y gestiona los eventos de tap, pinch y rotate.
- Carga el modelo GLB en respuesta al tap sobre un plano.
- Aplica transformaciones de posición, escala y rotación usando detectores de gestos estándar de Android.

2. activity_main.xml
- Define la vista principal con el widget <io.github.sceneview.ar.scene.ArSceneView />.

3. Carpeta assets/
Aquí debes colocar los archivos .glb que se utilizarán en la app.

## 🛠 Tecnologías Utilizadas
Kotlin: Lenguaje principal de desarrollo.
SceneView (ARSceneView): Motor de renderizado 3D y RA sobre Android.
ARCore: Detección de planos y tracking espacial.
Android Coroutines: Para carga asíncrona de modelos 3D.
Android Studio: IDE recomendado.
Assets 3D: Modelos en formato .glb compatibles con SceneView.

## 🚀 Ejecución y Pruebas
1. Requisitos:
- Dispositivo físico compatible con ARCore (lista oficial).
- Servicios de Google Play para RA instalados.

2. Configuración:
- Clona o descarga el repositorio.
- Abre el proyecto en Android Studio.

3. Ejecución:
- Compila y ejecuta la app en un dispositivo compatible o en un emulador con soporte ARCore (solo para pruebas básicas).
- Otorga permisos de cámara y almacenamiento si se solicitan.
- Toca la pantalla para detectar un plano y colocar el modelo.
- Usa gestos multitáctiles para mover, escalar y rotar el modelo 3D.

## ℹ️ Notas y Consideraciones
El modelo .glb debe estar en la carpeta assets, no en res ni subcarpetas.
Si no ves el modelo, verifica el nombre, la ubicación y que el archivo esté bien exportado (usa gltf-viewer.donmccurdy.com para validarlo).
En dispositivos NO compatibles con ARCore, la app mostrará un mensaje y no funcionará en modo RA.
Para solo ver modelos 3D sin RA, puedes usar SceneView en vez de ArSceneView (requiere modificaciones).

## 🤓 Explicación técnica (resumida)
La app utiliza ARCore para detección de planos y SceneView para renderizado 3D. Los modelos se cargan de assets de forma asíncrona usando corrutinas. Los gestos de interacción se implementan con GestureDetector y ScaleGestureDetector para ofrecer una experiencia natural.
Toda la lógica está centralizada en MainActivity.kt para facilitar el análisis y la experimentación.

## 🚩 Requerimientos para el correcto funcionamiento
Android 8.0 (API 26) o superior.
Dispositivo compatible con ARCore.
Servicios de Google Play para RA instalados y actualizados.

## 📚 Recursos y modelos recomendados
SceneView - Documentación oficial
GLTF/GLB Viewer Online
Modelos 3D de ejemplo

## Link del funcionamiento
https://youtube.com/shorts/HxbqL10q21I?feature=share
