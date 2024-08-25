import { twMerge } from "tailwind-merge";
import { ToolButton } from "./ToolButton";
import { useAppContext } from "../contexts/useAppContext";

export const ToolsBar = () => {
  const {actions} = useAppContext()

  return (
    <div className={twMerge(
      'w-full h-[70px] bg-gray-300 border-b-2 border-gray-400',
      'flex justify-start items-center space-x-2 px-2'
    )}>
      <ToolButton iconPath="/new-file-icon.svg" label="Novo [CTRL + N]" onClick={actions.handleNew} />
      <ToolButton iconPath="/open-file-icon.svg" label="Abrir [CTRL + O]" onClick={actions.handleOpenFile} />
      <ToolButton iconPath="/save-file-icon.svg" label="Salvar [CTRL + S]" onClick={actions.handleSaveFile} />
      <ToolButton iconPath="/copy-icon.svg" label="Copiar [CTRL + C]" onClick={() => alert('Copiar')} />
      <ToolButton iconPath="/paste-icon.svg" label="Colar [CTRL + V]" onClick={() => alert('Colar')} />
      <ToolButton iconPath="/cut-icon.svg" label="Recortar [CTRL + X]" onClick={() => alert('Recortar')} />
      <ToolButton iconPath="/compile-icon.svg" label="Compilar [F7]" onClick={actions.handleCompile} />
      <ToolButton iconPath="/team-icon.svg" label="Equipe [F1]" onClick={actions.handleShowTeam} />
    </div>
  );
}
