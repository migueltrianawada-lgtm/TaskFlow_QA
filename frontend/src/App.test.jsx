import { cleanup, fireEvent, render, screen, waitFor } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import App from "./App";

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

const resource = {
  id: 12, nome: "Sala Aurora", tipo: "SALA", localizacao: "Bloco A",
  capacidade: 30, competencia: null, quantidadeTotal: null, restrito: false, ativo: true,
};

function response(data, status = 200) {
  return { ok: status >= 200 && status < 300, status, json: async () => data };
}

function mockServer({ role = "SOLICITANTE", resources = [resource] } = {}) {
  const fetchMock = vi.fn(async (url, options = {}) => {
    if (url === "/api/auth/login") {
      return response({ accessToken: "test-token", usuario: {
        id: 1, nome: "Ana Silva", email: "ana@example.edu", perfis: [role], ativo: true,
      } });
    }
    if (url === "/api/recursos") return response(resources);
    if (url === "/api/reservas") return response([]);
    if (url === "/api/usuarios") return response([]);
    if (url === "/api/reservas" && options.method === "POST") return response({ id: 1 }, 201);
    return response({});
  });
  vi.stubGlobal("fetch", fetchMock);
  return fetchMock;
}

function fillLogin() {
  fireEvent.change(screen.getByLabelText("E-mail"), { target: { value: "ana@example.edu" } });
  fireEvent.change(screen.getByLabelText("Senha"), { target: { value: "password" } });
  fireEvent.click(screen.getByRole("button", { name: /entrar na plataforma/i }));
}

describe("TaskFlow frontend", () => {
  beforeEach(() => {
    sessionStorage.clear();
  });

  it("valida os campos obrigatórios do login", () => {
    mockServer();
    render(<App />);
    fireEvent.click(screen.getByRole("button", { name: /entrar na plataforma/i }));
    expect(screen.getByRole("alert")).toHaveTextContent("Informe seu e-mail e sua senha");
  });

  it("autentica no servidor e esconde a gestão de solicitantes", async () => {
    const fetchMock = mockServer();
    render(<App />);
    fillLogin();
    expect(await screen.findByRole("heading", { name: "Olá, Ana!" })).toBeInTheDocument();
    expect(screen.queryByRole("button", { name: "Gestão" })).not.toBeInTheDocument();
    expect(sessionStorage.getItem("taskflow.accessToken")).toBe("test-token");
    await waitFor(() => expect(fetchMock).toHaveBeenCalledWith(
      "/api/recursos",
      expect.objectContaining({ headers: expect.any(Headers) }),
    ));
    const resourceRequest = fetchMock.mock.calls.find(([url]) => url === "/api/recursos");
    expect(resourceRequest[1].headers.get("Authorization")).toBe("Bearer test-token");
  });

  it("carrega recursos do servidor e permite iniciar uma solicitação", async () => {
    mockServer();
    render(<App />);
    fillLogin();
    await screen.findByRole("heading", { name: "Olá, Ana!" });
    fireEvent.click(screen.getByRole("button", { name: "Recursos" }));
    expect(await screen.findByRole("heading", { name: "Sala Aurora" })).toBeInTheDocument();
    fireEvent.click(screen.getByRole("button", { name: /solicitar reserva/i }));
    expect(await screen.findByRole("heading", { name: "Nova solicitação" })).toBeInTheDocument();
    expect(screen.getByLabelText("Sala Aurora")).toBeChecked();
  });

  it("envia a reserva para a API sem exibir confirmação falsa", async () => {
    const fetchMock = mockServer();
    render(<App />);
    fillLogin();
    await screen.findByRole("heading", { name: "Olá, Ana!" });
    fireEvent.click(screen.getByRole("button", { name: "Reservas" }));
    fireEvent.click(screen.getByRole("button", { name: /nova reserva/i }));
    fireEvent.change(screen.getByLabelText("Nome da atividade"), { target: { value: "Aula de QA" } });
    fireEvent.click(screen.getByLabelText("Sala Aurora"));
    fireEvent.change(screen.getByLabelText("Data"), { target: { value: "2027-01-20" } });
    fireEvent.click(screen.getByRole("button", { name: "Enviar solicitação" }));
    await waitFor(() => expect(fetchMock.mock.calls.some(([url, options]) =>
      url === "/api/reservas" && options.method === "POST")).toBe(true));
    const post = fetchMock.mock.calls.find(([url, options]) => url === "/api/reservas" && options.method === "POST");
    expect(JSON.parse(post[1].body)).toMatchObject({
      finalidade: "Aula de QA", recursoIds: [12], quantidades: {},
    });
  });
});
