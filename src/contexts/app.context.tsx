import { createContext, PropsWithChildren, ReactNode, useCallback, useEffect, useState } from "react";

interface AppContextType {
  terminalMessage?: ReactNode;
  statusFile?: string;
  terminalHeight: number;
  setTerminalHeight: (height: number) => void;
  handleNew: () => void;
  editorText: string;
  setEditorText: (text: string) => void;
  actions: {
    handleNew: () => void;
    handleOpenFile: () => void;
    handleSaveFile: () => void;
    handleCompile: () => void;
    handleShowTeam: () => void;
  }
}

export const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppContextProvider = ({ children }: PropsWithChildren) => {
  const [terminalHeight, setTerminalHeight] = useState(100);
  const [terminalMessage, setTerminalMessage] = useState<ReactNode>(undefined);
  const [statusFile, setStatusFile] = useState<string | undefined>(undefined);
  const [editorText, setEditorText] = useState<string>('');
  const [isNewFile, setIsNewFile] = useState<boolean>(true);

  const handleNew = useCallback(() => {
    setTerminalMessage(undefined);
    setStatusFile(undefined);
    setEditorText('');
    setIsNewFile(true);
  }, [])

  const handleOpenFile = useCallback(async () => {
    const filePath = await window.electron.openFileDialog();

    if (filePath?.length) {
      const fileContent = await window.electron.readFile(filePath[0]);
      setEditorText(fileContent || '');
      setStatusFile(filePath[0]);
      setTerminalMessage('');
      setIsNewFile(false)
    }
  }, [])

  const handleSaveFile = useCallback(async () => {
    if (isNewFile) {
      const { success, filePath } = await window.electron.openSaveFileDialog(editorText);
      if (!success) return;

      setStatusFile(filePath);
      setTerminalMessage('');
      setIsNewFile(false)
    } else {
      alert('Ainda não implementado')
      setTerminalMessage('');
    }
  }, [editorText, isNewFile])

  const handleCompile = useCallback(() => {
    setTerminalMessage('Compilação de programas ainda não foi implementada.')
  }, [])

  const handleShowTeam = useCallback(() => {
    setTerminalMessage(
      <>
        - Gustavo Vergilio Poleza <br />
        - Lemuel Kauê Manske de Liz <br />
        - João Henrique Stolf Galeazzi
      </>
    )
  }, [])

  useEffect(() => {
    const shortcuts = [
      { ctrl: true, key: 'N', action: handleNew },
      { ctrl: true, key: 'O', action: handleOpenFile },
      { ctrl: true, key: 'S', action: handleSaveFile },
      { ctrl: false, key: 'F7', action: handleCompile },
      { ctrl: false, key: 'F1', action: handleShowTeam },
    ]

    const handleKeyDown = (e: KeyboardEvent) => {
      const key = e.key.toUpperCase();
      const ctrl = e.ctrlKey || e.metaKey;
      const shortcut = shortcuts.find(s => s.key === key && s.ctrl === ctrl);

      if (shortcut) {
        e.preventDefault();
        shortcut.action();
      }
    }

    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    }
  }, [handleNew, handleOpenFile, handleSaveFile, handleCompile, handleShowTeam])

  return (
    <AppContext.Provider value={{
      statusFile,
      terminalMessage,
      terminalHeight,
      setTerminalHeight,
      handleNew,
      editorText,
      setEditorText,
      actions: {
        handleNew,
        handleOpenFile,
        handleSaveFile,
        handleCompile,
        handleShowTeam,
      }
    }}>
      {children}
    </AppContext.Provider>
  );
}
