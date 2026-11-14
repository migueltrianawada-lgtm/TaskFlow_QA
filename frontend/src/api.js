const TOKEN_KEY = "taskflow.accessToken";

export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  if (token) sessionStorage.setItem(TOKEN_KEY, token);
  else sessionStorage.removeItem(TOKEN_KEY);
}

export async function api(path, options = {}) {
  const headers = new Headers(options.headers || {});
  const token = getToken();
  if (token) headers.set("Authorization", `Bearer ${token}`);
  if (options.body && !headers.has("Content-Type")) headers.set("Content-Type", "application/json");
  const response = await fetch(`/api${path}`, { ...options, headers });
  if (!response.ok) {
    let message = "Não foi possível concluir a operação.";
    try {
      const body = await response.json();
      message = body.detail || body.message || message;
    } catch {
      // Keep the user-facing fallback for non-JSON server errors.
    }
    if (response.status === 401) {
      setToken(null);
      window.dispatchEvent(new Event("taskflow:unauthorized"));
    }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}

export function formatTipo(tipo) {
  return ({ SALA: "Sala", PROFESSOR: "Professor", MATERIAL: "Material", EQUIPAMENTO: "Equipamento" })[tipo] || tipo;
}

export function formatStatus(status) {
  return ({
    PENDENTE: "Pendente",
    AGUARDANDO_APROVACAO: "Aguardando aprovação",
    APROVADA: "Aprovada",
    RECUSADA: "Recusada",
    CANCELADA: "Cancelada",
  })[status] || status;
}
