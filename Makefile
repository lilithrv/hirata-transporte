.PHONY: serve docs pdf clean all

DIR := $(PWD)

# Verificar si Docker está abierto
DOCKER_CHECK := $(shell docker info > /dev/null 2>&1 && echo "ok" || echo "fail")
ifneq ($(DOCKER_CHECK),ok)
$(error "Docker no está corriendo. Abre Docker Desktop en tu Mac e intenta de nuevo.")
endif

# =============================================================================
# WIKI WEB (MkDocs)
# =============================================================================
serve:
	@echo "🚀 Wiki web con live reload en http://localhost:8000 ..."
	@echo "   (Presiona Ctrl+C para detener)"
	docker run --rm -it -p 8000:8000 -v "$(DIR):/docs" squidfunk/mkdocs-material serve -a 0.0.0.0:8000 --livereload

docs:
	@echo "📦 Generando sitio web estático..."
	docker run --rm -v "$(DIR):/docs" squidfunk/mkdocs-material build --clean
	@echo "✅ Sitio listo en ./site/"

# =============================================================================
# PDF (Pandoc - ¡NO MkDocs!)
# =============================================================================
pdf:
	@echo "📄 Generando PDF con Pandoc..."
	@echo "   1. Combinando archivos Markdown..."
	@docker run --rm -v "$(DIR):/workdir" -w /workdir python:3.9-alpine sh -c "pip install --quiet --no-cache-dir pyyaml && python scripts/build_pdf.py"
	
	@echo "   2. Compilando PDF con Pandoc + LaTeX..."
	@docker run --rm -v "$(DIR):/data" pandoc/extra /data/_build/combined.md -o /data/documentacion_tecnica.pdf --from markdown --template /data/templates/eisvogel.latex --toc --number-sections -V geometry:margin=2.5cm -V lang=es -V colorlinks=true -V linkcolor=blue -V fontsize=11pt -V title="Manual Técnico Hirata Transporte" --resource-path=/data/docs/usuario:/data/docs/tecnico:/data/docs
	
	@echo "✅ PDF generado: documentacion_tecnica.pdf"

# =============================================================================
# COMANDOS COMBINADOS
# =============================================================================
all: docs pdf
	@echo "🚀 ¡Wiki estática y PDF generados correctamente!"

clean:
	rm -rf site/ _build/ documentacion_tecnica.pdf
	@echo "🧹 Archivos temporales eliminados"