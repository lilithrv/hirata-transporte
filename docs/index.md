# 🚛 Sistema de Gestión Hirata Transporte

Bienvenido al portal de documentación unificado de la Empresa de Transporte "Hirata". Este sistema automatiza tanto el control operativo de la flota de camiones como la gestión informática de las oficinas centrales.

## 🎯 Objetivo del Proyecto
El software resuelve la falta de automatización en el monitoreo de kilometraje de los camiones (alertas a los 5,000 km) y centraliza el control de mantenimiento de los equipos de cómputo, actualizaciones de software e inventario de repuestos de la empresa.

---

## 🗺️ Mapa de Documentación

Esta Wiki está organizada según la audiencia y el rol que desempeñes en la empresa. Selecciona una sección para comenzar:

### 👥 Área de Desarrollo e IT
Contiene las guías de arquitectura, modelo de datos relacional en MySQL y especificaciones técnicas del código fuente en Java Swing.
*   **[Manual Técnico - Arquitectura y Stack](tecnico/arquitectura.md):** Estructura en capas del software y uso de consultas seguras con `PreparedStatement`.
*   **[Manual Técnico - Modelo de Base de Datos](tecnico/base-datos.md):** Estructura de las tablas en MySQL (`transporte_hirata`) y diccionario de datos.

### 🔧 Área Operativa y Técnicos
Manuales de usuario paso a paso y flujos de trabajo de la mesa de ayuda para el registro de mantenciones y control de stock físico.
*   **[Manual de Usuario - Registro de Mantenimiento](usuario/mantenimiento.md):** Cómo gestionar y cerrar órdenes de trabajo de los equipos.
*   **[Manual de Usuario - Control de Inventario](usuario/inventario.md):** Flujo para dar de alta repuestos, editar existencias y reaccionar ante alertas de stock bajo.