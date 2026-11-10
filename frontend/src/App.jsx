export default function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <a className="brand" href="/" aria-label="TaskFlow, página inicial">
          TaskFlow
        </a>
        <span className="header-caption">Gestão de recursos</span>
      </header>

      <main className="main-content">
        <section className="intro" aria-labelledby="page-title">
          <p className="eyebrow">Alocação acadêmica</p>
          <h1 id="page-title">Organização de recursos</h1>
          <p className="intro-copy">
            Organize salas, professores e materiais sem conflitos de horário.
          </p>
        </section>

        <section className="foundation" aria-label="Estado do projeto">
          <h2>Estrutura inicial</h2>
          <p>
            A base da aplicação está pronta para os fluxos de reservas,
            aprovações e gestão de recursos.
          </p>
        </section>
      </main>
    </div>
  );
}
