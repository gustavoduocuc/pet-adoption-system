# Pruebas y cobertura

Este proyecto usa JaCoCo para medir la cobertura de pruebas.

## Ejecutar pruebas y generar reporte

```bash
./mvnw clean verify
```

Tras ejecutarlo, abre el reporte en:

```
target/site/jacoco/index.html
```

## Aplicar el umbral de cobertura (60%)

Para que el build falle si no se alcanza el mínimo de cobertura:

```bash
./mvnw -Pcoverage-gate clean verify
```

Este perfil valida que las siguientes métricas cumplan con al menos **60%**:

- Líneas (LINE)
- Instrucciones (INSTRUCTION)
- Ramas (BRANCH)
- Complejidad (COMPLEXITY)
- Métodos (METHOD)
- Clases (CLASS)
