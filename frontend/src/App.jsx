import { useMemo, useState } from "react";

const initialResources = [
  { id: 1, name: "Sala Inovação", type: "Sala", location: "Bloco A · 2º andar", capacity: 32, detail: "Projetor · videoconferência", available: true, restricted: false },
  { id: 2, name: "Laboratório 03", type: "Sala", location: "Bloco B · térreo", capacity: 24, detail: "Computadores · laboratório", available: true, restricted: true },
  { id: 3, name: "Mariana Costa", type: "Professor", location: "Departamento de Design", capacity: null, detail: "Design de produto · UX", available: true, restricted: false },
  { id: 4, name: "Projetor portátil", type: "Equipamento", location: "Central de materiais", capacity: null, detail: "4 unidades disponíveis", available: true, restricted: false },
  { id: 5, name: "Kit Arduino", type: "Material", location: "Laboratório · Bloco B", capacity: null, detail: "12 unidades disponíveis", available: false, restricted: false },
  { id: 6, name: "Auditório Central", type: "Sala", location: "Bloco C · térreo", capacity: 120, detail: "Áudio · palco · projetor", available: true, restricted: true },
];

const initialReservations = [
  { id: 1042, title: "Workshop de prototipagem", date: "Hoje", time: "09:00 — 11:00", resource: "Sala Inovação", status: "Aprovada", owner: "Você" },
  { id: 1043, title: "Aula de experiência do usuário", date: "Hoje", time: "14:00 — 16:00", resource: "Laboratório 03", status: "Aguardando aprovação", owner: "Você" },
  { id: 1044, title: "Reunião de planejamento", date: "Amanhã", time: "10:00 — 11:30", resource: "Mariana Costa", status: "Aprovada", owner: "Você" },
];

const initialApprovals = [
  { id: 1048, title: "Palestra de inovação", requestedBy: "Rafael Nunes", resource: "Auditório Central", date: "18 nov · 14:00 — 17:00" },
  { id: 1049, title: "Aula prática de prototipagem", requestedBy: "Luiza Martins", resource: "Laboratório 03", date: "19 nov · 09:00 — 12:00" },
];

const navigation = [
  { id: "dashboard", label: "Visão geral", icon: "⌂" },
  { id: "resources", label: "Recursos", icon: "▦" },
  { id: "reservations", label: "Reservas", icon: "▤" },
  { id: "management", label: "Gestão", icon: "⚙" },
];

function Icon({ children }) {
  return <span className="nav-icon" aria-hidden="true">{children}</span>;
}

function StatusBadge({ children }) {
  const tone = children === "Aprovada" || children === "Disponível"
    ? "success"
    : children === "Aguardando aprovação"
      ? "warning"
      : children === "Cancelada" || children === "Em manutenção"
        ? "muted"
        : "info";
  return <span className={`status-badge ${tone}`}>{children}</span>;
}

function PageHeading({ eyebrow, title, description, action }) {
  return (
    <div className="page-heading">
      <div>
        <p className="eyebrow">{eyebrow}</p>
        <h1>{title}</h1>
        <p className="page-description">{description}</p>
      </div>
      {action}
    </div>
  );
}

function LoginScreen({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("Solicitante");
  const [error, setError] = useState("");

  function submit(event) {
    event.preventDefault();
    if (!email.trim() || !password) {
      setError("Informe seu e-mail e sua senha para continuar.");
      return;
    }
    onLogin({ name: email.split("@")[0].replace(/[._-]/g, " "), email, role });
  }

  return (
    <main className="login-page">
      <section className="login-story">
        <a className="brand login-brand" href="#inicio" aria-label="TaskFlow, início">
          <span className="brand-mark">T</span> taskflow
        </a>
        <div className="login-message">
          <p className="eyebrow light">Seu campus, em sintonia</p>
          <h1>Espaços para boas ideias acontecerem.</h1>
          <p>Encontre recursos, organize encontros e acompanhe cada reserva em um só lugar.</p>
          <div className="login-decoration" aria-hidden="true">
            <div className="decoration-card decoration-one"><span>◷</span><strong>Agenda organizada</strong><small>Sem conflitos de horário</small></div>
            <div className="decoration-card decoration-two"><span>✓</span><strong>Tudo conectado</strong><small>Salas, pessoas e materiais</small></div>
          </div>
        </div>
        <p className="login-footer">TaskFlow · Gestão acadêmica de recursos</p>
      </section>
      <section className="login-panel">
        <div className="login-form-wrap">
          <span className="mobile-login-brand"><span className="brand-mark">T</span> taskflow</span>
          <p className="eyebrow">Bem-vindo de volta</p>
          <h2>Acesse sua conta</h2>
          <p className="muted-copy">Entre para continuar organizando seu dia.</p>
          <form onSubmit={submit} className="login-form">
            <label htmlFor="login-email">E-mail</label>
            <input id="login-email" type="email" placeholder="voce@instituicao.edu.br" value={email} onChange={(event) => setEmail(event.target.value)} />
            <div className="label-row"><label htmlFor="login-password">Senha</label><a href="#ajuda" onClick={(event) => event.preventDefault()}>Esqueceu?</a></div>
            <input id="login-password" type="password" placeholder="Sua senha" value={password} onChange={(event) => setPassword(event.target.value)} />
            <label htmlFor="login-role">Acessar como</label>
            <select id="login-role" value={role} onChange={(event) => setRole(event.target.value)}>
              <option>Solicitante</option>
              <option>Responsável</option>
              <option>Administrador</option>
            </select>
            {error && <p className="form-error" role="alert">{error}</p>}
            <button className="button primary full-width" type="submit">Entrar na plataforma <span aria-hidden="true">→</span></button>
          </form>
          <p className="prototype-note"><span aria-hidden="true">ⓘ</span> Protótipo visual: o acesso não valida credenciais no servidor.</p>
        </div>
      </section>
    </main>
  );
}

function Dashboard({ user, reservations, approvals, onNavigate }) {
  const greetingName = user.name.split(" ")[0] || user.role;
  return (
    <>
      <PageHeading
        eyebrow="Quarta-feira, 13 de novembro"
        title={`Bom dia, ${greetingName}!`}
        description="Aqui está um resumo do que acontece no seu espaço hoje."
        action={<button className="button primary" onClick={() => onNavigate("reservations")}>＋ Nova reserva</button>}
      />
      <section className="stat-grid" aria-label="Resumo">
        <article className="stat-card"><span className="stat-icon violet">▤</span><span className="stat-label">Reservas este mês</span><strong>12</strong><small className="trend positive">↑ 2 em relação ao mês passado</small></article>
        <article className="stat-card"><span className="stat-icon green">◷</span><span className="stat-label">Horas alocadas</span><strong>28,5 <small>h</small></strong><small>Em 4 recursos diferentes</small></article>
        <article className="stat-card"><span className="stat-icon amber">⌛</span><span className="stat-label">Aguardando aprovação</span><strong>{approvals.length}</strong><small>Solicitações pendentes</small></article>
        <article className="stat-card"><span className="stat-icon blue">▦</span><span className="stat-label">Recursos disponíveis</span><strong>18 <small>/ 24</small></strong><small>Para o período selecionado</small></article>
      </section>
      <div className="dashboard-grid">
        <section className="panel schedule-panel">
          <div className="panel-heading"><div><h2>Sua agenda</h2><p>Próximos compromissos</p></div><button className="text-button" onClick={() => onNavigate("reservations")}>Ver todas <span aria-hidden="true">→</span></button></div>
          <div className="schedule-list">
            {reservations.slice(0, 3).map((reservation, index) => (
              <article className="schedule-item" key={reservation.id}>
                <div className={`schedule-date ${index === 0 ? "today" : ""}`}><strong>{index === 0 ? "13" : index === 1 ? "13" : "14"}</strong><span>{index < 2 ? "NOV" : "NOV"}</span></div>
                <div className="schedule-details"><strong>{reservation.title}</strong><span>{reservation.time} · {reservation.resource}</span></div>
                <StatusBadge>{reservation.status}</StatusBadge>
              </article>
            ))}
          </div>
          <button className="button secondary full-width calendar-button" onClick={() => onNavigate("reservations")}>Abrir calendário completo <span aria-hidden="true">→</span></button>
        </section>
        <section className="panel quick-panel">
          <div className="panel-heading"><div><h2>Acesso rápido</h2><p>O que você precisa fazer?</p></div></div>
          <div className="quick-actions">
            <button onClick={() => onNavigate("resources")}><span className="quick-icon lavender">⌕</span><span><strong>Encontrar um espaço</strong><small>Pesquise salas e equipamentos</small></span><b>→</b></button>
            <button onClick={() => onNavigate("reservations")}><span className="quick-icon mint">＋</span><span><strong>Fazer uma reserva</strong><small>Organize sua próxima atividade</small></span><b>→</b></button>
            <button onClick={() => onNavigate("management")}><span className="quick-icon peach">▣</span><span><strong>Ver minhas solicitações</strong><small>Acompanhe aprovações e status</small></span><b>→</b></button>
          </div>
          <div className="tip-card"><span aria-hidden="true">✦</span><div><strong>Dica do dia</strong><p>Reserve com antecedência para ter mais opções de horários e espaços.</p></div></div>
        </section>
      </div>
    </>
  );
}

function ResourcesScreen({ resources, onReserve }) {
  const [term, setTerm] = useState("");
  const [type, setType] = useState("Todos os tipos");
  const [availability, setAvailability] = useState("Qualquer disponibilidade");
  const filtered = resources.filter((resource) => {
    const matchesTerm = `${resource.name} ${resource.location} ${resource.detail}`.toLowerCase().includes(term.toLowerCase());
    const matchesType = type === "Todos os tipos" || resource.type === type;
    const matchesAvailability = availability !== "Disponíveis agora" || resource.available;
    return matchesTerm && matchesType && matchesAvailability;
  });
  return (
    <>
      <PageHeading eyebrow="Espaços e equipamentos" title="Encontre seu próximo espaço" description="Pesquise por tipo, capacidade, localização e disponibilidade." action={<button className="button secondary" onClick={() => { setTerm(""); setType("Todos os tipos"); setAvailability("Qualquer disponibilidade"); }}>Limpar filtros</button>} />
      <section className="filter-panel">
        <label className="search-field"><span aria-hidden="true">⌕</span><input aria-label="Buscar recurso" placeholder="Buscar por nome, local ou competência..." value={term} onChange={(event) => setTerm(event.target.value)} /></label>
        <label className="filter-select"><span>Tipo</span><select aria-label="Filtrar por tipo" value={type} onChange={(event) => setType(event.target.value)}><option>Todos os tipos</option><option>Sala</option><option>Professor</option><option>Material</option><option>Equipamento</option></select></label>
        <label className="filter-select"><span>Disponibilidade</span><select aria-label="Filtrar por disponibilidade" value={availability} onChange={(event) => setAvailability(event.target.value)}><option>Qualquer disponibilidade</option><option>Disponíveis agora</option></select></label>
      </section>
      <div className="results-heading"><p><strong>{filtered.length} recursos</strong> encontrados</p><button className="text-button">Mais filtros <span aria-hidden="true">⌄</span></button></div>
      {filtered.length ? <section className="resource-grid" aria-label="Recursos disponíveis">
        {filtered.map((resource) => (
          <article className={`resource-card ${resource.available ? "" : "unavailable"}`} key={resource.id}>
            <div className={`resource-art resource-${resource.type.toLowerCase()}`}><span aria-hidden="true">{resource.type === "Sala" ? "▦" : resource.type === "Professor" ? "♙" : "▣"}</span><span className="resource-type">{resource.type}</span></div>
            <div className="resource-content">
              <div className="resource-title-row"><h2>{resource.name}</h2><span className="availability-dot" aria-label={resource.available ? "Disponível" : "Em manutenção"} /></div>
              <p className="resource-location"><span aria-hidden="true">⌖</span> {resource.location}</p>
              <p className="resource-detail">{resource.type === "Sala" ? `${resource.capacity} pessoas · ` : ""}{resource.detail}</p>
              <div className="resource-footer"><StatusBadge>{resource.available ? "Disponível" : "Em manutenção"}</StatusBadge>{resource.restricted && <span className="restricted-label">Requer aprovação</span>}</div>
              <button className="button secondary full-width resource-action" disabled={!resource.available} onClick={() => onReserve(resource)}>Ver disponibilidade <span aria-hidden="true">→</span></button>
            </div>
          </article>
        ))}
      </section> : <div className="empty-state"><span aria-hidden="true">⌕</span><h2>Nenhum recurso encontrado</h2><p>Tente mudar os termos ou remover alguns filtros.</p></div>}
    </>
  );
}

function ReservationsScreen({ reservations, setReservations, resources, preselectedResource, clearPreselection }) {
  const [showForm, setShowForm] = useState(Boolean(preselectedResource));
  const [notice, setNotice] = useState("");
  const [form, setForm] = useState({ title: "", resource: preselectedResource?.name || "", date: "", start: "09:00", end: "10:00" });
  const [filter, setFilter] = useState("Todas");
  const visibleReservations = reservations.filter((reservation) => filter === "Todas" || reservation.status === filter);

  function createReservation(event) {
    event.preventDefault();
    if (!form.title.trim() || !form.resource || !form.date || form.start >= form.end) {
      setNotice("Preencha os campos e informe um horário final posterior ao inicial.");
      return;
    }
    const resource = resources.find((item) => item.name === form.resource);
    const status = resource?.restricted ? "Aguardando aprovação" : "Aprovada";
    setReservations((items) => [{
      id: Math.max(1000, ...items.map((item) => item.id)) + 1,
      title: form.title.trim(),
      date: new Date(`${form.date}T12:00:00`).toLocaleDateString("pt-BR", { day: "2-digit", month: "short" }),
      time: `${form.start} — ${form.end}`,
      resource: form.resource,
      status,
      owner: "Você",
    }, ...items]);
    setNotice(resource?.restricted ? "Solicitação registrada no protótipo; requer aprovação." : "Reserva adicionada ao protótipo.");
    setForm({ title: "", resource: "", date: "", start: "09:00", end: "10:00" });
    setShowForm(false);
    clearPreselection?.();
  }

  function cancelReservation(id) {
    setReservations((items) => items.map((item) => item.id === id ? { ...item, status: "Cancelada" } : item));
    setNotice("Reserva cancelada no protótipo local.");
  }

  return (
    <>
      <PageHeading eyebrow="Organize seu tempo" title="Suas reservas" description="Crie solicitações e acompanhe cada etapa do processo." action={<button className="button primary" onClick={() => setShowForm((open) => !open)}>＋ Nova reserva</button>} />
      {notice && <div className="notice" role="status"><span>{notice}</span><button aria-label="Fechar mensagem" onClick={() => setNotice("")}>×</button></div>}
      {showForm && <section className="panel reservation-form-panel"><div className="panel-heading"><div><h2>Nova solicitação</h2><p>Os dados ficam apenas neste protótipo.</p></div><button className="icon-button" aria-label="Fechar formulário" onClick={() => { setShowForm(false); clearPreselection?.(); }}>×</button></div>
        <form className="reservation-form" onSubmit={createReservation}>
          <label>Nome da atividade<input value={form.title} onChange={(event) => setForm({ ...form, title: event.target.value })} placeholder="Ex.: Reunião de planejamento" required /></label>
          <label>Recurso<select value={form.resource} onChange={(event) => setForm({ ...form, resource: event.target.value })} required><option value="">Selecione um recurso</option>{resources.filter((resource) => resource.available).map((resource) => <option key={resource.id}>{resource.name}</option>)}</select></label>
          <label>Data<input type="date" value={form.date} onChange={(event) => setForm({ ...form, date: event.target.value })} required /></label>
          <label>Início<input type="time" value={form.start} onChange={(event) => setForm({ ...form, start: event.target.value })} required /></label>
          <label>Término<input type="time" value={form.end} onChange={(event) => setForm({ ...form, end: event.target.value })} required /></label>
          <div className="form-actions"><button type="button" className="button secondary" onClick={() => setShowForm(false)}>Cancelar</button><button type="submit" className="button primary">Enviar solicitação</button></div>
        </form>
      </section>}
      <section className="panel reservations-panel">
        <div className="panel-heading reservation-list-heading"><div><h2>Histórico de solicitações</h2><p>Consulte e gerencie suas reservas.</p></div><label className="compact-filter"><span className="sr-only">Filtrar reservas</span><select aria-label="Filtrar reservas" value={filter} onChange={(event) => setFilter(event.target.value)}><option>Todas</option><option>Aprovada</option><option>Aguardando aprovação</option><option>Cancelada</option></select></label></div>
        <div className="table-scroll"><table><thead><tr><th>Atividade</th><th>Data e horário</th><th>Recurso</th><th>Status</th><th>Ação</th></tr></thead><tbody>
          {visibleReservations.map((reservation) => <tr key={reservation.id}><td><strong>{reservation.title}</strong><small>Solicitação #{reservation.id}</small></td><td>{reservation.date}<small>{reservation.time}</small></td><td>{reservation.resource}</td><td><StatusBadge>{reservation.status}</StatusBadge></td><td>{reservation.status !== "Cancelada" ? <button className="table-action" onClick={() => cancelReservation(reservation.id)}>Cancelar</button> : <span className="muted-copy">—</span>}</td></tr>)}
          {!visibleReservations.length && <tr><td colSpan="5" className="table-empty">Nenhuma reserva nesta categoria.</td></tr>}
        </tbody></table></div>
      </section>
    </>
  );
}

function ManagementScreen({ user, resources, setResources, approvals, setApprovals }) {
  const tabs = user.role === "Administrador"
    ? ["Recursos", "Usuários", "Manutenção", "Auditoria", "Relatórios"]
    : ["Aprovações", "Materiais", "Auditoria", "Relatórios"];
  const [activeTab, setActiveTab] = useState(tabs[0]);
  const [notice, setNotice] = useState("");
  const [newResource, setNewResource] = useState({ name: "", type: "Sala", location: "", capacity: "" });
  const [newUser, setNewUser] = useState({ name: "", email: "", role: "Solicitante" });
  const [maintenanceId, setMaintenanceId] = useState("");

  function addResource(event) {
    event.preventDefault();
    if (!newResource.name.trim() || !newResource.location.trim()) return;
    setResources((items) => [...items, {
      id: Date.now(),
      name: newResource.name.trim(),
      type: newResource.type,
      location: newResource.location.trim(),
      capacity: newResource.capacity ? Number(newResource.capacity) : null,
      detail: newResource.type === "Sala" ? "Novo recurso cadastrado" : "Novo recurso cadastrado",
      available: true,
      restricted: false,
    }]);
    setNewResource({ name: "", type: "Sala", location: "", capacity: "" });
    setNotice("Recurso adicionado somente à demonstração local.");
  }

  function handleApproval(id, approved) {
    setApprovals((items) => items.filter((item) => item.id !== id));
    setNotice(`Solicitação ${approved ? "aprovada" : "recusada"} no protótipo local.`);
  }

  return (
    <>
      <PageHeading eyebrow="Administração do sistema" title="Central de gestão" description="Acompanhe operações e mantenha os recursos do campus organizados." />
      {notice && <div className="notice" role="status"><span>{notice}</span><button aria-label="Fechar mensagem" onClick={() => setNotice("")}>×</button></div>}
      <section className="panel management-panel">
        <div className="management-tabs" role="tablist" aria-label="Áreas de gestão">
          {tabs.map((tab) => <button key={tab} role="tab" aria-selected={activeTab === tab} className={activeTab === tab ? "active" : ""} onClick={() => setActiveTab(tab)}>{tab}{tab === "Aprovações" && approvals.length > 0 && <span className="tab-count">{approvals.length}</span>}</button>)}
        </div>
        <div className="management-content">
          {activeTab === "Recursos" && <div className="management-section"><div className="section-title"><div><h2>Cadastro de recursos</h2><p>Adicione salas, docentes e materiais ao catálogo.</p></div><span className="total-pill">{resources.length} cadastrados</span></div>
            <form className="inline-form resource-create-form" onSubmit={addResource}>
              <label>Nome<input placeholder="Nome do recurso" value={newResource.name} onChange={(event) => setNewResource({ ...newResource, name: event.target.value })} required /></label>
              <label>Tipo<select value={newResource.type} onChange={(event) => setNewResource({ ...newResource, type: event.target.value })}><option>Sala</option><option>Professor</option><option>Material</option><option>Equipamento</option></select></label>
              <label>Localização<input placeholder="Bloco ou departamento" value={newResource.location} onChange={(event) => setNewResource({ ...newResource, location: event.target.value })} required /></label>
              <label>Capacidade<input type="number" min="1" placeholder="—" value={newResource.capacity} onChange={(event) => setNewResource({ ...newResource, capacity: event.target.value })} /></label>
              <button className="button primary" type="submit">Adicionar</button>
            </form>
            <div className="table-scroll"><table><thead><tr><th>Recurso</th><th>Tipo</th><th>Localização</th><th>Situação</th></tr></thead><tbody>{resources.map((resource) => <tr key={resource.id}><td><strong>{resource.name}</strong></td><td>{resource.type}</td><td>{resource.location}</td><td><StatusBadge>{resource.available ? "Disponível" : "Em manutenção"}</StatusBadge></td></tr>)}</tbody></table></div>
          </div>}
          {activeTab === "Usuários" && <div className="management-section"><div className="section-title"><div><h2>Usuários e perfis</h2><p>Cadastre contas e associe um perfil de acesso.</p></div></div>
            <form className="inline-form user-create-form" onSubmit={(event) => { event.preventDefault(); setNotice(`Usuário ${newUser.name} preparado no protótipo local com perfil ${newUser.role}.`); setNewUser({ name: "", email: "", role: "Solicitante" }); }}>
              <label>Nome<input value={newUser.name} onChange={(event) => setNewUser({ ...newUser, name: event.target.value })} placeholder="Nome completo" required /></label><label>E-mail<input type="email" value={newUser.email} onChange={(event) => setNewUser({ ...newUser, email: event.target.value })} placeholder="nome@instituicao.edu.br" required /></label><label>Perfil<select value={newUser.role} onChange={(event) => setNewUser({ ...newUser, role: event.target.value })}><option>Solicitante</option><option>Responsável</option><option>Administrador</option></select></label><button className="button primary">Adicionar usuário</button>
            </form>
            <div className="user-list"><div><span className="avatar avatar-purple">MC</span><span><strong>Mariana Costa</strong><small>mariana.costa@instituicao.edu.br</small></span><StatusBadge>Responsável</StatusBadge></div><div><span className="avatar avatar-blue">RN</span><span><strong>Rafael Nunes</strong><small>rafael.nunes@instituicao.edu.br</small></span><StatusBadge>Solicitante</StatusBadge></div><div><span className="avatar avatar-orange">LM</span><span><strong>Luiza Martins</strong><small>luiza.martins@instituicao.edu.br</small></span><StatusBadge>Solicitante</StatusBadge></div></div>
          </div>}
          {activeTab === "Manutenção" && <div className="management-section"><div className="section-title"><div><h2>Bloqueios de manutenção</h2><p>Marque recursos indisponíveis durante reparos ou manutenção.</p></div></div>
            <form className="inline-form maintenance-form" onSubmit={(event) => { event.preventDefault(); if (!maintenanceId) return; setResources((items) => items.map((item) => item.id === Number(maintenanceId) ? { ...item, available: false } : item)); setNotice("Recurso sinalizado como em manutenção neste protótipo."); setMaintenanceId(""); }}>
              <label>Recurso<select value={maintenanceId} onChange={(event) => setMaintenanceId(event.target.value)} required><option value="">Selecione</option>{resources.filter((resource) => resource.available).map((resource) => <option value={resource.id} key={resource.id}>{resource.name}</option>)}</select></label><label>Início<input type="date" required /></label><label>Término<input type="date" required /></label><label>Motivo<input placeholder="Ex.: revisão dos equipamentos" required /></label><button className="button primary">Criar bloqueio</button>
            </form>
            <div className="maintenance-list">{resources.filter((resource) => !resource.available).map((resource) => <div key={resource.id}><span className="status-icon">!</span><span><strong>{resource.name}</strong><small>Recurso indisponível · período de manutenção</small></span><StatusBadge>Em manutenção</StatusBadge></div>)}</div>
          </div>}
          {activeTab === "Aprovações" && <div className="management-section"><div className="section-title"><div><h2>Solicitações pendentes</h2><p>Revise os pedidos de recursos restritos.</p></div><span className="total-pill">{approvals.length} pendentes</span></div>
            {approvals.length ? <div className="approval-list">{approvals.map((approval) => <article key={approval.id}><div className="approval-avatar">▦</div><div className="approval-details"><strong>{approval.title}</strong><span>Solicitado por {approval.requestedBy}</span><small>{approval.resource} · {approval.date}</small></div><div className="approval-actions"><button className="button secondary" onClick={() => handleApproval(approval.id, false)}>Recusar</button><button className="button approve-button" onClick={() => handleApproval(approval.id, true)}>Aprovar</button></div></article>)}</div> : <div className="empty-state compact"><span aria-hidden="true">✓</span><h2>Tudo em dia!</h2><p>Não há solicitações aguardando análise.</p></div>}
          </div>}
          {activeTab === "Materiais" && <div className="management-section"><div className="section-title"><div><h2>Retirada e devolução</h2><p>Registre a movimentação dos materiais reservados.</p></div></div>
            <div className="table-scroll"><table><thead><tr><th>Material</th><th>Solicitante</th><th>Reserva</th><th>Quantidade</th><th>Ação</th></tr></thead><tbody><tr><td><strong>Projetor portátil</strong></td><td>Rafael Nunes</td><td>Hoje · 14:00</td><td>1 unidade</td><td><button className="table-action" onClick={() => setNotice("Retirada registrada no protótipo local.")}>Registrar retirada</button></td></tr><tr><td><strong>Kit Arduino</strong></td><td>Luiza Martins</td><td>Hoje · 16:00</td><td>4 unidades</td><td><button className="table-action" onClick={() => setNotice("Devolução registrada no protótipo local.")}>Registrar devolução</button></td></tr></tbody></table></div>
          </div>}
          {activeTab === "Auditoria" && <div className="management-section"><div className="section-title"><div><h2>Histórico de atividades</h2><p>Registro demonstrativo de ações recentes na plataforma.</p></div><button className="button secondary">Exportar histórico</button></div>
            <div className="audit-list"><div><span className="audit-dot green-dot" /><span><strong>Reserva aprovada</strong><small>Mariana Costa · Laboratório 03 · hoje, 10:42</small></span><span className="audit-ref">#1041</span></div><div><span className="audit-dot blue-dot" /><span><strong>Recurso atualizado</strong><small>Administrador · Sala Inovação · hoje, 09:18</small></span><span className="audit-ref">Recurso</span></div><div><span className="audit-dot amber-dot" /><span><strong>Solicitação criada</strong><small>Rafael Nunes · Auditório Central · ontem, 16:35</small></span><span className="audit-ref">#1048</span></div><div><span className="audit-dot purple-dot" /><span><strong>Material devolvido</strong><small>Mariana Costa · Projetor portátil · ontem, 15:10</small></span><span className="audit-ref">Movimentação</span></div></div>
          </div>}
          {activeTab === "Relatórios" && <div className="management-section"><div className="section-title"><div><h2>Utilização dos recursos</h2><p>Resumo demonstrativo do período de novembro.</p></div><select aria-label="Período do relatório" className="compact-filter"><option>Novembro 2026</option><option>Outubro 2026</option></select></div>
            <div className="report-stats"><article><small>Horas reservadas</small><strong>486 h</strong><span>+12% neste mês</span></article><article><small>Recursos utilizados</small><strong>32</strong><span>de 42 cadastrados</span></article><article><small>Conflitos evitados</small><strong>18</strong><span>solicitações protegidas</span></article></div>
            <div className="usage-chart"><div className="chart-heading"><strong>Horas alocadas por recurso</strong><span>Últimos 7 dias</span></div>{[["Salas de aula", 82, "124 h"], ["Laboratórios", 64, "96 h"], ["Auditórios", 46, "68 h"], ["Equipamentos", 31, "42 h"]].map(([label, percent, value]) => <div className="bar-row" key={label}><span>{label}</span><div className="bar-track"><span style={{ width: `${percent}%` }} /></div><strong>{value}</strong></div>)}</div>
          </div>}
        </div>
      </section>
    </>
  );
}

export default function App() {
  const [user, setUser] = useState(null);
  const [screen, setScreen] = useState("login");
  const [resources, setResources] = useState(initialResources);
  const [reservations, setReservations] = useState(initialReservations);
  const [approvals, setApprovals] = useState(initialApprovals);
  const [preselectedResource, setPreselectedResource] = useState(null);
  const visibleNavigation = useMemo(
    () => user?.role === "Solicitante" ? navigation.filter((item) => item.id !== "management") : navigation,
    [user],
  );

  function login(nextUser) {
    setUser(nextUser);
    setScreen("dashboard");
  }

  function logout() {
    setUser(null);
    setScreen("login");
  }

  function reserveResource(resource) {
    setPreselectedResource(resource);
    setScreen("reservations");
  }

  if (!user || screen === "login") return <LoginScreen onLogin={login} />;

  return (
    <div className="app-layout">
      <aside className="sidebar">
        <a className="brand sidebar-brand" href="#inicio" onClick={(event) => { event.preventDefault(); setScreen("dashboard"); }} aria-label="TaskFlow, página inicial"><span className="brand-mark">T</span><span>taskflow</span></a>
        <div className="workspace-label">ESPAÇO DE TRABALHO</div>
        <div className="workspace-switch"><span className="workspace-avatar">I</span><span><strong>Instituto Central</strong><small>Campus principal</small></span><span className="chevron">⌄</span></div>
        <nav className="main-nav" aria-label="Navegação principal">
          <span className="nav-section-label">MENU</span>
          {visibleNavigation.map((item) => <button key={item.id} className={screen === item.id ? "selected" : ""} onClick={() => { setScreen(item.id); setPreselectedResource(null); }}><Icon>{item.icon}</Icon>{item.label}{item.id === "management" && user.role === "Responsável" && approvals.length > 0 && <span className="nav-count">{approvals.length}</span>}</button>)}
        </nav>
        <div className="sidebar-bottom">
          <div className="help-card"><span aria-hidden="true">✦</span><strong>Precisa de ajuda?</strong><p>Acesse nosso guia rápido e tire suas dúvidas.</p><button onClick={() => setScreen("management")}>Ver central de ajuda <span aria-hidden="true">→</span></button></div>
          <button className="profile-button" onClick={logout}><span className="profile-avatar">{user.name.slice(0, 1).toUpperCase()}</span><span className="profile-copy"><strong>{user.name}</strong><small>{user.role}</small></span><span className="profile-menu">⋯</span></button>
        </div>
      </aside>
      <div className="main-column">
        <header className="topbar"><div className="breadcrumb"><span>Início</span><span aria-hidden="true">/</span><strong>{navigation.find((item) => item.id === screen)?.label || "Gestão"}</strong></div><div className="topbar-actions"><button className="icon-button notification-button" aria-label="Notificações">♧<i /></button><span className="topbar-divider" /><button className="topbar-user" onClick={logout}><span className="profile-avatar">{user.name.slice(0, 1).toUpperCase()}</span><span>{user.role}</span><span aria-hidden="true">⌄</span></button></div></header>
        <main className="page-content">
          {screen === "dashboard" && <Dashboard user={user} reservations={reservations} approvals={approvals} onNavigate={setScreen} />}
          {screen === "resources" && <ResourcesScreen resources={resources} onReserve={reserveResource} />}
          {screen === "reservations" && <ReservationsScreen reservations={reservations} setReservations={setReservations} resources={resources} preselectedResource={preselectedResource} clearPreselection={() => setPreselectedResource(null)} />}
          {screen === "management" && <ManagementScreen user={user} resources={resources} setResources={setResources} approvals={approvals} setApprovals={setApprovals} />}
        </main>
        <footer className="app-footer"><span>© 2026 TaskFlow</span><span>Organizando espaços, conectando pessoas.</span><a href="#ajuda">Ajuda e suporte</a></footer>
      </div>
    </div>
  );
}
