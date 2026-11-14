import { readdir, readFile, writeFile } from "node:fs/promises";
import { relative } from "node:path";
import { fileURLToPath } from "node:url";

const root = new URL("../docs/testes-bdd/", import.meta.url);
const output = new URL("../docs/testes-bdd/relatorio-cobertura.md", import.meta.url);
const featureFiles = (await readdir(root))
  .filter((name) => name.endsWith(".feature"))
  .sort();

const functional = new Map(Array.from({ length: 14 }, (_, i) => [`RF-${String(i + 1).padStart(2, "0")}`, 0]));
const nonFunctional = new Map(Array.from({ length: 22 }, (_, i) => [`RNF-${String(i + 1).padStart(2, "0")}`, 0]));
const featureRows = [];
let totalScenarios = 0;
let executableScenarios = 0;

for (const filename of featureFiles) {
  const content = await readFile(new URL(filename, root), "utf8");
  let pendingTags = [];
  let featureTags = [];
  let scenarioCount = 0;
  let executableCount = 0;

  for (const rawLine of content.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (line.startsWith("@")) {
      pendingTags.push(...line.match(/@[A-Za-z0-9_-]+/g));
      continue;
    }

    if (/^(Funcionalidade|Feature):/.test(line)) {
      featureTags = [...pendingTags];
      pendingTags = [];
      continue;
    }

    if (/^(Cenário|Esquema do Cenário|Scenario|Scenario Outline):/.test(line)) {
      const tags = [...featureTags, ...pendingTags];
      pendingTags = [];
      scenarioCount++;
      totalScenarios++;
      if (tags.includes("@executavel")) {
        executableCount++;
        executableScenarios++;
      }

      for (const tag of tags) {
        if (functional.has(tag.slice(1))) functional.set(tag.slice(1), functional.get(tag.slice(1)) + 1);
        if (nonFunctional.has(tag.slice(1))) nonFunctional.set(tag.slice(1), nonFunctional.get(tag.slice(1)) + 1);
      }
      continue;
    }

    if (line && !line.startsWith("#")) pendingTags = [];
  }

  featureRows.push(`| \`${filename}\` | ${scenarioCount} | ${executableCount} |`);
}

const coveredFunctional = [...functional.values()].filter((count) => count > 0).length;
const coveredNonFunctional = [...nonFunctional.values()].filter((count) => count > 0).length;
const tableRows = (map) => [...map]
  .map(([id, count]) => `| ${id} | ${count > 0 ? "Rastreado" : "Sem cenário"} | ${count} |`)
  .join("\n");

const report = `# Relatório de cobertura BDD

Relatório gerado automaticamente por \`scripts/report-bdd-coverage.mjs\`, contando cenários e tags de rastreabilidade nos arquivos Gherkin.

## Resumo

- Arquivos Gherkin: **${featureFiles.length}**
- Cenários/esquemas especificados: **${totalScenarios}** (esquemas contados por declaração, sem expandir linhas de Exemplos)
- Cenários marcados \`@executavel\`: **${executableScenarios}/${totalScenarios}**
- RFs rastreados por pelo menos um cenário: **${coveredFunctional}/14 (${Math.round((coveredFunctional / 14) * 100)}%)**
- RNFs rastreados por pelo menos um cenário: **${coveredNonFunctional}/22 (${Math.round((coveredNonFunctional / 22) * 100)}%)**
- Cobertura de código: **não medida por este relatório**. A cobertura de tags Gherkin não representa cobertura de código nem prova que regras não implementadas funcionam.

## Cenários por arquivo

| Arquivo | Cenários especificados | Executáveis |
| --- | ---: | ---: |
${featureRows.join("\n")}

## Rastreabilidade de requisitos funcionais

| Requisito | Cobertura documental | Cenários etiquetados |
| --- | --- | ---: |
${tableRows(functional)}

## Rastreabilidade de requisitos não funcionais

| Requisito | Cobertura documental | Cenários etiquetados |
| --- | --- | ---: |
${tableRows(nonFunctional)}

## Limites e próximos critérios de cobertura

- A suíte Cucumber executa somente cenários marcados \`@executavel\`; os demais cenários ainda não estão conectados a passos BDD executáveis. Isso não determina, por si só, se a funcionalidade correspondente existe no código.
- Os testes do PostgreSQL Testcontainers verificam a infraestrutura de teste e conectividade SQL. Não representam cobertura de persistência de negócio.
- O ensaio exploratório local de 2.000 usuários, limitado a GET de disponibilidade na API com H2, está documentado em \`docs/performance/resultado-01.md\`; não substitui o teste de aceitação definido no plano. Testes em navegador, viewports e métricas ponta a ponta requerem automação e ambiente controlado.
- Para elevar a cobertura executável, conectar os cenários a passos Cucumber ligados aos casos de uso e medir cobertura de código separadamente.
`;

await writeFile(output, report, "utf8");
console.log(`BDD coverage report generated: ${relative(process.cwd(), fileURLToPath(output))}`);
console.log(`Scenarios: ${totalScenarios}; executable: ${executableScenarios}; RF: ${coveredFunctional}/14; RNF: ${coveredNonFunctional}/22`);

if (coveredFunctional !== functional.size || coveredNonFunctional !== nonFunctional.size) {
  console.error("One or more RF/RNF identifiers have no tagged scenario.");
  process.exitCode = 1;
}
