# OWASP Dependency-Check (SCA level)

## Objetivo
- Detectar vulnerabilidades conocidas en dependencias directas y transitivas del proyecto.
- Mantener una línea base de riesgo y planificar remediación incremental.

## Ejecución on-demand (manual)
- Ejecutar escaneo con tests (recomendado para validación completa):
  - `./mvnw -Psecurity-scan test dependency-check:check`
- Si el objetivo no es validar calidad funcional en esa corrida y solo quieres análisis SCA, se pueden saltar los tests así:
  - `./mvnw -Psecurity-scan -DskipTests dependency-check:check`
- Reportes generados:
  - `target/dependency-check-report.html`
  - `target/dependency-check-report.json`

## Lectura rápida del reporte
- Prioriza primero CVSS alto/crítico y dependencias de runtime.
- Distingue directas vs transitivas antes de definir la acción.
- Revisa si la evidencia de CPE/CVE parece falsa positiva antes de decidir suppressions.

## Política de triage recomendada
1. `Critical/High` en runtime: corregir en el siguiente ciclo.
2. `Medium` en runtime o `High` solo test/dev: planificar por sprint.
3. `Low`: registrar y revisar periódicamente.

## Plan de remediación por hallazgo
- Actualizar versión de dependencia si hay parche disponible.
- Sustituir librería si no hay parche o la dependencia está abandonada.
- Aplicar suppression temporal solo con justificación, fecha de expiración y ticket asociado.

## Troubleshooting corto
- Si falla la actualización de feed NVD:
  - `./mvnw -Psecurity-scan dependency-check:purge`
  - Reintentar `dependency-check:check`.
- Si persiste un error de parsing del feed, tratarlo como incidencia externa del proveedor de datos y reintentar más tarde.

## Próximo paso para CI (no implementado en esta fase)
- Crear un job dedicado de seguridad que ejecute:
  - `./mvnw -Psecurity-scan -DskipTests dependency-check:check`
- Publicar `target/dependency-check-report.html` como artefacto del pipeline.
- Empezar en modo report-only y luego definir umbral de bloqueo por CVSS.

## Decisiones aplicadas en remediación posterior a escaneo (16-04-2026)
- Se actualizó `spring-boot-starter-parent` de `4.0.4` a `4.0.5` para resolver transitivas vulnerables desde el BOM padre.
- Se forzó `tomcat.version=11.0.21` como override puntual para cubrir CVEs remanentes en `tomcat-embed-core`.
- Se eliminó `spring-boot-devtools` del `pom.xml` para retirar superficie de riesgo solo-dev y evitar hallazgos no productivos.
- Se validó que la suite de tests siguiera verde antes de aceptar la remediación.

## Checklist operativa recomendada de cada ciclo SCA
1. Ejecutar validación funcional:
   - `./mvnw test`
2. Ejecutar escaneo SCA:
   - `./mvnw -Psecurity-scan -DskipTests dependency-check:check`
3. Revisar reportes:
   - `target/dependency-check-report.html`
   - `target/dependency-check-report.json`
4. Clasificar findings:
   - runtime vs dev-only,
   - directos vs transitivos,
   - severidad CVSS.
5. Definir acción:
   - upgrade por parent/BOM,
   - override puntual,
   - suppression solo con justificación y ticket.
