import { twMerge } from "tailwind-merge";
import { ToolButton } from "./ToolButton";
import { useAppContext } from "../contexts/useAppContext";
import NewFileIcon from '../assets/new-file-icon.svg'
import OpenFileIcon from '../assets/open-file-icon.svg'
import SaveFileIcon from '../assets/save-file-icon.svg'
import CopyIcon from '../assets/copy-icon.svg'
import PasteIcon from '../assets/paste-icon.svg'
import CutIcon from '../assets/cut-icon.svg'
import CompileIcon from '../assets/compile-icon.svg'
import TeamIcon from '../assets/team-icon.svg'

export const ToolsBar = () => {
  const {actions} = useAppContext()

  return (
    <div className={twMerge(
      'w-full h-[70px] bg-gray-300 border-b-2 border-gray-400',
      'flex justify-start items-center space-x-2 px-2'
    )}>
      <ToolButton iconPath={NewFileIcon} label="Novo [CTRL + N]" onClick={actions.handleNew} />
      <ToolButton iconPath={OpenFileIcon} label="Abrir [CTRL + O]" onClick={actions.handleOpenFile} />
      <ToolButton iconPath={SaveFileIcon} label="Salvar [CTRL + S]" onClick={actions.handleSaveFile} />
      <ToolButton iconPath={CopyIcon} label="Copiar [CTRL + C]" onClick={actions.handleCopyToClipboard} />
      <ToolButton iconPath={PasteIcon} label="Colar [CTRL + V]" onClick={actions.handlePasteFromClipboard} />
      <ToolButton iconPath={CutIcon} label="Recortar [CTRL + X]" onClick={actions.handleCutToClipboard} />
      <ToolButton iconPath={CompileIcon} label="Compilar [F7]" onClick={actions.handleCompile} />
      <ToolButton iconPath={TeamIcon} label="Equipe [F1]" onClick={actions.handleShowTeam} />
    </div>
  );
}
