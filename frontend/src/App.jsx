import { useCallback, useEffect, useMemo, useState } from "react";
import { api, formatStatus, formatTipo, getToken, setToken } from "./api";

const navigation = [
  { id: "dashboard", label: "Visão geral", icon: "⌂" },
  { id: "resources", label: "Recursos", icon: "▦" },
  { id: "reservations", label: "Reservas", icon: "▤" },
  { id: "management", label: "Gestão", icon: "⚙" },
];

const roleLabel = (roles = []) => {
  const role = roles[0];
  return ({ SOLICITANTE: "Solicitante", RESPONSAVEL: "Responsável", ADMINISTRADOR: "Administrador" })[role] || "Usuário";
};

const toIso = (date, time) => new Date(`${date}T${time}:00`).toISOString();
const dateLabel = (value) => new Date(value).toLocaleDateString("pt-BR", { day: "2-digit", month: "short", year: "numeric" });
const timeLabel = (value) => new Date(value).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
const dateInputValue = (value) => {
  const date = new Date(value);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
};

function Icon({ children }) {
  return <span className="nav-icon" aria-hidden="true">{children}</span>;
}

function StatusBadge({ children }) {
  const tone = ["Aprovada", "Disponível"].includes(children) ? "success"
    : children === "Aguardando aprovação" ? "warning"
      : ["Cancelada", "Em manutenção", "Recusada"].includes(children) ? "muted" : "info";
  return <span className={`status-badge ${tone}`}>{children}</span>;
}

function PageHeading({ eyebrow, title, description, action }) {
  return <div className="page-heading"><div><p className="eyebrow">{eyebrow}</p><h1>{title}</h1><p className="page-description">{description}</p></div>{action}</div>;
}

function LoginScreen({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  async function submit(event) {
    event.preventDefault();
    if (!email.trim() || !password) {
      setError("Informe seu e-mail e sua senha para continuar.");
      return;
    }
    setBusy(true);
    setError("");
    try {
      await onLogin(email.trim(), password);
    } catch (reason) {
      setError(reason.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-story">
        <a className="brand login-brand" href="#inicio"><span className="brand-mark">T</span> taskflow</a>
        <div className="login-message"><p className="eyebrow light">Seu campus, em sintonia</p><h1>Espaços para boas ideias acontecerem.</h1><p>Encontre recursos, organize encontros e acompanhe cada reserva em um só lugar.</p>
          <div className="login-decoration" aria-hidden="true"><div className="decoration-card decoration-one"><span>◷</span><strong>Agenda organizada</strong><small>Sem conflitos de horário</small></div><div className="decoration-card decoration-two"><span>✓</span><strong>Tudo conectado</strong><small>Salas, pessoas e materiais</small></div></div>
        </div><p className="login-footer">TaskFlow · Gestão acadêmica de recursos</p>
      </section>
      <section className="login-panel"><div className="login-form-wrap"><span className="mobile-login-brand"><span className="brand-mark">T</span> taskflow</span>
        <p className="eyebrow">Bem-vindo de volta</p><h2>Acesse sua conta</h2><p className="muted-copy">Entre para continuar organizando seu dia.</p>
        <form onSubmit={submit} className="login-form">
          <label htmlFor="login-email">E-mail</label><input id="login-email" type="email" autoComplete="username" placeholder="voce@instituicao.edu.br" value={email} onChange={(event) => setEmail(event.target.value)} />
          <div className="label-row"><label htmlFor="login-password">Senha</label></div><input id="login-password" type="password" autoComplete="current-password" placeholder="Sua senha" value={password} onChange={(event) => setPassword(event.target.value)} />
          {error && <p className="form-error" role="alert">{error}</p>}
          <button className="button primary full-width" type="submit" disabled={busy}>{busy ? "Validando..." : "Entrar na plataforma"} <span aria-hidden="true">→</span></button>
        </form>
        <p className="prototype-note"><span aria-hidden="true">ⓘ</span> A autenticação é validada pelo servidor TaskFlow.</p>
      </div></section>
    </main>
  );
}

function Dashboard({ user, reservations, resources, onNavigate }) {
  const monthReservations = reservations.filter((r) => new Date(r.inicio).getMonth() === new Date().getMonth());
  const hours = reservations.filter((r) => r.status === "APROVADA")
    .reduce((sum, r) => sum + (new Date(r.fim) - new Date(r.inicio)) / 3600000, 0);
  return <>
    <PageHeading eyebrow="TaskFlow" title={`Olá, ${user.nome.split(" ")[0]}!`} description="Acompanhe reservas e recursos do seu campus." action={<button className="button primary" onClick={() => onNavigate("reservations")}>＋ Nova reserva</button>} />
    <section className="stat-grid" aria-label="Resumo">
      <article className="stat-card"><span className="stat-label">Reservas neste mês</span><strong>{monthReservations.length}</strong><small>Dados atualizados do servidor</small></article>
      <article className="stat-card"><span className="stat-label">Horas aprovadas</span><strong>{hours.toFixed(1)} <small>h</small></strong><small>Período carregado</small></article>
      <article className="stat-card"><span className="stat-label">Aguardando aprovação</span><strong>{reservations.filter((r) => r.status === "AGUARDANDO_APROVACAO").length}</strong><small>Solicitações pendentes</small></article>
      <article className="stat-card"><span className="stat-label">Recursos cadastrados</span><strong>{resources.length}</strong><small>Catálogo ativo</small></article>
    </section>
    <section className="panel schedule-panel"><div className="panel-heading"><div><h2>Próximas reservas</h2><p>Compromissos carregados da sua agenda.</p></div><button className="text-button" onClick={() => onNavigate("reservations")}>Ver reservas →</button></div>
      {reservations.length ? <div className="schedule-list">{reservations.slice(0, 5).map((r) => <article className="schedule-item" key={r.id}><div className="schedule-date"><strong>{new Date(r.inicio).getDate()}</strong><span>{new Date(r.inicio).toLocaleDateString("pt-BR", { month: "short" }).toUpperCase()}</span></div><div className="schedule-details"><strong>{r.finalidade}</strong><span>{dateLabel(r.inicio)} · {timeLabel(r.inicio)}–{timeLabel(r.fim)} · {r.recursos.map((x) => x.nome).join(", ")}</span></div><StatusBadge>{formatStatus(r.status)}</StatusBadge></article>)}</div> : <div className="empty-state compact"><h2>Nenhuma reserva registrada</h2><p>Crie sua primeira solicitação para vê-la aqui.</p></div>}
    </section>
  </>;
}

function ResourcesScreen({ resources, reload, onReserve }) {
  const [term, setTerm] = useState("");
  const [type, setType] = useState("");
  const [location, setLocation] = useState("");
  const [minimumCapacity, setMinimumCapacity] = useState("");
  const [period, setPeriod] = useState({ inicio: "", fim: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const filtered = resources.filter((r) => `${r.nome} ${r.localizacao || ""} ${r.competencia || ""}`.toLowerCase().includes(term.toLowerCase()));

  async function search() {
    setLoading(true);
    setError("");
    const query = new URLSearchParams();
    if (type) query.set("tipo", type);
    if (location) query.set("localizacao", location);
    if (minimumCapacity) query.set("capacidadeMinima", minimumCapacity);
    if (term) query.set("competencia", term);
    if (period.inicio && period.fim) {
      query.set("inicio", new Date(period.inicio).toISOString());
      query.set("fim", new Date(period.fim).toISOString());
    }
    try {
      await reload(query.toString());
    } catch (reason) {
      setError(reason.message);
    } finally {
      setLoading(false);
    }
  }

  return <>
    <PageHeading eyebrow="Espaços e equipamentos" title="Encontre um recurso" description="Pesquise por tipo, capacidade, localização, competência e período." />
    <section className="filter-panel">
      <label className="search-field"><span aria-hidden="true">⌕</span><input aria-label="Buscar recurso" placeholder="Buscar por nome ou competência..." value={term} onChange={(e) => setTerm(e.target.value)} /></label>
      <label className="filter-select"><span>Tipo</span><select aria-label="Filtrar por tipo" value={type} onChange={(e) => setType(e.target.value)}><option value="">Todos os tipos</option><option value="SALA">Sala</option><option value="PROFESSOR">Professor</option><option value="MATERIAL">Material</option><option value="EQUIPAMENTO">Equipamento</option></select></label>
      <label className="filter-select"><span>Localização</span><input aria-label="Localização" value={location} onChange={(e) => setLocation(e.target.value)} placeholder="Bloco ou departamento" /></label>
      <label className="filter-select"><span>Capacidade mínima</span><input aria-label="Capacidade mínima" type="number" min="1" value={minimumCapacity} onChange={(e) => setMinimumCapacity(e.target.value)} /></label>
      <label className="filter-select"><span>Disponível desde</span><input aria-label="Disponível desde" type="datetime-local" value={period.inicio} onChange={(e) => setPeriod({ ...period, inicio: e.target.value })} /></label>
      <label className="filter-select"><span>Até</span><input aria-label="Disponível até" type="datetime-local" value={period.fim} onChange={(e) => setPeriod({ ...period, fim: e.target.value })} /></label>
      <button className="button primary" onClick={search} disabled={loading}>{loading ? "Buscando..." : "Pesquisar"}</button>
    </section>
    {error && <p className="form-error" role="alert">{error}</p>}
    <div className="results-heading"><p><strong>{filtered.length} recursos</strong> encontrados</p></div>
    {filtered.length ? <section className="resource-grid" aria-label="Recursos disponíveis">{filtered.map((r) => <article className="resource-card" key={r.id}>
      <div className={`resource-art resource-${formatTipo(r.tipo).toLowerCase()}`}><span aria-hidden="true">{r.tipo === "SALA" ? "▦" : r.tipo === "PROFESSOR" ? "♙" : "▣"}</span><span className="resource-type">{formatTipo(r.tipo)}</span></div>
      <div className="resource-content"><div className="resource-title-row"><h2>{r.nome}</h2></div><p className="resource-location">⌖ {r.localizacao || "Localização não informada"}</p><p className="resource-detail">{r.capacidade ? `${r.capacidade} pessoas · ` : ""}{r.competencia || (r.quantidadeTotal != null ? `${r.quantidadeTotal} unidades` : "")}</p>
        <div className="resource-footer">{period.inicio && period.fim ? <StatusBadge>Disponível</StatusBadge> : <span className="restricted-label">Consulte disponibilidade por período</span>}{r.restrito && <span className="restricted-label">Requer aprovação</span>}</div>
        <button className="button secondary full-width resource-action" onClick={() => onReserve(r)}>Solicitar reserva →</button>
      </div></article>)}</section> : <div className="empty-state"><span aria-hidden="true">⌕</span><h2>Nenhum recurso encontrado</h2><p>Altere os filtros e tente novamente.</p></div>}
  </>;
}

function ReservationsScreen({ reservations, resources, reload, onChanged, preselectedResource, clearPreselection, user }) {
  const [showForm, setShowForm] = useState(Boolean(preselectedResource));
  const [editing, setEditing] = useState(null);
  const [notice, setNotice] = useState("");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const [form, setForm] = useState({
    finalidade: "", recursoIds: preselectedResource ? [preselectedResource.id] : [],
    date: "", start: "09:00", end: "10:00", quantidades: {},
  });
  const [filter, setFilter] = useState("");
  const visible = reservations.filter((r) => !filter || r.status === filter);

  function startEdit(item) {
    setEditing(item);
    setForm({
      finalidade: item.finalidade,
      recursoIds: item.recursos.map((resource) => resource.id),
      date: dateInputValue(item.inicio),
      start: timeLabel(item.inicio),
      end: timeLabel(item.fim),
      quantidades: Object.fromEntries(item.recursos.map((resource) =>
        [resource.id, resource.quantidadeReservada || 1])),
    });
    setShowForm(true);
    setError("");
  }

  async function submit(event) {
    event.preventDefault();
    const start = toIso(form.date, form.start);
    const end = toIso(form.date, form.end);
    if (!form.finalidade.trim() || !form.recursoIds.length || new Date(end) <= new Date(start)) {
      setError("Informe a atividade, o recurso e um horário final posterior ao inicial.");
      return;
    }
    setBusy(true);
    setError("");
    try {
      const body = {
        finalidade: form.finalidade.trim(), inicio: start, fim: end,
        recursoIds: form.recursoIds.map(Number),
        quantidades: Object.fromEntries(Object.entries(form.quantidades)
          .filter(([id]) => form.recursoIds.includes(Number(id)))
          .map(([id, quantity]) => [id, Number(quantity)])),
      };
      if (editing) await api(`/reservas/${editing.id}`, { method: "PUT", body: JSON.stringify(body) });
      else await api("/reservas", { method: "POST", body: JSON.stringify(body) });
      setNotice(editing ? "Reserva atualizada." : "Solicitação registrada.");
      setEditing(null);
      setShowForm(false);
      clearPreselection();
      await onChanged();
    } catch (reason) {
      setError(reason.message);
    } finally {
      setBusy(false);
    }
  }

  async function cancel(id) {
    setError("");
    try {
      await api(`/reservas/${id}/cancelamento`, { method: "POST" });
      setNotice("Reserva cancelada.");
      await onChanged();
    } catch (reason) {
      setError(reason.message);
    }
  }

  return <>
    <PageHeading eyebrow="Organize seu tempo" title="Reservas" description="Crie, altere ou cancele solicitações e acompanhe o status." action={<button className="button primary" onClick={() => { setEditing(null); setShowForm((open) => !open); }}>＋ Nova reserva</button>} />
    {notice && <div className="notice" role="status"><span>{notice}</span><button aria-label="Fechar mensagem" onClick={() => setNotice("")}>×</button></div>}
    {error && <p className="form-error" role="alert">{error}</p>}
    {showForm && <section className="panel reservation-form-panel"><div className="panel-heading"><div><h2>{editing ? "Alterar reserva" : "Nova solicitação"}</h2><p>O servidor valida disponibilidade e conflitos antes de confirmar.</p></div><button className="icon-button" aria-label="Fechar formulário" onClick={() => { setShowForm(false); setEditing(null); clearPreselection(); }}>×</button></div>
      <form className="reservation-form" onSubmit={submit}>
        <label>Nome da atividade<input value={form.finalidade} onChange={(e) => setForm({ ...form, finalidade: e.target.value })} maxLength="500" required /></label>
        <fieldset className="resource-options"><legend>Recursos necessários</legend>{resources.map((r) => {
          const selected = form.recursoIds.includes(r.id);
          const usesQuantity = ["MATERIAL", "EQUIPAMENTO"].includes(r.tipo);
          return <div key={r.id}><label><input type="checkbox" checked={selected} onChange={(event) => {
            const ids = event.target.checked
              ? [...form.recursoIds, r.id]
              : form.recursoIds.filter((id) => id !== r.id);
            setForm({ ...form, recursoIds: ids, quantidades: { ...form.quantidades, [r.id]: form.quantidades[r.id] || 1 } });
          }} /> {r.nome}{r.restrito ? " · requer aprovação" : ""}</label>
            {selected && usesQuantity && <label>Quantidade de {r.nome}<input type="number" min="1" max={r.quantidadeTotal ?? undefined} value={form.quantidades[r.id] || 1} onChange={(event) => setForm({ ...form, quantidades: { ...form.quantidades, [r.id]: event.target.value } })} required /></label>}
          </div>;
        })}</fieldset>
        <label>Data<input type="date" min={new Date().toISOString().slice(0, 10)} value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} required /></label>
        <label>Início<input type="time" value={form.start} onChange={(e) => setForm({ ...form, start: e.target.value })} required /></label>
        <label>Término<input type="time" value={form.end} onChange={(e) => setForm({ ...form, end: e.target.value })} required /></label>
        <div className="form-actions"><button type="button" className="button secondary" onClick={() => { setShowForm(false); setEditing(null); }}>Cancelar</button><button type="submit" className="button primary" disabled={busy}>{busy ? "Salvando..." : editing ? "Salvar alterações" : "Enviar solicitação"}</button></div>
      </form>
    </section>}
    <section className="panel reservations-panel"><div className="panel-heading reservation-list-heading"><div><h2>Histórico de solicitações</h2><p>Consulte e gerencie suas reservas.</p></div><label className="compact-filter"><span className="sr-only">Filtrar reservas</span><select aria-label="Filtrar reservas" value={filter} onChange={(e) => setFilter(e.target.value)}><option value="">Todas</option><option value="APROVADA">Aprovada</option><option value="AGUARDANDO_APROVACAO">Aguardando aprovação</option><option value="RECUSADA">Recusada</option><option value="CANCELADA">Cancelada</option></select></label></div>
      <div className="table-scroll"><table><thead><tr><th>Atividade</th><th>Data e horário</th><th>Recurso</th><th>Status</th><th>Ação</th></tr></thead><tbody>
        {visible.map((r) => <tr key={r.id}><td><strong>{r.finalidade}</strong><small>Solicitação #{r.id}{user.role !== "Solicitante" ? ` · ${r.solicitanteNome}` : ""}</small></td><td>{dateLabel(r.inicio)}<small>{timeLabel(r.inicio)} — {timeLabel(r.fim)}</small></td><td>{r.recursos.map((x) => x.nome).join(", ")}</td><td><StatusBadge>{formatStatus(r.status)}</StatusBadge></td><td>{!["CANCELADA", "RECUSADA"].includes(r.status) ? <><button className="table-action" onClick={() => startEdit(r)}>Alterar</button> <button className="table-action" onClick={() => cancel(r.id)}>Cancelar</button></> : "—"}</td></tr>)}
        {!visible.length && <tr><td colSpan="5" className="table-empty">Nenhuma reserva nesta categoria.</td></tr>}
      </tbody></table></div>
    </section>
  </>;
}

function ManagementScreen({ user, resources, users, reservations, reloadAll }) {
  const admin = user.role === "Administrador";
  const tabs = admin ? ["Recursos", "Usuários", "Manutenção", "Auditoria", "Relatórios"] : ["Aprovações", "Materiais", "Auditoria", "Relatórios"];
  const [activeTab, setActiveTab] = useState(tabs[0]);
  const [notice, setNotice] = useState("");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const [resource, setResource] = useState({ nome: "", tipo: "SALA", localizacao: "", capacidade: "", competencia: "", quantidadeTotal: "", restrito: false, docenteUsuarioId: "" });
  const [newUser, setNewUser] = useState({ nome: "", email: "", senha: "", perfil: "SOLICITANTE" });
  const [maintenance, setMaintenance] = useState({ recursoId: "", inicio: "", fim: "", justificativa: "" });
  const [audits, setAudits] = useState([]);
  const [blocks, setBlocks] = useState([]);
  const [report, setReport] = useState(null);
  const [movementHistory, setMovementHistory] = useState({});
  const pending = reservations.filter((r) => r.status === "AGUARDANDO_APROVACAO");
  const inventoryReservations = reservations.filter((r) => r.recursos.some((x) => ["MATERIAL", "EQUIPAMENTO"].includes(x.tipo)));

  async function act(operation, success) {
    setBusy(true);
    setError("");
    setNotice("");
    try {
      await operation();
      setNotice(success);
      try {
        await reloadAll();
        if (activeTab === "Materiais") await loadMovementHistory();
      } catch {
        setError("A alteração foi salva, mas não foi possível atualizar a lista. Recarregue a página.");
      }
      return true;
    } catch (reason) {
      setError(reason.message);
      return false;
    } finally {
      setBusy(false);
    }
  }

  async function loadAudits() {
    try { setAudits(await api("/auditorias")); } catch (reason) { setError(reason.message); }
  }
  async function loadBlocks() {
    try { setBlocks(await api("/manutencoes")); } catch (reason) { setError(reason.message); }
  }
  async function loadReport() {
    const today = new Date();
    const begin = new Date(today.getFullYear(), today.getMonth(), 1);
    const end = new Date(today.getFullYear(), today.getMonth() + 1, 1);
    try { setReport(await api(`/relatorios/utilizacao?inicio=${begin.toISOString()}&fim=${end.toISOString()}`)); }
    catch (reason) { setError(reason.message); }
  }
  async function loadMovementHistory() {
    try {
      const entries = await Promise.all(inventoryReservations.map(async (reservation) => [
        reservation.id, await api(`/reservas/${reservation.id}/movimentacoes`),
      ]));
      setMovementHistory(Object.fromEntries(entries));
    } catch (reason) {
      setError(reason.message);
    }
  }

  useEffect(() => {
    if (activeTab === "Auditoria") loadAudits();
    if (activeTab === "Manutenção") loadBlocks();
    if (activeTab === "Relatórios") loadReport();
    if (activeTab === "Materiais") loadMovementHistory();
  }, [activeTab]);

  async function createResource(event) {
    event.preventDefault();
    const body = {
      nome: resource.nome, tipo: resource.tipo, localizacao: resource.localizacao || null,
      competencia: resource.competencia || null,
      capacidade: resource.tipo === "SALA" ? Number(resource.capacidade) : null,
      quantidadeTotal: ["MATERIAL", "EQUIPAMENTO"].includes(resource.tipo) ? Number(resource.quantidadeTotal) : null,
      restrito: resource.restrito,
      docenteUsuarioId: resource.tipo === "PROFESSOR" ? Number(resource.docenteUsuarioId) : null,
    };
    if (await act(() => api("/recursos", { method: "POST", body: JSON.stringify(body) }),
      "Recurso cadastrado.")) {
      setResource({ nome: "", tipo: "SALA", localizacao: "", capacidade: "", competencia: "", quantidadeTotal: "", restrito: false, docenteUsuarioId: "" });
    }
  }

  async function createUser(event) {
    event.preventDefault();
    if (await act(() => api("/usuarios", { method: "POST", body: JSON.stringify(newUser) }), "Usuário cadastrado.")) {
      setNewUser({ nome: "", email: "", senha: "", perfil: "SOLICITANTE" });
    }
  }

  async function toggleUserStatus(target) {
    await act(() => api(`/usuarios/${target.id}/status`, {
      method: "PATCH", body: JSON.stringify({ ativo: !target.ativo }),
    }), target.ativo ? "Usuário desativado." : "Usuário ativado.");
  }

  async function createBlock(event) {
    event.preventDefault();
    const body = { ...maintenance, recursoId: Number(maintenance.recursoId),
      inicio: new Date(`${maintenance.inicio}:00`).toISOString(),
      fim: new Date(`${maintenance.fim}:00`).toISOString() };
    if (await act(() => api("/manutencoes", { method: "POST", body: JSON.stringify(body) }),
      "Bloqueio de manutenção registrado.")) {
      setMaintenance({ recursoId: "", inicio: "", fim: "", justificativa: "" });
    }
  }

  async function decide(reservationId, approved) {
    await act(() => api(`/reservas/${reservationId}/aprovacao`, {
      method: "POST", body: JSON.stringify({ aprovada: approved, justificativa: "" }),
    }), approved ? "Solicitação aprovada." : "Solicitação recusada.");
  }

  async function move(reservationId, resourceId, tipo, quantidade) {
    await act(() => api(`/reservas/${reservationId}/movimentacoes`, {
      method: "POST", body: JSON.stringify({ recursoId: resourceId, tipo, quantidade }),
    }), tipo === "RETIRADA" ? "Retirada registrada." : "Devolução registrada.");
  }

  return <>
    <PageHeading eyebrow="Administração do sistema" title="Central de gestão" description="Gerencie recursos e acompanhe as operações autorizadas ao seu perfil." />
    {notice && <div className="notice" role="status"><span>{notice}</span><button aria-label="Fechar mensagem" onClick={() => setNotice("")}>×</button></div>}
    {error && <p className="form-error" role="alert">{error}</p>}
    <section className="panel management-panel"><div className="management-tabs" role="tablist" aria-label="Áreas de gestão">
      {tabs.map((tab) => <button key={tab} role="tab" aria-selected={activeTab === tab} className={activeTab === tab ? "active" : ""} onClick={() => setActiveTab(tab)}>{tab}{tab === "Aprovações" && pending.length > 0 && <span className="tab-count">{pending.length}</span>}</button>)}
    </div><div className="management-content">
      {activeTab === "Recursos" && <div className="management-section"><div className="section-title"><div><h2>Cadastro de recursos</h2><p>Cadastre salas, professores, materiais e equipamentos.</p></div><span className="total-pill">{resources.length} cadastrados</span></div>
        <form className="inline-form resource-create-form" onSubmit={createResource}>
          <label>Nome<input value={resource.nome} onChange={(e) => setResource({ ...resource, nome: e.target.value })} required /></label>
          <label>Tipo<select value={resource.tipo} onChange={(e) => setResource({ ...resource, tipo: e.target.value })}><option value="SALA">Sala</option><option value="PROFESSOR">Professor</option><option value="MATERIAL">Material</option><option value="EQUIPAMENTO">Equipamento</option></select></label>
          <label>Localização<input value={resource.localizacao} onChange={(e) => setResource({ ...resource, localizacao: e.target.value })} /></label>
          {resource.tipo === "SALA" && <label>Capacidade<input type="number" min="1" value={resource.capacidade} onChange={(e) => setResource({ ...resource, capacidade: e.target.value })} required /></label>}
          {resource.tipo === "PROFESSOR" && <><label>Competência<input value={resource.competencia} onChange={(e) => setResource({ ...resource, competencia: e.target.value })} required /></label><label>Usuário docente<select value={resource.docenteUsuarioId} onChange={(e) => setResource({ ...resource, docenteUsuarioId: e.target.value })} required><option value="">Selecione</option>{users.map((u) => <option key={u.id} value={u.id}>{u.nome}</option>)}</select></label></>}
          {["MATERIAL", "EQUIPAMENTO"].includes(resource.tipo) && <label>Quantidade total<input type="number" min="0" value={resource.quantidadeTotal} onChange={(e) => setResource({ ...resource, quantidadeTotal: e.target.value })} required /></label>}
          <label className="checkbox-label"><input type="checkbox" checked={resource.restrito} onChange={(e) => setResource({ ...resource, restrito: e.target.checked })} /> Requer aprovação</label>
          <button className="button primary" type="submit" disabled={busy}>Adicionar</button>
        </form>
        <div className="table-scroll"><table><thead><tr><th>Recurso</th><th>Tipo</th><th>Localização</th><th>Situação</th></tr></thead><tbody>{resources.map((r) => <tr key={r.id}><td><strong>{r.nome}</strong></td><td>{formatTipo(r.tipo)}</td><td>{r.localizacao}</td><td><StatusBadge>Ativo</StatusBadge></td></tr>)}</tbody></table></div>
      </div>}
      {activeTab === "Usuários" && <div className="management-section"><div className="section-title"><div><h2>Usuários e perfis</h2><p>Contas são criadas por um administrador, sem cadastro público.</p></div></div>
        <form className="inline-form user-create-form" onSubmit={createUser}><label>Nome<input value={newUser.nome} onChange={(e) => setNewUser({ ...newUser, nome: e.target.value })} required /></label><label>E-mail<input type="email" value={newUser.email} onChange={(e) => setNewUser({ ...newUser, email: e.target.value })} required /></label><label>Senha temporária<input type="password" minLength="12" value={newUser.senha} onChange={(e) => setNewUser({ ...newUser, senha: e.target.value })} required /></label><label>Perfil<select value={newUser.perfil} onChange={(e) => setNewUser({ ...newUser, perfil: e.target.value })}><option value="SOLICITANTE">Solicitante</option><option value="RESPONSAVEL">Responsável</option><option value="ADMINISTRADOR">Administrador</option></select></label><button className="button primary" disabled={busy}>Adicionar usuário</button></form>
        <div className="user-list">{users.map((u) => <div key={u.id}><span className="avatar avatar-purple">{u.nome.slice(0, 2).toUpperCase()}</span><span><strong>{u.nome}</strong><small>{u.email}</small></span><StatusBadge>{u.ativo ? roleLabel(u.perfis) : "Inativo"}</StatusBadge><button className="table-action" onClick={() => toggleUserStatus(u)} disabled={busy || u.email === user.email}>{u.ativo ? "Desativar" : "Ativar"}</button></div>)}</div>
      </div>}
      {activeTab === "Manutenção" && <div className="management-section"><div className="section-title"><div><h2>Bloqueios de manutenção</h2><p>Períodos bloqueados não ficam disponíveis para novas reservas.</p></div></div>
        <form className="inline-form maintenance-form" onSubmit={createBlock}><label>Recurso<select value={maintenance.recursoId} onChange={(e) => setMaintenance({ ...maintenance, recursoId: e.target.value })} required><option value="">Selecione</option>{resources.map((r) => <option key={r.id} value={r.id}>{r.nome}</option>)}</select></label><label>Início<input type="datetime-local" value={maintenance.inicio} onChange={(e) => setMaintenance({ ...maintenance, inicio: e.target.value })} required /></label><label>Término<input type="datetime-local" value={maintenance.fim} onChange={(e) => setMaintenance({ ...maintenance, fim: e.target.value })} required /></label><label>Motivo<input value={maintenance.justificativa} onChange={(e) => setMaintenance({ ...maintenance, justificativa: e.target.value })} required /></label><button className="button primary" disabled={busy}>Criar bloqueio</button></form>
        <div className="maintenance-list">{blocks.map((b) => <div key={b.id}><span className="status-icon">!</span><span><strong>{b.recurso}</strong><small>{dateLabel(b.inicio)} — {dateLabel(b.fim)} · {b.justificativa}</small></span><StatusBadge>Em manutenção</StatusBadge></div>)}</div>
      </div>}
      {activeTab === "Aprovações" && <div className="management-section"><div className="section-title"><div><h2>Solicitações pendentes</h2><p>Analise solicitações de recursos restritos.</p></div><span className="total-pill">{pending.length} pendentes</span></div>
        {pending.length ? <div className="approval-list">{pending.map((r) => <article key={r.id}><div className="approval-avatar">▦</div><div className="approval-details"><strong>{r.finalidade}</strong><span>Solicitado por {r.solicitanteNome}</span><small>{r.recursos.map((x) => x.nome).join(", ")} · {dateLabel(r.inicio)} · {timeLabel(r.inicio)}–{timeLabel(r.fim)}</small></div><div className="approval-actions"><button className="button secondary" onClick={() => decide(r.id, false)} disabled={busy}>Recusar</button><button className="button approve-button" onClick={() => decide(r.id, true)} disabled={busy}>Aprovar</button></div></article>)}</div> : <div className="empty-state compact"><span aria-hidden="true">✓</span><h2>Tudo em dia!</h2><p>Não há solicitações aguardando análise.</p></div>}
      </div>}
      {activeTab === "Materiais" && <div className="management-section"><div className="section-title"><div><h2>Retirada e devolução</h2><p>Registre cada movimentação associada à reserva.</p></div></div>
        <div className="table-scroll"><table><thead><tr><th>Material</th><th>Solicitante</th><th>Reserva</th><th>Quantidade</th><th>Ação</th></tr></thead><tbody>{inventoryReservations.flatMap((r) => r.recursos.filter((x) => ["MATERIAL", "EQUIPAMENTO"].includes(x.tipo)).map((item) => {
          const movements = (movementHistory[r.id] || []).filter((entry) => entry.recursoId === item.id);
          const withdrawn = movements.filter((entry) => entry.tipo === "RETIRADA").reduce((sum, entry) => sum + entry.quantidade, 0);
          const returned = movements.filter((entry) => entry.tipo === "DEVOLUCAO").reduce((sum, entry) => sum + entry.quantidade, 0);
          const qty = item.quantidadeReservada;
          return <tr key={`${r.id}-${item.id}`}><td><strong>{item.nome}</strong></td><td>{r.solicitanteNome}</td><td>{dateLabel(r.inicio)} · {timeLabel(r.inicio)}</td><td>{qty} · retirado {withdrawn}, devolvido {returned}</td><td>{withdrawn < qty && <button className="table-action" onClick={() => move(r.id, item.id, "RETIRADA", qty - withdrawn)} disabled={busy}>Registrar retirada</button>} {returned < withdrawn && <button className="table-action" onClick={() => move(r.id, item.id, "DEVOLUCAO", withdrawn - returned)} disabled={busy}>Registrar devolução</button>}</td></tr>;
        }))}</tbody></table></div>
      </div>}
      {activeTab === "Auditoria" && <div className="management-section"><div className="section-title"><div><h2>Histórico de atividades</h2><p>Registros de auditoria recentes armazenados pelo servidor.</p></div></div>
        <div className="audit-list">{audits.map((a) => <div key={a.id}><span className="audit-dot blue-dot" /><span><strong>{a.acao} · {a.tipoEntidade}</strong><small>{a.usuario} · {new Date(a.ocorridaEm).toLocaleString("pt-BR")}</small></span><span className="audit-ref">{a.entidadeId ? `#${a.entidadeId}` : ""}</span></div>)}{!audits.length && <p>Nenhuma alteração registrada.</p>}</div>
      </div>}
      {activeTab === "Relatórios" && <div className="management-section"><div className="section-title"><div><h2>Utilização dos recursos</h2><p>Consolidado do período mensal atual.</p></div></div>
        {report && <><div className="report-stats"><article><small>Horas alocadas</small><strong>{report.horasAlocadas} h</strong></article><article><small>Recursos utilizados</small><strong>{report.recursosUtilizados}</strong></article><article><small>Conflitos evitados</small><strong>{report.conflitosEvitados}</strong></article></div>
          <div className="usage-chart"><div className="chart-heading"><strong>Horas alocadas por recurso</strong></div>{report.utilizacaoPorRecurso.map((item) => <div className="bar-row" key={item.recursoId}><span>{item.nome}</span><div className="bar-track"><span style={{ width: `${Math.min(100, Number(item.horasAlocadas || item.horas) * 2)}%` }} /></div><strong>{item.horas} h</strong></div>)}</div>
        </>}
      </div>}
    </div></section>
  </>;
}

export default function App() {
  const [user, setUser] = useState(null);
  const [screen, setScreen] = useState("login");
  const [resources, setResources] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [users, setUsers] = useState([]);
  const [preselectedResource, setPreselectedResource] = useState(null);
  const [loading, setLoading] = useState(Boolean(getToken()));
  const [error, setError] = useState("");
  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const visibleNavigation = useMemo(() => user?.role === "Solicitante"
    ? navigation.filter((item) => item.id !== "management") : navigation, [user]);

  const loadData = useCallback(async (resourceQuery = "") => {
    const requests = [
      api(`/recursos${resourceQuery ? `?${resourceQuery}` : ""}`),
      api("/reservas"),
    ];
    if (user?.role === "Administrador") requests.push(api("/usuarios"));
    const [nextResources, nextReservations, nextUsers = []] = await Promise.all(requests);
    setResources(nextResources);
    setReservations(nextReservations);
    setUsers(nextUsers);
  }, [user?.role]);

  useEffect(() => {
    if (!getToken()) return;
    api("/auth/me").then(async (profile) => {
      const nextUser = { ...profile, role: roleLabel(profile.perfis) };
      setUser(nextUser);
      setScreen("dashboard");
    }).catch(() => {
      setToken(null);
      setLoading(false);
    });
    const unauthorized = () => {
      setUser(null);
      setScreen("login");
      setError("Sua sessão expirou. Entre novamente.");
    };
    window.addEventListener("taskflow:unauthorized", unauthorized);
    return () => window.removeEventListener("taskflow:unauthorized", unauthorized);
  }, []);

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    loadData().catch((reason) => setError(reason.message)).finally(() => setLoading(false));
  }, [user, loadData]);

  async function login(email, senha) {
    const result = await api("/auth/login", { method: "POST", body: JSON.stringify({ email, senha }) });
    setToken(result.accessToken);
    setUser({ ...result.usuario, role: roleLabel(result.usuario.perfis) });
    setScreen("dashboard");
  }

  function logout() {
    setToken(null);
    setUser(null);
    setScreen("login");
    setResources([]);
    setReservations([]);
  }

  async function toggleNotifications() {
    const opening = !showNotifications;
    setShowNotifications(opening);
    if (!opening) return;
    try {
      setNotifications(await api("/notificacoes"));
    } catch (reason) {
      setError(reason.message);
    }
  }

  function reserveResource(resource) {
    setPreselectedResource(resource);
    setScreen("reservations");
  }

  if (!user || screen === "login") return <LoginScreen onLogin={login} />;

  return <div className="app-layout">
    <aside className="sidebar">
      <a className="brand sidebar-brand" href="#inicio" onClick={(event) => { event.preventDefault(); setScreen("dashboard"); }} aria-label="TaskFlow, página inicial"><span className="brand-mark">T</span><span>taskflow</span></a>
      <div className="workspace-label">ESPAÇO DE TRABALHO</div><div className="workspace-switch"><span className="workspace-avatar">T</span><span><strong>TaskFlow</strong><small>Gestão acadêmica</small></span></div>
      <nav className="main-nav" aria-label="Navegação principal"><span className="nav-section-label">MENU</span>{visibleNavigation.map((item) => <button key={item.id} className={screen === item.id ? "selected" : ""} onClick={() => { setScreen(item.id); setPreselectedResource(null); }}><Icon>{item.icon}</Icon>{item.label}</button>)}</nav>
      <div className="sidebar-bottom"><div className="help-card"><span aria-hidden="true">✦</span><strong>Precisa de ajuda?</strong><p>Consulte as áreas de recursos e reservas.</p><button onClick={() => setScreen("resources")}>Ver recursos →</button></div>
        <button className="profile-button" onClick={logout}><span className="profile-avatar">{user.nome.slice(0, 1).toUpperCase()}</span><span className="profile-copy"><strong>{user.nome}</strong><small>{user.role}</small></span><span className="profile-menu">Sair</span></button>
      </div>
    </aside>
    <div className="main-column"><header className="topbar"><div className="breadcrumb"><span>Início</span><span aria-hidden="true">/</span><strong>{navigation.find((item) => item.id === screen)?.label || "Gestão"}</strong></div><div className="topbar-actions"><button className="icon-button notification-button" aria-label="Notificações" onClick={toggleNotifications}>♧</button><span>{user.role}</span><span className="topbar-divider" /><button className="topbar-user" onClick={logout}>Sair</button></div></header>
      <main className="page-content" key={screen}>
        {error && <div className="notice" role="alert"><span>{error}</span><button aria-label="Fechar mensagem" onClick={() => setError("")}>×</button></div>}
        {showNotifications && <section className="panel notification-panel" aria-label="Notificações"><div className="panel-heading"><div><h2>Notificações</h2><p>Atualizações sobre suas reservas.</p></div><button className="icon-button" aria-label="Fechar notificações" onClick={() => setShowNotifications(false)}>×</button></div>{notifications.length ? notifications.map((item) => <p key={item.id}>{item.mensagem} · {new Date(item.criadaEm).toLocaleString("pt-BR")}</p>) : <p>Nenhuma notificação recebida.</p>}</section>}
        {loading && <p role="status">Carregando dados atualizados...</p>}
        {screen === "dashboard" && <Dashboard user={user} reservations={reservations} resources={resources} onNavigate={setScreen} />}
        {screen === "resources" && <ResourcesScreen resources={resources} reload={(query) => loadData(query)} onReserve={reserveResource} />}
        {screen === "reservations" && <ReservationsScreen reservations={reservations} resources={resources} reload={loadData} onChanged={loadData} preselectedResource={preselectedResource} clearPreselection={() => setPreselectedResource(null)} user={user} />}
        {screen === "management" && <ManagementScreen user={user} resources={resources} users={users} reservations={reservations} reloadAll={loadData} />}
      </main>
      <footer className="app-footer"><span>© 2026 TaskFlow</span><span>Organizando espaços, conectando pessoas.</span><a href="#ajuda">Ajuda e suporte</a></footer>
    </div>
  </div>;
}
