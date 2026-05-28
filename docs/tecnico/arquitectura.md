# Arquitectura y Stack Tecnológico

Este documento detalla la estructura técnica, los patrones de diseño y las tecnologías utilizadas en el desarrollo del **Sistema de Gestión Hirata Transporte**.

---

## 1. Stack Tecnológico

El sistema ha sido construido utilizando tecnologías estables que garantizan un despliegue rápido en entornos locales y un rendimiento óptimo en la gestión de datos.

| Componente | Tecnología | Versión / Detalle |
| :--- | :--- | :--- |
| **Lenguaje de Programación** | Java | JDK 17 o superior |
| **Entorno de Desarrollo (IDE)** | Apache NetBeans | Gestión de interfaz gráfica mediante Matisse |
| **Base de Datos** | MySQL | Motor relacional para persistencia de datos |
| **Arquitectura Local** | Cliente-Servidor Monolítico | Ejecución directa en equipos de escritorio |

---

## 2. Arquitectura de Software

El sistema implementa un patrón basado en la **Arquitectura en Capas (Layered Architecture)**, lo que permite separar la lógica de presentación de la manipulación de los datos en la base de datos.

```mermaid
graph TD
    A[Capa de Presentación:<br> JFrame / Swing] -->|Envía datos / Peticiones| B[Capa de Lógica / <br>Controladores]
    B -->|Consultas SQL Preparadas| C[(Capa de Datos: <br>MySQL)]
    C -->|Retorna ResultSets / Estado| B
    B -->|Actualiza Interfaz| A
````

---
