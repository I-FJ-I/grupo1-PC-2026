// Genera el sitio estático de la memoria del curso.
//
//   node docs/generate.mjs
//
// Lee todos los .md de docs/, los convierte a HTML en docs/site/ y crea un
// index.html que enlaza a todos. Copia también las carpetas de recursos
// (capturas/, B3/) para que las imágenes y los enlaces relativos funcionen.

import { readdir, readFile, writeFile, mkdir, cp, rm } from 'node:fs/promises';
import path from 'node:path';
import { marked } from 'marked';
import { gfmHeadingId } from 'marked-gfm-heading-id';

const DOCS = path.resolve('docs');
const SITE = path.join(DOCS, 'site');

marked.use(gfmHeadingId(), { gfm: true });

const FRONT_MATTER = /^---\r?\n([\s\S]*?)---\r?\n/;

function splitFrontMatter(source) {
  const match = source.match(FRONT_MATTER);
  if (!match) return { data: {}, body: source };

  const data = {};
  for (const line of match[1].split(/\r?\n/)) {
    const separator = line.indexOf(':');
    if (separator === -1) continue;
    const key = line.slice(0, separator).trim();
    const value = line
      .slice(separator + 1)
      .trim()
      .replace(/^["']|["']$/g, '');
    if (key) data[key] = value;
  }
  return { data, body: source.slice(match[0].length) };
}

function extractTitle(data, body, fileName) {
  if (data.title) return data.title;
  const heading = body.match(/^#\s+(.+)$/m);
  if (heading) return heading[1].trim();
  return fileName.replace(/\.md$/i, '');
}

// Un enlace a otro boletín escrito como .md debe apuntar al .html generado.
function rewriteInternalLinks(html) {
  return html.replace(/href="([^"]+?)\.md(?=#|")/g, 'href="$1.html"');
}

function escapeHtml(text) {
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

const STYLES = `
  :root { color-scheme: light dark; }
  * { box-sizing: border-box; }
  body {
    margin: 0;
    font-family: -apple-system, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    line-height: 1.6;
    color: #1f2328;
    background: #ffffff;
  }
  header {
    border-bottom: 1px solid #d0d7de;
    padding: 0.9rem 1.5rem;
    background: #f6f8fa;
  }
  header a { color: #1f2328; font-weight: 600; text-decoration: none; }
  main { max-width: 60rem; margin: 0 auto; padding: 2rem 1.5rem 4rem; }
  h1, h2 { border-bottom: 1px solid #d0d7de; padding-bottom: 0.3rem; }
  h1 { font-size: 2rem; }
  h2 { font-size: 1.5rem; margin-top: 2rem; }
  h3 { font-size: 1.2rem; margin-top: 1.5rem; }
  a { color: #0969da; }
  img { max-width: 100%; height: auto; border: 1px solid #d0d7de; border-radius: 6px; }
  code {
    font-family: ui-monospace, SFMono-Regular, Consolas, "Liberation Mono", monospace;
    font-size: 0.9em;
    background: #f6f8fa;
    padding: 0.15em 0.35em;
    border-radius: 4px;
  }
  pre {
    background: #f6f8fa;
    border: 1px solid #d0d7de;
    border-radius: 6px;
    padding: 1rem;
    overflow-x: auto;
  }
  pre code { background: none; padding: 0; }
  table { border-collapse: collapse; width: 100%; margin: 1rem 0; display: block; overflow-x: auto; }
  th, td { border: 1px solid #d0d7de; padding: 0.4rem 0.7rem; text-align: left; }
  th { background: #f6f8fa; }
  blockquote {
    margin: 1rem 0;
    padding: 0.5rem 1rem;
    border-left: 4px solid #d0d7de;
    color: #57606a;
  }
  .back { margin-top: 0; }
  .index li { margin-bottom: 0.6rem; }
  @media (prefers-color-scheme: dark) {
    body { background: #0d1117; color: #e6edf3; }
    header { background: #161b22; border-color: #30363d; }
    header a { color: #e6edf3; }
    h1, h2, blockquote { border-color: #30363d; }
    a { color: #4493f8; }
    code, pre, th { background: #161b22; }
    th, td, pre, img { border-color: #30363d; }
    blockquote { color: #9198a1; }
  }
`;

function layout({ title, content, withBackLink }) {
  const backLink = withBackLink ? '<p class="back"><a href="index.html">← Índice</a></p>' : '';
  return `<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${escapeHtml(title)}</title>
<style>${STYLES}</style>
</head>
<body>
<header><a href="index.html">Prácticas Continuas · Task API</a></header>
<main>
${backLink}
${content}
</main>
</body>
</html>
`;
}

function sortKey(fileName) {
  const number = fileName.match(/(\d+)/);
  return number ? Number(number[1]) : Number.MAX_SAFE_INTEGER;
}

async function main() {
  const entries = await readdir(DOCS, { withFileTypes: true });
  const markdownFiles = entries
    .filter((entry) => entry.isFile() && entry.name.toLowerCase().endsWith('.md'))
    .map((entry) => entry.name)
    .sort((a, b) => sortKey(a) - sortKey(b) || a.localeCompare(b));

  if (markdownFiles.length === 0) {
    throw new Error('No se ha encontrado ningún .md en docs/');
  }

  await rm(SITE, { recursive: true, force: true });
  await mkdir(SITE, { recursive: true });

  const pages = [];

  for (const fileName of markdownFiles) {
    const raw = await readFile(path.join(DOCS, fileName), 'utf8');
    const { data, body } = splitFrontMatter(raw);
    const title = extractTitle(data, body, fileName);
    const html = rewriteInternalLinks(marked.parse(body));
    const outputName = `${fileName.replace(/\.md$/i, '')}.html`;

    await writeFile(
      path.join(SITE, outputName),
      layout({ title, content: html, withBackLink: true }),
      'utf8',
    );

    pages.push({ title, outputName });
    console.log(`  ${fileName} -> site/${outputName}`);
  }

  const items = pages
    .map(({ title, outputName }) => `      <li><a href="${outputName}">${escapeHtml(title)}</a></li>`)
    .join('\n');

  const indexHtml = layout({
    title: 'Memoria de prácticas · Task API',
    withBackLink: false,
    content: `<h1>Memoria de prácticas</h1>
    <p>Documentación de las sesiones del curso, una por entrega.</p>
    <ul class="index">
${items}
    </ul>`,
  });

  await writeFile(path.join(SITE, 'index.html'), indexHtml, 'utf8');
  console.log(`  index.html -> ${pages.length} páginas`);

  // Recursos referenciados desde los .md (capturas/, B3/...) para que las rutas
  // relativas sigan siendo válidas dentro de docs/site/.
  const ASSET_EXTENSIONS = new Set([
    '.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.pdf', '.txt', '.zip', '.jar',
  ]);

  for (const entry of entries) {
    const extension = path.extname(entry.name).toLowerCase();
    const isAsset = entry.isDirectory() || ASSET_EXTENSIONS.has(extension);
    if (entry.name === 'site' || !isAsset) continue;
    await cp(path.join(DOCS, entry.name), path.join(SITE, entry.name), { recursive: true });
    console.log(`  recurso copiado: ${entry.name}`);
  }

  console.log('\nSitio generado en docs/site/');
}

main().catch((error) => {
  console.error(error.message);
  process.exit(1);
});
