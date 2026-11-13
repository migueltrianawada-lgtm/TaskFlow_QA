import { cleanup, fireEvent, render, screen, within } from "@testing-library/react";
import { afterEach, describe, expect, it } from "vitest";
import App from "./App";

afterEach(cleanup);

function signIn(role = "Solicitante") {
  fireEvent.change(screen.getByLabelText("E-mail"), {
    target: { value: "ana.silva@instituicao.edu.br" },
  });
  fireEvent.change(screen.getByLabelText("Senha"), {
    target: { value: "senha-local" },
  });
  fireEvent.change(screen.getByLabelText("Acessar como"), {
    target: { value: role },
  });
  fireEvent.click(screen.getByRole("button", { name: /entrar na plataforma/i }));
}

describe("TaskFlow frontend", () => {
  it("apresenta o login e informa que a autenticação é apenas demonstrativa", () => {
    render(<App />);

    expect(screen.getByRole("heading", { name: "Acesse sua conta" })).toBeInTheDocument();
    expect(screen.getByText(/não valida credenciais no servidor/i)).toBeInTheDocument();
  });

  it("valida os campos obrigatórios do login", () => {
    render(<App />);

    fireEvent.click(screen.getByRole("button", { name: /entrar na plataforma/i }));

    expect(screen.getByRole("alert")).toHaveTextContent("Informe seu e-mail e sua senha");
  });

  it("abre o painel e mostra as áreas disponíveis para um solicitante", () => {
    render(<App />);
    signIn();

    expect(screen.getByRole("heading", { name: /bom dia, ana!/i })).toBeInTheDocument();
    expect(screen.getByRole("navigation", { name: "Navegação principal" })).toHaveTextContent("Recursos");
    expect(screen.queryByRole("button", { name: "Gestão" })).not.toBeInTheDocument();
  });

  it("filtra recursos pelo texto digitado", () => {
    render(<App />);
    signIn();
    fireEvent.click(screen.getByRole("button", { name: "Recursos" }));

    fireEvent.change(screen.getByRole("textbox", { name: "Buscar recurso" }), {
      target: { value: "Auditório" },
    });

    expect(screen.getByRole("heading", { name: "Auditório Central" })).toBeInTheDocument();
    expect(screen.queryByRole("heading", { name: "Sala Inovação" })).not.toBeInTheDocument();
  });

  it("cria uma solicitação de reserva e a exibe no histórico local", () => {
    render(<App />);
    signIn();
    fireEvent.click(screen.getByRole("button", { name: "Reservas" }));
    fireEvent.click(screen.getByRole("button", { name: /nova reserva/i }));

    fireEvent.change(screen.getByLabelText("Nome da atividade"), {
      target: { value: "Aula de demonstração" },
    });
    fireEvent.change(screen.getByLabelText("Recurso"), {
      target: { value: "Sala Inovação" },
    });
    fireEvent.change(screen.getByLabelText("Data"), {
      target: { value: "2026-11-20" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Enviar solicitação" }));

    expect(screen.getByRole("status")).toHaveTextContent("Reserva adicionada");
    expect(screen.getByText("Aula de demonstração")).toBeInTheDocument();
  });

  it("mostra áreas de gestão para administrador e permite revisar aprovações como responsável", () => {
    render(<App />);
    signIn("Administrador");
    fireEvent.click(within(screen.getByRole("navigation", { name: "Navegação principal" }))
      .getByRole("button", { name: /gestão/i }));
    expect(screen.getByRole("tab", { name: "Usuários" })).toBeInTheDocument();
    expect(screen.getByRole("tab", { name: "Manutenção" })).toBeInTheDocument();
  });

  it("mostra a fila de aprovações para o perfil responsável", () => {
    render(<App />);
    signIn("Responsável");
    fireEvent.click(within(screen.getByRole("navigation", { name: "Navegação principal" }))
      .getByRole("button", { name: /gestão/i }));

    expect(screen.getByRole("heading", { name: "Solicitações pendentes" })).toBeInTheDocument();
    expect(screen.getByText("Palestra de inovação")).toBeInTheDocument();
    const approval = screen.getByText("Palestra de inovação").closest("article");
    fireEvent.click(within(approval).getByRole("button", { name: "Aprovar" }));
    expect(screen.getByRole("status")).toHaveTextContent("Solicitação aprovada");
    expect(screen.queryByText("Palestra de inovação")).not.toBeInTheDocument();
  });
});
