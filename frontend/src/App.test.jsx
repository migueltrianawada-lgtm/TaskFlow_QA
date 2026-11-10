import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import App from "./App";

describe("TaskFlow frontend", () => {
  it("apresenta o nome do sistema e sua finalidade", () => {
    render(<App />);

    expect(screen.getByRole("link", { name: "TaskFlow, página inicial" })).toBeInTheDocument();
    expect(
      screen.getByText(/organize salas, professores e materiais sem conflitos/i),
    ).toBeInTheDocument();
  });
});
