#!/usr/bin/env python3
import yaml
import os

def extract_nav_files(nav):
    files = []
    for item in nav:
        if isinstance(item, str):
            files.append(item)
        elif isinstance(item, dict):
            for key, value in item.items():
                if isinstance(value, list):
                    files.extend(extract_nav_files(value))
                else:
                    files.append(value)
    return files

def combine_markdown(files, docs_dir="docs", output_dir="_build"):
    os.makedirs(output_dir, exist_ok=True)
    combined = []
    
    for f in files:
        path = os.path.join(docs_dir, f)
        if os.path.exists(path):
            with open(path, 'r', encoding='utf-8') as md:
                content = md.read()
                # Arregla las rutas de las imágenes para Pandoc
                content = content.replace('](assets/', f']({os.path.abspath(docs_dir)}/assets/')
                combined.append(f"\n\n<!-- {f} -->\n\n")
                combined.append(content)
                combined.append("\n\\newpage\n")
    
    output_path = os.path.join(output_dir, "combined.md")
    with open(output_path, 'w', encoding='utf-8') as out:
        out.write("\n".join(combined))

if __name__ == "__main__":
    with open('mkdocs.yml', 'r', encoding='utf-8') as f:
        config = yaml.load(f, Loader=yaml.BaseLoader)
    
    files = extract_nav_files(config['nav'])
    combine_markdown(files)
